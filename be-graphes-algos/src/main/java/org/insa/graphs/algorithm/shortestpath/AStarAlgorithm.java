package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.utils.LabelStar;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {
	
    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    /**
     * Create and return a new initial label on the given node
     * and implements additional information about the Destination Node, the chosen Mode (Distance or Time), and the Maximum Speed
     */
    @Override
    protected LabelStar createInitLabel (Node node) {
		double inf = Double.POSITIVE_INFINITY;
		ShortestPathData data = getInputData();
		int maximumSpeed = (data.getGraph().getGraphInformation() == null) ? -1 : data.getGraph().getGraphInformation().getMaximumSpeed();
		return new LabelStar(node, false, inf, null, data.getDestination(), data.getMode(), maximumSpeed);
	}

}
