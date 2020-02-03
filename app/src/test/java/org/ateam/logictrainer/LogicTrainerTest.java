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

}