package org.ateam.logictrainer;

import android.app.Application;
import android.content.Context;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LogicTrainerApplication extends Application {

	private static final String FILENAME = "logictrainer.savegame";
	private LogicTrainer logicTrainer;
	private Options options;


	@Override
	public void onCreate() {
		super.onCreate();
		
		load();
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
