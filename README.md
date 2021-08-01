# straming-app project

[![Java CI](https://github.com/paspao/quarkus-streaming-app/actions/workflows/build.yml/badge.svg)](https://github.com/paspao/quarkus-streaming-app/actions/workflows/build.yml)

Support project for the article https://paspaola.it/2021/05/21/QuarkusStreamingApp.html
There are two topologies:

* the first one commented at line 142 of the StreamingApp.java class
* the second one active at line 167 of the StreamingApp.java class

## Build

```shell script
./mvnw package
```

## Run

```shell script
docker compose up --build
```

* KafDrop http://localhost:9000
* Streaming app http://localhost:8080

## Stop

```shell script
docker compose down
```
