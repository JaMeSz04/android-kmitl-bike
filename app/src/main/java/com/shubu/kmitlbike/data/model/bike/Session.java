package com.shubu.kmitlbike.data.model.bike;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import com.shubu.kmitlbike.data.model.Timestamps;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.bike.RouteLineItem;

@Generated("com.robohorse.robopojogenerator")
public class Session {

	@SerializedName("duration")
	private int duration;

	@SerializedName("selected_plan")
	private UsagePlan selectedPlan;

	@SerializedName("distance")
	private String distance;

	@SerializedName("timestamps")
	private Timestamps timestamps;

	@SerializedName("route_line")
	private List<RouteLineItem> routeLine;

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

	public void setSelectedPlan(UsagePlan selectedPlan){
		this.selectedPlan = selectedPlan;
	}

	public UsagePlan getSelectedPlan(){
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

	public void setRouteLine(List<RouteLineItem> routeLine){
		this.routeLine = routeLine;
	}

	public List<RouteLineItem> getRouteLine(){
		return routeLine;
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
			"Session{" + 
			"duration = '" + duration + '\'' + 
			",selected_plan = '" + selectedPlan + '\'' + 
			",distance = '" + distance + '\'' + 
			",timestamps = '" + timestamps + '\'' + 
			",route_line = '" + routeLine + '\'' + 
			",id = '" + id + '\'' + 
			",bike = '" + bike + '\'' + 
			"}";
		}
}