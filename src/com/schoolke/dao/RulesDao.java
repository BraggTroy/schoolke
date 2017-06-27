package com.schoolke.dao;

import com.schoolke.bean.Rules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * Created by Bragg Troy on 2017/3/21.
 */
public class RulesDao extends BaseDao {
    /*通过type获取协议*/
    public Rules getRulesByType(String type){
        Rules rules = new Rules();

        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT * FROM rules WHERE rules.type = ?");
            psmt.setString(1,type);
            rs = psmt.executeQuery();
            if(rs.last()){
                rules.setId(rs.getInt(1));
                rules.setType(rs.getString(2));
                rules.setText(rs.getString(3));
                return rules;
            }
        }catch (Exception ex){
            System.out.println("获取"+ type +"协议内容异常"+ex.getMessage());
        }
        finally {
            super.closeAll(rs,psmt,con);
        }
        return null;
    }
}
