package com.schoolke.dao;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class BaseDao {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/schoolke_database_cs?characterEncoding=utf-8&useUnicode=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public Connection getConnection(){
        Connection con = null;
        try{
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL , USERNAME , PASSWORD);
        }
        catch(Exception ex){
            System.out.println("创建数据库连接出错：" + ex.getMessage());
        }
        return con;
    }


    public void closeAll(ResultSet rs , PreparedStatement psmt , Connection con){
        if(null != rs){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(null != psmt){
            try {
                psmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(null != con){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
