package com.schoolke.dao;

import com.schoolke.bean.Goods;
import com.schoolke.bean.Message;
import com.schoolke.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Bragg Troy on 2017/3/30.
 */
public class MessageDao extends BaseDao {
    // 插入新的留言
    public int publishNewMsg(Message message){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("INSERT INTO message(msg,msgTime,userId,goodsId)\n" +
                    "VALUES(?,?,?,?)");
            psmt.setString(1,message.getMsg());
            psmt.setString(2,message.getMsgTime());
            psmt.setInt(3,message.getUser().getId());
            psmt.setInt(4,message.getGoods().getId());
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("插入新的留言异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return val;
    }

    // 通过商品id获取其留言信息
    public ArrayList<Message> getMsgByGoodsId(int goodsId){
        ArrayList<Message> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT m.id,m.msg,m.msgTime,u.id,u.userName,u.head FROM message AS m INNER JOIN user AS u\n" +
                    "WHERE m.userId=u.id AND m.goodsId = ? ORDER BY m.msgTime DESC");
            psmt.setInt(1,goodsId);
            rs = psmt.executeQuery();

            while (rs.next()){
                Message msg = new Message();
                msg.setId(rs.getInt(1));
                msg.setMsg(rs.getString(2));
                msg.setMsgTime(rs.getString(3));
                User user = new User();
                user.setId(rs.getInt(4));
                user.setUserName(rs.getString(5));
                user.setHeader(rs.getString(6));

                msg.setUser(user);

                arr.add(msg);
            }

        }catch (Exception ex){
            System.out.print("获取商品留言异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return  arr;
    }

    //    通过userID获取关于该用户的留言信息
    public ArrayList<Message> getMessagesAboutMe(int userId){
        ArrayList<Message> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT * FROM message m \n" +
                "WHERE m.goodsId IN(SELECT goods.id FROM goods WHERE goods.userId = ?)");
            psmt.setInt(1,userId);
            rs = psmt.executeQuery();
            while (rs.next()){
                Message message = new Message();
                message.setId(rs.getInt(1));
                message.setMsg(rs.getString(2));
                message.setMsgTime(rs.getString(3));
                User user = new UserDao().getUserInfoById(rs.getInt(4));
                Goods goods = new GoodsDao().getGoodsById(rs.getInt(5));
                message.setUser(user);
                message.setGoods(goods);
                arr.add(message);
            }
        }catch (Exception ex){
            System.out.print("关于我的留言异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return arr;
    }

    // 获取用户已发布的留言
    public ArrayList<Message> getMessagesMy(int userId){
        ArrayList<Message> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT m.id,m.msg,m.msgTime,m.userId,m.goodsId,g.id,g.goodsName\n" +
                "FROM message m INNER JOIN goods g\n" +
                "WHERE m.goodsId = g.id AND m.userId = ?");
            psmt.setInt(1,userId);
            rs = psmt.executeQuery();
            while (rs.next()){
                Message message = new Message();
                message.setId(rs.getInt(1));
                message.setMsg(rs.getString(2));
                message.setMsgTime(rs.getString(3));
                Goods goods = new Goods();
                goods.setId(rs.getInt(6));
                goods.setGoodsName(rs.getString(7));
                message.setGoods(goods);
                arr.add(message);
            }

        }catch (Exception ex){
            System.out.print("获取用户已发布的留言异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }

    // 删除我的已发布留言
    public int deleteMsg(int msgId){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("DELETE FROM message WHERE message.id = ?");
            psmt.setInt(1,msgId);
            val = psmt.executeUpdate();
            return val;
        }catch (Exception ex){
            System.out.print("删除异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return val;
    }

}


