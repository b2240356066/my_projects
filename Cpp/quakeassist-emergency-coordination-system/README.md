# QuakeAssist Emergency Coordination System

A C++ simulation of an emergency coordination system designed for post-earthquake disaster management.

## Description
This project models how emergency requests are handled after a major earthquake.
Supply and rescue requests are managed in separate queues, while limited field teams are dispatched
based on overall emergency priority.

Stack-based mission tracking is used to handle overload situations and rollback operations.

## Features
- Separate queues for supply and rescue requests
- Emergency level computation across queues
- Stack-based mission assignment and rollback
- Team dispatch based on highest urgency

## Data Structures Used
- Queues
- Stacks
