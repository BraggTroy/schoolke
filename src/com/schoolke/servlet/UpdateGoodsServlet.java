package com.schoolke.servlet;

import com.schoolke.bean.Classify;
import com.schoolke.bean.Goods;
import com.schoolke.dao.GoodsDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bragg Troy on 2017/4/1.
 */
@WebServlet(name = "UpdateGoodsServlet" , urlPatterns = "/updateGoods")
public class UpdateGoodsServlet extends HttpServlet {
    // 更新商品信息
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        int goodsId = Integer.parseInt(request.getParameter("goodsId"));
        String goodsName = request.getParameter("goodsName");
        String schoolPrice = request.getParameter("schoolPrice");
        String originPrice = request.getParameter("originPrice");
        String sell = request.getParameter("sell");
        String remark = request.getParameter("remark");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df.format(new Date());
        int classify_c = Integer.parseInt(request.getParameter("classify_c"));

        Classify cy = new Classify();
        cy.setId(classify_c);

        Goods goods = new Goods();
        goods.setId(goodsId);
        goods.setGoodsName(goodsName);
        goods.setSchoolPrice(schoolPrice);
        goods.setOriginPrice(originPrice);
        goods.setSell(sell);
        goods.setRemark(remark);
        goods.setPublishTime(time);
        goods.setClassify(cy);

        int val = new GoodsDao().UpdateGoodsInfo(goods);
        out.print(val);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
