package com.shubu.kmitlbike.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class UsagePlan{

	@SerializedName("period")
	private int period;

	@SerializedName("price")
	private int price;

	@SerializedName("id")
	private int id;

	@SerializedName("plan_name")
	private String planName;

	public void setPeriod(int period){
		this.period = period;
	}

	public int getPeriod(){
		return period;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPlanName(String planName){
		this.planName = planName;
	}

	public String getPlanName(){
		return planName;
	}

	@Override
 	public String toString(){
		return 
			"UsagePlan{" + 
			"period = '" + period + '\'' + 
			",price = '" + price + '\'' + 
			",id = '" + id + '\'' + 
			",plan_name = '" + planName + '\'' + 
			"}";
		}
}