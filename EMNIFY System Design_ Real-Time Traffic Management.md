# **EMNIFY System Design: Real-Time Traffic Management**

## **1\. Problem Statement**

Design a robust, scalable, and highly available system to manage mobile data traffic for a telecommunications company. The system must meet the following requirements:

* **Real-time Traffic Ingestion:** Efficiently ingest and process a high volume of data records (up to 3 million records per minute) containing user traffic information, including customer ID and SIM details.  
* **Dynamic Data Limit Enforcement:** Enforce predefined data consumption limits for each SIM in real-time. Upon reaching a limit, the system must trigger an external service to block the SIM's connectivity.  
* **Usage and Billing APIs:** Expose APIs for customers to view their historical data usage and incurred costs over a specified period. The APIs must handle approximately 1000 requests per second.  
* **Monthly Billing:** Generate a final, accurate bill for each customer at the end of every month based on their total data consumption.

The system must prioritize **consistency** and **availability** to ensure accurate billing and reliable service.

## **2\. High-Level Architecture**

The solution is architected as a decoupled, event-driven system to handle high throughput and varying loads. It is composed of three primary components: a **Data Ingestion Pipeline**, a **Real-Time Data Enforcer**, and an **API & Billing Service**.

C4Context  
    title System Context for EMNIFY Traffic Management

    Person(customer, "Customer", "Mobile network subscriber")  
    System(external\_blocking, "External Blocking Service", "Service to block/unblock SIM connectivity.")  
    System\_Ext(billing\_system, "Billing System", "External system for generating and managing invoices.")

    Boundary(traffic\_management\_system, "EMNIFY Traffic Management System") {  
        System(data\_ingestion, "Data Ingestion Pipeline", "Ingests and processes high-volume traffic records.")  
        System(data\_enforcer, "Real-Time Data Enforcer", "Monitors data usage and enforces limits.")  
        System(api\_service, "API & Billing Service", "Serves API requests and performs monthly billing.")  
    }

    Rel(customer, api\_service, "Requests usage data and cost via API")  
    Rel(data\_ingestion, data\_enforcer, "Pushes real-time usage data to")  
    Rel(data\_enforcer, external\_blocking, "Sends block request to", "Asynchronously via a message queue")  
    Rel(api\_service, data\_ingestion, "Queries historical data from", "Read-only access")  
    Rel(api\_service, billing\_system, "Sends billing data to", "End-of-month batch process")

## **3\. Component Breakdown**

### **a. Data Ingestion & Processing Pipeline**

This pipeline is the entry point for all traffic data, designed to handle massive scale.

* **Message Queue (Apache Kafka):** Serves as a buffer to absorb traffic spikes. Producers send raw traffic records to Kafka topics.  
* **Stream Processing (Apache Flink/Spark Streaming):** A cluster of workers consumes data from Kafka. It performs real-time aggregation of data usage per SIM and customer, normalizing all traffic units to a consistent format (bytes).  
* **Data Stores:**  
  * **Real-Time Data Store (Redis/Cassandra):** A fast, in-memory or distributed key-value store to maintain a running total of data consumed per SIM. This is the **hot path** for limit enforcement.  
  * **Historical Data Store (Apache Druid/ClickHouse):** An analytical data store optimized for time-series queries. It stores all raw and aggregated traffic data for historical analysis, usage APIs, and billing. This is the **cold path**.

### **b. Real-Time Data Enforcer**

This component is critical for immediate action upon a limit breach.

* **Logic:** A dedicated stream processing job continuously monitors the real-time data store. It fetches the data limit for a SIM and compares it to the current usage.  
* **Trigger Mechanism:** When a limit is reached, a message is published to a dedicated queue. A worker then consumes this message to make an asynchronous, idempotent API call to the external service responsible for blocking the SIM. This prevents blocking the main data pipeline and provides a robust retry mechanism.

### **c. API & Billing Service**

This layer handles all customer interactions and financial operations.

* **API Gateway (NGINX/AWS API Gateway):** Provides a single entry point for all API requests. It handles load balancing, rate limiting, and authentication.  
* **Usage API Microservice:** A stateless service that queries the historical data store to retrieve usage data for a given customer and time range. It calculates the cost based on the total bytes consumed and predefined rates.  
* **Billing Service (Scheduled Process):** A separate, batch-oriented service that runs at month-end. It queries the historical data store for all customers, calculates the final bill based on their total consumption for the billing period, and sends the data to the external billing system.

The system's modular and decoupled design ensures that components can scale independently, providing high availability and fault tolerance.