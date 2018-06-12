# 电商系统

## 这是一个分布式电商系统
项目功能实现：</br>

本系统使用RPC模式进行开发，使用Bubbo结合Zookeepe（做服务注册中心）实习服务提供方和服务消费方的接口调用。</br>

### 后台管理服务

* 商品添加（利用redis实现商品id全国唯一性）

* 商品上架（ActiveMQ消息队列实现不同系统间通信、solr进行商品信息保存）

* sku管理

* 品牌管理

### 前台系统实现

* 商品搜索（基于solr、商品详细界面使用Freemarker实现静态化）

* 商品添加购物车（涉及sku联动、redis实现用户购物车管理）

* 单点登录系统（基于redis实现Session共享）

* 订单提交系统（利用单点登录系统实现用户登录判断）</br>

具体集群环境搭建：</br>

[我的博客](https://blog.csdn.net/AaronLin6)</br>

[Solr服务搭建](https://blog.csdn.net/AaronLin6/article/details/79826667)</br>

[Nginx搭建](https://blog.csdn.net/aaronlin6/article/details/80099939)</br>

[ActiveMQ](https://blog.csdn.net/aaronlin6/article/details/79854297)
