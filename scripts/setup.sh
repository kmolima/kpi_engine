#!/bin/bash

# Configure MQTT Broker Metrics Producer
if [ ! -d "hivemq-prometheus-extension/" ]; then
  echo "Setting up HiveMQ Prometheus extension"
    curl -L https://github.com/hivemq/hivemq-prometheus-extension/releases/download/4.0.8/hivemq-prometheus-extension-4.0.8.zip \
  -o ./hivemq-prometheus-extension.zip
  unzip ./hivemq-prometheus-extension.zip -d .
fi

echo "Starting Data Platform"
docker compose -f docker-compose.yml up -d
