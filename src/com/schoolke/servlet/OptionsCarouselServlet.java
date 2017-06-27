package com.schoolke.servlet;

import com.schoolke.bean.Carousel;
import com.schoolke.dao.CarouselDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bragg Troy on 2017/5/10.
 */
@WebServlet(name = "OptionsCarouselServlet" , urlPatterns = "/optionsCarousel")
public class OptionsCarouselServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        CarouselDao cd = new CarouselDao();

        String type = request.getParameter("TYPE");

        // 判断是save，add，delete
        if("SAVE".equals(type)){
            int id = Integer.parseInt(request.getParameter("ID"));
            String link = request.getParameter("LINK");
            Carousel carousel = new Carousel();
            carousel.setId(id);
            carousel.setLink(link);
            int val = cd.saveCarousel(carousel);
            out.print(val);
        }
        if("ADD".equals(type)){

        }
        if("DELETE".equals(type)){
            int id = Integer.parseInt(request.getParameter("ID"));
            int val = cd.deleteCarousel(id);
            out.print(val);
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
