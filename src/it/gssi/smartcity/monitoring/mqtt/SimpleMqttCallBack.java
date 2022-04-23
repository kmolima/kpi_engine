package it.gssi.smartcity.monitoring.mqtt;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import it.gssi.smartcity.modeling.engine.EvaluationEngine;
import it.gssi.smartcity.modeling.gathering.SCModelLoader;

public class SimpleMqttCallBack implements MqttCallback {
	
private SCModelLoader loader;

  public SCModelLoader getLoader() {
	return loader;
}

public void setLoader(SCModelLoader loader) {
	this.loader = loader;
}

public void connectionLost(Throwable throwable) {
    System.out.println("Connection to MQTT broker lost!");
  }

  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
	  String jsonString =new String(mqttMessage.getPayload());
	 
		
	  //System.out.println("Parameter "+topic+" value received:\t"+  jsonString);
	  
	  JSONObject  jsonObject=new JSONObject();
	    JSONParser jsonParser=new  JSONParser();
	    if ((jsonString != null) && !(jsonString.isEmpty())) {
	        try {
	            jsonObject=(JSONObject) jsonParser.parse(jsonString);
	            Iterator i=   jsonObject.keySet().iterator();
	            Object parameter = i.next();
	            String value = (String) jsonObject.get(parameter.toString());
	      	  if(loader.checkUpdateSCModel(parameter.toString(), value)) {
	      		 Timestamp ts = Timestamp.from(Instant.now());
	      		 // System.err.println("Triggering Smart City Evaluation..."+"("+ts+")");
	      		 EvaluationEngine engine = new EvaluationEngine();
	      		 engine.runEval("continousmonitoring/models/AQ.sc", "continousmonitoring/models/mykpi.kpi");
	      		 
	      		
	      	  }
	      	  
	        } catch (org.json.simple.parser.ParseException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 
	 
    
  }

  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
	 System.err.println("Delivery of a message complete"); 
  }
}
