/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package it.gssi.smartcity.modeling.engine;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.EolModule;

/**
 * This class is responsible for launching the evaluation of a given smart city model on top a given kpi model
 * 
 * @author Ludovico Iovino
 * @author MT Rossi
 */
public class EvaluationEngine {
	
	public static void main(String[] args) throws Exception {
	
	
		EvaluationEngine engine = new EvaluationEngine();
		
		String scmodel = args[0];
		String kpimodel = args[1];
		
		engine.runEval( scmodel, kpimodel);
	}


	
	public void runEval(String scpath, String kpipath) throws Exception{
		long startTime = System.currentTimeMillis();
		
		
		Path root = Paths.get(EvaluationEngine.class.getResource("").toURI()),
			
			metamodelsRoot = root.getParent().resolve("metamodels")
			;
		
		EmfModel kpimodel = new EmfModel();
		kpimodel.setName("kpi");
		kpimodel.setMetamodelFile(metamodelsRoot.resolve("kpi.ecore").toFile().getAbsolutePath());
		kpimodel.setModelFile(kpipath);
		kpimodel.setReadOnLoad(true);
		kpimodel.setStoredOnDisposal(true);
		kpimodel.load();
		
		EmfModel scmodel = new EmfModel();
		scmodel.setName("smartcity");
		scmodel.setMetamodelFile(metamodelsRoot.resolve("smart_city.ecore").toFile().getAbsolutePath());
		scmodel.setModelFile(scpath);
		scmodel.setReadOnLoad(true);
		scmodel.clearCache();
		
		scmodel.setCachingEnabled(false);
		scmodel.setStoredOnDisposal(false);
		scmodel.load();
				
		EolModule module = new EolModule();
				module.parse(EvaluationEngine.class.getResource("eol/main.eol").toURI());
				module.getContext().getModelRepository().addModel(kpimodel);
				module.getContext().getModelRepository().addModel(scmodel);
		Object result =	module.execute();
				
				kpimodel.dispose();
				
				
				scmodel.dispose();
				
				

				//System.out.println("Updated model in " + kpimodel.getModelFile());
				long endTime = System.currentTimeMillis();
				System.err.println((endTime - startTime) + " Engine Execution in milliseconds");
	}
	
}
