package com.rail.electric.simulator.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBaseModel {
	private final static Logger logger =  LoggerFactory.getLogger(AbstractBaseModel.class);
	
	private Properties props = new Properties();
	private File userConfFile;
	
	protected AbstractBaseModel() {
		init();
	}
	
	private void init() {
        StringBuilder dir = new StringBuilder();
        dir.append(System.getProperty("user.home"));
        dir.append(File.separator);
        dir.append(".simulator");
        dir.append(File.separator);
        String applicationDataPath = dir.toString();
        File f = new File(applicationDataPath);
        f.mkdirs();
        userConfFile = new File(applicationDataPath + getConfFileName() + ".properties");
        if (!userConfFile.exists()) {
            try {
                userConfFile.createNewFile();
            } catch (IOException e) {
               logger.error("Failed to create user configuration file {}, caused by {}", userConfFile.getName(), 
            		   e.toString());
            }
        }

        try {
            props.load(new FileInputStream(userConfFile));
        } catch (FileNotFoundException e) {
        	logger.error("Failed to find user configuration file {}, caused by {}", userConfFile.getName(), 
         		   e.toString());
        } catch (IOException e) {
        	logger.error("Failed to load user configuration file {}, caused by {}", userConfFile.getName(), 
         		   e.toString());
        }
    }
	
	abstract protected String getConfFileName();
	
	protected void setProperty(String name, String value) {
		props.setProperty(name, value);
	}
	
	protected String getProperty(String name) {
		return props.getProperty(name);
	}
	
	protected String getProperty(String name, String defaultValue) {
		return props.getProperty(name, defaultValue);
	}
	
	public boolean save() {
        try {
            props.store(new FileOutputStream(userConfFile), "");
            return true;
        } catch (FileNotFoundException e) {
        	logger.error("Failed to find user configuration file {}, caused by {}", userConfFile.getName(), 
          		   e.toString());
        } catch (IOException e) {
        	logger.error("Failed to save user configuration file {}, caused by {}", userConfFile.getName(), 
          		   e.toString());
        }
        return false;
    }

}
