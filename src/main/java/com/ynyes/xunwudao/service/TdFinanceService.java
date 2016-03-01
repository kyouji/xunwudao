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

import com.ynyes.xunwudao.entity.TdFinance;
import com.ynyes.xunwudao.repository.TdFinanceRepo;

/**
 * TdMallService 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdFinanceService {
    @Autowired
    TdFinanceRepo repository;
    
    @Autowired
    TdStockService tdStockService;
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
    public void delete(TdFinance e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdFinance> entities)
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
    public TdFinance findOne(Long id)
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
    public List<TdFinance> findAll(Iterable<Long> ids)
    {
        return (List<TdFinance>) repository.findAll(ids);
    }
    
    public List<TdFinance> findAllOrderBySortIdAsc()
    {
        Sort sort = new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time"));
        
        return (List<TdFinance>) repository.findAll(sort);
    }
    
    /**
     * 根据username查找所有【票据整理】信息
     * @return
     */
    public List<TdFinance> findByUserId(Long userId)
    {
        Sort sort = new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time"));
        
        return (List<TdFinance>) repository.findByUserId(userId, sort);
    }
    
    //根据时间查找？
    public List<TdFinance> findByTime(Date time)
    {
        return repository.findByTime(time);
    }
    
    public TdFinance findByUserIdAndTime(Long userId, Date time)
    {
        
        return repository.findByUserIdAndTime(userId, time);
    }
    
    
    public Page<TdFinance> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return repository.findAll(pageRequest);
    }
    
    public Page<TdFinance> findByTime(Date time, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId").and(new Sort(Direction.ASC, "userId")));
        
        return repository.findByTime(time, pageRequest);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdFinance save(TdFinance e)
    {
		// 保存赠品
		tdStockService.save(e.getStockList());
        return repository.save(e);
    }
    
    public List<TdFinance> save(List<TdFinance> entities)
    {
        
        return (List<TdFinance>) repository.save(entities);
    }
    
    public List<TdFinance> findAll(){
    	return (List<TdFinance>) repository.findAll();
    }

}
