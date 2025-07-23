package com.raisin;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

public class RecordProcessor {

    //private static final Logger LOG = LoggerFactory.getLogger(RecordProcessor.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static final String SOURCE_A_URL = "http://localhost:7299/source/a";
    private static final String SOURCE_B_URL = "http://localhost:7299/source/b";
    private static final String SINK_A_URL = "http://localhost:7299/sink/a";

    private final ConcurrentLinkedQueue<String> sourceAQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> sourceBQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean runningSourceA = true;
    private volatile boolean runningSourceB = true;

    public static void main(String[] args) {
        RecordProcessor processor = new RecordProcessor();
        processor.start();
    }

    public void stopSourceA() {
        runningSourceA = false;
    }

    public void stopSourceB() {
        runningSourceB = false;
    }


    public void stop() {
        runningSourceA = false;
        runningSourceB = false;
        executorService.shutdown(); // Stop accepting new tasks
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Forcefully terminate tasks
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void start() {
        executorService.submit(() -> fetchRecords(SOURCE_A_URL, sourceAQueue, this::stopSourceA));
        executorService.submit(() -> fetchRecords(SOURCE_B_URL, sourceBQueue, this::stopSourceB));
        executorService.submit(this::processRecords);
    }

    private void fetchRecords(String url, ConcurrentLinkedQueue<String> queue,  Runnable stopCallback) {
        while (runningSourceA || runningSourceB) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    queue.add(line);
                    if (line.contains("\"status\": \"done\"") || line.contains("<done/>")) {
                        stopCallback.run();
                        break;
                    }
                }
                scanner.close();
            } catch (IOException e) {
                System.err.println("Error fetching record " + e.getMessage());
            }
        }
        stop();
    }

    private void processRecords() {
        ConcurrentMap<String, String> recordsA = new ConcurrentHashMap<>();
        ConcurrentMap<String, String> recordsB = new ConcurrentHashMap<>();

        while (runningSourceA || runningSourceB || !sourceAQueue.isEmpty() || !sourceBQueue.isEmpty()) {
            try {
                String recordA = sourceAQueue.poll();
                String recordB = sourceBQueue.poll();

                if (recordA != null) {
                    processRecordA(recordA, recordsA, recordsB);
                }

                if (recordB != null) {
                    processRecordB(recordB, recordsA, recordsB);
                }

            } catch (Exception e) {
                //LOG.error("Error processing records", e);
                System.err.println("Error processing records: " + e.getMessage());
            }
        }

        // Send orphaned records to the sink
        sendOrphanedRecords(recordsA, recordsB);
    }

    public void processRecordA(String record, Map<String, String> recordsA, Map<String, String> recordsB) {
        try {
            JSONObject json = new JSONObject(record);
            String id = json.getString("id");

            if (recordsB.containsKey(id)) {
                sendToSink("joined", id);
                recordsB.remove(id);
            } else {
                recordsA.put(id, record);
            }
        } catch (Exception e) {
            //LOG.error("Error processing record A", e);
            System.err.println("Error processing record A: " + e.getMessage());
            // Handle defective record
            recordsA.remove(record);
        }
    }

    public void processRecordB(String record, Map<String, String> recordsA, Map<String, String> recordsB) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(record)));
            Element element = doc.getDocumentElement();
            String id = element.getElementsByTagName("id").item(0).getAttributes().getNamedItem("value").getNodeValue();
            if (recordsA.containsKey(id)) {
                sendToSink("joined", id);
                recordsA.remove(id);
            } else {
                recordsB.put(id, record);
            }
        } catch (SAXParseException e) {
            // Handle specific XML parsing errors
            System.err.println("XML Parsing Error: " + e.getMessage());
            //LOG.error("XML Parsing Error", e);
            // Handle defective record
            recordsB.remove(record);
        } catch (Exception e) {
            // Handle other exceptions
            System.err.println("Error processing record B: " + e.getMessage());
            //LOG.error("Error processing record B", e);
            // Handle defective record
            recordsB.remove(record);
        }
    }

    public void sendToSink(String kind, String id) throws IOException {
        HttpURLConnection connection = null;
        int responseCode = 0;
        int retryCount = 0;
        int maxRetries = 3;
        int retryDelay = 2000; // 2 seconds

        while (retryCount < maxRetries) {
            try {
                connection = (HttpURLConnection) new URL(SINK_A_URL).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonInputString = String.format("{\"kind\": \"%s\", \"id\": \"%s\"}", kind, id);
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                responseCode = connection.getResponseCode();
                if (responseCode == 406 || responseCode == 408) {
                    retryCount++;
                    if (retryCount < maxRetries) {
                        Thread.sleep(retryDelay);
                    }
                } else {
                    break;
                }
            } catch (IOException | InterruptedException e) {
                //LOG.error("Error sending to sink", e);
                System.err.println("Error sending to sink: " + e.getMessage());
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new IOException("Failed to send to sink after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Thread interrupted during retry delay", ie);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        if (responseCode == 406) {
            System.err.println("Failed to send to sink after " + maxRetries + " attempts");
            throw new IOException("Failed to send to sink after " + maxRetries + " attempts");
        }
    }

    private void sendOrphanedRecords(Map<String, String> recordsA, Map<String, String> recordsB) {
        for (String id : recordsA.keySet()) {
            try {
                sendToSink("orphaned", id);
            } catch (IOException e) {
                System.err.println("Error sending orphaned record to sink: " + e.getMessage());
                //LOG.error("Error sending orphaned record to sink", e);
            }
        }

        for (String id : recordsB.keySet()) {
            try {
                sendToSink("orphaned", id);
            } catch (IOException e) {
                System.err.println("Error sending orphaned record to sink: " + e.getMessage());
                //LOG.error("Error sending orphaned record to sink", e);
            }
        }
    }
}
