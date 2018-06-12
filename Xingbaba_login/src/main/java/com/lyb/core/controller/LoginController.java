package com.lyb.core.controller;

import com.lyb.common.utils.RequestUtils;
import com.lyb.core.bean.user.Buyer;
import com.lyb.core.service.user.BuyerService;
import com.lyb.core.service.user.SessionProvider;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *单点登录系统
 *去登录界面 GET
 * 提交登录表单 POST
 * 加密 MD5 + 十六进制 + 加盐
 *
 * Created by lin on 2018/4/18.
 */
@Controller
public class LoginController {

    //去登录界面
    @RequestMapping(value = "/login.aspx",method = RequestMethod.GET) //俩请求路径相同，通过设置请求方式不同，让springmvc进行区分
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/isLogin.aspx")
    public @ResponseBody   //将MappingJacksonValue对象以Json形式返回
    MappingJacksonValue isLogin(String callback,HttpServletRequest request,HttpServletResponse response){ //callback(不能改名)是由jquery提供拥有传递数据的，可以实现跨域
        Integer result=0;
        String userName = sessionProvider.getAttributeForUserName(RequestUtils.getCSESSIONID(request, response));
        if(null != userName){
            result = 1;
        }
        /*JSONObject jo=new JSONObject();
        jo.put("result",result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter.write(jo.toString)*/

        MappingJacksonValue mjv=new MappingJacksonValue(result);
        mjv.setJsonpFunction(callback);
        return mjv;
    }

    @Autowired
    private BuyerService buyerService;
    @Autowired
    private  SessionProvider sessionProvider;
    @RequestMapping(value = "/login.aspx",method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl, HttpServletRequest request,
                        HttpServletResponse response,Model model){
        //1.用户名不能为空
        if(null !=  username){
            //2.密码名不能为空
            if(null!=password){
                //3.用户名不正确
                Buyer buyer = buyerService.selectBuyerByUserName(username);
                if(null != buyer){
                    //4.密码不正确
                    if(buyer.getPassword().equals(encodePassword(password))){
                        //5.保存用户名到Session中  实际保存到Redis中
                        sessionProvider.setAttributeForUserName(RequestUtils.getCSESSIONID(request,response),buyer.getUsername());
                        //6.跳转到之前访问界面  //这里可以判断returnUrl是否是空，是则返回首页，不是则返回原来页面
                        return "redirect:"+returnUrl;  //重定向中不能含有中文，有中文直接无效
                    }else {
                        model.addAttribute("error","密码必须正确");
                    }
                }else {
                    model.addAttribute("error","用户名必须正确");
                }
            }else {
                model.addAttribute("error","密码不能为空");
            }
        }else {
            model.addAttribute("error","用户名不能为空");
        }

        return "login";
    }

    //加密
    public String encodePassword(String password){
        //加盐 的思路 加上一定乱序字符串增加复杂度
        //password="sdsdsdnskksn" + password +"sdjsnsck";

        //1.一层 MD5 算法加密
        String algorithm="MD5";
        char[] chars=null;
        try {
            MessageDigest instance = MessageDigest.getInstance(algorithm); //使用jdk提供的MD5加密
            //加密结果
            byte[] digest = instance.digest(password.getBytes());

            //2.二层 十六进制
            chars = Hex.encodeHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(chars);
    }

}
