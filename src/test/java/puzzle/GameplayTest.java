package puzzle;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameplayTest {
	private static Gameplay game;
	private String p = "*****";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		game = new Gameplay();
	}
	
	public void setUp(int i) {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public final void testSolvePuzzle() {
		
		game.setUp(0, p);
		String s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		game.setUp(1, p);
		s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		game.setUp(2, p);
		s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		game.setUp(3, p);
		s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		game.setUp(4, p);
		s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		game.setUp(5, p);
		s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		game.setUp(6, p);
		s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		game.setUp(7, p);
		s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		game.setUp(8, p);
		s = game.solvePuzzle();
		assertEquals("Puzzle solved!", s, "Puzzle solved");
		
	}

	@Test
	public final void testTimeOut() {
		game.setUp(9, p);
		String s = game.solvePuzzle();
		assertEquals("Puzzle timeout", s, "Puzzle solved");
	}

}
