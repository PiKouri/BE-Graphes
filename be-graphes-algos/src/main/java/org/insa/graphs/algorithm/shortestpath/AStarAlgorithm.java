package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.LabelStar;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    @Override
    protected Label[] createLabelList (int nbNodes) {return new LabelStar[nbNodes];}
    
    @Override
	protected BinaryHeap<LabelStar> createLabelHeap() {return new BinaryHeap<LabelStar>();}

}
