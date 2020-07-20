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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.launch.EolRunConfiguration;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.apache.commons.io.FileUtils;

/**
 * This example demonstrates using the Epsilon Object Language, the core language of Epsilon, in a stand-alone manner 
 * 
 * @author Sina Madani
 * @author Dimitrios Kolovos
 */
public class EvaluationEngine {
	
	public static void main(String[] args) throws Exception {
		
		Path root = Paths.get(EvaluationEngine.class.getResource("").toURI()),
			modelsRoot = root.getParent().resolve("models"),
			metamodelsRoot = root.getParent().resolve("metamodels")
			;
		
		
		StringProperties modelProperties_kpi = new StringProperties();
		modelProperties_kpi.setProperty(EmfModel.PROPERTY_NAME, "kpi");
		modelProperties_kpi.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI,
				metamodelsRoot.resolve("kpi.ecore").toAbsolutePath().toUri().toString()
		);
		
		//load the kpi model to be evaluated
		modelProperties_kpi.setProperty(EmfModel.PROPERTY_MODEL_URI,
			modelsRoot.resolve("mykpi.kpi").toAbsolutePath().toUri().toString()
		);
		
		
		EvaluationEngine engine= new EvaluationEngine();
		StringProperties modelProperties_sc = new StringProperties();
		modelProperties_sc.setProperty(EmfModel.PROPERTY_NAME, "smartcity");
		modelProperties_sc.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI,
				metamodelsRoot.resolve("smart_city.ecore").toAbsolutePath().toUri().toString()
		);
		
		//run the evaluation on the smart cities contained in the package model
		
		String[] extensions = new String[] { "sc" };

		File dir=new File(modelsRoot.toString());
		List<File> smartcities = (List<File>) FileUtils.listFiles(dir, extensions, true);
	
		for (File sc : smartcities) {
			modelProperties_sc.setProperty(EmfModel.PROPERTY_MODEL_URI,
					modelsRoot.resolve(sc.getName()).toAbsolutePath().toUri().toString()
				);
		//execute the evaluation on the smart city sc subject		
		engine.runEvaluation(modelProperties_sc,modelProperties_kpi,root);

		}
		
	}

	private void runEvaluation(StringProperties modelProperties_sc, StringProperties modelProperties_kpi, Path root) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();

		
		EolRunConfiguration runConfig = EolRunConfiguration.Builder()
				.withScript(root.resolve("eol/main.eol"))
				.withModel(new EmfModel(), modelProperties_sc).withModel(new EmfModel(),modelProperties_kpi)
				.withParameter("Thread", Thread.class)
				.build();
			
			runConfig.run();
			
			long endTime = System.currentTimeMillis();
			System.err.println("Execution took " + (endTime - startTime) + " milliseconds");

	}
	
}
