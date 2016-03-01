package com.ynyes.xunwudao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdFinance;

/**
 * TdEnterprise 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdFinanceRepo extends
		PagingAndSortingRepository<TdFinance, Long>,
		JpaSpecificationExecutor<TdFinance> 
{ 
	 TdFinance findByUserIdAndTime(Long userId, Date time);
	 
    List<TdFinance> findByUserId(Long userId, Sort sort);
    List<TdFinance> findByTime(Date time);
    
    Page<TdFinance> findByTime(Date time, Pageable pageable);
}