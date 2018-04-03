package com.shubu.kmitlbike.data.model.bike;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class BikeBorrowRequest{

	@SerializedName("selected_plan")
	private int selectedPlan;

	@SerializedName("location")
	private Location location;

	@SerializedName("nonce")
	private int nonce;

	public void setSelectedPlan(int selectedPlan){
		this.selectedPlan = selectedPlan;
	}

	public int getSelectedPlan(){
		return selectedPlan;
	}

	public void setLocation(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return location;
	}

	public void setNonce(int nonce){
		this.nonce = nonce;
	}

	public int getNonce(){
		return nonce;
	}

	@Override
 	public String toString(){
		return 
			"BikeBorrowRequest{" + 
			"selected_plan = '" + selectedPlan + '\'' + 
			",location = '" + location + '\'' + 
			",nonce = '" + nonce + '\'' + 
			"}";
		}
}