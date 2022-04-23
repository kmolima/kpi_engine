package it.gssi.smartcity.monitoring.mqtt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.eol.types.EolMap;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import it.gssi.smartcity.modeling.gathering.SCModelLoader;

public class Subscriber {

  public static void main(String[] args) throws MqttException, Exception {

   
    SCModelLoader scmodel = new SCModelLoader("continousmonitoring/models/AQ.sc");
    System.out.println("== START SUBSCRIBER =="+scmodel.getSmartCityLatLong());
	EolMap latlong = scmodel.getSmartCityLatLong();
	
	//List<String> sources = Arrays.asList("https://breezometer.com/", "");
	EolMap params = scmodel.getRequiredParams();
	
	
    MqttClient client=new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
    SimpleMqttCallBack callback = new SimpleMqttCallBack();
    callback.setLoader(scmodel);
    
    client.setCallback(  callback);
    MqttConnectOptions option = new MqttConnectOptions();
    option.setKeepAliveInterval(30);
    client.connect(option);
   

    Iterator it = params.entrySet().iterator();
    while (it.hasNext()) {
    	 Map.Entry pair = (Map.Entry)it.next();
    	 String topic = "lat/"+latlong.get("latitude").toString()+"/long/"+latlong.get("longitude").toString()+"/"+pair.getKey().toString();
    	 //System.out.println(topic.toLowerCase());
    	 client.subscribe(topic.toLowerCase());
    }
  
	
   
  }

}
