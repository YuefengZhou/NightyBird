package entities;

public class PreferenceManager {
	private static PreferenceManager instance = null;
	private PreferenceManager() {
		
	}
	public static PreferenceManager getInstance() {
		if (instance == null)
			instance = new PreferenceManager();
		return instance;
	}
	
}
