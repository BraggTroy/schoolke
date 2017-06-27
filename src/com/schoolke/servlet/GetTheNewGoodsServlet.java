package com.schoolke.servlet;

import com.schoolke.bean.PreGoods;
import com.schoolke.dao.GoodsDao;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Bragg Troy on 2017/4/6.
 */
@WebServlet(name = "GetTheNewGoodsServlet" , urlPatterns = "/getTheNewGoods")
public class GetTheNewGoodsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");

        ArrayList<PreGoods> arr = new GoodsDao().getTheNew();
        JSONArray jsonArray = new JSONArray(arr);
        String result = jsonArray.toString();

        PrintWriter out = response.getWriter();
        out.print(result);

    }
}
