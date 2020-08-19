package com.iceolive.httpmock.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.iceolive.httpmock.model.MockData;
import com.iceolive.httpmock.util.PathUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 钢翼
 */
@Slf4j
@Service
public class MockService {
    private List<MockData> mockDataList;
    @Value("${mock.readFromResource}")
    private boolean readFromResource;
    @Value("${mock.filepath}")
    private String mockFile;


    final Pattern pattern =  Pattern.compile("\\$\\{(.*)\\}");
    public  List<MockData> getMockDataList() {
        if (this.mockDataList != null) {
            return this.mockDataList;
        }
        List<MockData> result = new ArrayList<MockData>();
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            if(readFromResource){
                inputStream = this.getClass().getClassLoader().getResourceAsStream("mock.json");
            }else {
                if (mockFile.startsWith("/") || mockFile.startsWith("\\")) {
                    mockFile = PathUtil.getRootPath() + mockFile;
                }
                inputStream = new FileInputStream(new File(mockFile));
            }
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            String json = baos.toString("UTF-8");
            result = JSONObject.parseObject(json, new TypeReference<List<MockData>>() {
            });

        } catch (IOException e) {
            log.error("解析mock.json异常", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                log.error("关闭流异常", e);
            }
        }
        this.mockDataList = result;
        return this.mockDataList;
    }

    public MockData get(String url, String method, Map<String, String[]> params) {
        for (MockData mockData : getMockDataList()) {
            boolean match = true;
            boolean matchUrl = false;
            if(mockData.isIgnoreCase()){
                if (mockData.getMethod().toLowerCase().equals(method.toLowerCase()) && mockData.getUrl().toLowerCase().equals(url.toLowerCase())) {
                    matchUrl = true;
                }
            }else{
                if (mockData.getMethod().toLowerCase().equals(method.toLowerCase()) && mockData.getUrl().equals(url)) {
                    matchUrl = true;
                }
            }
            if (matchUrl) {
                String rule = mockData.getRule();
                if(!StringUtils.isEmpty(rule)) {


                    for (String p : rule.split("&")) {
                        String name = null;
                        String value = null;
                        String operate = null;
                        if (p.indexOf("!=") > -1) {
                            name = p.split("!=")[0];
                            value = p.split("!=")[1];
                            operate = "!=";
                        } else if (p.indexOf("*=") > -1) {
                            name = p.split("\\*=")[0];
                            value = p.split("\\*=")[1];
                            operate = "*=";

                        } else if (p.indexOf("=") > -1) {
                            name = p.split("=")[0];
                            value = p.split("=")[1];
                            operate = "=";
                        } else {
                            match = false;
                            break;
                        }
                        if (params.containsKey(name)) {
                            String value1 = params.get(name)[0];
                            if (operate.equals("!=")) {
                                if (value.equals(value1)) {
                                    match = false;
                                    break;
                                }
                            } else if (operate.equals("*=")) {
                                if (value1.indexOf(value) == -1) {
                                    match = false;
                                    break;
                                }
                            } else if (operate.equals("=")) {
                                if (!value.equals(value1)) {
                                    match = false;
                                    break;
                                }
                            }
                        } else {
                            match = false;

                        }
                    }
                }else{
                    match = true;
                }
            } else {
                match = false;
            }
            if (match) {
                if (StringUtils.isEmpty(mockData.getContentType())) {
                    mockData.setContentType("application/json;charset=UTF-8");
                }
                String str = mockData.getResult();
                Matcher matcher =  pattern.matcher(str);
                while (matcher.find()){
                    String name = matcher.group(1);
                    if(params.containsKey(name)){
                        String value = params.get(name)[0];
                        str = str.replace(matcher.group(0),value);
                    }
                }
                mockData.setResult(str);
                return mockData;
            }
        }
        MockData mockData = new MockData();
        mockData.setContentType("application/json;charset=UTF-8");
        mockData.setCode(500);
        mockData.setResult("匹配不到mock路由规则");
        return mockData;
    }
}
