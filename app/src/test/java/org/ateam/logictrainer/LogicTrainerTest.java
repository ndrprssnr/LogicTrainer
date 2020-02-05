package org.ateam.logictrainer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 31.01.2020.
 *
 * @author Andre
 */
public class LogicTrainerTest {

	@Test
	public void testInitialGameIsNotWon() {
		LogicTrainer trainer = new LogicTrainer(new Options());
		assertFalse(trainer.isWon());
	}

	@Test
	public void testInitialGameIsNotGameOver() {
		LogicTrainer trainer = new LogicTrainer(new Options());
		assertTrue(trainer.isNotGameOver());
	}

	@Test
	public void testInitialGameCannotCheck() {
		LogicTrainer trainer = new LogicTrainer(new Options());
		assertFalse(trainer.canCheck());
	}

	@Test
	public void testInitialGameMakeStep() {
		LogicTrainer trainer = new LogicTrainer(new Options());
		LogicTrainer.Result result = trainer.makeStep();
		assertNotNull(result);
		assertEquals(0, result.black);
		assertEquals(0, result.white);
	}

	@Test
	public void testInitialGameHasEmptyCodebreakerPanel() {
		LogicTrainer trainer = new LogicTrainer(new Options());
		assertEquals(0, trainer.getCodebreakerPanels().size());
	}

}