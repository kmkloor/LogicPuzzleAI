package puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

/**
 * The Gameplay class has the set up and analysis methods used to solve the game. 
 * Based on a clue's equation the appropriate method is used to read and mark the database. 
 * Categories are arranged on a grid {@see Gridmanager} where values 0, 1, 2, and 3 
 * respectively indicate that the cross-section is undetermined, not a match, a match, or an invalid comparison.
 * 
 */
public class Gameplay {
	
	/** The puzzle. */
	private Puzzle puzzle;
	
	/** The grid. */
	private GridManager grid;
	
	/** The connection. */
	private Connection conn;
	
	/** The categories. */
	private List<String> categories;

	/**
	 * Sets up a puzzle grid based on the puzzle id.
	 *
	 * @param i the puzzle id
	 * @param p the database password
	 */
	public void setUp(int i, String p){
		try {
			LoadPuzzles loadPuzzles = new LoadPuzzles();
			loadPuzzles.importPuzzles();
			puzzle = loadPuzzles.getLogicPuzzles().getPuzzles().get(i);
			puzzle.buildPuzzle();
		}
		catch(JAXBException e) {
			System.out.println(e.getLocalizedMessage());	
		}
		grid = new GridManager();
		conn = null;
		conn = grid.getConnection(p);
		grid.buildDB(puzzle);

		//compile easy access list of categories
		categories = new ArrayList<String>();
		for (Category catList:puzzle.getCategories()) {
			for (String item:catList.getItems()){
				categories.add(item);
			}
		}
		destroyDuplicates();
	}
	
	public void puzzleCount() {
		
	}

	/**
	 * Destroy duplicates. We want to make sure that a = a comparisons are not being checked or considered. 
	 */
	private void destroyDuplicates() {
		for(String s:categories) {
			for (String t: categories) {
				if(puzzle.getCategoryfromItem(s) == puzzle.getCategoryfromItem(t)) {
					grid.updateIntersection(s, t, 3);
				}
			}
		}
	}

	/**
	 * This process directs the clue for analysis and action depending on the clue's equation.
	 * Options include equivalent (subject is/has object), not equivalent (subject is not/doesn't have object) 
	 * greater than/less than (depending on whether the subject has more or less than the object) and no equation 
	 *  {@See Puzzle#decipherClues}
	 */
	private void analyzeClues(){
		for(Clue clue: puzzle.getProcessedClues()) {
			//equals
			if(clue.getEquation().equals("=")) {
				equals(clue);
			}
			//not equals
			if(clue.getEquation().equals("!=")) {
				grid.updateIntersection(clue.subject(0), clue.object(0), 1);

			}
			//greater than
			if(clue.getEquation().equals(">") ) {
				comparison(clue.subject(0), clue.object(0), clue.getStep());
			}
			//less than
			if (clue.getEquation().equals("<")){
				comparison(clue.object(0),clue.subject(0), clue.getStep());
			}
			//no equation
			if(clue.getEquation().equals("")) {
				if(clue.getSubjectList().size() > 0) {
					for(String s:clue.getSubjectList()) {
						for(String t:clue.getSubjectList()){
							grid.updateIntersection(s, t, 1);
						}
					}
				}
				if(clue.getObjectList().size() > 0) {
					for(String s:clue.getObjectList()) {
						for(String t:clue.getObjectList()){
							grid.updateIntersection(s, t, 1);
						}
					}
				}
			}
		}
	}
	
	/**
	 * The helper method for clues that are equivalent. Deals with "either" clues as well, i.e. 
	 * a is either j or y. We know j != y, we also know that if either j or y is no longer an 
	 * option for a that a must equal the remaining option.
	 *
	 * @param clue the clue
	 */
	private void equals(Clue clue) {
			// a or j = y
			if(clue.getSubjectList().size() == 2) {
				equalsHelper(clue.getSubjectList(), clue.object(0));
			}
			// a = j or y
			if(clue.getObjectList().size() == 2) {
				equalsHelper(clue.getObjectList(), clue.subject(0));
			}
			// for non-either equivalent clues where simply a = y
			if(clue.getSubjectList().size() == 1 && clue.getObjectList().size() == 1) {
				grid.updateIntersection(clue.subject(0), clue.object(0), 2);
			}
	}
	
	/**
	 * The helper method for processing either clues.
	 *
	 * @param either the list of values for the either side of the equation
	 * @param single the compared single value
	 */
	
	private void equalsHelper(List<String> either, String single) {
		// a or j (either) = y (single)
		// a != j
		grid.updateIntersection(either.get(0), either.get(1), 1);
		// if a != y then j = y
		if(grid.checkIntersection(single, either.get(0)) == 1){
			grid.updateIntersection(single, either.get(1), 2);
		}
		// if j != y then a = y
		if(grid.checkIntersection(single, either.get(1)) == 1){
			grid.updateIntersection(single, either.get(0), 2);
		}
	}



	/**
	 * Comparison method. Deals with comparison clues that may or may not include a 
	 * step value. Built to consider the step categories maximum and minimum values, for 
	 * example if a is earlier than x, x cannot possibly occupy the minimum time value and a 
	 * cannot possibly occupy the maximum time value. 
	 *
	 * @param subject the subject
	 * @param object the object
	 * @param i the value of the comparison, i.e. $1.00 more than = 1, 2 hours after = 2
	 */
	private void comparison(String subject, String object, float i) {
		// where subject a > object b
		// a != b
		grid.updateIntersection(subject, object, 1);
		//steps apart
		Category stepCategory = null;
		int step = 1;
		//determine clue step category and if clue step is more than one
		for(Category cat:puzzle.getCategories()) {
			if(cat.getStep() != 0 && (i % cat.getStep() == 0)){
				
				if((int)(i / cat.getStep()) > 1) {
					step = (int)(i / cat.getStep());
					
				}
				stepCategory = cat;
			}
		}
		//set number of items in category
		int max = stepCategory.getItems().size() - 1;
		// a cannot be the minimum value and b cannot be the maximum value
		grid.updateIntersection(subject, stepCategory.item(0), 1);
		grid.updateIntersection(object, stepCategory.item(max), 1);
		//if step > 1 a cannot be the minimum * # of steps and b cannot be the maximum * # of steps
		if(step > 1) {
			for(int j=0; j < step; j++) {
				grid.updateIntersection(subject, stepCategory.item(j), 1);
				grid.updateIntersection(object, stepCategory.item(max - j), 1);
			}
		}
		// if a is not the maximum, b must be smaller than the maximum - 1
		// continue until grid = 0 or ends
		int down = max;
		while(down > 1) {
			if(grid.checkIntersection(subject, stepCategory.item(down)) == 1){
				grid.updateIntersection(object, stepCategory.item(down - step), 1);	
			}
			down -= 1;
		}
		// if b is not the minimum, a must be bigger than the minimum + 1
		// continue until grid = 0 or ends
		int up = 0;
		while(up < max - 1) {
			if(grid.checkIntersection(object, stepCategory.item(up)) == 1) {
				grid.updateIntersection(subject, stepCategory.item(up + step), 1);	
			}
			up += 1;
		}
		//if either a or b is determined and step is known
		if(i != 0) {
			for(String item:stepCategory.getItems()) {
				if(grid.checkIntersection(subject, item) == 2) {
					grid.updateIntersection(object, stepCategory.item(stepCategory.getItems().indexOf(item) - step), 2);
				}
				if(grid.checkIntersection(object, item) == 2) {
					grid.updateIntersection(subject, stepCategory.item(stepCategory.getItems().indexOf(item) + step), 2);
				}
			}
		}
	}

	/**
	 * Traverse the game grid to search for a specific value. 
	 *
	 * @param i the search number
	 * @return true, if successful
	 */
	private boolean readGrid(int i) {
		boolean find = false;
		List<String> rows = new ArrayList<String>();
		for(String row:categories) {
			for(String column:categories) {
				int j = grid.checkIntersection(row, column);
				rows.add(row + ":" + column);
				if(!rows.contains(column + ":" + row)) {
					if(j == i && i == 0) {
						find = true;
					}
					if(j == i && i == 1) {
						System.out.println(row + " != " + column);
						find = true;

					}
					if(j == i && i == 2) {
						System.out.println(row + " = " + column);
						find = true;
					}
				}
			}
		}
		return find;
	}



	/**
	 * Traverses the grid to check for categories where process of elimination might be used.
	 * If of the set {a, b, c, d} we know that a = x then we also know that b != x, c != x, and d != x
	 */
	private void processElimination() {
		for(String row:categories) {
			for(String column:categories){
				int i = grid.checkIntersection(row, column);
				if (i == 0) {
					boolean check = true;
					for(String j: otherItems(row)) {
						if (grid.checkIntersection(j, column) == 0 || grid.checkIntersection(j, column) == 2) {
							check = false;
						}
					}
					if(check) {
						grid.updateIntersection(row, column, 2);
					}	
				}
			}
		}
	}

	/**
	 * Traverses the grid to check for categories where process of elimination might be used.
	 * If of the set {a, b, c, d} we know that a != x, b!= x, and c!= x then we know d = x
	 */
	private void processMatch() {
		for(String row:categories) {
			for(String column:categories) {
				int i = grid.checkIntersection(row, column);
				if (i == 2) {
					for(String j: otherItems(row)) {
						grid.updateIntersection(j, column, 1);
						for(String k:otherItems(column)) {
							grid.updateIntersection(row, k, 1);
						}
					}
					for(String comparison:categories) {
						if(!puzzle.getCategoryfromItem(comparison)
								.equals(puzzle.getCategoryfromItem(column))) {
							if(grid.checkIntersection(comparison, row) == 1) {
								grid.updateIntersection(comparison, column, 1);
							}
							if(grid.checkIntersection(comparison, row) == 2) {
								grid.updateIntersection(comparison, column, 2);
							}
						}
					}
				} 
			}
		}
	}

	/**
	 * Combines the three main methods - anayzeClues, processElimination, and processMatch 
	 * to solve the puzzle. The check boolean is triggered if no 0 squares are left on the grid. 
	 * The timeout is a safety measure to escape endless analysis. 
	 */
	public String solvePuzzle() {
		long startTime = System.currentTimeMillis();
		boolean check = true;
		while(check) {
			try {
			analyzeClues();
			processElimination();
			processMatch();
			check = readGrid(0);
			}
			catch (NullPointerException e) {
				return ("Puzzle error:" + e.getStackTrace().toString());
			}
			long elapsed = System.currentTimeMillis()-startTime;
			if (elapsed>10000) {
				//if puzzle failed print grid as is
				readGrid(1);
				readGrid(2);
				tearDown();
				return "Puzzle timeout" ;
				//throw new RuntimeException("timeout");
			}
			else if (!check){
				solved();
				tearDown();
				return "Puzzle solved!" ;
			}
		}
		tearDown();
		return "Puzzle didn't run.";
	}

	/**
	 * If the puzzle was solved print the results.
	 */
	private void solved() {
		for(String row:puzzle.getCategories().get(0).getItems()) {
			StringBuilder sb = new StringBuilder();
			sb.append(row);
			for(String column:categories) {
				if (grid.checkIntersection(row, column) == 2) {
					sb.append(" = " + column);
				}
			}
			System.out.println(sb.toString());
		}
	}

	/**
	 * Return the other items of a category if a single item is given. 
	 * If a is part of {a, b, c, d}, otherItems(a) will return {b, c, d}
	 *
	 * @param item a single item
	 * @return the list of other items
	 */
	private List<String> otherItems(String item) {
		for(Category cat:puzzle.getCategories()) {
			List<String> otherItems = new ArrayList<String>();
			for(String i:cat.getItems()) {
				otherItems.add(i);
			}
			if (otherItems.contains(item)) {
				otherItems.remove(item);
				return otherItems;
			}
		}
		return null;
	}



	/**
	 * Detroy the database after solving or failing the puzzle.
	 */
	private void tearDown() {
		grid.destroyDB();
	}

}

