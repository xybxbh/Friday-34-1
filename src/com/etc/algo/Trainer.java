package com.etc.algo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import com.rs.bean.Dataset;
import com.rs.bean.Rate;

public class Trainer {
	private double lamda;
	private double mu;
	private int rank;
	private int iteration;
	
	private Dataset dataset;
	private Dataset testset;
	private SVD svd;
	private UserBasedCF ubcf;
	
	public Trainer(double lamda, double mu, int rank, int iteration) {
		this.lamda = lamda;
		this.mu = mu;
		this.rank = rank;
		this.iteration = iteration;
	}
	
	public void getData(String filepath) throws IOException {
		int userNum = 0, ratingNum = 0, itemNum = 0;
		HashMap<Integer, Rate> rate = new HashMap<Integer, Rate>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath))));
		String line;
		int count = 0, rateid = 0, user = -1, item;
		double score;
		//graph
		HashMap<Integer, HashMap<Integer, Double>> graph = new HashMap<Integer, HashMap<Integer, Double>>();
		HashMap<Integer, HashMap<Integer, Double>> reversegraph = new HashMap<Integer, HashMap<Integer, Double>>();
		HashMap<Integer, Double> tempset = new HashMap<Integer, Double>();
		while(true) {
			line = br.readLine();
			if (line == null)
				break;
			if (count <= 0) {
				String[] fields = line.split("\\|");
				user = Integer.parseInt(fields[0]);
				count = Integer.parseInt(fields[1]);
				userNum = userNum < user ? user : userNum;
				ratingNum += count;
			}else {
				String[] fields = line.split("  ");
				item = Integer.parseInt(fields[0]);
				score = (double)(Double.parseDouble(fields[1])/20);
				rate.put(rateid, new Rate(rateid, user, item, score));
				//graph
				if (!graph.containsKey(user)) {
					HashMap<Integer, Double> t = new HashMap<Integer, Double>();
					t.put(item, score);
					graph.put(user, t);
				}
				else {
					HashMap<Integer, Double> t = graph.get(user);
					t.put(item, score);
					graph.put(user, t);
				}
				if (!reversegraph.containsKey(item)) {
					HashMap<Integer, Double> t = new HashMap<Integer, Double>();
					t.put(user, score);
					reversegraph.put(item, t);
				}
				else {
					HashMap<Integer, Double> t = reversegraph.get(item);
					t.put(user, score);
					reversegraph.put(item, t);
				}
				itemNum = itemNum < item ? item : itemNum;
				rateid++;
				count--;
			}
		}
		br.close();
		dataset = new Dataset(userNum + 1, ratingNum, itemNum + 1, rate, graph, reversegraph);
	}
	
	public void train() {
		double rmse;
		svd = new SVD(lamda, mu, dataset.getUserNum(), dataset.getRatingNum(), dataset.getItemNum(),
				rank, 5, 0, dataset.getRate(), testset.getRate(), dataset.getGraph());
		svd.init();
		svd.mean1();
		for (int i = 0; i < iteration; i++) {
			rmse = svd.gradientDiscent1();
			System.out.println("iter: " + i + ", rmse: " + rmse);
		}
		svd.test();
		HashMap<Integer, Rate> testMatrix = svd.getTestMatrix();
		Iterator iterator = testMatrix.keySet().iterator();
		while (iterator.hasNext()) {
			Rate rate = testMatrix.get(iterator.next());
			//if (rate.getUser() == 1)
			System.out.println("user: " + rate.getUser() + ", item: " + rate.getItem() + ", score: " + rate.getScore());
		}
		/*
		ubcf = new UserBasedCF(dataset.getUserNum(), dataset.getRatingNum(), dataset.getItemNum(), 
				dataset.getRate(), dataset.getGraph(), dataset.getReverseGraph());
		ubcf.generateWeight();*/
	}
	
	public void getTest(String filepath) throws IOException {
		int userNum = 0, ratingNum = 0, itemNum = 0;
		HashMap<Integer, Rate> rate = new HashMap<Integer, Rate>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath))));
		String line;
		int count = 0, rateid = 0, user = -1, item;
		double score;
		//graph
		HashMap<Integer, HashMap<Integer, Double>> graph = new HashMap<Integer, HashMap<Integer, Double>>();
		HashMap<Integer, HashMap<Integer, Double>> reversegraph = new HashMap<Integer, HashMap<Integer, Double>>();
		HashMap<Integer, Double> tempset = new HashMap<Integer, Double>();
		while(true) {
			line = br.readLine();
			if (line == null)
				break;
			if (count <= 0) {
				String[] fields = line.split("\\|");
				user = Integer.parseInt(fields[0]);
				count = Integer.parseInt(fields[1]);
				userNum = userNum < user ? user : userNum;
				ratingNum += count;
			}else {
				String[] fields = line.split("  ");
				item = Integer.parseInt(fields[0]);
				score = 0.0;
				rate.put(rateid, new Rate(rateid, user, item, score));
				
				itemNum = itemNum < item ? item : itemNum;
				rateid++;
				count--;
			}
		}
		br.close();
		testset = new Dataset(userNum + 1, ratingNum, itemNum + 1, rate, graph, reversegraph);
	}
	
	public static void main(String[] args) throws IOException {
		Trainer trainer = new Trainer(0.1 , 0.05, 300, 30);
		trainer.getData("G:/data-new/train.txt");
		trainer.getTest("G:/data-new/test.txt");
		System.out.println(trainer.dataset.getUserNum());
		System.out.println(trainer.dataset.getRatingNum());
		System.out.println(trainer.dataset.getItemNum());
		trainer.train();
	}
	
}
