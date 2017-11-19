package com.wy.insurance.box.backend.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public class OSSUtils {

    private String accessKeyId;
    private String secretAccessKey;
    private String endpoint;
    private OSSClient client;
    public static final String RES_BUCKET_NAME = "wyres";
    private static final String OSS_BUCKET_URL = "https://res.winbaoxian.com/";


    /**
     * Instantiates a new Oss util.
     *
     * @param accessKeyId     the access key id
     * @param secretAccessKey the secret access key
     */
    public OSSUtils(String accessKeyId, String secretAccessKey) {
        this(null, accessKeyId, secretAccessKey);
    }

    /**
     * Instantiates a new Oss util.
     *
     * @param endpoint        the endpoint
     * @param accessKeyId     the access key id
     * @param secretAccessKey the secret access key
     */
    public OSSUtils(String endpoint, String accessKeyId, String secretAccessKey) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.endpoint = endpoint;
        initClient(endpoint, accessKeyId, secretAccessKey);
    }

    /**
     * 初始化client
     *
     * @param endpoint
     * @param accessKeyId
     * @param secretAccessKey
     */
    private void initClient(String endpoint, String accessKeyId, String secretAccessKey) {
        if (client == null) {
            if (StringUtils.isNotEmpty(endpoint)) {
                client = new OSSClient(endpoint, accessKeyId, secretAccessKey);
            } else {
                client = new OSSClient(accessKeyId, secretAccessKey);
            }
        }
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public OSSClient getClient() {
        initClient(endpoint, accessKeyId, secretAccessKey);
        return client;
    }

    /**
     * Upload image string.
     *
     * @param folderName the folder name
     * @param fileName   the file name
     * @param file       the file
     * @return the string
     * @throws Exception the exception
     */
    public String uploadImage(String folderName, String fileName, MultipartFile file) throws Exception {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.getSize());
        // 可以在metadata中标记文件类型
        String contentType = file.getContentType();
        if (StringUtils.isNotEmpty(contentType)) {
            objectMeta.setContentType(contentType);
        }
        if (file != null) {
            StringBuilder key = new StringBuilder(folderName);
            if (!folderName.endsWith("/")) {
                key.append("/");
            }
            if (contentType.indexOf("image/") == 0) {
                key.append(fileName).append(".").append(contentType.substring(6));
            } else {
                key.append(fileName).append(".jpg");
            }
            client.putObject(RES_BUCKET_NAME, key.toString(), file.getInputStream(), objectMeta);
            return OSS_BUCKET_URL + key.toString();
        } else {
            return null;
        }
    }

    public String uploadPdf(String folderName, String fileName, File file) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        folderName = folderName + simpleDateFormat.format(new Date()) + "/";
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("application/pdf");
        String key = folderName + fileName + ".pdf";
        client.putObject(RES_BUCKET_NAME, key, new FileInputStream(file), objectMeta);
        return OSS_BUCKET_URL + key;
    }

    /**
     * Upload file string.
     *
     * @param folderName the folder name
     * @param fileName   the file name
     * @param file       the file
     * @return string string
     * @throws Exception the exception
     */
    public String uploadFile(String folderName, String fileName, File file, String fileType) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        folderName = folderName + simpleDateFormat.format(new Date()) + "/";
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("application/" + fileType);
        String key = folderName + fileName;
        client.putObject(RES_BUCKET_NAME, key, new FileInputStream(file), objectMeta);
        return OSS_BUCKET_URL + key;
    }

    /**
     * Upload file string.
     *
     * @param folderName  the folder name
     * @param fileName    the file name
     * @param inputStream the input stream
     * @return string string
     * @throws Exception the exception
     */
    public String uploadFile(String folderName, String fileName, InputStream inputStream) throws Exception {
        if (inputStream != null) {
            StringBuilder key = new StringBuilder(folderName);
            if (!folderName.endsWith("/")) {
                key.append("/");
            }
            key.append(fileName);
            client.putObject(RES_BUCKET_NAME, key.toString(), inputStream);
            return OSS_BUCKET_URL + key.toString();
        } else {
            return null;
        }
    }
}
