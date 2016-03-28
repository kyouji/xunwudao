    <script>
    $(document).ready(function(){
    	var showIcon = ${showIcon!''};
        $(".nav").click(function(showIcon){
        	for(var i = 1;i<6;i++){
	    		$(".nav").removeClass("current-"+i);
	    	}
	    	
	    	$(this).addClass("current-"+showIcon);
	    });
	    
	    var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
		}else{
			$("#call_us").attr("href","/info/aboutUs");
		}
    });

    </script>
    
    <a show-id="1" class="nav a1 <#if !showIcon?? || showIcon?? && showIcon=1>current-1</#if>" href="/index">首页</a>
    <a show-id="2" class="nav a2 <#if showIcon?? && showIcon=2>current-2</#if>" href="/goods/index">商城</a>
    <a show-id="3" class="nav a3 <#if showIcon?? && showIcon=3>current-3</#if>" href="/cart">购物车</a>
    <a show-id="4" class="nav a4 <#if showIcon?? && showIcon=4>current-4</#if>" href="/user/center">个人中心</a>
    <a show-id="5" id="btn-inft-more" class="nav a5 <#if showIcon?? && showIcon=5>current-5</#if>">更多</a>
    <div class="hide-nav">
      <a class="ab-us" href="/info/aboutUs">关于我们</a>
      <a class="opinion" href="/info/suggestion">意见反馈</a>
      <#if site??&&site.telephone??&&site.telephone?length gt 0><a id="call_us" class="opinion" href="tel:${site.telephone!''}">联系我们</a></#if>
    </div>