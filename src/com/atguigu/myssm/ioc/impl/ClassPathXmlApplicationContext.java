package com.atguigu.myssm.ioc.impl;

import com.atguigu.myssm.ioc.BeanFactory;
import com.atguigu.myssm.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Neo
 * @version 1.0
 */
public class ClassPathXmlApplicationContext implements BeanFactory {
    private Map<String,Object> beanMap = new HashMap<>();

    public ClassPathXmlApplicationContext() {
        this("applicationContext.xml");
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }

    public ClassPathXmlApplicationContext(String path){
        System.out.println("ClassPathXmlApplicationContext 带参构造器");
        if (StringUtils.isEmpty(path)){
            throw new RuntimeException("IOC容器的配置文件没有指定....");
        }
        try {
            // 配置文件 将servletPath （fruit） 与 fruitController 关联起来
            //读取配件文件
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            //1.创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //3.创建Document对象
            Document document = documentBuilder.parse(inputStream);

            //4.获取所有的bean节点下的id值和class值，通过反射创建对象，将id和对象存入beanMap容器中
            //在DispatcherServlet中通过该容器获取对象并调用方法
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    //获取id值 --- > fruit
                    String beanId = beanElement.getAttribute("id");
                    //获取class值 --- > com.atguigu.fruit.servlets.FruitServlet
                    String className = beanElement.getAttribute("class");
                    //通过反射创建该类对象
                    Class BeanClass = Class.forName(className);
                    Object beanObj = BeanClass.newInstance();
                    //将配置文件中<bean>标签中的id，和class值创建的对象放入到map集合中
                    beanMap.put(beanId, beanObj);
                }
            }
            // 组装bean之间依赖关系
            //读取bean下的所有 property子节点 获取到name值和ref值，根据refId从beanMap集合中获取到需要依赖的对象，
            // 然后根据当前beanId获取到当前对象,通过反射给当前对象的属性赋值(将需要依赖的对象实例赋值给该属性)
            //
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    NodeList beanChildNodesList = beanElement.getChildNodes();
                    for (int j = 0; j < beanChildNodesList.getLength(); j++) {
                        Node beanChildNode = beanChildNodesList.item(j);
                        if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(beanChildNode.getNodeName())){
                            Element propertyElement = (Element) beanChildNode;
                            String propertyName = propertyElement.getAttribute("name");
                            String propertyRef = propertyElement.getAttribute("ref");
                            Object refObj = beanMap.get(propertyRef);
                            //将refObj 设置到当前bean对应的实例中的property属性上去
                            Object beanObj = beanMap.get(beanId);
                            Class beanClazz = beanObj.getClass();
                            Field propertyField = beanClazz.getDeclaredField(propertyName);
                            propertyField.setAccessible(true);
                            propertyField.set(beanObj,refObj);
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


}
