package no.smartocean.modeling.engine.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.EolModule;
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
	
	public Object execute(EmfModel kpimodel, EmfModel subjectmodel) throws URISyntaxException, Exception {
		
		EolModule module = new EolModule();
		module.parse(EolTaskLoader.class.getResource("scripts/main.eol").toURI());
		module.getContext().getModelRepository().addModel(kpimodel);
		module.getContext().getModelRepository().addModel(subjectmodel);
		Object result =	module.execute();
			
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> translate(URI subject_metamodel_uri, URI kpi_metamodel_uri, URI subject_uri, URI kpi_uri, String subject_alias, String kpi_alias) {
		Path kpi_model,kpi_metamodel;
		Path so_model, so_metamodel;
		ArrayList<String> sequence = new ArrayList<String>();
		try {
			
			kpi_metamodel = Path.of(kpi_metamodel_uri);
			so_metamodel = Path.of(subject_metamodel_uri);
			
			kpi_model = Path.of(kpi_uri);
			so_model = Path.of(subject_uri);
			
			EcoreLoader kpi_loader = new EcoreLoader(kpi_metamodel);
			EcoreLoader so_loader = new EcoreLoader(so_metamodel);
			
			EmfModel subject = so_loader.loadModelFromFile(so_model, subject_alias);
			EmfModel kpi = kpi_loader.loadModelFromFile(kpi_model, kpi_alias);
			
			Object result = this.run(kpi, subject);
			sequence = (ArrayList<String>) result;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error Loading EMF models instances to EOL script");
		}
		return sequence;
	}
	
	

	public static void main(String[] args) {
		
		Path kpi_model,kpi_metamodel;
		Path so_model, so_metamodel;
		ArrayList<String> sequence = new ArrayList<String>();
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
			sequence = (ArrayList<String>) result;
			System.out.println("EOL result:");
			System.out.println(sequence);
			for(String query: sequence)
				System.out.println(query);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error Loading EMF models instances to EOL script");
		}
	}

}
