package com.etc.algo;

import java.util.HashMap;
import java.util.Iterator;

import com.rs.bean.Rate;

public class UserBasedCF {
	private int userNum;
	private int ratingNum;
	private int itemNum;
	private double d;
	private double[] userVector; 
	private double[] itemVector;
	private double[] userTemp;
	private double[] itemTemp;
	private HashMap<Integer, Rate> ratingMatrix;
	private HashMap<Integer, HashMap<Integer, Double>> graph;
	private HashMap<Integer, HashMap<Integer, Double>> reversegraph;
	private HashMap<Integer, HashMap<Integer, Double>> weight;
	
	public UserBasedCF(int userNum, int ratingNum, int itemNum, 
			HashMap<Integer, Rate> ratingMatrix, HashMap<Integer, HashMap<Integer, Double>> graph,
			HashMap<Integer, HashMap<Integer, Double>> reversegraph) {
		this.userNum = userNum;
		this.ratingNum = ratingNum;
		this.itemNum = itemNum;
		this.d = 0.85;
		this.ratingMatrix = ratingMatrix;
		this.graph = graph;
		this.reversegraph = reversegraph;
		this.userVector = new double[userNum];
		this.itemVector = new double[itemNum];
		this.userTemp = new double[userNum];
		this.itemTemp = new double[itemNum];
		this.weight = new HashMap<Integer, HashMap<Integer, Double>>();
		
	}

	public void generateWeight() {
		int q = 0, p = 0;
	/*	Iterator iterator1 = graph.keySet().iterator();
		while (iterator1.hasNext()) {
			int nextuser = (int) iterator1.next();
			//System.out.println(nextuser);
			q++;
			p += graph.get(nextuser).size();
		}
		System.out.println(q);
		System.out.println(graph.size());
		System.out.println(p);
		System.out.println(graph.get(0).size());*/
		HashMap<Integer, HashMap<Integer, Double>> c = new HashMap<Integer, HashMap<Integer, Double>>();
		Iterator iterator1 = reversegraph.keySet().iterator();
		while (iterator1.hasNext()) {
			int nextitem = (int) iterator1.next();
			//int nextitem = 17863;
			System.out.println(nextitem);
			HashMap<Integer, Double> userMap = reversegraph.get(nextitem);
			Iterator iter1 = userMap.keySet().iterator();
			while (iter1.hasNext()) {
				int nextuser = (int) iter1.next();
				Iterator it1 = userMap.keySet().iterator();
				System.out.println("nextuser: " + nextuser);
				while (it1.hasNext()) {
					int compareuser = (int) it1.next();
					//System.out.println("nextuser: " + nextuser + ", cu: " + compareuser);
					if (compareuser == nextuser)
						continue;
					if (!c.containsKey(nextuser)) {
						HashMap<Integer, Double> t = new HashMap<Integer, Double>();
						double value = (1 / Math.log(1 + userMap.size()));
						t.put(compareuser, value);
						c.put(nextuser, t);
					}
					else {
						HashMap<Integer, Double> t = c.get(nextuser);
						if (!c.get(nextuser).containsKey(compareuser)) {
							double value = (1 / Math.log(1 + userMap.size()));
							t.put(compareuser, value);
						}
						else {
							double value = t.get(compareuser) + (1 / Math.log(1 + userMap.size()));
							t.put(compareuser, value);
						}
						c.put(nextuser, t);
					}
				}
			}
			System.out.println(nextitem);
		}
		
		Iterator iterator2 = c.keySet().iterator();
		while (iterator2.hasNext()) {
			int nextuser = (int) iterator2.next();
			HashMap<Integer, Double> user2user = c.get(iterator2.next());
			Iterator iter2 = user2user.keySet().iterator();
			while (iter2.hasNext()) {
				int compareuser = (int) iter2.next();
				if (!weight.containsKey(nextuser)) {
					HashMap<Integer, Double> u = new HashMap<Integer, Double>();
					double value = (user2user.get(compareuser))/(Math.sqrt(graph.get(nextuser).size() * graph.get(compareuser).size()));
					u.put(compareuser, value);
					weight.put(nextuser, u);
				}
				else {
					HashMap<Integer, Double> u = c.get(iter2.next());
					double value = (user2user.get(compareuser))/(Math.sqrt(graph.get(nextuser).size() * graph.get(compareuser).size()));
					u.put(compareuser, value);
					weight.put(nextuser, u);
				}
			}
			System.out.println(nextuser);
		}
		
	}
	public void getScore() {
		//sort and get the score with highest similarity, then calculate unknown scores
	}
}
