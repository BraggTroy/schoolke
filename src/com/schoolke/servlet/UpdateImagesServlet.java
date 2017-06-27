package com.schoolke.servlet;

import com.schoolke.bean.GoodsImages;
import com.schoolke.dao.GoodsDao;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Bragg Troy on 2017/3/20.
 */
@javax.servlet.annotation.WebServlet(name = "UpdateImagesServlet" , urlPatterns = "/updateImages")
public class UpdateImagesServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        int val = 0;
        int i = 0;
        int[] result = new int[5];

        int imagesNum = 0;
        int goodsId = 0;


        //首先，判断是否multipart编码类型
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置缓存区
            ServletContext servletContext = this.getServletContext();
            String tempPath = servletContext.getRealPath("temp");
            // 设置上传路径
            String posterPath = servletContext.getRealPath("/images/goodsImages");
            String resourcePath = servletContext.getRealPath("resource");

            File repository = new File(tempPath);
            factory.setRepository(repository);
            // 创建处理带文件上传的表单处理器
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                // 重新封装request
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        if("imagesNum".equals(item.getFieldName())){
                            imagesNum = Integer.parseInt(item.getString());
                        }
                        if("goodsId".equals(item.getFieldName())){
                            goodsId = Integer.parseInt(item.getString());
                        }
                    }
                }
                for (FileItem item : items) {

                    if (!item.isFormField()) {
                        String clientFilePath = item.getName();
                        if(clientFilePath != null && clientFilePath.length() != 0){
//                            System.out.println("i的值为："+i);
                            GoodsImages goodsImages = new GoodsImages();

                            //	文件扩展名
                            String ext = clientFilePath.substring(clientFilePath.lastIndexOf("."));
                            // 生成随机永不重复的文件名
                            String fileName = UUID.randomUUID().toString() + ext;
                            // 保存到服务器端完整路径(绝对路径)
                            String fullPath = posterPath + "\\" + fileName;

                            String input_name = "pic"+ String.valueOf(i);
                            /*System.out.println("input-name:"+input_name);
                            System.out.println("item.getFieldName():"+item.getFieldName());*/
                            if(input_name.equals(item.getFieldName())){
                                goodsImages.setName(fileName);
                                goodsImages.setGoodsId(goodsId);
//                                System.out.println(fileName+goodsId);
                                // 写入
                                item.write(new File(fullPath));
                                val = new GoodsDao().UpdateGoodsImages(goodsImages);
                                result[i] = val;
                                i++;
                            }

                        }
                    }
                }
            }catch (Exception ex){
                System.out.println("上传商品图片信息异常"+ex.getMessage());
            }
        }

        Map json = new HashMap();
        json.put("result",result);

        JSONObject jsonObject = new JSONObject(json);
        String res = jsonObject.toString();
        out.print(res);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
