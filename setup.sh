#!/bin/bash

img1=$( sudo docker images -q publisher )

if [[ -n "$img1" ]]; then
  echo 'Publisher container image exists'
else
  echo 'No publisher container image'
fi

img2=$( sudo docker images -q data_instrumentation )

if [[ -n "$img2" ]]; then
  echo 'Data validation container image exists'
else
  echo 'No data validation container image'
fi

# setup data producer
if [ ! -d "publisher/" ]; then
  echo "Setting up publisher"
  git clone https://github.com/kmolima/mqtt_clients.git publisher/
  cd publisher
  sudo docker build -t publisher .
  cd -
fi

# setup data validation
if [ ! -d "data_instrumentation/" ] && [ ! -n "$img2" ] ; then
  echo "Setting up data validation"
  git clone https://github.com/kmolima/data_instrumentation.git data_instrumentation/
  cd data_instrumentation/
  sudo docker build -t data_instrumentation .
  cd -
fi

# setup prometheus broker extension
if [ ! -d "hivemq-prometheus-extension/" ]; then
  echo "Setting up HiveMQ Prometheus extension"
    curl -L https://github.com/hivemq/hivemq-prometheus-extension/releases/download/4.0.8/hivemq-prometheus-extension-4.0.8.zip \
  -o ./hivemq-prometheus-extension.zip
  unzip ./hivemq-prometheus-extension.zip -d .
fi

# Remove unecessary folders
rm -rf data_instrumentation/ hivemq-prometheus-extension.zip

# run docker compose
docker compose up
