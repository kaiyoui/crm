package com.yidatec.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

/**
 * 读取系统配置类
 */
class ConfigProperties {
	/**
	 * 日志记录
	 */
	private Log logger = LogFactory.getLog(ConfigProperties.class);

	/**
	 * 配置文件
	 */
	private static String setting = "/application.properties";

	/**
	 * 配置属性
	 */
	private static Properties prop = null;

	/**
	 * 单例
	 */
	private static final ConfigProperties instance = new ConfigProperties();

	/**
	 * 构造函数
	 */
	private ConfigProperties() {
		prop = new Properties();
		try {
			prop.load(this.getClass().getResourceAsStream(setting));
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	/**
	 * 获取设置
	 * 
	 * @param key
	 *            属性名
	 * @return String 属性值
	 */
	public static String getSetting(String key) {
		return instance.prop.getProperty(key);
	}



	/**
	 * 微信公网IP
	 * @return
	 */
	public static String getWeixinHost() {
		return getSetting("weChatHost");
	}
	
	/**
	 * 微信公网IPS
	 * @return
	 */
	public static String getWeixinHostNoPrefix() {
		return getSetting("weChatHostNoPrefix");
	}
	
	/**
	 * 微信用当前项目虚拟路径
	 * @return
	 */
	public static String getWeixinContextPath() {
		return getSetting("weChatContextPath");
	}
	
	/**
	 * 微信APP ID
	 * @return
	 */
	public static String getWeixinAppId() {
		return getSetting("weChatAppId");
	}
	
	/**
	 * 微信APP SECRET
	 * @return
	 */
	public static String getWeixinAppSecret() {
		return getSetting("weChatAppSecret");
	}
	
	/**
	 * 微信APP Token
	 * @return
	 */
	public static String getWeixinToken() {
		return getSetting("weChatToken");
	}

    public static String getWeixinCorpId() {
        return getSetting("weixin_CORP_ID");
    }

    public static String getWeixinCorpSecret() {
        return getSetting("weixin_CORP_SECRET");
    }

    public static String getWeixinAgentId() {
        return getSetting("weixin_AGENT_ID");
    }

    public static String getWeixinAesKey() {
        return getSetting("aesKey");
    }

    public static String getEnterpriseToken() {
        return getSetting("enterpriseToken");
    }

	public static String getJsapi_debug(){
		return getSetting("jsapiDebug");
	}

	public static String getPageSize(){
		return getSetting("pageSize");
	}


	/**
	 * 文件上传路径
	 * @return
	 */
	public static String getFileUrl() {
		return getSetting("imagePath");
	}

}
