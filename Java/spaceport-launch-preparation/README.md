# 🚀 AstraNova Spaceport: Launch & Transit Planner

A Java-based scheduling and routing system designed for the AstraNova Spaceport. This project handles operational timelines for orbital launches and optimizes internal transit routes for spaceport personnel. 

## 🧠 Core Modules & Algorithms

This project is divided into two distinct operational components:

* **🛰️ Part I: Launch Preparation Planner (Graph-based Scheduling)**
  * Computes the earliest feasible execution timeline for interdependent launch operations.
  * Satisfies complex prerequisite constraints to ensure the minimum total time required for mission readiness.
  * Parses mission parameters, execution times, and operational dependencies directly from XML files.
  
* **🚝 Part II: Spaceport Transit Network (Shortest Path & Regex)**
  * Calculates the fastest route between any two locations across the spaceport by combining walking paths and autonomous shuttle corridors.
  * Factors in a standard walking speed of 10 km/h and dynamic shuttle speeds provided by the input data.
  * Employs advanced Regular Expressions (`Pattern` and `Matcher`) to robustly parse flexible `.dat` input files, safely handling variable whitespace and formatting.
  * Outputs highly precise step-by-step navigation instructions, calculating step durations to two decimal places and rounding total travel time to the nearest minute.

## 🛠️ Tech Stack
* **Language:** Java (OpenJDK 11)
* **Key Topics:** Shortest Path Algorithms, Graph-based Scheduling, Regular Expressions (Regex)

## 📂 Architecture
The system is built using the following core classes:
* `Main.java`: Application entry point.
* `LaunchPlan.java` & `LaunchOperationsTimeline.java`: Core logic for parsing and computing the launch readiness timeline.
* `Operation.java`: Data model for individual launch procedures.
* `SpaceportTransitPlanner.java` & `SpaceportTransitNetwork.java`: Core routing logic and Regular Expression parsers.
* `ShuttleCorridor.java`, `Station.java`, `Point.java`, `Edge.java`: Transit network data structures.
* `RouteInstruction.java`: Formatter for the step-by-step transit output.

## 🚀 How to Run Locally

### Prerequisites
Ensure you have the Java Development Kit (JDK 11 or higher) installed on your system.

### Compilation
Navigate to the root directory of the project where your `.java` files are located and compile them:
```bash
javac *.java -d .

java Main <launchPlansXMLFile> <spaceportTransitDATFile>
