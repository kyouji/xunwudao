<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>票据整理</title>
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


<style>
.apply_content dd div .hide{display:none;}
.apply_content dd .hide{display:none;}
.apply_content dt .hide{display:none;}
.apply_content .hide{display:none;}

.tab-content dl dd span{color: #333;font-weight:normal;}


</style>
<script>
$(document).ready(function(){

    $("#bill_deal").Validform({
            tiptype:4,
            ajaxPost:true,
            callback: function (data) { 
                if (data.code == 0)
                {
                	var dialog = $.dialog.alert('保存成功！', 
                            function(){location.href='/Verwalter/bill/gather/list';});
                }
                else 
                {
                    $.dialog.alert(data.msg);
                    if (data.check == 0)
                    {
                        location.href='/Verwalter/login';
                    }
                }
             }
    });
    
            //初始化上传控件
        $(".upload-img").each(function () {
            $(this).InitSWFUpload({ 
                sendurl: "/Verwalter/importUpload", 
                flashurl: "/mag/js/swfupload.swf",
                filetypes: "*.*;" 
            });
        });
});

function showEnter(){
    $(".enter").css("display","block");
    $(".pro").css("display","none");
}
function showPro(){
    $(".pro").css("display","block");
    $(".enter").css("display","none");
}

function forbidsubmit()
{
    $("#submitbutton").attr("disabled",true);
    $("#submitbutton").css("background","#666666");
}

function allowsubmit()
{
    $("#submitbutton").removeAttr("disabled");
    $("#submitbutton").css("background","#e67817");
}

function selectDate(time)
{
	<#--location.href='/Verwalter/bill/deal/${bill.id?c}?statusId=3&time='+time;-->
	location.href='/Verwalter/bill/gather/deal/<#if gather??>${gather.id?c}</#if>?time='+time;
}

//导入表格
function importFile()
{
	var fileUrl = document.getElementById("fileUrl").value;
	var time = document.getElementById("time").value;
	var userId = document.getElementById("userId").value;
	
	  $.ajax({
	      type:"post",
	      url:"/Verwalter/import/gather/submit",
	      data:{"fileUrl":fileUrl,
	    	  		"time":time,
	    	  		"userId":userId},
	      success:function(data){
			if (data.code == 1)
			{
	            alert(data.msg);
			}
			else{
				console.log(FormatDate(data.cell2));
				console.log(FormatDate(data.cell32));
				console.log(FormatDate(data.cell35));
				if(data.cell0 =="" || data.cell0 != <#if user??&&user.number??>${user.number!''}</#if>)
				{
					var dialog = $.dialog.alert(
						"用户编号不匹配，请确认要操作的用户编号。"
					);
				}
				else if(data.cell2 =="")
				{
					var date = FormatDate(data.cell2);
					if(date != time)
					{
						var dialog = $.dialog.alert(
							"月份不匹配，请确认月份。"
						);
					}

				}
				else{
				
					$("#generalAmount").val(data.cell3);
					$("#generalIncome").val(data.cell4);
					$("#generalTax").val(data.cell5);
					$("#specialAmount").val(data.cell6);
					$("#specialIncome").val(data.cell7);
					$("#specialTax").val(data.cell8);
					$("#noTicketIncome").val(data.cell9);
					$("#noTicketTax").val(data.cell10);
					$("#totalIncome").val(data.cell11);
					$("#totalTax").val(data.cell12);
					$("#taxRetention").val(data.cell13);
					$("#vat").val(data.cell14);
					$("#vatAmount").val(data.cell15);
					$("#transDeductionAmount").val(data.cell16);
					$("#transDeduction").val(data.cell17);
					$("#taxDeductionAmount").val(data.cell18);
					$("#taxDeduction").val(data.cell19);
					$("#totalVat").val(data.cell20);
					$("#inBillAmount").val(data.cell21);
					$("#inBill").val(data.cell22);
					$("#taxAdd").val(data.cell23);
					$("#taxBearing").val(data.cell24);
					$("#incomeTax").val(data.cell25);
					$("#urbanTax").val(data.cell26);
					$("#eduAdd").val(data.cell27);
					$("#eduAddLocal").val(data.cell28);
					$("#landTax").val(data.cell29);
					$("#deTodoAmount").val(data.cell30);
					$("#deTodo").val(data.cell31);
					$("#deTodoDate").val(data.cell32);
					$("#deDoneAmount").val(data.cell33);
					$("#deDone").val(data.cell34);
					$("#deDoneDate").val(data.cell35);
					$("#incomeTaxTodo").val(data.cell36);
					$("#content").val(data.cell37);
					
					var dialog = $.dialog.alert(
						"导入成功！请核对信息后提交保存"
						//function(){location.href='/Verwalter/bill/list/2';}
					);
				}


				
			}
	      }
	  });
}

//格式化日期
function FormatDate (strTime) {
    var date = new Date(strTime);
    return date.getFullYear()+"-"+(date.getMonth()+1);
}
</script>
</head>

<body class="mainbody">

<!--导航栏-->
<div class="location" style="position: static; top: 0px;">
  <a href="/Verwalter/bill/list/${statusId!""}"><i></i><span>返回列表页</span></a>
  <a href="/Verwalter/center" class="home"><i></i><span>首页</span></a>
  <i class="arrow"></i>
  <span>票据管理</span>
  <i class="arrow"></i>
    <span>票据整理</span>
</div>
<div class="line10"></div>
<!--/导航栏-->

<!--内容-->
<div class="content-tab-wrap">
  <div id="floatHead" class="content-tab" style="position: static; top: 52px;">
    <div class="content-tab-ul-wrap">
      <ul>
          <li><a href="javascript:;" onclick="tabs(this);" class="selected menu">基本资料</a></li>
           <li><a href="javascript:;" onclick="tabs(this);" class="">票据整理</a></li>
      </ul>
    </div>
  </div>
</div>


<!--安全设置-->
<form name="bill_deal" method="post" action="/Verwalter/bill/gather/save" id="bill_deal">

<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="${__VIEWSTATE!""}" >
<input type="hidden" name="userId" id="userId" value="<#if user??>${user.id?c!""}</#if>" >
<input type="hidden" name="id" value="<#if tdGather??>${tdGather.id?c!""}</#if>" >
<input type="hidden" name="billId" value="<#if bill??>${bill.id?c!""}</#if>" >
<div class="tab-content" style="display: block;">
        <dl>
         <dt>发布月份</dt>
         <dd>
             <div class="input-date" style="width: 240px;">
                 <input name="time" type="text" id="time" value="<#if time??>${time!''}<#elseif date??>${date?string("yyyy-MM")}</#if>" class="input date" onfocus="WdatePicker({dateFmt:'yyyy-MM',lang:'zh-cn'})" datatype="/^\s*$|^\d{4}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" "  onchange="javascript:selectDate(this.value);">
                 <i  style="right: 70px;">日期</i>
             </div>
             <#--<span class="Validform_checktip">不选择默认当前月份</span>-->
         </dd>
        </dl>
    <dl>
       <dt>用户名：</dt>
       <dd><#if user??>${user.username!''}</#if></dd>
    </dl>
    <dl>
       <dt>用户编号：</dt>
       <dd><#if user??>${user.number!''}</#if></dd>
    </dl>
    <#--    
    <dl>
    <dt>票据</dt>
    <dd>
        <img src="<#if bill.imgUrl??&&bill.imgUrl != "">/images/${bill.imgUrl!""} <#else>/client/images/foote22.png</#if>"  />
    </dd>
  </dl>
  <dl>
    <dt>票据下载</dt>
    <dd><a href="/download/data?name=${bill.imgUrl!''}">${bill.imgUrl!''}</a></dd>
  </dl>
  -->
    <dl>
        <dt>标题</dt>
        <dd>
            <input name="title" type="text" value="<#if tdGather??>${tdGather.title!""}</#if>" id="title" class="input normal" ignore="ignore" sucmsg=" ">
            <span class="Validform_checktip">*不填则默认显示：某月票据整理汇总如下</span>
        </dd>
    </dl>  
        <dl>
            <dt>导入表格</dt>
            <dd>
                <input name="fileUrl"   type="text" id="fileUrl" value="<#if article??>${article.imgUrl!""}</#if>" class="input normal upload-path">
                <div class="upload-box upload-img"></div>
                <div><input type="button" onclick="javascript:importFile();" value="导入"/></div>
            </dd>
        </dl>    
	<dl>
	    <dt>排序数字</dt>
	    <dd>
	        <input name="sortId" type="text" value="<#if tdGather??>${tdGather.sortId!""}<#else>99</#if>" id="txtSortId" class="input txt100" datatype="n" ignore="ignore" sucmsg=" ">
	        <span class="Validform_checktip">*数字，越小越向前</span>
	    </dd>
	</dl>  
 </div> 
 
  <!-- 票据整理 -->
<div class="tab-content" style="display: none;">
<a href="javascript:void(0)"  class="import-subtitle">收入</a>
  <dl>
  	<dt>本月普票</dt>
    <dd>
	    <input  name="generalAmount" id="generalAmount" type="text" value="<#if tdGather??&&tdGather.generalAmount??>${tdGather.generalAmount}</#if>" class="input normal" style="width:24px;color:#0083E4;text-align:center;"  datatype="n" ignore="ignore" sucmsg=" ">&nbsp;份，
	    <span>不含税收入</span>
	    <input name="generalIncome" id="generalIncome" type="text" value="<#if tdGather??&&tdGather.generalIncome??>${tdGather.generalIncome?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" errormsg="请输入最多2位小数" sucmsg=" ">&nbsp;元，
		 <#if user?? && user.enterTypeId==2>
		    <span>销项税</span>
		    <input name="generalTax" id="generalTax" type="text" value="<#if tdGather??&&tdGather.generalTax??>${tdGather.generalTax?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数" >&nbsp;元
    	</#if>
    </dd>
  </dl>
 <#if user?? && user.enterTypeId==2>
	  <dl>
	    <dt>本月专票</dt>
	    <dd>
		    <input name="specialAmount" id="specialAmount" type="text" value="<#if tdGather??>${tdGather.specialAmount!''}</#if>" class="input normal" style="width:24px;color:#0083E4;text-align:center;"  datatype="n" ignore="ignore" sucmsg=" "  >&nbsp;份，
		    <span>不含税收入</span>
		    <input name="specialIncome" id="specialIncome" type="text" value="<#if tdGather??&&tdGather.specialIncome??>${tdGather.specialIncome?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元，
		    <span>销项税</span>
		    <input name="specialTax" id="specialTax" type="text" value="<#if tdGather??&&tdGather.specialTax??>${tdGather.specialTax?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
	    </dd>
	  </dl>
	  <dl>
	    <dt>不开票收入</dt>
	    <dd>
		    <input name="noTicketIncome" id="noTicketIncome" type="text" value="<#if tdGather??&&tdGather.noTicketIncome??>${tdGather.noTicketIncome?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元，
		    <span>销项税</span>
		    <input name="noTicketTax" id="noTicketTax" type="text" value="<#if tdGather??&&tdGather.noTicketTax??>${tdGather.noTicketTax?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元，
	    </dd>
	  </dl>
	  <dl>
	    <dt>不含税收入合计</dt>
	    <dd>
	    	<input name="totalIncome" id="totalIncome" type="text" value="<#if tdGather??&&tdGather.totalIncome??>${tdGather.totalIncome?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
	    </dd>
	  </dl>
	  <dl>
	    <dt>销项税合计</dt>
	    <dd>
	    	<input name="totalTax" id="totalTax" type="text" value="<#if tdGather??&&tdGather.totalTax??>${tdGather.totalTax?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
	    </dd>
	  </dl>
  </#if>
 

    <!-- 进货 -->
<#if user?? && user.enterTypeId==2>
<a href="javascript:void(0)"  class="import-subtitle">进货</a>
	  <dl>
	    <dt>上月留抵税金</dt>
	    <dd>
	    	<input name="taxRetention" id="taxRetention" type="text" value="<#if tdGather??&&tdGather.taxRetention??>${tdGather.taxRetention?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
	    </dd>
	  </dl>
	  
	  <dl>
	    <dt>上月进项税额</dt>
	    <dd>
		    <input name="vatAmount" id="vatAmount" type="text" value="<#if tdGather??>${tdGather.vatAmount!''}</#if>" class="input normal" style="width:24px;color:#0083E4;text-align:center;"  datatype="n" ignore="ignore" sucmsg=" " >&nbsp;份，
		    <input name="vat" id="vat" type="text" value="<#if tdGather??&&tdGather.vat??>${tdGather.vat?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
		    <span class="Validform_checktip">上月待抵扣税金转入本月进项税额</span>
	    </dd>
	  </dl>
	
	  <dl>
	    <dt>运费抵扣进项税</dt>
	    <dd>
		    <input name="transDeductionAmount" id="transDeductionAmount" type="text" value="<#if tdGather??>${tdGather.transDeductionAmount!''}</#if>" class="input normal" style="width:24px;color:#0083E4;text-align:center;"  datatype="n" ignore="ignore" sucmsg=" " >&nbsp;份，
		    <input name="transDeduction" id="transDeduction" type="text" value="<#if tdGather??&&tdGather.transDeduction??>${tdGather.transDeduction?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
		    <span class="Validform_checktip">运费(含机动车)抵扣进项税</span>
	    </dd>
	  </dl>
	  
	  <dl>
	    <dt>本月增值税抵扣发票</dt>
	    <dd>
		    <input name="taxDeductionAmount" id="taxDeductionAmount" type="text" value="<#if tdGather??>${tdGather.taxDeductionAmount!''}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="n" ignore="ignore" sucmsg=" " >&nbsp;份，
		    <input name="taxDeduction" id="taxDeduction" type="text" value="<#if tdGather??&&tdGather.taxDeduction??>${tdGather.taxDeduction?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
	    </dd>
	  </dl>
	
	  <dl>
	    <dt>本月进项税额合计</dt>
	    <dd>
	    	<input name="totalVat" id="totalVat" type="text" value="<#if tdGather??&&tdGather.totalVat??>${tdGather.totalVat?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
	    </dd>
	  </dl>
<#elseif user?? && user.enterTypeId==1>
	<a href="javascript:void(0)"  class="import-subtitle">进货</a>
	  <dl>
	    <dt>本月进货发票</dt>
	    <dd>
		    <input name="inBillAmount" id="inBillAmount" type="text" value="<#if tdGather??>${tdGather.inBillAmount!''}</#if>" class="input normal" style="width:24px;color:#0083E4;text-align:center;"  datatype="n" ignore="ignore" sucmsg=" " >&nbsp;份，
		    <input name="inBill" id="inBill" type="text" value="<#if tdGather??&&tdGather.inBill??>${tdGather.inBill?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
		    <span class="Validform_checktip">小规模纳税人本月进货发票</span>
	    </dd>
	  </dl>

</#if>	
      <!-- 本月应纳税金 -->
	<a href="javascript:void(0)"  class="import-subtitle">本月应纳税金</a>
  <dl>
    <dt>增值税</dt>
    <dd>
	    <input name="taxAdd" id="taxAdd" type="text" value="<#if tdGather??>${tdGather.taxAdd?string("0.00")!''}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元，
	    <span>累计税负</span>
	    <input name="taxBearing" id="taxBearing"  type="text" value="<#if tdGather??&&tdGather.taxBearing??>${tdGather.taxBearing?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="*" sucmsg=" " >
    </dd>
  </dl>
  
  <dl>
    <dt>所得税</dt>
    <dd>
    	<input name="incomeTax" id="incomeTax" type="text" value="<#if tdGather??&&tdGather.incomeTax??>${tdGather.incomeTax?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
    </dd>
  </dl>
  <dl>
    <dt>城建税</dt>
    <dd>
	    <input name="urbanTax" id="urbanTax" type="text" value="<#if tdGather??&&tdGather.urbanTax??>${tdGather.urbanTax?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元，
	    <span>教育费附加</span>
	    <input name="eduAdd" id="eduAdd" type="text" value="<#if tdGather??&&tdGather.eduAdd??>${tdGather.eduAdd?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元，
	    <span>地方教育费附加</span>
	    <input name="eduAddLocal" id="eduAddLocal" type="text" value="<#if tdGather??&&tdGather.eduAddLocal??>${tdGather.eduAddLocal?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
    </dd>
  </dl>
  
  <dl>
    <dt>地税合计</dt>
    <dd>
    	<input name="landTax" id="landTax" type="text" value="<#if tdGather??&&tdGather.landTax??>${tdGather.landTax?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
    </dd>
  </dl>

<#if user?? && user.enterTypeId==2>
    <!-- 待抵扣-->

  <dl>
    <dt>未收抵扣联</dt>
    <dd>
	    <input name="deTodoAmount" id="deTodoAmount" type="text" value="<#if tdGather??>${tdGather.deTodoAmount!''}</#if>" class="input normal" style="width:24px;color:#0083E4;text-align:center;"  datatype="n" ignore="ignore" sucmsg=" " >&nbsp;份，
	    <span>税额</span>
	    <input name="deTodo" id="deTodo" type="text" value="<#if tdGather??&&tdGather.deTodo??>${tdGather.deTodo?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元，
	    <span>最早日期</span>
	     <div class="input-date">
	         <input name="deTodoDate" type="text" id="deTodoDate" value="<#if tdGather??&&tdGather.deTodoDate??>${tdGather.deTodoDate?string("yyyy-MM-dd")}</#if>" class="input date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',lang:'zh-cn'})" datatype="/^\s*$|^\d{4}\-\d{1,2}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" ">
	         <i>日期</i>
	     </div>
    </dd>
  </dl>
  
    <dl>
    <dt>已收抵扣联</dt>
    <dd>
	    <input name="deDoneAmount" id="deDoneAmount" type="text" value="<#if tdGather??>${tdGather.deDoneAmount!''}</#if>" class="input normal" style="width:24px;color:#0083E4;text-align:center;"  datatype="n" ignore="ignore" sucmsg=" " >&nbsp;份，
	    <span>税额</span>
	    <input name="deDone" id="deDone" type="text" value="<#if tdGather??&&tdGather.deDone??>${tdGather.deDone?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元，
	    <span>最早日期</span>
	     <div class="input-date">
	         <input name="deDoneDate" type="text" id="deDoneDate" value="<#if tdGather??&&tdGather.deDoneDate??>${tdGather.deDoneDate?string('yyyy-MM-dd')}</#if>" class="input date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',lang:'zh-cn'})" datatype="/^\s*$|^\d{4}\-\d{1,2}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" ">
	         <i>日期</i>
	     </div>
    </dd>
  </dl>
  
  <dl>
    <dt>本月应纳所得税</dt>
    <dd>
    <input name="incomeTaxTodo" id="incomeTaxTodo" type="text" value="<#if tdGather??&&tdGather.incomeTaxTodo??>${tdGather.incomeTaxTodo?string("0.00")}</#if>" class="input normal" style="width:64px;color:#E71212;text-align:center;"  datatype="/^(([1-9]\d*)|0)((\.\d{2})|(\.\d{1}))?$/" ignore="ignore" sucmsg=" " errormsg="请输入最多2位小数">&nbsp;元
    </dd>
  </dl>
 
</#if>
<!-- 补充说明-->
<a href="javascript:void(0)"  class="import-subtitle">补充说明</a>
    <dl>
        <dt>补充说明</dt>
        <dd>
            <textarea name="content" rows="2" cols="20" id="content" class="input" style="height:200px;width:450px;" datatype="*0-255" sucmsg=" "><#if tdGather??>${tdGather.content!""}</#if></textarea>
            <span class="Validform_checktip">显示给用户查看</span>
        </dd>
    </dl>

    <dl>
        <dt>备注</dt>
        <dd>
            <textarea name="remark" rows="2" cols="20" id="remark" class="input" style="height:350px;width:600px;" datatype="*0-1024" sucmsg=" "><#if tdGather??>${tdGather.remark!""}</#if></textarea>
            <span class="Validform_checktip">不显示给用户查看</span>
        </dd>
    </dl>    
</div>  

<!--导入表格 -->
<div class="tab-content" style="display: none;">

</div>
    

<!--/安全设置-->


   <!--工具栏-->
<div class="page-footer">
  <div class="btn-list">
    <input type="submit" name="btnSubmit" value="提交保存" id="btnSubmit" class="btn">
    <input name="btnReturn" type="button" value="返回上一页" class="btn yellow" onclick="javascript:history.back(-1);">
  </div>
  <div class="clear"></div>

</div>
<!--/工具栏-->
</div>



 </form>

</body></html>