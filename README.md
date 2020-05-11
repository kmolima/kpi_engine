# SmartCityModeling
In a Smart City context, the Smart Governance performs many decision-making processes through the management of Key Performance Indicators (KPIs), reflecting the degree of smartness and sustainability of smart cities.
In this work we propose an approach to support KPIs assessment over smart cities. In particular, the proposed approach provides that, given a Smart City Candidate, its corresponding [Smart City Model](https://github.com/gssi/SmartCityModeling/blob/master/ecmfa.smartcity/model/mycity.model) and the [KPI Model](https://github.com/gssi/SmartCityModeling/blob/master/ecmfa.smartcity/model/mykpi.model), conform to the [metamodels](https://github.com/gssi/SmartCityModeling/tree/master/ecmfa.smartcity), will be used as input for an Evaluation Engine that will interpret and calculate the modeled KPIs for the candidate city. The evaluation engine produces a list of KPIs, identified in the KPI Model and reported in the Evaluated KPI Model. 

## SmartCityModeling Overview
![Proposed Approach](https://github.com/gssi/SmartCityModeling/blob/master/approach.jpg)

## Contributors
- Martina De Sanctis - Gran Sasso Science Institute - GSSI, L'Aquila, Italy
- Ludovico Iovino - Gran Sasso Science Institute - GSSI, L'Aquila, Italy
- Maria Teresa Rossi - Gran Sasso Science Institute - GSSI, L'Aquila, Italy

## Dependencies
To run the Evaluation Engine you need to download Eclipse.
The evaluation engine has been implemented with Epsilon, via the Epsilon Object Language (EOL), thus we suggest to download the Eclipse IDE with the necessary Epsilon plugin already installed at this [link](https://www.eclipse.org/epsilon/download/).

## How to run
First, you have to import the [project](https://github.com/gssi/SmartCityModeling/tree/master/ecmfa.smartcity) in an Eclipse workspace. Then, register the [smart city](https://github.com/gssi/SmartCityModeling/blob/master/ecmfa.smartcity/smart_city.ecore) and the [KPI](https://github.com/gssi/SmartCityModeling/blob/master/ecmfa.smartcity/kpi.ecore) metamodels as *Ecore Packages*.
At this stage, you have to create a new *Run Configuration* following these steps:
1. Click on *Run Configurations*;
2. Right-click on *EOL Program* in the list of launch configurations and select *New Configuration*;
3. In the newly opened window, after giving a name to the new configuration, you have to select the script to run, in this case insert the address of the file named *"main.eol"*;
4. In the view *Models*, click on *Add* and select *EMF Model*;
5. Now, in the view *Configure EMF Model* choose a name like *"smart city"* and in the field *Model file* insert the address of the smart city model that you can find in the folder *model* of the project. Meanwhile, in the field *Metamodels*, insert the address of the file .ecore containing the smart city metamodel;
6. After clicking on the *OK* button, repeat the steps from **4 to 5** to add the KPI model;
7. Finally, click on *Run* and the evaluation results will be displayed in the console.

