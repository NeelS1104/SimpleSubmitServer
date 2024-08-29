package spss;


import java.util.List;

/* 
 * The Student class is where student objects are created to be used in the 
 * SPSS class. The class contains various methods that does computations for  
 * the student's name, scores, and number of submissions. This class contains  
 * information of a student that a Submit server would require. 
 */

public class Student {

	private String name;
	private List<Integer> topScore;
	private int numOfSubmissions;

	// Constructor to initialize a new student using a name provided
	public Student(String name) {
		this.name = name;
	}

	// Checks if the given name is equal to the name of the current student
	public boolean isMatching(String name) {
		boolean valid = false;
		if (this.name.equals(name)) {
			valid = true;
		}
		return valid;
	}

	// returns the number of submissions to the SPSS the student had
	public int getNumOfSubmissions() {
		return numOfSubmissions;
	}

	/*
	 * This method compares the students current submission score to a new
	 * submission. The total score is computed by adding up all the int values in
	 * the list.
	 */
	public void compareTopScore(List<Integer> compareScore) {
		// checks if the student even has a first submission and if they don't
		// sets the new submission to topScore
		if (topScore == null) {
			topScore = compareScore;
		} else {
			int sumCurrent = 0;
			int sumTest = 0;
			// loops through both lists adding up all the scores
			for (int index = 0; index < topScore.size(); index++) {
				sumCurrent = sumCurrent + topScore.get(index);
				sumTest = sumTest + compareScore.get(index);
			}
			// compares the scores to see which one is higher and sets the new
			// score to topScore if it's higher than the current.
			if (sumTest > sumCurrent) {
				topScore = compareScore;
			}
		}
		// Increases the number of submissions that student has
		numOfSubmissions++;
	}

	// This method calculates the total score of the current top submission
	public int scoreTotal() {
		int score = 0;
		// checks to make sure there is actually a submission, then loops
		// through and adds up the scores
		if (topScore != null) {
			for (Integer num : topScore) {
				score = score + num;
			}
		}

		return score;
	}

	// This method is used to assist the satisfactory method in SPSS. It checks
	// if the current topScore submissions passes at least half of the tests.
	public boolean passingScore(int numOfTests) {
		boolean passing = false;
		int scoreCounter = 0;
		// loops through topScore, adding 1 to the counter if the test has a
		// value of more than 1 indicating that it passed a test.
		for (Integer num : topScore) {
			if (num > 0) {
				scoreCounter++;
			}
		}
		// checks if number of tests passed is > or = to total tests
		if (scoreCounter >= numOfTests / 2) {
			passing = true;
		}
		return passing;
	}

	// This method is used to assist the gotExtraCredit method in SPSS. It
	// checks if the user only had one submissions and passed every test
	public boolean extraCredit(int numOfTests) {
		boolean credit = false;
		int scoreCounter = 0;
		// loops through topScore counting the passed tests
		for (Integer num : topScore) {
			if (num > 0) {
				scoreCounter++;
			}
		}
		// checks if it passes both conditions in order to qualify for extra
		// credit
		if (scoreCounter == numOfTests && numOfSubmissions == 1) {
			credit = true;
		}
		return credit;
	}

}
