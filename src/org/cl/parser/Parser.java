package org.cl.parser;

import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
public class Parser {


	public static String parser(HtmlPage detail_page) throws IOException {
		DomElement div = detail_page.getElementById("pl_app_userlist");
		String keywords = "", desc = "";
		if(div==null){//不是正常的app_detail页面，则使用meta中的keywords、description作为描述
			HtmlElement header = detail_page.getHead();
			keywords += detail_page.getTitleText()+" ";
			List<HtmlMeta> keyword_meta = (List<HtmlMeta>) header.getByXPath("meta[@name='keywords']");
			if(keyword_meta.size()>0){keywords+=keyword_meta.get(0).getAttribute("content");}
			List<HtmlMeta> desc_meta = (List<HtmlMeta>) header.getByXPath("meta[@name='description']");
			if(desc_meta.size()>0){ desc += desc_meta.get(0).getAttribute("content");}
		}else{//正常的app_detail页面，则使用标签和简介作为描述
			@SuppressWarnings("unchecked")
			List<HtmlAnchor> tag_list = (List<HtmlAnchor>) div.getByXPath(".//a[@title='标签']");
			if(tag_list.size()>0)
				keywords = tag_list.get(0).asText();
			div = detail_page.getElementById("pl_app_desc");
			desc = div.asText();
			desc = desc.substring(3, desc.length());
			desc = desc.replaceAll("\\s+", " ");
		}
		return keywords+"**"+desc;
	}

}
