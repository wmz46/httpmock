package com.iceolive.httpmock.model;

import lombok.Data;

/**
 * @author 钢翼
 */
@Data
public class MockData {
    private String url;
    private boolean ignoreCase = true;
    private String method = "get";
    private String rule;
    private String result;
    private int code = 404;
    private String contentType = "application/json;charset=UTF-8";

}
