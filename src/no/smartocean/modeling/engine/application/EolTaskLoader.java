package no.smartocean.modeling.engine.application;

import java.net.URISyntaxException;
import java.nio.file.Path;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.launch.EolRunConfiguration;

public class EolTaskLoader {
	
	final private Path script;
	
	public EolTaskLoader(String resource) throws URISyntaxException {
		script = Path.of(EolTaskLoader.class.getResource(resource).toURI());
		System.out.println(script);
		
	}
	
	public Object run(EmfModel kpimodel, EmfModel subjectmodel) throws URISyntaxException, Exception {
		Object result=null;	
		
		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
				.withScript(script)
				.withModel(kpimodel)
				.withModel(subjectmodel)
				//.withParameter("Thread", Thread.class)
				.build();
				
		runConfig.run();
		
		result = runConfig.getResult();
		System.out.println("EOL script execution took: "+runConfig.getExecutionTime().getSeconds());
		
		kpimodel.dispose();
		subjectmodel.dispose();
		
		return result;
	}
	
	

	public static void main(String[] args) {
		
		Path kpi_model,kpi_metamodel;
		Path so_model, so_metamodel;
		try {
			
			kpi_metamodel = Path.of(EolTaskLoader.class.getResource("/no/smartocean/modeling/metamodels/kpi.ecore").toURI());
			so_metamodel = Path.of(EolTaskLoader.class.getResource("/no/smartocean/modeling/metamodels/monitoring.ecore").toURI());
			
			kpi_model = Path.of(EolTaskLoader.class.getResource("/timed_kpi_manual.model").toURI());
			so_model = Path.of(EolTaskLoader.class.getResource("/smart_ocean_manual.model").toURI());
			
			EcoreLoader kpi_loader = new EcoreLoader(kpi_metamodel);
			EcoreLoader so_loader = new EcoreLoader(so_metamodel);
			
			EmfModel subject = so_loader.loadModelFromFile(so_model, "smartocean");
			EmfModel kpi = kpi_loader.loadModelFromFile(kpi_model, "kpi");
			
			EolTaskLoader task_loader = new EolTaskLoader("scripts/main.eol");
			Object result= task_loader.run(kpi, subject);
			System.out.println("EOL result:");
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error Loading EMF models instances to EOL script");
		}
	}

}
