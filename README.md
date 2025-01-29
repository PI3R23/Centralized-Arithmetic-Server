# Centralized Arithmetic Server (CAS)

## Project Description

The **Centralized Arithmetic Server (CAS)** is a Java-based server application that provides computational services to multiple clients. The server handles basic arithmetic operations (addition, subtraction, multiplication, division) and provides statistics about the operations performed. The application operates using two protocols: UDP (for service discovery) and TCP (for client communication).

## Features

1. **Service Discovery**:
   - The server listens on a UDP port for service discovery.
   - Clients send a broadcast message with the content `CAS DISCOVER`, and the server responds with `CAS FOUND`, allowing the client to obtain the server's IP address.

2. **Client Communication**:
   - The server listens on a TCP port to accept client connections.
   - Clients send requests in the format `<OPER> <ARG1> <ARG2>`, where `OPER` is one of the operations: `ADD`, `SUB`, `MUL`, `DIV`, and `ARG1` and `ARG2` are integer arguments.
   - The server performs the requested operation and returns the result or an `ERROR` message in case of invalid input (e.g., division by zero).

3. **Statistics Reporting**:
   - The server collects statistics about the number of connections, operations performed, errors, and the sum of computed values.
   - Every 10 seconds, the server prints the statistics to the console.

## System Requirements

- Java 8 (JDK 1.8)

## Test Client
A simple test client is provided in the `Client/` directory. You can use it to test the server's functionality.

## How to Run the Application?

### Run the server:
```bash
java -jar CAS.jar <port>
```

### To run the client, follow these steps:
1. Navigate to the `Client/src` directory.
2. Compile the client:
   ```bash
   javac Client.java
   ```
3. Run the Client:
   ```bash
   java Client
   ```

## Testing the Application
To test the application, you can use a client that sends requests to the server. The client should:
1. Send a broadcast message `CAS DISCOVER` to the UDP port to discover the server.
2. After receiving the `CAS FOUND` response, connect to the server using TCP.
3. Send requests in the format `<OPER> <ARG1> <ARG2>` and wait for the server's response.

#### Example: Subtraction (SUB)
Let's assume the client wants to perform a subtraction operation. The client sends the following request to the server:
```bash
SUB 10 4
```
- `SUB` is the operation (subtraction).
- `10` is the first argument (`ARG1`).
- `4` is the second argument (`ARG2`).

The server will compute the result and respond with:
```bash
6
```