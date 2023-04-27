#!/bin/bash


docker compose down

docker image rm -f hivemq/hivemq-ce prom/prometheus data_instrumentation publisher grafana/grafana

rm -rf publisher/ data_instrumentation/ hivemq-prometheus-extension*

./mvnw clean
