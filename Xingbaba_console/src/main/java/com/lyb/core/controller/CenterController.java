package com.lyb.core.controller;

import com.lyb.core.bean.TestTb;
import com.lyb.core.service.TestTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by lin on 2018/3/13.
 */
@Controller
@RequestMapping(value ="/control")  //提出公共的文件夹路径
public class CenterController {

/*
   @Autowired
    private TestTbService testTbService;
*/

//入口
    /*返回值类型
    ModelAndView :跳转视图 + 数据  不用
    Void ：异步时用 ajax 时使用
    String :跳转视图 + Model
     */
/*
    @RequestMapping(value ="/test/index.do")
    public String index(Model model){ //这里的Model用于携带数据
        TestTb testTb=new TestTb();
        testTb.setName("范冰冰10");
        testTb.setBirthday(new Date());
        testTbService.insertTetstTb(testTb);
        return "index";
    }*/

    //入口
    @RequestMapping(value ="/index.do")
    public String index(Model model){ //这里的Model用于携带数据
        return "index";
    }

    //头部
    @RequestMapping(value ="/top.do")
    public String top(Model model){ //这里的Model用于携带数据

        return "top";
    }

    //身体
    @RequestMapping(value ="/main.do")
    public String main(Model model){ //这里的Model用于携带数据

        return "main";
    }
    //左身体
    @RequestMapping(value ="/left.do")
    public String left(Model model){ //这里的Model用于携带数据

        return "left";
    }
    //右身体
    @RequestMapping(value ="/right.do")
    public String right(Model model){ //这里的Model用于携带数据

        return "right";
    }

    //产品身体
    @RequestMapping(value ="/frame/product_main.do")
    public String product_main(Model model){ //这里的Model用于携带数据

        return "frame/product_main";
    }

    //产品身体-左边
    @RequestMapping(value ="/frame/product_left.do")
    public String product_left(Model model){ //这里的Model用于携带数据

        return "frame/product_left";
    }


}
