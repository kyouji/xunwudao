<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>账务处理</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<script type="text/javascript" src="/mag/js/WdatePicker.js"></script>
<link href="/mag/style/pagination.css" rel="stylesheet" type="text/css">
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
</head>

<body class="mainbody"><div class="" style="left: 0px; top: 0px; visibility: hidden; position: absolute;"><table class="ui_border"><tbody><tr><td class="ui_lt"></td><td class="ui_t"></td><td class="ui_rt"></td></tr><tr><td class="ui_l"></td><td class="ui_c"><div class="ui_inner"><table class="ui_dialog"><tbody><tr><td colspan="2"><div class="ui_title_bar"><div class="ui_title" unselectable="on" style="cursor: move;"></div><div class="ui_title_buttons"><a class="ui_min" href="javascript:void(0);" title="最小化" style="display: inline-block;"><b class="ui_min_b"></b></a><a class="ui_max" href="javascript:void(0);" title="最大化" style="display: inline-block;"><b class="ui_max_b"></b></a><a class="ui_res" href="javascript:void(0);" title="还原"><b class="ui_res_b"></b><b class="ui_res_t"></b></a><a class="ui_close" href="javascript:void(0);" title="关闭(esc键)" style="display: inline-block;">×</a></div></div></td></tr><tr><td class="ui_icon" style="display: none;"></td><td class="ui_main" style="width: auto; height: auto;"><div class="ui_content" style="padding: 10px;"></div></td></tr><tr><td colspan="2"><div class="ui_buttons" style="display: none;"></div></td></tr></tbody></table></div></td><td class="ui_r"></td></tr><tr><td class="ui_lb"></td><td class="ui_b"></td><td class="ui_rb" style="cursor: se-resize;"></td></tr></tbody></table></div>
<form name="form1" method="post" action="/Verwalter/bill/fee/list" id="form1">
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
  <span>账务处理</span>
  <i class="arrow"></i>
  <span>税费沟通</span>  
</div>
<!--/导航栏-->

<!--工具栏-->
<div class="toolbar-wrap">
  <div id="floatHead" class="toolbar" style="position: static; top: 42px;">
    <div class="l-list">
      <ul class="icon-list">
        <li><a class="all" href="javascript:;" onclick="checkAll(this);"><i></i><span>全选</span></a></li>
        <li><a onclick="return ExePostBack('btnDelete');" id="btnDelete" class="del" href="javascript:__doPostBack('btnDelete','')"><i></i><span>删除</span></a></li>
      </ul>
      <#--<ul class="icon-list">
      	<li><a class="del" href="/Verwalter/import/gather?time=${time!''}"><i></i><span>导入excel</span></a></li>
      </ul>
      -->
    	<div class="menu-list">
        	<span style="margin:9px 5px 0 5px;float:left; font-size:12px;">筛选月份：</span>
		      <div class="input-date" style="width:204px;">
		        <input  name="time" type="text" style="font-size:12px;" value="<#if time??>${time}</#if>" class="input date" onfocus="WdatePicker({dateFmt:&#39;yyyy-MM&#39;})" datatype="/^\s*$|^\d{4}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" ">
		        <i style="right:36px;">日期</i>
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
    <th width="8%">用户名</th>
    <th width="6%">用户编号</th>
    <th width="15%">公司名称</th>
    <#--<th width="8%">公司类型</th>-->
    <th width="6%">不含税收入</th>
    <th width="6%">增值税</th>
    <th width="6%">所得税</th>
    <th width="6%">地税</th>
    <th width="6%">用户是否确认税费</th>
    <th width="12%">操作</th>
  </tr>
    <#if gather_page??>
        <#list gather_page.content as item>
            <tr>
                <td align="center">
                    <span class="checkall" style="vertical-align:middle;">
                        <input id="listChkId" type="checkbox" name="listChkId" value="${item_index}" >
                    </span>
                    <input type="hidden" name="listId" id="listId" value="${item.id}">
                </td>
                <#if ("user_" + item.id)?eval??>
        		<#assign user = ("user_" + item.id)?eval>
	                <td align="center">
	                		<a href="/Verwalter/user/edit?id=${user.id?c}">${user.username!''}</a>
	                </td>
	                <td align="center">${user.number!""}</td>
	                <td align="center">${user.enterName!""}</td>
                <#else>
                	<td align="center"></td>
                	<td align="center"></td>
                	<td align="center"></td>
                </#if>	
                <#--
                <#if ("enterType_" + item.id)?eval??>
	    		<#assign enterType = ("enterType_" + item.id)?eval>
		            <td align="center">${enterType!""}</td>
	            <#else>
	            	<td align="center"></td>
	            </#if>
	            -->	
                <td align="center">￥<font color="#C30000"><#if item.totalIncome??>${item.totalIncome?string("0.00")}<#else>0.00</#if></font></td>
                <td align="center">￥<font color="#C30000"><#if item.taxAdd??>${item.taxAdd?string("0.00")}<#else>0.00</#if></font></td>
                <td align="center">￥<font color="#C30000"><#if item.incomeTax??>${item.incomeTax?string("0.00")}<#else>0.00</#if></font></td>
                <td align="center">￥<font color="#C30000"><#if item.landTax??>${item.landTax?string("0.00")}<#else>0.00</#if></font></td>
                <td align="center"><#if item.statusId??><#if item.statusId==1>否<#elseif item.statusId==2>是<#elseif item.statusId==0>票据整理未确认</#if></#if></font></td>

                <td align="center">
                    <a href="/Verwalter/bill/gather/deal/${item.id}?time=${time!''}">财税复核</a>&nbsp;|
                    <a href="">税费扣缴</a>&nbsp;|
                    <a href="/Verwalter/bill/finance/edit?userId=${item.userId?c}&time=${time!''}">财务状况</a>
                </td>    
              </tr>
        </#list>
    </#if>
     
</tbody>
</table>

<!--/列表-->

<!--内容底部-->
<#assign PAGE_DATA=gather_page />
<#include "/site_mag/list_footer.ftl" />
<!--/内容底部-->
</form>


</body></html>