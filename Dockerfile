# Docker multi-stage build: https://docs.docker.com/build/building/multi-stage/
#
# Build stage
#
FROM maven:3.9.1-eclipse-temurin-11-alpine AS build
MAINTAINER keila

COPY src /home/kpi_engine/src
COPY pom.xml /home/kpi_engine
RUN mvn -f /home/kpi_engine/pom.xml clean package


#
# Package stage
#
FROM eclipse-temurin:11-alpine

# Install python3 on alpine
RUN apk add --no-cache python3

# Build KPI Engine - use docker.yaml to run from docker compose - use --no-cache flag to build
COPY --from=build /home/kpi_engine/target/kpi_engine-0.0.1-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/kpi.jar
COPY config/config.yaml /etc/kpi_engine/config.yaml
COPY models /home/kpi_engine/models
COPY src/no /home/kpi_engine/src/no
COPY scripts/entrypoint.py /home/kpi_engine/entrypoint.py
RUN mkdir /home/kpi_engine/reproduced
RUN touch /home/kpi_engine/reproduced/http_queries.output
RUN touch /home/kpi_engine/reproduced/browser_expr.output

# Execution
WORKDIR /home/kpi_engine
ENTRYPOINT ["python3","entrypoint.py"]
CMD ["models/oceanops_kpis.model"]  # overwritten by arguments passed to docker execution
