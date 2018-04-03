package com.shubu.kmitlbike.data.model.bike;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class RouteLineItem{

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("longitude")
	private double longitude;

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"RouteLineItem{" + 
			"latitude = '" + latitude + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}