package com.lyb.core.service.product;

import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.*;
import com.lyb.core.dao.product.BrandDao;
import com.lyb.core.dao.product.ColorDao;
import com.lyb.core.dao.product.ProductDao;
import com.lyb.core.dao.product.SkuDao;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.aspectj.weaver.patterns.PerObject;
import org.springframework.aop.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by lin on 2018/3/25.
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ColorDao colorDao;


    public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow){

        ProductQuery productQuery=new ProductQuery();
        productQuery.setPageNo(Pagination.cpn(pageNo)); //页号只可能大于等于1
        //排序
        productQuery.setOrderByClause("id desc"); //倒序
        //默认每页条数10
        //productQuery.setPageSize(10);
        StringBuilder params=new StringBuilder();
        ProductQuery.Criteria criteria = productQuery.createCriteria();
        if(null != name){
            criteria.andNameLike("%"+name+"%");
            params.append("name=").append(name);
        }
        if(null != brandId){
            criteria.andBrandIdEqualTo(brandId);
            params.append("&brandId=").append(brandId);
        }
        if(null != isShow){
            criteria.andIsShowEqualTo(isShow);
            params.append("&isShow=").append(isShow);
        }else {
            criteria.andIsShowEqualTo(false);//默认显示下架
            params.append("&isShow=").append(false);
        }

        Pagination pagination=new Pagination(
            productQuery.getPageNo(),
                productQuery.getPageSize(),
                productDao.countByExample(productQuery),//总条数
                productDao.selectByExample(productQuery)
        );

        String url="/product/list.do";
        pagination.pageView(url,params.toString()); //分页
        return pagination;
    }


    //加载颜色
    public List<Color> selectColorList(){
        ColorQuery colorQuery=new ColorQuery();
        colorQuery.createCriteria().andParentIdNotEqualTo(0L); //父id不是0的颜色，（不加载色系）
        return colorDao.selectByExample(colorQuery);
    }

    //保存商品
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private Jedis jedis;
    public void insertProduct(Product product){
        //保存商品
        Long id = jedis.incr("pno"); //通过Redis对id进行自增长
        product.setId(id);
        //默认商品是下架状态
        product.setIsShow(false);
        //删除 后台程序写的不删除
        product.setIsDel(true);
        productDao.insertSelective(product);

        //返回ID
        //保存SKU
        String[] colors=product.getColors().split(",");
        String[] sizes=product.getSizes().split(",");

        for(String color:colors){
            for (String size:sizes){
                //保存SKU
                Sku sku=new Sku();
                //商品ID
               sku.setProductId(product.getId());
                //颜色
                sku.setColorId(Long.parseLong(color));
                //尺码
                sku.setSize(size);
                //市场价
                sku.setMarketPrice(999f);
                //售价
                sku.setPrice(666f);
                //运费
                sku.setDeliveFee(8f);
                //库存
                sku.setStock(0);
                //最大限制购买
                sku.setUpperLimit(200);
                //保存时间
                sku.setCreateTime(new Date());
                skuDao.insert(sku);
            }
        }
    }

    @Autowired
    private JmsTemplate jmsTemplate;
    //上架
    public void isShow(Long[] ids){
        Product product=new Product();
        product.setIsShow(true);
        for(final Long id:ids){
            product.setId(id);
            //商品状态变更
            productDao.updateByPrimaryKeySelective(product);

            //发送消息到ActiveMQ中 brandId
            //mq.xml中指定了默认目标，这里发送消息也可以重新指定目标
            //jmsTemplate.send("brandId",messageCreator);
            jmsTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));  //发送消息
                }
            });

            //TODO 静态化
        }


    }


}
