package com.shubu.kmitlbike.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import com.shubu.kmitlbike.data.model.bike.Bike;

@Generated("com.robohorse.robopojogenerator")
public class ProfileHistory{

	@SerializedName("duration")
	private int duration;

	@SerializedName("selected_plan")
	private SelectedPlan selectedPlan;

	@SerializedName("distance")
	private String distance;

	@SerializedName("timestamps")
	private Timestamps timestamps;

	@SerializedName("id")
	private int id;

	@SerializedName("bike")
	private Bike bike;

	public void setDuration(int duration){
		this.duration = duration;
	}

	public int getDuration(){
		return duration;
	}

	public void setSelectedPlan(SelectedPlan selectedPlan){
		this.selectedPlan = selectedPlan;
	}

	public SelectedPlan getSelectedPlan(){
		return selectedPlan;
	}

	public void setDistance(String distance){
		this.distance = distance;
	}

	public String getDistance(){
		return distance;
	}

	public void setTimestamps(Timestamps timestamps){
		this.timestamps = timestamps;
	}

	public Timestamps getTimestamps(){
		return timestamps;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setBike(Bike bike){
		this.bike = bike;
	}

	public Bike getBike(){
		return bike;
	}

	@Override
 	public String toString(){
		return 
			"ProfileHistory{" + 
			"duration = '" + duration + '\'' + 
			",selected_plan = '" + selectedPlan + '\'' + 
			",distance = '" + distance + '\'' + 
			",timestamps = '" + timestamps + '\'' + 
			",id = '" + id + '\'' + 
			",bike = '" + bike + '\'' + 
			"}";
		}
}