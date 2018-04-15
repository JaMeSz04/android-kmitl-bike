package com.shubu.kmitlbike.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class VersionForm{

	@SerializedName("version_code")
	private String versionCode;

	@SerializedName("platform")
	private String platform;

	public VersionForm(String platform, String versionCode){
		this.platform = platform;
		this.versionCode = versionCode;
	}

	public void setVersionCode(String versionCode){
		this.versionCode = versionCode;
	}

	public String getVersionCode(){
		return versionCode;
	}

	public void setPlatform(String platform){
		this.platform = platform;
	}

	public String getPlatform(){
		return platform;
	}

	@Override
 	public String toString(){
		return 
			"VersionForm{" + 
			"version_code = '" + versionCode + '\'' + 
			",platform = '" + platform + '\'' + 
			"}";
		}
}