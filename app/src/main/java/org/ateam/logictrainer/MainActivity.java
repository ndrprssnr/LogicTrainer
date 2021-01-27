package org.ateam.logictrainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.ateam.logictrainer.LogicTrainer.PlayColors;
import org.ateam.logictrainer.LogicTrainer.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements OnGesturePerformedListener {

	private static final String ACTION_NEW = "action_new";
	private static final String ACTION_LEFT = "action_left";
	private static final String ACTION_OPTIONS = "action_options";

	private static final int[] PLAYCHOICE_IDS = new int[]{R.id.play_choice_textview1, R.id.play_choice_textview2, R.id.play_choice_textview3, R.id.play_choice_textview4};
	private static final Map<Integer, PlayColors> viewIdToColorMap;
	static {
		viewIdToColorMap = new HashMap<>();
		viewIdToColorMap.put(R.id.color_chooser_red, PlayColors.Red);
		viewIdToColorMap.put(R.id.color_chooser_green, PlayColors.Green);
		viewIdToColorMap.put(R.id.color_chooser_blue, PlayColors.Blue);
		viewIdToColorMap.put(R.id.color_chooser_yellow, PlayColors.Yellow);
		viewIdToColorMap.put(R.id.color_chooser_orange, PlayColors.Orange);
		viewIdToColorMap.put(R.id.color_chooser_brown, PlayColors.Brown);
		viewIdToColorMap.put(R.id.color_chooser_empty, null);
	}

	private View codebreakerPanel;
	private int selectedIndex = -1;
	private View selectedColorView = null;
	private Map<PlayColors, Boolean> lockedColors = new HashMap<>();

	private GestureLibrary gestureLib;


	private OnClickListener getListener(final int index) {
		return v -> {
			final LogicTrainer trainer = ((LogicTrainerApplication) getApplicationContext()).getLogicTrainer();
			if (trainer.isNotGameOver()) {
				if (selectedIndex > -1) {
					setCodebreakerButton(selectedIndex, false);
				}
				selectedIndex = index;
				selectedColorView = v;
				setCodebreakerButton(index, true);
				final View colorChooserView = findViewById(R.id.colorchooser);
				colorChooserView.setVisibility(View.VISIBLE);
			}
		};
	}

	private void initColorChooserPanel() {

		final View colorChooserView = findViewById(R.id.colorchooser);
		colorChooserView.setVisibility(View.GONE);

		OnClickListener listener = v -> {
			final LogicTrainer trainer = ((LogicTrainerApplication) getApplicationContext()).getLogicTrainer();
			PlayColors color = viewIdToColorMap.getOrDefault(v.getId(), null);
			if (!lockedColors.getOrDefault(color, false)) {
				trainer.setPlayerChoice(selectedIndex - 1, color);
				setPlayColor(selectedColorView, color);

				Button checkButton = findViewById(R.id.check_button);
				checkButton.setEnabled(trainer.canCheck());
				selectedIndex = -1;
				selectedColorView = null;
				colorChooserView.setVisibility(View.GONE);
			}
		};

		View.OnLongClickListener longClickListener = v -> {
			PlayColors color = viewIdToColorMap.getOrDefault(v.getId(), null);
			if (color == null) {
				return true;
			}
			if (!lockedColors.getOrDefault(color, false)) {
				lockedColors.put(color, true);
				lockColor((ImageView) v);
			} else {
				lockedColors.put(color, false);
				unlockColor((ImageView)v);
			}
			return true;
		};
		setColorChooserListeners(colorChooserView, R.id.color_chooser_red, listener, longClickListener);
		setColorChooserListeners(colorChooserView, R.id.color_chooser_green, listener, longClickListener);
		setColorChooserListeners(colorChooserView, R.id.color_chooser_blue, listener, longClickListener);
		setColorChooserListeners(colorChooserView, R.id.color_chooser_yellow, listener, longClickListener);
		setColorChooserListeners(colorChooserView, R.id.color_chooser_orange, listener, longClickListener);
		setColorChooserListeners(colorChooserView, R.id.color_chooser_brown, listener, longClickListener);
		setColorChooserListeners(colorChooserView, R.id.color_chooser_empty, listener, longClickListener);
	}

	private void setColorChooserListeners(View colorChooserView, int colorViewId, OnClickListener onClickListener, View.OnLongClickListener longClickListener) {
		View view;
		view = colorChooserView.findViewById(colorViewId);
		view.setOnClickListener(onClickListener);
		view.setOnLongClickListener(longClickListener);
	}

	private void resetColorChooser() {
		lockedColors.clear();
		final View colorChooserView = findViewById(R.id.colorchooser);
		for (int viewId : viewIdToColorMap.keySet()) {
			unlockColor((ImageView) colorChooserView.findViewById(viewId));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_game:
				newGame();
				return true;
			case R.id.show_solution:
				showSolution();
				return true;
			case R.id.options:
				this.startActivity(new Intent(this, OptionsActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.main2, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			finish();
		}
		setContentView(gestureOverlayView);
		init();
	}


	//	@Override
	//	public void onBackPressed() {
	//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	//		builder.setMessage("Are you sure you want to exit?")
	//		       .setCancelable(false)
	//		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	//		           public void onClick(DialogInterface dialog, int id) {
	//		                MainActivity.this.finish();
	//		           }
	//		       })
	//		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	//		           public void onClick(DialogInterface dialog, int id) {
	//		                dialog.cancel();
	//		           }
	//		       });
	//		AlertDialog alert = builder.create();
	//		alert.show();
	//	}


	public void init() {
		LogicTrainer trainer = ((LogicTrainerApplication) getApplicationContext()).getLogicTrainer();

		final LinearLayout playfield = findViewById(R.id.codebreaker_panels);

		initCodebreakerPanel(trainer.getCurrentCodebreakerPanel());
		initColorChooserPanel();

		Button checkButton = findViewById(R.id.check_button);
		checkButton.setEnabled(trainer.canCheck());
		checkButton.setOnClickListener(v -> {
			Button checkButton1 = findViewById(R.id.check_button);
			checkButton1.setEnabled(false);
			LogicTrainer trainer1 = ((LogicTrainerApplication) getApplicationContext()).getLogicTrainer();
			if (trainer1.isNotGameOver()) { // check should be unnecessary, but just in case...
				PlayColors[] codebreakerPanel = trainer1.getCurrentCodebreakerPanel();
				Result eval = trainer1.makeStep();
				addLine(playfield, codebreakerPanel, eval);
				if (trainer1.isNotGameOver()) {
					resetCodebreakerPanel();
				} else {
					if (trainer1.isWon()) {
						Toast.makeText(getApplicationContext(), getString(R.string.win), Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), getString(R.string.lost), Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		List<PlayColors[]> playerPanels = trainer.getCodebreakerPanels();
		List<Result> evals = trainer.getResults();
		if (playerPanels.size() == evals.size()) {
			for (int i = 0; i < playerPanels.size(); i++) {
				addLine(playfield, playerPanels.get(i), evals.get(i));
			}
		}
	}

	private void newGame() {
		((LogicTrainerApplication) getApplicationContext()).resetLogicTrainer();
		resetCodebreakerPanel();
		resetColorChooser();
		final LinearLayout playfield = findViewById(R.id.codebreaker_panels);
		playfield.removeAllViews();
		Button checkButton = findViewById(R.id.check_button);
		checkButton.setEnabled(false);
		LinearLayout solutionView = findViewById(R.id.codemaker_panel);
		solutionView.removeAllViews();
	}

	private void showSolution() {
		LinearLayout solutionView = findViewById(R.id.codemaker_panel);
		// delete old solution views, if any
		solutionView.removeAllViews();

		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.line_rdo, null);
		LogicTrainer trainer = ((LogicTrainerApplication) getApplicationContext()).getLogicTrainer();
		PlayColors[] target = trainer.getCodemakerPanel();
		for (int i = 0; i < 4; i++) {
			View playChoiceView = view.findViewById(PLAYCHOICE_IDS[i]);
			PlayColors color = target[i];
			setPlayColor(playChoiceView, color);
		}
		view.findViewById(R.id.eval_black_textview).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.eval_white_textview).setVisibility(View.INVISIBLE);
		solutionView.addView(view);
	}


	private void addLine(final LinearLayout playfield, final PlayColors[] playerChoice, final Result eval) {
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.line_rdo, null);
		for (int i = 0; i < 4; i++) {
			View playChoiceView = view.findViewById(PLAYCHOICE_IDS[i]);
			PlayColors color = playerChoice[i];
			setPlayColor(playChoiceView, color);
		}
		((TextView) view.findViewById(R.id.eval_black_textview)).setText(String.format("%s", eval.black));
		((TextView) view.findViewById(R.id.eval_white_textview)).setText(String.format("%s", eval.white));
		playfield.addView(view);

		// automatically scroll to last element in the scroll view 
		final ScrollView scrollView = findViewById(R.id.playfield_scrollview);
		scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
	}

	private void setPlayColor(View playChoiceView, PlayColors color) {
		Resources res = getResources();
		Drawable shape = null;
		if (color != null) {
			switch (color) {
				case Red:
					shape = res.getDrawable(R.drawable.red_circle);
					break;
				case Green:
					shape = res.getDrawable(R.drawable.green_circle);
					break;
				case Blue:
					shape = res.getDrawable(R.drawable.blue_circle);
					break;
				case Yellow:
					shape = res.getDrawable(R.drawable.yellow_circle);
					break;
				case Brown:
					shape = res.getDrawable(R.drawable.brown_circle);
					break;
				case Orange:
					shape = res.getDrawable(R.drawable.orange_circle);
					break;
			}
		} else {
			shape = res.getDrawable(R.drawable.empty_circle);
		}

		if (shape != null) {
			playChoiceView.setBackground(shape);
		}
	}


	private void initCodebreakerPanel(PlayColors[] playerChoice) {
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		codebreakerPanel = inflater.inflate(R.layout.line, null);
		if (playerChoice != null) {
			for (int i = 0; i < 4; i++) {
				if (playerChoice[i] != null) {
					View playChoiceView = codebreakerPanel.findViewById(PLAYCHOICE_IDS[i]);
					PlayColors color = playerChoice[i];
					setPlayColor(playChoiceView, color);
				}
			}
		}
		LinearLayout codebreakerPanelContainer = findViewById(R.id.codebreaker_panel);
		codebreakerPanelContainer.addView(codebreakerPanel);

		TextView v1 = codebreakerPanel.findViewById(R.id.play_choice_textview1);
		v1.setOnClickListener(getListener(1));
		TextView v2 = codebreakerPanel.findViewById(R.id.play_choice_textview2);
		v2.setOnClickListener(getListener(2));
		TextView v3 = codebreakerPanel.findViewById(R.id.play_choice_textview3);
		v3.setOnClickListener(getListener(3));
		TextView v4 = codebreakerPanel.findViewById(R.id.play_choice_textview4);
		v4.setOnClickListener(getListener(4));
	}

	public void resetCodebreakerPanel() {
		TextView v1 = codebreakerPanel.findViewById(R.id.play_choice_textview1);
		v1.setBackgroundResource(R.drawable.empty_circle);
		TextView v2 = codebreakerPanel.findViewById(R.id.play_choice_textview2);
		v2.setBackgroundResource(R.drawable.empty_circle);
		TextView v3 = codebreakerPanel.findViewById(R.id.play_choice_textview3);
		v3.setBackgroundResource(R.drawable.empty_circle);
		TextView v4 = codebreakerPanel.findViewById(R.id.play_choice_textview4);
		v4.setBackgroundResource(R.drawable.empty_circle);

		LogicTrainer trainer = ((LogicTrainerApplication) getApplicationContext()).getLogicTrainer();
		trainer.resetPlayerPanel();
		Button checkButton = findViewById(R.id.check_button);
		checkButton.setEnabled(trainer.canCheck());
		selectedIndex = -1;
	}

	private void setCodebreakerButton(int index, boolean pressed) {
		TextView view = null;
		switch (index) {
			case 1:
				view = codebreakerPanel.findViewById(R.id.play_choice_textview1);
				break;
			case 2:
				view = codebreakerPanel.findViewById(R.id.play_choice_textview2);
				break;
			case 3:
				view = codebreakerPanel.findViewById(R.id.play_choice_textview3);
				break;
			case 4:
				view = codebreakerPanel.findViewById(R.id.play_choice_textview4);
				break;
		}

		int drawable = (pressed) ? R.drawable.empty_circle_pressed : R.drawable.empty_circle;
		LogicTrainer trainer = ((LogicTrainerApplication) getApplicationContext()).getLogicTrainer();
		if (trainer.getCurrentCodebreakerPanel()[index - 1] != null) {
			switch (trainer.getCurrentCodebreakerPanel()[index - 1]) {
				case Red:
					drawable = (pressed) ? R.drawable.red_circle_pressed : R.drawable.red_circle;
					break;
				case Green:
					drawable = (pressed) ? R.drawable.green_circle_pressed : R.drawable.green_circle;
					break;
				case Blue:
					drawable = (pressed) ? R.drawable.blue_circle_pressed : R.drawable.blue_circle;
					break;
				case Yellow:
					drawable = (pressed) ? R.drawable.yellow_circle_pressed : R.drawable.yellow_circle;
					break;
				case Brown:
					drawable = (pressed) ? R.drawable.brown_circle_pressed : R.drawable.brown_circle;
					break;
				case Orange:
					drawable = (pressed) ? R.drawable.orange_circle_pressed : R.drawable.orange_circle;
					break;
			}
		}
		view.setBackgroundResource(drawable);
	}

	@Override
	protected void onPause() {
		super.onPause();
		((LogicTrainerApplication) getApplicationContext()).save();
	}


	@Override
	protected void onResume() {
		super.onResume();
		((LogicTrainerApplication) getApplicationContext()).load();
	}


	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		String highestName = "";
		double highestScore = 0.0;
		for (Prediction prediction : predictions) {
			if (prediction.score > highestScore) {
				highestName = prediction.name;
				highestScore = prediction.score;
			}
		}
		if ((highestName.equals(ACTION_NEW) && highestScore > 2.0)) {
			Toast.makeText(this, getString(R.string.add_new_game), Toast.LENGTH_SHORT).show();
			newGame();
		} else if ((highestName.equals(ACTION_LEFT) && highestScore > 15.0)) {
			resetCodebreakerPanel();
		} else if ((highestName.equals(ACTION_OPTIONS) && highestScore > 2.0)) {
			this.startActivity(new Intent(this, OptionsActivity.class));
		}
	}

	private void lockColor(ImageView imageView) {
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation(.8f);

		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		imageView.setColorFilter(filter);
		imageView.setImageAlpha(64);
	}

	private void unlockColor(ImageView imageView) {
		imageView.clearColorFilter();
		imageView.setImageAlpha(255);
	}
}