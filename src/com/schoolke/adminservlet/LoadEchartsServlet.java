package com.schoolke.adminservlet;

import com.schoolke.dao.GoodsDao;
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
 * Created by Bragg Troy on 2017/5/12.
 */
@WebServlet(name = "LoadEchartsServlet" , urlPatterns = "/loadEcharts")
public class LoadEchartsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        GoodsDao gd = new GoodsDao();
        int state_on = gd.echartsNumber(1);
        int state_down = gd.echartsNumber(0);

        Map json = new HashMap();
        json.put("state_on",state_on);
        json.put("state_down",state_down);

        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.toString();
        out.print(result);

    }
}
