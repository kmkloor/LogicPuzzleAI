package puzzle;

import javax.xml.bind.JAXBException;

public class Test {
	
	public static void main(String[] args) throws JAXBException {
	Gameplay game = new Gameplay();	
	LoadPuzzles loadPuzzles = new LoadPuzzles();
	loadPuzzles.importPuzzles();
	String password = "*****";
	for (Puzzle p : loadPuzzles.getLogicPuzzles().getPuzzles()) {
		game.setUp(p.getId(), password);
		game.solvePuzzle();
	}
	}


}

