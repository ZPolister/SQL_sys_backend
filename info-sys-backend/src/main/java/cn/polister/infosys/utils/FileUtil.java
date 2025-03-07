package cn.polister.infosys.utils;

import cn.idev.excel.FastExcel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class FileUtil {
    /**
     * 输出excel文件
     * @param data 输出的数据
     */
    public<T> void outputExcelFile(String fileName, List<T> data, Class<?> dataClass, String sheetName) throws IOException {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();


        assert response != null;
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader("Content-disposition",
                "attachment;filename*=utf-8''" + fileName + ".xlsx");
        Long start = System.currentTimeMillis();
        // 写入数据
        FastExcel.write(response.getOutputStream(), dataClass)
                .sheet(sheetName)
                .doWrite(data);
        Long end = System.currentTimeMillis();
        log.info("输出{}成功，耗时：{}", fileName, end - start);
    }
}
