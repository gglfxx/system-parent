package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.dao.ImageDao;
import com.online.system.web.entity.ImageVideo;
import com.online.system.web.entity.Result;
import com.online.system.web.service.ImageService;
import com.online.system.common.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片实现类
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${minio.io.bucketName}")
    private String bucketName;

    @Value("${minio.io.endpoint}")
    private String endpoint;

    private static final String module="image";

    @Resource
    private ImageDao imageDao;
    /**
     * 上传图片
     * @param file
     * @return
     */
    @Override
    public Result<String> uploadImage(MultipartFile file, HttpServletRequest request) {
        Result<String> result = new Result<>();
        ImageVideo image = null;
        String resType = request.getParameter("resType");
        int code = 1;
        String msg = "图片上传失败！";
        String name = null;
        //图片路径
        String url = null;
        String imageUrl = null;
        try {
            if(file.isEmpty()|| StringUtils.isBlank(resType)){
                code = 1;
                msg = "上传文件或文件类型不能为空！";
            }else{
                name = file.getOriginalFilename();
                imageUrl = resType+"/"+name;
                url = MinioUtil.uploadFileByInputStream(bucketName, imageUrl, file.getInputStream(),file.getContentType());
                image = new ImageVideo();
                image.setModule(module);
                image.setResType(resType);
                image.setTitle(name.substring(0,name.lastIndexOf(".")));
                image.setSimpleName(name.substring(0,name.lastIndexOf(".")));
                image.setUrl(url);
                int count = imageDao.insert(image);
                if(count ==1){
                    code = 0;
                    msg = "图片上传成功";
                }
            }
        } catch (Exception e) {
            log.warn("图片管理上传图片异常："+e.getMessage());
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 图片查询
     * @param page
     * @return
     */
    @Override
    public Result<List<ImageVideo>> queryImages(Page<ImageVideo> page,String resType) {
        int code = 0;
        String msg = "查询成功";
        Result<List<ImageVideo>>  result = new Result<>();
        QueryWrapper<ImageVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("res_type", resType);
        queryWrapper.orderByAsc("create_time");
        IPage<ImageVideo> imageIPage = imageDao.selectPage(page,queryWrapper);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(imageIPage.getTotal());
        result.setData(imageIPage.getRecords());
        return result;
    }

    /**
     * 图片删除
     * @param request
     * @return
     */
    @Override
    public Result<String> deleteImages(HttpServletRequest request) {
        Result<String> result = new Result<>();
        List<String> deleteObjects = new ArrayList<>();
        int code = 1;
        String msg = "图片删除失败！";
        String ids =  request.getParameter("ids");
        String[] images = null;
        String imageUrl = null;
        try {
            if(StringUtils.isNotBlank(ids)&&ids.split(",").length>0){
                images = ids.split(",");
                //通过查询取出图片路径 然后先删除文件服务器上的资源 然后删除数据库
                QueryWrapper<ImageVideo> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("id",images);
                List<ImageVideo> imageList = imageDao.selectList(queryWrapper);
                if(imageList!=null&&!imageList.isEmpty()){
                    for(ImageVideo image:imageList){
                        imageUrl = image.getUrl().replaceAll(endpoint+"/"+bucketName+"/","");
                        deleteObjects.add(imageUrl);
                    }
                    boolean success = MinioUtil.deleteObjects(bucketName,deleteObjects);
                    if(success){
                        int count = imageDao.deleteBatchIds(Arrays.asList(images));
                        if(images.length==count){
                            code = 0;
                            msg = "图片删除成功";
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.warn("图片管理删除图片异常："+e.getMessage());
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
