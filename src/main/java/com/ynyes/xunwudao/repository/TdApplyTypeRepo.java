package com.ynyes.xunwudao.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdApplyType;

/**
 * TdEnterprise 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdApplyTypeRepo extends
		PagingAndSortingRepository<TdApplyType, Long>,
		JpaSpecificationExecutor<TdApplyType> 
{ 
    Page<TdApplyType> findByTitleContainingOrderBySortIdAsc(String keywords, Pageable page);
    List<TdApplyType>findByIsEnableTrueOrderBySortIdAsc();

   

}