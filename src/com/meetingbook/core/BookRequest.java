package com.meetingbook.core;

import java.util.Date;

public class BookRequest {
	// request submission time, should be unique
	private final Date submissionTime;
	// employee id
	private final String employeeId;
	// meeting start time
	private final Date startTime;
	// meeting duration in hours
	private final int duration;
	
	public BookRequest(Date submissionTime, String employeeId, Date startTime, int duration) {
		this.submissionTime = submissionTime;
		this.employeeId = employeeId;
		this.startTime = startTime;
		this.duration = duration;
	}

	public Date getSubmissionTime() {
		return submissionTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public int getDuration() {
		return duration;
	}
	
	public String toString() {
		return "{submissionTime: " + DateFormat.REQUEST_TIME_FORMAT.format(submissionTime)
			 + " employeeId: " + employeeId
			 + " startTime: " + DateFormat.MEETING_TIME_FORMAT.format(startTime)
			 + " duration: " + duration + "}";
	}
	
}
