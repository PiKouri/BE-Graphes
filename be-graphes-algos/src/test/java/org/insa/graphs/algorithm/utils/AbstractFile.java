package org.insa.graphs.algorithm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Graph;

public class AbstractFile {
	
	/**
     * Path to the Maps folder
     */
	protected final static String pathMaps = "/home/etudiant/Desktop/BE-Graphes/Maps/";
	
	/**
     * Path to the folder where the results will be put
     */
	protected final static String resultPath = "/home/etudiant/Desktop/BE-Graphes/Results/";
	
	// Graph 
	protected static Graph graph;
	
	// Some ArcInspectors
	protected static ArcInspector arcInspector;
	
	protected static String mapName;
	
	protected static Mode mode;
	
	protected static int nbIterations;
	
	protected static String extra = "";

	protected static String createFileName() {
		String name = mapName;
		name += ((mode==Mode.LENGTH) ? "_distance_" : "_temps_");
		name += nbIterations + extra + ".txt";
		return name;
	}
	
	protected static BufferedWriter writeHeader(String filename) throws Exception {
		File file = new File(resultPath + filename);
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(mapName+"\n");
		out.write(((mode==Mode.LENGTH)?0:1)+"\n");
		out.write(nbIterations + "\n");
		out.write((extra!="")?(extra+"\n"):"");
		return out;
	}
	
}
