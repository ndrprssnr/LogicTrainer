package org.ateam.logictrainer;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

/**
 * Created on 07.02.2021.
 *
 * @author Andre
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

	@Test
	public void useAppContext() {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		assertEquals("org.ateam.logictrainer", appContext.getPackageName());
	}

}
