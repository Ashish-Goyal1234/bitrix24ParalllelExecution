package com.bitrix.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileConfigurations {

	private Properties config;

	public void loadConfigurations() {
		if (config == null) {
			try {
				config = new Properties();
				String paths = System.getProperty("user.dir") + Iconstants.PROPERTIES_FILE;
				FileInputStream fis;
				fis = new FileInputStream(paths);
				config.load(fis);
			} catch (IOException e) {
				BitrixLogger.fatal("Failed to load Configurations .", e);
			}
				BitrixLogger.info("Successfully loaded the configurations.");
		}
	}

	public String getUrl() {
		if (config != null) {
			return config.getProperty("url");
		}
		return null;
	}

	public String getBrowserName() {
		if (config != null) {
			return config.getProperty("browser");
		}
		return null;
	}

	public int getImplicitTimeout() {
		if (config != null) {
			return Integer.parseInt(config.getProperty("implicitWait"));
		}
		return 0;
	}
	
	 public int getPageLoadTimeout() {
	        if (config != null) {
	            return Integer.parseInt(config.getProperty("pageloadTimeout"));
	        }
	        return 0;
	    }
	 
	 public int getScriptTimeout() {
	        if (config != null) {
	            return Integer.parseInt(config.getProperty("scriptTimeout"));
	        }
	        return 0;
	    }
	 
	 public String getScreenshotPath() {
	        if (config != null) {
	            return config.getProperty("screenshotsPath");
	        }
	        return null;
	    }
	 
	 public String getextentReportsPath() {
	        if (config != null) {
	            return config.getProperty("extentReports");
	        }
	        return null;
	    }
	 
	 public String getExcelSheetPath(String moduleFilePath) {
	        if (config != null) {
	            return config.getProperty(moduleFilePath);
	        }
	        return null;
	    }
	 
	 public String getLocalOrRemote() {
			if (config != null) {
				return config.getProperty("localOrRemote");
			}
			return null;
		}
	
}
