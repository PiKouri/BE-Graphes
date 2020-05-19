package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
//import java.util.Arrays;
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
		
		Node s = data.getOrigin();
		Node d = data.getDestination();

		// Verification du cas simple : Graphe vide
		if (graph.getNodes().size() == 0) return new ShortestPathSolution(data, Status.INFEASIBLE);
		
		// Verification du cas simple : Origine = destination
		if (s == d) return new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, new ArrayList<Arc>())); 
		
		final int nbNodes = graph.size();

		final List<Node> nodes = graph.getNodes();

		// Liste des labels
		// ArrayList<Label> listLabels = new ArrayList<Label>(nbNodes);
		Label[] listLabels = createLabelList(nbNodes);
		
		// File de priorité
		BinaryHeap<Label> heap = createLabelHeap();
		
		// Initialisation 

		int nbIterations = 0;
		double inf = Double.POSITIVE_INFINITY;
		for (Node node : nodes) {
			listLabels[node.getId()] = new Label(node, false, inf, null);
		}
		listLabels[s.getId()].setCost(0);
		heap.insert(listLabels[s.getId()]); 
		// Le tas ne contient que le sommet d'origine initialement
		
		
		// Notify observers about the first event (origin processed).
        notifyOriginProcessed(s);

		// Itérations

		while (!(heap.isEmpty())) {
			// Incrémentation du compteur d'itérations
			nbIterations++;
			
			Node x = heap.deleteMin().getCurrentNode();
			
			//Affichage de la validité du tas
			 if(heap.isValid()) System.out.println("Tas valide");
			else {
				System.out.println("Tas invalide -> Arrêt de l'algorithme");
				break;
			} 
			
			listLabels[x.getId()].mark();
			// Notify observers that node x is marked.
			notifyNodeMarked(x);
			
			// On arrête lorsque le node destination est marqué
			if (x == d) break;
			
			int nbSuccesseursExplores = 0;
			
			for (Arc arcXY : x.getSuccessors()) {
				
				// Vérification du mode de transport
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
			
			// Affichage du nombre de successeurs explorés comparé au nombre total de successeurs
			System.out.println("Node "+x.getId()+": "+nbSuccesseursExplores+"/"+x.getNumberOfSuccessors()+" Successeurs explorés");
			
			// Coût des labels marqués croissant au cours des itérations
			System.out.println("Cost: " + listLabels[x.getId()].getCost()); 
		}
		
		// Création de la ShortestPathSolution
		
		ArrayList<Arc> arcs = new ArrayList<>();
		Node i = d;
		Label labelI = listLabels[i.getId()];
		if (labelI.getParent()==null) {
			solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
        	// The destination has been found, notify the observers.
            notifyDestinationReached(d);
            
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
		// Nombre d'arcs du plus court chemin et nombre d'itérations de l'algorithme
		System.out.println("Nombre d'arcs du PCC : " + arcs.size());
		System.out.println("Nombre d'itérations de l'algorithme : " +nbIterations);
		
		return solution;

	}
	
	protected Label[] createLabelList (int nbNodes) {return new Label[nbNodes];}
	protected BinaryHeap<Label> createLabelHeap() {return new BinaryHeap<Label>();}
}
