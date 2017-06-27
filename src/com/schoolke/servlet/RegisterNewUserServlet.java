package com.schoolke.servlet;

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

/**
 * Created by Bragg Troy on 2017/3/27.
 */
@WebServlet(name = "RegisterNewUserServlet" , urlPatterns = "/registerNewUser")
public class RegisterNewUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String contact = request.getParameter("contact");
        String school = request.getParameter("school");
        String time = request.getParameter("time");
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setContact(contact);
        user.setSchool(school);
        user.setRegistrationDate(time);

        int val = new UserDao().registerNewUser(user);
        System.out.println("haha"+val);
        User newUser = new UserDao().getUserInfoById(val);
        JSONObject jsonObject = new JSONObject(newUser);
        String result = jsonObject.toString();
        out.print(result);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
