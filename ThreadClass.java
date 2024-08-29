package spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// This is a new Thread class that extends the class Thread. This class is used
// to process file submissions concurrently
public class ThreadClass extends Thread {

	String submission;
	SPSS spss;

	// constructor that initializes the SPSS instance and submission file path.
	public ThreadClass(SPSS spss, String submission) {
		this.spss = spss;
		this.submission = submission;
	}

	// This is an overridden run method that is called when the thread starts
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		System.out.println("T" + name + "start");
		File file = new File(submission);
		try (Scanner scan = new Scanner(file)) {
			// processes each line of the file
			while (scan.hasNextLine()) {
				String line = scan.nextLine().trim();
				
				// new scanner to read elements in the line
				Scanner lineScanner = new Scanner(line);
				// reads the students name
				String student = lineScanner.next();
				List<Integer> list = new ArrayList<>();
				// read all the numbers in the line and add it to the List
				while (lineScanner.hasNextInt()) {
					list.add(lineScanner.nextInt());
				}
				
				// Synchronize on the shared SPSS object for thread safety
				synchronized (spss) {
					// Only add submission if student exists 
					// or can be added successfully
					if (spss.isNameInStudents(student)) {
						
						// ensures list is not empty before adding submission
						if (!list.isEmpty()) {
							spss.addSubmission(student, list);
						}
					}
				}
				lineScanner.close();
			}
			// catch and handle if the file is not found.
		} catch (FileNotFoundException e) {
			System.err.println();
		}
		System.out.println("T" + name + "end");
	}
}
