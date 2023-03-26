package no.smartocean.modeling.engine.application;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class KpiEngine {

	public static void main(String args[]) throws Exception {

		Path conf = args.length > 0 ? Path.of(args[0]) : Path.of("config/config.yaml");
		if (Files.isReadable(conf)) {
			try {
				
				final AppConfig config = AppConfig.loadFromFile(conf);
				
				URL url = config.getURL("query?query=com_hivemq_messages_incoming_publish_bytes"); //TODO get from EOL script main

				// set request timeout
				int timeout = 10000; // 10 seconds
				RequestConfig.Builder requestBuilder = RequestConfig.custom();
				requestBuilder = requestBuilder.setConnectTimeout(timeout);
				requestBuilder = requestBuilder.setConnectionRequestTimeout(timeout);        


				HttpClientBuilder builder = HttpClientBuilder.create();
				builder.setDefaultRequestConfig(requestBuilder.build());

				CloseableHttpClient client = builder.build();
				final HttpPost httpPost = new HttpPost(url.toURI());
				httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

				CloseableHttpResponse response = client.execute(httpPost);
				
				//HTTP API - https://prometheus.io/docs/prometheus/latest/querying/api/#format-overview
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode >= 200 && statusCode < 300) { 
					
		            HttpEntity httpEntity = response.getEntity();
		            
		            String jsonOutput = EntityUtils.toString(httpEntity);

		            System.out.println("Success!\n" + jsonOutput);
					
					// Create a reader with the input stream reader.
		            //EntityUtils.toByteArray(httpEntity); //TODO Sanitize json
	
				} else if(statusCode == 400){
					System.out.println("Error in connection to prometheus via the HTTP API for query: \n"+url.toString()+"\n Parameters are missing or incorrect");
					System.err.println(response.getStatusLine().getReasonPhrase());
				}
				else {
					System.out.println("Error in connection to prometheus via the HTTP API");
					System.err.println("Response code: "+statusCode+ " "+response.getStatusLine().getReasonPhrase());
				}

			} catch (IOException e) {
				e.printStackTrace();

			} finally {

			}
		}
	}

}
