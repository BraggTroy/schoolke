package com.schoolke.dao;

import com.schoolke.bean.Carousel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Bragg Troy on 2017/5/9.
 */
public class CarouselDao extends BaseDao {

    // 获取轮播
    public ArrayList<Carousel> getCarousel(){
        ArrayList<Carousel> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT * FROM carousel");
            rs = psmt.executeQuery();

            while (rs.next()){
                Carousel carousel = new Carousel();
                carousel.setId(rs.getInt(1));
                carousel.setName(rs.getString(2));
                carousel.setLink(rs.getString(3));
                arr.add(carousel);
            }

        }catch (Exception ex){
            System.out.print("获取轮播异常"+ex.getMessage());
        }finally {
            closeAll(rs,psmt,con);
        }
    return arr;
    }


    // 编辑轮播
    public int saveCarousel(Carousel carousel){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            con = super.getConnection();
            psmt = con.prepareStatement("UPDATE carousel SET link = ? WHERE id = ?");
            psmt.setString(1,carousel.getLink());
            psmt.setInt(2,carousel.getId());
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("编辑轮播异常"+ex.getMessage());
        }finally {
            closeAll(rs,psmt,con);
        }
        return val;
    }




    //    添加轮播






    // 删除轮播
    public int deleteCarousel(int id){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            con = super.getConnection();
            psmt = con.prepareStatement("DELETE FROM carousel WHERE id = ?");
            psmt.setInt(1,id);
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("删除轮播异常"+ex.getMessage());
        }finally {
            closeAll(rs,psmt,con);
        }

        return val;
    }

}
