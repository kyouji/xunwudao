<div class="line20"></div>
<div class="pagelist">
  <div class="l-btns">
    <span>显示</span><input name="size" type="text" value="${size!""}" onchange="javascript:setTimeout(__doPostBack('btnSize',''), 0)" onkeypress="if (WebForm_TextBoxKeyHandler(event) == false) return false;" class="pagenum" onkeydown="return checkNumber(event);"><span>条/页</span>
    <input name="page" type="hidden" value="${PAGE_DATA.number!'0'}" />
  </div>
  <div id="PageContent" class="default"><span>共<#if PAGE_DATA??>${PAGE_DATA.totalElements}</#if>数据,分<#if PAGE_DATA??>${PAGE_DATA.totalPages}</#if>页显示,当前为第<#if PAGE_DATA??>${PAGE_DATA.number+1}</#if>页</span>
        <#if PAGE_DATA??>
            <#assign continueEnter=false>
            <#if PAGE_DATA.number+1 == 1>
                <span class="disabled">&lt;&lt;上一页</span>
            <#else>
                <a href="javascript:__doPostBack('btnPage','${PAGE_DATA.number-1}')">&lt;&lt;上一页</a>
            </#if>
            
            <#if PAGE_DATA.totalPages gt 0>
                <#list 1..PAGE_DATA.totalPages as page>
                    <#if page <= 3 || (PAGE_DATA.totalPages-page) < 3 || (PAGE_DATA.number+1-page)?abs<3 >
                        <#if page == PAGE_DATA.number+1>
                            <span class="current">${page}</span>
                        <#else>
                            <a href="javascript:__doPostBack('btnPage','${page-1}')">${page}</a>
                        </#if>
                        <#assign continueEnter=false>
                    <#else>
                        <#if !continueEnter>
                            ...
                            <#assign continueEnter=true>
                        </#if>
                    </#if>
                </#list>
            </#if>
            
            <#if PAGE_DATA.number+1 == PAGE_DATA.totalPages || PAGE_DATA.totalPages==0>
                <span class="disabled">下一页&gt;&gt;</span>
            <#else>
                <a href="javascript:__doPostBack('btnPage','${PAGE_DATA.number+1}')">下一页&gt;&gt;</a>
            </#if>
        </#if>
  </div>
</div>