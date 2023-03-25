package it.gssi.smartcity.monitoring.mqtt;

import com.tinkerforge.IPConnection;
import com.tinkerforge.NetworkException;
import com.tinkerforge.TinkerforgeException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;

import org.eclipse.epsilon.eol.types.EolMap;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletAirQuality;
import com.tinkerforge.BrickletAirQuality.AllValues;

import it.gssi.smartcity.modeling.gathering.SCModelLoader;

import com.tinkerforge.BrickletLCD128x64;

public class AirQualitySensorPublisher implements Runnable{
	private static final String HOST = "localhost";
	private static final int PORT = 4223;

	// Change XYZ to the UID of your Air Quality Bricklet
	private static final String UID = "Qsc";
    private static final String DISPLAY_UID = "R27";

	// Note: To make the example code cleaner we do not handle exceptions. Exceptions
	//       you might normally want to catch are described in the documentation
	public static void main(String args[]) {
		AirQualitySensorPublisher p= new AirQualitySensorPublisher();
		Thread t1 =new Thread(p);  
		t1.start();  
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletAirQuality aq = new BrickletAirQuality(UID, ipcon); // Create device object
        BrickletLCD128x64 lcd = new BrickletLCD128x64(DISPLAY_UID, ipcon); // Create device object
        System.out.println("== START PUBLISHER Tinkerforge Air Quality Bricklet==");
        
       // System.out.println("thread is running...");  
    	String city= "continousmonitoring/models/AQ.sc";
    	SCModelLoader scmodel = new SCModelLoader(city);
    	
    	
        try {
        	EolMap latlong = scmodel.getSmartCityLatLong();
        	
			ipcon.connect(HOST, PORT);
		
       
		aq.addTemperatureListener(new BrickletAirQuality.TemperatureListener() {
			
			@Override
			public void temperature(int temperature) {
				// TODO Auto-generated method stub
			
				
				// Clear display
		        try {
		        	
		        	double value =  temperature/100.0;
		        	
					lcd.clearDisplay();
					
			        lcd.writeLine(0, 0,"Temperature: "+value+ " C");
			        
				    MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
				    MqttConnectOptions option = new MqttConnectOptions();
				    option.setKeepAliveInterval(30);
				    client.connect(option);
				    
				    MqttMessage message = new MqttMessage();
			        
				   
			        String messageJson="{\"CountrysideTemperature\":\""+value+"\"}";
			        message.setPayload( messageJson.getBytes());
			        String topic = "lat/"+latlong.get("latitude").toString()+"/long/"+latlong.get("longitude").toString()+"/CountrysideTemperature";
			       	//System.out.println(topic);
			        //String topic = pair.getKey().toString();
			        client.publish(topic.toLowerCase(), message);
			       	Timestamp ts = Timestamp.from(Instant.now());
			        //System.out.println("\tNew parameter Temperature  detection : "+ messageJson +" from Air Quality Sensor "+"("+ts+")");
			       
				} catch (TinkerforgeException | MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});

        // Add all values listener
        aq.addHumidityListener(new BrickletAirQuality.HumidityListener() {
			
			@Override
			public void humidity(int humidity) {
				// TODO Auto-generated method stub
				
		    	
				double humidityvalue =  humidity/100.0;
				
				
				//System.out.println(humidityvalue);
				// Clear display
		        try {
		        	
					lcd.clearDisplay();
					
			        lcd.writeLine(1, 0,"Humidity: "+humidityvalue+ "%RH");
			        
				    MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
				    MqttConnectOptions option = new MqttConnectOptions();
				    option.setKeepAliveInterval(30);
				    client.connect(option);
				    
				    MqttMessage message = new MqttMessage();
			        
				    Double value = humidityvalue;
			        String messageJson="{\"Humidity\":\""+value+"\"}";
			        message.setPayload( messageJson.getBytes());
			        String topic = "lat/"+latlong.get("latitude").toString()+"/long/"+latlong.get("longitude").toString()+"/Humidity";
			       //	System.out.println(topic);
			        //String topic = pair.getKey().toString();
			        client.publish(topic.toLowerCase(), message);
			       	Timestamp ts = Timestamp.from(Instant.now());
			       // System.out.println("\tNew parameter Humidity  detection : "+ messageJson +" from Air Quality Sensor "+"("+ts+")");
			       
				} catch (TinkerforgeException | MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

        // Set period for all values callback to 1s (1000ms)
        aq.setHumidityCallbackConfiguration(50000,true,'x',0,0);
        aq.setTemperatureCallbackConfiguration(50000,true,'x',0,0);
        
        System.out.println("Press key to exit"); System.in.read();
        ipcon.disconnect();
        } catch (NetworkException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AlreadyConnectedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // Connect to brickd
 catch (TinkerforgeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}