package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.graphs.algorithm.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.*;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import org.insa.graphs.algorithm.shortestpath.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DijkstraTest {
	
	// Modifiable Variables
	
	/**
     * True if we want to display test information such as : "Node a to Node b", etc
     */
	private final boolean INFOS = true; // 
	
	/**
     * Path to the Maps folder
     */
	private final static String pathMaps = "/home/etudiant/Desktop/BE-Graphes/Maps";
	
	/**
     * Number of iterations (number of tested pairs of Nodes) for the loaded maps
     */
	private final static int nbIterations = 1;

	
	
	// Data use for tests
    protected static ShortestPathData data;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d, f2e;
    
    // Some graphs...
    private static Graph defaultGraph, emptyGraph, hauteGaronneGraph;
    
    // Some ArcInspectors
    private static ArcInspector [] arcInspectors;
    
    // Precision in doubles' comparison during test
    private static double precision = 0.5;
    
    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        a2b = Node.linkNodes(nodes[0], nodes[1], 10, speed10, null);
        a2c = Node.linkNodes(nodes[0], nodes[2], 15, speed10, null);
        a2e = Node.linkNodes(nodes[0], nodes[4], 15, speed20, null);
        b2c = Node.linkNodes(nodes[1], nodes[2], 10, speed10, null);
        c2d_1 = Node.linkNodes(nodes[2], nodes[3], 20, speed10, null);
        c2d_2 = Node.linkNodes(nodes[2], nodes[3], 10, speed10, null);
        c2d_3 = Node.linkNodes(nodes[2], nodes[3], 15, speed20, null);
        d2a = Node.linkNodes(nodes[3], nodes[0], 15, speed10, null);
        d2e = Node.linkNodes(nodes[3], nodes[4], 22.8f, speed20, null);
        e2d = Node.linkNodes(nodes[4], nodes[0], 10, speed10, null);

        defaultGraph = new Graph("ID", "", Arrays.asList(nodes), null);
        emptyGraph = new Graph("ID", "", new ArrayList<Node>(), null);
        try (GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathMaps + "/haute-garonne.mapgr"))))) {
        	hauteGaronneGraph = reader.read();
        }
        
        arcInspectors = new ArcInspector[2];
		arcInspectors[0] = ArcInspectorFactory.getAllFilters().get(0); // First ArcInspector = pedestrians + bicycles
		arcInspectors[1] = ArcInspectorFactory.getAllFilters().get(2); // Third ArcInspector = only cars

    }
    
    @Test
    public void testIsValidDefaultGraph() {
    	for (int index=0; index < nodes.length; index++) {
    		for (int index2=0; index2 < nodes.length; index2++) {
    			if (INFOS) System.out.printf("\nNode %d to Node %d\n", index, index2);
    			Node debut = nodes[index];
    			Node fin = nodes[index2];
				
    			for (ArcInspector AI : arcInspectors) {
			    	data = new ShortestPathData(defaultGraph, debut, fin, AI);
			    	ShortestPathSolution Solution = createSolution();
			    	if (Solution.isFeasible()) assertEquals(true, Solution.getPath().isValid());
			    	/*Path Path = Solution.getPath();
			    	System.out.println("Path " + Path.toString());
			    	if (Path.getOrigin() != null) System.out.println("Get origin :" + Path.getOrigin().getId());
			    	if (!Path.isEmpty()) System.out.println("Get Destination :" + ((Path.getArcs()).get(Path.getArcs().size()-1)).getDestination().getId());*/
    			}
		    }
    	}
    }
    
	@Test
    public void testIsValidCheminInexistant() {
		Node debut = nodes[0];
    	Node fin = nodes[5];
    	for (ArcInspector AI : arcInspectors) {
	    	data = new ShortestPathData(defaultGraph, debut, fin, AI);
	    	assertEquals(Status.INFEASIBLE,createSolution().getStatus());
		}		        
    }
	
	@Test
    public void testIsValidEmptyGraph() {
		for (int index=0; index < nodes.length-1; index++) {
    		for (int index2=0; index2 < nodes.length-1; index2++) {
    			if (INFOS) System.out.printf("\nNode %d to Node %d\n", index, index2);
    			Node debut = nodes[index];
    			Node fin = nodes[index2];
    			for (ArcInspector AI : arcInspectors) {
    		    	data = new ShortestPathData(emptyGraph, debut, fin, AI);
    		        assertEquals(Status.INFEASIBLE,createSolution().getStatus());
    			}		        
    		}
		}
    }
	
	@Test
	public void testOptimaliteOracleDefaultGraph() {
		for (int index=0; index < nodes.length; index++) {
    		for (int index2=0; index2 < nodes.length; index2++) {
    			if (INFOS) System.out.printf("\nNode %d to Node %d\n", index, index2);
    			Node debut = nodes[index];
    			Node fin = nodes[index2];
				
    			for (ArcInspector AI : arcInspectors) {
    		    	data = new ShortestPathData(defaultGraph, debut, fin, AI);
    		    	ShortestPathSolution Solution = createSolution();
    		    	ShortestPathSolution SolutionBellmanFord = (new BellmanFordAlgorithm(data)).run();
    		    	
    		    	//assertEquals(SolutionBellmanFord.getStatus(), Solution.getStatus());

    		    	if (Solution.isFeasible() && SolutionBellmanFord.isFeasible()) assertEquals(SolutionBellmanFord.getPath().getLength(), Solution.getPath().getLength(), precision);
    		    	if (Solution.isFeasible() && SolutionBellmanFord.isFeasible()) assertEquals(SolutionBellmanFord.getPath().getMinimumTravelTime(), Solution.getPath().getMinimumTravelTime(), precision);
    			}
    		}
    	}
	}
	
	@Test
	public void testHauteGaronneGraph() {
		List<Node> nodes = hauteGaronneGraph.getNodes();
		
		// We randomly test nbIterations pairs of Node in the map Haute-Garonne
		
		for (int i = 0; i < nbIterations; i++) {
			int index = (int) (Math.random()*(nodes.size()-1));
			int index2 = (int) (Math.random()*(nodes.size()-1));
			if (INFOS) System.out.printf("\nNode %d to Node %d\n", index, index2);
			Node debut = nodes.get(index);
			Node fin = nodes.get(index2);
			
			for (ArcInspector AI : arcInspectors) {
		    	data = new ShortestPathData(hauteGaronneGraph, debut, fin, AI);
		    	ShortestPathSolution Solution = createSolution();
		    	ShortestPathSolution SolutionBellmanFord = (new BellmanFordAlgorithm(data)).run();
		    	if (Solution.isFeasible()) assertEquals(true, Solution.getPath().isValid());
		    	/*Path Path = Solution.getPath();
		    	System.out.println("Path " + Path.toString());
		    	if (Path.getOrigin() != null) System.out.println("Get origin :" + Path.getOrigin().getId());
		    	if (!Path.isEmpty()) System.out.println("Get Destination :" + ((Path.getArcs()).get(Path.getArcs().size()-1)).getDestination().getId());*/
		    	if (Solution.isFeasible() && SolutionBellmanFord.isFeasible()) assertEquals(SolutionBellmanFord.getPath().getLength(), Solution.getPath().getLength(), precision);
		    	if (Solution.isFeasible() && SolutionBellmanFord.isFeasible()) assertEquals(SolutionBellmanFord.getPath().getMinimumTravelTime(), Solution.getPath().getMinimumTravelTime(), precision);
			}
    	}
    }
	
	/**
	 * @return The ShortestPathSolution using Dijkstra Algorithm.
	 */
	protected ShortestPathSolution createSolution() {
		return (new DijkstraAlgorithm(data)).run();
	}
}
