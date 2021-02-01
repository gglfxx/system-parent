package com.online.system.web.handler;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.ImageVideo;
import com.online.system.web.entity.Result;
import com.online.system.web.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图片管理
 */
@RestController
@RequestMapping("/image")
public class ImageHandler {

    @Autowired
    private ImageService imageService;

    /**
     * 查询图片
     * 加载图片
     * @param request
     * @return
     */
    @GetMapping("/findImages")
    public Result<List<ImageVideo>> queryImages(HttpServletRequest request){
        Result<List<ImageVideo>> result = new Result<>();
        int pageNo = Integer.valueOf(request.getParameter("pageNo"));
        int pageSize = Integer.valueOf(request.getParameter("pageSize"));
        String resType = request.getParameter("resType");
        Page<ImageVideo> page = new Page<>(pageNo,pageSize);
        result = imageService.queryImages(page,resType);
        return result;
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("uploadImage")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        Result<String> result = new Result<>();
        result = imageService.uploadImage(file,request);
        return result;
    }

    /**
     * 删除多张或单张图片
     * @param request
     * @return
     */
    @GetMapping("deleteImages")
    public Result<String> deleteImages(HttpServletRequest request){
        Result<String> result = new Result<>();
        result = imageService.deleteImages(request);
        return result;
    }
}
