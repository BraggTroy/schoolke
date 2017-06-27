package com.schoolke.servlet;

import com.schoolke.bean.Goods;
import com.schoolke.bean.GoodsImages;
import com.schoolke.dao.GoodsDao;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bragg Troy on 2017/3/29.
 */
@WebServlet(name = "GetGoodsInfoServlet" , urlPatterns = "/getGoodsInfo")
public class GetGoodsInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        int id = Integer.parseInt(request.getParameter("goodsId"));
        GoodsDao goodsDao = new GoodsDao();
        Goods goods = goodsDao.getGoodsById(id);
        ArrayList<GoodsImages> goodsImages = goodsDao.getGoodsPicById(id);
        ArrayList<Goods> goodsList = goodsDao.getUserGoodsListByGoodsId(id);

        Map json = new HashMap();
        json.put("goods",goods);
        json.put("images",goodsImages);
        json.put("list",goodsList);

        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.toString();

        out.print(result);

    }
}
