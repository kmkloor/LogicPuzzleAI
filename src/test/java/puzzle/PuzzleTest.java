package puzzle;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PuzzleTest {
	private static LoadPuzzles loadPuzzles;
	private static Puzzle puzzle;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		loadPuzzles = new LoadPuzzles();
		loadPuzzles.importPuzzles();
		puzzle = loadPuzzles.getLogicPuzzles().getPuzzles().get(0);
		puzzle.buildPuzzle();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}


	@After
	public void tearDown() throws Exception {
	}
	
	public void setUp(int i) {
		puzzle = loadPuzzles.getLogicPuzzles().getPuzzles().get(i);
		puzzle.buildPuzzle();
	}

	@Test
	public final void testGetId() {
		setUp(0);
		assertEquals(0, puzzle.getId(), "Puzzle Id = 0" );
	}


	@Test
	public final void testGetTitle() {
		setUp(0);
		assertEquals("Mixtape", puzzle.getTitle(),"Puzzle Name = Mixtape");
	}

	@Test
	public final void testGetStory() {
		setUp(0);
		String str = "Powerhouse Records is about to release its first charity album next month. Each track on the album is by a different band, and all proceeds directly benefit the California SPCA. Using only the clues that follow, match each track to its band, title and genre, and determine its order on the album.";
		assertEquals(str, puzzle.getStory(),"Puzzle Story = Powerhouse Records...");
	}

	@Test
	public final void testGetRawClues() {
		setUp(0);
		String clue = "The first track is \"Can't Escape\".";
		String clue1 = "The fourth track is either \"Poorly Worded\" or Gizmo Glory's track.";
		String clue2 = "Maniac Android's song is 1 track after \"Anything Else\".";
		String clue3 = "\"Beverly\" is sometime after \"Anything Else\".";
		String clue4 = "Abacus's track is either \"Beverly\" or \"Can't Escape\".";
		String clue5 = "Gizmo Glory's song is \"Anything Else\".";
		List<String> rawClues = puzzle.getRawClues();
		assertEquals(6, rawClues.size(), "6 clues" );
		assertEquals(clue, rawClues.get(0),"Clue 1");
		assertEquals(clue1, rawClues.get(1),"Clue 1");
		assertEquals(clue2, rawClues.get(2),"Clue 2");
		assertEquals(clue3, rawClues.get(3),"Clue 3");
		assertEquals(clue4, rawClues.get(4),"Clue 4");
		assertEquals(clue5, rawClues.get(5),"Clue 5");
	}

	@Test
	public final void testGetProcessedClues() {
		setUp(0);
		String clue = "[first]=[Can't Escape]";
		String clue1 = "[fourth]=[Gizmo Glory][Poorly Worded]";
		String clue2 = "[Maniac Android] 1.0>[Anything Else]";
		String clue3 = "[Beverly]>[Anything Else]";
		String clue4 = "[Abacus]=[Beverly][Can't Escape]";
		List<Clue> processsedClues = puzzle.getProcessedClues();
		assertEquals(6, processsedClues.size(), "6 clues" );
		assertEquals(clue, processsedClues.get(0).toString(),"Clue 0");
		assertEquals(clue1, processsedClues.get(1).toString(),"Clue 1");
		assertEquals(clue2, processsedClues.get(2).toString(),"Clue 2");
		assertEquals(clue3, processsedClues.get(3).toString(),"Clue 3");
		assertEquals(clue4, processsedClues.get(4).toString(),"Clue 4");
		setUp(1);
		processsedClues = puzzle.getProcessedClues();
		String clue5 = "[Jeffrey]=[gray]";
		assertEquals(clue5, processsedClues.get(3).toString(),"Clue 2");
		setUp(8);
		processsedClues = puzzle.getProcessedClues();
		String clue6 = "[green & red] 12.0<[horned viper]";
		assertEquals(clue6, processsedClues.get(3).toString(),"Clue 3");
	}
	


	@Test
	public final void testGetCategories() {
		setUp(0);
		List<Category> categories = puzzle.getCategories();
		assertEquals("band names", categories.get(0).getName(),"Category 1");
		assertEquals("song titles", categories.get(1).getName(),"Category 1");
		assertEquals("order", categories.get(2).getName(),"Category 1");
	}

	@Test
	public final void testDetermineSteps() {
		setUp(0);
		List<Category> categories = puzzle.getCategories();
		assertEquals(0.0, categories.get(1).getStep() ,"Test no step");
		assertEquals(1.0, categories.get(2).getStep() ,"Test ranked");
		setUp(2);
		categories = puzzle.getCategories();
		assertEquals(5000.0, categories.get(0).getStep() ,"Test large");
		setUp(3);
		categories = puzzle.getCategories();
		assertEquals(5.0, categories.get(2).getStep() ,"Test money");
		setUp(4);
		categories = puzzle.getCategories();
		assertEquals(1.0, categories.get(2).getStep() ,"Test time");
	}

	@Test
	public final void testGetCategoryfromItem() {
		setUp(0);
		Category cat = puzzle.getCategoryfromItem("fourth");
		assertEquals("order", cat.getName() ,"Category = order");
		Category cat2 = puzzle.getCategoryfromItem("pizza");
		assertEquals(null, cat2 ,"Null category");
	}

}
