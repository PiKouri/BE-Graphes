package org.insa.graphs.algorithm.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import java.util.concurrent.TimeUnit;

public class ResultFile extends AbstractFile {
	
	// Data use for tests
	protected static ShortestPathData data;
		
	private static FileInputStream init(String testFilename) throws Exception{
		File testFile = new File(resultPath+testFilename);
		if (testFile.exists()) {
			String word = "";
			FileInputStream fls = new FileInputStream(testFile);
			int n=0;
			int l=0;
			boolean ok = false;
			while ((l=fls.read())!=-1 && !ok) {
				switch (n) {
					case 0 : 				
						if (((char)l)!='\n')word += (char)l;
						else {
							ResultFile.mapName = word;
							word = "";
							n++;
						}
						break;
					case 1 :
						if (((char)l)!='\n')word += (char)l;
						else {
							ResultFile.mode =((word == "0") ? Mode.LENGTH : Mode.TIME);
							word = "";
							n++;
						}
						break;
					case 2 :
						if (((char)l)!='\n')word += (char)l;
						else {
							ResultFile.nbIterations = Integer.parseInt(word);
							word = "";
							n++;
							ok = true;
						}
						break;
				}
				if (ok) break;
			}
			try (GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathMaps + ResultFile.mapName + ".mapgr"))))) {
				graph = reader.read();
		    }
			if (ResultFile.mode == Mode.LENGTH) 
				arcInspector = ArcInspectorFactory.getAllFilters().get(0); // Shortest
			else 
				arcInspector = ArcInspectorFactory.getAllFilters().get(2); // Fastest
			return fls;
		}
		return null;
	}
	
	public static void createResultFile(String testFilename) throws Exception{
			
			List<Node> nodes = graph.getNodes();
			
			long lStartTime = 0;
			long lEndTime = 0;
			long lElapsedTime;
			String filename = "";
			
			Node debut=null;
			Node fin=null;
			
			for (int ii=1; ii<3; ii++) { // CHANGE
				FileInputStream fls = init(testFilename);
	    		switch (ii) {
	    		case 0 : 
	    			ResultFile.extra = "_bellmanford";
	    			filename = createFileName();
	    			ResultFile.extra = "bellmanford";
	    			break;
	    		case 1 : 
	    			ResultFile.extra = "_dijkstra";
	    			filename = createFileName();
	    			ResultFile.extra = "dijkstra";
	    			break;
	    		case 2 : 
	    			ResultFile.extra = "_astar";
	    			filename = createFileName();
	    			ResultFile.extra = "astar";
	    			break;
	    		}
	    		BufferedWriter out = writeHeader(filename);
				for (int i=0; i<ResultFile.nbIterations; i++) {
					int n = 0;
					String word = "";
					int l=0;
					while (n<=1)
					{
						l=fls.read();
						if (n==0) {
							if (((char)l)!=' ')word += (char)l;
							else {
								n++;
								debut = nodes.get(Integer.parseInt(word));
								word = "";
							}
						} else {
							if (((char)l)!='\n')word += (char)l;
							else {
								n++;
								fin = nodes.get(Integer.parseInt(word));
							}
						}
					}
					
					data = new ShortestPathData(graph, debut, fin, arcInspector);
					
					
					ShortestPathSolution Solution;
					switch (ii) {
		    		case 0 : 
		    			lStartTime = System.nanoTime();
						Solution = (new BellmanFordAlgorithm(data)).run();
						lEndTime = System.nanoTime();
		    			break;
		    		case 1 : 
		    			lStartTime = System.nanoTime();
				    	Solution = (new DijkstraAlgorithm(data)).run();
				    	lEndTime = System.nanoTime();
		    			break;
		    		case 2 : 
		    			lStartTime = System.nanoTime();
				    	Solution = (new AStarAlgorithm(data)).run();
				    	lEndTime = System.nanoTime();
		    			break;
		    		}
					lElapsedTime = lEndTime - lStartTime;;
			    	
			    	
		    		// Write the results in the file
			    	
					out.write(debut.getId() + " " + fin.getId() + " ");
					out.write(Long.toString(lElapsedTime));
					out.write("\n");
					
		    	}
				out.close();
			
			}
		}
}
