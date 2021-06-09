package com.online.system.common.utils;

import com.online.system.web.entity.MinIoProperties;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * minio工具类
 */

@Component
@Configuration
@EnableConfigurationProperties({MinIoProperties.class})
@Slf4j
public class MinioUtil {

    private static MinIoProperties minio;

    public MinioUtil(MinIoProperties minio) {
        this.minio = minio;
    }

    private static MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            String bucketName = minio.getBucketName();
            //初始化minio
            minioClient = MinioClient.builder().endpoint(minio.getEndpoint()).credentials(minio.getAccessKey(),
                    minio.getSecretKey()).build();
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                // 新建桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.warn("初始化minio异常："+e.getMessage());
        }
    }

    /**
     * 获取图片路径
     * @param bucket
     * @return
     */
    public static String getObjectPrefixUrl(String bucket) {
        return String.format("%s/%s/", minio.getEndpoint(), bucket);
    }

    /**
     * 根据文件路径上传
     * @param bucketName 桶名称
     * @param fileName 文件名称
     * @param path 文件路径
     * @return
     */
    public static String uploadFileByFileName(String bucketName,String fileName,String path)throws Exception{
        minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName).object(fileName)
                .filename(path).build());
        return getObjectPrefixUrl(bucketName) + fileName;
    }

    /**
     * 根据文件流上传
     * @param bucketName 存储桶名称
     * @param fileName 文件名称
     * @param input 输入流
     * @param contentType 文件类型
     * @return
     */
    public static String uploadFileByInputStream(String bucketName, String fileName,
                                                 InputStream input, String contentType)throws Exception{
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).
                stream(input, input.available(), -1).contentType(contentType).build());
        return getObjectPrefixUrl(bucketName) + fileName;
    }

    /**
     *
     * @param bucketName 存储桶名称
     * @param fileName 资源名称
     */
    public static void deleteObject(String bucketName,String fileName)throws Exception{
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }

    /**
     * 批量删除
     * @param bucketName
     * @throws Exception
     */
    public static boolean deleteObjects(String bucketName, List<String> objectNames)throws Exception{
        List<DeleteObject> deleteObjects = new ArrayList<>(objectNames.size());
        for (String objectName : objectNames){
            deleteObjects.add(new DeleteObject(objectName));
        }
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                RemoveObjectsArgs.builder().bucket(bucketName).objects(deleteObjects).build());
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            System.out.println(
                    "Error in deleting object " + error.objectName() + "; " + error.message());
        }
        return true;
    }

    /**
     * 获取资源流文件
     * @param bucketName
     * @param fileName
     * @return
     */
    public static InputStream getInputStream(String bucketName,String fileName)throws Exception{
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }

    /**
     * 获取文件路径
     * @param bucketName
     * @param fileName
     * @return
     */
    public static String getFileUrl(String bucketName,String fileName)throws Exception{
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).build());
    }

    /**
     * 带递归的匹配列出某个存储桶中的所有对象。
     * @param bucketName 桶名称
     * @param prefix
     * @param recursive
     * @return
     */
    public static Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive){
        return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build());
    }


}
