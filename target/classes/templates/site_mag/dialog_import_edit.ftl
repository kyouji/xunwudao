<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>商品挑选</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<script type="text/javascript" src="/mag/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js?skin=idialog"></script>
<script type="text/javascript" src="/mag/js/WdatePicker.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.queue.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.handlers.js"></script>
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
<link href="/mag/style/pagination.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
$(document).ready(function(){
            //初始化上传控件
        $(".upload-img").each(function () {
            $(this).InitSWFUpload({ 
                sendurl: "/Verwalter/importUpload", 
                flashurl: "/mag/js/swfupload.swf",
                filetypes: "*.*;" 
            });
        });
});


    //窗口API
    var api = frameElement.api, W = api.opener;
    api.button({
        name: '导入',
        focus: true,
        callback: function () {
            importFile();
            return false;
        }
    }, {
        name: '取消'
    });
    
//导入表格
function importFile()
{
	var fileUrl = document.getElementById("fileUrl").value;
	var time = document.getElementById("import_time").value;
	
	  $.ajax({
	      type:"post",
	      url:"/Verwalter/import/pay/submit",
	      data:{"fileUrl":fileUrl,
	    	  		"time":time},
	      success:function(data){
			if (data.code == 1)
			{
	            var dialog = $.dialog.alert(data.msg);
			}
			else{
				api.close();
				var dialog = $.dialog.confirm(
					"导入成功！要向微信推送消息吗？",
					function(){location.href='/Verwalter/user/pay/list?time=${time!''}';},
					function(){location.href='/Verwalter/user/pay/list?time='+time;}
				);
			}
	      }
	  });
}


   
</script>
</head>

<body>
<div class="div-content">
        <dl>
         <dt>发布月份</dt>
         <dd>
             <div class="input-date" style="width: 240px;">
                 <input name="import_time" type="text" id="import_time" value="<#if time??>${time!''}<#elseif date??>${date?string("yyyy-MM")}</#if>" class="input date" onfocus="WdatePicker({dateFmt:'yyyy-MM',lang:'zh-cn'})" datatype="/^\s*$|^\d{4}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" "  onchange="javascript:selectDate(this.value);">
                 <i  style="right: 80px;">日期</i>
             </div>
             <#--<span class="Validform_checktip">不选择默认当前月份</span>-->
         </dd>
        </dl>

        <dl>
            <dt>excel文件</dt>
            <dd>
                <input name="fileUrl"   type="text" id="fileUrl" value="" class="input normal upload-path">
                <div class="upload-box upload-img"></div>
            </dd>
        </dl>    
    
</div>

</body>
</html>