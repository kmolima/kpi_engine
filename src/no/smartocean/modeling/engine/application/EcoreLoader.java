package no.smartocean.modeling.engine.application;

import java.nio.file.Path;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;

public class EcoreLoader {
final private Path path;
	
	public EcoreLoader(Path metamodel) {
		this.path  = metamodel;

	}
	
	public Path getPath() {
		return this.path;
	}
	
	
	/**
	 * 
	 * @param xmi - path to ecore metamodel file
	 * @param name - argument name of the model to be used in scripts
	 * @return EmfModel object
	 * @throws EolModelLoadingException
	 */
	public EmfModel loadModelFromFile(Path xmi, String name) throws EolModelLoadingException {
		
		return loadModelFromFile(xmi,name,false,false);
	}
	
	/**
	 * 
	 * @param xmi -  path to ecore metamodel file
	 * @param name - argument name of the model to be used in scripts
	 * @param caching - enable cache for the model
	 * @param store - store model on disposal
	 * @return EmfModel object
	 * @throws EolModelLoadingException
	 */
	public EmfModel loadModelFromFile(Path xmi, String name, boolean caching, boolean store) throws EolModelLoadingException {
		
		EmfModel model_instance = new EmfModel();
		//model_instance.clearCache();
		model_instance.setName(name);
		model_instance.setMetamodelFile(this.path.toAbsolutePath().toString());
		model_instance.setModelFile(xmi.toAbsolutePath().toString());
		model_instance.setReadOnLoad(true);
		model_instance.setStoredOnDisposal(true);
		
		model_instance.setCachingEnabled(caching);
		model_instance.setStoredOnDisposal(store);
		
		model_instance.load();
		
		return model_instance;
	}
	
	/**
	 * 
	 * @param xmi - path to ecore metamodel file
	 * @param name - argument name of the model to be used in scripts
	 * @return EmfModel object
	 * @throws EolModelLoadingException
	 */
	public InMemoryEmfModel loadModelFromResource(Resource xmi, String name) throws EolModelLoadingException {
		
		return loadModelFromResource(xmi,name,false,false);
	}
	
	/**
	 * 
	 * @param xmi -  path to ecore metamodel file
	 * @param name - argument name of the model to be used in scripts
	 * @param caching - enable cache for the model
	 * @param store - store model on disposal
	 * @return EmfModel object
	 * @throws EolModelLoadingException
	 */
	public InMemoryEmfModel loadModelFromResource(Resource uri, String name, boolean caching, boolean store) throws EolModelLoadingException {
				
		InMemoryEmfModel model_instance = new InMemoryEmfModel(uri);
		model_instance.setName(name);
		model_instance.setReadOnLoad(true);
		model_instance.setCachingEnabled(caching);
		model_instance.setStoredOnDisposal(store);
		
		model_instance.load();
		
		return model_instance;
	}
}
