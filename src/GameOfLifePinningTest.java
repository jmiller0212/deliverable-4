import org.junit.*;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

public class GameOfLifePinningTest {
	/*
	 * READ ME: You may need to write pinning tests for methods from multiple
	 * classes, if you decide to refactor methods from multiple classes.
	 * 
	 * In general, a pinning test doesn't necessarily have to be a unit test; it can
	 * be an end-to-end test that spans multiple classes that you slap on quickly
	 * for the purposes of refactoring. The end-to-end pinning test is gradually
	 * refined into more high quality unit tests. Sometimes this is necessary
	 * because writing unit tests itself requires refactoring to make the code more
	 * testable (e.g. dependency injection), and you need a temporary end-to-end
	 * pinning test to protect the code base meanwhile.
	 * 
	 * For this deliverable, there is no reason you cannot write unit tests for
	 * pinning tests as the dependency injection(s) has already been done for you.
	 * You are required to localize each pinning unit test within the tested class
	 * as we did for Deliverable 2 (meaning it should not exercise any code from
	 * external classes). You will have to use Mockito mock objects to achieve this.
	 * 
	 * Also, you may have to use behavior verification instead of state verification
	 * to test some methods because the state change happens within a mocked
	 * external object. Remember that you can use behavior verification only on
	 * mocked objects (technically, you can use Mockito.verify on real objects too
	 * using something called a Spy, but you wouldn't need to go to that length for
	 * this deliverable).
	 */

	MainPanel mp;
	Cell[][] cells;

	@Before
	public void setUp() {
		/*
		 * Initialize the text fixture. For the initial pattern, use the "blinker"
		 * pattern shown in:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Examples_of_patterns
		 * The actual pattern GIF is at:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#/media/File:Game_of_life_blinker.gif
		 * Start from the vertical bar on a 5X5 matrix as shown in the GIF.
		 */
		cells = new Cell[5][5];
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				Cell c = mock(Cell.class);
				if(j == 2 && (i == 1 || i == 2 || i == 3)) {
					when(c.getAlive()).thenReturn(true);
					when(c.toString()).thenReturn("X");
				} else {
					when(c.getAlive()).thenReturn(false);
					when(c.toString()).thenReturn(".");
				}
				doNothing().when(c).setAlive(true);
				doNothing().when(c).setAlive(false);
				cells[i][j] = c;
			}
		}
		mp = new MainPanel(5);
		mp.clear();
		mp.setCells(cells);
	}
	
	@After
	public void tearDown() { }

	@Test
	public void testIterateCell() {
		assertTrue(mp.iterateCell(2, 1));
		assertTrue(mp.iterateCell(2, 2));
		assertTrue(mp.iterateCell(2, 3));
		assertFalse(mp.iterateCell(1, 2));
		assertFalse(mp.iterateCell(3, 2));
	}
	
	@Test
	public void testCalculateNextIteration() {
		mp.calculateNextIteration();
		verify(cells[2][1], times(1)).setAlive(true);
		verify(cells[2][2], times(1)).setAlive(true);
		verify(cells[2][3], times(1)).setAlive(true);
		verify(cells[1][2], times(1)).setAlive(false);
		verify(cells[3][2], times(1)).setAlive(false);
	}
	
	@Test
	public void testCellToString() {
		Cell c = new Cell(true);
		assertEquals("X", c.toString());
	}
}
