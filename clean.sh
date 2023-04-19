#!/bin/bash


docker compose down

docker image rm -f hivemq-ce prometheus-ce data-validation-ce publisher-ce

rm -rf publisher/ data_instrumentation/ hivemq-prometheus-extension*

./mvnw clean
