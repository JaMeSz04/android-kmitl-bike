package com.shubu.kmitlbike.data.model.bike;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Location{

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

	public void setLongitude(double longtitude){
		this.longitude = longtitude;
	}

	public double getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"Location{" +
			",latitude = '" + latitude + '\'' + 
			",longtitude = '" + longitude + '\'' +
			"}";
		}
}