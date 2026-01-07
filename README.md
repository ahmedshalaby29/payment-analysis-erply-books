# Payment Behavior Analysis

This tool analyzes customer payment data from ERPLY Books to identify customers whose payment behavior has worsened (paid on time in 2024, but paying late in the current period).

## Prerequisites

- Java 17 or higher
- Maven

## Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/ahmedshalaby29/payment-analysis-erply-books.git
    cd payment-analysis-erply-books
    ```

2.  **Configure API Token:**
    Create a `.env` file in the root of the project and add your ERPLY Books API token:
    ```
    API_TOKEN=your_api_token_here
    ```

## Building

Compile the project using Maven:
```bash
mvn clean compile
```

## Running

Run the analysis using the `exec:java` goal:
```bash
mvn exec:java -Dexec.mainClass="com.erply.test.Main"
```

## Output

The program will output a list of customers who meet the criteria for worsened payment behavior.

## Project Structure

- `src/main/java`: Source code
- `ERPLY_solution.txt`: Single-file solution for submission
