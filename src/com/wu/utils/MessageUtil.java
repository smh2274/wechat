package com.wu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wu.bean.TextMessageBean;

public class MessageUtil {
	
	public static final String TEXT_MESSAGE="text";
	public static final String IMAGE_MESSAGE="image";
	public static final String VOICE_MESSAGE="voice";
	public static final String VIDEO_MESSAGE="video";
	public static final String SHORTVIDEO_MESSAGE="shortvideo";
	public static final String LOCATION_MESSAGE="location";
	public static final String LINK_MESSAGE="link";
	
	/**
	 * xmlתmap
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xml2Map(HttpServletRequest request) throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		InputStream iStream = request.getInputStream();
		Document document = reader.read(iStream);
		Element element = document.getRootElement();
		List<Element> list = element.elements();
		for (Element element2 : list) {
			map.put(element2.getName(), element2.getText());
		}
		iStream.close();
		return map;
	}
	
	public static String map2xml(TextMessageBean o) {
		XStream stream = new XStream(new DomDriver());
		stream.alias("xml", o.getClass());
		return stream.toXML(o);
	}
}
