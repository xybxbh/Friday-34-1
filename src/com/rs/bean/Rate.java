package com.rs.bean;

public class Rate {
	private int rateid;
	private int user;
	private int item;
	private double score;
	
	public Rate(int rateid, int user, int item, double score) {
		this.rateid = rateid;
		this.user = user;
		this.item = item;
		this.score = score;
	}
	
	public int getRateid() {
		return rateid;
	}
	
	public int getUser() {
		return user;
	}
	
	public int getItem() {
		return item;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}

}
