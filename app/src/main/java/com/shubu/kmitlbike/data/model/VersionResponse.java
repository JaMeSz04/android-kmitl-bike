package com.shubu.kmitlbike.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class VersionResponse{

	@SerializedName("version_code")
	private String versionCode;

	@SerializedName("required_update")
	private boolean requiredUpdate;

	@SerializedName("platform")
	private String platform;

	@SerializedName("url")
	private String url;

	public void setVersionCode(String versionCode){
		this.versionCode = versionCode;
	}

	public String getVersionCode(){
		return versionCode;
	}

	public void setRequiredUpdate(boolean requiredUpdate){
		this.requiredUpdate = requiredUpdate;
	}

	public boolean isRequiredUpdate(){
		return requiredUpdate;
	}

	public void setPlatform(String platform){
		this.platform = platform;
	}

	public String getPlatform(){
		return platform;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"VersionResponse{" + 
			"version_code = '" + versionCode + '\'' + 
			",required_update = '" + requiredUpdate + '\'' + 
			",platform = '" + platform + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}