package com.ynyes.xunwudao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdGather;

/**
 * TdEnterprise 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdGatherRepo extends
		PagingAndSortingRepository<TdGather, Long>,
		JpaSpecificationExecutor<TdGather> 
{ 
	 TdGather findByUserIdAndTime(Long userId, Date time);
	 
    Page<TdGather> findByTitleContainingOrderBySortIdAsc(String keywords, Pageable page);
    Page<TdGather> findByTime(Date time, Pageable page);
    
    List<TdGather> findByUserId(Long userId, Sort sort);
    List<TdGather> findByTime(Date time);
}