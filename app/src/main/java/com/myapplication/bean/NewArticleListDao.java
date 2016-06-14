package com.myapplication.bean;

import java.util.List;

public class NewArticleListDao extends Entity{

	private int state;
	private String message;
	private List<NewsList> orders;
	private List<NewsList> carousels;
	private List<NewsList> normal;
	private List<NewsList> activity;
	private List<NewsList> newvote;

	public List<NewsList> getActivity() {
		return activity;
	}

	public void setActivity(List<NewsList> activity) {
		this.activity = activity;
	}

	public List<NewsList> getNewvote() {
		return newvote;
	}

	public void setNewvote(List<NewsList> newvote) {
		this.newvote = newvote;
	}

	public List<NewsList> getOrders() {
		return orders;
	}

	public void setOrders(List<NewsList> orders) {
		this.orders = orders;
	}

	public List<NewsList> getCarousels() {
		return carousels;
	}

	public void setCarousels(List<NewsList> carousels) {
		this.carousels = carousels;
	}

	public List<NewsList> getNormal() {
		return normal;
	}

	public void setNormal(List<NewsList> normal) {
		this.normal = normal;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
