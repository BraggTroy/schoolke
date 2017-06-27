package com.schoolke.servlet;

import com.schoolke.bean.Rules;
import com.schoolke.dao.RulesDao;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bragg Troy on 2017/3/21.
 */
@WebServlet(name = "GetRulesByTypeServlet" , urlPatterns = "/getRules.do")
public class GetRulesByTypeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        response.setCharacterEncoding("utf-8");

        String type = request.getParameter("type");
        RulesDao rulesDao = new RulesDao();
        Rules rules = rulesDao.getRulesByType(type);

        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject(rules);
        String result = jsonObject.toString();
        out.print(result);
    }

}
