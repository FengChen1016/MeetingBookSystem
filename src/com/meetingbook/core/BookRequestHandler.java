package com.meetingbook.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.meetingbook.input.BatchInput;
import com.meetingbook.output.CalendarView;

public class BookRequestHandler {
	
	private BatchInput input;
	private final BookRequestProcessor reqProcessor;
	private CalendarView view;
	
	public BookRequestHandler(BatchInput input, BookRequestProcessor reqProcessor, CalendarView view) {
		this.input = input;
		this.reqProcessor = reqProcessor;
		this.view = view;
	}
	
	public void handleBatchRequest() {
		// input
		List<BookRequest> brList = input.load();
		// process
		Map<String, List<BookRequest>> records = reqProcessor.process(brList, input.getOfficeHours());
		// output
		view.render(records);
	}
	
}
