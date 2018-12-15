package com.tool;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Iterator;

/**
 * reference:https://blog.csdn.net/u010361786/article/details/80511073
 */
public class ImgUtil {

    /**
     * 旋转图片调用方法
     */
    public static void judgeRotate(String fileName) {
        //获取图片旋转角度
        int angel = getRotateAngleForPhoto(fileName);
        if (angel > 0 && angel < 360) {
            rotateImage(new File(fileName), angel);
        }
    }

    /**
     * 图片翻转时，计算图片翻转到正常显示需旋转角度
     */
    private static int getRotateAngleForPhoto(String fileName) {
        File file = new File(fileName);
        int angel = 0;
        try {
            //核心对象操作对象
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            //获取所有不同类型的Directory，如ExifSubIFDDirectory, ExifInteropDirectory, ExifThumbnailDirectory等，这些类均为ExifDirectoryBase extends Directory子类
            //分别遍历每一个Directory，根据Directory的Tags就可以读取到相应的信息
            int orientation = 0;
            Iterable<Directory> iterable = metadata.getDirectories();
            for (Iterator<Directory> iter = iterable.iterator(); iter.hasNext(); ) {
                Directory dr = iter.next();
                if (dr.getString(ExifIFD0Directory.TAG_ORIENTATION) != null) {
                    orientation = dr.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                }
                /*Collection<Tag> tags = dr.getTags();
                for (Tag tag : tags) {
                    System.out.println(tag.getTagName() + "： " + tag.getDescription());
                }*/
            }
            if (orientation == 0 || orientation == 1) {
                angel = 360;
            } else if (orientation == 3) {
                angel = 180;
            } else if (orientation == 6) {
                angel = 90;
            } else if (orientation == 8) {
                angel = 270;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return angel;
    }

    /**
     * 旋转图片为指定角度,并输出到源路径
     *
     * @param file  目标图像
     * @param angel 旋转角度
     * @return File
     */
    private static File rotateImage(File file, final int angel) {
        BufferedImage src = null;
        try {
            src = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);

        BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();

        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        try {
            ImageIO.write(bi, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 调用方法输出图片文件
        outImage(file.getPath(), bi, (float) 0.5);
        return file;
    }

    /**
     * 计算旋转参数
     */
    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree,we need to do some conversion.
        if (angel > 90) {
            if (angel / 9 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }

    /**
     * 将图片文件输出到指定的路径，并可设定压缩质量
     *
     * @param outImgPath
     * @param newImg
     * @param quality
     */
    private static void outImage(String outImgPath, BufferedImage newImg, float quality) {
        // 判断输出的文件夹路径是否存在，不存在则创建
        File file = new File(outImgPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 输出到文件流
        FileChannel fc;
        try {
            FileOutputStream newimage = new FileOutputStream(outImgPath);
            //获取图片大小
            fc = newimage.getChannel();
            //1M
            if (fc.size() > 1 * 1024 * 1024) {
                quality = (float) (1 * 1024 * 1024.00 / fc.size() * 0.5);
            }

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(newImg);
            // 压缩质量
            jep.setQuality(quality, true);
            encoder.encode(newImg, jep);
            newimage.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ImageFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * * 图片文件读取
     *
     * @param srcImgPath
     * @return
     */
    public static BufferedImage inputImage(String srcImgPath) {
        BufferedImage srcImage = null;
        File file = new File(srcImgPath);
        try {
            // 构造BufferedImage对象
            FileInputStream in = new FileInputStream(file);
            byte[] b = new byte[5];
            in.read(b);
            srcImage = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println("读取图片文件出错！" + e.getMessage());
            e.printStackTrace();
        }
        return srcImage;
    }

}
