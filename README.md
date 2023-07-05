# Marine Data Observability using KPIs
This is the repository for the Case Study submitted for ACM/IEEE 26th International Conference on Model-Driven Engineering Languages and Systems (MODELS).


## Overview

Ocean data is crucial for climate forecast, ocean state determination, and for the industry to detect and prevent incidents such as oil spills, structural integrity faults, and fish welfare degradation. Many challenges can impact the quality of marine data at different points of data delivery pipelines: from acquisition and transmission at the Internet-of-Underwater-Things (IoUT) level up to management and sharing. We propose a model-driven solution for marine data quality assessment in a platform using Key Performance Indicators (KPIs) to identify violations of established objectives and help optimize the usage of data generated during the sensor battery lifetime. The solution work builds on top of previous implementations of the multi-level model framework [1] demonstrating its generalizability. 

[1] [Rossi, Maria Teresa, et al. "Leveraging Multi-Level Modeling for Multi-Domain Quality Assessment." 2021 ACM/IEEE International Conference on Model Driven Engineering Languages and Systems Companion (MODELS-C). IEEE, 2021](https://ieeexplore.ieee.org/abstract/document/9643700) 

This instantiation aims to enable observability of software systems, using the multi-level model framework as a semantic translator between the platform and the platform-defined KPIs, and the [Prometheus](https://prometheus.io/) monitoring toolkit where the platform-measured metrics are stored.

This repository contains the following components of the overall solution:
* Quality Evaluation System (QES) that receives as input a subject and a quality definition model
* Query Engine that interfaces with the metrics monitoring toolkit.


The remaining components part of the contribution, namely the data validation service of the platform and a data provider are available in:
* [Data Validation Repository (including the metrics producer component)](https://github.com/kmolima/data_instrumentation)
* [MQTT Data Publisher Repository](https://github.com/kmolima/data_instrumentation)
Those are used via the published container images in: [https://hub.docker.com/repositories/km0lima](https://hub.docker.com/repositories/km0lima)

All the prototype components (data platform + observability subsystem) can be set up locally using Docker Compose to replicate the prototype used in the case study setup. Instructions are provided below in the [setup section](https://github.com/kmolima/kpi_engine/edit/main/README.md#reproduce-case-study-setup).

The results reported in the paper from the Query Engine execution can be found [under the results folder](results/).

Instructions on how to test the execution of the prototype can be found in the [test section](https://github.com/kmolima/kpi_engine/blob/main/README.md#test-the-query-engine) below.


# QES Engine
This component concerns the instantiation of the MLM framework for system observability purposes using KPIs.

Particularly, we are working at the levels @1 and @0 of the specified multi-level model. The models at level @1, namely the Ocean Data Platform model and the time bounded KPI model are used in input for the QES engine which interacts with the Query service. The models overview are showned below.


## Ocean Data Platform Metamodel (The Subject in the framework)

![Ocean Data Platform Metamodel](/img/data_platform-diag.jpeg "Ecore Diagram")


## Time-bounded KPI Metamodel (The Quality Definition in the framework)
Key Performance Indicators (KPI) for the platform measured runtime metrics.

Models instances with a focus on monitoring marine data quality are available [under the models folder](models/).

![KPI Metamodel](/img/timedKpi-diag.jpeg "Ecore Diagram")

# Reproduce case study setup

## Run a local instance of the data platform
Dependency: [Docker Compose](https://docs.docker.com/compose/install/)

For the messasing service we are using the [community edition of the HiveMQ MQTT Broker](https://github.com/hivemq/hivemq-community-edition) and its metrics producer extension. 


Note: If you need elevated privileges to run docker, please run the script bellow accordingly (e.g. add ``` sudo ```).  
Make sure the scripts are ran from the home directory of the repository.

```bash
./scripts/reproduce.sh
```
Access the Prometheus monitoring toolkit configuration panel and verify the metrics producers targets:
http://localhost:9090/targets.

In general the targets will be in *UP* State around 1 minute after building the docker contatiners.

## Interpretation of results
The results of the KPI Engine execution are stored under the [reproduced folder](reproduced/). After the execution 2 output files must be listed under that directory.
1. One with the generated PromQL queries used by the KPI Engine service to fetch runtime metrics from Prometheus Timeseries Database (TSDB).
2. Another with the URL to visualize the queries in the Prometheus Expression Browser. When running locally outside docker (see intructions below in the [test section](https://github.com/kmolima/kpi_engine#run-a-local-instance-of-the-data-platform)), the application can launch the browser directly. Otherwise, if desktop is not supported, it is printed on the console ([reference to implementation](https://github.com/kmolima/kpi_engine/blob/748129288d7419b3884296c90adf3267e1810e93/src/no/smartocean/modeling/engine/application/KpiEngine.java#L65C1-L65C61)).
  

# Test the Prototype
After clonning this repository and changing the working directory into the cloned folder.

Note: Add execution permission to the scripts 

```bash
chmod +x setup.sh clean.sh mvnw run_http_service.sh launch_browser.sh
```

## Build and Execute the Query Engine Locally
Dependency: [Java 11](https://www.oracle.com/java/technologies/downloads/#java11)

### Build with Maven Wrapper

```bash
./mvnw clean package
```

### HTTP Service in Action
Run the KPI Engine to translate the Models and interact with the HTTP Query API to fullfill the KPIs (uses the 3 data-related KPI from OceanOps):

```bash
./run_http_service.sh
```

To configure a specific KPI model file add as the first argument:

```bash
./run_http_service.sh models/test/near_factor_kpi.model
```


### Launch the Prometheus Browser Expression (Dashboard)
Launch the Expression Browser of the Prometheus toolkit to visualize the KPI metrics and results (uses the 3 data-related KPI from OceanOps):

```bash
./launch_browser.sh
```

To configure a specific KPI model file add as the first argument:

```bash
./launch_browser.sh models/test/near_factor_kpi.model
```

### Cleanup

Deletes the dependent repositories, build folder, and docker container images.

Note: If you need elevated privileges to run docker, please run the script bellow accordingly (e.g. add ``` sudo ```) 

```bash
./clean.sh
```
