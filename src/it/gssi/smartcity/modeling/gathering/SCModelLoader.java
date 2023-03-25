/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package it.gssi.smartcity.modeling.gathering;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.launch.EolRunConfiguration;
import org.eclipse.epsilon.eol.types.EolMap;
import org.eclipse.paho.client.mqttv3.MqttMessage;



import org.eclipse.epsilon.emc.emf.EmfModel;


public class SCModelLoader {
	private String modelpath;

	public SCModelLoader(String path) {
		// TODO Auto-generated constructor stub
		this.modelpath=path;
		
	}
	
	
	public boolean checkUpdateSCModel(String topic, String mqttMessage) throws Exception {
		Path root, modelsRoot;
		StringProperties modelProperties = new StringProperties();
		
		 long startTime = System.currentTimeMillis();
			root = Paths.get(SCModelLoader.class.getResource("").toURI());
			modelsRoot = root.getParent().resolve("models");
			
			Path metamodelsRoot = root.getParent().resolve("metamodels");
			
			EmfModel targetModel = new EmfModel();
			String smartcityMM = metamodelsRoot.resolve("smart_city.ecore").toAbsolutePath().toUri().toString();
					
			StringProperties targetProperties = new StringProperties();
			targetProperties.setProperty(EmfModel.PROPERTY_NAME, "smartcitymodel");
			targetProperties.setProperty(EmfModel.PROPERTY_ALIASES, "smartcitymodel");
			targetProperties.setProperty(EmfModel.PROPERTY_EXPAND, "true");
			targetProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, smartcityMM);
			targetProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,
					URI.createFileURI(new File(this.modelpath).getAbsolutePath()).toString()
			);
			
			targetProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "true");
			targetProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "true");
			
		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
				.withScript(root.resolve("updatesmartcity.eol"))
				.withModel(targetModel,targetProperties)
				
				//.withProfiling()
				.withParameter("param", topic)
				.withParameter("value",  mqttMessage.toString())
				.build();
		
				
		runConfig.run();
		boolean result = (boolean) runConfig.getResult();
		targetModel.dispose();
		
		 if (result) { 
			 //System.err.println("	--> Updated model saved in " + targetModel.getModelFile());
			 long endTime = System.currentTimeMillis();
				System.err.println((endTime - startTime)+" SC model update in milliseconds");
				
			 return true;	 
		 }
			
		return false;
	}
	
	
	
	public EolMap getSmartCityLatLong() throws URISyntaxException {
		
		
		Path root, modelsRoot;
		
		StringProperties modelProperties = new StringProperties();
		
		
			root = Paths.get(SCModelLoader.class.getResource("").toURI());
			modelsRoot = root.getParent().resolve("models");
			
			Path metamodelsRoot = root.getParent().resolve("metamodels");
			
			EmfModel targetModel = new EmfModel();
			String smartcityMM = metamodelsRoot.resolve("smart_city.ecore").toAbsolutePath().toUri().toString();
					
			StringProperties targetProperties = new StringProperties();
			targetProperties.setProperty(EmfModel.PROPERTY_NAME, "smartcitymodel");
			targetProperties.setProperty(EmfModel.PROPERTY_ALIASES, "smartcitymodel");
			targetProperties.setProperty(EmfModel.PROPERTY_EXPAND, "true");
			targetProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, smartcityMM);
			targetProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,
					URI.createFileURI(new File(this.modelpath).getAbsolutePath()).toString()
			);
			
			targetProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "true");
			targetProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "true");
			
		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
				.withScript(root.resolve("getsmartcity.eol"))
				.withModel(targetModel,targetProperties)
				//.withProfiling()
				.build();
		runConfig.run();	
			//System.out.println(runConfig.getResult());
			
		EolMap results = (EolMap) runConfig.getResult();
		
		return results;
	}
	
	
	public  EolMap getRequiredParams() throws Exception{
		
		Path root, modelsRoot;
		
		StringProperties modelProperties = new StringProperties();
		
		
			root = Paths.get(SCModelLoader.class.getResource("").toURI());
			modelsRoot = root.getParent().resolve("models");
			
			Path metamodelsRoot = root.getParent().resolve("metamodels");
			
			EmfModel targetModel = new EmfModel();
			String smartcityMM = metamodelsRoot.resolve("smart_city.ecore").toAbsolutePath().toUri().toString();
					
			StringProperties targetProperties = new StringProperties();
			targetProperties.setProperty(EmfModel.PROPERTY_NAME, "smartcitymodel");
			targetProperties.setProperty(EmfModel.PROPERTY_ALIASES, "smartcitymodel");
			targetProperties.setProperty(EmfModel.PROPERTY_EXPAND, "true");
			targetProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, smartcityMM);
			targetProperties.setProperty(EmfModel.PROPERTY_MODEL_URI,
					URI.createFileURI(new File(this.modelpath).getAbsolutePath()).toString()
			);
			
			targetProperties.setProperty(EmfModel.PROPERTY_READONLOAD, "true");
			targetProperties.setProperty(EmfModel.PROPERTY_STOREONDISPOSAL, "true");
			
		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
				.withScript(root.resolve("subscriber.eol"))
				.withModel(targetModel,targetProperties)
				//.withParameter("source", source)
				//.withProfiling()
				.build();
		
			//System.out.println(runConfig.getResult());
		runConfig.run();	
		
			
			
			//System.out.println(runConfig.getResult());
			
			
		
		EolMap results = (EolMap) runConfig.getResult();
		//System.out.println("Required parameters in the loaded smart city model: "+results.toString());
		return results;
			
			
	}
	
}
