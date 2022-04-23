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

public class BreezoMeterGatherer extends DataGatherer{
	private String key;
	private String latitude;
	private String longitude;
		
	public static void main(String args[]) throws URISyntaxException {
		String key = "50286f6207344bd8b4ad1b66ecccea5c";
		
		SCModelLoader scmodel = new SCModelLoader("bz.sc");
		
		EolMap latlong = scmodel.getSmartCityLatLong();
		
		BreezoMeterGatherer breezometer = new BreezoMeterGatherer( key, latlong.get("latitude").toString(), latlong.get("longitude").toString());
		
		//System.out.println(breezometer.getPollutants());
	}

	public BreezoMeterGatherer(String key, String latitude, String longitude) {
		// TODO Auto-generated constructor stub
		this.key=key;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public HashMap checkPollutants( ) {
		
		 return this.getPollutants( );
	}
	
	
	
	public HashMap getPollutants( ) {
		
	        
		HashMap<String, Double> breezopollutants = new HashMap<String, Double>();
        
		JSONParser parser = new JSONParser();

        try {       
        	String url =String.format("https://api.breezometer.com/air-quality/v2/current-conditions?lat=%s&lon=%s&key=%s&features=pollutants_concentrations",latitude, longitude, key);
          
        	URL oracle = new URL(url); // URL to Parse
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
           
            String inputLine;
            while ((inputLine = in.readLine()) != null) {              
               JSONObject a = (JSONObject) parser.parse(inputLine);
               JSONObject data= (JSONObject)(a.get("data"));
               JSONObject pollutants = (JSONObject)data.get("pollutants");
                // Loop through each item
               Iterator it = pollutants.entrySet().iterator();
           	
               while (it.hasNext()) {
               	
               Map.Entry pair = (Map.Entry)it.next();
               String pollutant= (String) (pair.getKey());
               JSONObject jpollutant =(JSONObject) pair.getValue();
               
               JSONObject concentration =(JSONObject) jpollutant.get("concentration");
               Number conc = (Number) concentration.get("value");
               Double value = 0.0;
               if(conc instanceof Long) {
            	  value = (Double) conc.doubleValue();
               }else {
            	   value = (Double) conc; 
            	   
               }
               
               breezopollutants.put(pollutant, value);
               }
             
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
		
       return breezopollutants;
	}
	
	 public Double getPollutant(String pollutant) {
		 JSONParser parser = new JSONParser();

	        try {        
	            URL oracle = new URL(String.format("https://api.breezometer.com/air-quality/v2/current-conditions?lat=%s&lon=%s&key=%s&features=pollutants_concentrations",latitude, longitude, key)); // URL to Parse
	            URLConnection yc = oracle.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
	           
	            String inputLine;
	            while ((inputLine = in.readLine()) != null) {              
	               JSONObject a = (JSONObject) parser.parse(inputLine);
	               JSONObject data= (JSONObject)(a.get("data"));
	               JSONObject pollutants = (JSONObject)data.get("pollutants");
	                // Loop through each item
	               
	               JSONObject realpollutant= (JSONObject) pollutants.get(pollutant);
	               JSONObject concentration = (JSONObject) realpollutant.get("concentration");
	               double value = (Double)concentration.get("value");
	              return (value);
	            }
	            in.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
			return 0.0;  
	 }
	
}
