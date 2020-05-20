package org.insa.graphs.algorithm.utils;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Point;
import org.insa.graphs.algorithm.AbstractInputData.Mode;


public class LabelStar extends Label{
	
	private final double estimatedCost;
	
	private final Node destinationNode;
	
	private final Mode mode;
	
	private final int maxSpeed;
	
	/**
     * Create a new label on the given node and add it to the list of Labels.
     * 
     * @param currentNode Node to label.
     * @param marked Is true if the shortest path is known for this node.
     * @param cost Current cost of the shortest path from the origin to this node.
     * @param parent Previous Node in the current shortest path (Arc).
     * @param destinationNode Destination node to reach
     * @param mode Chosen mode : distance or time
     * @param maxSpeed Maximum speed allowed in the graph
     */
	public LabelStar(Node currentNode, boolean marked, double cost, Arc parent, Node destinationNode, Mode mode, int maxSpeed) {
		super(currentNode, marked, cost, parent);
		this.destinationNode = destinationNode;
		this.mode = mode;
		this.maxSpeed = maxSpeed;
		this.estimatedCost = getEstimationCost();
	}
	
	/**
     * @return The estimated cost between current node and destination node (depending on time or distance).
    */
	private double getEstimationCost() { 
		return (mode == Mode.LENGTH) ? this.getEstimatedDistance() : this.getEstimatedTime();
	};
	
	/**
     * @return The estimated distance between current node and destination node.
    */
	private double getEstimatedDistance() {
		if (this.getCurrentNode().getPoint() != null && this.destinationNode.getPoint() != null)
		return Point.distance(this.getCurrentNode().getPoint(), this.destinationNode.getPoint());
		// If the Node has no linked point : use Dijkstra Algorithm (Labels)
		else return 0;
	}
	
	/**
     * @return The estimated time needed between current node and destination node.
    */
	private double getEstimatedTime() {
		return (this.maxSpeed!=-1) ? this.getEstimatedDistance()/this.maxSpeed : this.getEstimatedDistance();
	}
	
	@Override
	public double getTotalCost() { return this.cost + this.estimatedCost; }
}
