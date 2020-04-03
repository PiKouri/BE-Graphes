package org.insa.graphs.model;
// import java.util.ArrayList;

public class Label implements Comparable<Label> {
	
	/**
     * Create a new label on the given node and add it to the list of Labels.
     * 
     * @param currentNode Node to label.
     * @param marked Is true if the shortest path is known for this node.
     * @param cost Current cost of the shortest path from the origin to this node.
     * @param parent Previous Node in the current shortest path (Arc).
     */
	public Label(Node currentNode, boolean marked, double cost, Arc parent) {
		this.currentNode=currentNode;
		this.marked=marked;
		this.cost=cost;
		this.parent=parent;
	}
	
	// Sommet courant : sommet associé à ce label (sommet ou numéro de sommet).
	private Node currentNode;
	
	// Marque : booléen, vrai lorsque le coût min de ce sommet est définitivement connu par l'algorithme.
	private boolean marked;
	
	// Coût : valeur courante du plus court chemin depuis l'origine vers le sommet.
	private double cost;
	
	// Père : correspond au sommet précédent sur le chemin correspondant au plus court chemin courant. Afin de reconstruire le chemin à la fin de l'algorithme, mieux vaut stocker l'arc plutôt que seulement le père.
	private Arc parent;
	
	/**
     * @return The current labeled node.
    */
	public Node getCurrentNode() { return this.currentNode; }
	
	/**
     * @return True if the minimum cost is truly known
    */
	public boolean isMarked() { return this.marked; }
	
	/**
     * @return The cost of the current shortest path from the origin to this node.
    */
	public double getCost() { return this.cost; }

	/**
     * @return The previous node in the current shortest path.
    */
	public Arc getParent() { return this.parent; }	
	
	/**
	 * Change the current labeled node as marked
	 */
	public void mark() { this.marked = true; }
	
	/**
	 * Set the cost
	 */
	public void setCost(double newCost) { this.cost = newCost; }
	
	/**
	 * Set the parent
	 */
	public void setParent(Arc newParent) { this.parent = newParent; }
	
	public int compareTo(Label other) {
        return Double.compare(this.getCost(), other.getCost());		
	}
}
