package no.smartocean.modeling.engine.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.utils.URIBuilder;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class AppConfig {
    private static AppConfig instance = null; //singleton

    private String clientId;
    private String protocol;
    private String host;
    private int port;
    private List<String> endpoint;
    private Object authentication; //TODO

    public AppConfig() {

    }

    public Object getAuthentication() {
        return authentication;
    }

    private void setAuthentication(Object authentication) {
        this.authentication = authentication;
    }

    public String getClient_id() {
        return clientId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    protected void setClientId(String clientId) {
        this.clientId = clientId;
    }

    protected void setHost(String host) {
        this.host = host;
    }

    protected void setPort(int port) {
        this.port = port;
    }

    public List<String> getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(List<String> endpoint) {
		this.endpoint = endpoint;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public static AppConfig loadFromFile(Path config) throws IOException {

        Constructor constructor = new Constructor(AppConfig.class); //ClientConfig.class is root
        TypeDescription configDescription = new TypeDescription(AppConfig.class);
      //configDescription.addPropertyParameters("kpis", KpiModel.class);
        constructor.addTypeDescription(configDescription);
        Yaml yaml = new Yaml(constructor);
        yaml.setBeanAccess(BeanAccess.FIELD);
        instance = yaml.load(Files.newInputStream(config));
        yaml.setBeanAccess(BeanAccess.DEFAULT);
        return instance;
    }

    public static AppConfig getInstance() {
        return instance;
    }
    
    public URL getURL(String query, Map<String,String> params) throws MalformedURLException, URISyntaxException {
    	URIBuilder builder = new URIBuilder();
    	builder = builder.setScheme(this.protocol).setHost(this.host).setPort(this.port).setPathSegments(this.endpoint).setPathSegments(query);  	
    	for(Entry<String,String> param: params.entrySet()) {
    		builder.addParameter(param.getKey(), param.getValue());
    	}
    	
    	URL url = builder.build().toURL();
    	return url;
    	
    }
    
    public URL getURL(String query) throws MalformedURLException, URISyntaxException {
    	URIBuilder builder = new URIBuilder();
    	builder.setScheme(this.protocol).setHost(this.host).setPort(this.port);
    	
    	if(query.contains("?")) {
    		ArrayList<String> path = new ArrayList<>(this.endpoint);
    		String[] result = query.split("\\?");
    		path.add(result[0]);
    		builder.setPathSegments(path).setCustomQuery(result[1]);
    	}
    	else
    		builder.setPathSegments(this.endpoint).setCustomQuery(query);
    	URL url = builder.build().toURL();
    	return url;
    	
    }
    
    public String getURLEncoded(String url) throws UnsupportedEncodingException {
    	
    	return URLEncoder.encode(url.toString(), StandardCharsets.UTF_8.toString());
    	
    }

    public static void main(String args[]) throws IOException, URISyntaxException {

        AppConfig config = AppConfig.loadFromFile(Path.of("config/config.yaml"));
        System.out.println(config.getURL("query?query=com_hivemq_messages_incoming_publish_bytes"));
    }
}
