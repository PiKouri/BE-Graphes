package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.AbstractInputData.Mode;


public class TestPerformance {
	
	public static void main(String[] args) throws Exception{
		
		TestFile.createTestFile("haute-garonne", Mode.LENGTH, 2);
		ResultFile.createResultFile("haute-garonne_distance_2.txt");
		
	}
	
}
