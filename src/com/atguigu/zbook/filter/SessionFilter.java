package com.atguigu.zbook.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Neo
 * @version 1.0
 */
/*@WebFilter(urlPatterns = {"*.do","*.html"},initParams = {
        @WebInitParam(name = "access", value = "/web24/page.do?operate=page&page=user/login," +
                "/web24/user.do?null,/web24/page.do?operate=page&page=user/regist")
})*/
public class SessionFilter implements Filter {
    List<String> accessList = null;
    @Override
    public void init(FilterConfig config) throws ServletException {
        String accessStr = config.getInitParameter("access");
        String[] accessArr = accessStr.split(",");
        accessList = Arrays.asList(accessArr);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String uri = request.getRequestURI();
        String query = request.getQueryString();
        System.out.println("getRequestURI : " + uri);
        System.out.println("getQueryString : " + query);

        String str = uri + "?" + query;
        if (!accessList.contains(str)){
            HttpSession session = request.getSession();
            Object currUser = session.getAttribute("currUser");
            if (currUser == null){
                response.sendRedirect("page.do?operate=page&page=user/login");
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
