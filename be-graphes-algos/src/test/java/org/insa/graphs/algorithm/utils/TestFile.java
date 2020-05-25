package org.insa.graphs.algorithm.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

public class TestFile extends AbstractFile{
	
	public static void createTestFile(String mapName, Mode mode, int nbIterations) throws Exception {

		TestFile.mapName = mapName;
		TestFile.mode = mode;
		TestFile.nbIterations = nbIterations;
		try (GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathMaps + mapName + ".mapgr"))))) {
			graph = reader.read();
	    }
		if (TestFile.mode == Mode.LENGTH) 
			arcInspector = ArcInspectorFactory.getAllFilters().get(0); // Shortest
		else 
			arcInspector = ArcInspectorFactory.getAllFilters().get(2); // Fastest
		
		String filename = createFileName();
		
		BufferedWriter out = writeHeader(filename);
		
		List<Node> nodes = graph.getNodes();
		int index = 0;
		int index2 = 0;
		
		boolean ok = false;
		
		// We randomly test nbIterations pairs of Node in the loaded map
		
		for (int i = 0; i < TestFile.nbIterations; i++) {
			ok = false;
			while (!ok) {
				index = (int) (Math.random()*(nodes.size()-1));
				index2 = (int) (Math.random()*(nodes.size()-1));
				ok = true; // TODO 
				// Verify connexity
				
			}
			Node debut = nodes.get(index);
			Node fin = nodes.get(index2);
			out.write(debut.getId() + " " + fin.getId() + "\n");
		}
		out.close();
		
	}
	
}
