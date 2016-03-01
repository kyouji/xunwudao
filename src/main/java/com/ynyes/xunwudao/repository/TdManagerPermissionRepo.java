package com.ynyes.xunwudao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdManagerPermission;

/**
 * TdManagerPermission 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdManagerPermissionRepo extends
		PagingAndSortingRepository<TdManagerPermission, Long>,
		JpaSpecificationExecutor<TdManagerPermission> 
{

}
