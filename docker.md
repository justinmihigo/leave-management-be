# Docker Guide for Leave Management System

## Project Overview

The Leave Management System is a Spring Boot application designed to manage employee leave requests. It uses MongoDB as the database and provides RESTful APIs for user and leave management. The application is containerized using Docker for easy deployment.

## Prerequisites

Before running the application in a Docker container, ensure the following are installed on your system:

1. [Docker](https://www.docker.com/) (version 20.10 or later)

## Steps to Run the Docker Container

### 1. Pull the Docker Image
The pre-built Docker image for the Leave Management System is available on Docker Hub. Pull the image using the following command:

```bash
docker pull justinmihigo/leave_management
```

### 2. Run the Docker Container
Run the Docker container using the pulled image:

```bash
docker run -p 8080:8080 justinmihigo/leave_management
```

### 3. Access the Application
Once the container is running, the application will be accessible at:

```
http://localhost:8080
```

## Notes

- The application exposes port `8080` in the container, which is mapped to port `8080` on the host machine.
- Ensure MongoDB is running and accessible as per the configuration in `application.properties`.

## Troubleshooting

- If the application fails to start, check the logs using:

```bash
docker logs <container_id>
```

- Verify that the MongoDB connection string in `application.properties` is correct and the database is reachable.

## Cleaning Up

To stop and remove the container, use the following commands:

```bash
docker ps
docker stop <container_id>
docker rm <container_id>
```