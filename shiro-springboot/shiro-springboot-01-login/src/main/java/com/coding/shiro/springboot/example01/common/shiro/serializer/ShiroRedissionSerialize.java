package com.coding.shiro.springboot.example01.common.shiro.serializer;

import java.io.*;

import org.apache.shiro.codec.Base64;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义序列化工具
 */
@Slf4j
public class ShiroRedissionSerialize {

    // 序列化方法
    public static String serialize(Object object) {
        // 判断对象是否为空
        if (ObjectUtils.isEmpty(object)) {
            return null;
        }
        // 流的操作
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        String encodeBase64 = null;
        bos = new ByteArrayOutputStream();
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            // 转换字符串
            encodeBase64 = Base64.encodeToString(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("流写入异常", e);
        } finally {
            // 关闭流
            try {
                bos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("流写入异常", e);
            }
        }
        return encodeBase64;
    }

    // 反序列化方法
    public static Object deserialize(String str) {
        // 判断是否为空
        if (ObjectUtils.isEmpty(str)) {
            return null;
        }
        // 流从操作
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        Object object = null;
        // 转换对象
        bis = new ByteArrayInputStream(Base64.decode(str));
        try {
            ois = new ObjectInputStream(bis);
            object = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("流读取异常", e);
        } finally {
            // 关闭流
            try {
                bis.close();
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("流读取异常", e);
            }

        }
        return object;
    }
}
