package com.ynyes.xunwudao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.xunwudao.entity.TdPhoto;
import com.ynyes.xunwudao.repository.TdPhotoRepo;

/**
 * TdMallService 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdPhotoService {
    @Autowired
    TdPhotoRepo repository;
    
    
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
    public void delete(TdPhoto e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdPhoto> entities)
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
    public TdPhoto findOne(Long id)
    {
        if (null == id)
        {
            return null;
        }
        
        return repository.findOne(id);
    }
    
    /**
     * 根据后台排序查找
     * @author Zhangji
     */
    public List<TdPhoto> findByUserId(Long userId)
    {
    	Sort sort = new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time"));
        return repository.findByUserId( userId, sort);
    }
    
    /**
     * 查找
     * 
     * @param ids
     * @return
     */
    public List<TdPhoto> findAll(Iterable<Long> ids)
    {
        return (List<TdPhoto>) repository.findAll(ids);
    }
    
    public List<TdPhoto> findAllOrderBySortIdAsc()
    {
        Sort sort = new Sort(Direction.ASC, "sortId").and(new Sort(Direction.DESC, "time"));
        
        return (List<TdPhoto>) repository.findAll(sort);
    }
    
    public List<TdPhoto> findByStatusIdAndUserId(Long statusId , Long userId)
    {
        return repository.findByStatusIdAndUserId(statusId, userId);
    }
    
    
    public Page<TdPhoto> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return repository.findAll(pageRequest);
    }
    
    public Page<TdPhoto> searchAllOrderBySortIdAsc(String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByTitleContaining(keywords, pageRequest);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdPhoto save(TdPhoto e)
    {
        
        return repository.save(e);
    }
    
    public List<TdPhoto> save(List<TdPhoto> entities)
    {
        
        return (List<TdPhoto>) repository.save(entities);
    }
    
    public List<TdPhoto> findAll(){
    	return (List<TdPhoto>) repository.findAll();
    }
}
