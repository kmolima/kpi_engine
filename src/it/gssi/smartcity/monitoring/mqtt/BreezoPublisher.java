package it.gssi.smartcity.monitoring.mqtt;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.epsilon.eol.types.EolMap;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.gssi.smartcity.modeling.gathering.SCModelLoader;
import it.gssi.smartcity.monitoring.gathering.BreezoMeterGatherer;

public class BreezoPublisher implements Runnable{


public static void main(String[] args) throws MqttException, Exception {
	BreezoPublisher p=new BreezoPublisher();  
	Thread t1 =new Thread(p);  
	t1.start();  
	  
  }

@Override
public void run() {
	// TODO Auto-generated method stub
	System.out.println("thread is running...");  
	String city= "continousmonitoring/models/AQ.sc";
	
	String key = "50286f6207344bd8b4ad1b66ecccea5c";
	

		SCModelLoader scmodel = new SCModelLoader(city);
		
		EolMap latlong;
		try {
		
		latlong = scmodel.getSmartCityLatLong();
		
		BreezoMeterGatherer breezometer = new BreezoMeterGatherer( key, latlong.get("latitude").toString(), latlong.get("longitude").toString());
		
	    System.out.println("== START PUBLISHER =="+city);

	    MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
	    MqttConnectOptions option = new MqttConnectOptions();
	    option.setKeepAliveInterval(30);
	    client.connect(option);
	    
	    MqttMessage message = new MqttMessage();
	    
	    while (true) {
	    	long startTime=  System.currentTimeMillis();
	    	HashMap actualvalues = breezometer.checkPollutants();
	    	long endTime = System.currentTimeMillis(); 
	    	System.err.println((endTime - startTime)+" Getting data from Brezomeeter in milliseconds");
	    	
	    	Iterator it = actualvalues.entrySet().iterator();
	    	
	        while (it.hasNext()) {
	        	
	        Map.Entry pair = (Map.Entry)it.next();
	       
	        Double value = (Double) pair.getValue();
	        String messageJson="{\""+pair.getKey().toString()+"\":\""+value+"\"}";
	        message.setPayload( messageJson.getBytes());
	        String topic = "lat/"+latlong.get("latitude").toString()+"/long/"+latlong.get("longitude").toString()+"/"+pair.getKey().toString();
	       	//System.out.println(topic);
	        //String topic = pair.getKey().toString();
	        client.publish(topic.toLowerCase(), message);
	       	Timestamp ts = Timestamp.from(Instant.now());
	        //System.out.println("\tNew parameter "+pair.getKey().toString()+"  detection : "+ messageJson +" from Breezometer"+"("+ts+")");
	       
	       	it.remove(); // avoids a ConcurrentModificationException
	        
	        }
	      
			Thread.sleep(50000);
			
	    	 
		}
		} catch (URISyntaxException | MqttException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
   
   
	//client.disconnect(); 
    //System.out.println("== END PUBLISHER ==");

}


}
