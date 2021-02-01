package com.online.system.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.ImageVideo;
import com.online.system.web.entity.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图片接口
 */
public interface ImageService {
    //图片上传
    Result<String> uploadImage(MultipartFile file, HttpServletRequest request);

    //图片查询
    Result<List<ImageVideo>> queryImages(Page<ImageVideo> page,String resType);

    //删除多张或单张照片
    Result<String> deleteImages(HttpServletRequest request);
}
