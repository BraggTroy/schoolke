package com.schoolke.dao;

import com.schoolke.bean.Notice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Bragg Troy on 2017/5/5.
 */
public class NoticeDao extends BaseDao {
    // 发布新的通知公告
    public int publishNotice(String name,String time, String text){
        int noticeId = 0;
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;

        try {
            con = super.getConnection();
            psmt = con.prepareStatement("INSERT INTO notice(name,time,text) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            psmt.setString(1,name);
            psmt.setString(2,time);
            psmt.setString(3,text);
            val = psmt.executeUpdate();
            if(val == 1){
                try{
                    rs = psmt.getGeneratedKeys();
                    if(rs.next()){
                        noticeId = rs.getInt(1);
                    }
                }catch (Exception e){
                    System.out.println("返回插入ID异常"+e.getMessage());
                }
            }

        }catch (Exception ex){
            System.out.print("发布异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return noticeId;
    }

    // 通过ID获取校园客公告信息
    public Notice getNoticeById(int id){
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;

        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT * FROM notice WHERE id = ?");
            psmt.setInt(1,id);
            rs = psmt.executeQuery();
            if (rs.next()){
                Notice notice = new Notice();
                notice.setId(rs.getInt(1));
                notice.setName(rs.getString(2));
                notice.setTime(rs.getString(3));
                notice.setText(rs.getString(4));
                return notice;
            }

        }catch (Exception ex){
            System.out.print("发布异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return null;
    }

    // 获取全部通知公告列表
    public ArrayList<Notice> getAllNotices(){
        ArrayList<Notice> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;

        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT n.id,n.name,n.time FROM notice as n ORDER BY n.id DESC");
            rs = psmt.executeQuery();
            while (rs.next()){
                Notice notice = new Notice();
                notice.setId(rs.getInt(1));
                notice.setName(rs.getString(2));
                notice.setTime(rs.getString(3));
                arr.add(notice);
            }

        }catch (Exception ex){
            System.out.print("获取全部公告异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return arr;
    }



}
