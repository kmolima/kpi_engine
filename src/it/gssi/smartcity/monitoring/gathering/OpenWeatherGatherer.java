package it.gssi.smartcity.monitoring.gathering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.epsilon.eol.types.EolMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import it.gssi.smartcity.modeling.gathering.SCModelLoader;

public class OpenWeatherGatherer extends DataGatherer{
	private String key;
	private String latitude;
	private String longitude;
		
	public static void main(String args[]) throws URISyntaxException {
		String key = "fdb82d2d70f50f86b201092b818fdb76";
		
		SCModelLoader scmodel = new SCModelLoader("continousmonitoring/models/AQ.sc");
		
		EolMap latlong = scmodel.getSmartCityLatLong();
		
		OpenWeatherGatherer ow = new OpenWeatherGatherer( key, latlong.get("latitude").toString(), latlong.get("longitude").toString());
		
		//System.out.println(ow.getTemperature());
	}

	public OpenWeatherGatherer(String key, String latitude, String longitude) {
		// TODO Auto-generated constructor stub
		this.key=key;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	
	
	
	
	public Double getTemperature( ) {
		
	      Double temp=0.0; 
		
		JSONParser parser = new JSONParser();

        try {       
        	String url =String.format("https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&appid=%s&units=metric",latitude, longitude, key);
          
        	URL oracle = new URL(url); // URL to Parse
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
           
            String inputLine;
            while ((inputLine = in.readLine()) != null) {              
               JSONObject a = (JSONObject) parser.parse(inputLine);
               JSONObject data= (JSONObject)(a.get("current"));
                temp = (Double)data.get("temp");
              
             
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
		
       return temp;
	}
	
	 
	
}
