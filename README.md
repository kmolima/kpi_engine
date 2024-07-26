# System KPI Engine 

This repository contains data and instructions for the Case Study submitted for ACM/IEEE 26th International Conference on Model-Driven Engineering Languages and Systems (MODELS).
## Marine Data Observability using KPIs


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

