package com.ynyes.xunwudao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.xunwudao.entity.TdPayRecord;

public interface TdPayRecordRepo extends
        PagingAndSortingRepository<TdPayRecord, Long>,
        JpaSpecificationExecutor<TdPayRecord> 
{
    List<TdPayRecord> findByOrderIdOrderByCreateTimeDesc(Long orderId);
}
