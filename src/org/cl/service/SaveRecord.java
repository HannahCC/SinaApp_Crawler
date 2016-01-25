package org.cl.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.cl.conf.Config;

public class SaveRecord 
{
	//获取电影时异常记录
	private static FileOutputStream error_info=null;
	//IP地址
	private static FileOutputStream ip_info=null;
	
	static{
		//创建结果存放文件
		File temp1=new File(Config.ROOT_PATH+"error_info.txt");
		//File temp2=new File(Config.ROOT_PATH+"IP_new.txt");
		try 
		{
			error_info=new FileOutputStream(temp1,true);
			//ip_info=new FileOutputStream(temp2,true);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	public static void saveError(String info) {
		info += "\r\n";
		try {
			error_info.write(info.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveIP(String info) {
		info += "\r\n";
		try {
			System.out.println(info);
			ip_info.write(info.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 关闭所有与用户信息存储文件
	 * @throws IOException */
	public static void close()
	{
		try {
			error_info.flush();
			error_info.close();
			//ip_info.flush();
			//ip_info.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}

