package puzzle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The puzzle class. Generated from logicpuzzles.xml but also contains puzzle processing methods - mainly 
 * turning raw text clues into {@link #Clue} class objects. 
 */
@XmlRootElement(name = "puzzle")
@XmlAccessorType (XmlAccessType.FIELD)
public class Puzzle {
	
	/** The raw clues. */
	@XmlElementWrapper(name = "clues")
	@XmlElement(name = "clue")
	private List<String> rawClues;
	
	/** The clue verb. Some puzzles have a specific word in place of is/are, 
	 * for example a puzzle about artists might use "painted" in one or more clues
	 * this value is added to the XML for individual puzzles instead of an ever-growing
	 * list of splitwords*/
	@XmlElement(name = "clueverb")
	private String clueVerb = "";
	
	/** The categories. */
	@XmlElement(name = "category")
	private List<Category> categories;
	
	/** The id. */
	private int id;
	
	/** The title. */
	private String title;
	
	/** The story. */
	private String story;
	
	/** The processed clues. */
	private List<Clue> processedClues;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the story.
	 *
	 * @return the story
	 */
	public String getStory() {
		return story;
	}

	/**
	 * Gets the raw clues.
	 *
	 * @return the raw clues
	 */
	public List<String> getRawClues() {
		return rawClues;
	}
	
	/**
	 * Gets the processed clues.
	 *
	 * @return the processed clues
	 */
	public List<Clue> getProcessedClues() {
		return processedClues;
	}

	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}
	
	/**
	 * Puzzle set up. Determine category steps and process raw clues accordingly.
	 */
	//generate puzzle
	public void buildPuzzle() throws NumberFormatException, NullPointerException {
		determineSteps();
		processedClues = new ArrayList<Clue>();
		for(String str:rawClues) {
				decipherClues(str);
		}
	}

	/**
	 * Determine steps. Check first three category items to determine if there is any 
	 * determinable step between their values. For example, 9:00AM, 10:00AM and 11:00AM
	 * should be recognized as having a step of 1. 
	 * 
	 * Manual override of text steps, i.e. first, second, third.
	 */
	private void determineSteps() throws NumberFormatException {
		
		for (Category cat : categories) {
			cat.setStep(0);
			float i = numberHelper(cat.getItems().get(0));
			float j = numberHelper(cat.getItems().get(1));
			float k = numberHelper(cat.getItems().get(2));
				if(Math.abs(i - j) == Math.abs(j - k)) {
					cat.setStep(Math.abs(i - j));		
				}
			if (cat.getItems().contains("first")) {
				cat.setStep((float)1.0);
			}
		}
	}
	
	/**
	 * Number helper. Attempt to find any number in the string. Return 0 if none is found.
	 *
	 * @param str the str
	 * @return the float
	 */
	private float numberHelper(String str) {
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(str);
		if(m.find()) {
			return Float.parseFloat(str.substring(m.start(), m.end()));
		}
		return 0;
	}
	
	//decipher clues

	/**
	 * Decipher clues. Determine where to split the clue based on splitwords. Determine the 
	 * clue's "equation" (i.e. is it saying items are equivalent or is it comparing them). Extract category items 
	 * the clue to determine a subject and an object. Create a new clue object with these values.
	 * 
	 * If the clue contains a step value add it. 
	 *
	 * Some clues of the format "The three artists are Van Gogh, the artist who painted the Mona Lisa, and the artist who lived in Barcelona" 
	 * will have either no object or subject ( _____ = Van Gogh, Mona Lisa, Barcelona). Remove the equation so future compiling knows to
	 * process the clue instead as (Van Gogh != Mona Lisa != Barcelona).
	 * 
	 * @param str the raw text clue
	 */
	private void decipherClues(String str) throws NullPointerException {
		String[] clue = new String[3];
		String eq = "";
		float j = 0;
		//if clue contains standard splitword (than, is not, etc.), break on word
		for (SplitWords s : SplitWords.values()) {
			if (str.contains(s.getWord())) {
				clue = clueSplitter(str, s.getWord());
				eq = s.getEquation();
				break;
			}
		}
		//if split did not occur, check for split on clueverb
		if (clue[0] == null) {
			clue = clueSplitter(str, clueVerb);
			eq = "=";
		}
		//create new clue
		Clue newClue = new Clue(extractCategoryItems(clue[0]), j, eq, extractCategoryItems(clue[1]) );
		//for comparisons check if the clue contains a step variable 
				//(eg. 1 more than vs. some more than)
				if (eq == "<" || eq == ">") {
					for (Category cat : categories) {
						j = doesClueContainNumericStep(str, newClue.getSubjectList(), clue[0].length(), cat.getStep());
						if(j > 0) {
							newClue.setStep(j);
							for(int i = 0; i < newClue.getSubjectList().size() - 1; i++) {
								if (getCategoryfromItem(newClue.subject(i)).getStep() > 0) {
									newClue.getSubjectList().remove(i);
								}
							}
						}
					}
				}
		//if either side has no category items remove equation
		if (newClue.getSubjectList().isEmpty() || newClue.getObjectList().isEmpty()) {
			newClue.setEquation("");
		}
		processedClues.add(newClue);
		System.out.println(newClue.toString());
	}
	

	
	//decipherClue helper methods
	
	/**
	 * Extract category items. Scan text for words that match given category items.
	 *
	 * @param str the str
	 * @return the list
	 */
	private List<String> extractCategoryItems(String str) {
		List<String> items = new ArrayList<String>();    
		// populate it
		for (Category cat : categories) {
			for (String item : cat.getItems()) {
				if (str != null && str.contains(item)) {
					items.add(item);
				}
			}
		}
		return items;
	}
	

	/**
	 * Does clue contain a numeric step. Search the clue text for any 
	 *
	 * @param str the str
	 * @param clue the clue
	 * @param i the i
	 * @param j the j
	 * @return the float
	 */
	private float doesClueContainNumericStep(String str, List<String> clue, int i, float j) throws NumberFormatException {
		if (j == 0) {
			return 0;
		}
		LinkedList<String> numbers = new LinkedList<String>();
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(str.substring(0, i));
		while (m.find()) {
			numbers.add(m.group());
		}
		for (String num : numbers) {
			if (!clue.contains(num) && !num.isEmpty()) {
					float k = Float.parseFloat(num);
					if (k % j == 0) {
						return k;
				}
			}
		}
		return 0;
	}

	/**
	 * Clue splitter. Given a string and a word to cut on it returns an array 
	 * such that array[0] is the clue before the split word and array[1]
	 * is the clue after the split word.
	 *
	 * @param str the str
	 * @param word the word
	 * @return the string[]
	 */
	private String[] clueSplitter(String str, String word) {
		String[] splits = new String[2];
		splits = str.split(word);
		return splits;
	}
	
	
	//public finder method
	
	/**
	 * Gets the category from the item.
	 *
	 * @param itemMatch the item match
	 * @return cat the category 
	 */
	public Category getCategoryfromItem(String itemMatch){
		for(Category cat:getCategories()) {
			for(String i:cat.getItems()) {
				if(i.equals(itemMatch)) {
					return cat;
				}
			}
		}
		return null;
	}
	



}
