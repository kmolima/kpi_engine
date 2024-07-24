# Replicate the case study setup
These instructions to replicate study results are part of the artifact submitted and accepted in MODELS'23: https://ieeexplore.ieee.org/abstract/document/10343791/

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