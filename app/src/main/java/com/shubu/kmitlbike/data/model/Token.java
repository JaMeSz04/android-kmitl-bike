package com.shubu.kmitlbike.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Token{

	@SerializedName("token")
	private String token;

	public Token(String token){
		this.token = token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"Token{" + 
			"token = '" + token + '\'' + 
			"}";
		}
}