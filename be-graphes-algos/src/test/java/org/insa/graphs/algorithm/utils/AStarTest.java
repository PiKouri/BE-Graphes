package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;

public class AStarTest extends DijkstraTest {

	/**
	 * @return The ShortestPathSolution using AStar Algorithm.
	 */
	protected ShortestPathSolution createSolution() {
		return (new AStarAlgorithm(data)).run();
	}
}
