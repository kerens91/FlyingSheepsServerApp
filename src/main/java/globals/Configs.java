package globals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * This class manages the properties access
 */
public class Configs {
	private Properties properties;
	private static Configs configs_instance = null;
	
	public static Configs getInstance() {
    	if (configs_instance == null) {
    		configs_instance = new Configs();
    	}
    	return configs_instance;
    }
	
	private Configs() {
		properties = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(Constants.CONFIGS_PATH);
			properties.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getStringProperty(String key) {
		String object = properties.getProperty(key);
		if (object == null) {
			printError("wrong property key " + key);
		}
		return object;
	}
	
	public int getIntProperty(String key) {
		String object = properties.getProperty(key);
		if (object == null) {
			printError("wrong property key " + key);
		}
		return Integer.valueOf(object);
	}
	
    private void printError(String print) {
    	System.out.println("PROPERTIES_ERROR: " + print);
    }

}
