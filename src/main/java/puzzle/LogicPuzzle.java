package puzzle;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// TODO: Auto-generated Javadoc
/**
 * The Class LogicPuzzle.
 */
@XmlRootElement(name = "logicpuzzles")
@XmlAccessorType (XmlAccessType.FIELD)
public class LogicPuzzle 
{
	
	/** The puzzle. */
	@XmlElement(name = "puzzle")
	private List<Puzzle> puzzle = null;

	/**
	 * Gets the puzzles.
	 *
	 * @return the puzzles
	 */
	public List<Puzzle> getPuzzles() {
		return puzzle;
	}

	/**
	 * Sets the puzzles.
	 *
	 * @param puzzle the new puzzles
	 */
	public void setPuzzles(List<Puzzle> puzzle) {
		this.puzzle = puzzle;
	}

	

}