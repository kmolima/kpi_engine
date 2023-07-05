import sys
import os

#if len(sys.argv) < 2:
#    print("Provide entrypoint option as argument when running the application.\nOptions: http_service or browser")

#argument = sys.argv[1]
rest = ""
if len(sys.argv) > 1:
    rest = sys.argv[1:]

arguments=" ".join(rest)

#if argument == "http_service":
print("Interacting with Prometheus TSBD via HTTP")
os.system("java -cp /usr/local/lib/kpi.jar no.smartocean.modeling.engine.application.KpiService /etc/kpi_engine/config.yaml "+arguments+" > /home/kpi_engine/reproduced/http_queries.output")
#elif argument == "browser":
print("Parametrizing Prometheus expression browser for KPI visualization")
os.system("java -cp /usr/local/lib/kpi.jar no.smartocean.modeling.engine.application.KpiEngine /etc/kpi_engine/config.yaml "+arguments+" > /home/kpi_engine/reproduced/browser_expr.output")
print("Execution results for the semantic translator copied to the reproduced folder.")
