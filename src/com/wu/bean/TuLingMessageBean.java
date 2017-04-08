package com.wu.bean;

import java.util.List;
/**
 * 图灵机器人api格式bean
 * @author small bright
 *
 */
public class TuLingMessageBean {
	 /**
     * code : 302000
     * text : ********
     * list : [{"article":"","source":"","detailurl":"","icon":"","trainnum":"","start":"","terminal":"","starttime":"","endtime":"","name":"","info":""}]
     */

    private int code;
    private String text;
    private String url;
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private List<ListEntity> list;

    public void setCode(int code) {
        this.code = code;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        /**
         * article : 
         * source : 
         * detailurl : 
         * icon : 
         * trainnum : 
         * start : 
         * terminal : 
         * starttime : 
         * endtime : 
         * name : 
         * info : 
         */

        private String article;
        private String source;
        private String detailurl;
        private String icon;
        private String trainnum;
        private String start;
        private String terminal;
        private String starttime;
        private String endtime;
        private String name;
        private String info;

        public void setArticle(String article) {
            this.article = article;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setTrainnum(String trainnum) {
            this.trainnum = trainnum;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getArticle() {
            return article;
        }

        public String getSource() {
            return source;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public String getIcon() {
            return icon;
        }

        public String getTrainnum() {
            return trainnum;
        }

        public String getStart() {
            return start;
        }

        public String getTerminal() {
            return terminal;
        }

        public String getStarttime() {
            return starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public String getName() {
            return name;
        }

        public String getInfo() {
            return info;
        }
    }
}
