package org.insa.graphs.model;

public class LabelStar extends Label implements Comparable<Label>{
	
	public LabelStar(Node currentNode, boolean marked, double cost, Arc parent) {super(currentNode, marked, cost, parent);}
	
	public double getEstimationCost() { return 0;};
	
	@Override
	public double getTotalCost() { return this.cost + this.getEstimationCost(); }
}
