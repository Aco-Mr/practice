package com.aco.practice.demo1.util;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author A.co
 * @date 2023/4/3 21:48
 */
@Slf4j
public class JasyptUtils {
    /**
     * 加密使用密钥
     */
    private static final String PRIVATE_KEY = "acoBasicAlgorithm";

    /**
     * 文本加密
     */
    private static BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

    static {
        basicTextEncryptor.setPassword(PRIVATE_KEY);
    }

    /**
     * 私有构造方法，防止被意外实例化
     */
    private JasyptUtils() {
    }

    /**
     * 明文加密
     *
     * @param plaintext 明文
     * @return String
     */
    public static String encrypt(String plaintext) {
        log.info("明文字符串为：{}", plaintext);
        // 使用的加密算法可以在源码的类注释中看到
        String ciphertext = basicTextEncryptor.encrypt(plaintext);
        log.info("密文字符串为：{}", ciphertext);
        return ciphertext;
    }

    /**
     * 解密
     *
     * @param ciphertext 密文
     * @return String
     */
    public static String decrypt(String ciphertext) {
        log.info("密文字符串为：{}", ciphertext);
        ciphertext = "ENC(" + ciphertext + ")";
        if (PropertyValueEncryptionUtils.isEncryptedValue(ciphertext)) {
            String plaintext = PropertyValueEncryptionUtils.decrypt(ciphertext, basicTextEncryptor);
            log.info("明文字符串为：{}", plaintext);
            return plaintext;
        }
        log.error("解密失败！");
        return "";
    }
}
