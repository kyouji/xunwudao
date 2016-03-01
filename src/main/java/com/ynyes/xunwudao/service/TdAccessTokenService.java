package com.ynyes.xunwudao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.xunwudao.entity.TdAccessToken;
import com.ynyes.xunwudao.repository.TdAccessTokenRepo;

@Service
@Transactional
public class TdAccessTokenService {
	
	@Autowired
	TdAccessTokenRepo repository;
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
    public void delete(TdAccessToken e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdAccessToken> entities)
    {
        if (null != entities)
        {
            repository.delete(entities);
        }
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdAccessToken save(TdAccessToken e)
    {       
        return repository.save(e);
    }
    
    public List<TdAccessToken> save(List<TdAccessToken> entities)
    {
        
        return (List<TdAccessToken>) repository.save(entities);
    }
    
    //查询
    public TdAccessToken findTopBy(){

    	return repository.findTopBy();
    	
    }
    
    
    public List<TdAccessToken> findAll(){
    	return (List<TdAccessToken>) repository.findAll();
    }
}
