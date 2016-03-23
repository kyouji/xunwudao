package com.ynyes.xunwudao.util;

import java.util.Properties;

/**
 * 后台常用常量
 * @author Sharon
 *
 */
public class SiteMagConstant {

    public static final int pageSize = 20;
    
    public static final String templatePath = "src/main/resources/templates/client/";
    
    public static final String backupPath ;
    public static final String imagePath; 
    static {
    	Properties props = System.getProperties();
    	String operation = props.getProperty("os.name");
    	if (operation.contains("Linux"))
    	{
    		 backupPath = "/mnt/root/backup/xunwudao/";
    		 imagePath = "/mnt/root/images/csb/";
    		
//   		 backupPath = "/mnt/root/test/backup/";
//   		 imagePath = "/mnt/root/test/images";
    	}
    	else
    	{
    		backupPath = "src/main/resources/backup/";
    	    imagePath = "src/main/resources/static/images/";
    	}
    }
   
    
}