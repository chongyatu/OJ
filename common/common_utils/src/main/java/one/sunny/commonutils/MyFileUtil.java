package one.sunny.commonutils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MyFileUtil {
    /**
     * MultipartFile 是一个在 Java 编程中用于处理文件上传的接口，
     * 常见于 Spring Framework 的 Web 应用程序中，尤其是在使用 Spring MVC 的场景下。
     * 它主要用于接收客户端上传的文件，并在服务器端进行处理。
     * @param multiFile
     * @return
     * @throws IOException
     */

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
            /**
             * 在 Java 中，使用 File.createTempFile 方法创建临时文件后，
             * 可以通过 MultipartFile 的 transferTo 方法将上传的文件内容写入到这个临时文件中。
             * 这是一种常见的处理文件上传并将文件保存到临时位置的做法。
             */
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
