package com.meetingbook;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MeetingBatchBookTest {
	
	private static final String filePath = "test/input.txt";
	private static File inputFile;
	private static final PrintStream sysOut = System.out;
	
	private static final String OUTPUT;
	
	static {
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		PrintStream newOut = new PrintStream(out);
		System.setOut(newOut);
		System.out.println("2011-03-21");
		System.out.println("09:00 11:00 EMP002");
		System.out.println("2011-03-22");
		System.out.println("14:00 16:00 EMP003");
		System.out.println("16:00 17:00 EMP004");
		
		OUTPUT = out.toString();
		System.setOut(sysOut);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		inputFile = new File(filePath);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testDoBatchProcess() {
		ByteArrayOutputStream bout = null;
		try {
			bout=new ByteArrayOutputStream();
			PrintStream newOut = new PrintStream(bout);
			System.setOut(newOut);
			
			MeetingBatchBook.doBatchProcess(inputFile);
			String result = bout.toString();
			System.setOut(sysOut);
			System.out.print(result);
			assertEquals(OUTPUT, result);
			
		} finally {
			if (bout != null) {
				try {
					bout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

}
