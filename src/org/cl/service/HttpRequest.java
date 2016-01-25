package org.cl.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.cl.conf.Config;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

public class HttpRequest {
	/**
	 * 防止因为网络原因而获取网页失败，每次失败后进行重试，最多重试3次 
	 * @param wc
	 * @param href
	 * @return
	 */
	//private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	private WebClient wc = null;
	private int request_counts = 0;

	public HttpRequest(){
		wc = createWC();
	}

	public HtmlPage getPage(String href){
		slowdown();
		HtmlPage page = null;
		int retry = 0;
		while(retry<Config.RETRY_MAX){
			try {
				retry++;
				request_counts++;
				page = wc.getPage(href);
				retry = Config.RETRY_MAX;
			}catch (FailingHttpStatusCodeException e){
				e.printStackTrace();
				retry = Config.RETRY_MAX;
				SaveRecord.saveError(href+"\tstatusCode:"+e.getStatusCode());
				changeIP();
			} catch (IOException e) {
				e.printStackTrace();
				SaveRecord.saveError(href+"\twrongMsg:"+e.getMessage());
				changeIP();
			} catch (Exception e) {
				e.printStackTrace();
				SaveRecord.saveError(href+"\twrongMsg:"+e.getMessage());
				changeIP();
			}
		}
		checkpage(page,href);
		return page;
	}

	private void checkpage(HtmlPage page, String info) {
		if(page==null){SaveRecord.saveError(info);}//获取页面失败
		else if(page.getTitleText().equals("豆瓣电影")){//跳转到主页
			SaveRecord.saveError(page.getUrl()+"\t"+page.getTitleText());
			page = null;
		}else if(page.getTitleText().contains("禁止访问")){//页面爬取失败
			SaveRecord.saveError(page.getUrl()+"\t"+page.getTitleText());
			page = null;
		}
	}

	private void slowdown() {
		//降低爬取速度，防止账号被锁
		try {
			Thread.sleep(getUnitSleepTime());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//当请求次数达到一定数量后，程序休眠30min，降低爬取频数
		if(request_counts>Config.REQUEST_MAX){
			changeIP();
		}
	}

	/** 每次获取一个IP，所有IP使用一轮后休眠一次 **/
	private void changeIP() {

		System.out.println("暂无IP更换策略。");
		/*SaveRecord.saveIP(Config.PROXY.get(Config.COUNT)+"\t"+Config.PROT.get(Config.COUNT)+"\t"+request_counts+"\t"+getCurrentTime());
		if(Config.COUNT<Config.COUNT_MAX-1){
			//fallasleep(getUnitSleepTime());
			Config.COUNT++;
		}else {
			fallasleep(getSleepTime());
			Config.COUNT = 0;//重新循环使用账号
		}request_counts=0;	//请求数归零
		wc = createWC();*/
	}

	/**
	 * 使用代理IP创建一个WC
	 * @return
	 */
	private WebClient createWC() {
		WebClient wc = null;
		if(Config.PROXY==null||Config.PROXY.size()==0){wc = new WebClient(BrowserVersion.CHROME);}
		else{
			wc = new WebClient(BrowserVersion.CHROME,Config.PROXY.get(Config.COUNT),Config.PROT.get(Config.COUNT));
		}
		/*HttpClient httpclient = new DefaultHttpClient();
		HttpClientParams.setCookiePolicy(httpclient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);*/
		//WebClient wc = new WebClient(BrowserVersion.CHROME);
		wc.getOptions().setCssEnabled(false);
		wc.getOptions().setActiveXNative(false);
		wc.getOptions().setJavaScriptEnabled(false);
		return wc;
	}

	/** 每次请求，线程休眠随机时间，以此降低爬取速度*/
	private long getUnitSleepTime(){
		Random r = new Random();
		int sleep_time = r.nextInt(Config.UNIT_SLEEP_TIME);
		return sleep_time;
	}

	/** 达到最大的请求次数或请求被拒绝时程序的休眠时间,单位为毫秒（建议3600000-7200000）*/
	private long getSleepTime(){
		Random r = new Random();
		int sleep_time = r.nextInt(Config.SLEEP_TIME);
		return sleep_time;
	}

	private void fallasleep(long sleepTime){
		System.out.println(Thread.currentThread().getName()+"Start to sleep!!!NowTime:"+getCurrentTime()+"I will sleep for "+sleepTime);
		try
		{	//休眠指定时间
			Thread.sleep(sleepTime);
		}catch (InterruptedException ee)
		{
			ee.printStackTrace();
			System.out.println(Thread.currentThread().getName()+"Fail to sleep!!!");
		}
		System.out.println(Thread.currentThread().getName()+"Succeed to sleep!!!NowTime:"+getCurrentTime());
	}

	private String getCurrentTime() {
		return String.format("%tT", new Date());
		//return df.format(new Date());
	}
}
