<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>处理业务申请</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.queue.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.handlers.js"></script>
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
    $(function () {
        //初始化表单验证
        $("#form1").initValidform();
    });
    
    //处理票据
    function applyDeal()
		{
			var statusId = $("input[name='statusId']:checked").val();
			var id = <#if apply??>${apply.id?c}</#if>;
			var remark = $("#remark").val();
			var sortId = $("#sortId").val();
			  $.ajax({
			      type:"post",
			      url:"/Verwalter/user/apply/save",
			      data:{"statusId":statusId,
			      		   "remark":remark,
			      		   "sortId":sortId,
			    	  		"id":id},
			      success:function(data){
					if (data.code == 1)
					{
			            alert(data.msg);
			            if(data.login==1)
			            {
			            	location.href='/Verwalter/login';
			            }
					}
					else{
						$.dialog.alert(data.msg)
						
						setTimeout("location.href='/Verwalter/user/apply/list?statusId=${apply.statusId!''}'",2000);
						
					}
			      }
			  });
		}
</script>
</head>

<body class="mainbody">
<!--导航栏-->
<div class="location">
  <a href="/Verwalter/user/apply/list" class="back"><i></i><span>返回列表页</span></a>
  <a href="/Verwalter/center" class="home"><i></i><span>首页</span></a>
  <i class="arrow"></i>
  <span>意见或建议</span>
  <i class="arrow"></i>
  <span>处理</span>
</div>
<div class="line10"></div>
<!--/导航栏-->

<!--内容-->
<div class="content-tab-wrap">
  <div id="floatHead" class="content-tab">
    <div class="content-tab-ul-wrap">
      <ul>
        <li><a href="javascript:;" onclick="tabs(this);" class="selected">信息</a></li>
      </ul>
    </div>
  </div>
</div>

<div class="tab-content">
  <dl>
    <dt>用户</dt>
    <dd>
         ${apply.realName!''}
    </dd>
  </dl>
  <dl>
    <dt>提交时间</dt>
    <dd>
        <#if apply.time??>${apply.time?string('yyyy年MM月dd日 hh:mm:ss')}</#if>
    </dd>
  </dl>
   <dl>
       <dt>详细说明</dt>
       <dd>
           <textarea rows="2" cols="20" disabled="" class="input" style="height:250px;width:750px;" >${apply.content!""}</textarea>
       </dd>
   </dl>   
	<dl>
        <dt>处理</dt>
        <dd>
            <div class="rule-multi-radio multi-radio">
                <span id="rblStatus" style="display: none;">
                    <input type="radio" name="statusId" value="1" <#if apply?? && apply.statusId?? && apply.statusId==1>checked="checked"</#if> ><label>已读</label>
                    <input type="radio" name="statusId" value="2" <#if !apply?? || apply?? && apply.statusId?? && apply.statusId==0>checked="checked"</#if>><label>未读</label>
                </span>
            </div>
        </dd>
    </dl>   
    <dl>
        <dt>排序数字</dt>
        <dd>
            <input name="sortId"  type="text" value="<#if apply??>${apply.sortId!""}<#else>99</#if>" id="sortId" class="input txt100" datatype="n" sucmsg=" ">
            <span class="Validform_checktip">*数字，越小越向前</span>
        </dd>
    </dl>    
   <dl>
       <dt>备注</dt>
       <dd>
           <textarea name="remark"  rows="2" cols="20" id="remark" class="input"  style="height:150px;width:750px;"  datatype="*0-255" sucmsg=" "><#if apply??>${apply.remark!""}</#if></textarea>
       </dd>
   </dl>  

</div>
<!--/内容-->


<!--工具栏-->
<div class="page-footer">
  <div class="btn-list">
    <input type="submit" name="btnSubmit" value="提交保存" id="btnSubmit" onclick="applyDeal();" class="btn">
    <input name="btnReturn" type="button" value="返回上一页" class="btn yellow" onclick="javascript:history.back(-1);">
  </div>
  <div class="clear"></div>
</div>
<!--/工具栏-->


</body></html>