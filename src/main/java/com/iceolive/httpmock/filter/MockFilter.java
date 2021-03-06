package com.iceolive.httpmock.filter;

import com.iceolive.httpmock.model.MockData;
import com.iceolive.httpmock.service.MockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author 钢翼
 */
@Component
@Slf4j
public class MockFilter extends OncePerRequestFilter {
    @Autowired
    MockService mockService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String url = request.getServletPath();
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("body:" + sb.toString());
        MockData result = mockService.get(url, method, request.getParameterMap());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        if (result.getCode() == 302) {
            response.setStatus(result.getCode());
            response.setHeader("location", result.getResult());
        } else {
            response.setContentType(result.getContentType());
            response.setStatus(result.getCode());
            response.getWriter()
                    .write(result.getResult());
        }

    }
}
