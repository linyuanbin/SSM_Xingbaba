-- MySQL dump 10.13  Distrib 5.6.28, for Win64 (x86_64)
--
-- Host: localhost    Database: babasport
-- ------------------------------------------------------
-- Server version	5.6.28-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bbs_brand`
--

DROP TABLE IF EXISTS `bbs_brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(40) NOT NULL COMMENT '名称',
  `description` varchar(80) DEFAULT NULL COMMENT '描述',
  `img_url` varchar(80) DEFAULT NULL COMMENT '图片Url',
  `web_site` varchar(80) DEFAULT NULL COMMENT '品牌网址',
  `sort` int(11) DEFAULT NULL COMMENT '排序:最大最排前',
  `is_display` tinyint(1) DEFAULT NULL COMMENT '是否可见 1:可见 0:不可见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='品牌';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_buyer`
--

DROP TABLE IF EXISTS `bbs_buyer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_buyer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(18) NOT NULL COMMENT '用户名',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `gender` int(1) DEFAULT NULL COMMENT '性别:0:保密,1:男,2:女',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `real_name` varchar(8) DEFAULT NULL COMMENT '真实名字',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `province` varchar(11) DEFAULT NULL COMMENT '省ID',
  `city` varchar(11) DEFAULT NULL COMMENT '市ID',
  `town` varchar(11) DEFAULT NULL COMMENT '县ID',
  `addr` varchar(255) DEFAULT NULL COMMENT '地址',
  `is_del` tinyint(1) DEFAULT NULL COMMENT '是否已删除:1:未,0:删除了',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='购买者';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_color`
--

DROP TABLE IF EXISTS `bbs_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_color` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父ID为色系',
  `img_url` varchar(50) DEFAULT NULL COMMENT '颜色对应的衣服小图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COMMENT='颜色大全';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_detail`
--

DROP TABLE IF EXISTS `bbs_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品编号',
  `product_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `color` varchar(11) NOT NULL COMMENT ' 颜色名称',
  `size` varchar(11) NOT NULL COMMENT '尺码',
  `price` float(8,2) NOT NULL COMMENT '商品销售价',
  `amount` int(11) NOT NULL COMMENT '购买数量',
  PRIMARY KEY (`id`),
  KEY `fk_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COMMENT='订单详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_order`
--

DROP TABLE IF EXISTS `bbs_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID或订单号',
  `deliver_fee` float(8,2) NOT NULL COMMENT '运费',
  `total_price` float(8,2) NOT NULL COMMENT '应付金额',
  `order_price` float(8,2) NOT NULL COMMENT '订单金额',
  `payment_way` tinyint(1) NOT NULL COMMENT '支付方式 0:到付 1:在线 2:邮局 3:公司转帐',
  `payment_cash` tinyint(1) DEFAULT NULL COMMENT '货到付款方式.1现金,2POS刷卡',
  `delivery` tinyint(1) DEFAULT NULL COMMENT '送货时间',
  `is_confirm` tinyint(1) DEFAULT NULL COMMENT '是否电话确认 1:是  0: 否',
  `is_paiy` tinyint(1) NOT NULL COMMENT '支付状态 :0到付1待付款,2已付款,3待退款,4退款成功,5退款失败',
  `order_state` tinyint(1) NOT NULL COMMENT '订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货',
  `create_date` datetime NOT NULL COMMENT '订单生成时间',
  `note` varchar(100) DEFAULT NULL COMMENT '附言',
  `buyer_id` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`),
  KEY `buyer_id` (`buyer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COMMENT='订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_product`
--

DROP TABLE IF EXISTS `bbs_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID或商品编号',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `name` varchar(255) NOT NULL COMMENT '商品名称',
  `weight` float(8,2) DEFAULT NULL COMMENT '重量 单位:克',
  `is_new` tinyint(1) DEFAULT NULL COMMENT '是否新品:0:旧品,1:新品',
  `is_hot` tinyint(1) DEFAULT NULL COMMENT '是否热销:0,否 1:是',
  `is_commend` tinyint(1) DEFAULT NULL COMMENT '推荐 1推荐 0 不推荐',
  `is_show` tinyint(1) DEFAULT NULL COMMENT '上下架:0否 1是',
  `img_url` longtext COMMENT '商品图片集',
  `is_del` tinyint(1) DEFAULT NULL COMMENT '是否删除:0删除,1,没删除',
  `description` longtext COMMENT '商品描述',
  `package_list` longtext COMMENT '包装清单',
  `colors` varchar(255) DEFAULT NULL COMMENT '颜色集',
  `sizes` varchar(255) DEFAULT NULL COMMENT '尺寸集',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=442 DEFAULT CHARSET=utf8 COMMENT='商品';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_sku`
--

DROP TABLE IF EXISTS `bbs_sku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `color_id` bigint(20) DEFAULT NULL COMMENT '颜色ID',
  `size` varchar(5) DEFAULT NULL COMMENT '尺码',
  `market_price` float(8,2) DEFAULT NULL COMMENT '市场价',
  `price` float(8,2) NOT NULL COMMENT '售价',
  `delive_fee` float(8,2) DEFAULT NULL COMMENT '运费 默认10元',
  `stock` int(5) NOT NULL COMMENT '库存',
  `upper_limit` int(5) DEFAULT NULL COMMENT '购买限制',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `color_id` (`color_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6616 DEFAULT CHARSET=utf8 COMMENT='最小销售单元';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-13 11:02:52
