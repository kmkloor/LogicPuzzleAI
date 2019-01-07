package puzzle;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


// TODO: Auto-generated Javadoc
/**
 * The Class LoadPuzzles.
 */
public class LoadPuzzles {
	
	/** The logic puzzles. */
	private LogicPuzzle logicPuzzles;
	
	/**
	 * Instantiates a new load puzzles.
	 */
	public LoadPuzzles() {
		logicPuzzles = new LogicPuzzle();
	}
	
	/**
	 * Gets the logic puzzles.
	 *
	 * @return the logic puzzles
	 */
	public LogicPuzzle getLogicPuzzles() {
		return logicPuzzles;
	}

	/**
	 * Import puzzles.
	 *
	 * @throws JAXBException the JAXB exception
	 */
	public void importPuzzles() throws JAXBException {
		logicPuzzles = new LogicPuzzle();
		JAXBContext jaxbContext = JAXBContext.newInstance(LogicPuzzle.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		logicPuzzles = (LogicPuzzle) jaxbUnmarshaller.unmarshal( new File("src/main/resources/logicpuzzles.xml") );	
	}

}
