package com.aco.practice.demo1.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author A.co
 * @version 1.0
 * @date 2024/11/5 21:35
 */
public class FileUtil {
    public static String getFileNameFromPath(String path) {
        if (path.contains("/")) {
            return path.substring(path.lastIndexOf("/") + 1);
        } else {
            return path.substring(path.lastIndexOf("\\") + 1);
        }
    }

    public static String getDirectoryNameFromPath(String path) {
        if (path.contains("/")) {
            String dir = path.substring(0, path.lastIndexOf("/"));
            return dir.replace("/", File.separator);
        } else {
            return path.substring(0, path.lastIndexOf("\\")).replace("\\", File.separator);
        }
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (0 < index) {
            return fileName.substring(0, index);
        } else {
            return fileName;
        }
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+File.separator+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    private long parseSize(String size) {
        Assert.hasLength(size, "Size must not be empty");
        size = size.toUpperCase();
        return size.endsWith("KB")?Long.valueOf(size.substring(0, size.length() - 2)).longValue() * 1024L:(size.endsWith("MB")?Long.valueOf(size.substring(0, size.length() - 2)).longValue() * 1024L * 1024L:Long.valueOf(size).longValue());
    }

    /**
     * 正则替换所有特殊字符
     * @param orgStr
     * @return
     */
    public static String replaceSpecStr(String orgStr){
        if (StringUtils.isNotBlank(orgStr)) {
            if (orgStr.length() > 200){
//                throw new OutOfBusinessException("文件名称不能超过200字");
            }
            String regEx="[\\s$&\"\'<>]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(orgStr);
            return m.replaceAll("*");
        }
        return orgStr;
    }


    /**
     * 删除单个文件
     * @param   sPath 被删除文件path
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;

    }

    public static void main( String[] args ) {
        String a = " $&'\"<>";
        System.out.println(replaceSpecStr(a));
    }
}
