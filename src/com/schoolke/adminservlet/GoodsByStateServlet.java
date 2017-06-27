package com.schoolke.adminservlet;

import com.schoolke.bean.PreGoods;
import com.schoolke.bean.RecommendGoods;
import com.schoolke.dao.GoodsDao;
import org.json.JSONArray;
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
 * Created by Bragg Troy on 2017/5/5.
 */
@WebServlet(name = "GoodsByStateServlet" , urlPatterns = "/goodsByState")
public class GoodsByStateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        int state = Integer.parseInt(request.getParameter("STATE"));
        // state == 1 => ON
        // state == 0 => DOWN
        GoodsDao gd = new GoodsDao();
        ArrayList<PreGoods> arrayList = gd.getGoodsByState(state);
        ArrayList<RecommendGoods> arr_rd = gd.getRecommends();

        for (PreGoods pregoods : arrayList) {
            for (RecommendGoods rdgood : arr_rd){
                if(pregoods.getId() == rdgood.getGoods().getId()){
                    pregoods.setState(3);
                }
            }
        }

        JSONArray jsonArray = new JSONArray(arrayList);
        String result = jsonArray.toString();
        out.print(result);

    }
}
