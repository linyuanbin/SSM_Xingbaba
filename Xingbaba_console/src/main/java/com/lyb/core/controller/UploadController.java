package com.lyb.core.controller;

import com.lyb.common.web.Constants;
import com.lyb.core.service.product.UploadService;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lin on 2018/3/20.
 */
@Controller
public class UploadController {

    @Autowired
    private UploadService uploadService;

    //上传图片
    @RequestMapping(value="/upload/uploadPic.do")   //这里参数名 pic 需要和edit.jsp中 <input type="file" name="pic" onchange="uploadPic()"/> 标签的name值一致，否则接收不到
    public void uploadPic(@RequestParam(required = false) MultipartFile pic, HttpServletResponse response) throws IOException { //pic允许空
        //public void uploadPic(@RequestParam MultipartFile pic){ //这样当pic传进来空值时就会抛出异常
        String ext = FilenameUtils.getExtension(pic.getOriginalFilename());
        String path = uploadService.uploadPic(pic.getBytes(),pic.getOriginalFilename(),pic.getSize()); //接收到保存到服务器中文件夹路径
        System.out.println(path);
        String url= Constants.IMG_URL + path ;
        JSONObject jo=new JSONObject();
        jo.put("url",url);
        response.setContentType("application/json;charset=UTF-8"); //返回值类型 json
        response.getWriter().write(jo.toString());
    }

    //批量上传图片
    @RequestMapping(value="/upload/uploadPics.do")   //这里参数名 pic 需要和edit.jsp中 <input type="file" name="pic" onchange="uploadPic()"/> 标签的name值一致，否则接收不到
    public @ResponseBody  //@ResponseBody springMvc将对象转换成字符串传到页面
    List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics, HttpServletResponse response) throws IOException { //pic允许空
        //public void uploadPic(@RequestParam MultipartFile pic){ //这样当pic传进来空值时就会抛出异常
        //String ext = FilenameUtils.getExtension(pic.getOriginalFilename());
        List<String> urls = new ArrayList<String>();
        for (MultipartFile pic : pics) {
                String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize()); //接收到保存到服务器中文件夹路径
                String url = Constants.IMG_URL + path;
                urls.add(url);
        }
        return urls;
    }

    //上传富文本图片
    @RequestMapping(value="/upload/uploadFck.do")
    public void uploadPic(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //在不知道前台返回数据的key时，可以用Spring提供的MultipartRequest进行接收，这时候MultipartRequest里面只有图片资源
        MultipartRequest mr=(MultipartRequest)request;
        Map<String, MultipartFile> fileMap = mr.getFileMap();
        Set<Map.Entry<String, MultipartFile>> entries = fileMap.entrySet();
        for(Map.Entry<String, MultipartFile> entry:entries){
            MultipartFile pic=entry.getValue();
            String path = uploadService.uploadPic(pic.getBytes(),pic.getOriginalFilename(),pic.getSize()); //接收到保存到服务器中文件夹路径
            String url= Constants.IMG_URL + path ;
            JSONObject jo=new JSONObject();
            jo.put("error",0);//表示上传成功
            jo.put("url",url);
            response.setContentType("application/json;charset=UTF-8"); //返回值类型 json
            response.getWriter().write(jo.toString());
        }
    }

}
