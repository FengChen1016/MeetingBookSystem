package com.meetingbook.output;

import java.util.List;
import java.util.Map;

import com.meetingbook.core.BookRequest;

public interface CalendarView {
	
	void render(Map<String, List<BookRequest>> records);
	
}
