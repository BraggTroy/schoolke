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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bragg Troy on 2017/5/5.
 */
@WebServlet(name = "NoticeListServlet" , urlPatterns = "/noticeList")
public class NoticeListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        ArrayList<Notice> arrayList = new NoticeDao().getAllNotices();
        Map json = new HashMap();
        json.put("list",arrayList);
        JSONObject jsonObject = new JSONObject(json);
        String result= jsonObject.toString();
        out.print(result);

    }
}
