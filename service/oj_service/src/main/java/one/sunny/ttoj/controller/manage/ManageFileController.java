package one.sunny.ttoj.controller.manage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Api("manage_file_service")
@RestController
@RequestMapping("manage/file")
@CrossOrigin
public class ManageFileController {
    @Value("${testCaseSaveLocation}")
    private String testCaseSaveLocation;

    @ApiOperation("通过题目id获得题目测试用例")
    @GetMapping("testcase/{problemId}")
    public void downloadTestCase(HttpServletResponse response, @PathVariable("problemId") Long problemId) throws IOException {
        File downloadFile = new File(testCaseSaveLocation + problemId + ".zip");
        if (!downloadFile.exists()) {
            return;
        }
        FileInputStream inputStream = new FileInputStream(downloadFile);
        // 设置响应头、以附件形式打开文件
        response.setHeader("content-disposition", "attachment;filename=" + problemId + ".zip");
        //文件大小
        response.addHeader("content-length", "" + downloadFile.length());
        response.addHeader("content-type", "application/zip");
        ServletOutputStream outputStream = response.getOutputStream();
        int len;
        byte[] data = new byte[1024];
        while ((len = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, len);
        }
        outputStream.close();
        inputStream.close();
    }
}
