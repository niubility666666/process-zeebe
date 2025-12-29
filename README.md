process-zeebe

A Zeebe-based workflow engine and monitoring platform, providing process execution APIs, worker integration, and an Operate-like data importer for workflow visibility.

📌 Project Overview

process-zeebe is a workflow platform built on top of Camunda Zeebe, focusing on:

Process deployment & instance execution

Job worker integration

Workflow data importing and materialization

Process / job / variable monitoring

Lightweight alternative to Camunda Operate (backend-focused)

The project is designed to be independently deployed, Spring Boot friendly, and easy to integrate into existing systems.

🧠 Architecture Overview

This project follows the Zeebe recommended architecture:

Zeebe Broker
   ↓ (Record Stream)
Process Importer
   ↓
Materialized Storage
   ↓
Process Engine APIs


It does not reimplement BPMN semantics, but relies on Zeebe’s native execution model.

🏗️ Project Structure
process-zeebe
├── process-engine
│   ├── src/main/java/com/rt/engine
│   │   ├── StartApp.java              # Spring Boot entry point
│   │   ├── controller                 # REST APIs
│   │   ├── service                    # Zeebe client & workflow services
│   │   ├── bean
│   │   │   └── dto                    # API DTOs (process, job, form, node)
│   │   └── config                     # Zeebe / Spring configuration
│   └── src/main/resources
│       └── application.yml
│
├── process-importer
│   ├── src/main/java/com/rt/importer
│   │   ├── service
│   │   │   ├── ZeebeImportService     # Zeebe record importer
│   │   │   └── ZeebeHazelcastService  # State/cache integration
│   │   ├── repository
│   │   │   ├── ProcessRepository
│   │   │   ├── ProcessInstanceRepository
│   │   │   ├── JobRepository
│   │   │   ├── VariableRepository
│   │   │   ├── TimerRepository
│   │   │   └── IncidentRepository
│   └── src/main/resources
│       ├── application.yml
│       └── logback.xml

🚀 Core Modules
1️⃣ process-engine

Workflow execution & API layer

Responsibilities:

Deploy BPMN processes

Start process instances

Complete jobs / tasks

Provide process & job APIs

Serve workflow metadata to external systems

Key features:

Zeebe Client encapsulation

REST-based workflow control

DTO-based API contract

Spring Boot native startup

2️⃣ process-importer

Workflow data importer & materialized view layer

Responsibilities:

Subscribe to Zeebe record stream

Import process / job / variable / timer data

Persist workflow runtime data

Support monitoring & query use cases

Imported data includes:

Process definitions

Process instances

Jobs

Variables

Timers

Incidents

Messages

This module plays a similar role to Camunda Operate, focusing on data ingestion rather than UI.

🔧 Tech Stack

Java 21

Spring Boot 3.2.3

Camunda Zeebe 8.x

Hazelcast (state/cache)

Relational database (materialized views)

📦 Running the Project
1. Prerequisites

Java 21

Zeebe Broker (Standalone or Cluster)

Database (for importer persistence)

2. Start process-importer
cd process-importer
mvn spring-boot:run


This service listens to Zeebe records and builds queryable workflow data.

3. Start process-engine
cd process-engine
mvn spring-boot:run


This service exposes workflow APIs to external systems.

🔍 Supported BPMN Capabilities

All BPMN execution semantics are provided natively by Zeebe, including:

Service Task (Job Worker)

User Task

Exclusive Gateway (XOR)

Parallel Gateway (AND)

Timer Event

SubProcess

Message Events

📡 Typical Use Cases

Workflow orchestration platform

Microservice process coordination

Internal workflow middle platform

Zeebe-based process monitoring backend

Custom Operate / workflow console backend

🛣️ Future Enhancements

Embedded Zeebe runtime (local dev & CI)

REST-based workflow monitoring APIs

Web-based Operate-lite console

Elasticsearch exporter support

Multi-tenant workflow isolation

Kubernetes / Helm deployment

📄 License

Apache License 2.0

⭐ Notes

This project focuses on engineering practicality and extensibility rather than rebuilding a workflow engine.
It is intended for teams who want to build workflow products on top of Zeebe, not replace it.
