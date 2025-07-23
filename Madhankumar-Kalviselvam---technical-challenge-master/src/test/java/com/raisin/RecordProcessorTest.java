package com.raisin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RecordProcessorTest {

    private RecordProcessor recordProcessor;
    private ConcurrentMap<String, String> recordsA;
    private ConcurrentMap<String, String> recordsB;

    @BeforeEach
    public void setUp() {
        recordProcessor = new RecordProcessor();
        recordsA = new ConcurrentHashMap<>();
        recordsB = new ConcurrentHashMap<>();
    }

    @Test
    public void testProcessRecordA() throws IOException {
        RecordProcessor spyProcessor = Mockito.spy(recordProcessor);
        doNothing().when(spyProcessor).sendToSink(anyString(), anyString());

        String record = "{\"id\": \"123\", \"data\": \"test\"}";
        recordsB.put("123", "{\"id\": \"123\", \"data\": \"testB\"}");

        spyProcessor.processRecordA(record, recordsA, recordsB);

        assertFalse(recordsB.containsKey("123"));
        assertTrue(recordsA.isEmpty());
        verify(spyProcessor, times(1)).sendToSink("joined", "123");
    }

    @Test
    public void testProcessRecordA_MissingId() {
        String record = "{\"data\": \"test\"}";

        recordProcessor.processRecordA(record, recordsA, recordsB);

        assertTrue(recordsA.isEmpty());
    }

    @Test
    public void testProcessRecordB() throws IOException {
        RecordProcessor spyProcessor = Mockito.spy(recordProcessor);
        doNothing().when(spyProcessor).sendToSink(anyString(), anyString());

        String record = "<record><id value=\"123\"/></record>";
        recordsA.put("123", "{\"id\": \"123\", \"data\": \"testA\"}");

        spyProcessor.processRecordB(record, recordsA, recordsB);

        assertFalse(recordsA.containsKey("123"));
        assertTrue(recordsB.isEmpty());
        verify(spyProcessor, times(1)).sendToSink("joined", "123");
    }

    @Test
    public void testProcessRecordB_MalformedXML() {
        String record = "<record><id value=\"123\"></record>";

        recordProcessor.processRecordB(record, recordsA, recordsB);

        assertTrue(recordsB.isEmpty());
    }

    @Test
    public void testSendToSink() throws IOException {
        RecordProcessor spyProcessor = Mockito.spy(recordProcessor);
        doNothing().when(spyProcessor).sendToSink(anyString(), anyString());

        spyProcessor.sendToSink("testKind", "123");

        verify(spyProcessor, times(1)).sendToSink("testKind", "123");
    }
}
