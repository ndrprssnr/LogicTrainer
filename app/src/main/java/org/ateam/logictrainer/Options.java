package org.ateam.logictrainer;

import java.io.Serializable;

class Options implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4083420040347847882L;

	static final int MAX_STEP = 7;

	private boolean codemakerDuplicateColorsAllowed = true;
	private boolean unlimitedTrials = false;

	boolean isCodemakerDuplicateColorsAllowed() {
		return codemakerDuplicateColorsAllowed;
	}

	void setCodemakerDuplicateColorsAllowed(
			boolean codemakerDuplicateColorsAllowed) {
		this.codemakerDuplicateColorsAllowed = codemakerDuplicateColorsAllowed;
	}

	boolean isUnlimitedTrials() {
		return unlimitedTrials;
	}

	void setUnlimitedTrials(boolean unlimitedTrials) {
		this.unlimitedTrials = unlimitedTrials;
	}

}
