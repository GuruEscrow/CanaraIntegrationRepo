package demon.libraries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * This class contains all reusable methods to perform actions
 * on Properties file 
 * @author Guruprasad
 */

public class PropertiesUtility {

	private Properties property;

	/*
	 * This method is used to initialize properties file
	 * 
	 * @param filePath
	 */

	public void propertiesInit(String filePath) {

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		property = new Properties();
		try {
			property.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method is used to fetch data from properties file
	 * 
	 * @param key
	 * 
	 * @return
	 */

	public String readData(String key) {

		return property.getProperty(key);
	}

	/*
	 * This method is used to write and save data to properties file
	 * 
	 * @param key
	 * 
	 * @param value
	 * 
	 * @param filePath
	 */

	public void writeToProperties(String key, String value, String filePath) {

		property.put(key, value);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			property.store(fos, "Updated");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
