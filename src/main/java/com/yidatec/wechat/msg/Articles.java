package com.yidatec.wechat.msg;

/**
 * 多图文消息
 * @author east.com
 *
 */
public class Articles {
	private String Title;
	private String Description;
	private String PicUrl;
	private String Url;
	
	public Articles() {
		Title = "";
		Description = "";
		PicUrl = "";
		Url = "";
	}
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
}