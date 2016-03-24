package com.ynyes.xunwudao.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdUserCollect;

/**
 * TdUserCollect 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdUserCollectRepo extends
		PagingAndSortingRepository<TdUserCollect, Long>,
		JpaSpecificationExecutor<TdUserCollect> 
{
    Page<TdUserCollect> findByUserIdOrderByIdDesc(Long userId, Pageable page);
    
    Page<TdUserCollect> findByUsernameAndGoodsTitleContainingOrderByIdDesc(String username, String keywords, Pageable page);
    
    List<TdUserCollect> findByUsername(String username);
    
    List<TdUserCollect> findByUserIdOrderByIdDesc(Long userId);
    
    TdUserCollect findByUserIdAndGoodsId(Long userId, Long goodsId);
    
    Long countByGoodsId(Long goodsId);
    
    // add to libiao
    List<TdUserCollect> findByUsernameAndTypeOrderByIdDesc(String username,Long type);
    
    TdUserCollect findByUsernameAndCookId(String username,Long cookId);
}
