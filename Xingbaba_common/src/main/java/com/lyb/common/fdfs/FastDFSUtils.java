package com.lyb.common.fdfs;

/*
import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;
*/
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * 上传图片到FastFDFS
 * Created by lin on 2018/3/21.
 */
public class FastDFSUtils {

    public static String uploadPic(byte[] pic ,String name,long size){
        String path = null;
        //ClientGloble 读配置文件
        ClassPathResource resource = new ClassPathResource("fdfs_client.conf");//获取resources下面的资源
        try {
            String dfs=resource.getClassLoader().getResource("fdfs_client.conf").getPath();
            System.out.println(dfs+"..............................");
            ClientGlobal.init(dfs);//通过resource得到路径
            //老大客户端
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();//通过主服务器客户端获取存储文件的目标服务器的地址 trackerServer中存放着小弟地址
            StorageServer storageServer  = null;
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);//storageServer这个参数没有意义所有用空值
            String ext = FilenameUtils.getExtension(name);

            NameValuePair[] meta_list = new NameValuePair[3];
            meta_list[0] = new NameValuePair("fileName",name); //文件原始名
            meta_list[1] = new NameValuePair("fileExt",ext);//文件扩展名
            meta_list[2] = new NameValuePair("fileSize",String.valueOf(size)); //图片大小

            //  group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
            path = storageClient1.upload_file1(pic, ext, meta_list);  //上传图片  返回存储在文件系统的子节点文件夹中的具体路径
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return path;
    }
}
