package com.meetingbook;

import java.io.File;
import java.util.List;
import java.util.ServiceLoader;

import com.meetingbook.core.BookRequestHandler;
import com.meetingbook.core.BookRequestProcessor;
import com.meetingbook.input.BatchInput;
import com.meetingbook.input.FSBatchInput;
import com.meetingbook.output.CalendarView;

import static com.meetingbook.util.CollectionUtils.toList;


public class MeetingBatchBook {
	public static final String DEFAULT_FILE_PATH = "../input.txt";

	public static void main(String[] args) {
		// TODO support specify input file path in argument
		
		File inputFile = new File(DEFAULT_FILE_PATH);
		doBatchProcess(inputFile);
		
	}

	public static void doBatchProcess(File inputFile) {
		// construct BatchInput
		BatchInput input = new FSBatchInput(inputFile);
		
		BookRequestProcessor reqProcessor = new BookRequestProcessor();
		
		// Load CalendarView implementation
		// CalendarView implementation can be easily switched in 
		// configuration file in META-INF/services
		List<CalendarView> views = toList(ServiceLoader.load(CalendarView.class).iterator());
		if (views == null || views.size() == 0) {
			System.out.println("No CalendarView implementation registered.");
			return;
		}
		// Here we only use the first implementation
		CalendarView view = views.get(0);
		
		BookRequestHandler reqHandler = new BookRequestHandler(input, reqProcessor, view);
		reqHandler.handleBatchRequest();
	}

}
