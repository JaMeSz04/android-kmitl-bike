package com.shubu.kmitlbike.data.model.bike;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class BikeBorrowResponse{

	@SerializedName("session")
	private Session session;

	@SerializedName("message")
	private String message;

	@SerializedName("point")
	private int point;

	public void setSession(Session session){
		this.session = session;
	}

	public Session getSession(){
		return session;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setPoint(int point){
		this.point = point;
	}

	public int getPoint(){
		return point;
	}

	@Override
 	public String toString(){
		return 
			"BikeBorrowResponse{" + 
			"session = '" + session + '\'' + 
			",message = '" + message + '\'' + 
			",point = '" + point + '\'' + 
			"}";
		}
}