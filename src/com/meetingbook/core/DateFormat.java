package com.meetingbook.core;

import java.text.SimpleDateFormat;

// Not thread safe
public class DateFormat {
	
	public static final SimpleDateFormat OFFICE_HOUR_FORMAT = new SimpleDateFormat("HHmm");
	
	public static final SimpleDateFormat REQUEST_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final SimpleDateFormat MEETING_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static final SimpleDateFormat OUTPUT_DATE = new SimpleDateFormat("yyyy-MM-dd");

	public static final SimpleDateFormat OUTPUT_TIME = new SimpleDateFormat("HH:mm");
	
	private DateFormat() {
	}
}
