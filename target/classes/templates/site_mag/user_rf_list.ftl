<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>分销列表</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
</head>

<body class="mainbody">
<form name="form1" method="post" action="/Verwalter/product/itemegory/list" id="form1">
<div>
<input type="hidden" name="__EVENTTARGET" id="__EVENTTARGET" value="${__EVENTTARGET!""}" />
<input type="hidden" name="__EVENTARGUMENT" id="__EVENTARGUMENT" value="${__EVENTARGUMENT!""}" />
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="${__VIEWSTATE!""}" />
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
<div class="location">
  <a href="javascript:history.back(-1);" class="back"><i></i><span>返回上一页</span></a>
  <a href="/Verwalter/center" class="home"><i></i><span>首页</span></a>
  <i class="arrow"></i
  <span>分销用户【${user.username!''}】</span>
</div>
<!--/导航栏-->

<!--工具栏-->
<#--
<div class="toolbar-wrap">
  <div id="floatHead" class="toolbar">
    <div class="l-list">
      <ul class="icon-list">
        <li><a class="add" href="/Verwalter/product/itemegory/edit"><i></i><span>新增</span></a></li>
        <li><a id="btnSave" class="save" href="javascript:__doPostBack('btnSave','')"><i></i><span>保存</span></a></li>
        <li><a class="all" href="javascript:;" onclick="checkAll(this);"><i></i><span>全选</span></a></li>
        <li><a onclick="return ExePostBack('btnDelete');" id="btnDelete" class="del" href="javascript:__doPostBack('btnDelete','')"><i></i><span>删除</span></a></li>
      </ul>
    </div>
  </div>
</div>
-->
<!--/工具栏-->

<!--列表-->

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ltable">
    <tbody><tr class="odd_bg">
        <th align="left">用户名</th>
        <th align="left" width="12%">编号</th>
        <th align="left" width="20%">积分</th>
        <th align="left" width="20%">消费</th>
        <th width="20%">操作</th>
    </tr>
    
    <tr>
        <td style="margin-left:20px;">
            <span class="folder-open"></span>
            <a href="/Verwalter/user/rf/edit?id=${user.id?c!""}">${user.username!""}</a>
        </td>
        <td>${user.number!""}</td>
        <td><#if user.totalPoints??>${user.totalPoints?c!""}<#else>0</#if></td>
        <td><#if user.spend??>${user.spend?string("0.00")}<#else>0.00</#if></td>
        <td align="center">
            <a href="/Verwalter/user/rf/edit?id=${user.id?c!""}">修改</a>
        </td>
    </tr>
	<#if one_list??>
	<#list one_list as item>
        <tr class="odd_bg">
            <td style="margin-left:20px;">
                    <span style="display:inline-block;width:24px;"></span>
                    <span class="folder-line"></span>
	                <span class="folder-open"></span>
                <a href="/Verwalter/user/rf/edit?id=${item.id?c!""}">${item.username!""}</a>
            </td>
            <td>${item.number!""}</td>
            <td><#if item.totalPoints??>${item.totalPoints?c!""}<#else>0</#if></td>
            <td><#if item.spend??>${item.spend?string("0.00")}<#else>0.00</#if></td>
            <td align="center">
                <a href="/Verwalter/user/rf/edit?id=${item.id?c!""}">修改</a>
            </td>
        </tr>
        	<#if ("list_two_"+item.id)?eval??>
				<#assign two_list = ("list_two_"+item.id)?eval>
				<#list two_list as two>
			        <tr style="background-color:#eeeeee;">
			            <td style="margin-left:20px;">
			                    <span style="display:inline-block;width:48px;"></span>
			                    <span class="folder-line"></span>
				                <span class="folder-open"></span>
			                <a href="/Verwalter/user/rf/edit?id=${two.id?c!""}">${two.username!""}</a>
			            </td>
			            <td>${two.number!""}</td>
			            <td><#if item.totalPoints??>${two.totalPoints?c!""}<#else>0</#if></td>
			            <td><#if two.spend??>${two.spend?string("0.00")}<#else>0.00</#if></td>
			            <td align="center">
			                <a href="/Verwalter/user/rf/edit?id=${two.id?c!""}">修改</a>
			            </td>
			        </tr>
			    </#list>
		    <#else>
		    <tr><td>哎</td></tr>
		    </#if>    
	</#list>
	</#if>
    
    </tbody>
</table>

<!--/列表-->

<!--内容底部-->
<div class="line20"></div>
<div class="pagelist">
</div>
<!--/内容底部-->

</form>

</body></html>