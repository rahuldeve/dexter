/**
 *  Copyright 2011 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.cnr.isti.hpc.dexter.cli.graph;

import it.cnr.isti.hpc.cli.AbstractCommandLineInterface;
import it.cnr.isti.hpc.dexter.graph.Node;
import it.cnr.isti.hpc.dexter.graph.NodeFactory;
import it.cnr.isti.hpc.dexter.graph.NodesWriter;
import it.cnr.isti.hpc.io.reader.RecordReader;
import it.cnr.isti.hpc.log.ProgressLogger;
import it.cnr.isti.hpc.property.ProjectProperties;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 21/nov/2011
 */
public class IndexOutcomingNodesCLI extends AbstractCommandLineInterface {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(IndexOutcomingNodesCLI.class);


	private static String[] params = new String[] { "input" };

	private static final String USAGE = "java -cp $jar "
			+ IndexOutcomingNodesCLI.class + " -input outcoming-node-file";

	public static void main(String[] args) {
		IndexOutcomingNodesCLI cli = new IndexOutcomingNodesCLI(args);
		ProjectProperties properties = new ProjectProperties(IndexOutcomingNodesCLI.class);
		String outcoming = properties.get("ram.outcoming.nodes");
		File outcomingFile = new File(outcoming);
		if (outcomingFile.exists()){
			logger.info("serialized file {} yet exists, removing", outcoming);
			outcomingFile.delete();
		}
		ProgressLogger pl = new ProgressLogger("indexed {} nodes", 100000);
		RecordReader<Node> reader = new RecordReader<Node>(cli.getInput(), new Node.Parser());
		NodesWriter writer = NodeFactory.getOutcomingNodeWriter(NodeFactory.STD_TYPE);
		for (Node n : reader ){
			pl.up();
			writer.add(n);
		}
		writer.close();
		
		

	}
	
	

	public IndexOutcomingNodesCLI(String[] args) {
		super(args, params, USAGE);
	}
 }