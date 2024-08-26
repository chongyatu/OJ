package one.sunny.commonutils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MyFileUtil {
    public static File MultipartFileToFile(MultipartFile multiFile) throws IOException {
        if (multiFile == null) {
            return null;
        }
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, suffix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            throw e;
        }
    }

    public static String readFileByStripTrailing(File file) {
        String readString = FileUtil.readString(file, StandardCharsets.UTF_8);
//        return readString.replaceAll("\r\n", "\n").replaceAll("\r", "\n").stripTrailing();
        return null;
    }

    public static String md5FileByStripTrailing(File file) {
        return SecureUtil.md5(readFileByStripTrailing(file));
    }
}
