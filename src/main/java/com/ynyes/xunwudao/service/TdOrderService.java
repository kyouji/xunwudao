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

import com.ynyes.xunwudao.entity.TdOrder;
import com.ynyes.xunwudao.repository.TdOrderRepo;

/**
 * TdOrder 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdOrderService {
    @Autowired
    TdOrderRepo repository;
    
    public List<TdOrder> findByUsernameAndGoodsId(String username, Long goodsId)
    {
        if (null == username || null == goodsId)
        {
            return null;
        }
        
        return repository.findByUsernameAndGoodsId(username, goodsId);
    }
    
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
    public void delete(TdOrder e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdOrder> entities)
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
    public TdOrder findOne(Long id)
    {
        if (null == id)
        {
            return null;
        }
        
        return repository.findOne(id);
    }
    
    /**
     * 查找
     * 
     * @param ids
     * @return
     */
    public List<TdOrder> findAll(Iterable<Long> ids)
    {
        return (List<TdOrder>) repository.findAll(ids);
    }
    
    public List<TdOrder> findAll()
    {
        return (List<TdOrder>) repository.findAll();
    }
    
    public Page<TdOrder> findAll(int page, int size)
    {
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        return (Page<TdOrder>) repository.findAll(pageRequest);
    }
    
    public Page<TdOrder> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return repository.findAll(pageRequest);
    }
    
    public Page<TdOrder> findAllOrderByIdDesc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
        
        return repository.findAll(pageRequest);
    }
    
    public Page<TdOrder> findByStatusIdOrderByIdDesc(long statusId, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByStatusIdOrderByIdDesc(statusId, pageRequest);
    }
    
    //xunwudao
    public List<TdOrder> findByUserIdOrderByOrderTimeDesc(Long userId)
    {
        
        return repository.findByUserIdOrderByOrderTimeDesc(userId);
    }
    
    public Page<TdOrder> findByUsername(String username, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameOrderByIdDesc(username, pageRequest);
    }
    
    public TdOrder findByOrderNumber(String orderNumber)
    {
        return repository.findByOrderNumber(orderNumber);
    }
    
    public Page<TdOrder> findByUsernameAndTimeAfter(String username, Date time, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameAndOrderTimeAfterOrderByIdDesc(username, time, pageRequest);
    }
    
    public Page<TdOrder> findByUsernameAndTimeAfterAndSearch(String username, Date time, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameAndOrderTimeAfterAndOrderNumberContainingOrderByIdDesc(username, time, keywords, pageRequest);
    }
    
    public Page<TdOrder> findByUsernameAndSearch(String username, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameAndOrderNumberContainingOrderByIdDesc(username, keywords, pageRequest);
    }
    
    public Page<TdOrder> findByUsernameAndStatusId(String username, long statusId, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameAndStatusIdOrderByIdDesc(username, statusId, pageRequest);
    }
    
    public Page<TdOrder> findByUsernameAndStatusIdAndSearch(String username, long statusId, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameAndStatusIdAndOrderNumberContainingOrderByIdDesc(username, statusId, keywords, pageRequest);
    }
    
    public Page<TdOrder> findByUsernameAndStatusIdAndTimeAfter(String username, long statusId, Date time, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameAndStatusIdAndOrderTimeAfterOrderByIdDesc(username, statusId, time, pageRequest);
    }
    
    public Page<TdOrder> findByUsernameAndStatusIdAndTimeAfterAndSearch(String username, long statusId, Date time, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByUsernameAndStatusIdAndOrderTimeAfterAndOrderNumberContainingOrderByIdDesc(username, statusId, time, keywords, pageRequest);
    }
    
    public Long countByUsernameAndStatusId(String username, long statusId)
    {
        return repository.countByUsernameAndStatusId(username, statusId);
    }
    
    public Long countByUsername(String username)
    {
        return repository.countByUsername(username);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdOrder save(TdOrder e)
    {
        
        return repository.save(e);
    }
    
    public List<TdOrder> save(List<TdOrder> entities)
    {
        
        return (List<TdOrder>) repository.save(entities);
    }
    /**
	 * @author lichong
	 * @注释：
	 */
    public Page<TdOrder> findByDiysitename(String diysitename, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByshopTitleOrderByIdDesc(diysitename, pageRequest);
    }
    public Page<TdOrder> findByDiysitenameAndSearch(String diysitename, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByshopTitleAndOrderNumberContainingOrderByIdDesc(diysitename, keywords, pageRequest);
    }
    public Page<TdOrder> findByDiysitenameAndStatusIdAndSearch(String diysitename, long statusId, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByshopTitleAndStatusIdAndOrderNumberContainingOrderByIdDesc(diysitename, statusId, keywords, pageRequest);
    }
    public Page<TdOrder> findByDiysitenameAndStatusId(String diysitename, long statusId, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByshopTitleAndStatusIdOrderByIdDesc(diysitename, statusId, pageRequest);
    }
    public Page<TdOrder> findByDiysitenameAndTimeAfter(String diysitename, Date time, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByshopTitleAndOrderTimeAfterOrderByIdDesc(diysitename, time, pageRequest);
    }
    public Page<TdOrder> findByDiysitenameAndTimeAfterAndSearch(String diysitename, Date time, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByshopTitleAndOrderTimeAfterAndOrderNumberContainingOrderByIdDesc(diysitename, time, keywords, pageRequest);
    }
    public Page<TdOrder> findByDiysitenameAndStatusIdAndTimeAfterAndSearch(String diysitename, long statusId, Date time, String keywords, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByshopTitleAndStatusIdAndOrderTimeAfterAndOrderNumberContainingOrderByIdDesc(diysitename, statusId, time, keywords, pageRequest);
    }
    public Page<TdOrder> findByDiysitenameAndStatusIdAndTimeAfter(String diysitename, long statusId, Date time, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByshopTitleAndStatusIdAndOrderTimeAfterOrderByIdDesc(diysitename, statusId, time, pageRequest);
    }
    
    /**
	 * @author lc
	 * @注释：线下同盟店信息
	 */
    public List<TdOrder> findByshopIdAndstatusId(long shopId, long statusId){
    	return repository.findByShopIdAndStatusIdOrderByIdDesc(shopId, statusId);
    }
    /**
	 * @author lc
	 * @注释：订单收入查询
	 */
    public List<TdOrder> findAllVerifyBelongShopTitle(String diysitename){
    	return repository.findByStatusIdAndShopTitleOrStatusIdAndShopTitle(5L, diysitename, 6L, diysitename);
    }
    public Page<TdOrder> findAllVerifyBelongShopTitleOrderByIdDesc(String diysitename, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findByStatusIdAndShopTitleOrStatusIdAndShopTitleOrderByIdDesc(5L, diysitename, 6L, diysitename, pageRequest);
    }
    public List<TdOrder> findAllVerifyBelongShopTitleAndTimeAfter(String diysitename, Date time){
    	return repository.findByStatusIdAndShopTitleAndOrderTimeAfterOrStatusIdAndShopTitleAndOrderTimeAfterOrderByIdDesc(5L, diysitename, time, 6L, diysitename, time);
    }
    public Page<TdOrder> findAllVerifyBelongShopTitleTimeAfterOrderByIdDesc(String diysitename, Date time,  int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findByStatusIdAndShopTitleAndOrderTimeAfterOrStatusIdAndShopTitleAndOrderTimeAfterOrderByIdDesc(5L, diysitename, time, 6L, diysitename, time, pageRequest);
    }
    
    /**
	 * @author lc
	 * @注释：订单返利查询
	 */
    public List<TdOrder> findByUsernameIn(List<String> tdUsers){
    	return repository.findByUsernameIn(tdUsers);
    }
    public Page<TdOrder> findByUsernameIn(List<String> tdUsers, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findByUsernameInOrderByIdDesc(tdUsers, pageRequest);
    }
    
    public List<TdOrder> findByUsernameInAndOrderTimeAfter(List<String> tdUsers, Date time){
    	return repository.findByUsernameInAndOrderTimeAfterOrderByIdDesc(tdUsers, time);
    }
    public Page<TdOrder> findByUsernameInAndOrderTimeAfter(List<String> tdUsers, Date time, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findByUsernameInAndOrderTimeAfterOrderByIdDesc(tdUsers, time, pageRequest);
    }
    /**
	 * @author lc
	 * @注释：根据订单类型和订单状态进行查询
	 */
    public Page<TdOrder> findByStatusAndTypeOrderByIdDesc(long statusId, long typeId, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findByStatusIdAndTypeIdOrderByIdDesc(statusId, typeId, pageRequest);
    }
    public List<TdOrder> findByStatusAndTypeIdOrderByIdDesc(long statusId, long typeId){
    	return repository.findByStatusIdAndTypeIdOrderByIdDesc(statusId, typeId);
    }
    public Page<TdOrder> findByStatusOrderByIdDesc(long StatusId, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findByStatusIdOrderByIdDesc(StatusId, pageRequest);
    }
    public List<TdOrder> findByStatusOrderByIdDesc(long StatusId){
    	return repository.findByStatusIdOrderByIdDesc(StatusId);
    }
    public Page<TdOrder> findBytypeIdOrderByIdDesc(long typeId, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findBytypeIdOrderByIdDesc(typeId, pageRequest);
    }
    public List<TdOrder> findBytypeIdOrderByIdDesc(long typeId){
    	return repository.findBytypeIdOrderByIdDesc(typeId);
    }
    
    
    /**
	 * @author lc
	 * @注释 按时间、订单类型和订单状态查询
	 */
    public Page<TdOrder> findByTimeAfterOrderByIdDesc(Date time, int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size);
        
        return repository.findByOrderTimeAfterOrderByIdDesc(time, pageRequest);
    }
    public List<TdOrder> findByTimeAfterOrderByIdDesc(Date time){
    	return repository.findByOrderTimeAfterOrderByIdDesc(time);
    }
    
    public Page<TdOrder> findByStatusIdAndTypeIdAndTimeAfterOrderByIdDesc(long statusId, long typeId, Date time, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findByStatusIdAndTypeIdAndOrderTimeAfterOrderByIdDesc(statusId, typeId, time, pageRequest);
    }
    public List<TdOrder> findByStatusAndTypeIdAndTimeAfterOrderByIdDesc(long statusId, long typeId, Date time){
    	return repository.findByStatusIdAndTypeIdAndOrderTimeAfterOrderByIdDesc(statusId, typeId, time);
    }
    public Page<TdOrder> findByStatusAndTimeAfterOrderByIdDesc(long StatusId, Date time, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findByStatusIdAndOrderTimeAfterOrderByIdDesc(StatusId, time, pageRequest);
    }
    public List<TdOrder> findByStatusAndTimeAfterOrderByIdDesc(long StatusId, Date time){
    	return repository.findByStatusIdAndOrderTimeAfterOrderByIdDesc(StatusId, time);
    }
    public Page<TdOrder> findBytypeIdAndTimeAfterOrderByIdDesc(long typeId, Date time, int page, int size){
    	PageRequest pageRequest = new PageRequest(page, size);
    	return repository.findBytypeIdAndOrderTimeAfterOrderByIdDesc(typeId, time, pageRequest);
    }
    public List<TdOrder> findBytypeIdAndTimeAfterOrderByIdDesc(long typeId, Date time){
    	return repository.findBytypeIdAndOrderTimeAfterOrderByIdDesc(typeId, time);
    }
    /**
     * 按交易状态查询
     * @author libiao
     */
    public List<TdOrder> findByStatusId(Long statusId){
    	return repository.findByStatusId(statusId);
    }
    
    public List<TdOrder> findAll(Long statusId){
    	return (List<TdOrder>) repository.findAll();
    }
    
    /**
     * 订单编号的各类排序
     * @author libiao
     * 
     * @param orderNumber
     * @param page
     * @param size
     * @return
     */
    public Page<TdOrder> searchByOrderNumber(String orderNumber,int page,int size){
    	if(null == orderNumber){
    		return null;
    	}
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
    	return repository.findByOrderNumberContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrPayTypeTitleContainingIgnoreCase(orderNumber, orderNumber, orderNumber, pageRequest);
    }
    
    public List<TdOrder> searchByOrderNumberAndTypeIdOrderByIdDesc(String keywords,Long type){
    	if(null == keywords){
    		return null;
    	}
    	return repository.findByOrderNumberContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrPayTypeTitleContainingIgnoreCaseOrderByIdDesc(keywords, keywords, keywords, type);
    }
    public Page<TdOrder> searchByOrderNumberAndTypeIdOrderByIdDesc(String keywords,Long type,int page, int size){
    	if(null == keywords){
    		return null;
    	}
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
    	return repository.findByOrderNumberContainingIgnoreCaseAndTypeIdOrUsernameContainingIgnoreCaseAndTypeIdOrPayTypeTitleContainingIgnoreCaseAndTypeId(keywords, type, keywords, type, keywords, type, pageRequest);
    }
    
    public Page<TdOrder> searchByOrderNumberAndStatusOrderByIdDesc(String keywords,long StatusId, int page, int size){
    	if(null == keywords){
    		return null;
    	}
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
    	return repository.findByOrderNumberContainingIgnoreCaseAndStatusIdOrUsernameContainingIgnoreCaseAndStatusIdOrPayTypeTitleContainingIgnoreCaseAndStatusIdOrderByIdDesc(keywords, StatusId, keywords, StatusId, keywords, StatusId, pageRequest);
    }
    
    public List<TdOrder> searchByOrderNumberAndStatusOrderByIdDesc(String keywords,long StatusId){
    	if(null == keywords){
    		return null;
    	}
    	return repository.findByOrderNumberContainingIgnoreCaseAndStatusIdOrUsernameContainingIgnoreCaseAndStatusIdOrPayTypeTitleContainingIgnoreCaseAndStatusIdOrderByIdDesc(keywords, StatusId, keywords, StatusId, keywords, StatusId);
    }
    
    public Page<TdOrder> searchByOrderNumberAndStatusAndTypeOrderByIdDesc(String orderNumber,long statusId, long typeId, int page, int size){
    	if(null == orderNumber){
    		return null;
    	}
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
    	return repository.findByOrderNumberContainingIgnoreCaseAndStatusIdAndTypeIdOrUsernameContainingIgnoreCaseAndStatusIdAndTypeIdOrPayTypeTitleContainingIgnoreCaseAndStatusIdAndTypeId(orderNumber,statusId, typeId, orderNumber,statusId, typeId, orderNumber,statusId, typeId, pageRequest);
    }
    public List<TdOrder> searchByOrderNumberAndStatusAndTypeIdOrderByIdDesc(String orderNumber,long statusId, long typeId){
    	if(null == orderNumber){
    		return null;
    	}
    	return repository.findByOrderNumberContainingIgnoreCaseAndStatusIdAndTypeIdOrUsernameContainingIgnoreCaseAndStatusIdAndTypeIdOrPayTypeTitleContainingIgnoreCaseAndStatusIdAndTypeIdOrderByIdDesc(orderNumber,statusId, typeId, orderNumber,statusId, typeId, orderNumber,statusId, typeId);
    }
    
    public Page<TdOrder> searchByOrderNumberAndTimeAfterOrderByIdDesc(String orderNumber,Date time, int page, int size)
    {
    	if(null == orderNumber){
    		return null;
    	}
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
        
        return repository.findByOrderNumberContainingIgnoreCaseAndOrderTimeAfterOrUsernameContainingIgnoreCaseAndOrderTimeAfterOrPayTypeTitleContainingIgnoreCaseAndOrderTimeAfter(orderNumber, time, orderNumber, time, orderNumber, time, pageRequest);
    }
    public List<TdOrder> searchByOrderNumberAndTimeAfterOrderByIdDesc(String orderNumber,Date time){
    	if(null == orderNumber){
    		return null;
    	}
    	return repository.findByOrderNumberContainingIgnoreCaseAndOrderTimeAfterOrUsernameContainingIgnoreCaseAndOrderTimeAfterOrPayTypeTitleContainingIgnoreCaseAndOrderTimeAfterOrderByIdDesc(orderNumber, time, orderNumber, time, orderNumber, time);
    }
    
    public Page<TdOrder> searchByOrderNumberAndtypeIdAndTimeAfterOrderByIdDesc(String orderNumber,long typeId, Date time, int page, int size){
    	if(null == orderNumber){
    		return null;
    	}
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
    	return repository.findByOrderNumberContainingIgnoreCaseAndTypeIdAndOrderTimeAfterOrUsernameContainingIgnoreCaseAndTypeIdAndOrderTimeAfterOrPayTypeTitleContainingIgnoreCaseAndTypeIdAndOrderTimeAfter(orderNumber,typeId, time, orderNumber,typeId, time, orderNumber,typeId, time, pageRequest);
    }
    public List<TdOrder> searchByOrderNumberAndtypeIdAndTimeAfterOrderByIdDesc(String orderNumber,long typeId, Date time){
    	if(null == orderNumber){
    		return null;
    	}
    	return repository.findByOrderNumberContainingIgnoreCaseAndTypeIdAndOrderTimeAfterOrUsernameContainingIgnoreCaseAndTypeIdAndOrderTimeAfterOrPayTypeTitleContainingIgnoreCaseAndTypeIdAndOrderTimeAfterOrderByIdDesc(orderNumber,typeId, time, orderNumber,typeId, time, orderNumber,typeId, time);
    }
    
    public Page<TdOrder> searchByOrderNumberAndStatusAndTimeAfterOrderByIdDesc(String orderNumber,long StatusId, Date time, int page, int size){
    	if(null == orderNumber){
    		return null;
    	}
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
    	return repository.findByOrderNumberContainingIgnoreCaseAndStatusIdAndOrderTimeAfterOrUsernameContainingIgnoreCaseAndStatusIdAndOrderTimeAfterOrPayTypeTitleContainingIgnoreCaseAndStatusIdAndOrderTimeAfter(orderNumber,StatusId, time, orderNumber,StatusId, time, orderNumber,StatusId, time, pageRequest);
    }
    public List<TdOrder> searchByOrderNumberAndStatusAndTimeAfterOrderByIdDesc(String orderNumber,long StatusId, Date time){
    	if(null == orderNumber){
    		return null;
    	}
    	return repository.findByOrderNumberContainingIgnoreCaseAndStatusIdAndOrderTimeAfterOrUsernameContainingIgnoreCaseAndStatusIdAndOrderTimeAfterOrPayTypeTitleContainingIgnoreCaseAndStatusIdAndOrderTimeAfterOrderByIdDesc(orderNumber,StatusId, time, orderNumber,StatusId, time, orderNumber,StatusId, time);
    }
    public Page<TdOrder> searchByOrderNumberAndStatusIdAndTypeIdAndTimeAfterOrderByIdDesc(String orderNumber,long statusId, long typeId, Date time, int page, int size){
    	if(null == orderNumber){
    		return null;
    	}
    	PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
    	return repository.findByOrderNumberContainingIgnoreCaseAndStatusIdAndTypeIdAndOrderTimeAfterOrUsernameContainingIgnoreCaseAndStatusIdAndTypeIdAndOrderTimeAfterOrPayTypeTitleContainingIgnoreCaseAndStatusIdAndTypeIdAndOrderTimeAfter(orderNumber,statusId, typeId, time, orderNumber,statusId, typeId, time, orderNumber,statusId, typeId, time, pageRequest);
    }
    public List<TdOrder> searchByOrderNumberAndStatusAndTypeIdAndTimeAfterOrderByIdDesc(String orderNumber,long statusId, long typeId, Date time){
    	if(null == orderNumber){
    		return null;
    	}
    	return repository.findByOrderNumberContainingIgnoreCaseAndStatusIdAndTypeIdAndOrderTimeAfterOrUsernameContainingIgnoreCaseAndStatusIdAndTypeIdAndOrderTimeAfterOrPayTypeTitleContainingIgnoreCaseAndStatusIdAndTypeIdAndOrderTimeAfterOrderByIdDesc(orderNumber,statusId, typeId, time, orderNumber,statusId, typeId, time, orderNumber,statusId, typeId, time);
    }
    
    /*--------------------------------*/


	


	/**
	 * @author dengxiao 根据电话号码和交易状态查找订单（不分页）
	 */

	// 查找指定用户的所有订单
	public List<TdOrder> findByUsername(String username) {
		return repository.findByUsernameOrderByOrderTimeDesc(username);
	};

	// 查找指定用户已经完成的订单
	public List<TdOrder> findByUsernameAndStatusId(String username) {
		return repository.findByUsernameAndStatusIdOrderByOrderTimeDesc(username, 6L);
	};

	// 查找指定用户未完成的订单
	public List<TdOrder> findByUsernameAndStatusIdNot(String username) {
		return repository.findByUsernameAndStatusIdNotOrderByOrderTimeDesc(username, 6L);
	};


	public List<TdOrder> findByUsernameAndFinishTimeBetweenAndStatusId(String username, Date beginDate,
			Date finishDate) {
		if (null == username || null == beginDate || null == finishDate) {
			return null;
		}
		return repository.findByUsernameAndFinishTimeBetweenAndStatusIdOrderByOrderTimeAsc(username, beginDate,
				finishDate, 6L);
	}

	public List<TdOrder> findByUsernameAndFinishTimeBetween(String username, Date beginDate, Date finishDate) {
		if (null == username || null == beginDate || null == finishDate) {
			return null;
		}
		return repository.findByUsernameAndFinishTimeBetweenOrderByOrderTimeAsc(username, beginDate, finishDate);
	}

	// 查找指定用户已停车的订单
	public List<TdOrder> findByUsernameAndParked(String username) {
		if (null == username) {
			return null;
		}
		return repository.findByUsernameAndStatusIdOrUsernameAndStatusIdOrUsernameAndStatusIdOrderByOrderTimeDesc(
				username, 4L, username, 5L, username, 6L);
	}

	// 查找指定用户未停车的订单
	public List<TdOrder> findByUsernameAndNotParked(String username) {
		if (null == username) {
			return null;
		}
		return repository.findByUsernameAndStatusIdOrUsernameAndStatusIdOrUsernameAndStatusIdOrderByOrderTimeDesc(
				username, 1L, username, 2L, username, 3L);
	}

	// 查找指定用户等待审核的订单
	public List<TdOrder> findCheckingOrder(String username) {
		if (null == username) {
			return null;
		}
		return repository.findByUsernameAndStatusIdOrderByOrderTimeDesc(username, 7L);
	}

	// 查找指定用户所有已取消的订单
	public List<TdOrder> findByCancelOrder(String username) {
		if (null == username) {
			return null;
		}
		return repository.findByUsernameAndStatusIdOrUsernameAndStatusIdOrUsernameAndStatusIdOrderByOrderTimeDesc(
				username, 7L, username, 8L, username, 9L);
	}




	// 查找指定用户已经取消的订单
	public List<TdOrder> findCancelOrderByUsername(String username) {
		if (null == username) {
			return null;
		}
		return repository.findByUsernameAndStatusIdOrderByOrderTimeDesc(username, 9L);
	}

	
	/*--------------------------------------zhangji --------------------------------------------*/
	
	//status为空，keywords为空
	public Page<TdOrder> findByFinishTimeBefore(Date date , int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.findByFinishTimeBefore(date, pageRequest);
	}
	public Page<TdOrder> findByOrderTimeAfter(Date date , int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.findByOrderTimeAfter(date, pageRequest);
	}
	public Page<TdOrder> findByOrderTimeAfterAndFinishTimeBefore(Date date1 , Date date2 , int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.findByOrderTimeAfterAndFinishTimeBefore(date1, date2 , pageRequest);
	}
	
	//status为空 keywords不为空
	public Page<TdOrder> findbyFinishTimeBeforeAndSearch(Date date ,String keywords , int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.
				findByFinishTimeBeforeAndOrderNumberContainingOrFinishTimeBeforeAndUsernameContainingOrFinishTimeBeforeAndRealNameContaining
					(date, keywords ,  date , keywords , date, keywords , pageRequest);
	}
	public Page<TdOrder> findByOrderTimeAfterAndSearch(Date date ,String keywords , int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.
				findByOrderTimeAfterAndOrderNumberContainingOrOrderTimeAfterAndUsernameContainingOrOrderTimeAfterAndRealNameContaining
					(date, keywords ,  date , keywords , date, keywords , pageRequest);
	}
	public Page<TdOrder> findByOrderTimeAfterAndFinishTimeBeforeAndSearch(Date date1 , Date date2 ,String keywords , int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.
				findByOrderTimeAfterAndFinishTimeBeforeAndOrderNumberContainingOrOrderTimeAfterAndFinishTimeBeforeAndUsernameContainingOrOrderTimeAfterAndFinishTimeBeforeAndRealNameContaining
					(date1, date2 , keywords ,  date1 , date2 , keywords , date1,  date2 , keywords , pageRequest);
	}
	
	//statusId不为空 keywords为空
	public Page<TdOrder> findByStatusIdAndFinishTimeBefore(Long statusId ,Date date , int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.findByStatusIdAndFinishTimeBefore(statusId , date, pageRequest);
	}	
	public Page<TdOrder> findByStatusIdAndOrderTimeAfter(Long statusId ,Date date , int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.findByStatusIdAndOrderTimeAfter(statusId , date, pageRequest);
	}	
	public Page<TdOrder> findByStatusIdAndOrderTimeAfterAndFinishTimeBefore(Long statusId ,Date date1 , Date date2 ,int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.findByStatusIdAndOrderTimeAfterAndFinishTimeBefore(statusId , date1, date2 , pageRequest);
	}	
	
	//statusId不为空 keywords不为空
	public Page<TdOrder> findByStatusIdAndFinishTimeBeforeAndSearch(Long statusId ,Date date , String keywords ,int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.
				findByStatusIdAndFinishTimeBeforeAndOrderNumberContainingOrStatusIdAndFinishTimeBeforeAndUsernameContainingOrStatusIdAndFinishTimeBeforeAndRealNameContaining
					(statusId , date , keywords , statusId , date , keywords ,statusId , date , keywords ,pageRequest);
	}	
	public Page<TdOrder> findByStatusIdAndOrderTimeAfterAndSearch(Long statusId ,Date date , String keywords ,int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.
				findByStatusIdAndOrderTimeAfterAndOrderNumberContainingOrStatusIdAndOrderTimeAfterAndUsernameContainingOrStatusIdAndOrderTimeAfterAndRealNameContaining
					(statusId , date , keywords , statusId , date , keywords ,statusId , date , keywords ,pageRequest);
	}	
	public Page<TdOrder> findByStatusIdAndOrderTimeAfterAndFinishTimeBeforeAndSearch(Long statusId ,Date date1 , Date date2 , String keywords ,int page , int size)
	{
		PageRequest pageRequest = new PageRequest(page , size , new Sort(Direction.DESC,"orderTime"));
		return repository.
				findByStatusIdAndOrderTimeAfterAndFinishTimeBeforeAndOrderNumberContainingOrStatusIdAndOrderTimeAfterAndFinishTimeBeforeAndUsernameContainingOrStatusIdAndOrderTimeAfterAndFinishTimeBeforeAndRealNameContaining
					(statusId , date1, date2 , keywords , statusId , date1, date2 , keywords ,statusId , date1, date2 , keywords ,pageRequest);
	}	
    
}
