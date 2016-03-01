package com.ynyes.xunwudao.controller.front;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ynyes.xunwudao.entity.TdArticleCategory;
import com.ynyes.xunwudao.entity.TdKeywords;
import com.ynyes.xunwudao.entity.TdNavigationMenu;
import com.ynyes.xunwudao.service.TdArticleCategoryService;
import com.ynyes.xunwudao.service.TdArticleService;
import com.ynyes.xunwudao.service.TdCommonService;
import com.ynyes.xunwudao.service.TdKeywordsService;
import com.ynyes.xunwudao.service.TdNavigationMenuService;
import com.ynyes.xunwudao.util.ClientConstant;

/**
 * 课程检索
 * @author zhangji
 *
 */
@Controller
public class TdSearchController {
    
    @Autowired
    private TdCommonService tdCommonService;
    
    @Autowired
    private TdKeywordsService tdKeywordsService;
    
    @Autowired
    private TdArticleCategoryService tdArticleCategoryService;
    
    @Autowired
    private TdArticleService tdArticleService;
    
	@Autowired 
    private TdNavigationMenuService tdNavigationMenuService;   //zhangji
    
 // 组成：[排序字段]-[销量排序标志]-[价格排序标志]-[上架时间排序标志]-[是否有货]-[页号]_[价格低值]-[价格高值]
    @RequestMapping(value="/search", method = RequestMethod.GET)
    public String list(String keywords,
    		String __EVENTTARGET,
            String __EVENTARGUMENT,
    		Integer page, HttpServletRequest req, ModelMap map){
        
        tdCommonService.setHeader(map, req);
      //翻页 zhangji
	    if (null != __EVENTTARGET)
        {
            if (__EVENTTARGET.equalsIgnoreCase("btnPage"))
            {
                if (null != __EVENTARGUMENT)
                {
                    page = Integer.parseInt(__EVENTARGUMENT);
                } 
            }
            
        }
	    /*翻页 end*/
        
        if (null == page || page < 0)
        {
            page = 0;
        }
        
        if (null != keywords)
        {
            TdKeywords key = tdKeywordsService.findByTitle(keywords);
            
            if (null != key)
            {
                if (null == key.getTotalSearch())
                {
                    key.setTotalSearch(1L);
                }
                else
                {
                    key.setTotalSearch(key.getTotalSearch() + 1L);
                }
                
                key.setLastSearchTime(new Date());
                
                tdKeywordsService.save(key);
            }
            
            map.addAttribute("course_page", tdArticleService.searchArticle(keywords.trim(), page, ClientConstant.coursePageSize));    
        }
	    TdNavigationMenu menu = tdNavigationMenuService.findOne(12L);
	    
	    map.addAttribute("menu_name", menu.getTitle());
	    map.addAttribute("menu_id", menu.getId()); //菜单id zhangji
	    map.addAttribute("menu_sub_name", menu.getName());//英文名称 zhangji
        List<TdArticleCategory> catList = tdArticleCategoryService.findByMenuId(12L);
        map.addAttribute("info_category_list", catList); //栏目的列表 zhangji     
        map.addAttribute("pageId", page);
        map.addAttribute("keywords", keywords);          
        
        return "/client/search_result";
    }
    
    @RequestMapping(value="/search/page")
    public String searchPage(String keywords,
    		String __EVENTTARGET,
            String __EVENTARGUMENT,
    		Integer page, HttpServletRequest req, ModelMap map){
        
        tdCommonService.setHeader(map, req);
      //翻页 zhangji
	    if (null != __EVENTTARGET)
        {
            if (__EVENTTARGET.equalsIgnoreCase("btnPage"))
            {
                if (null != __EVENTARGUMENT)
                {
                    page = Integer.parseInt(__EVENTARGUMENT);
                } 
            }
            
        }
	    /*翻页 end*/
        
        if (null == page || page < 0)
        {
            page = 0;
        }
        
        if (null != keywords)
        {
            TdKeywords key = tdKeywordsService.findByTitle(keywords);
            
            if (null != key)
            {
                if (null == key.getTotalSearch())
                {
                    key.setTotalSearch(1L);
                }
                else
                {
                    key.setTotalSearch(key.getTotalSearch() + 1L);
                }
                
                key.setLastSearchTime(new Date());
                
                tdKeywordsService.save(key);
            }
            
            map.addAttribute("course_page", tdArticleService.searchArticle(keywords.trim(), page, ClientConstant.coursePageSize));    
        }
	    TdNavigationMenu menu = tdNavigationMenuService.findOne(12L);
	    
	    map.addAttribute("menu_name", menu.getTitle());
	    map.addAttribute("menu_id", menu.getId()); //菜单id zhangji
	    map.addAttribute("menu_sub_name", menu.getName());//英文名称 zhangji
        List<TdArticleCategory> catList = tdArticleCategoryService.findByMenuId(12L);
        map.addAttribute("info_category_list", catList); //栏目的列表 zhangji     
        map.addAttribute("pageId", page);
        map.addAttribute("keywords", keywords);          
        
        return "/client/search_result_detail";
    }
}
