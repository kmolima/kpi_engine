# Interpretation of results
This folder contains the results of the case study setup execution. The generated queries showcase the abstraction of constraints imposed by the query language such as long identifiers, metric types or labels.
The 2 output files refers to:
1. [**http_queries.output**](http_queries.output) - Generated PromQL queries used by the KPI Engine service to fetch runtime metrics from Prometheus Timeseries Database (TSDB). The query expressions, which all are successfully executed (semantically correct) results from the translation of the data platform and the 3 selected KPIs model instances into the TSDB HTTP API.

2. [**browser_expr.output**](browser_expr.output) - Generated URL to visualize the queries in the Prometheus Expression Browser with the incorporated PromQL query and the parametrized configurations of the dashboard for visualization. When running locally outside docker (see instructions in the [test section](https://github.com/kmolima/kpi_engine/blob/main/Test.md)), the application can launch the browser directly. Otherwise, if the desktop is not supported, it is printed on the console and must be manually copied to a browser for visualization ([reference to implementation](https://github.com/kmolima/kpi_engine/blob/748129288d7419b3884296c90adf3267e1810e93/src/no/smartocean/modeling/engine/application/KpiEngine.java#L65C1-L65C61)).

The results indicate that the Data platform is violating targets of 2 KPIs by the empty response returned from the queries. Because of the setup of the study, with replayed data from the production system, the time difference between the data collection and the arrival to the running prototype can be verified running the function "_month_" on the delay metric ID, as depicted in the figure: 

![Delay time difference.](/img/kpi1_time_in_months.png "Delay time difference (page 9, section V. B)")

Please refer to the paper for more details on the results obtained (section V).

   
