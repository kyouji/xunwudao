package com.ynyes.xunwudao.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.xunwudao.entity.TdBill;
import com.ynyes.xunwudao.repository.TdBillRepo;

/**
 * TdMallService 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdBillService {
    @Autowired
    TdBillRepo repository;
    
    Sort sort = new Sort(Direction.DESC, "time");
    
    /**
     * 删除
     * 
     * @param id 菜单项ID
     */
    public void delete(Long id)
    {
        if (null != id)
        {
            repository.delete(id);
        }
    }
    
    /**
     * 删除
     * 
     * @param e 菜单项
     */
    public void delete(TdBill e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdBill> entities)
    {
        if (null != entities)
        {
            repository.delete(entities);
        }
    }
    
    /**
     * 查找
     * 
     * @param id ID
     * @return
     */
    public TdBill findOne(Long id)
    {
        if (null == id)
        {
            return null;
        }
        
        return repository.findOne(id);
    }
    
    /**
     * 根据后台排序查找
     * @author Zhangji
     */
    public List<TdBill> findByUserId(Long userId)
    {
    	Sort sort = new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"));
        return repository.findByUserId( userId, sort);
    }
     
    /**
     * 查找
     * 
     * @param ids
     * @return
     */
    public List<TdBill> findAll(Iterable<Long> ids)
    {
        return (List<TdBill>) repository.findAll(ids);
    }
    
    public List<TdBill> findAllOrderBySortIdAsc()
    {
        Sort sort = new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"));
        
        return (List<TdBill>) repository.findAll(sort);
    }
    
    public List<TdBill> findByStatusIdAndUserId(Long statusId , Long userId)
    {
        return repository.findByStatusIdAndUserId(statusId, userId, sort);
    }
    
    
    public Page<TdBill> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time"));
        
        return repository.findAll(pageRequest);
    }
    
    public Page<TdBill> searchAllOrderBySortIdAsc(String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByTitleContainingOrderByIdAsc(keywords, pageRequest);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdBill save(TdBill e)
    {
        
        return repository.save(e);
    }
    
    public List<TdBill> save(List<TdBill> entities)
    {
        
        return (List<TdBill>) repository.save(entities);
    }
    
    public List<TdBill> findAll(){
    	return (List<TdBill>) repository.findAll();
    }
    
	public List<TdBill> findByStatusIdOrderByTimeDesc(Long StatusId){
		return (List<TdBill>) repository.findByStatusIdOrderByTimeDesc( StatusId);
	}
	
	public List<TdBill> findByStatusIdAndBillTypeIdOrderByTimeDesc(Long statusId, Long billTypeId){
		return (List<TdBill>) repository.findByStatusIdAndBillTypeIdOrderByTimeDesc(statusId, billTypeId);
	}
    
    
    /*-=====================================
	++++++++++++++++筛选++++++++             +++++++
	================       ========zhangji=======
	=======================================
	   代号：【keywords】【date1】【date2】【StatusId】
	======================================*/
	//0000
	public Page<TdBill> findAll( int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findAll(  pageRequest);
	}
    
    
	//0001
	public Page<TdBill> findByStatusId(Long StatusId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByStatusId( StatusId, pageRequest);
	}
	
	//0010
	public Page<TdBill> findByTimeBefore(Date date2, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeBefore( date2, pageRequest);
	}
	
	//0011
	public Page<TdBill> findByTimeBeforeAndStatusId(Date date2, Long StatusId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeBeforeAndStatusId( date2, StatusId, pageRequest);
	}
	
	//0100
	public Page<TdBill> findByTimeAfter(Date date1, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfter( date1, pageRequest);
	}
	
	//0101
	public Page<TdBill> findByTimeAfterAndStatusId(Date date1, Long StatusId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfterAndStatusId(date1, StatusId, pageRequest);
	}
	
	//0110
	public Page<TdBill> findByTimeAfterAndTimeBefore(Date date1, Date date2,  int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfterAndTimeBefore( date1,date2, pageRequest);
	}
	
	//0111
	public Page<TdBill> findByTimeAfterAndTimeBeforeAndStatusId(Date date1, Date date2, Long StatusId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfterAndTimeBeforeAndStatusId(date1,  date2,  StatusId, pageRequest);
	}
	
	//1000
	public Page<TdBill> findBySearch(Long billTypeId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByBillTypeId( billTypeId, pageRequest);
	}
	
	//1001
	public Page<TdBill> findByStatusIdAndSearch(Long StatusId, Long billTypeId,  int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByStatusIdAndBillTypeId( StatusId, billTypeId, pageRequest);
	}
	
	//1010
	public Page<TdBill> findByTimeBeforeAndSearch(Date date2, Long billTypeId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeBeforeAndBillTypeId( date2, billTypeId, pageRequest);
	}
	
	//1011
	public Page<TdBill> findByTimeBeforeAndStatusIdAndSearch(Date date2, Long StatusId, Long billTypeId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeBeforeAndStatusIdAndBillTypeId( date2, StatusId, billTypeId, pageRequest);
	}
	
	//1100
	public Page<TdBill> findByTimeAfterAndSearch(Date date1, Long billTypeId,  int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfterAndBillTypeId( date1, billTypeId, pageRequest);
	}
	
	//1101
	public Page<TdBill> findByTimeAfterAndStatusIdAndSearch(Date date1, Long StatusId, Long billTypeId,  int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfterAndStatusIdAndBillTypeId( date1, StatusId, billTypeId, pageRequest);
	}
	
	//1110
	public Page<TdBill> findByTimeAfterAndTimeBeforeAndSearch(Date date1, Date date2, Long billTypeId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfterAndTimeBeforeAndBillTypeId( date1, date2, billTypeId, pageRequest);
	}
	
	//1111
	public Page<TdBill> findByTimeAfterAndTimeBeforeAndStatusIdAndSearch(Date date1, Date date2, Long StatusId, Long billTypeId,int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfterAndTimeBeforeAndStatusIdAndBillTypeId(date1, date2, StatusId, billTypeId, pageRequest);
	}
	
	
	//意外情况 指定用户的票据筛选
	public Page<TdBill> findByUserId(Long userId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByUserId( userId, pageRequest);
	}
	
	public Page<TdBill> findByStatusIdAndUserId(Long statusId, Long userId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByStatusIdAndUserId(statusId,  userId, pageRequest);
	}
	
	public Page<TdBill> findByTimeBeforeAndUserId(Date date2, Long userId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeBeforeAndUserId(date2,  userId, pageRequest);
	}
	
	public Page<TdBill> findByTimeBeforeAndStatusIdAndUserId(Date date2, Long statusId, Long userId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeBeforeAndStatusIdAndUserId(date2, statusId, userId, pageRequest);
	}
	
	public Page<TdBill> findByTimeAfterAndUserId(Date date1, Long userId, int page , int size){
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
				.and(new Sort(Direction.ASC,"id")));
		return  repository.findByTimeAfterAndUserId(date1, userId, pageRequest);
	}
		public Page<TdBill> findByTimeAfterAndStatusIdAndUserId(Date date1, Long statusId, Long userId, int page , int size){
			PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
					.and(new Sort(Direction.ASC,"id")));
			return  repository.findByTimeAfterAndStatusIdAndUserId(date1, statusId, userId, pageRequest);
	}
		
		public Page<TdBill> findByTimeAfterAndTimeBeforeAndUserId(Date date1, Date date2, Long userId, int page , int size){
			PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
					.and(new Sort(Direction.ASC,"id")));
			return  repository.findByTimeAfterAndTimeBeforeAndUserId(date1, date2, userId, pageRequest);
	}
		
		public Page<TdBill> findByTimeAfterAndTimeBeforeAndStatusIdAndUserId(Date date1, Date date2, Long statusId, Long userId, int page , int size){
			PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "time").and(new Sort(Direction.DESC, "finishTime"))
					.and(new Sort(Direction.ASC,"id")));
			return  repository.findByTimeAfterAndTimeBeforeAndStatusIdAndUserId(date1, date2, statusId,userId, pageRequest);
	}
}
