package com.ynyes.xunwudao.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdArea;

/**
 * TdEnterprise 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdAreaRepo extends
		PagingAndSortingRepository<TdArea, Long>,
		JpaSpecificationExecutor<TdArea> 
{ 
    Page<TdArea> findByTitleContainingOrderBySortIdAsc(String keywords, Pageable page);
    List<TdArea>findByIsEnableTrueOrderBySortIdAsc();

   

}