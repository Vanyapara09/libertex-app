# Getting Started

## Overview

This readme describes how to start app on local machine

### App setup

Run command to build image:

```shell script
docker-compose build
```

### Run

Start app:
```shell script
docker-compose up -d
```

Stop all services:
```shell script
docker-compose down 
```

Rebuild image and run:
```shell script
docker-compose up --build
```

### Additional information
During application startup, the script [data.sql](./src/main/resources/data.sql) creates a default account in the database.
Go to page [http://localhost:8080/api/v1/accounts](http://localhost:8080/api/v1/accounts/1) to get information about account.

