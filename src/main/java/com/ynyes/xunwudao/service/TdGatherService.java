package com.ynyes.xunwudao.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.xunwudao.entity.TdGather;
import com.ynyes.xunwudao.repository.TdGatherRepo;

/**
 * TdMallService 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdGatherService {
    @Autowired
    TdGatherRepo repository;
    
    
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
    public void delete(TdGather e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdGather> entities)
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
    public TdGather findOne(Long id)
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
    public List<TdGather> findAll(Iterable<Long> ids)
    {
        return (List<TdGather>) repository.findAll(ids);
    }
    
    public List<TdGather> findAllOrderBySortIdAsc()
    {
        Sort sort = new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time"));
        
        return (List<TdGather>) repository.findAll(sort);
    }
    
    /**
     * 根据username查找所有【票据整理】信息
     * @return
     */
    public List<TdGather> findByUserId(Long userId)
    {
        Sort sort = new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time"));
        
        return (List<TdGather>) repository.findByUserId(userId, sort);
    }
    
    //根据时间查找？
    public List<TdGather> findByTime(Date time)
    {
        return repository.findByTime(time);
    }
    
    public TdGather findByUserIdAndTime(Long userId, Date time)
    {
        
        return repository.findByUserIdAndTime(userId, time);
    }
    
    
    public Page<TdGather> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return repository.findAll(pageRequest);
    }
    
    /**
     * 根据时间  	
     * 【用途】后台票据管理导入
     */
    public Page<TdGather> findByTime(Date time, int page, int size)
    {
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC,"id")));
        return repository.findByTime(time , pageRequest);
    }
    
    public Page<TdGather> searchAllOrderBySortIdAsc(String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByTitleContainingOrderBySortIdAsc(keywords, pageRequest);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdGather save(TdGather e)
    {
        
        return repository.save(e);
    }
    
    public List<TdGather> save(List<TdGather> entities)
    {
        
        return (List<TdGather>) repository.save(entities);
    }
    
    public List<TdGather> findAll(){
    	return (List<TdGather>) repository.findAll();
    }

}
