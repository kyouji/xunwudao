package com.ynyes.xunwudao.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdBillType;

/**
 * TdEnterprise 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdBillTypeRepo extends
		PagingAndSortingRepository<TdBillType, Long>,
		JpaSpecificationExecutor<TdBillType> 
{ 
    Page<TdBillType> findByTitleContainingOrderBySortIdAsc(String keywords, Pageable page);
    List<TdBillType>findByIsEnableTrueOrderBySortIdAsc();

   

}