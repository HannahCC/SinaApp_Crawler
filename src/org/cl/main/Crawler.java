package org.cl.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cl.conf.Config;
import org.cl.parser.Parser;
import org.cl.service.GetInfo;
import org.cl.service.HttpRequest;
import org.cl.service.SaveInfo;
import org.cl.service.SaveRecord;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
/**
 * 要过滤的
 * Sina Visitor System
 * 
 * 
 * 美剧猎人_ym	
 * 运动伴侣
 * 绿狗SIRI网
 * 奢品秀Sp
 * 迅雷会员活动
 * 美发秀秀网
 * 美图拍拍web版
 * **
 * @author Hannah
 *
 */
public class Crawler {
	public static void main(String args[]) throws IOException{
		HttpRequest hr = new HttpRequest();
		Map<String, String> key_map = new HashMap<String, String>();
		
		GetInfo.getStringMap(Config.ROOT_PATH+"Config\\Src_Url.txt", key_map, "\t", 0, 1);
		GetInfo.idfilter_userId(key_map, "Feature_SRC\\Src_Description.txt", "\t", 0);
		GetInfo.idfilter_userId(key_map, "Feature_SRC\\Src_NotExist.txt", "\t", 0);
		for(Entry<String, String> src_url : key_map.entrySet()){
			String app_name = src_url.getKey();
			String url = src_url.getValue();
			System.out.println("Searching "+app_name+"\t"+url);
			if(app_name.equals(""))continue;
			HtmlPage detail_page = hr.getPage(url);
			if(detail_page==null){
				SaveInfo.saveString(Config.ROOT_PATH, "Feature_SRC\\Src_NotExist.txt", app_name+"\t"+url, true);
				continue;}
			String Description = Parser.parser(detail_page);
			SaveInfo.saveString(Config.ROOT_PATH, "Feature_SRC\\Src_Description.txt", app_name+"\t"+Description, true);
		}
		SaveRecord.close();
	}


}
