package com.siam.package_util.util;

import com.alibaba.fastjson.JSONObject;
import com.siam.package_common.util.CloseableHttpClientUtil;
import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_common.util.Sha1Utils;
import com.siam.package_util.model.dto.OrderDetailPrintDto;
import com.siam.package_util.model.dto.OrderPrintDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 芯烨云打印机开放 API
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(value = "xinyeyun")
public class XinYeYunUtils {

    private String user;

    private String key;

    private String domain;

    private ApiUrl apiUrl;

    @Data
    public static class ApiUrl {
        private String addPrinters;
        private String setVoiceType;
        private String print;
        private String playVoiceExt;
        private String playCustomVoice;
    }

    /**
     * 打印后厨总单
     * @param isKitchenTotalOrder
     * @param orderPrintDto
     * @param orderDetailPrintDto
     * @param orderDetailPrintDtoList
     * @return
     */
    public JSONObject kitchenTotalOrderDataPrint(OrderPrintDto orderPrintDto, List<OrderDetailPrintDto> orderDetailPrintDtoList) {
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
        int mmLetterSizeLen = mm.getInteger("mmLetterSizeLen");
        String character = "";
        for (int i = 0; i < mmLetterSizeLen; i++) {
            character = character + "-";
        }

        //门店后厨单打印
        String backKitchenMenuContent = "<C>" + character + "<BR></C>" +
//                "<RH n=\"2\"><TABLE col=\"24,24\" w=2 h=2><tr>桌号：" + orderPrintDto.getTableName() + "<td>下单人：" + orderPrintDto.getContactRealName() + "</tr></TABLE><BR>" +
                "<RH n=\"2\"><L><B>下单人：" + orderPrintDto.getContactRealName() + "</B><BR>" +
                "<BOLD></BOLD><BR>" +
                "<BOLD>时间：" + orderPrintDto.getOrderTime() + "</BOLD><BR></L>" +
                "<C>" + character + "<BR>" +
                "<B>        </B><BR></C>" +
                "<L><B>备注：" + orderPrintDto.getRemark() + "</B><BR></L>" +
                "<BR><BR>" +
                "<C>" + character + "<BR></C></RH>" +
                "<L><TABLE col=\"22,18,8\" w=2 h=2><tr>商品名称<td>规格<td>数量</tr></TABLE>";
        JSONObject printResultData = new JSONObject();

        String backKitchenFoodListContent = "";
        for (OrderDetailPrintDto food : orderDetailPrintDtoList) {
            String foodContent = getFoodContent(food);
            backKitchenFoodListContent = backKitchenFoodListContent + foodContent;
        }
        String titleContent = "<C><B>总单</B><BR></C>";
        String bottomContent = "<BR></L>" + character;
        backKitchenMenuContent = titleContent + backKitchenMenuContent + backKitchenFoodListContent + bottomContent;
        printResultData = print(orderPrintDto.getPrinterSn(), backKitchenMenuContent, "1", "2", "1");

        //门店总氮打印
        return printResultData;
    }

    /**
     * 打印后厨单商品
     * @param orderPrintDto
     * @param orderDetailPrintDto
     * @return
     */
    public JSONObject kitchenTotalOrderDataPrint(OrderPrintDto orderPrintDto, OrderDetailPrintDto orderDetailPrintDto) {
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
        int mmLetterSizeLen = mm.getInteger("mmLetterSizeLen");
        String character = "";
        for (int i = 0; i < mmLetterSizeLen; i++) {
            character = character + "-";
        }

        //门店后厨单打印
        String backKitchenMenuContent = "<C>" + character + "<BR></C>" +
//                "<RH n=\"2\"><TABLE col=\"24,24\" w=2 h=2><tr>桌号：" + orderPrintDto.getTableName() + "<td>下单人：" + orderPrintDto.getContactRealName() + "</tr></TABLE><BR>" +
                "<RH n=\"2\"><L><B>下单人：" + orderPrintDto.getContactRealName() + "</B><BR>" +
                "<BOLD></BOLD><BR>" +
                "<BOLD>时间：" + orderPrintDto.getOrderTime() + "</BOLD><BR></L>" +
                "<C>" + character + "<BR>" +
                "<B>        </B><BR></C>" +
                "<L><B>备注：" + orderPrintDto.getRemark() + "</B><BR></L>" +
                "<BR><BR>" +
                "<C>" + character + "<BR></C></RH>" +
                "<L><TABLE col=\"22,18,8\" w=2 h=2><tr>商品名称<td>规格<td>数量</tr></TABLE>";
        JSONObject printResultData = new JSONObject();

        String foodContent = getFoodContent(orderDetailPrintDto);
        String titleContent = "<C><B>" + orderDetailPrintDto.getPrinterName() + "</B><BR></C>";
        String bottomContent = "<BR></L>";
        for (int i = 0; i < orderDetailPrintDto.getPrintNum(); i++) {
            String content = titleContent + backKitchenMenuContent + foodContent + bottomContent;
            printResultData = print(orderDetailPrintDto.getPrinterSn(), content, "1", "2", "1");
        }

        //门店总氮打印
        return printResultData;
    }

    /**
     * 获取商品数据
     *
     * @param orderDetailPrintDto 订单商品信息
     */
    public String getFoodContent(OrderDetailPrintDto orderDetailPrintDto) {
        String foodContent = "<TABLE col=\"22,18,8\" w=2 h=2><tr>" + orderDetailPrintDto.getGoodsName() + "<td>" +
                orderDetailPrintDto.getGoodsSpecs() + "<td>" + orderDetailPrintDto.getNumber() + orderDetailPrintDto.getUnitName() + "</tr></TABLE><BR>";
        return foodContent;
    }

    /**
     * 结账单打印
     *
     * @param orderPrintDto 订单信息
     * @param printingMenuList  订单商品信息
     */
    public JSONObject checkoutDataPrint(OrderPrintDto orderPrintDto, List<OrderDetailPrintDto> printingMenuList) {
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
        int mmLetterSizeLen = mm.getInteger("mmLetterSizeLen");
        String character = "";
        for (int i = 0; i < mmLetterSizeLen; i++) {
            character = character + "-";
        }
        String checkoutFoodListContent = "";

        String printTime = DateUtilsPlus.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");

        //用户结账单打印
        String checkoutMenuContent = "<C><B>" + orderPrintDto.getShopName() + "</B><BR></C>" +
                "<C>" + character + "<BR></C>" +
                "<RH n=\"2\"><C><B>结账单<BR>" +
                "#" + orderPrintDto.getQueueNo() + "</B><BR></C>" +
                "<L><BOLD>订单号：" + orderPrintDto.getOrderNo() + "<BR>" +
                "商品总数：" + orderPrintDto.getGoodsTotalQuantity() + "<BR>" +
                "下单时间：" + orderPrintDto.getOrderTime() + "<BR>" +
                "结账时间：" + orderPrintDto.getPaymentTime() + "</BOLD><BR></L></RH>" +
                character + "<BR>" +
                "<TABLE col=\"24,14,10\" w=1 h=1><tr>商品名称<td>数量<td>小计</tr></TABLE>" +
                character + "<BR>";
        for (OrderDetailPrintDto food : printingMenuList) {
            String foodContent = "<TABLE col=\"24,14,10\" w=2 h=2><tr>" + food.getGoodsName() + "<td>" + "x" + food.getNumber() + "<td>" + food.getSubtotal() + "</tr></TABLE>" +
                    "<L><TABLE col=\"46,2\"><tr>" + food.getGoodsSpecs() + "<td> </tr></TABLE></L> <BR>";
            checkoutFoodListContent = checkoutFoodListContent + foodContent;
        }
        String characterContent = "结算清单";
        String characterText = character.substring(0, (mmLetterSizeLen / 2) - characterContent.length());
        String discountText = "";
        if (orderPrintDto.getFullReductionRuleId() != null) {
            discountText = discountText + "<TABLE col=\"24,24\" w=1 h=1><tr>满减优惠<td>" + orderPrintDto.getFullReductionRuleDescription() + "</tr></TABLE>";
        }
        if (orderPrintDto.getCouponsId() != null) {
            discountText = discountText + "<TABLE col=\"24,24\" w=1 h=1><tr>使用优惠券<td>" + orderPrintDto.getCouponsDescription() + "</tr></TABLE>";
        }
        String payTypeName = "";
        if (StringUtils.isNotBlank(orderPrintDto.getPayTypeName())) {
            payTypeName = "<L><BOLD>支付方式：" + payTypeName + "</BOLD><BR></L>";
        }
        checkoutMenuContent = checkoutMenuContent + checkoutFoodListContent + "<BR></L>" +
                "<TABLE col=\"24,24\" w=1 h=1><tr>配送费<td>" + orderPrintDto.getDeliveryFee() + "</tr></TABLE>" +
                "<TABLE col=\"24,24\" w=1 h=1><tr>包装费<td>" + orderPrintDto.getPackingCharges() + "</tr></TABLE>" +
                discountText +
                characterText + characterContent + characterText + "<BR>" +
                "<TABLE col=\"24,24\" w=2 h=2><tr>实收金额<td>￥" + orderPrintDto.getActualPrice() + "</tr></TABLE><RH n=\"2\"><BR>" +
                payTypeName +
                character + "<BR>" +
                "<L><BOLD>门店地址：" + orderPrintDto.getShopAddress() + "<BR>" +
                "门店电话：" + orderPrintDto.getShopContactNumber() + "</BOLD><BR></L>" +
                "<C><BOLD>谢谢惠顾，欢迎下次光临<BR>" +
                printTime + "</BOLD><BR></C></RH>";
        return print(orderPrintDto.getPrinterSn(), checkoutMenuContent, "1", "2", "1");
    }

    /**
     * 打印小票订单
     *
     * @param content 打印内容,打印内容使用 GBK 编码判断,不能超过12K
     * @param copies  打印份数，默认为 1，最大值为 5，超出范围将视为无效参数
     * @param voice   声音播放模式，0 为取消订单模式，1 为静音模式，2 为来单播放模式，3 为有用户申请退单了，默认为2 来单播放模式
     * @return
     */
    public JSONObject print(String sn, String content, String copies, String voice, String mode) {
        JSONObject paramsObj = new JSONObject();
        paramsObj.put("content", content);
        paramsObj.put("copies", copies);
        paramsObj.put("voice", voice);
        paramsObj.put("mode", mode);
        paramsObj.put("sn", sn);
        JSONObject params = commonParams(paramsObj);

        JSONObject result_data = CloseableHttpClientUtil.sendPost(this.getApiUrl().getPrint(), params.toJSONString());
        return jsonDataAnalysis(params, result_data);
    }

    /**
     * 公共参数设置
     * @param params
     * @return
     */
    public JSONObject commonParams(JSONObject params) {
        JSONObject signParams = new JSONObject();

        signParams.put("user", user);
        signParams.put("key", key);
        signParams.put("timestamp", System.currentTimeMillis());

        JSONObject commonParams = new JSONObject();
        commonParams.putAll(signParams);
        //signParams.putAll(params);

        String signKey = getNotNullSignStr(signParams, "");
        commonParams.put("sign", signKey);

//        commonParams.put("debug", "1");
        commonParams.putAll(params);
        return commonParams;
    }

    /**
     * 获取签名
     * @param paramMap
     * @param secret
     * @return
     */
    public String getNotNullSignStr(Map<String, Object> paramMap, String secret) {
        StringBuilder builder = new StringBuilder();
        Set<String> paramsSet = paramMap.keySet();
        for (String param : paramsSet) {
            Object value = paramMap.get(param);
            builder.append(value);
        }

        String encryptStr = builder + "";
        log.info("芯烨云开放API请求参数签名前拼接字符串：" + encryptStr);
        // 步骤三，MD5进行加密，转化为大写
        String sign = Sha1Utils.sha1(encryptStr);
        return sign;
    }

    /**
     * 按照ASCII码进行由小到大排序
     *
     * @param paramList 传入的参数
     * @return
     */
    public List<String> sortParams(List<String> paramList) {
        Collections.sort(paramList);
        return paramList;
    }

    public JSONObject jsonDataAnalysis(JSONObject params, JSONObject result) {
        log.info("芯烨云开放API接口请求参数={},芯烨云开放API接口返回参数={}", params, result);
        JSONObject result_data = new JSONObject();
        if (result.getBoolean("success")) {
            JSONObject data = result.getJSONObject("data");
            if (data.getInteger("code") == 0) {
                result_data.put("success", data.getInteger("code") == 0 ? true : false);
                result_data.put("code", data.getInteger("code"));
                result_data.put("message", data.getString("msg"));
                result_data.put("data", data);
                return result_data;
            }
            result_data.put("success", false);
            result_data.put("code", data.getInteger("resCode"));
            result_data.put("message", data.getString("resMsg"));
            return result_data;
        }
        return result;
    }

    /**
     * 添加打印机到开发者账户（可批量） 【必接】
     *
     * @param items 数组元素为 json 对象：{"name":"打印机名称","sn":"打印机编号"}
     * @return
     */
    public JSONObject addPrinters(String items) {
        JSONObject paramsObj = new JSONObject();
        paramsObj.put("items", items);
        JSONObject params = this.commonParams(paramsObj);
        JSONObject result_data = CloseableHttpClientUtil.sendPost(this.getApiUrl().getAddPrinters(), params.toJSONString());
        return jsonDataAnalysis(params, result_data);
    }

    /**
     * 设置打印机语音类型
     *
     * @param voiceType   声音类型：
     *                    打印机固件版本为V10.xx的机器取值： 0 真人语音（大） 1 真
     *                    人语音（中） 2 真人语音（小） 3 嘀嘀声 4 静音
     *                    其它固件版本的机器取值：0 真人语音 3 嘀嘀声 4 静音
     * @param volumeLevel 声音大小：0 大 1 中 2 小 3 关闭
     *                    说明：打印机固件版本为非 V10.xx 的机器支持此参数
     * @return
     */
    public JSONObject setVoiceType(String voiceType, String volumeLevel) {
        JSONObject paramsObj = new JSONObject();
        paramsObj.put("voiceType", voiceType);
        paramsObj.put("volumeLevel", volumeLevel);
        JSONObject params = this.commonParams(paramsObj);
        JSONObject result_data = CloseableHttpClientUtil.sendPost(this.getApiUrl().getSetVoiceType(), params.toJSONString());
        return jsonDataAnalysis(params, result_data);
    }

    /**
     * 扩展语音播报
     *
     * @param content       语音播报内容，如#62，将播报序号为 62 对应的语音内容。
     *                      该内容需加入芯烨云技术支持 QQ 群（856926694）咨询
     * @param voiceTime     语音播报次数，默认为 1 次
     * @param voiceInterval 语音播报多次，当前次播报与下一次播报的时间间隔，只有
     *                      当播报次数大于 1 时才有效。
     * @return
     */
    public JSONObject playVoiceExt(String content, String voiceTime, String voiceInterval) {
        JSONObject paramsObj = new JSONObject();
        paramsObj.put("content", content);
        paramsObj.put("voiceTime", voiceTime);
        paramsObj.put("voiceInterval", voiceInterval);
        JSONObject params = this.commonParams(paramsObj);
        JSONObject result_data = CloseableHttpClientUtil.sendPost(this.getApiUrl().getPlayVoiceExt(), params.toJSONString());
        return jsonDataAnalysis(params, result_data);
    }

    /**
     * 自定义语音播报【已废弃，所有款式打印机均不支持该功能】
     *
     * @param content       语音播报内容可以自由指定内容，具体可以参考下发样例，
     *                      目前仅支持汉语播报
     * @param voiceTime     语音播报次数，默认为 1 次
     * @param voiceInterval 语音播报多次，当前次播报与下一次播报的时间间隔，只有
     *                      当播报次数大于 1 时才有效。
     * @return
     */
    public JSONObject playCustomVoice(String sn, String content, String voiceTime, String voiceInterval) {
        JSONObject paramsObj = new JSONObject();
        paramsObj.put("content", content);
        paramsObj.put("voiceTime", voiceTime);
        paramsObj.put("voiceInterval", voiceInterval);
        paramsObj.put("sn", sn);
        JSONObject params = this.commonParams(paramsObj);
        JSONObject result_data = CloseableHttpClientUtil.sendPost(this.getApiUrl().getPlayCustomVoice(), params.toJSONString());
        return jsonDataAnalysis(params, result_data);
    }
}