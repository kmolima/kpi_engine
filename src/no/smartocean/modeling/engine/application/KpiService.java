package no.smartocean.modeling.engine.application;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class KpiService {
	public static void main(String args[]){

		Path conf = args.length > 0 ? Path.of(args[0]) : Path.of("config/config.yaml");
		if (Files.isReadable(conf)) {
			try {
				
				final AppConfig config = AppConfig.loadFromFile(conf);
				
				EolTaskLoader semantic_translator = new EolTaskLoader("scripts/http.eol");
				
				URI subject_metamodel_uri = KpiEngine.class.getResource("/no/smartocean/modeling/metamodels/monitoring.ecore").toURI();
				URI kpi_metamodel_uri = KpiEngine.class.getResource("/no/smartocean/modeling/metamodels/kpi.ecore").toURI();
				
				URI subject_model_uri = KpiEngine.class.getResource("/smart_ocean_manual.model").toURI();
				URI kpi_model_uri = KpiEngine.class.getResource("/near_factor_kpi.model").toURI();
				
				ArrayList<String> queries = semantic_translator.translate(subject_metamodel_uri,kpi_metamodel_uri,subject_model_uri,kpi_model_uri,"smartocean","kpi");
				
				
				for(String query: queries) {
					System.out.println("Processing Query:");
					System.out.println("\t"+query);
					
					String ascii = config.getURL(query); //"query?query=com_hivemq_messages_incoming_publish_bytes"
					
					ResponseHandler handler = new ResponseHandler();
					JSONObject jsonObject = KpiService.getHTTPResponse(config, ascii);
		            handler.handle(jsonObject);
					
					
					System.out.println("Request URL Encoded:");
					System.out.println("\t"+ascii);
					
					
				}

			} catch (IOException | URISyntaxException | ParseException e) {
				e.printStackTrace();

			} finally {

			}
		}
	}
	
	public static JSONObject getHTTPResponse(AppConfig config, String ascii) throws ClientProtocolException, IOException, ParseException, URISyntaxException {
		
		JSONObject jsonObject=new JSONObject();
		
		// set request timeout
		int timeout = 10000; // 10 seconds //TODO improve this
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder = requestBuilder.setConnectTimeout(timeout);
		requestBuilder = requestBuilder.setConnectionRequestTimeout(timeout);        


		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultRequestConfig(requestBuilder.build());

		CloseableHttpClient client = builder.build();
		final HttpPost httpPost = new HttpPost(ascii); 
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

		CloseableHttpResponse response = client.execute(httpPost);
		
		//HTTP API - https://prometheus.io/docs/prometheus/latest/querying/api/#format-overview
		final int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode >= 200 && statusCode < 300) { 
			
            HttpEntity httpEntity = response.getEntity();
            
            // Create a reader with the input stream reader.
            //EntityUtils.toByteArray(httpEntity); //TODO Sanitize json
            
            String jsonOutput = EntityUtils.toString(httpEntity);
            
            JSONParser jsonParser=new  JSONParser();
            jsonObject=(JSONObject) jsonParser.parse(jsonOutput);

            System.out.println("Success!\n" + jsonObject);
			
		} else if(statusCode == 400){
			System.out.println("Error in connection to prometheus via the HTTP API for query: \n"+ascii+"\n Parameters are missing or incorrect");
			System.err.println(response.getStatusLine().getReasonPhrase());
		}
		else {
			System.out.println("Error in connection to prometheus via the HTTP API");
			System.err.println("Response code: "+statusCode+ " "+response.getStatusLine().getReasonPhrase());
		}
		
		return jsonObject;
		
	}
}
