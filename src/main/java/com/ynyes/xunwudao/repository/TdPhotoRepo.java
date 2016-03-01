package com.ynyes.xunwudao.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdPhoto;

/**
 * TdEnterprise 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdPhotoRepo extends
		PagingAndSortingRepository<TdPhoto, Long>,
		JpaSpecificationExecutor<TdPhoto> 
{ 
	
	List<TdPhoto> findByUserId(Long userId, Sort sort);
    Page<TdPhoto> findByTitleContaining(String keywords, Pageable page);
    List<TdPhoto>findByStatusIdAndUserId(Long statusId, Long userId);

   

}