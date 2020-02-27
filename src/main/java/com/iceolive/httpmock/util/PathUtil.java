package com.iceolive.httpmock.util;

import lombok.var;
import org.springframework.util.StringUtils;

public class PathUtil {
    /**
     * 获取到classes目录
     *
     * @return
     */
    public static String getClassPath() {
        String file = PathUtil.class.getResource("/").getPath();
        String systemName = System.getProperty("os.name");
        if (!StringUtils.isEmpty(systemName) && systemName.indexOf("Windows") != -1) {
            if (file.startsWith("/")) {
                file = file.substring(1);
            }
        }
        while (file.contains(".jar")) {
            file = file.substring(0, file.lastIndexOf("."));
            file = file.substring(0, file.lastIndexOf("/"));
        }
        if (file.startsWith("file:/")) {
            file = file.substring(6);
        }
        return file;
    }
    /**
     * 获取jar包根目录
     *
     * @return
     */
    public static String getRootPath() {
        var classPath = getClassPath();
        if (classPath.endsWith("test-classes/")) {
            return classPath.substring(0, classPath.length() - 13);
        } else if (classPath.endsWith("classes/")) {
            return classPath.substring(0, classPath.length() - 8);
        } else {
            return classPath;
        }
    }

}
