package com.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet(name = "/UserController", urlPatterns = { "/UserController" })
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        if ("upload".equals(action)) {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            request.setCharacterEncoding("UTF-8");
            // 1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            factory.setSizeThreshold(1024 * 500);// 设置内存的临界值为500K
            String tempPath = this.getServletConfig().getServletContext().getRealPath("/") + "WebContent/linshi";
            File linshi = new File(tempPath);// 当超过500K的时候，存到一个临时文件夹中
            if (!linshi.exists() || !linshi.isDirectory()) {
                linshi.mkdirs();
            }
            factory.setRepository(linshi);
            upload.setSizeMax(1024 * 1024 * 5);// 设置上传的文件总的大小不能超过5M
            String resultStr = null;
            String text = null;
            Integer quality = 1;
            String uid = getUUID();
            String realFilename = null;
            String filePath = this.getServletConfig().getServletContext().getRealPath("/") + "WebContent/srcImage";
            try {
                // 1. 得到 FileItem 的集合 items
                List<FileItem> items = upload.parseRequest(request);
                // 2. 遍历 items:
                for (FileItem item : items) {
                    // 若是一个一般的表单域, 打印信息
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        String value = item.getString("utf-8");
                        if ("text".equals(name)) {
                            text = value;
                        } else if ("quality".equals(name)) {
                            quality = Integer.valueOf(value);
                        }
                        System.out.println(name + ": " + value);
                    } else {
                        String fileName = item.getName();
                        long sizeInBytes = item.getSize();
                        System.out.println(fileName);
                        System.out.println(sizeInBytes);
                        realFilename = uid + "_" + fileName;
                        // 得到上传文件的扩展名
                        String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
                        List<String> fileFormat = new ArrayList<>();
                        fileFormat.add("png");
                        fileFormat.add("jpg");
                        fileFormat.add("jpeg");
                        if (!fileFormat.contains(fileExtName.toLowerCase())) {
                            session.setAttribute("message", "上传失败,文件类型不合法！");
                            break;
                        }
                        InputStream in = item.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;

                        File path = new File(filePath);
                        if (!path.exists() || !path.isDirectory()) {
                            path.mkdirs();
                        }
                        fileName = filePath + "/" + realFilename;// 文件最终上传的位置
                        System.out.println(fileName);
                        OutputStream out = new FileOutputStream(fileName);
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        out.close();
                        in.close();
                        session.setAttribute("message", "上传成功");
                        resultStr = fileName;
                    }
                }
            } catch (FileUploadException e) {
                session.setAttribute("message", "上传失败");
                e.printStackTrace();
            }
            int srcImageHeight;
            int srcImageWidth;
            if (resultStr != null) {
                InputStream is = new FileInputStream(new File(resultStr));
                BufferedImage srcImage = ImageIO.read(is);
                srcImageHeight = srcImage.getHeight();
                srcImageWidth = srcImage.getWidth();
                if (text == null || "".equals(text)) {
                    text = "爱";
                }
                // 创建新图片
                String newImageSrc = resultStr.substring(0, resultStr.lastIndexOf("/") + 1) + uid + "_"
                        + "newImage.jpg";
                createImage(srcImageWidth * quality / 3, srcImageHeight * quality / 3, newImageSrc);
                // 读取图片文件，得到BufferedImage对象
                BufferedImage bimg = ImageIO.read(new FileInputStream(newImageSrc));
                // 得到Graphics2D 对象
                Graphics2D g2d = (Graphics2D) bimg.getGraphics();
                // 设置画笔粗细
                g2d.setStroke(new BasicStroke(1));
                g2d.setFont(new Font("宋体", Font.PLAIN, 12));
                int size = 0;
                if (quality == 1) {
                    size = 36;
                } else if (quality == 2) {
                    size = 18;
                } else {
                    size = 12;
                }
                for (int i = 0; i <= srcImageWidth - size; i += size) {
                    for (int j = 0; j <= srcImageHeight - size; j += size) {
                        int r = 0, g = 0, b = 0;
                        int[] rgb = null;
                        for (int m = i; m < i + size; m += 3) {
                            for (int n = j; n < j + size; n += 3) {
                                int[] targetRGB = getRGB(srcImage, m, n);
                                r += targetRGB[0];
                                g += targetRGB[1];
                                b += targetRGB[2];
                            }
                        }
                        rgb = new int[] { r / (size * size / 9), g / (size * size / 9), b / (size * size / 9) };
                        // 设置颜色绘制文字
                        g2d.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                        g2d.drawString(text, i * quality / 3, j * quality / 3 + 12);
                    }
                }
                session.setAttribute("realFilename", realFilename);
                // 保存新图片
                String newImgPath = newImageSrc.substring(0, newImageSrc.lastIndexOf("/"));
                newImgPath = newImgPath.substring(0, newImgPath.lastIndexOf("/") + 1) + "resultImg";
                File newImgPathFile = new File(newImgPath);
                if (!newImgPathFile.exists() || !newImgPathFile.isDirectory()) {
                    newImgPathFile.mkdirs();
                }
                ImageIO.write(bimg, "JPG", new FileOutputStream(newImgPath + "/" + realFilename));
                is.close();
                for (int i = 0; i < 10; i++) {
                    System.gc();
                }
                File backgroundImg = new File(filePath + "/" + uid + "_" + "newImage.jpg");
                if (backgroundImg.exists() && backgroundImg.isFile()) {
                    if (backgroundImg.delete()) {
                        System.out.println("删除背景图成功");
                    } else {
                        System.out.println("删除背景图失败");
                    }
                }
                System.out.println("输出新图片");
            }
            if ("上传成功".equals(session.getAttribute("message"))) {
                response.sendRedirect("/result.jsp");
            } else {
                System.out.println("转换失败");
                response.sendRedirect("/index.jsp");
            }
        } else {
            // 下载图片
            String filename = request.getParameter("realFilename");
            String filePath = this.getServletConfig().getServletContext().getRealPath("/") + "WebContent/resultImg";
            // 设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(filePath + "/" + filename);
            // 创建输出流
            OutputStream out = response.getOutputStream();
            // 创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            // 循环将输入流中的内容读取到缓冲区当中
            while ((len = in.read(buffer)) > 0) {
                // 输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            // 关闭文件输入流
            in.close();
            // 关闭输出流
            out.close();
            System.out.println("下载成功！");
        }
    }

    /**
     * 取得图像上指定位置像素的 rgb 颜色分量。
     *
     * @param image 源图像。
     * @param x      图像上指定像素位置的x坐标。
     * @param y      图像上指定像素位置的y坐标。
     * @return 返回包含rgb 颜色分量值的数组。元素 index 由小到大分别对应 r，g，b。
     */
    public static int[] getRGB(BufferedImage image, int x, int y) {
        System.out.println(x + " " + y);
        int[] rgb = new int[3];
        int pixel = image.getRGB(x, y);
        rgb[0] = (pixel & 0xff0000) >> 16;
        rgb[1] = (pixel & 0xff00) >> 8;
        rgb[2] = (pixel & 0xff);
        return rgb;
    }

    /**
     * 创建背景图片
     *
     * @param width   图片宽
     * @param height  图片高
     * @param src     图片路径
     */
    public static void createImage(int width, int height, String src) {
        File file = new File(src);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setBackground(Color.BLACK);
        g2.clearRect(0, 0, width, height);
        try {
            ImageIO.write(bi, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
