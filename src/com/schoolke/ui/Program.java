package com.schoolke.ui;

import com.schoolke.bean.Goods;
import com.schoolke.dao.GoodsDao;
import com.schoolke.dao.ToolsDao;
import jdk.nashorn.internal.ir.RuntimeNode;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bragg Troy on 2017/3/21.
 */
public class Program {
    public static void main(String[] args){

//        GoodsDao gd = new GoodsDao();
//        Goods goods = gd.getGoodsById(1);
////        goods.setGoodsName("商品名称");
////        goods.setSchoolPrice("39");
////        String[] arr = new String[]{"dhfajkdhfk.jpg","fhajkdhf.jpg","fhjkadhf.png","123fasd.jpeg"};
////        gd.InsertGoodsInfo_getId(goods,arr);
//        System.out.println(goods.getId());
//        System.out.println(goods.getSchoolPrice());
//        System.out.println(goods.getGoodsName());



//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式


        String time = df.format(new Date());
        System.out.println(time);// new Date()为获取当前系统时间*/

//        String ip = new ToolsDao().getIpAddress();
//        request.getRemoteAddr();


        ArrayList arr = new GoodsDao().getTheNew();
        System.out.print(arr);
    }
}
