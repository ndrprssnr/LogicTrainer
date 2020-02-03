package org.ateam.logictrainer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class OptionsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.options);
		
		final Options options = ((LogicTrainerApplication)getApplicationContext()).getOptions();
		
		final CheckBox trialsBox = findViewById(R.id.trials_checkBox);
		trialsBox.setChecked(options.isUnlimitedTrials());
		trialsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				options.setUnlimitedTrials(isChecked);
			}
		});

		final CheckBox codemakerColorsBox = findViewById(R.id.codemaker_colors_checkBox);
//		final CheckBox codebreakerColorsBox = (CheckBox)findViewById(R.id.codebreaker_colors_checkBox);
		codemakerColorsBox.setChecked(options.isCodemakerDuplicateColorsAllowed());
		codemakerColorsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				options.setCodemakerDuplicateColorsAllowed(isChecked);
    			
//    			// when the code maker can use duplicate colors, the code breaker must also be allowed to do so 
//    			if (isChecked) {
//    				codebreakerColorsBox.setChecked(true);
//    				LogicTrainer.setCodebreakerDuplicateColorsAllowed(true);
//    			}
			}
		});

//		codebreakerColorsBox.setChecked(LogicTrainer.isCodebreakerDuplicateColorsAllowed());
//		codebreakerColorsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//    			LogicTrainer.setCodebreakerDuplicateColorsAllowed(isChecked);
//
//    			// when the code breaker cannot use duplicate colors, the code maker must also not be allowed to do so 
//    			if (!isChecked) {
//    				codemakerColorsBox.setChecked(false);
//    				LogicTrainer.setCodemakerDuplicateColorsAllowed(false);
//    			}
//			}
//		});
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		((LogicTrainerApplication)getApplicationContext()).save();
	}


//	@Override
//	protected void onStart() {
//		super.onStart();
//		((LogicTrainerApplication)getApplicationContext()).loadOptions();
//	}


	
}
