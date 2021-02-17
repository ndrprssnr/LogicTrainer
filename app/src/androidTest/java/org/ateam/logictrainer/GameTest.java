package org.ateam.logictrainer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created on 07.02.2021.
 *
 * @author Andre
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameTest {
	@Rule
	public ActivityScenarioRule<MainActivity> mainActivityRule = new ActivityScenarioRule<>(MainActivity.class);

	@Before
	public void before() {
		((LogicTrainerApplication)InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext()).resetLogicTrainer();
		mainActivityRule.getScenario().recreate();
	}

	@Test
	public void testGameStart() {
		onView(withId(R.id.check_button)).check(matches(isDisplayed())).check(matches(not(isEnabled())));
	}
}
