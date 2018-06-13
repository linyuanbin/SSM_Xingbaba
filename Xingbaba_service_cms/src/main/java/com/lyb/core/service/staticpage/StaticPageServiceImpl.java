/*
package com.lyb.core.service.staticpage;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Map;

*/
/**
 * 静态化
 * Created by lin on 2018/4/17.
 *//*

public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

    //声明
    //注入
    private Configuration configuration;
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) { //通过spring注入FreeMarkerConfigurer,得到configuration对象
        this.configuration=freeMarkerConfigurer.getConfiguration();
    }

    //商品静态化   Freemarker实现静态化    模板+数据=输出
    public void ProductStaticPage(Map<String,Object> map,String id){//productId

        //输出的路径
        String path = getPath("/html/product/" + id + ".html");
        File f=new File(path);
        File f2=new File("E:/apapap.txt");
        File parentFile = f.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();//创建多级父目录
        }

        Writer out=null;
        try {
            //加载指定模板  读文件 UTF-8
            Template template = configuration.getTemplate("product.html");

            //输出流  UTF-8
            out  =new OutputStreamWriter(new FileOutputStream(f),"UTF-8");

            template.process(map,out);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(path);
        }finally {
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(path);
                }
            }
        }

    }

    //获取真正路径
    public String getPath(String name){
        return servletContext.getRealPath(name);
    }

    //声明
    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
    }
}
*/
package com.lyb.core.service.staticpage;
import java.io.*;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态化
 * @author lin
 *
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

    //声明
    //注入
    private Configuration conf;
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.conf = freeMarkerConfigurer.getConfiguration();
    }

    //静态化 商品  ActiveMQ
    public void productStaticPage(Map<String,Object> root,String id){
        //输出的路径  全路径
        String path = getPath("/html/product/" + id + ".html");
        File f = new File(path);
        File parentFile = f.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        Writer out = null;
        FileOutputStream fileOutputStream=null;
        try {
            //读文件  UTF-8
            Template template = conf.getTemplate("product.html");
            //输出  UTF-8
            fileOutputStream=new FileOutputStream(f);
            out=new OutputStreamWriter(fileOutputStream,"UTF-8");
            //处理
            template.process(root,out);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("out=null 异常："+path);
        }finally {
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(null != fileOutputStream){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    //获取路径
    public String getPath(String name){
        return servletContext.getRealPath(name);
    }
    //声明
    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        // TODO Auto-generated method stub
        this.servletContext = servletContext;
    }

    //静态化订单
}
