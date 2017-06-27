package com.schoolke.servlet;

import com.schoolke.bean.Carousel;
import com.schoolke.dao.CarouselDao;
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
 * Created by Bragg Troy on 2017/5/9.
 */
@WebServlet(name = "CarouselServlet" , urlPatterns = "/carousel")
public class CarouselServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        ArrayList<Carousel> arrayList = new CarouselDao().getCarousel();
        JSONArray jsonArray = new JSONArray(arrayList);
        String result = jsonArray.toString();
        out.print(result);

    }
}
