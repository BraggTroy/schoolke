package com.schoolke.adminservlet;

import com.schoolke.bean.Admin;
import com.schoolke.dao.UserDao;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bragg Troy on 2017/4/19.
 */
@WebServlet(name = "LoginServlet" , urlPatterns = "/Login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("userName");
        String pwd = request.getParameter("password");
        Admin admin = new UserDao().AdminLogin(name, pwd);

        if(admin == null){
            out.print("null");
        }else{
            JSONObject jsonObject = new JSONObject(admin);
            String result = jsonObject.toString();
            out.print(result);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
