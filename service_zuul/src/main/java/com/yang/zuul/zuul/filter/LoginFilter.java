package com.yang.zuul.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Component
public class LoginFilter extends ZuulFilter {
    /*
    * 指定过滤器的类型 pre route post error
    * */
    @Override
    public String filterType() {
        return "pre";
    }

    /*
    * 执行顺序 ，返回值越小 ，优先级越小
    * */
    @Override
    public int filterOrder() {
        return 10;
    }

    /*
    * 是否执行run方法 true 执行
    * */
    @Override
    public boolean shouldFilter() {
        return true;
    }
    /*
    * 执行拦截的业务逻辑
    * */
    @Override
    public Object run() throws ZuulException {
        //zuul网关下的上下文环境
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = request.getParameter("token");
        //没有登陆就拦截
        if(StringUtils.isBlank(token)){
            //不再转发请求
            context.setSendZuulResponse(false);
            //设置响应状态吗
            context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            //设置响应信息
            context.setResponseBody("request err");
        }
        //什么都不干
        return null;
    }
}
