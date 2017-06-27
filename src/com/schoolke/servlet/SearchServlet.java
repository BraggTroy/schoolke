package com.schoolke.servlet;

import com.schoolke.bean.Classify;
import com.schoolke.bean.Goods;
import com.schoolke.bean.PreGoods;
import com.schoolke.dao.ClassifyDao;
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
 * Created by Bragg Troy on 2017/4/10.
 */
@WebServlet(name = "SearchServlet" , urlPatterns = "/search")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        String search = request.getParameter("keWords");

        int classifyId = 0;
        // 查询是否是分类
        ArrayList<Classify> arr_classify = new ClassifyDao().GetClassify();
        for (int i=0;i<arr_classify.size();i++){
            if((arr_classify.get(i).getName()).equals(search)){
                classifyId = arr_classify.get(i).getId();
            }
        }

        ArrayList<PreGoods> classify_goods = new ArrayList<>();
        if(classifyId > 0) {
            classify_goods = new GoodsDao().getGoodsByClassify(classifyId, "code");
        }

        ArrayList<PreGoods> search_goods = new GoodsDao().getGoodsBySearch(search);

        Map json = new HashMap();
        json.put("keWords",search);
        json.put("classify",classify_goods);
        json.put("search",search_goods);
        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.toString();

        out.print(result);
    }
}
