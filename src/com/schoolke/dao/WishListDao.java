package com.schoolke.dao;

import com.schoolke.bean.Goods;
import com.schoolke.bean.PreGoods;
import com.schoolke.bean.User;
import com.schoolke.bean.WishList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Bragg Troy on 2017/4/14.
 */
public class WishListDao extends BaseDao {

    // 获取用户wishlist数据
    public ArrayList<WishList> getWishListByUserId(int userId){
        ArrayList<WishList> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT w.id,g.id,g.goodsName,g.schoolPrice,g.state,img.name,u.id,u.userName,u.head\n" +
                "FROM goods g INNER JOIN wishlist w INNER JOIN goodsimages img INNER JOIN user u\n" +
                "ON g.id = w.goodsId AND g.id = img.goodsId AND u.id = g.userId AND w.userId = ?\n" +
                "GROUP BY g.id ORDER BY w.id DESC");
            psmt.setInt(1,userId);
            rs = psmt.executeQuery();
            while (rs.next()){
                WishList wishList = new WishList();
                wishList.setId(rs.getInt(1));

                PreGoods goods = new PreGoods();
                goods.setId(rs.getInt(2));
                goods.setGoodsName(rs.getString(3));
                goods.setSchoolPrice(rs.getString(4));
                goods.setState(rs.getInt(5));
                goods.setImageName(rs.getString(6));

                User user = new User();
                user.setId(rs.getInt(7));
                user.setUserName(rs.getString(8));
                user.setHeader(rs.getString(9));

                goods.setUser(user);
                wishList.setPreGoods(goods);

                arr.add(wishList);
            }
        }catch (Exception ex){
            System.out.print("获取wishlist异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }

    // 删除用户的购物车数据
    // 通过唯一的wishlist。id来删除
    public int deleteWishItem(int id){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("DELETE FROM wishlist WHERE wishlist.id = ?");
            psmt.setInt(1,id);
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("删除异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return val;
    }

    // 添加到购物车
    public int addToCart(int goodsId , int userId){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("INSERT INTO wishlist (wishlist.goodsId, wishlist.userId) VALUES (?, ?)");
            psmt.setInt(1,goodsId);
            psmt.setInt(2,userId);
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("添加购物车异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return val;
    }

    // 检测该商品是否已在当前用户的购物车内
    public int checkGoodsWithUser(int goodsId, int userId){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT COUNT(*) FROM wishlist WHERE wishlist.goodsId = ? AND wishlist.userId = ?");
            psmt.setInt(1,goodsId);
            psmt.setInt(2,userId);
            rs = psmt.executeQuery();
            if(rs.last()){
                val = rs.getInt(1);
            }

        }catch (Exception ex){
            System.out.print("添加购物车异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return val;
    }



}
