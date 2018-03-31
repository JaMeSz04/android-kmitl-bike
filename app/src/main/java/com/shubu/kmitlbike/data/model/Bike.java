package com.shubu.kmitlbike.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Bike{

	@SerializedName("mac_address")
	private String macAddress;

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("bike_name")
	private String bikeName;

	@SerializedName("bike_model")
	private String bikeModel;

	@SerializedName("id")
	private int id;

	@SerializedName("barcode")
	private String barcode;

	@SerializedName("longitude")
	private double longitude;

	public void setMacAddress(String macAddress){
		this.macAddress = macAddress;
	}

	public String getMacAddress(){
		return macAddress;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setBikeName(String bikeName){
		this.bikeName = bikeName;
	}

	public String getBikeName(){
		return bikeName;
	}

	public void setBikeModel(String bikeModel){
		this.bikeModel = bikeModel;
	}

	public String getBikeModel(){
		return bikeModel;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setBarcode(String barcode){
		this.barcode = barcode;
	}

	public String getBarcode(){
		return barcode;
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
			"Bike{" + 
			"mac_address = '" + macAddress + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",bike_name = '" + bikeName + '\'' + 
			",bike_model = '" + bikeModel + '\'' + 
			",id = '" + id + '\'' + 
			",barcode = '" + barcode + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}