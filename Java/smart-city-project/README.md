# 🏙️ The Aetheria Project: Smart City Architect

A Java-based network architecture system designed to manage and optimize the infrastructure of Aetheria, a fully automated smart city. This project implements advanced Graph and Directed Acyclic Graph (DAG) algorithms to solve real-world urban planning and system security challenges.

## 🧠 Core Algorithms & Features

The system is divided into four critical operational modules, each utilizing specific graph theory concepts:

* **⚡ Part A: The Power Grid Dilemma (Minimum Spanning Tree)**
  * **Objective:** Connect all 13 city districts with the minimum amount of high-voltage cabling.
  * **Algorithm:** Utilizes Minimum Spanning Tree (MST) logic on an undirected graph to find the most cost-efficient cabling route while ensuring no district is left isolated.

* **⚙️ Part B: Master Boot Sequence (Topological Sorting & Cycle Detection)**
  * **Objective:** Establish a safe, sequential boot-up order for critical city systems (Water, Security, Data Center, etc.) based on their dependencies.
  * **Algorithm:** Implements Topological Sorting on a Directed Acyclic Graph (DAG). Includes robust **Cycle Detection** to identify circular dependencies and prevent catastrophic system deadlocks.

* **📡 Part C: Secure Communication Hubs (Strongly Connected Components)**
  * **Objective:** Identify "High-Interaction Zones" within the city's one-way fiber optic network where stations can communicate bidirectionally.
  * **Algorithm:** Computes Strongly Connected Components (SCCs) to group nodes that have mutually reachable network paths.

* **☣️ Part D: Bio-Leak Containment Protocol (Breadth-First Search)**
  * **Objective:** Trace the potential spread of a bio-leak or digital virus from a source station and determine evacuation priorities based on distance.
  * **Algorithm:** Executes Breadth-First Search (BFS) on an adjacency-list bag structure to map reachable stations and calculate exact hop-distances (evacuation layers).

## 🛠️ Tech Stack
* **Language:** Java (OpenJDK 11)
* **Data Structures:** Adjacency-List Graphs (Bag-based), Directed Graphs (Digraphs)
* **Data Parsing:** Java standard `javax.xml.parsers` for interpreting city blueprints

## 📂 Architecture
* `AetheriaCity.java`: Application entry point and master controller.
* `PowerGrid.java`: MST logic for cabling optimization.
* `BootSequence.java`: Dependency resolution, topological sorting, and deadlock detection.
* `CommHubs.java`: SCC discovery for network zoning.
* `BioLeakContainment.java`: BFS pathfinding and distance mapping.
* *Core API:* `Graph.java`, `Digraph.java`, `Edge.java`, `Bag.java`

## 🚀 How to Run Locally

### Prerequisites
Ensure you have the Java Development Kit (JDK 11 or higher) installed on your system.

### Compilation
Navigate to the root directory of the project where your `.java` files are located and compile them:
```bash
javac *.java -d .

java AetheriaCity
