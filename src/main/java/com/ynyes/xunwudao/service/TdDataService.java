package com.ynyes.xunwudao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.xunwudao.entity.TdData;
import com.ynyes.xunwudao.repository.TdDataRepo;

/**
 * TdMallService 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdDataService {
    @Autowired
    TdDataRepo repository;
    
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
    public void delete(TdData e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdData> entities)
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
    public TdData findOne(Long id)
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
    public List<TdData> findAll(Iterable<Long> ids)
    {
        return (List<TdData>) repository.findAll(ids);
    }
    
    public List<TdData> findAllOrderBySortIdAsc()
    {
        Sort sort = new Sort(Direction.ASC, "sortId");
        
        return (List<TdData>) repository.findAll(sort);
    }
    
    public List<TdData> findByIsEnableTrueOrderBySortIdAsc()
    {
        return repository.findByIsEnableTrueOrderBySortIdAsc();
    }
    
    public List<TdData> findByUsernameAndDataTypeIdOrderBySortIdAsc(String username, Long dataTypeId)
    {
        return repository.findByUsernameAndDataTypeIdOrderBySortIdAsc(username, dataTypeId);
    }
    
    public Page<TdData> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return repository.findAll(pageRequest);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdData save(TdData e)
    {
        
        return repository.save(e);
    }
    
    public List<TdData> save(List<TdData> entities)
    {
        
        return (List<TdData>) repository.save(entities);
    }
    
    public List<TdData> findAll(){
    	return (List<TdData>) repository.findAll();
    }
}
