package org.bicsi.canada2014.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AlertModel implements Serializable {

	public String post;
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public double getLastmoddate() {
		return lastmoddate;
	}
	public void setLastmoddate(double lastmoddate) {
		this.lastmoddate = lastmoddate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String alerts_id;
	public String getAlerts_id() {
		return alerts_id;
	}
	public void setAlerts_id(String alerts_id) {
		this.alerts_id = alerts_id;
	}
	public double getCreateddate() {
		return createddate;
	}
	public void setCreateddate(double createddate) {
		this.createddate = createddate;
	}
	public double lastmoddate;
	public String date;
	
	public double createddate;
}