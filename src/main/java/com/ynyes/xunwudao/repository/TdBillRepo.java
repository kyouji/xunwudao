package com.ynyes.xunwudao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdBill;

/**
 * TdEnterprise 实体数据库操作接口
 * 
 * @author Sharon
 *
 */

public interface TdBillRepo extends
		PagingAndSortingRepository<TdBill, Long>,
		JpaSpecificationExecutor<TdBill> 
{ 
	
	List<TdBill>findByUserId(Long userId, Sort sort);
    List<TdBill>findByStatusIdAndUserId(Long statusId, Long userId, Sort sort);
    List<TdBill>findByStatusIdOrderByTimeDesc(Long statusId);
    List<TdBill>findByStatusIdAndBillTypeIdOrderByTimeDesc(Long statusId, Long billTypeId);
    
    Page<TdBill> findByTitleContainingOrderByIdAsc(String  keywords, Pageable page);
    
  //001
  	Page<TdBill>findByStatusId(Long StatusId, Pageable pageable);
  	//0010
  	Page<TdBill>findByTimeBefore(Date date2, Pageable pageable);
  	//0011
  	Page<TdBill>findByTimeBeforeAndStatusId(Date date2, Long StatusId, Pageable pageable);
  	//0100
  	Page<TdBill>findByTimeAfter(Date date2, Pageable pageable);
  	//0101
  	Page<TdBill>findByTimeAfterAndStatusId(Date date2, Long StatusId, Pageable pageable);
  	//0110
  	Page<TdBill>findByTimeAfterAndTimeBefore(Date date1, Date date2,  Pageable pageable);
  	//0111
  	Page<TdBill>findByTimeAfterAndTimeBeforeAndStatusId(Date date1, Date date2, Long StatusId,  Pageable pageable);
  	//1000
  	Page<TdBill>findByBillTypeId(Long billTypeId, Pageable pageable);
  	//1001
  	Page<TdBill>findByStatusIdAndBillTypeId(Long StatusId,  Long billTypeId, Pageable pageable);
  	//1010
  	Page<TdBill>findByTimeBeforeAndBillTypeId(Date date2, Long billTypeId, Pageable pageable);
  	//1011
  	Page<TdBill>findByTimeBeforeAndStatusIdAndBillTypeId(Date date2, Long StatusId, Long billTypeId, Pageable pageable);
  	//1100
  	Page<TdBill>findByTimeAfterAndBillTypeId(Date date1, Long billTypeId, Pageable pageable);
  	//1101
  	Page<TdBill>findByTimeAfterAndStatusIdAndBillTypeId(Date date1, Long StatusId, Long billTypeId, Pageable pageable);
  	//1110
  	Page<TdBill>findByTimeAfterAndTimeBeforeAndBillTypeId(Date date1, Date date2, Long billTypeId, Pageable pageable);
  	//1111
  	Page<TdBill>findByTimeAfterAndTimeBeforeAndStatusIdAndBillTypeId(Date date1, Date date2, Long StatusId, Long billTypeId, Pageable pageable);
  	
  	//指定用户的票据页面
  	Page<TdBill>findByUserId(Long userId, Pageable pageable);
  	Page<TdBill>findByStatusIdAndUserId(Long statusId, Long userId, Pageable pageable);
  	
  	Page<TdBill>findByTimeBeforeAndUserId(Date date2, Long userId, Pageable pageable);
  	Page<TdBill>findByTimeBeforeAndStatusIdAndUserId(Date date2, Long statusId, Long userId, Pageable pageable);
  	Page<TdBill>findByTimeAfterAndUserId(Date date1, Long userId, Pageable pageable);
  	Page<TdBill>findByTimeAfterAndStatusIdAndUserId(Date date1,Long statusId, Long userId, Pageable pageable);
  	Page<TdBill>findByTimeAfterAndTimeBeforeAndUserId(Date date1,Date date2,Long userId, Pageable pageable);
  	Page<TdBill>findByTimeAfterAndTimeBeforeAndStatusIdAndUserId(Date date1,Date date2,Long statusId, Long userId, Pageable pageable);

}