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
//APIKEY替换成你自己的API
		String APIKEY = "19f2f60448df4bf9a884d25b561da715";
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
		case TuLingMessageUtils.CODE_TRAIN:
			StringBuilder sb_train = new StringBuilder();
			sb_train.append(tulingBean.getText()+"\n");
			List<ListEntity> trainList = tulingBean.getList();
			for (ListEntity listEntity : trainList) {
				sb_train.append("车次：");
				sb_train.append(listEntity.getTrainnum());
				sb_train.append("\n");
				sb_train.append(listEntity.getStart());
				sb_train.append("\t到\t");
				sb_train.append(listEntity.getTerminal());
				sb_train.append("\n");
				sb_train.append("发车：");
				sb_train.append(listEntity.getStarttime());
				sb_train.append("\t");
				sb_train.append("终到：");
				sb_train.append(listEntity.getEndtime());
				sb_train.append("\n\n");
			}
			TextMessageBean train_bean = new TextMessageBean();
			train_bean.setFromUserName(map.get("ToUserName"));
			train_bean.setCreateTime(System.currentTimeMillis() / 1000);
			train_bean.setContent(sb_train.toString());
			// bean.setContent(map.get("Content"));
			train_bean.setToUserName(map.get("FromUserName"));
			train_bean.setMsgType("text");
			temp = MessageUtil.map2xml(train_bean);
			break;
		case TuLingMessageUtils.CODE_OTHER:
			StringBuilder sb_other = new StringBuilder();
			sb_other.append(tulingBean.getText()+"\n");
			List<ListEntity> otherList = tulingBean.getList();
			int i = 0;
			for (ListEntity listEntity : otherList) {
				i++;
				sb_other.append("菜名：");
				sb_other.append(listEntity.getName());
				sb_other.append("\n");
				sb_other.append("原料：");
				sb_other.append(listEntity.getInfo());
				sb_other.append("\n操作步骤：");
				sb_other.append(listEntity.getDetailurl());
				if (i>5) {
					break;
				}else {
					sb_other.append("\n\n");
				}
			}
			TextMessageBean other_bean = new TextMessageBean();
			other_bean.setFromUserName(map.get("ToUserName"));
			other_bean.setCreateTime(System.currentTimeMillis() / 1000);
			other_bean.setContent(sb_other.toString());
			// bean.setContent(map.get("Content"));
			other_bean.setToUserName(map.get("FromUserName"));
			other_bean.setMsgType("text");
			temp = MessageUtil.map2xml(other_bean);
			break;
		default:
			break;
		}
		return temp;
	}

}
