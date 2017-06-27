package com.schoolke.servlet;

import com.schoolke.bean.Goods;
import com.schoolke.bean.Message;
import com.schoolke.bean.User;
import com.schoolke.dao.MessageDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bragg Troy on 2017/3/30.
 */
@WebServlet(name = "PublishMsgServlet" , urlPatterns = "/publishMsg")
public class PublishMsgServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        String msg = request.getParameter("CONTENT");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());

        int userId = Integer.parseInt(request.getParameter("USERID"));
        int goodsId = Integer.parseInt(request.getParameter("GOODSID"));
        User user = new User();
        user.setId(userId);
        Goods goods = new Goods();
        goods.setId(goodsId);

        Message message = new Message();
        message.setMsg(msg);
        message.setMsgTime(time);
        message.setUser(user);
        message.setGoods(goods);

        int val = new MessageDao().publishNewMsg(message);
        PrintWriter out = response.getWriter();
        out.print(val);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
