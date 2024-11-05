package com.aco.practice.demo1.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件检查工具类
 * @author A.co
 * @version 1.0
 * @date 2024/11/5 21:34
 */
public class FileCheck {

    /**
     * 路径分隔符
     */
    public static final String SEPARATOR = "/";
    /**
     * Point
     */
    public static final String POINT = ".";
    /**
     * 图片类型
     */
    private static final List<String> TYPE_IMAGE = new ArrayList<>();
    /**
     * 文档类型
     */
    private static final List<String> TYPE_DOC = new ArrayList<>();
    /**
     * 音频类型
     */
    private static final List<String> TYPE_VIDEO = new ArrayList<>();
    /**
     * 压缩文件类型
     */
    private static final List<String> TYPE_COMPRESS = new ArrayList<>();

    /**
     * 支持的全部类型
     */
    private static final List<String> TYPE_ALL = new ArrayList<>();

    static {
        TYPE_IMAGE.add("png");
        TYPE_IMAGE.add("gif");
        TYPE_IMAGE.add("jpeg");
        TYPE_IMAGE.add("jpg");

        TYPE_DOC.add("pdf");
        TYPE_DOC.add("ppt");
        TYPE_DOC.add("xls");
        TYPE_DOC.add("xlsx");
        TYPE_DOC.add("pptx");
        TYPE_DOC.add("doc");
        TYPE_DOC.add("docx");
        TYPE_DOC.add("txt");
        TYPE_DOC.add("json");
        //增加wps
        TYPE_DOC.add("wps");

        TYPE_VIDEO.add("mp3");
        TYPE_VIDEO.add("mp4");
        TYPE_VIDEO.add("flv");

        TYPE_COMPRESS.add("zip");
        TYPE_COMPRESS.add("rar");

        TYPE_ALL.add("png");
        TYPE_ALL.add("gif");
        TYPE_ALL.add("jpeg");
        TYPE_ALL.add("jpg");
        TYPE_ALL.add("pdf");
        TYPE_ALL.add("ppt");
        TYPE_ALL.add("xls");
        TYPE_ALL.add("xlsx");
        TYPE_ALL.add("pptx");
        TYPE_ALL.add("doc");
        TYPE_ALL.add("docx");
        TYPE_ALL.add("mp3");
        TYPE_ALL.add("mp4");
        TYPE_ALL.add("zip");
        TYPE_ALL.add("rar");
        TYPE_ALL.add("txt");
        TYPE_ALL.add("json");
        TYPE_ALL.add("html");
        TYPE_ALL.add("ftl");
        TYPE_ALL.add("xml");
        //增加wps
        TYPE_ALL.add("wps");
    }
    /**
     * 检查符合的类型. <br>
     * 默认检查ALL 中的类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkAllType(String filename){
        return checkType(TYPE_ALL, filename);
    }
    /**
     * 检查图片类型. <br>
     * 默认检查 ['png', 'gif', 'jpeg', 'jpg'] 几种类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkImage(String filename){
        return checkImage(null, filename);
    }

    /**
     * 检查图片类型
     * @param types 可自行传入文件的类型集合，默认检查 ['png', 'gif', 'jpeg', 'jpg'] 几种类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkImage(List<String> types, String filename){
        List<String> checkTypes = types;
        if(types == null || types.size() == 0){
            checkTypes = TYPE_IMAGE;
        }

        return checkType(checkTypes, filename);
    }

    /**
     * 检查文档类型. <br>
     * 默认检查 ['pdf', 'ppt', 'xls', 'xlsx', 'pptx', 'doc', 'docx'] 几种类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkDoc(String filename){
        return checkDoc(null, filename);
    }

    /**
     * 检查文档类型
     * @param types 可自行传入文件的类型集合，默认检查 ['pdf', 'ppt', 'xls', 'xlsx', 'pptx', 'doc', 'docx'] 几种类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkDoc(List<String> types, String filename){
        List<String> checkTypes = types;
        if(types == null || types.size() == 0){
            checkTypes = TYPE_DOC;
        }

        return checkType(checkTypes, filename);
    }

    /**
     * 检查音频类型. <br>
     * 默认检查 ['mp3', 'mp4', 'flv'] 几种类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkVideo(String filename){
        return checkVideo(null, filename);
    }

    /**
     * 检查音频类型
     * @param types 可自行传入文件的类型集合，默认检查 ['mp3', 'mp4', 'flv'] 几种类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkVideo(List<String> types, String filename){
        List<String> checkTypes = types;
        if(types == null || types.size() == 0){
            checkTypes = TYPE_VIDEO;
        }

        return checkType(checkTypes, filename);
    }

    /**
     * 检查压缩文件类型. <br>
     * 默认检查 ['zip', 'rar'] 几种类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkCompress(String filename){
        return checkCompress(null, filename);
    }

    /**
     * 检查压缩文件类型
     * @param types 可自行传入文件的类型集合，默认检查 ['zip', 'rar'] 几种类型
     * @param filename 文件名称
     * @return
     */
    public static boolean checkCompress(List<String> types, String filename){
        List<String> checkTypes = types;
        if(types == null || types.size() == 0){
            checkTypes = TYPE_COMPRESS;
        }

        return checkType(checkTypes, filename);
    }

    /**
     * 检查类型通用方法
     */
    private static boolean checkType(List<String> checkTypes, String filename){
        return checkTypes.contains(getFilenameSuffix(filename));
    }

    /**
     * 获取文件名称的后缀
     *
     * @param filename
     * @return 文件后缀
     */
    public static String getFilenameSuffix(String filename) {
        String suffix = null;
        if (StringUtils.isNotBlank(filename) && filename.contains(POINT)) {
            suffix = filename.substring(filename.lastIndexOf(POINT) + 1).toLowerCase();
        }
        return suffix;
    }

    /**
     * 判断上传的是不是图片
     *
     * @param filename
     * @return 文件后缀
     */
    /**
     * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
     * @param imageFile
     * @return
     */
    public static boolean isImage(MultipartFile imageFile) {
//        if (!imageFile.exists()) {
//            return false;
//        }
        Image img = null;
        try {
            FileInputStream in = (FileInputStream) imageFile.getInputStream();
            img = ImageIO.read(in);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }
}
