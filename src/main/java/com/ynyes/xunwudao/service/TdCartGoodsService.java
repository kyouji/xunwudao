package com.ynyes.xunwudao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.xunwudao.entity.TdGoods;
import com.ynyes.xunwudao.service.TdGoodsService;
import com.ynyes.xunwudao.entity.TdCartGoods;
import com.ynyes.xunwudao.repository.TdCartGoodsRepo;

/**
 * TdCartGoods 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdCartGoodsService {
    
    @Autowired
    TdCartGoodsRepo repository;
    
    @Autowired
    TdGoodsService tdGoodsService;
   
    /*******功能部分*********/
    
    
    public List<TdCartGoods> updateGoodsInfo(List<TdCartGoods> cartGoodsList)
    {
        if (null == cartGoodsList || cartGoodsList.size() < 0)
        {
            return null;
        }
        
        for (TdCartGoods cartGoods : cartGoodsList)
        {
            if (null != cartGoods)
            {
                TdGoods goods = tdGoodsService.findOne(cartGoods.getGoodsId());
                
                if (null != goods)
                {
                    cartGoods.setGoodsCoverImageUri(goods.getCoverImageUri());
                    cartGoods.setGoodsTitle(goods.getTitle());
                    cartGoods.setPrice(goods.getSalePrice());
                }
            }
        }
        return cartGoodsList;
    }
    /*******功能部分*********/
    
    
    
    /*******信息查找部分*********/
    /**
     * 查找
     * 
     * @param ids
     * @return
     */
    public List<TdCartGoods> findAll(Iterable<Long> ids)
    {
        return (List<TdCartGoods>) repository.findAll(ids);
    }
    
    public List<TdCartGoods> findAll()
    {
        return (List<TdCartGoods>) repository.findAll();
    }
    
    public Page<TdCartGoods> findAllOrderByIdDesc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
        
        return repository.findAll(pageRequest);
    }
    
    public TdCartGoods findTopByGoodsIdAndPriceAndUsername(long goodsId, double price, String username)
    {
        return repository.findTopByGoodsIdAndPriceAndUsername(goodsId, price, username);
    }
    
    public List<TdCartGoods> findByGoodsIdAndPriceAndUsername(long goodsId, double price, String username)
    {
        return repository.findByGoodsIdAndPriceAndUsername(goodsId, price, username);
    }
    
    public List<TdCartGoods> findByGoodsIdAndUsername(Long goodsId, String username)
    {
        if (null == goodsId || null == username)
        {
            return null;
        }
        
        return repository.findByGoodsIdAndUsername(goodsId, username);
    }
    
    public List<TdCartGoods> findByGoodsIdAndUserId(Long goodsId, Long userId)
    {
        if (null == goodsId || null == userId)
        {
            return null;
        }
        
        return repository.findByGoodsIdAndUserId(goodsId, userId);
    }
    
    public List<TdCartGoods> findByUsername(String username)
    {
        return repository.findByUsername(username);
    }
    
    public List<TdCartGoods> findByUserId(Long userId)
    {
        return repository.findByUserId(userId);
    }
    
    public List<TdCartGoods> findByUsernameAndIsSelectedTrue(String username)
    {
        return repository.findByUsernameAndIsSelectedTrue(username);
    }
    
    public List<TdCartGoods> findByUserIdAndIsSelectedTrue(Long userId)
    {
        return repository.findByUserIdAndIsSelectedTrue(userId);
    }
    
    public List<TdCartGoods> findByisLoggedInFalse(){
    	return repository.findByIsLoggedInFalse();
    }
    
    //收藏直接购买
    public List<TdCartGoods> findByUsernameAndIsCollectedTrue(String username){
    	return repository.findByUsernameAndIsCollectedTrue(username);
    }
    
    public List<TdCartGoods> findByUserIdAndIsCollectedTrue(Long userId){
    	return repository.findByUserIdAndIsCollectedTrue(userId);
    }
      
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdCartGoods save(TdCartGoods e)
    {
        
        return repository.save(e);
    }
    
    public List<TdCartGoods> save(List<TdCartGoods> entities)
    {
        
        return (List<TdCartGoods>) repository.save(entities);
    }
    
    /**
     * 删除
     * 
     * @param id 菜单项ID
     */
    public void delete(Long id)
    {
        if (null != id)
        {
            repository.delete(id);
        }
    }
    
    public void delete(Iterable<TdCartGoods> entities)
    {
        repository.delete(entities);
    }
    
    /**
     * 删除
     * 
     * @param e 菜单项
     */
    public void delete(TdCartGoods e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdCartGoods> entities)
    {
        if (null != entities)
        {
            repository.delete(entities);
        }
    }
    
    /**
     * 查找
     * 
     * @param id ID
     * @return
     */
    public TdCartGoods findOne(Long id)
    {
        if (null == id)
        {
            return null;
        }
        
        return repository.findOne(id);
    }
}
