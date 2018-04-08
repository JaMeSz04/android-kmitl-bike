package com.shubu.kmitlbike.data.model.bike;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class BikeReturnResponse{

	@SerializedName("overdued")
	private boolean overdued;

	public void setOverdued(boolean overdued){
		this.overdued = overdued;
	}

	public boolean isOverdued(){
		return overdued;
	}

	@Override
 	public String toString(){
		return 
			"BikeReturnResponse{" + 
			"overdued = '" + overdued + '\'' + 
			"}";
		}
}