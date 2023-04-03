package com.aco.practice.demo1;

import com.aco.practice.demo1.util.JasyptUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class Demo1ApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    public void testEncrypt(){
        String plainText = "123456";
        String ciperText = JasyptUtils.encrypt(plainText);
        log.info("加密后的密文为：{}", ciperText);
    }

    @Test
    public void testDecrypt(){
        String ciperText = "7i/Lkb9SnPzWnx+0NzTvNw==";
        String plainText = JasyptUtils.decrypt(ciperText);
        log.info("解密后的明文为：{}", plainText);
    }
}
