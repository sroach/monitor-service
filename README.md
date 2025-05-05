# Monitor Service

A JVM-based service to monitor HTTP endpoints.

## Features

- Scheduled monitoring of HTTP endpoints
- Configurable list of URLs to monitor
- Configurable monitoring interval
- Logging of monitoring results

## Configuration

The service can be configured through the `application.yml` file:

```yaml
monitor:
  urls:
    - https://roach.gy/ping.txt
    - https://roach.gy/extension/api/ping
    - https://roach.gy/extension/actuator/health
  scheduler:
    intervalSeconds: 30
```

### Configuration Properties

- `monitor.urls`: A list of URLs to monitor
- `monitor.scheduler.intervalSeconds`: The interval between monitoring cycles in seconds (default: 30)

## Usage

1. Configure the URLs to monitor and the monitoring interval in `application.yml`
2. Start the application
3. The service will automatically start monitoring the configured URLs at the specified interval
4. Check the logs for monitoring results

## Logs

The service logs the following information:

- Start and end of each monitoring cycle
- Ping attempts for each URL
- Success or failure of each ping attempt
- Error details in case of failures

## Development

### Prerequisites

- Java 21
- Gradle (optional, if using the Gradle wrapper)

### Building

#### Using Gradle Wrapper (Recommended)

First, initialize the Gradle wrapper:

```bash
# If you have Gradle installed
gradle wrapper --gradle-version 8.7

# Or use an existing Gradle installation to generate the wrapper
gradle wrapper
```

Then build the project:

```bash
./gradlew clean build
```

#### Using Gradle

```bash
gradle clean build
```

#### Using Maven (Legacy)

```bash
mvn clean package
```

### Running

#### Using Gradle

```bash
./gradlew bootRun
```

#### Using Java directly

```bash
java -jar build/libs/service-0.0.1-SNAPSHOT.jar
```


## Endpoints

The service exposes the following endpoints:

- `/monitor/actuator/health`: Health check endpoint
- `/monitor/actuator/info`: Information about the application
- `/monitor/actuator/loggers`: Logger configuration
- `/monitor/actuator/metrics`: Application metrics
- `/monitor/actuator/beans`: Spring beans
- `/monitor/actuator/mappings`: Request mappings
- `/monitor/actuator/threaddump`: Thread dump
- `/monitor/actuator/heapdump`: Heap dump
