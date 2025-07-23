# Record Processor Project

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Fix the classpath if necessary.

## Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/your-repo.git
    cd your-repo
    ```

2. Install dependencies:
    ```sh
    mvn clean install
    ```

## Running the Application

### Using IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Click on File > Open and select the project directory.
3. Wait for IntelliJ to index the project and download dependencies.
4. In the Project tool window, navigate to src/main/java/com/raisin/RecordProcessor.java.
5. Right-click on the RecordProcessor class and select Run 'RecordProcessor.main()'.

### Using Maven

To run the application, use the following command:
```sh
mvn exec:java -Dexec.mainClass="com.raisin.RecordProcessor"
```

### Using JAR
```sh 
java -jar target/record-processor-1.0-SNAPSHOT.jar
```

## Testing
```sh
mvn test
```

### Using IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Click on File > Open and select the project directory.
3. Wait for IntelliJ to index the project and download dependencies.
4. In the Project tool window, navigate to src/test/java/com/raisin/RecordProcessorTest.java.
5. Right-click on the RecordProcessorTest class and select Run 'RecordProcessorTest'.

