# Test the Prototype Locally

## Step-by-Step

### Setup

```bash
git clone https://github.com/kmolima/kpi_engine.git
```

```bash
cd kpi_engine
```
After clonning this repository and changing the working directory into the cloned folder add execution permission to the scripts. 

```bash
chmod +x mvnw ./scripts/*
```

### Run the setup script
To replicate the case study setup please run the script below from the home directory of the repository.
Note: If you need elevated privileges to run docker, please run the script below accordingly (e.g. add ``` sudo ```).  


```bash
./scripts/setup.sh
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
./scripts/run_http_service.sh
```

To configure a specific KPI model file add as the first argument:

```bash
./scripts/run_http_service.sh models/test/near_factor_kpi.model
```


### Launch the Prometheus Browser Expression (Dashboard)
Launch the Expression Browser of the Prometheus toolkit to visualize the KPI metrics and results (uses the 3 data-related KPI from OceanOps):

```bash
./scripts/launch_browser.sh
```

To configure a specific KPI model file add as the first argument:

```bash
./scripts/launch_browser.sh models/test/near_factor_kpi.model
```

### Cleanup

Deletes the dependent repositories, build folder, and docker container images.

Note: If you need elevated privileges to run docker, please run the script bellow accordingly (e.g. add ``` sudo ```) 

```bash
./scripts/clean.sh
```

```bash
./mvnw clean
```
