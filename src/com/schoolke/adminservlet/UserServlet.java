package com.schoolke.adminservlet;

import com.schoolke.bean.User;
import com.schoolke.dao.UserDao;
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
 * Created by Bragg Troy on 2017/5/14.
 */
@WebServlet(name = "UserServlet" , urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        UserDao userDao = new UserDao();

        ArrayList<User> arr = userDao.getUserInfo();
        Map json = new HashMap();
        json.put("list",arr);

        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.toString();
        out.print(result);
    }
}
