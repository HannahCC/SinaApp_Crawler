package org.cl.conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Chenli
 *
 */
public class Config {
	public static String ROOT_PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static int UNIT_SLEEP_TIME = 1000;//millisecond
	public static int SLEEP_TIME = 3600000;//millisecond
	public static int REQUEST_MAX = 5000;//times
	public static int RETRY_MAX = 2;
	public static int COMMENT_MAX = 50000;
	
	
	public static List<String> PROXY;
	public static List<Integer> PROT;
	public static int COUNT = 0;
	public static int COUNT_MAX = 0;
	
	static {
		//获取配置
		File f = new File(ROOT_PATH+"Config\\Config.txt");
		Map<String,String> confmap = new HashMap<String,String>();
		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(f));
			String conf = "";
			while((conf = r.readLine())!= null){
				String conf_name = conf.split("\\s+")[0];
				String conf_value =  conf.split("\\s+")[1];
				confmap.put(conf_name, conf_value);
			}
			if(confmap.containsKey("UNIT_SLEEP_TIME")){UNIT_SLEEP_TIME = Integer.parseInt(confmap.get("UNIT_SLEEP_TIME"));}
			if(confmap.containsKey("SLEEP_TIME")){SLEEP_TIME = Integer.parseInt(confmap.get("SLEEP_TIME"));}
			if(confmap.containsKey("REQUEST_MAX")){REQUEST_MAX = Integer.parseInt(confmap.get("REQUEST_MAX"));}
			if(confmap.containsKey("COMMENT_MAX")){COMMENT_MAX = Integer.parseInt(confmap.get("COMMENT_MAX"));}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//getIP();
		
	}

	private static void getIP() {

		//获取IP地址
		try {
			File f = new File(ROOT_PATH+"IP.txt");
			BufferedReader r = new BufferedReader(new FileReader(f));
			String usr = "";
			PROXY = new ArrayList<String>();
			PROT = new ArrayList<Integer>();
			while((usr = r.readLine())!= null){
				PROXY.add(usr.split("\t")[0]);
				PROT.add(Integer.parseInt(usr.split("\t")[1]));
				COUNT_MAX++;
			}
			r.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
