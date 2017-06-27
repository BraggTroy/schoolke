package com.schoolke.dao;

import com.schoolke.bean.Admin;
import com.schoolke.bean.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Bragg Troy on 2017/3/22.
 */
public class UserDao extends BaseDao {
    /*用户登录*/
    public User userLogin(String name,String pwd){
        User user = new User();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT * FROM user WHERE userName = ? AND password = ?");
            psmt.setString(1,name);
            psmt.setString(2,pwd);
            rs = psmt.executeQuery();
            if(rs.last()){
                user.setId(rs.getInt(1));
                user.setUserName(rs.getString(2));
                user.setPassword("");
                user.setContact(rs.getString(4));
                user.setSchool(rs.getString(5));
                user.setPersonality(rs.getString(6));
                user.setRegistrationDate(rs.getString(7));
                user.setHeader(rs.getString(8));
                return user;
            }

        }catch (Exception ex){
            System.out.println("用户登录异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return null;
    }

    /*通过ID获取用户信息*/
    public User getUserInfoById(int id){
        User user = new User();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT id,userName,password,contact,school,personality,registrationDate,head\n" +
                    "FROM user WHERE user.id = ?");
            psmt.setInt(1,id);
            rs = psmt.executeQuery();
            if(rs.last()){
                user.setId(rs.getInt(1));
                user.setUserName(rs.getString(2));
                user.setContact(rs.getString(4));
                user.setSchool(rs.getString(5));
                user.setPersonality(rs.getString(6));
                user.setRegistrationDate(rs.getString(7));
                user.setHeader(rs.getString(8));

                return user;
            }

        }catch (Exception ex){
            System.out.print("由ID获取用户信息异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return null;
    }


    /*更新保存用户信息*/
    public int UpdateUserInfo(User user){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("UPDATE user \n" +
                    "SET userName=?,contact=?,school=?,personality=?,head=? \n" +
                    "WHERE id = ?");
            psmt.setString(1,user.getUserName());
            psmt.setString(2,user.getContact());
            psmt.setString(3,user.getSchool());
            psmt.setString(4,user.getPersonality());
            psmt.setString(5,user.getHeader());
            psmt.setInt(6,user.getId());

            val = psmt.executeUpdate();


        }catch (Exception ex){
            System.out.print("更新保存用户信息异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return val;
    }

    // 查询用户名是否存在
    public int userNameIsExist(String name){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT userName FROM user WHERE userName = ?");
            psmt.setString(1,name);
            rs = psmt.executeQuery();
            if(rs.last()){
                val = 1;
            }
        }catch (Exception ex){
            System.out.print("查询用户名异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return val;
    }

    /*注册新用户*/
    public int registerNewUser(User user){

        int val = 0;
        int userId = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("INSERT INTO user (userName,password,contact,school,registrationDate)\n" +
                    "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            psmt.setString(1,user.getUserName());
            psmt.setString(2,user.getPassword());
            psmt.setString(3,user.getContact());
            psmt.setString(4,user.getSchool());
            psmt.setString(5,user.getRegistrationDate());

            val = psmt.executeUpdate();
            if(val == 1){
                try {
                    rs = psmt.getGeneratedKeys();
                    System.out.println(rs);

                    if(rs.next()){
                        userId = rs.getInt(1);
                        System.out.println(userId);
                    }
                }catch (Exception e){
                    System.out.print("返回userId异常"+e.getMessage());
                }
            }

        }catch (Exception ex){
            System.out.print("注册异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return userId;
    }

    // 用户修改密码
    public int changePassword(int id, String newPassword){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("UPDATE user set password = ? WHERE id=?");
            psmt.setString(1,newPassword);
            psmt.setInt(2,id);
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("修改密码出错"+ex.getMessage());
        }finally {
            super.closeAll(null,psmt,con);
        }

        return val;
    }

    // 管理员登录
    public Admin AdminLogin(String name, String password){
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT id,name,password,level FROM admin WHERE name = ? AND password = ?");
            psmt.setString(1,name);
            psmt.setString(2,password);
            rs = psmt.executeQuery();
            if(rs.last()){
                Admin admin = new Admin();
                admin.setId(rs.getInt(1));
                admin.setName(rs.getString(2));
                admin.setPassword("");
                admin.setLevel(rs.getInt(4));
                return admin;
            }

        }catch (Exception ex){
            System.out.print("管理员登录异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return null;
    }

    // 统计用户
    public ArrayList<User> getUserInfo(){
        ArrayList<User> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT id,userName,head,registrationDate FROM user ORDER BY id DESC");

            rs= psmt.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUserName(rs.getString(2));
                user.setHeader(rs.getString(3));
                user.setRegistrationDate(rs.getString(4));
                arr.add(user);
            }
        }catch (Exception ex){
            System.out.print("统计用户异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }

}
