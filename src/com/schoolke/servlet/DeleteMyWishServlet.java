package com.schoolke.servlet;

import com.schoolke.dao.WishListDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bragg Troy on 2017/4/14.
 */
@WebServlet(name = "DeleteMyWishServlet" , urlPatterns = "/deleteMyWish")
public class DeleteMyWishServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        int id = Integer.parseInt(request.getParameter("ID"));
        int val = new WishListDao().deleteWishItem(id);
        PrintWriter out = response.getWriter();
        out.print(val);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
