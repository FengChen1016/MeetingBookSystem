package com.meetingbook.output;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.meetingbook.core.BookRequest;
import com.meetingbook.core.DateFormat;

public class SimpleConsoleSortedView implements CalendarView {

	@Override
	public void render(Map<String, List<BookRequest>> records) {
		if (records == null || records.size() == 0) {
			return;
		}
		
		List<String> dateList = new ArrayList<String>(records.keySet());
		Collections.sort(dateList);
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		for (String date : dateList) {
			System.out.println(date);
			List<BookRequest> brList = records.get(date);
			// sort book request in one day
			Collections.sort(brList, new Comparator<BookRequest>() {
				@Override
				public int compare(BookRequest br1, BookRequest br2) {
					return br1.getStartTime().compareTo(br2.getStartTime());
				}
			});
			// output as required format
			for (BookRequest br : brList) {
				Date startDate = br.getStartTime();
				int duration = br.getDuration();
				start.setTime(startDate);
				end.setTime(startDate);
				end.add(Calendar.HOUR_OF_DAY, duration);
				System.out.println(DateFormat.OUTPUT_TIME.format(startDate) 
						+ " " + DateFormat.OUTPUT_TIME.format(end.getTime())
						+ " " + br.getEmployeeId());
			}
			
		}
		
	}

}
