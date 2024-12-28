package com.hjy.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.hjy.reggie.common.BaseContext;
import com.hjy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//检查用户是否已登录
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher antPathMatcher = new AntPathMatcher();  //路径匹配器，支持通配符
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);
        //定义不需要处理的路径
        String[] urls= new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //如果不需要处理，直接放行
        if(check){
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //如果已登录，直接放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录,id为{}", request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            Long id = Thread.currentThread().getId();
            log.info("线程id={}",id);
            filterChain.doFilter(request, response);
            return;
        }
        //如果未登录，返回未登录，输出流方法
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
        //log.info("拦截到请求：{}", request.getRequestURI());
    }
    // 路径匹配，检查本次请求是否需要放行
    public boolean check(String[] urls ,String requestURI){
        for (String url : urls) {
            boolean match = antPathMatcher.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
