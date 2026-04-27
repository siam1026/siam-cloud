package com.siam.package_common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片合成服务类
 */
@Data
@Slf4j
@Component
public class ImageComposeUtils {

    @Autowired
    private OSSUtils ossUtils;

    /**
     * 图片合成
     */
    public void compoundImage(String mainImagePath, String qrCodePath, String savePath, String vipNo) throws IOException {
        HttpURLConnection httpUrl = (HttpURLConnection) new URL(mainImagePath).openConnection();
        httpUrl.connect();

        HttpURLConnection httpUrl_qrcode = (HttpURLConnection) new URL(qrCodePath).openConnection();
        httpUrl_qrcode.connect();

        try {
            BufferedImage big = ImageIO.read(httpUrl.getInputStream());
            BufferedImage erweima = ImageIO.read(httpUrl_qrcode.getInputStream());
            int width = 1080;
            int height = 1854;
            Image image = big.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics2D g = bufferedImage2.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.drawImage(erweima, 320, 910, 460, 460, null);
            Font font = new Font("SansSerif", Font.BOLD, 38);
            g.setFont(font);
            g.setPaint(Color.BLACK);
            String text = vipNo;
            g.drawString(text, 350, 850);
            g.dispose();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage2, "png", os);
            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());

            ossUtils.uploadImage(inputStream, savePath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpUrl.disconnect();
            httpUrl_qrcode.disconnect();
        }
    }

    /**
     * 获取字符串宽度（像素）
     */
    public int getWordWidth(Font font, String content) {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        return (int) font.getStringBounds(content, frc).getWidth();
    }

    public File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
        if (file.exists()) {
            return file;
        }
        ins.close();
        return file;
    }

}
