package com.meetingbook.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meetingbook.util.Pair;
import com.meetingbook.util.Predicate;

import static com.meetingbook.util.CollectionUtils.any;

public class BookRequestProcessor {
	// non-sorted meeting book requests, categorized by meeting start date
	protected final Map<String, List<BookRequest>> bookRecords = new HashMap<String, List<BookRequest>>();
	
	protected final Calendar cal = Calendar.getInstance();
	
	Map<String, List<BookRequest>> process(List<BookRequest> brList, Pair<Date, Date> officeHours) {
		if (brList == null || officeHours == null) {
			return Collections.emptyMap();
		}
		preProcess();
		for (BookRequest bookRequest : brList) {
			addRecord(bookRequest, officeHours);
		}
		postProcess();
		return bookRecords;
	}
	
	protected void addRecord(BookRequest bookRequest, Pair<Date, Date> officeHours) {
		Date startTime = bookRequest.getStartTime();
		int duration = bookRequest.getDuration();
		cal.setTime(startTime);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		// get key of bookRecords
		Calendar startDate = Calendar.getInstance();
		startDate.set(year, month, day, 0, 0, 0);
		Date meetingDate = startDate.getTime();
		String meetingDateStr = DateFormat.OUTPUT_DATE.format(meetingDate);
		
		if (bookRecords.get(meetingDateStr) == null) {  // first request of the date
			List<BookRequest> oneDayList = new ArrayList<BookRequest>();
			if (isValidMeetingTime(startTime, duration, officeHours)) {
				oneDayList.add(bookRequest);
				bookRecords.put(meetingDateStr, oneDayList);
			} // else {}   ignore invalid meeting time.  TODO alert user
			
		} else {
			if (!isValidMeetingTime(startTime, duration, officeHours)) {
				return;  // ignore invalid meeting time.  TODO alert user
			}
			List<BookRequest> oneDayList = bookRecords.get(meetingDateStr);
			// could be overlapped with multiple meeting records
			List<BookRequest> overlappedRequests = new ArrayList<BookRequest>();
			for (BookRequest br : oneDayList) {
				if (isOverlap(bookRequest, br)) {
					overlappedRequests.add(br);
				}
			}
			
			if (overlappedRequests.size() > 0) {
				final Date submissionTime = bookRequest.getSubmissionTime();
				
				if (!any(overlappedRequests, new Predicate<BookRequest>() {
					@Override
					public boolean fit(BookRequest r) {
						return r.getSubmissionTime().before(submissionTime);
					}
				})) {  // if submission time of this request is before all overlapped records
					// keep the first submitted one, this request
					oneDayList.removeAll(overlappedRequests);
					oneDayList.add(bookRequest);
				} // else {} ignore overlapped meeting request. TODO alert user
			} else {
				// no overlap to any existing record, safe to add into oneDayList
				oneDayList.add(bookRequest);
			}
		}
		
	}

	private boolean isOverlap(BookRequest bookRequest, BookRequest br) {
		Date s1 = bookRequest.getStartTime();
		int d1 = bookRequest.getDuration();
		
		Calendar start1 = Calendar.getInstance();
		start1.setTime(s1);
		Calendar end1 = Calendar.getInstance();
		end1.setTime(s1);
		end1.add(Calendar.HOUR_OF_DAY, d1);
		
		Date s2 = br.getStartTime();
		int d2 = br.getDuration();
		
		Calendar start2 = Calendar.getInstance();
		start2.setTime(s2);
		Calendar end2 = Calendar.getInstance();
		end2.setTime(s2);
		end2.add(Calendar.HOUR_OF_DAY, d2);
		
		String start1Str = DateFormat.OUTPUT_TIME.format(start1.getTime());
		String end1Str = DateFormat.OUTPUT_TIME.format(end1.getTime());
		String start2Str = DateFormat.OUTPUT_TIME.format(start2.getTime());
		String end2Str = DateFormat.OUTPUT_TIME.format(end2.getTime());
		return (start1Str.compareTo(start2Str) >= 0 && start1Str.compareTo(end2Str) < 0) 
			|| (end1Str.compareTo(start2Str) > 0 && end1Str.compareTo(end2Str) <= 0);
		// return (!start1.before(start2) && start1.before(end2)) || (end1.after(start2) && !end1.after(end2));
	}

	private boolean isValidMeetingTime(Date startTime, int duration, Pair<Date, Date> officeHours) {
		Calendar start = Calendar.getInstance();
		start.setTime(startTime);
		
		Calendar officeHourStart = Calendar.getInstance();
		officeHourStart.setTime(officeHours.getFirst());
		
		Calendar officeHourEnd = Calendar.getInstance();
		officeHourEnd.setTime(officeHours.getSecond());
		
		Calendar end = Calendar.getInstance();
		end.setTime(startTime);
		end.add(Calendar.HOUR_OF_DAY, duration);
		
		String startStr = DateFormat.OUTPUT_TIME.format(start.getTime());
		String endStr = DateFormat.OUTPUT_TIME.format(end.getTime());
		String officeStart = DateFormat.OUTPUT_TIME.format(officeHourStart.getTime());
		String officeEnd = DateFormat.OUTPUT_TIME.format(officeHourEnd.getTime());
		
		//return !start.before(officeHourStart) && !end.after(officeHourEnd);
		return startStr.compareTo(officeStart) >= 0 && endStr.compareTo(officeEnd) <=0;
	}

	// hook for sub class
	protected void preProcess() {
	}
	
	// hook for sub class
	protected void postProcess() {
	}
	
}
