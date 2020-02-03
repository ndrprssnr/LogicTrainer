package org.ateam.logictrainer;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Application;
import android.content.Context;

public class LogicTrainerApplication extends Application {

	private static final String FILENAME = "logictrainer.savegame";
	private LogicTrainer logicTrainer;
	private Options options;


	@Override
	public void onCreate() {
		super.onCreate();
		
		((LogicTrainerApplication)getApplicationContext()).load();
		if (logicTrainer == null) {
			options = new Options();
			logicTrainer = new LogicTrainer(options);
		}
	}

	
	
	public LogicTrainer getLogicTrainer() {
		return logicTrainer;
	}
	
	public void resetLogicTrainer() {
		logicTrainer = new LogicTrainer(options);
	}
	
	public Options getOptions() {
		return options;
	}
	
//	private String playColorsToString(PlayColors[] colors) {
//		String s = new String();
//		for (int i = 0; i < colors.length; i++) {
//			s = colors[i].name() + ((i < colors.length - 1) ? VALUE_SEPARATOR : "");
//		}
//		return s;
//	}
//	
//	private String evalToString(Eval eval) {
//		return "" + eval.black + VALUE_SEPARATOR + eval.white;
//	}
	
	
	public void save() {
		try {
//			getExternalFilesDir(null);
			ObjectOutputStream oos = new ObjectOutputStream(openFileOutput(FILENAME, Context.MODE_PRIVATE));
			oos.writeObject(logicTrainer);
			oos.writeObject(options);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void load() {
		try {
			ObjectInputStream ois = new ObjectInputStream(openFileInput(FILENAME));
			Object object = ois.readObject();
			if (object instanceof LogicTrainer) {
				logicTrainer = (LogicTrainer)object;
			}
			object = ois.readObject();
			if (object instanceof Options) {
				options = (Options)object;
			} else {
				options = new Options();
			}
			logicTrainer.setOptions(options);
			ois.close();
		} catch (EOFException e) {
			// end of file, do nothing
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
