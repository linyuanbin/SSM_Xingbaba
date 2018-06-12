package com.lyb.core.message;

import com.lyb.core.bean.product.Color;
import com.lyb.core.bean.product.Product;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.service.CmsService;
import com.lyb.core.service.SearchService;
import com.lyb.core.service.staticpage.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.*;

/**
 * Created by lin on 2018/4/8.
 */
public class CustomMessageListener implements MessageListener{

    @Autowired
    private StaticPageService staticPageService;
    @Autowired
    private CmsService cmsService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage am=(ActiveMQTextMessage) message;
        try {
            System.out.println("ActiveMQ中消息内容：cms"+am.getText());
            String id=am.getText();

            //数据
            Map<String,Object> root=new HashMap<String, Object>();

            //商品
            Product product = cmsService.selectProductById(Long.parseLong(id));
            //sku集
            List<Sku> skus = cmsService.selectSkuListByProductId(Long.parseLong(id));

            //遍历一次  颜色相同不要
            Set<Color> colors=new HashSet<Color>();  //Set中存放的是对象，要去重需要重写Color的equals和hashCode方法，通过比较对象的id进行去重
            for(Sku sku:skus){
                colors.add(sku.getColor());
            }

            root.put("product",product); //商品对象
            root.put("skus",skus); //sku对象集 包含color对象
            root.put("colors",colors);

            staticPageService.productStaticPage(root,id);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
