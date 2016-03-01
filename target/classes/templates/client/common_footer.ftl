    <script>
    $(document).ready(function(){
        $(".nav").click(function(){
	    	$(".nav").removeClass("current-1");
	    	$(this).addClass("current-1");
	    });
    });

    </script>
    
    <a class="nav a1 <#if !showIcon?? || showIcon?? && showIcon=1>current-1</#if>" href="/index">首页</a>
    <a class="nav a2 <#if showIcon?? && showIcon=2>current-1</#if>" href="/goods/index">商城</a>
    <a class="nav a3 <#if showIcon?? && showIcon=3>current-1</#if>" href="/cart">购物车</a>
    <a class="nav a4 <#if showIcon?? && showIcon=4>current-1</#if>" href="/user/center">个人中心</a>
    <a id="btn-inft-more" class="nav a5 <#if showIcon?? && showIcon=5>current-1</#if>">更多</a>
    <div class="hide-nav">
      <a class="ab-us" href="/info/aboutUs">关于我们</a>
      <a class="opinion" href="/info/suggestion">意见反馈</a>
      <#if site??&&site.telephone??&&site.telephone?length gt 0><a class="opinion" href="tel:${site.telephone!''}">联系我们</a></#if>
    </div>