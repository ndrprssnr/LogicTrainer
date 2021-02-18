package org.ateam.logictrainer;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created on 31.01.2020.
 *
 * @author Andre
 */
public class LogicTrainerTest {

	private LogicTrainer trainer;

	@Before
	public void before() {
		trainer = new LogicTrainer(new Options());
	}

	@Test
	public void testInitialGameIsNotWon() {
		assertFalse(trainer.isWon());
	}

	@Test
	public void testInitialGameIsNotGameOver() {
		assertTrue(trainer.isNotGameOver());
	}

	@Test
	public void testInitialGameCannotCheck() {
		assertFalse(trainer.canCheck());
	}

	@Test
	public void testInitialGameMakeStep() {
		LogicTrainer.Result result = trainer.makeStep();
		assertNotNull(result);
		assertEquals(0, result.black);
		assertEquals(0, result.white);
	}

	@Test
	public void testInitialGameHasEmptyCodebreakerPanel() {
		assertEquals(0, trainer.getCodebreakerPanels().size());
	}

	@Test
	public void testCanCheck() {
		trainer.setPlayerChoice(0, LogicTrainer.PlayColors.Red);
		trainer.setPlayerChoice(1, LogicTrainer.PlayColors.Red);
		trainer.setPlayerChoice(2, LogicTrainer.PlayColors.Red);
		trainer.setPlayerChoice(3, LogicTrainer.PlayColors.Red);

		assertTrue(trainer.canCheck());
	}

	@Test
	public void testReset() {
		trainer.setPlayerChoice(0, LogicTrainer.PlayColors.Red);
		trainer.setPlayerChoice(1, LogicTrainer.PlayColors.Red);
		trainer.setPlayerChoice(2, LogicTrainer.PlayColors.Red);
		trainer.setPlayerChoice(3, LogicTrainer.PlayColors.Red);

		trainer.resetPlayerPanel();
		assertFalse(trainer.canCheck());
	}

	@Test
	public void testCreateLogicTrainerWithValidCodemakerPanel() {
		LogicTrainer.PlayColors[] codemakerPanel = new LogicTrainer.PlayColors[4];
		Arrays.fill(codemakerPanel, LogicTrainer.PlayColors.Red);
		Options options = new Options();

		LogicTrainer trainer = new LogicTrainer(options, codemakerPanel);

		assertEquals(options, trainer.getOptions());
		assertArrayEquals(codemakerPanel, trainer.getCodemakerPanel());
		assertArrayEquals(new LogicTrainer.PlayColors[]{ null, null, null, null }, trainer.getCurrentCodebreakerPanel());
	}

	@Test(expected = RuntimeException.class)
	public void testCreateLogicTrainerWithTooSmallCodemakerPanel() {
		LogicTrainer.PlayColors[] codemakerPanel = new LogicTrainer.PlayColors[3];
		Arrays.fill(codemakerPanel, LogicTrainer.PlayColors.Red);

		new LogicTrainer(new Options(), codemakerPanel);
	}

	@Test(expected = RuntimeException.class)
	public void testCreateLogicTrainerWithTooLargeCodemakerPanel() {
		LogicTrainer.PlayColors[] codemakerPanel = new LogicTrainer.PlayColors[5];
		Arrays.fill(codemakerPanel, LogicTrainer.PlayColors.Red);

		new LogicTrainer(new Options(), codemakerPanel);
	}
}