package com.fruitshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeFormat {
	private Long hour ;
	private Long minute ;
	private Long second;

	public TimeFormat(Long milisecond) {
		this.hour = 0L;
		this.minute = 0L;
		this.second = milisecond/1000;
		if(second >=60)
		{
			this.minute = second/60;
			this.second = second%60;
		}
		if(minute>60)
		{
			this.hour = this.minute/60;
			this.minute = this.minute%60;
		}

	}

	@Override
	public String toString() {
		String time = new String();
		if(this.hour!=null&& this.hour > 0 )
		{
			time += this.hour +" giờ";

		}
		if(this.minute!=null&&this.minute >0)
		{
			time +=this.minute + " phút";
		}
		if(this.second>0)
		{
			time += this.second +"giây";
		}
		return time;
	}
}
