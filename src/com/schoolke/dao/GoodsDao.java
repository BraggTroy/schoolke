package com.schoolke.dao;

import com.schoolke.bean.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Bragg Troy on 2017/3/20.
 */
public class GoodsDao extends BaseDao {

    /**
     * 商品信息的发布，先插入基本信息，由返回的 id 完成图片的上传
     * @param goods 字段数组
     * @return
     */
    /*插入商品其他字段*/
    public int InsertGoodsInfo_getId(Goods goods){
        int goodsId = 0;
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("INSERT INTO goods(goodsName,schoolPrice,originPrice,sell,remark,publishTime,classifyId,userId,state)\n" +
                    "VALUES(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            psmt.setString(1,goods.getGoodsName());
            psmt.setString(2,goods.getSchoolPrice());
            psmt.setString(3,goods.getOriginPrice());
            psmt.setString(4,goods.getSell());
            psmt.setString(5,goods.getRemark());
            psmt.setString(6,goods.getPublishTime());
            psmt.setInt(7,goods.getClassify().getId());
            psmt.setInt(8,goods.getUser().getId());
            psmt.setInt(9,1); //state默认为1表示在售

            val = psmt.executeUpdate();
            if(val == 1){
                try{
                    rs = psmt.getGeneratedKeys();
                    if(rs.next()){
                        goodsId = rs.getInt(1);
                    }
                }catch (Exception e){
                    System.out.println("返回插入ID异常"+e.getMessage());
                }
            }

        }catch (Exception ex){
            System.out.println("插入商品字段数据异常"+ex.getMessage());
        }finally {
            super.closeAll(null,psmt,con);
        }

        return goodsId;
    }

    /*图片上传*/
    public int UpdateGoodsImages(GoodsImages goodsImages){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("INSERT INTO goodsimages(name,goodsId) VALUES(?,?)");
            psmt.setString(1,goodsImages.getName());
            psmt.setInt(2,goodsImages.getGoodsId());
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.println("图片上传异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return val;
    }

    /*通过商品ID获取商品详细信息*/
    public Goods getGoodsById(int id){
        Goods goods = new Goods();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT goods.id,goods.goodsName,goods.schoolPrice,goods.originPrice,goods.sell,goods.remark,goods.publishTime,\n" +
                    "classify.id,classify.name,classify.parentId,user.id,user.userName,user.head,goods.state,user.school\n" +
                    "FROM goods INNER JOIN classify INNER JOIN user\n" +
                    "ON goods.classifyId = classify.id AND goods.userId = user.id\n" +
                    "WHERE goods.id = ?");
            psmt.setInt(1,id);

            rs = psmt.executeQuery();
            if (rs.next()){
                goods = SelectGoodsObject(rs);
            }

        }catch (Exception ex){
            System.out.println("通过ID获取商品信息异常"+ex.getMessage());
        }
        finally {
            super.closeAll(rs,psmt,con);
        }
        return goods;
    }
    /*根据已得到的userId获取该用户最近发布的3件商品*/
    public ArrayList<Goods> getUsersResentGoods(int userId){
        ArrayList<Goods> goodsList = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT goods.id,goods.goodsName,user.id,user.userName,goods.publishTime\n" +
                    "FROM goods INNER JOIN user ON goods.userId = user.id\n" +
                    "WHERE user.id = ? AND goods.state = 1\n" +
                    "ORDER BY goods.publishTime DESC\n" +
                    "LIMIT 3");
            psmt.setInt(1,userId);
            rs = psmt.executeQuery();
            while (rs.next()){
                Goods goods = new Goods();
                User user = new User();
                goods.setId(rs.getInt(1));
                goods.setGoodsName(rs.getString(2));
                user.setId(rs.getInt(3));
                user.setUserName(rs.getString(4));
                goods.setUser(user);
                goods.setPublishTime(rs.getString(5));
                goodsList.add(goods);
            }

        }catch (Exception ex){
            System.out.println("获取用户最新发布的商品异常"+ex.getMessage());
        }
        finally {
            super.closeAll(rs,psmt,con);
        }
        return goodsList;
    }


    /*通过商品名模糊搜索商品-以列表的形式显示*/
    public ArrayList getGoodsByName_query(String name_query){
        ArrayList<Goods> arr = new ArrayList<>();

        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT goods.id,goods.goodsName,goods.schoolPrice,goods.originPrice,goods.publishTime,user.id,user.userName\n" +
                    "FROM goods INNER JOIN user WHERE goods.goodsName LIKE ?");
            name_query = "%"+ name_query +"%";
            psmt.setString(1,name_query);
            rs = psmt.executeQuery();
            while (rs.next()){
                Goods goods = new Goods();
                goods.setId(rs.getInt(1));
                goods.setGoodsName(rs.getString(2));
                goods.setSchoolPrice(rs.getString(3));
                goods.setOriginPrice(rs.getString(4));
                goods.setPublishTime(rs.getString(5));
                User user = new User();
                user.setId(rs.getInt(6));
                user.setUserName(rs.getString(7));
                goods.setUser(user);

                arr.add(goods);
            }

        }catch (Exception ex){
            System.out.println("模糊搜索商品信息异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }


    /*通过用户ID统计商品*/
    public ArrayList<Goods> getGoodsListByUserId(int id,int state){
        ArrayList<Goods> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT id,goodsName,schoolPrice,originPrice,sell,remark,state " +
                    "FROM goods WHERE goods.userId = ? AND goods.state = ? " +
                    "ORDER BY goods.publishTime DESC");
            psmt.setInt(1,id);
            psmt.setInt(2,state);
            rs = psmt.executeQuery();
            while (rs.next()){
                Goods goods = new Goods();
                goods.setId(rs.getInt(1));
                goods.setGoodsName(rs.getString(2));
                goods.setSchoolPrice(rs.getString(3));
                goods.setOriginPrice(rs.getString(4));
                goods.setSell(rs.getString(5));
                goods.setRemark(rs.getString(6));
                goods.setState(rs.getInt(7));
                arr.add(goods);
            }

        }catch (Exception ex){
            System.out.println("统计用户商品数异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }

    public Goods SelectGoodsObject(ResultSet rs){
        try{
            int id = rs.getInt(1);
            String goodsName = rs.getString(2);
            String schoolPrice = rs.getString(3);
            String originPrice = rs.getString(4);
            String sell = rs.getString(5);
            String remark = rs.getString(6);
            String publishTime = rs.getString(7);
            int classifyId = rs.getInt(8);
            String classifyName = rs.getString(9);
            int classifyParentId = rs.getInt(10);
            int userId = rs.getInt(11);
            String userName = rs.getString(12);
            String userHead = rs.getString(13);
            int state = rs.getInt(14);
            String userSchool = rs.getString(15);

            Classify classify = new Classify(classifyId,classifyName,classifyParentId);
            User user = new User();
            user.setId(userId);
            user.setUserName(userName);
            user.setSchool(userSchool);
            user.setHeader(userHead);

            Goods goods = new Goods(id,goodsName,schoolPrice,originPrice,sell,remark,publishTime,classify,user,state);
            return goods;
        }catch (Exception ex){
            System.out.println("返回商品对象异常"+ex.getMessage());
        }
        return null;
    }

    /*下架商品，批量下架*/
    /*上架 ， type*/
    public int downGoodsByIds(int id,String type,String time){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        String sql = "";

        try{

            if("ON".equals(type)){
                sql = "UPDATE goods SET state = 1,publishTime = ? WHERE id = ?";
                con = super.getConnection();
                psmt = con.prepareStatement(sql);
                psmt.setString(1,time);
                psmt.setInt(2,id);
                val = psmt.executeUpdate();
            }
            if("DOWN".equals(type)){
                sql = "UPDATE goods SET state = 0 WHERE id = ?";
                con = super.getConnection();
                psmt = con.prepareStatement(sql);
                psmt.setInt(1,id);
                val = psmt.executeUpdate();
            }


        }catch (Exception ex){
            System.out.print("批量下架商品异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return val;
    }

    // 通过商品ID获取商品图片
    public ArrayList<GoodsImages> getGoodsPicById(int goodsId){
        ArrayList<GoodsImages> arr = new ArrayList<>();

        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;

        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT * FROM goodsimages WHERE goodsimages.goodsId = ?");
            psmt.setInt(1,goodsId);

            rs = psmt.executeQuery();
            while (rs.next()){
                GoodsImages goodsImages = new GoodsImages(rs.getInt(1),rs.getString(2),rs.getInt(3));
                arr.add(goodsImages);
            }

        }catch (Exception ex){
            System.out.print("获取商品信息异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return arr;
    }

//    通过商品ID获取发布者其他商品信息
    public ArrayList<Goods> getUserGoodsListByGoodsId(int goodsId){
        ArrayList<Goods> arr = new ArrayList<>();

        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;

        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT goods.id,goods.goodsName,goods.state FROM goods \n" +
                    "WHERE goods.userId = (SELECT userId FROM goods WHERE goods.id = ?) AND goods.state= 1\n" +
                    "ORDER BY goods.publishTime DESC LIMIT 3");
            psmt.setInt(1,goodsId);
            rs = psmt.executeQuery();

            while (rs.next()){
                Goods goods = new Goods();
                goods.setId(rs.getInt(1));
                goods.setGoodsName(rs.getString(2));
                goods.setState(rs.getInt(3));
                arr.add(goods);
            }
        }catch (Exception ex){
            System.out.print("获取用户三商品信息异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }


    // 更新商品信息
    public int UpdateGoodsInfo(Goods goods){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("UPDATE goods SET goodsName = ?,schoolPrice = ?,originPrice = ?,\n" +
                    "sell = ?,remark = ?,publishTime = ?,classifyId = ?\n" +
                    "WHERE id = ?");
            psmt.setString(1,goods.getGoodsName());
            psmt.setString(2,goods.getSchoolPrice());
            psmt.setString(3,goods.getOriginPrice());
            psmt.setString(4,goods.getSell());
            psmt.setString(5,goods.getRemark());
            psmt.setString(6,goods.getPublishTime());
            psmt.setInt(7,goods.getClassify().getId());
            psmt.setInt(8,goods.getId());

            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("更新商品信息异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }
        return val;
    }

    // 删除商品数据库图片数据，不删除文件
    public int deleteImage(int imageId){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("DELETE FROM goodsimages WHERE goodsimages.id = ?");
            psmt.setInt(1,imageId);
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("删除商品数据异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return val;
    }


    // 首页获取最新发布的商品
    public ArrayList<PreGoods> getTheNew(){
        ArrayList<PreGoods> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT goods.id,goods.goodsName,goods.schoolPrice,goods.originPrice,goods.publishTime,goods.state,goodsimages.name,user.id,user.userName\n" +
                    "FROM goods INNER JOIN goodsimages INNER JOIN user \n" +
                    "ON goods.id = goodsimages.goodsId AND goods.userId = user.id\n" +
                    "WHERE goods.state = 1 GROUP BY goods.goodsName ORDER BY goods.id DESC LIMIT 10");
            rs = psmt.executeQuery();
            while (rs.next()){
                PreGoods goods = new PreGoods();
                goods.setId(rs.getInt(1));
                goods.setGoodsName(rs.getString(2));
                goods.setSchoolPrice(rs.getString(3));
                goods.setOriginPrice(rs.getString(4));
                goods.setPublishTime(rs.getString(5));
                goods.setState(rs.getInt(6));
                goods.setImageName(rs.getString(7));

                User user = new User();
                user.setId(rs.getInt(8));
                user.setUserName(rs.getString(9));
                goods.setUser(user);

                arr.add(goods);
            }

        }catch (Exception ex){
            System.out.print("获取最新商品异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }

    // 获取分类商品信息
    public ArrayList<PreGoods> getGoodsByClassify(int classifyId,String code){
        ArrayList<PreGoods> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try{
            con = super.getConnection();
            if("ALL".equals(code)){
                psmt = con.prepareStatement("SELECT g.id,g.goodsName,g.schoolPrice,g.originPrice,g.publishTime,g.state,goodsimages.name,u.id,u.userName,c.id\n" +
                        "FROM goods g INNER JOIN goodsimages INNER JOIN user u INNER JOIN classify c\n" +
                        "ON g.id = goodsimages.goodsId AND g.userId = u.id AND g.classifyId = c.id\n" +
                        "WHERE g.state = 1 AND c.parentId = ? GROUP BY g.id");
            }else {
                psmt = con.prepareStatement("SELECT g.id,g.goodsName,g.schoolPrice,g.originPrice,g.publishTime,g.state,goodsimages.name,u.id,u.userName,c.id\n" +
                        "FROM goods g INNER JOIN goodsimages INNER JOIN user u INNER JOIN classify c\n" +
                        "ON g.id = goodsimages.goodsId AND g.userId = u.id AND g.classifyId = c.id\n" +
                        "WHERE g.state = 1 AND c.id = ? GROUP BY g.id");
            }

            psmt.setInt(1,classifyId);
            rs = psmt.executeQuery();
            while (rs.next()){
                PreGoods goods = new PreGoods();
                goods.setId(rs.getInt(1));
                goods.setGoodsName(rs.getString(2));
                goods.setSchoolPrice(rs.getString(3));
                goods.setOriginPrice(rs.getString(4));
                goods.setPublishTime(rs.getString(5));
                goods.setState(rs.getInt(6));
                goods.setImageName(rs.getString(7));

                User user = new User();
                user.setId(rs.getInt(8));
                user.setUserName(rs.getString(9));
                goods.setUser(user);
                goods.setClassifyId(rs.getInt(10));
                arr.add(goods);
            }

        }catch (Exception ex){
            System.out.print("获取分类商品信息异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }

    // 搜索
    public ArrayList<PreGoods> getGoodsBySearch(String search){
        ArrayList<PreGoods> arrayList = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT g.id,g.goodsName,g.schoolPrice,g.originPrice,g.publishTime,g.state,goodsimages.name,u.id,u.userName\n" +
                "FROM goods g INNER JOIN goodsimages INNER JOIN user u \n" +
                "ON g.id = goodsimages.goodsId AND g.userId = u.id\n" +
                "WHERE g.state = 1 AND g.goodsName LIKE ? GROUP BY g.id");
            String str = "%"+ search +"%";
            psmt.setString(1,str);
            rs = psmt.executeQuery();

            while (rs.next()){
                PreGoods g = new PreGoods();
                g.setId(rs.getInt(1));
                g.setGoodsName(rs.getString(2));
                g.setSchoolPrice(rs.getString(3));
                g.setOriginPrice(rs.getString(4));
                g.setPublishTime(rs.getString(5));
                g.setState(rs.getInt(6));
                g.setImageName(rs.getString(7));
                User user = new User();
                user.setId(rs.getInt(8));
                user.setUserName(rs.getString(9));
                g.setUser(user);
                g.setClassifyId(0);
                arrayList.add(g);
            }

        }catch (Exception ex){
            System.out.print("搜索异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arrayList;
    }

    // 首页推荐商品
    public ArrayList<RecommendGoods> getRecommends(){
        ArrayList<RecommendGoods> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT r.id,r.goodsId,r.index,r.recommend_title FROM recommends r ORDER BY r.index");
            rs = psmt.executeQuery();
            while (rs.next()){
                RecommendGoods rd = new RecommendGoods();
                rd.setId(rs.getInt(1));
                rd.setGoods(getGoodsById(rs.getInt(2)));
                rd.setIndex(rs.getInt(3));
                rd.setRecommendTitle(rs.getString(4));
                arr.add(rd);
            }

        }catch (Exception ex){
            System.out.print("获取推荐商品异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }

    // 获取在售、下架商品信息
    public ArrayList<PreGoods> getGoodsByState(int state){
        ArrayList<PreGoods> arr = new ArrayList<>();
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;
        try{
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT goods.id,goods.goodsName,goods.schoolPrice,goods.originPrice,goods.publishTime,goods.state,goodsimages.name,user.id,user.userName\n" +
                "FROM goods INNER JOIN goodsimages INNER JOIN user \n" +
                "ON goods.id = goodsimages.goodsId AND goods.userId = user.id\n" +
                "WHERE goods.state = ? GROUP BY goods.goodsName ORDER BY goods.id DESC");
            psmt.setInt(1,state);
            rs = psmt.executeQuery();
            while (rs.next()){
                PreGoods preGoods = new PreGoods();
                preGoods.setId(rs.getInt(1));
                preGoods.setGoodsName(rs.getString(2));
                preGoods.setSchoolPrice(rs.getString(3));
                preGoods.setOriginPrice(rs.getString(4));
                preGoods.setPublishTime(rs.getString(5));
                preGoods.setState(rs.getInt(6));
                preGoods.setImageName(rs.getString(7));

                User user = new User();
                user.setId(rs.getInt(8));
                user.setUserName(rs.getString(9));
                preGoods.setUser(user);

                arr.add(preGoods);
            }
        }catch (Exception ex){
            System.out.print("获取最新商品异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return arr;
    }

    // 编辑已推荐商品
    public int saveRecommend(RecommendGoods rd){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("UPDATE recommends r SET r.recommend_title = ?,r.index = ? WHERE r.id = ?");

            psmt.setString(1,rd.getRecommendTitle());
            psmt.setInt(2,rd.getIndex());
            psmt.setInt(3,rd.getId());

            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("编辑已推荐商品异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return val;
    }

    // 新建推荐商品
    public int newRecommend(RecommendGoods rd){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("INSERT INTO recommends(recommends.goodsId,recommends.index,recommends.recommend_title) \n" +
                "VALUES(?,?,?)");
            psmt.setInt(1,rd.getGoods().getId());
            psmt.setInt(2,rd.getIndex());
            psmt.setString(3,rd.getRecommendTitle());
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("推荐商品异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return val;
    }

    // 取消推荐
    public int cancelRecommend(int id){
        int val = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("DELETE FROM recommends WHERE id = ?");
            psmt.setInt(1,id);
            val = psmt.executeUpdate();

        }catch (Exception ex){
            System.out.print("取消推荐商品异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return val;
    }

    // 总览、商品数量统计
    public int echartsNumber(int st){
        int num = 0;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs= null;
        try {
            con = super.getConnection();
            psmt = con.prepareStatement("SELECT COUNT(*) FROM goods WHERE state = ?");
            psmt.setInt(1,st);
            rs = psmt.executeQuery();
            if(rs.last()){
                num = rs.getInt(1);
            }

        }catch (Exception ex){
            System.out.print("统计异常"+ex.getMessage());
        }finally {
            super.closeAll(rs,psmt,con);
        }

        return num;
    }



}
