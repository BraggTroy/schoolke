package com.schoolke.servlet;

import com.schoolke.bean.Notice;
import com.schoolke.dao.NoticeDao;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bragg Troy on 2017/5/5.
 */
@WebServlet(name = "NoticeServlet" , urlPatterns = "/notice")
public class NoticeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        int id = Integer.parseInt(request.getParameter("ID"));
        Notice notice = new NoticeDao().getNoticeById(id);
        JSONObject jsonObject = new JSONObject(notice);
        String result = jsonObject.toString();
        out.print(result);
    }
}
