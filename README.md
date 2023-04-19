# KPI Engine

Ocean Data Platform Observability Models for the GSSI Multi-level Model Framework for Quality Evaluation of Systems (QES).

Particularly, we are working at the levels @1 and @0 of the specified multi-level model.


This work extends previous implementations of the refered framework [GSSI's Multi-level Model Framework for Quality Evaluation of Systems](https://github.com/gssi/SmartCityModeling), being the subject a platform that receives data from diferent monitored sites at sea.

For more details please check: [Rossi, Maria Teresa, et al. "Leveraging Multi-Level Modeling for Multi-Domain Quality Assessment." 2021 ACM/IEEE International Conference on Model Driven Engineering Languages and Systems Companion (MODELS-C). IEEE, 2021.](https://ieeexplore.ieee.org/abstract/document/9643700)

## Ocean Data Platform Metamodel (The Subject in the framework)

![Ocean Data Platform Metamodel](/img/data_platform-diag.jpeg "Ecore Diagram")


## Time-bounded KPI Metamodel (The Quality Definition in the framework)
Key Performance Indicators (KPI) for the platform with a focus on monitoring data quality.

![KPI Metamodel](/img/timedKpi-diag.jpeg "Ecore Diagram")

# Test the KPI Engine

Note: Add execution permission to scripts 

```bash
chmod +x steup.sh clean.sh mvnw run_http_service.sh launch_browser.sh
```

## Run a local instance of the data platform
Dependency: [Docker Compose](https://docs.docker.com/compose/install/)

Note: If you need elevated privileges to run docker, please run the script bellow accordingly (e.g. add ``` sudo ```) 

```bash
./setup &
```
Access the Prometheus monitoring toolkit configuration panel and verify the metrics producers targets:
http://localhost:9090/targets


## Build and Execute the KPI Engine 
Dependency: [Java 11](https://www.oracle.com/java/technologies/downloads/#java11)

### HTTP Service in Action
Run the KPI Engine to translate the Models and interact with the HTTP Query API to fullfill the KPIs:

```bash
./run_http_service.sh
```


### Launch the Prometheus Browser Expression (Dashboard)
Launch the Expression Browser of the Prometheus toolkit to visualize the KPI metrics and results:

```bash
./launch_browser.sh
```

### Cleanup

Note: If you need elevated privileges to run docker, please run the script bellow accordingly (e.g. add ``` sudo ```) 

```bash
./clean.sh
```
