package com.schoolke.servlet;

import com.schoolke.dao.GoodsDao;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Bragg Troy on 2017/3/24.
 */
@WebServlet(name = "DownThoseGoodsServlet" , urlPatterns = "/downThoseGoods")
public class DownThoseGoodsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        String ids_str = request.getParameter("IDS");
        String type = request.getParameter("TYPE");
        String time = request.getParameter("TIME");


        String[] ids = ids_str.split(",");
        int[] val_arr = new int[ids.length];

        for (int i=0;i<ids.length; i++){
            int id = Integer.parseInt(ids[i]);
            int val =  new GoodsDao().downGoodsByIds(id , type , time);
            System.out.println(val);
            val_arr[i] = val;
        }

        Map optList = new HashMap();
        optList.put("list",val_arr);
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject(optList);
        String result = jsonObject.toString();
        out.print(result);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
