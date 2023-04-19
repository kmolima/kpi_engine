#!/bin/bash

if [ "$#" -eq 0 ]; then
    echo "Running KPI Engine for oceanops KPIs models"
    java -cp target/kpi_engine-0.0.1-SNAPSHOT-jar-with-dependencies.jar no.smartocean.modeling.engine.application.KpiEngine
    exit 0
elif [ "$#" -eq 1 ]; then
    echo "Running KPI Engine for model: $1"
    java -cp target/kpi_engine-0.0.1-SNAPSHOT-jar-with-dependencies.jar no.smartocean.modeling.engine.application.KpiEngine config/config.yaml $1
    exit 0
else
    echo "Illegal number of parameters"
    exit 1
fi
