package com.meetingbook.core;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.meetingbook.util.Pair;

public class BookRequestProcessorTest {
	
	private static final BookRequestProcessor processor = new BookRequestProcessor();
	private static final  List<BookRequest> brList = new ArrayList<BookRequest>();
	private static Pair<Date, Date> officeHours = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 3, 5, 10, 15, 20);
		Date submissionTime = cal.getTime();
		cal.set(2014, 4, 2, 9, 0, 0);
		Date startTime = cal.getTime();
		BookRequest br1 = new BookRequest(submissionTime, "EMP001", startTime, 2);
		brList.add(br1);
		// br1 should be overwrite by br2 because they are overlapped 
		// and br2's submissionTime is before br1
		cal.set(2014, 3, 2, 10, 15, 20);
		submissionTime = cal.getTime();
		cal.set(2014, 4, 2, 9, 0, 0);
		startTime = cal.getTime();
		BookRequest br2 = new BookRequest(submissionTime, "EMP002", startTime, 1);
		brList.add(br2);
		// should be ignore, meeting end time exceed office end time
		cal.set(2014, 3, 10, 10, 20, 30);
		submissionTime = cal.getTime();
		cal.set(2014, 4, 10, 17, 0, 0);
		startTime = cal.getTime();
		BookRequest br3 = new BookRequest(submissionTime, "EMP003", startTime, 2);
		brList.add(br3);
		// valid request
		cal.set(2014, 3, 12, 10, 20, 30);
		submissionTime = cal.getTime();
		cal.set(2014, 4, 16, 17, 0, 0);
		startTime = cal.getTime();
		BookRequest br4 = new BookRequest(submissionTime, "EMP004", startTime, 1);
		brList.add(br4);
		
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.HOUR_OF_DAY, 9);
		cale.set(Calendar.MINUTE, 0);
		cale.set(Calendar.SECOND, 0);
		Date officeStart = cale.getTime();
		
		cale.set(Calendar.HOUR_OF_DAY, 18);
		cale.set(Calendar.MINUTE, 0);
		cale.set(Calendar.SECOND, 0);
		Date officeEnd = cale.getTime();
		
		officeHours = Pair.create(officeStart, officeEnd);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testProcess() {
		Map<String, List<BookRequest>> map = processor.process(brList, officeHours);
		
		assertEquals(2, map.size());
		assertEquals("EMP004", map.get("2014-05-16").get(0).getEmployeeId());
		assertEquals("EMP002", map.get("2014-05-02").get(0).getEmployeeId());
		
	}

}
