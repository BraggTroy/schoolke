package com.schoolke.dao;

import com.schoolke.bean.Classify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Bragg Troy on 2017/3/22.
 */
public class ClassifyDao extends BaseDao {
    /*获取分类信息*/
    public ArrayList<Classify> GetClassify(){
        ArrayList<Classify> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT id,name,parentId FROM classify");
            rs = psmt.executeQuery();
            while (rs.next()){
                Classify classify = new Classify(rs.getInt(1),rs.getString(2),rs.getInt(3));
                arr.add(classify);
            }
            return arr;
        }catch (Exception ex){
            System.out.println("获取分类信息异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return null;
    }
}
