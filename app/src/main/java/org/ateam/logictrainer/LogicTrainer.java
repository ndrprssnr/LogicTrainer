package org.ateam.logictrainer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class LogicTrainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7554367250243134431L;

	public enum PlayColors {
		Red, Green, Blue, Orange, Yellow, Brown,
	}
	
	public static class Result implements Serializable {
		private static final long serialVersionUID = -3089720496979911502L;
		byte black = 0;
		byte white = 0;

		public Result(byte black, byte white) {
			this.black = black;
			this.white = white;
		}
	}

	private transient Options options;

	public Options getOptions() {
		return options;
	}
	void setOptions(Options options) {
		this.options = options;
	}

	private PlayColors[] codemakerPanel = new PlayColors[4];
	PlayColors[] getCodemakerPanel() {
		return codemakerPanel;
	}

	private List<PlayColors[]> codebreakerPanels = new ArrayList<LogicTrainer.PlayColors[]>(Options.MAX_STEP);
	List<PlayColors[]> getCodebreakerPanels() {
		return codebreakerPanels;
	}

	private List<Result> results = new ArrayList<LogicTrainer.Result>(Options.MAX_STEP);
	List<Result> getResults() {
		return results;
	}

	private PlayColors[] currentCodebreakerPanel = new PlayColors[4];
	PlayColors[] getCurrentCodebreakerPanel() {
		return currentCodebreakerPanel;
	}

	private int step = 0;
    private boolean gameOver = false;
    private boolean won = false;

	boolean isWon() {
		return won;
	}

	boolean isNotGameOver() {
		return !gameOver;
	}

	LogicTrainer(Options options) {
		super();
		this.options = options;
		initTargetPanel();
		resetPlayerPanel();
	}

	LogicTrainer(Options options, PlayColors[] codemakerPanel) {
		if (codemakerPanel.length != 4) {
			throw new RuntimeException("Invalid codemaker panel size.");
		}
		this.options = options;
		this.codemakerPanel = codemakerPanel;
	}

	private void initTargetPanel() {
		List<PlayColors> playColors = Arrays.asList(PlayColors.values());
		if (options.isCodemakerDuplicateColorsAllowed()) {
			Random r = new Random();
			for (int i = 0; i < 4; i++) {
				codemakerPanel[i] = playColors.get(r.nextInt(playColors.size()));
			}
		} else {
			Collections.shuffle(playColors);
			for (int i = 0; i < 4; i++) {
				codemakerPanel[i] = playColors.get(i);
			}
		}
	}
	
	void resetPlayerPanel() {
		for (int i = 0; i < 4; i++) {
			currentCodebreakerPanel[i] = null;
		}
	}
	
	void setPlayerChoice(int index, PlayColors chosenColor) {
		if (index >= 0 && index < 4) {
			currentCodebreakerPanel[index] = chosenColor;
		}
	}
	
	public boolean canCheck() {
		for (PlayColors color : currentCodebreakerPanel) {
			if (color == null) {
				return false;
			}
		}
		return true;
	}
	
	private Result check() {
		byte black = 0;
		byte white = 0;
		boolean[] matchedIndices = new boolean[4];
		Arrays.fill(matchedIndices, false);
		// find all black matches first
		for (int i = 0; i < 4; i++) {
			if (codemakerPanel[i].equals(currentCodebreakerPanel[i])) {
				black++;
				matchedIndices[i] = true;
			}
		}
		//then, if necessary, find white matches
		if (black < 4) {
			for (int j = 0; j < 4; j++) {
				// skip black matches
				if (codemakerPanel[j].equals(currentCodebreakerPanel[j])) continue;
				for (int i = 0; i < 4; i++) {
					// skip already matched indices
					if (matchedIndices[i]) continue;
					if (j != i && codemakerPanel[i].equals(currentCodebreakerPanel[j])) {
						white++;
						matchedIndices[i] = true;
						break;
					}
				}
			}
		}
		return new Result(black, white);
	}
	
	public Result makeStep() {
		Result result = check();
		codebreakerPanels.add(currentCodebreakerPanel);
		results.add(result);
		if (result.black == 4) {
			won = true;
			gameOver = true;
		} else if (options.isUnlimitedTrials() || step < Options.MAX_STEP - 1) {
			step++;
			currentCodebreakerPanel = new PlayColors[4];
		} else {
			gameOver = true;
		}
		
		return result;
	}

	int getRoundNumber() {
		return step + 1;
	}
}
