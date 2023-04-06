package no.smartocean.modeling.engine.application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class KpiEngine {

	public static void main(String args[]) {

		Path conf = args.length > 0 ? Path.of(args[0]) : Path.of("config/config.yaml");
		if (Files.isReadable(conf)) {
			try {
				
				final AppConfig config = AppConfig.loadFromFile(conf);
				
				EolTaskLoader semantic_translator = new EolTaskLoader("scripts/main.eol");
				
				URI subject_metamodel_uri = KpiEngine.class.getResource("/no/smartocean/modeling/metamodels/monitoring.ecore").toURI();
				URI kpi_metamodel_uri = KpiEngine.class.getResource("/no/smartocean/modeling/metamodels/kpi.ecore").toURI();
				
				URI subject_model_uri = KpiEngine.class.getResource("/smart_ocean_manual.model").toURI();
				URI kpi_model_uri = KpiEngine.class.getResource("/near_factor_kpi.model").toURI();
				
				ArrayList<String> queries = semantic_translator.translate(subject_metamodel_uri,kpi_metamodel_uri,subject_model_uri,kpi_model_uri,"smartocean","kpi");
				
				
				for(String query: queries) {
					System.out.println("Processing Query:");
					System.out.println("\t"+query);
					
				
					URI browser = config.getConsoleBrowserAddr(queries);
					KpiEngine.openInBrowser(browser);
					
					System.out.println("Request URL Encoded:");
					System.out.println("\t"+browser);

					
					
				}

			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();

			} finally {

			}
		}
	}
	
	public static boolean openInBrowser(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    else {
	    	System.out.println("Browser Expression URL:\n\t"+uri);
	    }
	    return false;
	}
}
