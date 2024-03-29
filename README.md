# Marine Data Observability using KPIs
This is the repository for the Case Study submitted for ACM/IEEE 26th International Conference on Model-Driven Engineering Languages and Systems (MODELS).



[![DOI](https://zenodo.org/badge/618756916.svg)](https://zenodo.org/badge/latestdoi/618756916)


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

All the prototype components (data platform + observability subsystem) can be set up locally using Docker Compose to replicate the prototype used in the case study setup. 
Besides the implemented components, there is also a messaging service that is using the [community edition of the HiveMQ MQTT Broker](https://github.com/hivemq/hivemq-community-edition) and its metrics producer [extension for Prometheus](https://www.hivemq.com/extension/prometheus-extension/). Lastly, Prometheus is also part of the components of the prototype.

Instructions to reproduce the case study setup are provided below in the [setup section](https://github.com/kmolima/kpi_engine/tree/main#replicate-the-case-study-setup).

The results reported in the paper from the Query Engine execution can be found [under the results folder](results/).

Instructions on how to test the execution of the prototype can be found in the [test section](https://github.com/kmolima/kpi_engine/blob/main/Test.md).


# QES Engine
This component concerns the instantiation of the MLM framework for system observability purposes using KPIs.

Particularly, we are working at the levels @1 and @0 of the specified multi-level model. The models at level @1, namely the Ocean Data Platform model and the time bounded KPI model are used in input for the QES engine which interacts with the Query service. The models overview are showed below.


## Ocean Data Platform Metamodel (The Subject in the framework)

![Ocean Data Platform Metamodel](/img/data_platform-diag.jpeg "Ecore Diagram")


## Time-bounded KPI Metamodel (The Quality Definition in the framework)
Key Performance Indicators (KPI) for the platform measured runtime metrics.

Models instances with a focus on monitoring marine data quality are available [under the models folder](models/).

![KPI Metamodel](/img/timedkpi_diagram.png "Ecore Diagram")

# Replicate the case study setup
To replicate the case study please follow the steps described below. Check the [REQUIREMENTS](REQUIREMENTS) for more details on the execution environments and OS.

A video tutorial following the steps described below is available in: https://youtu.be/JM3qUPPS0fQ

## Instructions
Clone this repository and work in the home directory of the repository.

```bash
git clone https://github.com/kmolima/kpi_engine.git
```

```bash
cd kpi_engine
```

**Make sure the scripts are run from the home directory of the repository.**

Note: Add execution permission to the scripts 

```bash
chmod +x ./scripts/replicate.sh ./scripts/clean.sh 
```

## Run a local instance of the data platform
Dependency: [Docker](https://docs.docker.com/engine/install/)

### Run the setup script
To replicate the case study setup please run the script below from the home directory of the repository.
Note: If you need elevated privileges to run docker, please run the script below accordingly (e.g. add ``` sudo ```).  


```bash
./scripts/replicate.sh
```
Access the Prometheus monitoring toolkit configuration panel and verify the metrics producers' targets:
http://localhost:9090/targets.


In general, the targets will be in *UP* State around 1 minute after building the docker containers.

To verify the runtime metrics being produced by the prototype execution access the following link:
http://localhost:9090/graph?g0.expr=no_smartocean_data_ingestion_arrival_bucket&g0.tab=0&g0.stacked=0&g0.show_exemplars=0&g0.range_input=1h

For the newly developed metrics search for the ones with the prefix: "_no_smartocean__"

![Example of runtime metrics (Figure 7 a)](/img/running_system_zoom.png "Example of runtime metrics")


The script then launches the KPI Engine to perform the semantic translation based on the [KPIs and the Data Platform model instances](models/), being also responsible for the interaction with the Prometheus toolkit.

### Instructions to run on Windows
After cloning the repository, download the [metrics producer extension for HiveMQ](https://github.com/hivemq/hivemq-prometheus-extension/releases/download/4.0.8/hivemq-prometheus-extension-4.0.8.zip). Uncompress the folder into the cloned repository folder. Create an additional folder named "reproduced".

Run the data platforms prototype on the terminal:

```bash
docker compose -f docker-compose.yml up -d
```

Build the docker image for the KPI Engine:

```bash
docker build -t kpi_engine .
```

Access the Prometheus monitoring toolkit configuration panel and verify the metrics producers' targets:
http://localhost:9090/targets.


Wait a few minutes for the runtime metrics production from the data platform execution and then run the KPI Engine:

```bash
docker run --network="host" -v reproduced:/home/kpi_engine/reproduced -it kpi_engine
```

## Interpretation of results
The results of the KPI Engine execution are stored under the [reproduced folder](reproduced/). After the execution 2 output files must be listed under that directory.
1. **http_queries.output** - One with the generated PromQL queries used by the KPI Engine service to fetch runtime metrics from Prometheus Timeseries Database (TSDB).
There are differences in the output that refers to the time windows parameters of the queries because of the difference between the time when the paper results were executed and the replicated experiments and the environment where they were executed (outside docker). The query expressions, which all are successfully executed (semantically correct) are the same as the ones presented in the results section and folder of the archive.

To check the difference between both outputs you can run the following command:

```bash
diff results/http_queries.output reproduced/http_queries.output
```

2. **browser_expr.output** - Another is with the URL to visualize the queries in the Prometheus Expression Browser. When running locally outside docker (see instructions in the [test section](https://github.com/kmolima/kpi_engine/blob/main/Test.md)), the application can launch the browser directly. Otherwise, if the desktop is not supported, it is printed on the console and must be manually copied to a browser for visualization ([reference to implementation](https://github.com/kmolima/kpi_engine/blob/748129288d7419b3884296c90adf3267e1810e93/src/no/smartocean/modeling/engine/application/KpiEngine.java#L65C1-L65C61)). This will be the case when running from docker and if compared both outputs, the difference will reside precisely on this aspect.

To check the difference between both outputs you can run the following command:

```bash
diff results/browser_expr.output reproduced/browser_expr.output
```
An example of such a difference is depicted in the image below:

![Example of a generated URL](/img/URL.png "Example of a generated URL")


Please refer to the [README](results/Readme.md) in the results folder for more details.
   
## Cleanup

Deletes the dependent folders and docker container images.

Note: If you need elevated privileges to run docker, please run the script below accordingly (e.g. add ``` sudo ```) 

```bash
./scripts/clean.sh
```
