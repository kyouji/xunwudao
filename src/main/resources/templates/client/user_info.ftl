<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if site??>${site.seoKeywords!''}</#if>">
<meta name="description" content="<#if site??>${site.seoDescription!''}</#if>">
<meta name="copyright" content="<#if site??>${site.copyright!''}</#if>" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>循伍道</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/client/js/index.js"></script>
	<script type="text/javascript">
		
		function formSubmit()
		{
			form = document.forms["upload"];
			form.submit();
		}
		
		function headChange()
		{
			$("#file").click();
		}
		
		function infoSubmit(){
			var realName = $("#realName").val();
			//var headImageUrl = $("#file").val();
			var address = $("#address").val();
			var mobile = $("#mobile").val();
			var sex =$("#sex").val(); 
		
			  $.ajax({
			      type:"post",
			      url:"/user/info/submit",
			      data:{"realName":realName,
						    "sex":sex,
						    "mobile":mobile,
						    "address":address},
			      success:function(data){
					if (data.code == 1)
					{
			            alert(data.msg);
			            if(data.login==1)
			            {
			            	location.href='/login';
			            }
					}
					else{
						alert("修改成功！");
						location.href='/user/center';
					}
			      }
			  });
		}
	</script>
</head>
<body>
<form id="upload" name="upload" enctype="multipart/form-data" action="/client/userHead/upload" method="post">
	<input type="hidden" id="id" name="id" value="<#if user??>${user.id?c!''}</#if>"></input>
	<input id="file" style="display:none;" class="area_save_btn" name="Filedata"  type="file" value="<#if user??>${user.headImageUrl!''}</#if>" onchange="javascript:formSubmit();"/>
  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="/user/center"></a>
    <p class="c333">编辑个人资料</p>
    <a class="btn-registered c333" href="javascript:infoSubmit();">完成</a>
  </header>
  <!-- 头部 END -->

  <!-- 个人信息 -->
  <article class="personal-message">
    <section class="left">
      <div class="img">
        <img  src="<#if user??&&user.headImageUrl??&&user.headImageUrl?length gt 0>${user.headImageUrl}<#else>/client/images/default.jpg</#if>" 
        		onclick="javascript:headChange();"alt="头像">
      </div>
      <a class="edit" href="javascript:headChange();">编辑</a>
    </section>
    <section class="right">
      <div class="inp">
        <label>姓名：</label>
        <input type="text" name="realName" id="realName" value="<#if user??>${user.realName!''}</#if>">
      </div>
      <div class="inp">
        <label>性别：</label>
        <select id="sex" name="sex">
          <option value="true" <#if user??&&!user.sex?? || user??&&user.sex??&&user.sex>selected="selected"</#if>>男</option>
          <option value="false"<#if user??&&user.sex??&&!user.sex>selected="selected"</#if>>女</option>
        </select>
      </div>
      <div class="inp">
        <label>电话：</label>
        <input type="tel" id="mobile" name="mobile" value="<#if user??>${user.mobile!''}</#if>">
      </div>
      <div class="inp">
        <label>地址：</label>
        <input type="text" name="address" id="address" value="<#if user??>${user.address!''}</#if>">
      </div>
    </section>
  </article>
  <!-- 个人信息 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->
</form>
</body>
</html>