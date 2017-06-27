package com.schoolke.servlet;

import com.schoolke.bean.Goods;
import com.schoolke.bean.User;
import com.schoolke.dao.GoodsDao;
import com.schoolke.dao.UserDao;
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
 * Created by Bragg Troy on 2017/3/23.
 */
@WebServlet(name = "GetUsersGoodsLostServlet" , urlPatterns = "/getUsersGoodsLost")
public class GetUsersGoodsLostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        int id = Integer.parseInt(request.getParameter("ID"));
        User user = new UserDao().getUserInfoById(id);
        ArrayList<Goods> on_arr = new GoodsDao().getGoodsListByUserId(id,1);
        int length_on = on_arr.size();
        ArrayList<Goods> down_arr = new GoodsDao().getGoodsListByUserId(id,0);
        int length_down = down_arr.size();

        JSONArray jsonArray_on = new JSONArray(on_arr);
        String string_on = jsonArray_on.toString();
        JSONArray jsonArray_down = new JSONArray(down_arr);
        String string_down = jsonArray_down.toString();
        JSONObject jsonObject_user = new JSONObject(user);
        String user_result = jsonObject_user.toString();

        Map user_goods_info = new HashMap();
        user_goods_info.put("on_sell",string_on);
        user_goods_info.put("on_length",length_on);
        user_goods_info.put("down_sell",string_down);
        user_goods_info.put("down_length",length_down);
        user_goods_info.put("user",user_result);

        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject(user_goods_info);
        String result = jsonObject.toString();
        out.print(result);

    }
}
