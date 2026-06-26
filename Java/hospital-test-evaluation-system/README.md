# 🏥 MediPlan: Intelligent Diagnostic Planner

A Java-based automated diagnostic planning system designed to optimize hospital test evaluations. The system processes a catalogue of medical tests and their dependencies to determine the most efficient execution plan. It minimizes both the patient's physical burden (number of physical sample collections) and the hospital's processing costs.

## 🧠 Core Algorithms & Concepts
This project solves complex dependency problems using graph traversal and Dynamic Programming (DP):
* **Top-Down DP (Memoization):** Used to calculate the minimum patient burden by recursively determining required physical procedures (e.g., blood or urine samples) without recomputing shared dependencies.
* **Bottom-Up DP (Tabulation):** Used to efficiently calculate the aggregated hospital cost by building a cost table for all reachable tests.
* **Topological Sorting:** Implemented via Depth First Search (DFS) to determine the correct sequential execution order of the diagnostic tests.
* **Traceback:** Combines the results of both DP approaches to output an optimized, step-by-step diagnostic execution plan.
* **XML Parsing:** Utilizes standard Java libraries (`javax.xml.parsers`) to parse the dependency graphs and target requests without external dependencies.

## 🛠️ Tech Stack
* **Language:** Java (OpenJDK 11)
* **Key Topics:** Dynamic Programming, Directed Acyclic Graphs (DAG), Memoization, Tabulation

## 📂 Architecture
* `Main.java`: Application entry point.
* `DiagnosticCatalogue.java`: Parses `diagnostic_catalogue.xml` and dynamically computes processing costs for derived medical tests.
* `MediPlanDP.java`: The core algorithmic engine handling the memoization, tabulation, topological sorting, and diagnostic plan generation.
* `DiagnosticRequest.java`: Parses the single and multiple target queries from `diagnostic_requests.xml`.

## 🚀 How to Run Locally

### Prerequisites
Ensure you have the Java Development Kit (JDK 11 or higher) installed on your system.

### Compilation
Navigate to the project directory and compile all Java source files:

```bash
javac *.java -d .

java Main <diagnostic_catalogue.xml> <diagnostic_requests.xml>
