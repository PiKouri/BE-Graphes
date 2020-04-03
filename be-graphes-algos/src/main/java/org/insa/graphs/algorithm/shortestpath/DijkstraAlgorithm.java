package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
//import org.insa.graphs.model.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;

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

        // Initialize array of distances.
        double[] distances = new double[nbNodes];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[data.getOrigin().getId()] = 0;

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];

        // Actual algorithm, we will assume the graph does not contain negative
        // cycle...
        
        // Liste des labels
    	ArrayList<Label> listLabels = new ArrayList<Label>(nbNodes);
    	
    	// Tas binaire des noeuds marqués (stock les Ids)
    	BinaryHeap<Integer> binaryHeapMarked;
        
        boolean found = false;
        
        // Initialisation 
        
        double inf = Double.POSITIVE_INFINITY;
        for (Node node : nodes) {
        	listLabels.set(node.getId(), new Label(node, false, inf, null) );
        }
        Node s = data.getOrigin();
        listLabels.get(s.getId()).setCost(0);
        binaryHeapMarked.insert(s.getId()); // Le tas ne contient que le sommet d'origine initialement
        
        // Itérations
        
        for (int i = 0; !found && i < nbNodes; ++i) {
            found = true;
            for (Node node: graph.getNodes()) {
                for (Arc arc: node.getSuccessors()) {

                    // Small test to check allowed roads...
                    if (!data.isAllowed(arc)) {
                        continue;
                    }

                    // Retrieve weight of the arc.
                    double w = data.getCost(arc);
                    double oldDistance = distances[arc.getDestination().getId()];
                    double newDistance = distances[node.getId()] + w;

                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(arc.getDestination());
                    }

                    // Check if new distances would be better, if so update...
                    if (newDistance < oldDistance) {
                        found = false;
                        distances[arc.getDestination().getId()] = distances[node.getId()] + w;
                        predecessorArcs[arc.getDestination().getId()] = arc;
                    }
                }
            }
        return solution;
    }

}
