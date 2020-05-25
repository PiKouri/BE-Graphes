package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.*;


public class DijkstraTest extends AlgorithmTest{

	/**
	 * @return The ShortestPathSolution using Dijkstra Algorithm. (Tested)
	 */
	protected ShortestPathSolution createSolution1() {
		return (new DijkstraAlgorithm(data)).run();
	}
	
	/**
	 * @return The ShortestPathSolution using BellmanFord Algorithm. (Reference)
	 */
	protected ShortestPathSolution createSolution2() {
		return (new BellmanFordAlgorithm(data)).run();
	}
}
