package com.meetingbook.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.meetingbook.core.BookRequest;
import com.meetingbook.core.DateFormat;
import com.meetingbook.util.Pair;

public class FSBatchInput implements BatchInput {
	
	// batch request file
	private final File brFile;
	private Pair<Date, Date> officeHours;  // HHmm
	
	public FSBatchInput(String filePath) {
		this(new File(filePath));
	}
	
	public FSBatchInput(File file) {
		if (file == null) {
			throw new NullPointerException("Batch request file is null.");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException("Batch request file not exist:"
					+ file.getAbsolutePath());
		}
		this.brFile = file;
	}

	@Override
	public List<BookRequest> load() {
		List<BookRequest> brList = new ArrayList<BookRequest>();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(brFile)));
			String line;
			int i = 0;
			String requestInfo = null;
			String meetingInfo = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (i == 0) {  // record office hours
					String[] parts = line.split(" ", 2);
					if (parts.length < 2) {
						System.out.println("Invalid office hour format: " + line 
								+ " Expect format: HHMM HHMM");
						break;
					}
					try {
						officeHours = Pair.create(DateFormat.OFFICE_HOUR_FORMAT.parse(parts[0]), 
											DateFormat.OFFICE_HOUR_FORMAT.parse(parts[1]));
						i++;
						continue;
					} catch (ParseException e) {
						System.out.println("Invalid office hour format: " + line 
								+ " Expect format: HHMM HHMM");
						break;
					}
				}
				
				if (i%2 == 1) {  
					requestInfo = line;
				} else {  // record book request and clear requestInfo, meetingInfo
					meetingInfo = line;
					BookRequest bookRequest = parse(requestInfo, meetingInfo);
					if (bookRequest != null) {
						brList.add(bookRequest);
					}
					requestInfo = null;
					meetingInfo = null;
				}
				i++;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Batch request file not found." + brFile.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Error in reading batch request file: " + brFile.getAbsolutePath());
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println("Fail to close file input stream.");
					e.printStackTrace();
				}
			}
		}
		return brList;
	}

	private BookRequest parse(String requestInfo, String meetingInfo) {
		String[] rparts = requestInfo.split(" ");
		String[] mparts = meetingInfo.split(" ");
		StringBuilder sb = new StringBuilder();
		
		if (rparts.length == 3 && mparts.length == 3) {
			try {
				sb.append(rparts[0]).append(" ").append(rparts[1]);
				Date submissionTime = DateFormat.REQUEST_TIME_FORMAT.parse(sb.toString());
				String employeeId = rparts[2];
				sb.setLength(0);
				sb.append(mparts[0]).append(" ").append(mparts[1]);
				Date meetingTime = DateFormat.MEETING_TIME_FORMAT.parse(sb.toString());
				int duration = Integer.parseInt(mparts[2]);
				
				return new BookRequest(submissionTime, employeeId, meetingTime, duration);
				
			} catch (ParseException e) {
				System.out.println("Invalid date time format: " + requestInfo + " " + meetingInfo);
			} catch (NumberFormatException e) {
				System.out.println("Invalid meeting duration in: " + meetingInfo);
			}
		}
		
		System.out.println("Invalid request format: " + requestInfo + " " + meetingInfo);
		return null;
	}
	
	@Override
	public Pair<Date, Date> getOfficeHours() {
		return officeHours;
	}

}
