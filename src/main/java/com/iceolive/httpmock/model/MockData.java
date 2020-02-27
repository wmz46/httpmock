package com.iceolive.httpmock.model;

import lombok.Data;

/**
 * @author 钢翼
 */
@Data
public class MockData {
    private String url;
    private String method;
    private String rule;
    private String result;
    private int code;
    private String contentType;

}
