package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.PmsProductImageMaper;
import com.atguigu.gmall.manage.mapper.PmsProductInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductImageMaper pmsProductImageMaper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo info = new PmsProductInfo();
        info.setCatalog3Id(catalog3Id);
        return pmsProductInfoMapper.select(info);
    }

    @Override
    public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
        // 保存商品信息
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        // 拿到商品主键
        String productId = pmsProductInfo.getId();
        // 保存商品图片信息
        List<PmsProductImage> images = pmsProductInfo.getSpuImageList();
        for (PmsProductImage image : images) {
            image.setProductId(productId);
            pmsProductImageMaper.insertSelective(image);
        }

        // 保存销售属性信息
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
            pmsProductSaleAttr.setProductId(productId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);

            // 保存销售属性值
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(productId);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }
        return "success";
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr saleAttr = new PmsProductSaleAttr();
        saleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> saleAttrs = pmsProductSaleAttrMapper.select(saleAttr);
        for (PmsProductSaleAttr attr : saleAttrs) {
            PmsProductSaleAttrValue target = new PmsProductSaleAttrValue();
            target.setProductId(spuId);
            target.setSaleAttrId(attr.getSaleAttrId());
            List<PmsProductSaleAttrValue> list = pmsProductSaleAttrValueMapper.select(target);
            attr.setSpuSaleAttrValueList(list);
        }
        return saleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage image = new PmsProductImage();
        image.setProductId(spuId);
        return pmsProductImageMaper.select(image);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId, String skuId) {
 /*       PmsProductSaleAttr saleAttr = new PmsProductSaleAttr();
        saleAttr.setProductId(productId);
        List<PmsProductSaleAttr> searchOut = pmsProductSaleAttrMapper.select(saleAttr);
        for (PmsProductSaleAttr pmsProductSaleAttr : searchOut) {
            String saleAttrId = pmsProductSaleAttr.getSaleAttrId();

            PmsProductSaleAttrValue s = new PmsProductSaleAttrValue();
            s.setSaleAttrId(saleAttrId);
            s.setProductId(productId);
            List<PmsProductSaleAttrValue> list = pmsProductSaleAttrValueMapper.select(s);
            pmsProductSaleAttr.setSpuSaleAttrValueList(list);
        }
        return searchOut;*/
        return pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId, skuId);
    }

}
