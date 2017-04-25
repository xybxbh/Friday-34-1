package com.rs.bean;

import java.util.HashMap;

public class Dataset {
	private int userNum;
	private int ratingNum;
	private int itemNum;
	//private LinkedList<Rate> rate;
	//private Iterator<Rate> iterator;
	private HashMap<Integer, Rate> rate;
	private HashMap<Integer, HashMap<Integer, Double>> graph;
	private HashMap<Integer, HashMap<Integer, Double>> reversegraph;
	
	public Dataset(int userNum, int ratingNum, int itemNum, HashMap<Integer, Rate> rate, 
			HashMap<Integer, HashMap<Integer, Double>> graph, 
			HashMap<Integer, HashMap<Integer, Double>> reversegraph) {
		this.userNum = userNum;
		this.ratingNum = ratingNum;
		this.itemNum = itemNum;
		this.rate = rate;
		this.graph = graph;
		this.reversegraph = reversegraph;
	}
	
	public int getUserNum() {
		return userNum;
	}
	
	public int getRatingNum() {
		return ratingNum;
	}
	
	public int getItemNum() {
		return itemNum;
	}
	
	public HashMap<Integer, Rate> getRate() {
		return rate;
	}
	
	public HashMap<Integer, HashMap<Integer, Double>> getGraph() {
		return graph;
	}
	
	public HashMap<Integer, HashMap<Integer, Double>> getReverseGraph() {
		return reversegraph;
	}

}
