package com.schoolke.servlet;

import com.schoolke.dao.WishListDao;
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
 * Created by Bragg Troy on 2017/4/15.
 */
@WebServlet(name = "AddToWishServlet" , urlPatterns = "/addToWish")
public class AddToWishServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        int goodsId = Integer.parseInt(request.getParameter("GOODSID"));
        int userId = Integer.parseInt(request.getParameter("USER"));

        WishListDao wd = new WishListDao();
        int checkVal = wd.checkGoodsWithUser(goodsId, userId);
        Map json = new HashMap();
        if(checkVal >= 1){
            json.put("exist",true);
            json.put("add",false);
        }else{
            int addVal = wd.addToCart(goodsId, userId);
            if(addVal == 1){
                json.put("exist",false);
                json.put("add",true);
            }else{
                json.put("exist",false);
                json.put("add",false);
            }

        }

        JSONObject jsonObject = new JSONObject(json);
        out.print(jsonObject);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
