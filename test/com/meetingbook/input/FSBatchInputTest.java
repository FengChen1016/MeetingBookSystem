package com.meetingbook.input;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.meetingbook.core.BookRequest;

public class FSBatchInputTest {
	private static final String filePath = "test/input.txt";
	private static BatchInput input = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file = new File(filePath);
		input = new FSBatchInput(file);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		List<BookRequest> list = input.load();
		assertEquals(5, list.size());
		assertEquals("EMP002", list.get(1).getEmployeeId());
		assertEquals(2, list.get(2).getDuration());
	}

}
