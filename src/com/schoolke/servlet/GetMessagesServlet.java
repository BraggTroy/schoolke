package com.schoolke.servlet;

import com.schoolke.bean.Message;
import com.schoolke.dao.MessageDao;
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
 * Created by Bragg Troy on 2017/3/30.
 */
@WebServlet(name = "GetMessagesServlet" , urlPatterns = "/getMessages")
public class GetMessagesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        String type = request.getParameter("TYPE");
        int id = Integer.parseInt(request.getParameter("ID"));
        ArrayList<Message> arrayList = new ArrayList<>();
        if("FOR_GOODS".equals(type)){
//            id是goodsID
//            商品的留言
            arrayList = new MessageDao().getMsgByGoodsId(id);
        }
        if("FOR_USER".equals(type)){
            // 关于我的留言信息
            arrayList = new MessageDao().getMessagesAboutMe(id);
        }
        if("FOR_USER_MY".equals(type)){
            // 已发布的留言，评论
            arrayList = new MessageDao().getMessagesMy(id);
        }


        Map json = new HashMap();
        json.put("msgList",arrayList);
        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.toString();
        out.print(result);
    }
}
