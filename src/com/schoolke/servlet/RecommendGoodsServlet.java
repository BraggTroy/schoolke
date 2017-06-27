package com.schoolke.servlet;

import com.schoolke.bean.GoodsImages;
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
 * Created by Bragg Troy on 2017/4/22.
 */
@WebServlet(name = "RecommendGoodsServlet" , urlPatterns = "/recommendGoods")
public class RecommendGoodsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        GoodsDao gd = new GoodsDao();

        ArrayList<RecommendGoods> arrayList = gd.getRecommends();
        Map jsonResult = new HashMap();
        ArrayList resultArr = new ArrayList();

        for (int i=0; i<arrayList.size(); i++){
            int goodsId = arrayList.get(i).getGoods().getId();
            ArrayList<GoodsImages> arr = gd.getGoodsPicById(goodsId);
            JSONArray img_jsonArray = new JSONArray(arr);
            String result = img_jsonArray.toString();
            JSONObject info_jsonObject = new JSONObject(arrayList.get(i));
            Map json = new HashMap();
            json.put("images",result);
            json.put("info",info_jsonObject);
//            JSONObject json_o = new JSONObject(json);
            resultArr.add(json);
        }
        jsonResult.put("list",resultArr);
        JSONObject result_js = new JSONObject(jsonResult);
        String result = result_js.toString();
        out.print(result);

    }
}
