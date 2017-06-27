package com.schoolke.servlet;

import com.schoolke.bean.User;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bragg Troy on 2017/3/22.
 */
@WebServlet(name = "UserLoginServlet" , urlPatterns = "/userLogin")
public class UserLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        String userName = request.getParameter("NAME");
        String password = request.getParameter("PASSWORD");
        User user = new UserDao().userLogin(userName,password);
        PrintWriter out = response.getWriter();
        if(user == null){
            /*Map params = new HashMap();
            params.put("message","error");
            JSONObject jsonObject = new JSONObject(params);
            result = jsonObject.toString();*/
            String result = "0";
            out.print(result);
        }else{
            JSONObject jsonObject = new JSONObject(user);
            String result = jsonObject.toString();
            out.print(result);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
