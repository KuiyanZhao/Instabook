package com.instabook.service.impl;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.File;
import com.instabook.mapper.FileMapper;
import com.instabook.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Kuiyan Zhao
 * @version 1.0 2024-03-29
 * @since 2024-03-29
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Value("${alibaba.cloud.access-id}")
    private String accessId;

    @Value("${alibaba.cloud.access-key}")
    private String accessKey;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    @Value("${alibaba.cloud.oss.internal-endpoint}")
    private String internalEndpoint;

    private String getEndpoint() {
        if (StringUtils.isNotBlank(internalEndpoint) && !internalEndpoint.equals("null")) {
            return internalEndpoint;
        }
        return endpoint;
    }

    @Value("${alibaba.cloud.oss.bucket-name}")
    private String bucketName;



    @Override
    public File upload(File file) {
        String host = "https://".concat(bucketName + "." + endpoint);
        //authorization
        UserTokenInterceptor.getUser();
        file.setFileId(IdUtil.getSnowflakeNextId());
        file.setPath(LocalDate.now() + "/" + file.getFileId() + "/" + file.getMultipartFile().getOriginalFilename());
        file.setUrl(host + "/" + file.getPath());
        this.save(file);

        OSS ossClient = new OSSClientBuilder().build(getEndpoint(), accessId, accessKey);

        try (InputStream inputStream = file.getMultipartFile().getInputStream()) {
            ossClient.putObject(bucketName, file.getPath(), inputStream);
        } catch (IOException e) {
            throw new ClientException(ClientErrorEnum.IOError);
        } catch (OSSException e) {
            throw new ClientException(ClientErrorEnum.IOError, "OSS ERROR: " + e.getErrorMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        file.setMultipartFile(null);
        return file;
    }

    @Override
    public File delete(Long fileId) {
        File file = this.getById(fileId);
        if (file == null) {
            throw new ClientException(ClientErrorEnum.DataNotExist);
        }

        OSS ossClient = new OSSClientBuilder().build(getEndpoint(), accessId, accessKey);
        try {
            ossClient.deleteObject(bucketName, file.getPath());
        } catch (OSSException e) {
            throw new ClientException(ClientErrorEnum.IOError, "OSS ERROR: " + e.getErrorMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        this.removeById(fileId);
        return file;
    }

    @Override
    public File getByOuterId(Long userId) {
        return this.getOne(new QueryWrapper<File>().eq("outer_id", userId).last("limit 1"));
    }


}
