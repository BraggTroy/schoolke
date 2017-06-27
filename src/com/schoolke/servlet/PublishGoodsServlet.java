package com.schoolke.servlet;

import com.schoolke.bean.Classify;
import com.schoolke.bean.Goods;
import com.schoolke.bean.User;
import com.schoolke.dao.GoodsDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bragg Troy on 2017/3/20.
 */
@javax.servlet.annotation.WebServlet(name = "PublishGoodsServlet" , urlPatterns = "/publishGoods")
public class PublishGoodsServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String goodsName = request.getParameter("goodsName");
        String schoolPrice = request.getParameter("schoolPrice");
        String originPrice = request.getParameter("originPrice");
        String sell = request.getParameter("sell");
        String remark = request.getParameter("remark");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df.format(new Date());

//        String classify_p = request.getParameter("classify_p");
        int classify_c = Integer.parseInt(request.getParameter("classify_c"));
        int picNum = Integer.parseInt(request.getParameter("picNum"));
        int userId = Integer.parseInt(request.getParameter("userId"));

        Classify cy = new Classify();
        cy.setId(classify_c);
        User user = new User();
        user.setId(userId);


        Goods goods = new Goods();
        goods.setGoodsName(goodsName);
        goods.setSchoolPrice(schoolPrice);
        goods.setOriginPrice(originPrice);
        goods.setSell(sell);
        goods.setRemark(remark);
        goods.setPublishTime(time);
        goods.setClassify(cy);
        goods.setUser(user);

        int returnId = new GoodsDao().InsertGoodsInfo_getId(goods);

        PrintWriter out = response.getWriter();
        out.print(returnId);

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
