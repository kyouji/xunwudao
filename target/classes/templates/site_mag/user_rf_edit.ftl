<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>编辑会员信息</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js"></script>
<script type="text/javascript" src="/mag/js/WdatePicker.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.queue.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.handlers.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
<link href="/mag/style/WdatePicker.css" rel="stylesheet" type="text/css">
<script>
$(document).ready(function(){
  // 添加弹窗
    $("#addItem").click(function(){
        showDialogItem();
    });
       
    //创建窗口
    function showDialogItem(obj) {
        var objNum = arguments.length;
        
        var giftDialog = $.dialog({
            id: 'giftDialogId',
            lock: true,
            max: false,
            min: false,
            title: "用户列表",
            content: 'url:/Verwalter/user/rf/list/dialog?total=' + $("#var_box_gift").children("tr").length,
            width: 600,
            height: 250
        });
        
        //如果是修改状态，将对象传进去
        if (objNum == 1) {
            giftDialog.data = obj;
        }
    }
    
    //删除促销赠品节点
    function delGiftNode(obj) {
        $(obj).parent().parent().remove();
    }
});
</script>
</head>

<body class="mainbody">
<form name="form_user" method="post" action="/Verwalter/user/rf/save" id="form_user">
<div>
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="${__VIEWSTATE!""}" >
<input type="hidden" id="userId" name="userId" value="<#if user??>${user.id?c!""}</#if>" >
</div>
<!--导航栏-->
<div class="location" style="position: static; top: 0px;">
  <a href="/Verwalter/user/list"><i></i><span>返回列表页</span></a>
  <a href="/Verwalter/center" class="home"><i></i><span>首页</span></a>
  <i class="arrow"></i>
  <span>会员管理</span>
  <i class="arrow"></i>
  <span>编辑会员信息</span>
</div>
<div class="line10"></div>
<!--/导航栏-->

<!--内容-->
<div class="content-tab-wrap">
  <div id="floatHead" class="content-tab" style="position: static; top: 52px;">
    <div class="content-tab-ul-wrap">
      <ul>
        <li><a href="javascript:;" onclick="tabs(this);" class="selected">基本资料</a></li>
      </ul>
    </div>
  </div>
</div>

<!--基本资料-->
<div class="tab-content">
  
    <dl>
        <dt>用户名：</dt>
        <dd>
            <#if user??>
                <span>${user.username!""}</span>
            <#else>
                <input name="username" type="text" maxlength="200" class="input normal" datatype="s6-20" ajaxurl="/Verwalter/user/check<#if user??>?id=${user.id}</#if>" sucmsg=" " minlength="2">
            </#if>
            <span class="Validform_checktip">
        </span></dd>
    </dl>
  
  <dl>
    <dt>真实姓名</dt>
    <dd><#if user??>${user.realName!""}</#if></dd>
  </dl>
  <dl>
    <dt>手机号码</dt>
    <dd><#if user??>${user.mobile!""}</#if></dd>
  </dl>
  <dl>
    <dt>累计消费额</dt>
    <dd><span><#if user?? &&user.totalSpendCash??>${user.totalSpendCash}<#else>0</#if></span></dd>
  </dl>
  <dl>
    <dt>用户积分</dt>
    <dd><#if user?? && user.totalPoints??>${user.totalPoints?c}<#else>0</#if></dd>
  </dl>
  <dl>
    <dt>下级用户总数</dt>
    <dd><#if user?? && user.totalLowerUsers??>${user.totalLowerUsers?c}</#if></dd>
  </dl>
  
  <dl>
	<dt>上一级用户</dt>
	<dd>
	<input type="hidden" name="userId" id="userId" value="<#if user??>${user.id?c!''}</#if>">
	<#if userOne??>
	<input type="text" name="username" id="username" value="<#if userOne??>${userOne.username!''}</#if>">
	</#if>
	<input type="hidden" name="id" id="id" value="<#if userOne??>${userOne.id?c!''}</#if>">
	<input type="hidden" name="realName" id="realName" value="<#if userOne??>${userOne.realName!''}</#if>">
	<#--<input id="addItem" type="button" style="background: #1A7FCA; border: none;  width: 50px;  height: 20px;  color: #fff;  cursor: pointer;  display: inline-block;" value="修改" > -->
	</dd>
  </dl>


</div>



<!--借款标的-->

<!--/借款标的-->

<!--投资标的-->

<!--/投资标的-->

<!--资金明细-->

<!--/资金明细-->

<!--工具栏-->
<div class="page-footer">
  <div class="btn-list">
    <input type="submit" name="btnSubmit" value="提交保存" id="btnSubmit" class="btn">
    <input name="btnReturn" type="button" value="返回上一页" class="btn yellow" onclick="javascript:history.back(-1);">
  </div>
  <div class="clear"></div>
</div>
<!--/工具栏-->

</form>


</body></html>