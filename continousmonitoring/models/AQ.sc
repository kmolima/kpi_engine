<?xml version="1.0" encoding="UTF-8"?>
<smartcity:SmartCityModel xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:DataAnalytics="http://cs.gssi.it/smartcity/dataanalytics" xmlns:infrastructure="http://cs.gssi.it/smartcity/infrastructure" xmlns:smartcity="http://cs.gssi.it/smartcity" xsi:schemaLocation="http://cs.gssi.it/smartcity/dataanalytics file:///Users/administrator/git/SmartCityModeling/target/classes/it/gssi/smartcity/modeling/metamodels/smart_city.ecore#//DataAnalytics http://cs.gssi.it/smartcity/infrastructure file:///Users/administrator/git/SmartCityModeling/target/classes/it/gssi/smartcity/modeling/metamodels/smart_city.ecore#//infrastructure http://cs.gssi.it/smartcity file:///Users/administrator/git/SmartCityModeling/target/classes/it/gssi/smartcity/modeling/metamodels/smart_city.ecore" xmi:id="_lAZUYHWzEeqwqr-NlLgWuQ">
  <smartCities xmi:id="_lAZUYXWzEeqwqr-NlLgWuQ" city="L'Aquila" latitude="42.3634151" longitude="13.3682565">
    <stakeholders xsi:type="DataAnalytics:OpenData" xmi:id="_lAZUZHWzEeqwqr-NlLgWuQ" url="https://breezometer.com/" name="BreezoMeter"/>
    <stakeholders xsi:type="DataAnalytics:OpenData" xmi:id="_lAZUZXWzEeqwqr-NlLgWuQ" url="https://www.piste-ciclabili.com/" name="PisteCiclabili.com"/>
    <stakeholders xsi:type="DataAnalytics:OpenData" xmi:id="_lAZUZnWzEeqwqr-NlLgWuQ" url="http://asc.istat.it/asc_BL/" name="Atlante Statistico dei Comuni"/>
    <stakeholders xsi:type="DataAnalytics:OpenData" xmi:id="_3DnTkJnhEeqb6_1e3fZsNw" url="https://play.google.com/store?hl=en" name="GooglePlayStore"/>
    <stakeholders xsi:type="infrastructure:MonitoringInfrastructure">
      <devices xsi:type="infrastructure:Sensor" model="Tinkerforge Air Quality Bricklet" location="L'Aquila"/>
    </stakeholders>
    <stakeholders xsi:type="DataAnalytics:OpenData" url="openweathermap.org" name="OpenWeatherMap"/>
    <data xmi:id="_lAZUZ3WzEeqwqr-NlLgWuQ" name="AirMonitoring">
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUaHWzEeqwqr-NlLgWuQ" name="PM25" unit="ng/m3" src="_lAZUZHWzEeqwqr-NlLgWuQ" runtime="true" value="14.25"/>
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUaXWzEeqwqr-NlLgWuQ" name="PM10" unit="ng/m3" src="_lAZUZHWzEeqwqr-NlLgWuQ" runtime="true" value="19.23"/>
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUanWzEeqwqr-NlLgWuQ" name="NO2" unit="ng/m3" src="_lAZUZHWzEeqwqr-NlLgWuQ" runtime="true" value="11.99"/>
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUa3WzEeqwqr-NlLgWuQ" name="O3" unit="ng/m3" src="_lAZUZHWzEeqwqr-NlLgWuQ" runtime="true" value="58.6"/>
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUbHWzEeqwqr-NlLgWuQ" name="SO2" unit="ng/m3" src="_lAZUZHWzEeqwqr-NlLgWuQ" runtime="true" value="0.38"/>
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_pPfT4JaVEeq0XaWaWNepEA" name="CO2" unit="tonnes" src="_lAZUZHWzEeqwqr-NlLgWuQ"/>
      <data xsi:type="DataAnalytics:RealValue" name="Humidity" unit="% HU" src="//@smartCities.0/@stakeholders.4" runtime="true" value="46.79"/>
      <data xsi:type="DataAnalytics:RealValue" name="CountrysideTemperature" unit="degrees" src="//@smartCities.0/@stakeholders.5" runtime="true" value="30.02"/>
      <data xsi:type="DataAnalytics:RealValue" name="CityTemperature" unit="degrees" src="//@smartCities.0/@stakeholders.5" runtime="true" value="22.54"/>
    </data>
    <data xmi:id="_lAZUcXWzEeqwqr-NlLgWuQ" name="CityStatistics">
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUcnWzEeqwqr-NlLgWuQ" name="CityExt" unit="km2" src="_lAZUZnWzEeqwqr-NlLgWuQ" value="480.0"/>
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUc3WzEeqwqr-NlLgWuQ" name="CityPop" unit="inhabitants" value="69605.0"/>
    </data>
    <data xmi:id="_lAZUenWzEeqwqr-NlLgWuQ" name="BikePaths">
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUe3WzEeqwqr-NlLgWuQ" name="BykePathLength" unit="km" src="_lAZUZXWzEeqwqr-NlLgWuQ" value="86.0"/>
    </data>
    <data xmi:id="_lAZUfHWzEeqwqr-NlLgWuQ" name="Green Areas">
      <data xsi:type="DataAnalytics:RealValue" xmi:id="_lAZUfXWzEeqwqr-NlLgWuQ" name="TotalGreenArea" unit="hectares" src="_lAZUZnWzEeqwqr-NlLgWuQ" value="48.7235"/>
    </data>
    <data xmi:id="_Rtgt4JnjEeqb6_1e3fZsNw" name="TransportMonitoring">
      <data xsi:type="DataAnalytics:BoolValue" xmi:id="_VDX2cJnjEeqb6_1e3fZsNw" name="RealtimeTransportMonitoring" unit="yes/no" value="true"/>
    </data>
    <data xmi:id="_8EwAQJnjEeqb6_1e3fZsNw" name="MobileApplications">
      <data xsi:type="DataAnalytics:IntegerValue" xmi:id="___XbsJnjEeqb6_1e3fZsNw" name="MobileApplicationsPS" unit="units" src="_3DnTkJnhEeqb6_1e3fZsNw" value="13"/>
    </data>
  </smartCities>
</smartcity:SmartCityModel>
