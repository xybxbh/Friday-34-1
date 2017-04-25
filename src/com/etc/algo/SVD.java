package com.etc.algo;

import java.util.HashMap;
import java.util.Iterator;

import com.rs.bean.Rate;

public class SVD {
	private double lamda;
	private double mu;
	private int userNum;
	private int ratingNum;
	private int itemNum;
	private int rank;
	private double maxScore;
	private double minScore;
	
	private double[][] userMatrix;
	private double[][] itemMatrix;
	private double[] userBias;
	private double[] itemBias;
	private int[] userBiasCount;
	private int[] itemBiasCount;
	private HashMap<Integer, Rate> ratingMatrix;
	private HashMap<Integer, Rate> testMatrix;
	private double mean;
	HashMap<Integer, HashMap<Integer, Double>> graph;
	
	public SVD(double lamda, double mu, int userNum, int ratingNum, int itemNum,
			int rank, double maxScore, double minScore, HashMap<Integer, Rate> ratingMatrix,
			HashMap<Integer, Rate> testMatrix, HashMap<Integer, HashMap<Integer, Double>> graph) {
		this.lamda = lamda;
		this.mu = mu;
		this.userNum = userNum;
		this.ratingNum = ratingNum;
		this.itemNum = itemNum;
		this.rank = rank;
		this.maxScore = maxScore;
		this.minScore = minScore;
		this.ratingMatrix = ratingMatrix;
		this.testMatrix = testMatrix;
		this.graph = graph;
		/*
		this.userMatrix = new HashMap<Integer, Double>();
		this.itemMatrix = new HashMap<Integer, Double>();
		this.userBias = new HashMap<Integer, Double>();
		this.itemBias = new HashMap<Integer, Double>();
		this.userBiasCount = new HashMap<Integer, Integer>();
		this.itemBiasCount = new HashMap<Integer, Integer>();*/
		
		this.userMatrix = new double[userNum][rank];
		this.itemMatrix = new double[itemNum][rank];
		this.userBias = new double[userNum];
		this.itemBias = new double[itemNum];
		this.userBiasCount = new int[userNum];
		this.itemBiasCount = new int[itemNum];
		
	}
	
	public void init() {
		for (int j = 0; j < userNum; j++) {
			for (int i = 0; i < rank; i++) {
				//userMatrix.put(j*rank + i, Math.random()*(Math.sqrt((maxScore - minScore)/rank)));
				//userMatrix.put(j*rank + i, Math.random());
				userMatrix[j][i] = Math.random()*(Math.sqrt((maxScore - minScore)/rank));
				//userMatrix[j][i] = Math.random()*(maxScore - minScore);
			}
		}
		for (int j = 0; j < itemNum; j++) {
			for (int i = 0; i < rank; i++) {
				//itemMatrix.put(j*rank + i, Math.random()*(Math.sqrt((maxScore - minScore)/rank)));
				//itemMatrix.put(j*rank + i, Math.random());
				itemMatrix[j][i] = Math.random()*(Math.sqrt((maxScore - minScore)/rank));
				//itemMatrix[j][i] = Math.random()*(maxScore - minScore);
			}
		}
	}
	/*
	public void mean() {
		double stat = 0.0;
		int c = 0;
		Iterator iterator = ratingMatrix.keySet().iterator();
		while (iterator.hasNext()) {
			Rate rate = ratingMatrix.get(iterator.next());
			double score = rate.getScore();
			stat += score;
			c++;
		}
		mean = stat/c;
		
		Iterator iterator1 = ratingMatrix.keySet().iterator();
		while (iterator1.hasNext()) {
			Rate rate = ratingMatrix.get(iterator1.next());
			int i = rate.getUser();
			int j = rate.getItem();
			double score = rate.getScore();
			if (itemBias.containsKey(j)) {
				double tempi = itemBias.get(j) + score - mean;
				itemBias.put(j, tempi);
				int tempic = itemBiasCount.get(j) + 1;
				itemBiasCount.put(j, tempic);
			}
			else {
				itemBias.put(j, score - mean);
				itemBiasCount.put(j, 1);
			}
		}
		for (int j = 0; j < itemNum; j++) {
			if (itemBias.containsKey(j)) {
				double bi = itemBias.get(j)/itemBiasCount.get(j);
				itemBias.put(j, bi);
			}
			else {
				itemBias.put(j, 0.0);
			}
		}
		
		Iterator iterator2 = ratingMatrix.keySet().iterator();
		while (iterator2.hasNext()) {
			Rate rate = ratingMatrix.get(iterator2.next());
			int i = rate.getUser();
			int j = rate.getItem();
			double score = rate.getScore();
			if (userBias.containsKey(i)) {
				double tempu = userBias.get(i) + score - mean - itemBias.get(j);
				userBias.put(i, tempu);
				int tempuc = userBiasCount.get(i) + 1;
				userBiasCount.put(i, tempuc);
			}
			else {
				userBias.put(i, score - mean - itemBias.get(j));
				userBiasCount.put(i, 1);
			}
		}
		for (int i = 0; i < userNum; i++) {
			if (userBias.containsKey(i)) {
				double bu = userBias.get(i)/userBiasCount.get(i);
				userBias.put(i, bu);
			}
			else {
				userBias.put(i, 0.0);
			}
		}
	}*/
	
	public void mean1() {
		double stat = 0.0;
		int c = 0;
		Iterator iterator = ratingMatrix.keySet().iterator();
		while (iterator.hasNext()) {
			Rate rate = ratingMatrix.get(iterator.next());
			double score = rate.getScore();
			stat += score;
			c++;
		}
		mean = stat/c;
		
		Iterator iterator1 = ratingMatrix.keySet().iterator();
		while (iterator1.hasNext()) {
			Rate rate = ratingMatrix.get(iterator1.next());
			int i = rate.getUser();
			int j = rate.getItem();
			double score = rate.getScore();
			itemBias[j] += score - mean;
			itemBiasCount[j] += 1;
		}
		for (int j = 0; j < itemNum; j++) {
			if (itemBiasCount[j] != 0) {
				itemBias[j] = itemBias[j]/itemBiasCount[j];
			}
			else {
				itemBias[j] = 0.0;
			}
		}
		
		Iterator iterator2 = ratingMatrix.keySet().iterator();
		while (iterator2.hasNext()) {
			Rate rate = ratingMatrix.get(iterator2.next());
			int i = rate.getUser();
			int j = rate.getItem();
			double score = rate.getScore();
			userBias[i] += score - mean - itemBias[j];
			userBiasCount[i] += 1;
		}
		for (int i = 0; i < userNum; i++) {
			if (userBiasCount[i] != 0) {
				userBias[i] = userBias[i]/userBiasCount[i];
			}
			else {
				userBias[i] = 0.0;
			}
		}
	}
	/*1
	public double gradientDiscent() {
		double SSE = 0;
		int c = 0;/*
		for (int j = 0; j < userNum; j++) {
			for (int i = 0; i < rank; i++) {
				nablaUser.put(j*rank + i, 0.0);
			}
		}
		for (int j = 0; j < itemNum; j++) {
			for (int i = 0; i < rank; i++) {
				nablaItem.put(j*rank + i, 0.0);
			}
			//System.out.println(j);
		}*/
		
/*		for (int i = 0; i < userNum; i++) {
			for (int j = 0; j < itemNum; j++) {
				if (ratingMatrix.containsKey(i*itemNum + j)) {
					double preRating = 0;
					for (int k = 0; k < rank; k++)
						preRating += userMatrix.get(i*rank + k)*itemMatrix.get(j*rank + k);
					if (preRating > maxScore - minScore)
						preRating = maxScore;
					else if (preRating < 0)
						preRating = minScore;
					else
						preRating += minScore;
					double err = ratingMatrix.get(i*itemNum + j) - preRating;
					SSE += err*err;
					c++;
					for (int k = 0; k < rank; k++) {
						if (!nablaUser.containsKey(i*rank + k))
							nablaUser.put(i*rank + k, err*itemMatrix.get(j*rank + k) - lamda*userMatrix.get(i*rank + k));
						else {
							double nu = nablaUser.get(i*rank + k) + err*itemMatrix.get(j*rank + k) - lamda*userMatrix.get(i*rank + k);
							nablaUser.put(i*rank + k, nu);
						}
						if (!nablaItem.containsKey(j*rank + k))
							nablaItem.put(j*rank + k, err*userMatrix.get(i*rank + k) - lamda*itemMatrix.get(j*rank + k));
						else {
							double ni = nablaItem.get(j*rank + k) + err*userMatrix.get(i*rank + k) - lamda*itemMatrix.get(j*rank + k);
							nablaItem.put(j*rank + k, ni);
						}
					}
				}
			}
		}
		*/
	/*2	Iterator iterator = ratingMatrix.keySet().iterator();
		while (iterator.hasNext()) {
			Rate rate = ratingMatrix.get(iterator.next());
			int i = rate.getUser();
			int j = rate.getItem();
			double score = rate.getScore();
			double preRating = mean + userBias.get(i) + itemBias.get(j);
			for (int k = 0; k < rank; k++){
				if (!itemMatrix.containsKey(j*rank + k))
					{System.out.println(j);
					System.out.println(k);
					}
				preRating += userMatrix.get(i*rank + k)*itemMatrix.get(j*rank + k);}
			if (preRating > maxScore - minScore)
				preRating = maxScore;
			else if (preRating < 0)
				preRating = minScore;
			else
				preRating += minScore;
			double err = score - preRating;
			SSE += err*err;
			c++;
	/*		for (int k = 0; k < rank; k++) {
				if (!nablaUser.containsKey(i*rank + k))
					nablaUser.put(i*rank + k, err*itemMatrix.get(j*rank + k) - lamda*userMatrix.get(i*rank + k));
				else {
					double nu = nablaUser.get(i*rank + k) + err*itemMatrix.get(j*rank + k) - lamda*userMatrix.get(i*rank + k);
					nablaUser.put(i*rank + k, nu);
				}
				if (!nablaItem.containsKey(j*rank + k))
					nablaItem.put(j*rank + k, err*userMatrix.get(i*rank + k) - lamda*itemMatrix.get(j*rank + k));
				else {
					double ni = nablaItem.get(j*rank + k) + err*userMatrix.get(i*rank + k) - lamda*itemMatrix.get(j*rank + k);
					nablaItem.put(j*rank + k, ni);
				}
			}*/
	/*3		double bu = userBias.get(i) + mu*(err - lamda*userBias.get(i));
			userBias.put(i, bu);
			double bi = itemBias.get(j) + mu*(err - lamda*itemBias.get(j));
			itemBias.put(j, bi);
			System.out.println("before");
			for (int k = 0; k < rank; k++) {
				double tempu = userMatrix.get(i*rank + k) + mu*(err*itemMatrix.get(j*rank + k) - lamda*userMatrix.get(i*rank + k));
				userMatrix.put(j*rank + k, tempu);
				double tempi = itemMatrix.get(j*rank + k) + mu*(err*userMatrix.get(i*rank + k) - lamda*itemMatrix.get(j*rank + k));
				itemMatrix.put(j*rank + k, tempi);
			}
			System.out.println("after");
		}
	/*	for (int i = 0; i < userNum; i++) {
			for (int k = 0; k < rank; k++) {
				double temp = userMatrix.get(i*rank + k) + mu*nablaUser.get(i*rank + k);
				userMatrix.put(i*rank + k, temp);
			}
		}
		for (int j = 0; j < itemNum; j++) {
			for (int k = 0; k < rank; k++) {
				double temp = itemMatrix.get(j*rank + k) + mu*nablaItem.get(j*rank + k);
				itemMatrix.put(j*rank + k, temp);
			}
		}*/
		//mu = mu * 0.99;
/*		return Math.sqrt(SSE/c);
	}*/
	
	public double gradientDiscent1() {
		double SSE = 0;
		int c = 0;
	/*	Iterator iterator = ratingMatrix.keySet().iterator();
		while (iterator.hasNext()) {
			Rate rate = ratingMatrix.get(iterator.next());
			int i = rate.getUser();
			int j = rate.getItem();
			double score = rate.getScore();
			double preRating = mean + userBias.get(i) + itemBias.get(j);
			for (int k = 0; k < rank; k++){
				if (!itemMatrix.containsKey(j*rank + k))
					{System.out.println(j);
					System.out.println(k);
					}
				preRating += userMatrix.get(i*rank + k)*itemMatrix.get(j*rank + k);}
			if (preRating > maxScore - minScore)
				preRating = maxScore;
			else if (preRating < 0)
				preRating = minScore;
			else
				preRating += minScore;
			double err = score - preRating;
			SSE += err*err;
			c++;
	
			double bu = userBias.get(i) + mu*(err - lamda*userBias.get(i));
			userBias.put(i, bu);
			double bi = itemBias.get(j) + mu*(err - lamda*itemBias.get(j));
			itemBias.put(j, bi);
			
			for (int k = 0; k < rank; k++) {
				double tempu = userMatrix.get(i*rank + k) + mu*(err*itemMatrix.get(j*rank + k) - lamda*userMatrix.get(i*rank + k));
				userMatrix.put(j*rank + k, tempu);
				double tempi = itemMatrix.get(j*rank + k) + mu*(err*userMatrix.get(i*rank + k) - lamda*itemMatrix.get(j*rank + k));
				itemMatrix.put(j*rank + k, tempi);
			}
		}*/
		Iterator iterator = ratingMatrix.keySet().iterator();
		while (iterator.hasNext()) {
			Rate rate = ratingMatrix.get(iterator.next());
			int i = rate.getUser();
			int j = rate.getItem();
			double score = rate.getScore();
			double preRating = mean + userBias[i] + itemBias[j];
			for (int k = 0; k < rank; k++){
				if (itemMatrix[j][k] == 0)
					{System.out.println(j);
					System.out.println(k);
					}
				preRating += userMatrix[i][k]*itemMatrix[j][k];}
			if (preRating > maxScore - minScore)
				preRating = maxScore;
			else if (preRating < 0)
				preRating = minScore;
			else
				preRating += minScore;
			double err = score - preRating;
			SSE += err*err;
			c++;
				
			userBias[i] += mu*(err - lamda*userBias[i]);
			itemBias[j] += mu*(err - lamda*itemBias[j]);
				
			for (int k = 0; k < rank; k++) {
				userMatrix[i][k] += mu*(err*itemMatrix[j][k] - lamda*userMatrix[i][k]);
				itemMatrix[j][k] += mu*(err*userMatrix[i][k] - lamda*itemMatrix[j][k]);
			}
		}
		mu = mu * 0.99;
		return Math.sqrt(SSE/c);
	}
	
	public void test() {
		Iterator iterator = testMatrix.keySet().iterator();
		while (iterator.hasNext()) {
			Rate rate = testMatrix.get(iterator.next());
			int rateid = rate.getRateid();
			int i = rate.getUser();
			int j = rate.getItem();
			double score = rate.getScore() + mean + userBias[i] + itemBias[j];
			for (int k = 0; k < rank; k++){
				if (itemMatrix[j][k] == 0)
					{System.out.println(j);
					System.out.println(k);
					}
				score += userMatrix[i][k]*itemMatrix[j][k];}
			if (score > maxScore - minScore)
				score = maxScore;
			else if (score < 0)
				score = minScore;
			else
				score += minScore;
			rate.setScore(score);
			testMatrix.put(rateid, rate);
		}
	}
	
	public HashMap<Integer, Rate> getTestMatrix() {
		return testMatrix;
	}
}
