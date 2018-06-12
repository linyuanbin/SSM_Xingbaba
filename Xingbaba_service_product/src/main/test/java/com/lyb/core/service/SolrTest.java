package com.lyb.core.service;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.print.Doc;
import java.io.IOException;

/**
 * Created by lin on 2018/3/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class SolrTest {

    @Autowired
    private SolrServer solrServer;
    @Test
    public void testSolr() throws IOException, SolrServerException {
        SolrInputDocument doc=new SolrInputDocument();
        doc.addField("id",3);
        doc.addField("name","大头儿子3");
        solrServer.add(doc);
        solrServer.commit();
    }


   /* @Test
    public void testSolr() throws IOException, SolrServerException {
        String baseUrl="http://192.168.200.128:8080/solr";
        SolrServer  solrServer=new HttpSolrServer(baseUrl);
        SolrInputDocument doc=new SolrInputDocument();
        doc.addField("id",2);
        doc.addField("name","老王");
        solrServer.add(doc);
        solrServer.commit();
    }*/

}
