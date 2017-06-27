package com.schoolke.adminservlet;

import com.schoolke.bean.Goods;
import com.schoolke.bean.RecommendGoods;
import com.schoolke.dao.GoodsDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bragg Troy on 2017/5/11.
 */
@WebServlet(name = "OptionsRecommendServlet" , urlPatterns = "/optionsRecommend")
public class OptionsRecommendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        GoodsDao gd = new GoodsDao();
        RecommendGoods rd = new RecommendGoods();
        int val = 0;

        // 操作类型
        String type = request.getParameter("TYPE");
        if("EDIT".equals(type)){
            int id = Integer.parseInt(request.getParameter("ID"));
            int index = Integer.parseInt(request.getParameter("INDEX"));
            String text = request.getParameter("TEXT");
            rd.setId(id);
            rd.setIndex(index);
            rd.setRecommendTitle(text);
            val = gd.saveRecommend(rd);

        }
        if("ADD".equals(type)){
            int id = Integer.parseInt(request.getParameter("ID"));
            int index = Integer.parseInt(request.getParameter("INDEX"));
            String rd_title = request.getParameter("TEXT");
            rd.setIndex(index);
            rd.setRecommendTitle(rd_title);
            Goods goods = new Goods();
            goods.setId(id);
            rd.setGoods(goods);
            val = gd.newRecommend(rd);
        }
        if("CANCEL".equals(type)){
            int rd_id = Integer.parseInt(request.getParameter("ID"));
            val = gd.cancelRecommend(rd_id);
        }

        out.print(val);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
