<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>票据管理</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<script type="text/javascript" src="/mag/js/WdatePicker.js"></script>
<link href="/mag/style/pagination.css" rel="stylesheet" type="text/css">
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
</head>

<body class="mainbody"><div class="" style="left: 0px; top: 0px; visibility: hidden; position: absolute;"><table class="ui_border"><tbody><tr><td class="ui_lt"></td><td class="ui_t"></td><td class="ui_rt"></td></tr><tr><td class="ui_l"></td><td class="ui_c"><div class="ui_inner"><table class="ui_dialog"><tbody><tr><td colspan="2"><div class="ui_title_bar"><div class="ui_title" unselectable="on" style="cursor: move;"></div><div class="ui_title_buttons"><a class="ui_min" href="javascript:void(0);" title="最小化" style="display: inline-block;"><b class="ui_min_b"></b></a><a class="ui_max" href="javascript:void(0);" title="最大化" style="display: inline-block;"><b class="ui_max_b"></b></a><a class="ui_res" href="javascript:void(0);" title="还原"><b class="ui_res_b"></b><b class="ui_res_t"></b></a><a class="ui_close" href="javascript:void(0);" title="关闭(esc键)" style="display: inline-block;">×</a></div></div></td></tr><tr><td class="ui_icon" style="display: none;"></td><td class="ui_main" style="width: auto; height: auto;"><div class="ui_content" style="padding: 10px;"></div></td></tr><tr><td colspan="2"><div class="ui_buttons" style="display: none;"></div></td></tr></tbody></table></div></td><td class="ui_r"></td></tr><tr><td class="ui_lb"></td><td class="ui_b"></td><td class="ui_rb" style="cursor: se-resize;"></td></tr></tbody></table></div>
<form name="form1" method="post" action="/Verwalter/bill/list/<#if statusId??>${statusId!""}</#if>" id="form1">
<div>
<input type="hidden" name="__EVENTTARGET" id="__EVENTTARGET" value="${__EVENTTARGET!""}">
<input type="hidden" name="__EVENTARGUMENT" id="__EVENTARGUMENT" value="${__EVENTARGUMENT!""}">
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="${__VIEWSTATE!""}" >
</div>

<script type="text/javascript">
var theForm = document.forms['form1'];
    if (!theForm) {
        theForm = document.form1;
    }
    function __doPostBack(eventTarget, eventArgument) {
        if (!theForm.onsubmit || (theForm.onsubmit() != false)) {
            theForm.__EVENTTARGET.value = eventTarget;
            theForm.__EVENTARGUMENT.value = eventArgument;
            theForm.submit();
        }
    }
</script>
<!--导航栏-->
<div class="location" style="position: static; top: 0px;">
  <a href="javascript:history.back(-1);" class="back"><i></i><span>返回上一页</span></a>
  <a href="/Verwalter/center" class="home"><i></i><span>首页</span></a>
  <i class="arrow"></i>
  <span>票据管理</span>
  <i class="arrow"></i>
  <span>票据列表</span>  
</div>
<!--/导航栏-->

<!--工具栏-->
<div class="toolbar-wrap">
  <div id="floatHead" class="toolbar" style="position: static; top: 42px;">
    <div class="l-list">
      <ul class="icon-list">
        <li><a class="all" href="javascript:;" onclick="checkAll(this);"><i></i><span>全选</span></a></li>
        <#if tdManagerRole?? && tdManagerRole.isSys>
        <li><a onclick="return ExePostBack('btnDelete');" id="btnDelete" class="del" href="javascript:__doPostBack('btnDelete','')"><i></i><span>删除</span></a></li>
        </#if>
      </ul>
    	<div class="menu-list">
        	<span style="margin:9px 5px 0 5px;float:left; font-size:12px;">筛选查找：</span>
		      <div class="input-date" style="width:204px;">
		      	<span style="margin:9px 5px 0 5px;float:left; font-size:12px;" >从</span>
		        <input  name="date_1" type="text" style="font-size:12px;" value="<#if date_1??>${date_1}</#if>" class="input date" onfocus="WdatePicker({dateFmt:&#39;yyyy-MM-dd&#39;})" datatype="/^\s*$|^\d{4}\-\d{1,2}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" ">
		        <i>日期</i>
		      </div>		
	      		
		      <div class="input-date" style="width:204px;">
		      	<span style="margin:9px 5px 0 5px;float:left; font-size:12px;" >至</span>
		        <input  name="date_2" type="text" style="font-size:12px;" value="<#if date_2??>${date_2}</#if>" class="input date" onfocus="WdatePicker({dateFmt:&#39;yyyy-MM-dd&#39;})" datatype="/^\s*$|^\d{4}\-\d{1,2}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" ">
		        <i>日期</i>
		      </div>			   
		      
		<div class="rule-single-select single-select">
            <select name="billTypeId" onchange="javascript:__doPostBack('','')" id="ddlbillTypeId" style="display: none;">
                <option <#if billTypeId??><#else>selected="selected"</#if> value="">所有票据类别</option>
                <#if billType_list??>
                    <#list billType_list as c>
                        <option value="${c.id!""}" <#if billTypeId?? && c.id==billTypeId>selected="selected"</#if> >${c.title!""}</option>
                    </#list>
                </#if>
            </select>
        </div>
           
	    </div>
	    
	    <div class="r-list">
		    <a id="lbtnSearch" class="btn-search" href="javascript:__doPostBack('btnSearch','')">查询</a>
	    </div>        
    </div>
  </div>
</div>
<!--/工具栏-->

<!--列表-->

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ltable">
  <tbody>
  <tr class="odd_bg">
    <th width="4%">选择</th>
    <th width="6%">票据</th>
    <th width="12%">用户名</th>
    <th width="6%">用户编号</th>
    <th width="8%">联系人姓名</th>
    <th width="8%">联系人电话</th>
    <th width="8%">上传时间</th>
    <th width="8%">处理进度</th>
    <th width="6%">操作</th>
  </tr>
    <#if bill_page??>
        <#list bill_page.content as item>
            <tr>
                <td align="center">
                    <span class="checkall" style="vertical-align:middle;">
                        <input id="listChkId" type="checkbox" name="listChkId" value="${item_index}" >
                    </span>
                    <input type="hidden" name="listId" id="listId" value="${item.id}">
                </td>
                <td align="center"><img src="<#if item.imgUrl??&&item.imgUrl != "">/images/${item.imgUrl!""} <#else>/client/images/foote22.png</#if>" width=50 height=50 /></td>
                <#if ("user_" + item.id)?eval??>
        		<#assign user = ("user_" + item.id)?eval>
                <td align="center">
                		<a href="/Verwalter/bill/edit?id=${item.id}&roleId=${roleId!""}">${user.username!''}</a>
                </td>
                <td align="center">${user.number!""}</td>
                <td align="center">${user.realName!""}</td>
                <td align="center">${user.mobile!""}</td>
                <#else>
                <td align="center"></td>
                <td align="center"></td>
                <td align="center"></td>
                </#if>	
                <td align="center"><#if item.time??>${item.time?string("yyyy年MM月dd日 HH:mm:ss")}</#if></td>
                <#--<td align="center"><#if user.statusId??><#if user.statusId==0>待审核<#elseif user.statusId==1>正常</#if></#if></td>-->
                <td align="center">
                <#switch item.statusId>
                <#case 2>用户上传完成<#break>
                <#case 3>票据整理<#break>
                <#case 4>财务处理<#break>
                <#case 5>税费扣缴<#break>
                <#case 6>财务状况表<#break>
                </#switch>
                
                </td>
                <td align="center">
                    <a href="/Verwalter/bill/edit?id=${item.id}&roleId=${roleId!""}">详情</a>
                    <#if statusId?? && statusId lt 6>
                    |&nbsp;<a href="/Verwalter/bill/deal/${item.id}?statusId=${statusId+1}">处理</a></td> 
                    </#if>
                    <#--<a href="/Verwalter/user/edit?id=${user.id}&roleId=${roleId!""}&action=view">查看</a></td>-->
                    <#--<a href="/Verwalter/user/role?id=${user.id}">进入</a></td>-->
              </tr>
        </#list>
    </#if>
     
</tbody>
</table>

<!--/列表-->

<!--内容底部-->
<#assign PAGE_DATA=bill_page />
<#include "/site_mag/list_footer.ftl" />
<!--/内容底部-->
</form>


</body></html>