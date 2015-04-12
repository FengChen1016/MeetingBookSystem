package com.meetingbook.input;

import java.util.Date;
import java.util.List;

import com.meetingbook.core.BookRequest;
import com.meetingbook.util.Pair;

public interface BatchInput {
	
	List<BookRequest> load();
	
	Pair<Date, Date> getOfficeHours();
	
}
