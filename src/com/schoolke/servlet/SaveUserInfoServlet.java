package com.schoolke.servlet;

import com.schoolke.bean.User;
import com.schoolke.dao.UserDao;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Bragg Troy on 2017/3/25.
 */
@WebServlet(name = "SaveUserInfoServlet" , urlPatterns = "/saveUserInfo")
public class SaveUserInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        //首先，判断是否multipart编码类型
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(isMultipart){
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置缓存区
            ServletContext servletContext = this.getServletContext();
            String tempPath = servletContext.getRealPath("temp");
            // 设置上传路径
            String posterPath = servletContext.getRealPath("/images/headerImages");
            String resourcePath = servletContext.getRealPath("resource");

            File repository = new File(tempPath);
            factory.setRepository(repository);
            // 创建处理带文件上传的表单处理器
            ServletFileUpload upload = new ServletFileUpload(factory);

            User user = new User();
            int id = 0;
            String img = null;

            try{
                // 重新封装request
                List<FileItem> items = upload.parseRequest(request);
                for(FileItem item : items){
                    if(item.isFormField()){
                        if("userId".equals(item.getFieldName())){
                            id = Integer.parseInt(item.getString());
                            user.setId(id);
                        }
                        if("userName".equals(item.getFieldName())){
                            user.setUserName(new String(item.getString().getBytes("iso-8859-1"),"utf-8"));
                        }
                        if("remark".equals(item.getFieldName())){
                            user.setPersonality(new String(item.getString().getBytes("iso-8859-1"),"utf-8"));
                        }
                        if("school".equals(item.getFieldName())){
                            user.setSchool(new String(item.getString().getBytes("iso-8859-1"),"utf-8"));
                        }
                        if("phone".equals(item.getFieldName())){
                            user.setContact(new String(item.getString().getBytes("iso-8859-1"),"utf-8"));
                        }
                    }
                    else{
                        String clientFilePath = item.getName();
                        if(clientFilePath != null && clientFilePath.length() != 0){
                            //	文件扩展名
                            String ext = clientFilePath.substring(clientFilePath.lastIndexOf("."));
                            // 生成随机永不重复的文件名
                            String fileName = UUID.randomUUID().toString() + ext;
                            // 保存到服务器端完整路径(绝对路径)
                            String fullPath = posterPath + "\\" + fileName;
                            if("head".equals(item.getFieldName())){
                                img = fileName;
                            }
                            item.write(new File(fullPath));
                        }


                    }
                }


            }catch (Exception ex){
                System.out.println("保存信息异常"+ex.getMessage());
            }

            UserDao userDao = new UserDao();
            // 获取原始信息
            User o_user = userDao.getUserInfoById(id);
            if(img == null){
                user.setHeader(o_user.getHeader());
            }else{
                user.setHeader(img);
            }

            int val = userDao.UpdateUserInfo(user);
            Map json = new HashMap();

            if(val ==1){
                json.put("result","success");
                User n_user = userDao.getUserInfoById(id);
                json.put("user",n_user);

            }else{
                json.put("result","failed");
                json.put("message","Wrong");
            }

            JSONObject jsonObject = new JSONObject(json);
            String result = jsonObject.toString();
            out.print(result);

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
