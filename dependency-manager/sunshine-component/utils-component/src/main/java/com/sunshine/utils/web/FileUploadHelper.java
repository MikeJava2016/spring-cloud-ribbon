package com.sunshine.utils.web;

import com.sunshine.common.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/26 8:47
 **/
public class FileUploadHelper {

    private static Logger logger = LoggerFactory.getLogger(FileUploadHelper.class);

    private static final String FILE_PARAM = "file";

    public Result uploadFile(HttpServletRequest request, HttpServletResponse response) {
        MultipartFile file = (MultipartFile) request.getAttribute(FILE_PARAM);
        if (file == null) {
            logger.error("图片上传 获取不到图片文件");
        }
        return Result.success();
    }
}
