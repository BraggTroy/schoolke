package com.schoolke.servlet;

import com.schoolke.bean.Classify;
import com.schoolke.dao.ClassifyDao;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Bragg Troy on 2017/3/22.
 */
@WebServlet(name = "GetClassifyServlet" , urlPatterns = "/getClassify")
public class GetClassifyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        ArrayList<Classify> arr = new ClassifyDao().GetClassify();

        PrintWriter out = response.getWriter();
        JSONArray jsonArray = new JSONArray(arr);
        String result = jsonArray.toString();
        out.println(result);


    }
}
