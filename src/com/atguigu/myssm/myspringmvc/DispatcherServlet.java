package com.atguigu.myssm.myspringmvc;

import com.atguigu.myssm.ioc.BeanFactory;
import com.atguigu.myssm.utils.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Neo
 * @version 1.0
 */
@WebServlet("*.do")
@SuppressWarnings("all")
public class DispatcherServlet extends ViewBaseServlet {
    private BeanFactory beanFactory = null;

    @Override
    public void init() throws ServletException {
        super.init();
        //beanFactory = new ClassPathXmlApplicationContext();
        ServletContext servletContext = getServletContext();
        Object beanFactoryObj = servletContext.getAttribute("beanFactory");
        if (beanFactoryObj == null) {
            throw new RuntimeException("IOC容器配置文件有误....");
        }
        beanFactory = (BeanFactory) beanFactoryObj;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
        String servletPath = request.getServletPath();//   /fruit.do
        //字符串处理
        //   /fruit.do  -- 》 fruit.do  -- > fruit
        servletPath = servletPath.substring(1); //  fruit.do
        int index = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, index);  // fruit
        //
        Object controllerBeanObj = beanFactory.getBean(servletPath);
        //根据operate的值处理请求
        String operate = request.getParameter("operate");
        if (StringUtils.isEmpty(operate)) {
            operate = "index";
        }

        //通过反射获取FruitServlet的方法
        try {
//            Method method = controllerBeanObj.getClass().getDeclaredMethod(operate,HttpServletRequest.class,HttpServletResponse.class);
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (operate.equals(method.getName())) {
                    //1.统一获取请求参数
                    //1-1.获取当前方法的参数，返回参数数组
                    Parameter[] parameters = method.getParameters();
                    //1-2.parameterValues 用来承载参数的值
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];//parameter: java.lang.String oper
                        String parameterName = parameter.getName();//parameterName: oper
                        //如果参数名是request,response,session 那么就不是通过请求中获取参数的方式
                        if ("request".equals(parameterName)) {
                            parameterValues[i] = request;
                        } else if ("response".equals(parameterName)) {
                            parameterValues[i] = response;
                        } else if ("session".equals(parameterName)) {
                            parameterValues[i] = request.getSession();
                        } else {
                            //从请求中获取参数值
                            String parameterValue = request.getParameter(parameterName);
                            String typeName = parameter.getType().getName();

                            Object parameterObj = parameterValue;

                            if (parameterObj != null) {
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }
                            parameterValues[i] = parameterObj;
                        }
                    }
                    //2.controller组件中的方法调用
                    method.setAccessible(true);
                    Object methodReturnObj = method.invoke(controllerBeanObj, parameterValues);
                    //3.视图处理
                    String methodReturnStr = (String) methodReturnObj;
                    if (StringUtils.isEmpty(methodReturnStr)) {
                        return;
                    }
                    if (methodReturnStr.startsWith("redirect:")) {
                        String redirectStr = methodReturnStr.substring("redirect:".length());//fruit.do
                        response.sendRedirect(redirectStr);
                    } else if (methodReturnStr.startsWith("json:")) {
                        String jsonStr = methodReturnStr.substring("json:".length());
                        PrintWriter out = response.getWriter();
                        out.print(jsonStr);
                        out.flush();
                    } else {
                        super.processTemplate(methodReturnStr, request, response);// 比如 edit
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DispatcherException("DispatcherServlet出错了....");
        }
    }
}
