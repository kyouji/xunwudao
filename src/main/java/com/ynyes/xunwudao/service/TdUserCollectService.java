package com.ynyes.xunwudao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.xunwudao.entity.TdUserCollect;
import com.ynyes.xunwudao.repository.TdUserCollectRepo;

/**
 * TdUserCollect 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdUserCollectService {
    
    @Autowired
    TdUserCollectRepo repository;
    
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
    
    /**
     * 删除
     * 
     * @param e 菜单项
     */
    public void delete(TdUserCollect e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdUserCollect> entities)
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
    public TdUserCollect findOne(Long id)
    {
        if (null == id)
        {
            return null;
        }
        
        return repository.findOne(id);
    }
    
    /**
     * 查找
     * 
     * @param ids
     * @return
     */
    public TdUserCollect findByUserIdAndGoodsId(Long userId, Long goodsId)
    {
        if (null == userId || null == goodsId)
        {
            return null;
        }
        
        return repository.findByUserIdAndGoodsId(userId, goodsId);
    }
    
    public List<TdUserCollect> findAll(Iterable<Long> ids)
    {
        return (List<TdUserCollect>) repository.findAll(ids);
    }
    
    public List<TdUserCollect> findByUsername(String username)
    {
        return repository.findByUsername(username);
    }
    
    public List<TdUserCollect> findByUserId(Long userId){
    	return repository.findByUserIdOrderByIdDesc(userId);
    }
    
    public Long countByGoodsId(Long goodsId)
    {
        return repository.countByGoodsId(goodsId);
    }
    
    public Page<TdUserCollect> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return repository.findAll(pageRequest);
    }
    
    public Page<TdUserCollect> findByUserId(Long userId, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUserIdOrderByIdDesc(userId, pageRequest);
    }
    
    public Page<TdUserCollect> findByUsernameAndSearch(String username, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameAndGoodsTitleContainingOrderByIdDesc(username, keywords, pageRequest);
    }
    
    /**
     * @author libiao
     * 按收藏类型查找
     * 
     */
    public List<TdUserCollect> findByUsernameAndType(String username,Long type)
    {
    	if(null == username){
    		return null ;
    	}
    	return repository.findByUsernameAndTypeOrderByIdDesc(username, type);
    }
    
    public TdUserCollect findByUsernameAndCookId(String username,Long cookId)
    {
    	if(null ==username)
    	{
    		return null ;
    	}
    	return repository.findByUsernameAndCookId(username, cookId);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdUserCollect save(TdUserCollect e)
    {
        
        return repository.save(e);
    }
    
    public List<TdUserCollect> save(List<TdUserCollect> entities)
    {
        
        return (List<TdUserCollect>) repository.save(entities);
    }
}
