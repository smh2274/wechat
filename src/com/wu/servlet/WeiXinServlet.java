package com.wu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.alibaba.fastjson.JSON;
import com.wu.bean.TextMessageBean;
import com.wu.bean.TuLingMessageBean;
import com.wu.bean.TuLingMessageBean.ListEntity;
import com.wu.utils.CheckUtil;
import com.wu.utils.MessageUtil;
import com.wu.utils.TuLingMessageUtils;

import javafx.event.EventType;

public class WeiXinServlet extends HttpServlet {

	private static final String tulingRobot = "http://www.tuling123.com/openapi/api";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 获得请求参数
		String signature = req.getParameter("signature");
		// System.out.println("signature\t" + signature);
		String timestamp = req.getParameter("timestamp");
		// System.out.println("timestamp\t" + timestamp);
		String nonce = req.getParameter("nonce");
		// System.out.println("nonce\t" + nonce);
		String echostr = req.getParameter("echostr");
		// System.out.println("echostr\t" + echostr);

		PrintWriter pWriter = resp.getWriter();

		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			// System.out.println("返回数据\t" + echostr);
			pWriter.print(echostr);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter pWriter = resp.getWriter();
		pWriter.print("success");
		String temp = null;
		try {
			Map<String, String> map = MessageUtil.xml2Map(req);
			if (map.get("MsgType").equals("text")) {
				temp = doTextMessage(map);
			}
		
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("这是发给用户的消息\n" + temp);
		if (temp != null) {
			pWriter.print(temp);
		}
		pWriter.close();
		
		
		
	}

	public String tuling(String arg, String fromUser) throws IOException {
//APIKEY替换成你自己的API,我这里用111111代替
		String APIKEY = "1111111";
		String INFO = URLEncoder.encode(arg, "utf-8");
		String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO + "&userid=" + fromUser;
		System.out.println("请求图灵接口的url\r" + getURL);
		URL getUrl = new URL(getURL);
		HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
		connection.connect();

		// 取得输入流，并使用Reader读取
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		// 断开连接
		connection.disconnect();
		System.out.println(sb);
		return sb.toString();
	}

	/**
	 * 处理用户发来的文本消息
	 * 
	 * @param map
	 * @return
	 * @throws IOException
	 */
	private String doTextMessage(Map<String, String> map) throws IOException {
		String string = tuling(map.get("Content"), map.get("FromUserName"));
		// 从图灵服务器得到的消息
		TuLingMessageBean tulingBean = JSON.parseObject(string, TuLingMessageBean.class);
		String temp = null;
		switch (tulingBean.getCode()) {
		case TuLingMessageUtils.CODE_TEXT:
			TextMessageBean text_bean = new TextMessageBean();
			text_bean.setFromUserName(map.get("ToUserName"));
			text_bean.setCreateTime(System.currentTimeMillis() / 1000);
			text_bean.setContent(tulingBean.getText());
			// bean.setContent(map.get("Content"));
			text_bean.setToUserName(map.get("FromUserName"));
			text_bean.setMsgType("text");
			temp = MessageUtil.map2xml(text_bean);
			break;
		case TuLingMessageUtils.CODE_NEWS:
			List<ListEntity> newsList = tulingBean.getList();
			StringBuilder sb = new StringBuilder();
			for (ListEntity listEntity : newsList) {
				sb.append(listEntity.getArticle());
				sb.append("\n");
				sb.append("来源：" + listEntity.getSource());
				sb.append("\n");
				sb.append(listEntity.getDetailurl());
				sb.append("\n\n");
			}
			TextMessageBean news_bean = new TextMessageBean();
			news_bean.setFromUserName(map.get("ToUserName"));
			news_bean.setCreateTime(System.currentTimeMillis() / 1000);
			news_bean.setContent(sb.toString());
			// bean.setContent(map.get("Content"));
			news_bean.setToUserName(map.get("FromUserName"));
			news_bean.setMsgType("text");
			temp = MessageUtil.map2xml(news_bean);
			break;
		default:
			break;
		}
		return temp;
	}

}
