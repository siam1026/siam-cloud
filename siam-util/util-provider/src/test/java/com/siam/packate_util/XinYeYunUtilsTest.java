package com.siam.packate_util;

import com.alibaba.fastjson.JSONObject;
import com.siam.package_util.UtilApplication;
import com.siam.package_util.util.XinYeYunUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UtilApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties
public class XinYeYunUtilsTest {

    @Autowired
    private XinYeYunUtils xinYeYunUtils;

    @Test
    public void playCustomVoice_test() {
        xinYeYunUtils.playCustomVoice("", "来订单了", "1", "1");
    }

    @Test
    public void setVoiceType_test() {
        xinYeYunUtils.setVoiceType("0", "0");
    }

    @Test
    public void addPrinters_test() {
        List<JSONObject> items = new ArrayList<>();
        JSONObject item = new JSONObject();
        item.put("sn", "14JQK87GKXD5649");
        item.put("name", "kkrgjouyoo");
        items.add(item);
        xinYeYunUtils.addPrinters(JSONObject.toJSONString(items));
    }

    @Test
    public void print_test() {
        //58mm的机器，一行打印 16 个汉字，32 个字母
        //80mm的机器，一行打印 24 个汉字，48 个字母
        List<JSONObject> mmList = new ArrayList<>();
        JSONObject mm = new JSONObject();
        mm.put("mm", 80);//打印机大小类型
        mm.put("mmFontSizeLen", 24);//打印机大小类型
        mm.put("mmLetterSizeLen", 48);//打印机大小类型
        mm.put("titleCheckoutSpaceNum", 14);//打印机空格长度
        mm.put("titleBackKitchenSpaceNum", 14);//打印机空格长度
        mmList.add(mm);
        List<JSONObject> menuList = new ArrayList<>();
        JSONObject menu = new JSONObject();
        menu.put("foodName", "金钱班");
        menu.put("foodNum", 1);
        menu.put("foodUnit", "条");
        menu.put("foodWeight", "22两");
        menu.put("foodSpecs", "头/白灼");
        menu.put("foodPrice", "33.3");
        menu.put("title", "海鲜");
        menu.put("tableNumber", "13");
        menuList.add(menu);
        JSONObject menu1 = new JSONObject();
        menu1.put("foodName", "剁椒鱼头");
        menu1.put("foodNum", 1);
        menu1.put("foodUnit", "个");
        menu1.put("foodSpecs", "大/加配菜");
        menu1.put("foodPrice", "34.3");
        menu1.put("title", "燕翅鲍");
        menu1.put("tableNumber", "13");
        menuList.add(menu1);
        JSONObject menu2 = new JSONObject();
        menu2.put("foodName", "酸辣土豆丝炒牛肉");
        menu2.put("foodNum", 1);
        menu2.put("foodUnit", "份");
        menu2.put("foodSpecs", "不酸/不加牛肉");
        menu2.put("foodPrice", "44.3");
        menu2.put("title", "爆炒");
        menu2.put("tableNumber", "13");
        menuList.add(menu2);

        int titleBackKitchenSpaceNum = mm.getInteger("titleBackKitchenSpaceNum");
        int titleCheckoutSpaceNum = mm.getInteger("titleCheckoutSpaceNum");
        int mmLetterSizeLen = mm.getInteger("mmLetterSizeLen");
        String character = "";
        for (int i = 0; i < mmLetterSizeLen; i++) {
            character = character + "-";
        }
        //门店后厨单打印
        String backKitchenMenuContent = "<C>" + character + "<BR></C>" +
                "<RH n=\"2\"><L><B>桌号：32<HT>下单人：***</B><BR>" +
                "<BOLD>时间：2024-06-24 15:24:00</BOLD><BR></L>" +
                "<C><BOLD>" + character + "<BR></C>" +
                "<C><B>     测    试   </B><BR></C>" +
                "<L><B>备注：免辣~</B><BR></L>" +
                "<BR><BR>" +
                "<C>" + character + "<BR></C></RH>" +
                "<L><POS>1B3302</POS><TABLE col=\"22,18,8\" w=2 h=2><tr>商品名称<td>规格<td>数量</tr>";
        String backKitchenFoodListContent = "", checkoutFoodListContent = "";
        for (JSONObject food : menuList) {
            String foodName = food.getString("foodName");
            String foodUnit = food.getString("foodUnit");
            String title = food.getString("title");
            int foodNum = food.getInteger("foodNum");
            String foodSpecs = food.getString("foodSpecs");
            String titleContent = "<C><B>" + title + "</B><BR></C>";
            String foodContent = "<tr>" + foodName + "<td>" + foodSpecs + "<td>" + foodNum + "</tr>";
            xinYeYunUtils.print("14JQK87GKXD5649", titleContent + backKitchenMenuContent + foodContent + "</TABLE><BR></L>", "1", "2", "1");
            backKitchenFoodListContent = backKitchenFoodListContent + foodContent;
        }
        String titleContent = "<C><B>总单</B><BR></C>";
        String bottomContent = "</TABLE><POS>1B32</POS><BR></L>" + character + "<TABLE col=\"24,24\" w=2 h=2><tr>32<td>32</tr></TABLE>";
        backKitchenMenuContent = titleContent + backKitchenMenuContent + backKitchenFoodListContent + bottomContent;
        //门店总氮打印
        xinYeYunUtils.print("14JQK87GKXD5649", backKitchenMenuContent, "1", "2", "1");

        //用户结账单打印
        String checkoutMenuContent = "<C><B>店铺名称</B><BR></C>" +
                "<C>" + character + "<BR></C>" +
                "<RH n=\"2\"><C><BOLD>结账单</BOLD><BR></C>" +
                "<C><BOLD>#12</BOLD><BR></C>" +
                "<L><BOLD>订单号：20240602174200</BOLD><BR>" +
                "<BOLD>商品总数：1</BOLD><BR>" +
                "<BOLD>下单时间：2024-06-02 17:37</BOLD><BR>" +
                "<BOLD>结账时间：2024-06-02 17:45</BOLD><BR></RH>" +
                character + "<BR></L>" +
                "<L><TABLE col=\"22,18,8\" w=2 h=2><tr>商品名称<td>数量<td>小计</tr>" +
                character + "<BR>";
        for (JSONObject food : menuList) {
            String foodName = food.getString("foodName");
            String foodUnit = food.getString("foodUnit");
            String foodPrice = food.getString("foodPrice");
            int foodNum = food.getInteger("foodNum");
            String foodSpecs = food.getString("foodSpecs");
            String foodContent = "<tr>" + foodName + "<td>" + foodNum + "<td>" + foodPrice + "</tr>" +
                    "<BOLD>" + foodSpecs + "</BOLD><BR>";
            checkoutFoodListContent = checkoutFoodListContent + foodContent;
        }
        String characterText = character.substring(0, (mmLetterSizeLen / 2) - 4);
        checkoutMenuContent = checkoutMenuContent + checkoutFoodListContent + "</TABLE><BR></L>" +
                characterText + "结算清单" + characterText + "<BR>" +
                "<RH n=\"2\"><L><BOLD>实收金额：￥1</BOLD><BR></L>" +
                "<L><BOLD>支付方式：现金支付</BOLD><BR></L>" +
                character + "<BR>" +
                "<L><BOLD>门店地址：************************************</BOLD><BR></C>" +
                "<L><BOLD>门店电话：1557*******</BOLD><BR></C></RH>" +
                "<C><BOLD>谢谢回顾，欢迎下次光临</BOLD><BR></C>";
        xinYeYunUtils.print("14JQK87GKXD5649", checkoutMenuContent, "1", "2", "1");
    }
}