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
<style>
.change-mobile{position: absolute;
  right: 2%;
  top: 15%;
  z-index: 2;
  width: 48px;
  height: 30px;
  line-height: 30px;
  color: #fff;
  text-align: center;
  background-color: #72d377;
  border-radius: 3px;}
  
  .remove-it{
  margin-top:2%;
  z-index: 2;
  width: 48px;
  height: 30px;
  line-height: 30px;
  color: #fff;
  text-align: center;
  background-color: #72d377;
  border-radius: 3px;}
</style>
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
			var sex =$("#sex").val(); 
			var password = $("#password").val();
			var username = $("#username").val();
			var nickname = $("#nickname").val();
		
			  $.ajax({
			      type:"post",
			      url:"/user/info/submit",
			      data:{"realName":realName,
						    "sex":sex,
						    "password":password,
						    "nickname":nickname,
						    "username":username,
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
		
		function removeWX(){
			if(confirm("解除后将不能通过该微信登陆，确认吗？")){
				$.ajax({
			      type:"post",
			      url:"/user/remove/weixin",
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
						alert("解除成功！");
					}
			      }
			  });
			}
		}
		
		function removeQQ(){
			if(confirm("解除后将不能通过该QQ登陆，确认吗？")){
				$.ajax({
			      type:"post",
			      url:"/user/remove/qq",
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
						alert("解除成功！");
					}
			      }
			  });
			}
		}		
	</script>
</head>
<body>
<form id="upload" name="upload" enctype="multipart/form-data" action="/client/userHead/upload" method="post">
	<input type="hidden" id="id" name="id" value="<#if user??>${user.id?c!''}</#if>"></input>
	<input id="file" style="display:none;" class="area_save_btn" name="Filedata" capture="camera"  type="file" value="<#if user??>${user.headImageUrl!''}</#if>" onchange="javascript:formSubmit();"/>
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
        <label>昵称：</label>
        <input type="text" name="nickname" id="nickname" value="<#if user??>${user.nickname!''}</#if>">
      </div>
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
        <span style="color:#999999;" id="user_mobile_aru"><#if user??>${user.mobile!''}</#if></span>
        <a class="change-mobile" id="smsCodeBtn" href="/user/change/mobile">修改</a>
      </div>
      <div class="inp">
        <label>区域：</label>
        <select name="areaId" id="areaId" class="nece">
            <option value=''>请选择区域</option>
            <#if area_list??>
            	<#list area_list as item>
            		<option value="${item.id?c}" <#if user?? && user.areaId?? && user.areaId == item.id>selected="selected"</#if> >${item.title!''}</option>
            	</#list>
            </#if>		
          </select>
      </div>
      <div class="inp">
        <label>地址：</label>
        <input type="text" name="address" id="address" value="<#if user??>${user.address!''}</#if>">
      </div>
      <div class="inp">
        <label>账号：</label>
        <input type="text" name="username" id="username" value="<#if user??>${user.username!''}</#if>">
      </div>
      <div class="inp">
        <label>密码：</label>
        <input type="text" name="password" id="password" value="<#if user??>${user.password!''}</#if>">
      </div>
      <div class="inp">
	      <#if user.openid?? && user.openid?length gt 0>
	      	<input class="remove-it" type="button" onclick="javascript:removeWX();" value="解绑微信">
	      </#if>
	      <#if user.qqOpenid?? && user.qqOpenid?length gt 0>
	      	<input class="remove-it" type="button" onclick="javascript:removeQQ();" value="解绑QQ">
		  </#if>
	  </div>
    </section>
  </article>
  <!-- 个人信息 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
<script>
  $(document).ready(function(){
			var mobileAru = $("#user_mobile_aru").html();
			if(mobileAru == ""){
				if(confirm("您尚未绑定手机号，现在绑定吗？")){
					location.href="/user/change/mobile";
				}
			}
		});
</script>		
  <!-- 底部 END -->
</form>
</body>
</html>