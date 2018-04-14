package com.shubu.kmitlbike.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Timestamps{

	@SerializedName("borrow_date")
	private String borrowDate;

	@SerializedName("borrow_time")
	private String borrowTime;

	public void setBorrowDate(String borrowDate){
		this.borrowDate = borrowDate;
	}

	public String getBorrowDate(){
		return borrowDate;
	}

	public void setBorrowTime(String borrowTime){
		this.borrowTime = borrowTime;
	}

	public String getBorrowTime(){
		return borrowTime;
	}

	@Override
 	public String toString(){
		return 
			"Timestamps{" + 
			"borrow_date = '" + borrowDate + '\'' + 
			",borrow_time = '" + borrowTime + '\'' + 
			"}";
		}
}