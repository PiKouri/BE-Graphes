package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.*;

public class AStarTest extends AlgorithmTest {

	/**
	 * @return The ShortestPathSolution using AStar Algorithm. (Tested)
	 */
	protected ShortestPathSolution createSolution1() {
		return (new AStarAlgorithm(data)).run();
	}
	
	/**
	 * @return The ShortestPathSolution using Dijkstra Algorithm. (Reference)
	 */
	protected ShortestPathSolution createSolution2() {
		return (new DijkstraAlgorithm(data)).run();
	}
}
