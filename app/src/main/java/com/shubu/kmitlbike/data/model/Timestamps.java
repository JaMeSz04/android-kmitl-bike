package com.shubu.kmitlbike.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Timestamps{

	@SerializedName("return_time")
	private String returnTime;

	@SerializedName("borrow_date")
	private String borrowDate;

	@SerializedName("borrow_time")
	private String borrowTime;

	@SerializedName("return_date")
	private String returnDate;

	public void setReturnTime(String returnTime){
		this.returnTime = returnTime;
	}

	public String getReturnTime(){
		return returnTime;
	}

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

	public void setReturnDate(String returnDate){
		this.returnDate = returnDate;
	}

	public String getReturnDate(){
		return returnDate;
	}

	@Override
 	public String toString(){
		return 
			"Timestamps{" + 
			"return_time = '" + returnTime + '\'' + 
			",borrow_date = '" + borrowDate + '\'' + 
			",borrow_time = '" + borrowTime + '\'' + 
			",return_date = '" + returnDate + '\'' + 
			"}";
		}
}