package com.schoolke.servlet;

import com.schoolke.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bragg Troy on 2017/3/26.
 */
@WebServlet(name = "SearchThisUserNameServlet" , urlPatterns = "/searchUserName")
public class SearchThisUserNameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("USERNAME");

        int val = new UserDao().userNameIsExist(name);
        out.print(val);  //val==1则已经存在该用户名，val==0则用户名可用

    }
}
