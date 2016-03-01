package com.ynyes.xunwudao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.xunwudao.entity.TdApply;
import com.ynyes.xunwudao.repository.TdApplyRepo;

/**
 * TdMallService 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdApplyService {
    @Autowired
    TdApplyRepo repository;
    
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
    public void delete(TdApply e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdApply> entities)
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
    public TdApply findOne(Long id)
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
    public List<TdApply> findAll(Iterable<Long> ids)
    {
        return (List<TdApply>) repository.findAll(ids);
    }
    
    public List<TdApply> findAllOrderBySortIdAsc()
    {
        Sort sort = new Sort(Direction.ASC, "sortId");
        
        return (List<TdApply>) repository.findAll(sort);
    }
    
    /**
     * 查找未审核的业务（提示一下，防止重复提交）
     */
    public List<TdApply> findByUserIdAndStatusIdAndApplyTypeId(Long userId, Long statuId , Long applyTypeId)
    {
        return (List<TdApply>) repository.findByUserIdAndStatusIdAndApplyTypeId(userId, statuId, applyTypeId);
    }
    
    //后台查找未解决表单数量
    public List<TdApply> findByStatusId( Long statuId)
    {
        return (List<TdApply>) repository.findByStatusId(statuId);
    }
    
    public Page<TdApply> findAllOrderBySortIdAsc(int page, int size)
    {
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return (Page<TdApply>) repository.findAll(pageRequest);
    }
    
    public Page<TdApply> findAllOrderByTimeDesc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time"));
        
        return repository.findAll(pageRequest);
    }
    
    public Page<TdApply> findByApplyTypeIdOrderBySortIdAsc(Long applyTypeId, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time")));
        
        return repository.findByApplyTypeIdOrderBySortIdAsc(applyTypeId, pageRequest);
    }
    
    public Page<TdApply> findByStatusIdOrderBySortIdAsc(Long statusId, int page, int size)
    {
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time")));
    	
    	return repository.findByStatusIdOrderBySortIdAsc(statusId, pageRequest);
    }
    
    public Page<TdApply> findByApplyTypeIdAndStatusIdOrderBySortIdAsc(Long applyTypeId, Long statusId, int page, int size)
    {
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time")));
    	
    	return repository.findByApplyTypeIdAndStatusIdOrderBySortIdAsc(applyTypeId, statusId, pageRequest);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdApply save(TdApply e)
    {
        
        return repository.save(e);
    }
    
    public List<TdApply> save(List<TdApply> entities)
    {
        
        return (List<TdApply>) repository.save(entities);
    }
    
    public List<TdApply> findAll(){
    	return (List<TdApply>) repository.findAll();
    }
}
