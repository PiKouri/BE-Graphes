package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.graphs.algorithm.*;
import org.insa.graphs.model.*;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DijkstraTest {

	// Data use for tests
    private static ShortestPathData data;
	
    // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

    // Some paths...
    private static Path emptyPath, singleNodePath, shortPath, longPath, loopPath, longLoopPath,
            invalidPath;
    
    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes
        nodes = new Node[5];
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

        graph = new Graph("ID", "", Arrays.asList(nodes), null);

        emptyPath = new Path(graph, new ArrayList<Arc>());
        singleNodePath = new Path(graph, nodes[1]);
        shortPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1 }));
        longPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2e }));
        loopPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2a }));
        longLoopPath = new Path(graph,
                Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2a, a2c, c2d_3, d2a, a2b, b2c }));
        invalidPath = new Path(graph, Arrays.asList(new Arc[] { a2b, c2d_1, d2e }));

    }
    
    @Test
    public void testDistanceCheminInexistant() {
    	Node debut = invalidPath.getOrigin();
    	Node fin = invalidPath.getDestination();
    	List <Node> list = Arrays.asList(new Node[]{debut,fin});
		ArcInspector AI0 = ArcInspectorFactory.getAllFilters().get(0); // Premier ArcInspector
		ArcInspector AI2 = ArcInspectorFactory.getAllFilters().get(2); // Troisième ArcInspector
		
    	DijkstraTest.data = new ShortestPathData(graph, debut, fin, AI0);
        assertEquals((new DijkstraAlgorithm(data)).run().getPath(), Path.createShortestPathFromNodes(graph, list));
        
        DijkstraTest.data = new ShortestPathData(graph, debut, fin, AI2);
        assertEquals((new DijkstraAlgorithm(data)).run().getPath(), Path.createShortestPathFromNodes(graph, list));
    }
    
	@Test
	public void testCarreTemps() {
		assertEquals(0,0);
		//fail("Not yet implemented");
	}

}
