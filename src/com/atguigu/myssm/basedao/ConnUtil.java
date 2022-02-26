package com.atguigu.myssm.basedao;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Neo
 * @version 1.0
 */
public class ConnUtil {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    private  static   Properties properties = new Properties();
    static {
        try {
            InputStream inputStream = ConnUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConn(){

        Connection conn = threadLocal.get();
        if (conn == null){
            conn = ConnUtil.createConn();
            threadLocal.set(conn);
        }
        return threadLocal.get();
    }

    private static Connection createConn(){
        try {
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static void closeConn() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn == null){
            return;
        }
        if (!conn.isClosed()){
            conn.close();
            threadLocal.set(null);
        }
    }
}
