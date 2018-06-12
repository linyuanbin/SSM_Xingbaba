package com.lyb.core.service;

import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.Product;
import com.lyb.core.bean.product.ProductQuery;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.bean.product.SkuQuery;
import com.lyb.core.dao.product.ProductDao;
import com.lyb.core.dao.product.SkuDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全文搜索  用 Solr 实现
 * Created by lin on 2018/4/6.
 */
@Service(value = "searchService")
public class SearchServiceImpl implements SearchService{

    @Autowired
    private SolrServer solrServer;
    public Pagination searchPaginationByQuery(Integer pageNo,String keyword,Long brandId,String price) throws Exception {

        //创建包装类
        ProductQuery productQuery=new ProductQuery();
        StringBuilder params=new StringBuilder();
        params.append("keyword="+keyword);
        //当前页
        productQuery.setPageNo(Pagination.cpn(pageNo));
        //每页12条
        productQuery.setPageSize(12);

        List<Product> products=new ArrayList<>();

        SolrQuery solrQuery=new SolrQuery();
        //关键词
        solrQuery.set("q", "name_ik:" + keyword); //solr查询条件
        //过滤条件
        if(null != brandId){  //品牌
            solrQuery.addFilterQuery("brandId:"+brandId);
        }
        if(null != price){ //价格  0-99 1600
            String[] p=price.split("-");
            if(p.length == 2){
                //solrQuery.set("fq","price:[0 to 99]"); //查询价格在 0-99的商品信息  price:[0 TO 1500]
                solrQuery.addFilterQuery("price:["+p[0]+" TO "+p[1]+"]"); //不包含临界值时用小括号
            }else{
                solrQuery.addFilterQuery("price:["+p[0]+" TO *]"); //1600以上
            }
        }

        //设置高亮
        //存高亮
        solrQuery.setHighlight(true);//开启设置高亮
        solrQuery.addHighlightField("name_ik");//设置高亮的字段
        //设置高亮简单样式   <span style='color:red' >2016</span>
        solrQuery.setHighlightSimplePre("<span style='color:red'>"); //样式前缀
        solrQuery.setHighlightSimplePost("</span>");

        //拍排序
        solrQuery.addSort("price", SolrQuery.ORDER.asc);//递增顺序
        //分页
        solrQuery.setStart(productQuery.getStartRow()); //设置从solr查询的开始行
        solrQuery.setRows(productQuery.getPageSize());  // 设置从solr中查询每页条数
        //执行查询
        QueryResponse response = solrServer.query(solrQuery);

        //取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        // Map K ：V  442:map
        //map  k:v    name_ik : List<String>
        //List<String> list  2016新款    list.get(0)  //可以有多个

        //结果集
        SolrDocumentList docs = response.getResults();
        //发现的总条数  在构建分页时可使用
        long numFound = docs.getNumFound();

        for(SolrDocument doc:docs){
            Product product=new Product();
            //商品id
            String id= (String) doc.get("id");
            product.setId(Long.parseLong(id));
            //商品名称 ik
          /*  String name= (String) doc.get("name_ik");
            product.setName(name);*/
            //取高亮的name_ik值
            Map<String, List<String>> stringListMap = highlighting.get(id);
            List<String> list = stringListMap.get("name_ik");
            product.setName(list.get(0));

            //商品图片
            String url= (String) doc.get("url");
            product.setImgUrl(url);
            //售价
            product.setPrice((Float) doc.get("price"));
            //品牌ID
            product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));
            products.add(product);
        }

        //构建分页对象
        Pagination pagination=new Pagination(
                productQuery.getPageNo(),
                productQuery.getPageSize(),
                 Integer.parseInt(String.valueOf(numFound)),//总条数
                products
        );

        String url="/search";
        pagination.pageView(url,params.toString()); //分页
        return pagination;
    }

    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;
    //上架是时保存商品到solr
    public void insertProductToSolr(Long id){
        //TODO 保存商品信息到solr服务器
        SolrInputDocument doc=new SolrInputDocument();
        //商品ID
        doc.addField("id",id);
        //商品名称
        Product p = productDao.selectByPrimaryKey(id);
        doc.addField("name_ik",p.getName());
        //图片
        doc.addField("url",p.getImages()[0]); //只要第一张图片就行
        //商品价格  售价  select price from bbs_sku where product_id=id order by price asc limit 0,1   //查找到id对应商品的最低价格
        //用skuQuery拼接sql
        SkuQuery skuQuery=new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(id);
        skuQuery.setOrderByClause("price asc");
        skuQuery.setPageNo(1);
        skuQuery.setPageSize(1);
        skuQuery.setFields("price");
        List<Sku> skus = skuDao.selectByExample(skuQuery);
        doc.addField("price",skus.get(0).getPrice());
        //品牌id
        doc.addField("brandId",p.getBrandId()); //由于brandId这个名称solr的schema.xml文件中没有，因此需要先到schema.xml中添加   <field name="brandId" type="int" indexed="true" stored="true" />  497行
        //时间 可选
        try {
            solrServer.add(doc);
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
