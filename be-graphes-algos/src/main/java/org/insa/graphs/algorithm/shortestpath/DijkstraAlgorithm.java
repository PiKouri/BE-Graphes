package org.insa.graphs.algorithm.shortestpath;


import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.algorithm.utils.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.AbstractSolution.Status;
//import org.insa.graphs.model.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;
//import org.insa.graphs.algorithm.utils.PriorityQueue;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	// Modifiable Variable
	
	/**
     * True if we want to display information about the algorithm such as : validity of the heap, cost of current Node, number of explored successors, etc
     */
	private final boolean INFOS = false;
	
	public DijkstraAlgorithm(ShortestPathData data) {
		super(data);
	}
	
	@Override
	protected ShortestPathSolution doRun() {

		final ShortestPathData data = getInputData();
		ShortestPathSolution solution = null;

		// Retrieve the graph.
		Graph graph = data.getGraph();
		
		Node s = data.getOrigin();
		Node d = data.getDestination();
		
		// Verification of the simple case : Empty Graph
		if (graph.getNodes().size() == 0) return new ShortestPathSolution(data, Status.INFEASIBLE);
		
		// Verification of the simple case : Graph does not contain Origin or destination 
		if (!graph.getNodes().contains(s) || !graph.getNodes().contains(d)) return new ShortestPathSolution(data, Status.INFEASIBLE);
		
		// Verification of the simple case : Origin = Destination
		if (s == d) return new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, new ArrayList<Arc>())); 

		final List<Node> nodes = graph.getNodes();

		// Priority line
		BinaryHeap<Label> heap = new BinaryHeap<Label>();
		
		// Initialisation 

		@SuppressWarnings("unused")
		int nbIterations = 0;
		
		// Label list
		Label[] listLabels = new Label[nodes.size()];
		for (Node node : nodes) {
			listLabels[node.getId()] = createInitLabel(node);
		}
		
		listLabels[s.getId()].setCost(0);
		heap.insert(listLabels[s.getId()]); 
		// Initially the heap only contains Origin Node
		
		// Notify observers about the first event (origin processed).
        notifyOriginProcessed(s);

		// Itérations

		while (!(heap.isEmpty())) {
			// Incrementation of iteration counter
			nbIterations++;
			
			Node x = heap.deleteMin().getCurrentNode();
			
			//Display validity of the heap
			if(heap.isValid()) { if (INFOS)  System.out.println("	Valid heap");
			} else {
				if (INFOS) System.out.println("	Invalid heap -> STOP");
				break;
			} 
			
			listLabels[x.getId()].mark();
			// Notify observers that node x is marked.
			notifyNodeMarked(x);
			
			// If destination Node is marked, we stop the algorithm
			if (x.getId() == d.getId()) break;
			
			@SuppressWarnings("unused")
			int nbSuccesseursExplores = 0;
			
			for (Arc arcXY : x.getSuccessors()) {
				
				// Verification of the transport mode
                if (!(data.isAllowed(arcXY))) { 
                    continue;
                }
				Node y = arcXY.getDestination();
				
				if (!(listLabels[y.getId()].isMarked())) {
					
					nbSuccesseursExplores ++;
					
					double wXY = data.getCost(arcXY);
					double oldDistance = listLabels[y.getId()].getCost();
                    double newDistance = listLabels[x.getId()].getCost()+wXY;

                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(y);
                    }
                    
					if (oldDistance >= newDistance) {
						try {
							heap.remove(listLabels[y.getId()]);
						} catch (ElementNotFoundException e) {}
						listLabels[y.getId()].setCost(newDistance);
						listLabels[y.getId()].setParent(arcXY);
						heap.insert(listLabels[y.getId()]);
					}
				}
			}
			
			// Display the number of explored successors compared to the total number
			if (INFOS) System.out.println("	Node "+x.getId()+": "+nbSuccesseursExplores+"/"+x.getNumberOfSuccessors()+" Explored successors");
			
			// Cost of the marked labels is increasing through the iterations (Without AStar)
			if (INFOS) System.out.println("	Cost: " + listLabels[x.getId()].getCost()); 
			
			// Cost of the marked labels is increasing through the iterations (With AStar)
			if (INFOS) System.out.println("	Total Cost: " + listLabels[x.getId()].getTotalCost()); 
			if (INFOS) System.out.println();
		}
		
		// Creation of the ShortestPathSolution
		
		ArrayList<Arc> arcs = new ArrayList<>();
		Node i = d;
		Label labelI = listLabels[i.getId()];
		if (labelI.getParent()==null) {
			solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
        	// The destination has been found, notify the observers.
            notifyDestinationReached(d);
            
        	while (i.getId() != s.getId()) {
        		labelI = listLabels[i.getId()];
        		Arc arcI = labelI.getParent();
        		arcs.add(arcI);
        		i = arcI.getOrigin();
        	}
        	// Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
		// Number of arcs of the shortestpath and number of iterations of the algorithm
		if (INFOS) System.out.println("Number of arcs in the shortestpath : " + arcs.size());
		if (INFOS) System.out.println("Number of iterations of the algorithm : " +nbIterations);
		
		return solution;

	}
	
	/**
     * Create and return a new initial label on the given node.
     * 
     * @param node The node to label
     * @return The initial label of the given node
     */
	protected Label createInitLabel (Node node) {
		double inf = Double.POSITIVE_INFINITY;
		return new Label(node, false, inf, null);
	}
}
