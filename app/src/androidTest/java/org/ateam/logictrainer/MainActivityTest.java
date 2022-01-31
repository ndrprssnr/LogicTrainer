package org.ateam.logictrainer;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
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

	private final ViewAssertion matchesEmptyCircle = matchesViewWithSrc(R.drawable.empty_circle);
	private final ViewAssertion matchesEmptyCirclePressed = matchesViewWithSrc(R.drawable.empty_circle_pressed);
	private final ViewAssertion matchesRedCircle = matchesViewWithSrc(R.drawable.red_circle);
	private final ViewAssertion matchesRedCirclePressed = matchesViewWithSrc(R.drawable.red_circle_pressed);
	private final ViewAssertion matchesGreenCircle = matchesViewWithSrc(R.drawable.green_circle);
	private final ViewAssertion matchesGreenCirclePressed = matchesViewWithSrc(R.drawable.green_circle_pressed);
	private final ViewAssertion matchesBlueCircle = matchesViewWithSrc(R.drawable.blue_circle);
	private final ViewAssertion matchesBlueCirclePressed = matchesViewWithSrc(R.drawable.blue_circle_pressed);
	private final ViewAssertion matchesYellowCircle = matchesViewWithSrc(R.drawable.yellow_circle);
	private final ViewAssertion matchesYellowCirclePressed = matchesViewWithSrc(R.drawable.yellow_circle_pressed);
	private final ViewAssertion matchesOrangeCircle = matchesViewWithSrc(R.drawable.orange_circle);
	private final ViewAssertion matchesOrangeCirclePressed = matchesViewWithSrc(R.drawable.orange_circle_pressed);
	private final ViewAssertion matchesBrownCircle = matchesViewWithSrc(R.drawable.brown_circle);
	private final ViewAssertion matchesBrownCirclePressed = matchesViewWithSrc(R.drawable.brown_circle_pressed);


	@Before
	public void before() {
		((LogicTrainerApplication) getApplicationContext()).resetLogicTrainer();
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

	@Test
	public void testCanDisableAndEnableColorChooser() {
		checkDisableAndEnableColor(R.id.color_chooser_red, matchesRedCircle);
		checkDisableAndEnableColor(R.id.color_chooser_green, matchesGreenCircle);
		checkDisableAndEnableColor(R.id.color_chooser_blue, matchesBlueCircle);
		checkDisableAndEnableColor(R.id.color_chooser_yellow, matchesYellowCircle);
		checkDisableAndEnableColor(R.id.color_chooser_orange, matchesOrangeCircle);
		checkDisableAndEnableColor(R.id.color_chooser_brown, matchesBrownCircle);
	}

	private void checkDisableAndEnableColor(int colorChooserId, ViewAssertion colorMatcher) {
		// long press to disable, check that click does not alter codebreaker panel
		onView(withId(colorChooserId)).perform(longClick()).perform(click());
		onView(withId(R.id.codebreaker_panel_place0)).check(matchesEmptyCirclePressed);
		// long press to enable, check that click alters codebreaker panel
		onView(withId(colorChooserId)).perform(longClick()).perform(click());
		onView(withId(R.id.codebreaker_panel_place0)).check(colorMatcher);
		// clean up first panel place
		onView(withId(R.id.codebreaker_panel_place0)).perform(click());
		onView(withId(R.id.color_chooser_empty)).perform(click());
	}

	private Context getApplicationContext() {
		return InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
	}

	private class ImageViewSrcMatcher extends BoundedMatcher<View, ImageView> {
		int drawableID;

		public ImageViewSrcMatcher(int drawableID) {
			super(ImageView.class);
			this.drawableID = drawableID;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("has image with drawable " + drawableID);
		}

		@Override
		protected boolean matchesSafely(ImageView item) {
			BitmapDrawable expected = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), drawableID);
			BitmapDrawable back = (BitmapDrawable) item.getDrawable();

			assert expected != null;
			return ((BitmapDrawable) expected).getBitmap().sameAs(back.getBitmap());
		}

	}

	private ViewAssertion matchesViewWithSrc(int drawableId) {
		return matches(new ImageViewSrcMatcher(drawableId));
	}

}
