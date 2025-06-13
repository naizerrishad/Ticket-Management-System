# Ticket Management System

## 🎟️ Introduction

The **Ticket Management System** is a multithreaded application designed to simulate interactions between customers and vendors managing tickets in a shared pool. This project showcases core Java programming concepts including:

- Object-Oriented Programming (OOP)
- Exception Handling
- File Handling
- Multithreading and Synchronization
- GUI with JavaFX
- JSON Serialization using Gson

The backend is implemented in **pure Java**, and the frontend GUI is built with **JavaFX**. The system allows users to configure ticket settings, simulate ticket distribution, and save/load configurations using JSON.

---

## 🛠️ Setup Instructions

### • Prerequisites

- Java Development Kit (JDK) 17 or higher  
- JavaFX SDK  
- Java IDE (e.g., IntelliJ IDEA, Eclipse, or NetBeans)  
- [Gson library](https://github.com/google/gson) (for JSON serialization and deserialization)

### • How to build and run the application

1. Clone or download the project files.  
2. Import the project into your preferred Java IDE.  
3. Ensure the JavaFX SDK is properly configured in your IDE.  
4. Add the Gson library to your project:  
   - If using IntelliJ: File → Project Structure → Modules → Dependencies → Add JARs or Libraries  
   - Or include Gson using Maven/Gradle  
5. Compile and run the `TicketManagementApp` class located in the `frontend` package.

---

## 🖥️ UI Controls

- **Start**  
  Starts the system after reading configuration inputs. Initializes the customer and vendor threads and displays current settings in the status area.

- **Stop**  
  Halts the system by interrupting both threads and displays a confirmation message in the status area.

- **Save Configuration to File**  
  Saves the entered configuration (Total Tickets, Rates, etc.) to a file in JSON format using the Gson library.

- **Load Configuration from File**  
  Loads previously saved configuration settings from a JSON file and populates the input fields.

---

## ▶️ Usage Instructions

1. Fill in the text fields:
   - Total Tickets  
   - Ticket Release Rate  
   - Customer Retrieval Rate  
   - Maximum Ticket Capacity  
   - (Optional) File Name

   ⚠️ Ensure *Maximum Ticket Capacity* is less than *Total Tickets*.

2. Click **Start** to begin the simulation.  
3. Let the simulation run for a period (e.g., 10 seconds).  
4. Click **Stop** to halt the simulation.

### Saving and Loading Configurations

- To **save** a configuration:
  1. Enter a file name in the “File Name” field.
  2. Click **Save configuration to file**.

- To **load** a configuration:
  1. Enter the saved file name.
  2. Click **Load configuration from file**.

The file will be read/written in JSON format using Gson.

---

## 📂 Project Structure

