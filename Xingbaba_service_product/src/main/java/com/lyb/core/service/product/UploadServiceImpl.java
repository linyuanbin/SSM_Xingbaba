package com.lyb.core.service.product;

import com.lyb.common.fdfs.FastDFSUtils;
import org.springframework.stereotype.Service;

/**
 * Created by lin on 2018/3/21.
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

public String uploadPic(byte[] pic,String name,long size){
    System.out.println("Service..........................");
    return FastDFSUtils.uploadPic(pic,name,size);
}

}
