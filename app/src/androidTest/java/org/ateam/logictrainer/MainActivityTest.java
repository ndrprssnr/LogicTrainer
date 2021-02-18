package org.ateam.logictrainer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.HasBackgroundMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
public class MainActivityTest {
	@Rule
	public ActivityScenarioRule<MainActivity> mainActivityRule = new ActivityScenarioRule<>(MainActivity.class);

	private static final ViewAssertion matchesEmptyCircle = matchesBackground(R.drawable.empty_circle);
	private static final ViewAssertion matchesEmptyCirclePressed = matchesBackground(R.drawable.empty_circle_pressed);
	private static final ViewAssertion matchesRedCircle = matchesBackground(R.drawable.red_circle);
	private static final ViewAssertion matchesRedCirclePressed = matchesBackground(R.drawable.red_circle_pressed);
	private static final ViewAssertion matchesGreenCircle = matchesBackground(R.drawable.green_circle);
	private static final ViewAssertion matchesGreenCirclePressed = matchesBackground(R.drawable.green_circle_pressed);
	private static final ViewAssertion matchesBlueCircle = matchesBackground(R.drawable.blue_circle);
	private static final ViewAssertion matchesBlueCirclePressed = matchesBackground(R.drawable.blue_circle_pressed);
	private static final ViewAssertion matchesYellowCircle = matchesBackground(R.drawable.yellow_circle);
	private static final ViewAssertion matchesYellowCirclePressed = matchesBackground(R.drawable.yellow_circle_pressed);
	private static final ViewAssertion matchesOrangeCircle = matchesBackground(R.drawable.orange_circle);
	private static final ViewAssertion matchesOrangeCirclePressed = matchesBackground(R.drawable.orange_circle_pressed);
	private static final ViewAssertion matchesBrownCircle = matchesBackground(R.drawable.brown_circle);
	private static final ViewAssertion matchesBrownCirclePressed = matchesBackground(R.drawable.brown_circle_pressed);

	private static ViewAssertion matchesBackground(int drawableId) {
		return matches(new HasBackgroundMatcher(drawableId));
	}

	@Before
	public void before() {
		((LogicTrainerApplication)InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext()).resetLogicTrainer();
		mainActivityRule.getScenario().recreate();
	}

	@Test
	public void testGameStart() {
		onView(withId(R.id.check_button)).check(matches(isDisplayed())).check(matches(not(isEnabled())));
	}

	@Test
	public void testFirstPlaceIsSelected() {
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesEmptyCirclePressed);
		onView(withId(R.id.codebreaker_panel_place1)).check(matchesEmptyCircle);
		onView(withId(R.id.codebreaker_panel_place2)).check(matchesEmptyCircle);
		onView(withId(R.id.codebreaker_panel_place3)).check(matchesEmptyCircle);
	}

	@Test
	public void testPlaceCanBeSelected() {
		onView(withId(R.id.codebreaker_panel_place1)).check(matchesEmptyCircle).perform(click()).check(matchesEmptyCirclePressed);
		onView(withId(R.id.codebreaker_panel_place2)).check(matchesEmptyCircle).perform(click()).check(matchesEmptyCirclePressed);
		onView(withId(R.id.codebreaker_panel_place3)).check(matchesEmptyCircle).perform(click()).check(matchesEmptyCirclePressed);
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesEmptyCircle).perform(click()).check(matchesEmptyCirclePressed);
	}

	@Test
	public void testCanChooseColor() {
		onView(withId(R.id.color_chooser_red)).perform(click());
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesRedCircle).perform(click()).check(matchesRedCirclePressed);
		onView(withId(R.id.color_chooser_green)).perform(click());
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesGreenCircle).perform(click()).check(matchesGreenCirclePressed);
		onView(withId(R.id.color_chooser_blue)).perform(click());
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesBlueCircle).perform(click()).check(matchesBlueCirclePressed);
		onView(withId(R.id.color_chooser_yellow)).perform(click());
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesYellowCircle).perform(click()).check(matchesYellowCirclePressed);
		onView(withId(R.id.color_chooser_orange)).perform(click());
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesOrangeCircle).perform(click()).check(matchesOrangeCirclePressed);
		onView(withId(R.id.color_chooser_brown)).perform(click());
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesBrownCircle).perform(click()).check(matchesBrownCirclePressed);
	}
}
