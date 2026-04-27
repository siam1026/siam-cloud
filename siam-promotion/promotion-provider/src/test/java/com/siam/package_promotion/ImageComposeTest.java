package com.siam.package_promotion;

import com.alibaba.fastjson.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageComposeTest {

    public static void main(String[] args) throws IOException {
        CompoundImage("https://deerspot.oss-cn-hangzhou.aliyuncs.com/test/siamCompose.png", "https://deerspot.oss-cn-hangzhou.aliyuncs.com/test/eb831e8f0b42b8b3a8b4360c11a2b5e.jpg", "", "C:\\Users\\Administrator\\Desktop\\testComponse.png", "postion");
    }

    public static String CompoundImage(String mainImgPath, String qrcodePath, String text, String compoundPath, String databasePath) throws IOException {
        String destUrl = mainImgPath;
        HttpURLConnection httpUrl = (HttpURLConnection) new URL(destUrl).openConnection();
        httpUrl.connect();

        String destUrl_qrcode = "https://deerspot.oss-cn-hangzhou.aliyuncs.com/test/testQrcode.png";
        HttpURLConnection httpUrl_qrcode = (HttpURLConnection) new URL(destUrl_qrcode).openConnection();
        httpUrl_qrcode.connect();

        String ewmurl = "";
        try {
            BufferedImage big = ImageIO.read(httpUrl.getInputStream());
            URL url = new URL("https://img-blog.csdn.net/20150906104118760");
            BufferedImage erweima = ImageIO.read(httpUrl_qrcode.getInputStream());
            int width = 1080;
            int height = 1854;
            Image image = big.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics2D g = bufferedImage2.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.drawImage(erweima, 20, 1500, 300, 300, null);
            Font font = new Font("SansSerif", Font.BOLD, 38);
            g.setFont(font);
            g.setPaint(Color.BLACK);
            int wordWidth = getWordWidth(font, text);
            int i = width / 2;
            int i1 = (i - wordWidth) / 2;
            int numWidth = i + i1;
            g.drawString(text, numWidth - 10, 310 + 28);
            g.dispose();

            ewmurl = compoundPath;
            ImageIO.write(bufferedImage2, "jpg", new File(ewmurl));
            System.out.println("图片生成完毕");
            ewmurl = databasePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpUrl.disconnect();
        httpUrl_qrcode.disconnect();
        return ewmurl;
    }

    public static int getWordWidth(Font font, String content) {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        return (int) font.getStringBounds(content, frc).getWidth();
    }

    public static File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
        if (file.exists()) {
            return file;
        }
        ins.close();
        return file;
    }

    // ... rest of the test methods (qrcode, sendPost, getToken) remain unchanged

    public static void qrcode() {
        try {
            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + getToken());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            JSONObject paramJson = new JSONObject();
            paramJson.put("scene", "inviterId=111");
            paramJson.put("page", "pages/login/choose/choose");
            paramJson.put("width", 430);
            JSONObject lineColor = new JSONObject();
            lineColor.put("r", 0);
            lineColor.put("g", 0);
            lineColor.put("b", 0);
            paramJson.put("line_color", lineColor);
            printWriter.write(paramJson.toString());
            printWriter.flush();
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            OutputStream os = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\testQrcode.png"));
            BufferedImage read = ImageIO.read(httpURLConnection.getInputStream());
            if (read == null) {
                System.out.println("null");
            } else {
                ImageIO.write(read, "png", new File("C:\\Users\\Administrator\\Desktop\\testQrcode.png"));
            }
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String getToken() {
        try {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("grant_type", "client_credential");
            map.put("appid", "wx2e1a8193d3ed12fe");
            map.put("secret", "2774e3a86dc30fbf1ac63d81b56f2291");
            String rt = sendPost("https://api.weixin.qq.com/cgi-bin/token", map);
            JSONObject json = JSONObject.parseObject(rt);
            if (json.getString("access_token") != null) {
                return json.getString("access_token");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
