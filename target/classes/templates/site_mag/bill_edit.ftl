<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>处理票据</title>
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
    function billDeal()
		{
			var statusId = $("#statusId").val();
			var id = <#if bill??>${bill.id?c}</#if>;
			var remark = $("#remark").val();
			  $.ajax({
			      type:"post",
			      url:"/Verwalter/bill/save",
			      data:{"statusId":statusId,
			      		   "remark":remark,
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
						/**
						alert(data.msg)
						if(data.statusId != "")
							{
							  location.href='/Verwalter/bill/deal/${bill.id?c}?statusId='+statusId;
							}
						**/
						if (data.statusId == 2)
						{
						    var dialog = $.dialog.alert("操作成功！票据已改为待处理状态",
						    		function(){location.href='/Verwalter/bill/list/2';});
						}
						else if (data.statusId == 3)
						{
							var dialog = $.dialog.confirm('操作成功！继续进行下一步【票据整理】，确认吗？', 
									function(){location.href='/Verwalter/bill/deal/<#if bill??>${bill.id?c}</#if>?statusId=3';},
					                function(){location.href='/Verwalter/bill/list/2';});
						}
						else if (data.statusId == 4)
						{
						    var dialog = $.dialog.confirm('操作成功！继续进行下一步【财务处理】，确认吗？', 
                                   function(){location.href='/Verwalter/bill/finance/edit<#if bill??>?billId=${bill.id?c}</#if>';},
                                   function(){location.href='/Verwalter/bill/list/3';});
						}
						else if (data.statusId == 5)
                        {
                            var dialog = $.dialog.confirm('操作成功！继续进行下一步【税费扣缴】，确认吗？', 
                                function(){location.href='/Verwalter/user/pay<#if user??>?id=${user.id?c}</#if>';},
                                function(){location.href='/Verwalter/bill/list/4';});
                        }
						else if (data.statusId == 6)
                        {
                            var dialog = $.dialog.confirm('操作成功！继续进行下一步【上传财务状况表】，确认吗？', 
                                function(){location.href='/Verwalter/bill/finance/upload<#if bill??>?billId=${bill.id?c}</#if>';},
                                function(){location.href='/Verwalter/bill/list/5';});
                        }
						
					}
			      }
			  });
		}
</script>
</head>

<body class="mainbody">
<!--导航栏-->
<div class="location">
  <a href="/Verwalter/bill/user/billList/<#if user??>${user.id?c}</#if>" class="back"><i></i><span>返回列表页</span></a>
  <a href="/Verwalter/center" class="home"><i></i><span>首页</span></a>
  <i class="arrow"></i>
  <span>票据管理</span>
  <i class="arrow"></i>
  <span>处理票据</span>
</div>
<div class="line10"></div>
<!--/导航栏-->

<!--内容-->
<div class="content-tab-wrap">
  <div id="floatHead" class="content-tab">
    <div class="content-tab-ul-wrap">
      <ul>
        <li><a href="javascript:;" onclick="tabs(this);" class="selected">用户信息</a></li>
      </ul>
    </div>
  </div>
</div>

<div class="tab-content">
  <dl>
    <dt>票据</dt>
    <dd>
        <img src="<#if bill.imgUrl??&&bill.imgUrl != "">/images/${bill.imgUrl!""} <#else>/client/images/foote22.png</#if>"  />
    </dd>
  </dl>
  <dl>
    <dt>下载票据图片</dt>
    <dd>
        <a href="/download/data?name=${bill.imgUrl!''}" >${bill.imgUrl!''}</a>
    </dd>
  </dl>
  <dl>
    <dt>用户名</dt>
    <dd>
        ${user.username!''}
    </dd>
  </dl>
  <dl>
    <dt>联系人姓名</dt>
    <dd>
         ${user.realName!''}
    </dd>
  </dl>
  <dl>
    <dt>联系人电话</dt>
    <dd>
        ${user.mobile!''}
    </dd>
  </dl>
  <dl>
    <dt>上传时间</dt>
    <dd>
        <#if bill.time??>${bill.time?string('yyyy年MM月dd日 hh:mm:ss')}</#if>
    </dd>
  </dl>
  <dl>
	<dt>处理细节</dt>
    <dd>
        <div class="rule-single-select">
            <select name="statusId" id="statusId" datatype="*" sucmsg=" " nullmsg="请选择！" class="Validform_error" style="display: none;">
                <option value="2" >初始化状态</option>
				<option value="3" <#if bill.statusId?? && bill.statusId==2>selected="selected"</#if>>票据整理</option>
				<option value="4" <#if bill.statusId?? && bill.statusId==3>selected="selected"</#if>>财务处理</option>
				<option value="5" <#if bill.statusId?? && bill.statusId==4>selected="selected"</#if>>税费扣缴</option>
				<option value="6" <#if bill.statusId?? && bill.statusId==5>selected="selected"</#if>>财务状况表</option>
            </select>
        </div>
    </dd>
</dl>
<#if bill.finishtime??>
  <dl>
    <dt>上一次操作时间</dt>
    <dd>
        ${bill.finishtime?string('yyyy年MM月dd日 hh:mm:ss')}
    </dd>
  </dl>
 </#if> 
   <dl>
       <dt>备注</dt>
       <dd>
           <textarea name="remark"  rows="2" cols="20" id="remark" class="input" datatype="*0-255" sucmsg=" "><#if bill??>${bill.remark!""}</#if></textarea>
       </dd>
   </dl>  

</div>
<!--/内容-->


<!--工具栏-->
<div class="page-footer">
  <div class="btn-list">
    <input type="submit" name="btnSubmit" value="提交保存" id="btnSubmit" onclick="billDeal();" class="btn">
    <input name="btnReturn" type="button" value="返回上一页" class="btn yellow" onclick="javascript:history.back(-1);">
  </div>
  <div class="clear"></div>
</div>
<!--/工具栏-->


</body></html>