# Student Management System — Complete DevOps Pipeline

> INT332 — DevOps Virtualization and Configuration Management  
> Project Title: Design of Complete DevOps Pipeline using Docker, Maven, GitHub Actions, and Jenkins

![CI Pipeline](https://github.com/YOUR_USERNAME/devops-pipeline-project/actions/workflows/ci.yml/badge.svg)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?logo=jenkins&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?logo=springboot&logoColor=white)

---

## Problem Statement

Traditional software development relies on manual builds, testing, and deployments — causing slow delivery, inconsistent environments, and deployment failures. This project implements a complete automated DevOps pipeline that ensures fast, reliable, and secure software delivery.

---

## Tech Stack

| Tool | Purpose | Syllabus Unit |
|------|---------|--------------|
| Docker | Containerization, image building | Unit I, II |
| Docker Compose | Multi-service deployment | Unit III |
| Maven | Build automation, testing | Unit IV |
| GitHub Actions | CI pipeline | Unit V |
| Jenkins | CD pipeline | Unit VI |
| Trivy | Security scanning (bonus) | DevSecOps |

---

## Project Structure

```
devops-pipeline-project/
├── src/                          # Java Spring Boot application
├── pom.xml                       # Maven build config (Unit IV)
├── Dockerfile                    # Multi-stage Docker build (Unit II)
├── docker-compose.yml            # 3-service deployment (Unit III)
├── Jenkinsfile                   # Declarative CD pipeline (Unit VI)
├── nginx.conf                    # Reverse proxy config
└── .github/workflows/ci.yml      # GitHub Actions CI (Unit V)
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/students` | Get all students |
| GET | `/api/students/{id}` | Get student by ID |
| POST | `/api/students` | Add a new student |
| GET | `/api/students/health` | Health check |

---

## How to Run

### Option 1 — Docker Compose (recommended)
```bash
git clone https://github.com/YOUR_USERNAME/devops-pipeline-project.git
cd devops-pipeline-project
docker-compose up -d --build
```
App runs at: http://localhost:8080

### Option 2 — Maven only
```bash
mvn clean package
java -jar target/student-app-1.0.0.jar
```

### Option 3 — Docker only
```bash
docker build -t student-app:1.0.0 .
docker run -p 8080:8080 student-app:1.0.0
```

---

## Maven Lifecycle Commands

```bash
mvn validate          # Validate project structure
mvn compile           # Compile Java sources
mvn test              # Run JUnit tests via Surefire
mvn package           # Package into JAR (Shade plugin)
mvn verify            # Run all checks
mvn clean package     # Clean + full build
```

---

## Pipeline Architecture

```
Developer → GitHub Push
    → GitHub Actions CI (Unit V)
        → Maven Build & Test (Unit IV)
        → Docker Image Build (Unit II)
        → Push to Docker Hub + GHCR
        → Trivy Security Scan (DevSecOps)
    → Jenkins CD (Unit VI)
        → Checkout → Build → Test → Docker Build → Deploy
        → docker-compose up (Unit III)
        → Live Application
```

---

## Author

**Course:** INT332 — DevOps Virtualization and Configuration Management  
**Tools:** Docker · Maven · GitHub Actions · Jenkins · Trivy
