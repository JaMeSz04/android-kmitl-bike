package com.shubu.kmitlbike.data.model.bike;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class BikeReturnForm{

	@SerializedName("location")
	private Location location;

	@SerializedName("cancel")
	private boolean cancel;

	public BikeReturnForm(Location loc,  boolean isCancel){
		this.location = loc;
		this.cancel = isCancel;
	}

	public void setLocation(Location location){
		this.location = location;

	}

	public Location getLocation(){
		return location;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
 	public String toString(){
		return 
			"BikeReturnForm{" + 
			"location = '" + location + '\'' + "cancel = '" + cancel + '\'' +
			"}";
		}
}