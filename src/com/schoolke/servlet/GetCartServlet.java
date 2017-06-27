package com.schoolke.servlet;

import com.schoolke.bean.WishList;
import com.schoolke.dao.WishListDao;
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
 * Created by Bragg Troy on 2017/4/14.
 */
@WebServlet(name = "GetCartServlet" , urlPatterns = "/MyWishList")
public class GetCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        int id = Integer.parseInt(request.getParameter("ID"));

        ArrayList<WishList> arrayList = new WishListDao().getWishListByUserId(id);
        Map json = new HashMap();
        json.put("wish",arrayList);
        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.toString();
        out.print(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
