package com.lyb.core.message;

import com.lyb.core.service.SearchService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by lin on 2018/4/8.
 */
public class CustomMessageListener implements MessageListener{
    @Autowired
    private SearchService searchService;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage am=(ActiveMQTextMessage) message;
        try {
            System.out.println("ActiveMQ中消息内容："+am.getText());
            searchService.insertProductToSolr(Long.parseLong(am.getText()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
