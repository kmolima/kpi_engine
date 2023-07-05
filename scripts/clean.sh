#!/bin/bash


docker compose down

docker rmi -f hivemq/hivemq-ce prom/prometheus km0lima/data_instrumentation_models23 km0lima/mqtt_virtual_publisher kpi_engine

rm -rf hivemq-prometheus-extension* reproduced/*
