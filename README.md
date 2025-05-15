# First Helidon Project

Helidon MP application that uses JPA with an in-memory H2 database.

## Project Overview

This is a simple Helidon MP project that demonstrates the key features of the Helidon microservices framework. The application implements RESTful endpoints and showcases configuration, health checks, metrics, and more.

## Getting Started

### Prerequisites
- JDK 21+
- Maven 3.8+
- Docker (optional, for containerization)

### Building the Application

```bash
mvn clean package
```

The command above creates an executable jar file and a Docker image.

### Running the Application

```bash
java -jar target/firsthelidon.jar
```

The application will be available at http://localhost:8080.

## Features

This application demonstrates:

- REST endpoints using JAX-RS
- Configuration using MicroProfile Config
- Health checks using MicroProfile Health
- Metrics using MicroProfile Metrics
- OpenAPI documentation

## API Documentation

When the application is running, access the OpenAPI documentation at:
- http://localhost:8080/openapi (YAML format)
- http://localhost:8080/openapi-ui (Swagger UI)

## Testing

Run the tests using:

```bash
mvn test
```

## Exercise the application

Basic:
```
curl -X GET http://localhost:8080/simple-greet
Hello World!
```

JSON:
```
curl -X GET http://localhost:8080/greet
{"message":"Hello World!"}

curl -X GET http://localhost:8080/greet/Joe
{"message":"Hello Joe!"}

curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8080/greet/greeting

curl -X GET http://localhost:8080/greet/Jose
{"message":"Hola Jose!"}
```

```
curl -X GET http://localhost:8080/pokemon
[{"id":1,"type":12,"name":"Bulbasaur"}, ...]

curl -X GET http://localhost:8080/type
[{"id":1,"name":"Normal"}, ...]

curl -H "Content-Type: application/json" --request POST --data '{"id":100, "type":1, "name":"Test"}' http://localhost:8080/pokemon
```

## Try health

```
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...
```

## Try metrics

```
# Prometheus Format
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .
```

## Docker

### Building the Docker Image

The Docker image is built automatically during the Maven package phase.

### Running the Docker Container

```bash
docker run --rm -p 8080:8080 firsthelidon:latest
```

## Run the application in Kubernetes

If you don’t have access to a Kubernetes cluster, you can [install one](https://helidon.io/docs/latest/#/about/kubernetes) on your desktop.

### Verify connectivity to cluster

```
kubectl cluster-info                        # Verify which cluster
kubectl get pods                            # Verify connectivity to cluster
```

### Deploy the application to Kubernetes

```
kubectl create -f app.yaml                              # Deploy application
kubectl get pods                                        # Wait for quickstart pod to be RUNNING
kubectl get service  firsthelidon                     # Get service info
kubectl port-forward service/firsthelidon 8081:8080   # Forward service port to 8081
```

You can now exercise the application as you did before but use the port number 8081.

After you’re done, cleanup.

```
kubectl delete -f app.yaml
```

## Building a Native Image

The generation of native binaries requires an installation of GraalVM 22.1.0+.

You can build a native binary using Maven as follows:

```
mvn -Pnative-image install -DskipTests
```

The generation of the executable binary may take a few minutes to complete depending on
your hardware and operating system. When completed, the executable file will be available
under the `target` directory and be named after the artifact ID you have chosen during the
project generation phase.

## Building a Custom Runtime Image

Build the custom runtime image using the jlink image profile:

```
mvn package -Pjlink-image
```

This uses the helidon-maven-plugin to perform the custom image generation.
After the build completes it will report some statistics about the build including the reduction in image size.

The target/firsthelidon-jri directory is a self contained custom image of your application. It contains your application,
its runtime dependencies and the JDK modules it depends on. You can start your application using the provide start script:

```
./target/firsthelidon-jri/bin/start
```

Class Data Sharing (CDS) Archive
Also included in the custom image is a Class Data Sharing (CDS) archive that improves your application’s startup
performance and in-memory footprint. You can learn more about Class Data Sharing in the JDK documentation.

The CDS archive increases your image size to get these performance optimizations. It can be of significant size (tens of MB).
The size of the CDS archive is reported at the end of the build output.

If you’d rather have a smaller image size (with a slightly increased startup time) you can skip the creation of the CDS
archive by executing your build like this:

```
mvn package -Pjlink-image -Djlink.image.addClassDataSharingArchive=false
```

For more information on available configuration options see the helidon-maven-plugin documentation.

## Project Structure

```
src
├── main
│   ├── java               # Java source files
│   │   └── com/example
│   │       └── rest       # REST resources
│   └── resources
│       ├── META-INF       # Configuration files
│       └── WEB            # Web resources
└── test
    └── java               # Test source files
```

## Contributing

Please feel free to contribute to this project by opening issues or submitting pull requests.

## License

This project is licensed under the Apache License 2.0.
`