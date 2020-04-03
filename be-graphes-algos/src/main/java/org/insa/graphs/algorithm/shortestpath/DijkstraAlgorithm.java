package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.AbstractSolution.Status;
//import org.insa.graphs.model.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;
//import org.insa.graphs.algorithm.utils.PriorityQueue;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	public DijkstraAlgorithm(ShortestPathData data) {
		super(data);
	}

	@Override
	protected ShortestPathSolution doRun() {

		final ShortestPathData data = getInputData();
		ShortestPathSolution solution = null;

		// Retrieve the graph.
		Graph graph = data.getGraph();

		final int nbNodes = graph.size();

		final List<Node> nodes = graph.getNodes();

		// Liste des labels
		// ArrayList<Label> listLabels = new ArrayList<Label>(nbNodes);
		Label[] listLabels = new Label[nbNodes];
		
		// File de priorité
		BinaryHeap<Label> heap = new BinaryHeap<Label>();
		
		// Initialisation 

		double inf = Double.POSITIVE_INFINITY;
		for (Node node : nodes) {
			listLabels[node.getId()] = new Label(node, false, inf, null);
		}
		Node s = data.getOrigin();
		listLabels[s.getId()].setCost(0);
		Label labelS = listLabels[s.getId()];
		heap.insert(labelS); // Le tas ne contient que le sommet d'origine initialement

		// Itérations

		while (!(heap.isEmpty())) {
			Label labelX = heap.deleteMin();
			Node x = labelX.getCurrentNode();
			int xId = x.getId();
			System.out.println("Node "+xId);
			labelX.mark();
			listLabels[xId] = labelX;
			//System.out.println(heap.toStringTree());
			for (Arc arcXY : x.getSuccessors()) {
				// Small test to check allowed roads...
                if (!data.isAllowed(arcXY)) {
                    continue;
                }
				Node y = arcXY.getDestination();
				int yId = y.getId();
				Label labelY = listLabels[yId];
				if (!(labelY.isMarked())) {
					double wXY = data.getCost(arcXY);
					double oldDistance = labelY.getCost();
                    double newDistance = labelX.getCost()+wXY;

                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(y);
                    }
                    
					if (oldDistance > newDistance) {
						System.out.println("----Node "+yId+" : "+oldDistance + " par " + newDistance);
						labelY.setCost(newDistance);
						labelY.setParent(arcXY);
						listLabels[yId]=labelY;
						try {
							heap.remove(labelY);
							heap.insert(labelY);
						} catch (ElementNotFoundException e) {
							heap.insert(labelY);
						}
					}
				}
			}
		}
		
		// Création de la ShortestPathSolution
		
		ArrayList<Arc> arcs = new ArrayList<>();
		Node i = data.getDestination();
		Label labelI = listLabels[i.getId()];
		if (labelI.getParent()==null) {
			solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
        	// The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
            
        	while (i != s) {
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
		
		return solution;

	}
}
