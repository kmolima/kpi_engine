#!/bin/bash

if [ ! -d "reproduced/" ]; then
  echo "Creating reproduced folder"
  mkdir ./reproduced
else
  echo 'Reproduced folder already exists'
fi

# Configure MQTT Broker Metrics Producer
if [ ! -d "hivemq-prometheus-extension/" ]; then
  echo "Setting up HiveMQ Prometheus extension"
    curl -L https://github.com/hivemq/hivemq-prometheus-extension/releases/download/4.0.8/hivemq-prometheus-extension-4.0.8.zip \
  -o ./hivemq-prometheus-extension.zip
  unzip ./hivemq-prometheus-extension.zip -d .
fi

echo "Starting Data Platform"
docker compose -f docker-compose.yml up -d

docker build -t kpi_engine .

img1=$( docker images -q km0lima/mqtt_virtual_publisher )

if [[ -n "$img1" ]]; then
  echo 'Publisher container image exists'
else
  echo 'No publisher container image'
fi

img2=$( docker images -q km0lima/data_instrumentation_models23 )

if [[ -n "$img2" ]]; then
  echo 'Data validation container image exists'
else
  echo 'No data validation container image'
fi

img3=$( docker images -q kpi_engine )

if [[ -n "$img2" ]]; then
  echo 'KPI Engine container image exists'
else
  echo 'No KPI Engine container image'
fi

echo "Going to sleep to get some metrics from the platform execution"

sleep 30

echo "Running the KPI Engine"

p=$(readlink -f ./reproduced)

docker run --network="host" -v $p:/home/kpi_engine/reproduced -it kpi_engine
