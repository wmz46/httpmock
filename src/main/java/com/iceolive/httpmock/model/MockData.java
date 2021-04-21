package com.iceolive.httpmock.model;

import lombok.Data;

/**
 * @author 钢翼
 */
@Data
public class MockData {
    /**
     * 匹配url
     */
    private String url;
    /**
     * url忽略大小写
     */
    private boolean ignoreCase = true;
    /**
     * 匹配请求类型
     */
    private String method = "get";
    /**
     * 匹配规则
     */
    private String rule;
    /**
     * 返回的内容，支持参数替换，json内容或302重定向的url
     */
    private String result;
    /**
     * 返回状态
     */
    private int code = 404;
    /**
     * 返回类型
     * text/html 表示页面
     * application/json 表示json，默认赋值为json
     */
    private String contentType = "application/json;charset=UTF-8";
    /**
     * 返回内容的文件路径，如果改字段不为空，则优先级大于result
     */
    private String filePath;

}
