package com.schoolke.servlet;

import com.schoolke.bean.PreGoods;
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
 * Created by Bragg Troy on 2017/4/7.
 */
@WebServlet(name = "GoodsByClassifyServlet" , urlPatterns = "/goodsByClassify")
public class GoodsByClassifyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        Map json = new HashMap();
        int id = Integer.parseInt(request.getParameter("ID"));
        String code = request.getParameter("CODE");

            ArrayList<PreGoods> arr = new GoodsDao().getGoodsByClassify(id,code);
            json.put("list",arr);


        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.toString();
        out.print(result);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
