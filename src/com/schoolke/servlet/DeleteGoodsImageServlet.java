package com.schoolke.servlet;

import com.schoolke.dao.GoodsDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bragg Troy on 2017/4/4.
 */
@WebServlet(name = "DeleteGoodsImageServlet" , urlPatterns = "/deleteGoodsImage")
public class DeleteGoodsImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("utf-8");
        int imageId = Integer.parseInt(request.getParameter("DELID"));
        int val = new GoodsDao().deleteImage(imageId);
        PrintWriter out = response.getWriter();
        out.print(val);
//        request.getRemoteAddr();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
