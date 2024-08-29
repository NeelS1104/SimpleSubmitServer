package spss;

import java.util.ArrayList;
import java.util.List;





/* 
 * The SPSS class acts as a simple project submit server, acting like a model  
 * for the submit server. The class contains various methods which lets the user 
 * to add students, submissions, and other functions, which are necessary in a  
 * submit server.  
 */

public class SPSS {

	private int numTests;
	private int numOfSubmissions;
	private int numOfStudents;
	private ArrayList<Student> students;
	
	/* The SPSS constructor creates a new submit server. It takes 1 parameter, 
     * The parameter is the number of tests that the project will have. 
     * It also creates an ArrayList which holds all the students which can  
     * submit to the server. 
     */ 
	public SPSS(int numTests) {
		if (numTests <= 0) {
			numTests = 1;
		}
		this.numTests = numTests;
		students = new ArrayList<>();
	}

	/* The addStudent method adds a student if valid, to the list of students 
     * allowed to submit projects. A student can be added if the given parameter 
     * isn't null, blank, or already existing in the server. 
     */ 
	public boolean addStudent(String newStudent) {
		boolean valid = true;
		// checks to make sure given name isn't null or blank
		if (newStudent == null || newStudent.isBlank()) {
			valid = false;
		}
		// utilizes a helper method to check if the given name isn't already in
		// the server
		if (isNameInStudents(newStudent)) {
			valid = false;
		}
		if (valid) {
			// meets all the requirements and adds a new student to the server
			students.add(new Student(newStudent));
			numOfStudents++;
		}
		return valid;
	}

	/* helper method that checks if the given name is already in the server. 
	 * It does this by looping through the list of students and checks if the 
	 * name given is equal to any of the students name. Returns true or false 
	 * based on if its equal or not.  
	 */ 
	boolean isNameInStudents(String name) {
		boolean valid = false;
		// loops through all the students in the SPSS
		for (Student student : students) {
			// Checks if the names are matching
			if (student.isMatching(name)) {
				valid = true;
			}
		}
		return valid;
	}

	// returns the total number of students in the SPSS 
	public int numStudents() {
		return numOfStudents;
	}

	/* This is a helper method, which takes a name and returns a reference to a 
     * student object. It loops through the students list in SPSS and checks if  
     * the name matches with the given name and returns that student.  
     */
	private Student findStudent(String name) {
		Student find = null;
		// loops through list of students
		for (Student student : students) {
			// checks if the name matches
			if (student.isMatching(name)) {
				find = student;
			}
		}
		return find;
	}
	
	/*  
     * Method to add a submission for a student. It takes the name of the  
     * student and a list of test results as parameters. Returns true if the  
     * submission is valid and added successfully, false otherwise. A submission 
     * is considered valid if the test results list is not null or empty, the  
     * number of test results matches the expected number of tests, the student 
     * with the given name exists in the server's list of students, the 
     * and all test results are non-negative. If the submission is valid, 
     * it updates the student's top score and increments the total number of 
     * submissions. 
     */
	public boolean addSubmission(String name, List<Integer> testResults) {
		boolean valid = true;
		// Checks the parameters to make sure they are not null or empty
		if (testResults == null || testResults.isEmpty() || name == null) {
			valid = false;
		}
		// Checks if the size of the submissions matches the number of tests
		else if (testResults.size() != numTests) {
			valid = false;
		}
		// checks that the student exists
		else if (!isNameInStudents(name)) {
			valid = false;
		} else {
			// checks if any of the scores in the attempted submissions are
			// negative
			for (Integer num : testResults) {
				if (num < 0) {
					valid = false;
				}
			}
		}
		// if it passes all the conditions then it will be compared to students
		// previous score if any
		if (valid) {
			findStudent(name).compareTopScore(testResults);
			numOfSubmissions++;
		}

		return valid;
	}

	/* This method uses threads to add data for project submissions to the 
	 * current SPSS object concurrently. It will input the data from a file.
	 * Each line from the file will be counted as a new submission. If the 
	 * parameter is null or empty it will return false, otherwise it
	 * will return true. 
	 */
	public boolean readSubmissionsConcurrently(List<String> fileNames) {
		// Checks to ensure that fileNames is not null or empty
		if (fileNames == null || fileNames.isEmpty()) {
	        return false;
	    }

	    List<ThreadClass> threads = new ArrayList<>();

	    // Start threads
	    for (String fileName : fileNames) {
	        ThreadClass newThread = new ThreadClass(this, fileName);
	        newThread.start();
	        threads.add(newThread);
	    }

	    // Wait for all threads to finish
	    for (ThreadClass thread : threads) {
	        try {
	            thread.join();
	        } catch (InterruptedException e) {
	        	// Restore interrupted status
	            Thread.currentThread().interrupt(); 
	            // Return false if interrupted
	            return false; 
	        }
	    }

	    return true; // Return true if all threads complete successfully
	}

	/* Method to find the score of the student. It takes the name of the student 
     * and returns the score if the given parameter is valid. The name is  
     * considered valid if it is not null, or empty, and if the student is  
     * present in the list of students. 
     */ 
	public int score(String name) {
		int score = 0;
		// checks if the name given is not null, empty, and is in the list of
		// students in the SPSS
		if (name == null || name.isEmpty() || !isNameInStudents(name)) {
			score = -1;
		}
		if (score != -1) {
			// if passed all the conditions, it finds the student object using
			// the name and finds the score.
			score = findStudent(name).scoreTotal();
		}
		return score;
	}

	/* Method to find the number of submissions for a given student. It takes  
     * the name of a student and returns the number of submissions if the given 
     * parameter is valid. The name is considered valid if it is not null, or  
     * empty, and if the student is present in the list of students. 
     */ 
	public int numSubmissions(String name) {
		int num = 0;
		// checks if the name given is not null, empty, and is in the list of
		// students in the SPSS
		if (name == null || name.isEmpty() || !isNameInStudents(name)) {
			num = -1;
		}
		if (num != -1) {
			// if passed all conditions, then it finds the student object using
			// the name, and gets the number of submissions
			num = findStudent(name).getNumOfSubmissions();
		}
		return num;
	}

	// This method returns the total number of submissions in the submit server 
    // for all students. 
	public int numSubmissions() {
		return numOfSubmissions;
	}

	/* Method to find if a students submissions is considered satisfactory. A 
     * submissions is considered satisfactory if a pass at least half of the  
     * tests. It takes a name of a student and returns true or false based on 
     * if its passing given that the name is valid. The name is considered valid 
     * if it is not null, or empty, and if the student is present in the list  
     * of students. 
     */
	public boolean satisfactory(String name) {
		boolean valid = true;
		// checks if the name given is not null, empty, and is in the list of
		// students in the SPSS
		if (name == null || name.isEmpty() || !isNameInStudents(name)) {
			valid = false;
		}
		// makes sure the student doesn't have no submissions
		else if (numSubmissions(name) == 0) {
			valid = false;
		}
		if (valid) {
			// if passed all conditions, then it finds the student object using
			// the name, then uses the passingScore method to determine if
			// satisfactory
			valid = findStudent(name).passingScore(numTests);
		}
		return valid;
	}

	/* Method to see if a student gets extra credit. A student gets extra credit 
     * if they pass all the tests and only have 1 submission. It takes a name of 
     * a student and returns true or false based on if they got extra credit 
     * given that the name is valid. The name is considered valid if it is not  
     * null, or empty, and if the student is present in the list of students. 
     */ 
	public boolean gotExtraCredit(String name) {
		boolean valid = true;
		// checks if the name given is not null, empty, and is in the list of
		// students in the SPSS
		if (name == null || name.isEmpty() || !isNameInStudents(name)) {
			valid = false;
		}
		// makes sure the student doesn't have no submissions
		else if (numSubmissions(name) == 0) {
			valid = false;
		}
		if (valid) {
			// if passed all conditions, then it finds the student object using
			// the name, then uses the extraCredit method to determine if
			// satisfactory
			valid = findStudent(name).extraCredit(numTests);
		}
		return valid;
	}
}
