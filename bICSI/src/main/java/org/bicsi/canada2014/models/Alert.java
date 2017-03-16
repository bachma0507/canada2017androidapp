package org.bicsi.canada2014.models;

import java.util.Date;

import com.parse.ParseObject;

public class Alert  {
	
	public String objectId;
	public Date createdAt;
	public String text;
	public Date Updatedat;
	public Alert(){
		
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getUpdatedat() {
		return Updatedat;
	}
	public void setUpdatedat(Date Updatedat) {
		this.Updatedat = Updatedat;
	}
//	public String getDate() {
//		return date;
//	}
//	public void setDate(String date) {
//		this.date = date;
//	}
	
	public String getobjectId() {
		return objectId;
	}
	public void setobjectId(String objectId) {
		this.objectId = objectId;
	}
	public Date getcreatedAt() {
		return createdAt;
	}
	public void setCreatedat(Date createdAt) {
		this.createdAt = createdAt;
	}

}
