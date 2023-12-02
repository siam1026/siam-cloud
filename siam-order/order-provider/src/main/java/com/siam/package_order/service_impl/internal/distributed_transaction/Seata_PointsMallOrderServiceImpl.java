package com.siam.package_order.service_impl.internal.distributed_transaction;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.constant.RocketMQConst;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_weixin_basic.service.WxNotifyService;
import com.siam.package_weixin_basic.service.WxPublicPlatformNotifyService;
import com.siam.package_feign.mod_feign.goods.*;
import com.siam.package_feign.mod_feign.goods.internal.*;
import com.siam.package_feign.mod_feign.user.MemberBillingRecordFeignClient;
import com.siam.package_feign.mod_feign.user.MemberFeignClient;
import com.siam.package_feign.mod_feign.user.MemberInviteRelationFeignClient;
import com.siam.package_feign.mod_feign.user.MemberTradeRecordFeignClient;
import com.siam.package_goods.entity.internal.PointsMallCoupons;
import com.siam.package_goods.entity.internal.PointsMallCouponsMemberRelation;
import com.siam.package_goods.entity.internal.PointsMallFullReductionRule;
import com.siam.package_goods.entity.internal.PointsMallGoods;
import com.siam.package_order.controller.member.WxPayService;
import com.siam.package_order.entity.DeliveryAddress;
import com.siam.package_order.entity.internal.PointsMallOrder;
import com.siam.package_order.entity.internal.PointsMallOrderDetail;
import com.siam.package_order.mapper.internal.PointsMallOrderMapper;
import com.siam.package_order.model.example.internal.PointsMallOrderExample;
import com.siam.package_order.model.param.internal.PointsMallOrderParam;
import com.siam.package_order.model.result.internal.PointsMallOrderResult;
import com.siam.package_order.model.vo.internal.PointsMallOrderVo;
import com.siam.package_order.service.*;
import com.siam.package_order.service.internal.*;
import com.siam.package_user.auth.cache.MemberSessionManager;
import com.siam.package_user.entity.Member;
import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_common.util.GenerateNo;
import com.siam.package_common.util.GsonUtils;
import com.siam.package_common.util.RedisUtils;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分布式事务-seata
 *
 * @return
 * @author 暹罗
 */
@Slf4j
@Service
public class Seata_PointsMallOrderServiceImpl implements PointsMallOrderService {

    public static final String USER_ORDER_TOKEN_PREFIX = "order:token:";

    @Autowired
    private PointsMallOrderMapper pointsMallOrderMapper;

    @Autowired
    private PointsMallOrderDetailService orderDetailService;

    @Autowired
    private SettingFeignClient settingFeignClient;

    @Autowired
    private MemberBillingRecordFeignClient memberBillingRecordFeignClient;

    @Autowired
    private MemberFeignClient memberFeignClient;

    @Autowired
    private MemberTradeRecordFeignClient memberTradeRecordFeignClient;

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    private WxNotifyService wxNotifyService;

    @Autowired
    private WxPublicPlatformNotifyService wxPublicPlatformNotifyService;

    @Autowired
    private PointsMallOrderRefundService orderRefundService;

    @Autowired
    private MemberInviteRelationFeignClient memberInviteRelationFeignClient;

    @Autowired
    private PointsMallOrderLogisticsService orderLogisticsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private VipRechargeRecordFeignClient vipRechargeRecordFeignClient;

    @Autowired
    private PointsMallOrderRefundGoodsService orderRefundGoodsService;

    @Autowired
    private PointsMallOrderRefundProcessService orderRefundProcessService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Autowired
    private CommonService commonService;

    @Autowired
    private PointsMallShoppingCartFeignClient shoppingCartFeignClient;

    @Autowired
    private PointsMallGoodsFeignClient goodsFeignClient;

    @Autowired
    private PointsMallGoodsSpecificationOptionFeignClient goodsSpecificationOptionFeignClient;

    @Autowired
    private PointsMallCouponsMemberRelationFeignClient couponsMemberRelationFeignClient;

    @Autowired
    private PointsMallCouponsFeignClient couponsFeignClient;

    @Autowired
    private PointsMallCouponsGoodsRelationFeignClient couponsGoodsRelationFeignClient;

    @Autowired
    private PointsMallFullReductionRuleFeignClient fullReductionRuleFeignClient;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int countByExample(PointsMallOrderExample example) {
        return 0;
    }

    @Override
    public void deleteByPrimaryKey(Long id) {

    }

    @Override
    public String createOrderToken(PointsMallOrderParam param) {
        return null;
    }

    @Override
    @GlobalTransactional(timeoutMills = 60000, name = "spring-cloud-seata", rollbackFor = Exception.class)
    public PointsMallOrder insert(PointsMallOrderParam param) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        //TODO(MARK) - 购物车下单、直接购买两种形式可以不拆分成两个接口
        //TODO(MARK) - 系统默认免运费
        param.setDeliveryFee(BigDecimal.ZERO);

        log.info("开始全局事务，XID = " + RootContext.getXID());

//        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
//        Member dbMember = memberFeignClient.selectByPrimaryKey(loginMember.getId());

        Member dbMember = memberFeignClient.selectByPrimaryKey(2);


//        //校验防重令牌是否正确 -- 保证该接口的幂等性
//        String script = "if redis.call('get', KEYS[1]) == ARGV[1]  then  return redis.call('del', KEYS[1])  else  return 0 end";
//        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(USER_ORDER_TOKEN_PREFIX + loginMember.getId()), param.getOrderToken());
//        if(result == 0){
//            throw new StoneCustomerException("请勿重复提交");
//        }

        //校验：如果是从购物车下单的 那么需要校验购物车数据是否存在 以及购物车数据是否属于当前登录用户
        if(param.getShoppingCartIdList()!=null && !param.getShoppingCartIdList().isEmpty()){
            int count = shoppingCartFeignClient.countByIdListAndMemberId(param.getShoppingCartIdList(), dbMember.getId());
            if(count != param.getShoppingCartIdList().size()){
                throw new StoneCustomerException("购物车数据异常，请刷新页面重新下单");
            }
        }

        //根据收货地址id回写联系人信息
        DeliveryAddress dbDeliveryAddress = deliveryAddressService.selectByPrimaryKey(param.getDeliveryAddressId());
        if(dbDeliveryAddress == null){
            throw new StoneCustomerException("该收货地址不存在");
        }
        param.setContactRealname(dbDeliveryAddress.getRealname());
        param.setContactPhone(dbDeliveryAddress.getPhone());
        param.setContactProvince(dbDeliveryAddress.getProvince());
        param.setContactCity(dbDeliveryAddress.getCity());
        param.setContactArea(dbDeliveryAddress.getArea());
        param.setContactStreet(dbDeliveryAddress.getStreet());
        param.setContactSex(dbDeliveryAddress.getSex());
        param.setContactHouseNumber(dbDeliveryAddress.getHouseNumber());
        param.setContactLongitude(dbDeliveryAddress.getLongitude());
        param.setContactLatitude(dbDeliveryAddress.getLatitude());

        //1、校验前端的实付款金额是否正确
        this.validatePrice(param);

        //生成订单编号
        String orderNo = GenerateNo.getOrderNo();
        //生成订单取餐号
        Integer queueNo = getNextQueueNo();
        //生成支付截止时间(五分钟内未付款的订单会被自动关闭)
        Date paymentDeadline = DateUtilsPlus.addMinutes(new Date(), Quantity.INT_5);

        //2、添加订单记录
        PointsMallOrder insertOrder = new PointsMallOrder();
        insertOrder.setMemberId(dbMember.getId());
        insertOrder.setOrderNo(orderNo);
        insertOrder.setGoodsTotalQuantity(param.getGoodsTotalQuantity());
        insertOrder.setGoodsTotalPrice(param.getGoodsTotalPrice());
        insertOrder.setDeliveryFee(param.getDeliveryFee());
        insertOrder.setActualPrice(param.getActualPrice());
        insertOrder.setShoppingWay(Quantity.INT_2);
        insertOrder.setDeliveryAddressId(param.getDeliveryAddressId());
        insertOrder.setContactRealname(param.getContactRealname());
        insertOrder.setContactPhone(param.getContactPhone());
        insertOrder.setContactProvince(param.getContactProvince());
        insertOrder.setContactCity(param.getContactCity());
        insertOrder.setContactArea(param.getContactArea());
        insertOrder.setContactStreet(param.getContactStreet());
        insertOrder.setContactSex(param.getContactSex());
        insertOrder.setTradeId(null);
        insertOrder.setRemark(param.getRemark());
        insertOrder.setDescription(param.getDescription());
        insertOrder.setStatus(Quantity.INT_1);
        insertOrder.setOrderLogisticsId(null);
        insertOrder.setIsInvoice(false);
        insertOrder.setInvoiceId(null);
        insertOrder.setIsDeleted(false);
        insertOrder.setShopName(param.getShopName());
        insertOrder.setShopAddress(param.getShopAddress());
        insertOrder.setCancelReason(null);
        insertOrder.setPaymentDeadline(paymentDeadline);
        insertOrder.setCreateTime(new Date());
        insertOrder.setUpdateTime(new Date());
        insertOrder.setQueueNo(queueNo);
        insertOrder.setFullReductionRuleId(param.getFullReductionRuleId());
        insertOrder.setFullReductionRuleDescription(param.getFullReductionRuleDescription());
        insertOrder.setCouponsId(param.getCouponsId());
        insertOrder.setCouponsMemberRelationId(param.getCouponsMemberRelationId());
        insertOrder.setCouponsDescription(param.getCouponsDescription());
        insertOrder.setLimitedPrice(param.getLimitedPrice());
        insertOrder.setReducedPrice(param.getReducedPrice());
        insertOrder.setCouponsDiscountPrice(param.getCouponsDiscountPrice());
        insertOrder.setDeliveryWay(param.getDeliveryWay());
        insertOrder.setIsPayToMerchant(false);
        insertOrder.setBeforeReducedDeliveryFee(param.getBeforeReducedDeliveryFee());
        insertOrder.setContactHouseNumber(param.getContactHouseNumber());
        insertOrder.setContactLongitude(param.getContactLongitude());
        insertOrder.setContactLatitude(param.getContactLatitude());
        insertOrder.setFirstGoodsMainImage(param.getFirstGoodsMainImage());
        pointsMallOrderMapper.insert(insertOrder);

        //获取最新订单对象
        PointsMallOrder dbOrder = pointsMallOrderMapper.selectById(insertOrder.getId());

        //3、添加订单商品详情记录
        for(PointsMallOrderDetail orderDetail : param.getOrderDetailList()){
            // 添加订单商品详情记录
            PointsMallOrderDetail insertDetail = new PointsMallOrderDetail();
            insertDetail.setOrderId(dbOrder.getId());
            insertDetail.setGoodsId(orderDetail.getGoodsId());
            insertDetail.setGoodsName(orderDetail.getGoodsName());
            insertDetail.setMainImage(orderDetail.getMainImage());
            insertDetail.setSpecList(orderDetail.getSpecList());
            insertDetail.setPrice(orderDetail.getPrice());
            insertDetail.setNumber(orderDetail.getNumber());
            insertDetail.setSubtotal(orderDetail.getSubtotal());
            if(orderDetail.getIsUsedCoupons() != null){
                insertDetail.setIsUsedCoupons(orderDetail.getIsUsedCoupons());
                insertDetail.setCouponsDiscountPrice(orderDetail.getCouponsDiscountPrice());
                insertDetail.setAfterCouponsDiscountPrice(orderDetail.getPrice().subtract(orderDetail.getCouponsDiscountPrice()));
            }
            insertDetail.setIsDeleted(false);
            orderDetailService.insertSelective(insertDetail);

            //TODO - 减少商品库存 (规格的库存该怎么去变化)
            /*goodsFeignClient.decreaseStock(goodsId, number);*/
        }

        //4、如果是从购物车下单的 那么下单成功后需要删除购物车数据  注意用批量删除
        if(param.getShoppingCartIdList()!=null && param.getShoppingCartIdList().size()>0){
            BasicResult basicResult = shoppingCartFeignClient.batchDeleteByIdList(param.getShoppingCartIdList());
            if(!basicResult.getSuccess()){
                try {
                    GlobalTransactionContext.reload(RootContext.getXID()).rollback();
                    throw new StoneCustomerException("下单失败，请稍后重试");
                } catch (TransactionException e) {
                    e.printStackTrace();
                }
            }
        }

        //5、修改是否为新用户标识
        if(dbMember.getIsNewPeople()){
            dbMember.setIsNewPeople(false);
            dbMember.setIsRemindNewPeople(false);
            BasicResult basicResult = memberFeignClient.updateByPrimaryKeySelective(dbMember);
            if(!basicResult.getSuccess()){
                try {
                    GlobalTransactionContext.reload(RootContext.getXID()).rollback();
                    throw new StoneCustomerException("下单失败，请稍后重试");
                } catch (TransactionException e) {
                    e.printStackTrace();
                }
            }
        }

//        //模拟程序出错
//        int i = 10 / 0;

        //6、更新优惠卷的使用状态
        if (param.getCouponsMemberRelationId() != null) {
            couponsMemberRelationFeignClient.updateCouponsUsed(param.getCouponsMemberRelationId(),true);
        }

        //加入MQ延时队列，检测并关闭超时未支付的订单，5分钟
        Message message = new Message("TID_COMMON_MALL", "CLOSE_OVERDUE_ORDER", JSON.toJSONString(dbOrder).getBytes());
        message.setDelayTimeLevel(RocketMQConst.DELAY_TIME_LEVEL_5M);
        rocketMQTemplate.getProducer().send(message);

        return dbOrder;
    }

    @Override
    public PointsMallOrder insertByMQ(PointsMallOrderParam param, String transId) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        return null;
    }

    /**
     * 校验前端的实付款金额是否正确
     *
     * @author 暹罗
     */
    public void validatePrice(PointsMallOrderParam param){
        //订单总金额以后端计算为准，前端传递过来的总金额需要进行比对-防止前端计算错误
        //最终应付价格
        BigDecimal finalPrice;

        //1、计算商品总金额
        BigDecimal goodsTotalPrice = BigDecimal.ZERO;
        //商品总数量
        int goodsTotalQuantity = 0;
        for (PointsMallOrderDetail orderDetail : param.getOrderDetailList()) {
            PointsMallGoods dbGoods = goodsFeignClient.selectByPrimaryKey(orderDetail.getGoodsId());
            if (dbGoods == null){
                throw new StoneCustomerException("订单商品数据异常，请稍后重试");
            }
            //判断商品状态是否正确
            if(!dbGoods.getStatus().equals(PointsMallGoods.STATUS_ON_SHELF)) {
                String errorMsg = "";
                if(dbGoods.getStatus().equals(PointsMallGoods.STATUS_WAIT_ON_SHELF)){
                    errorMsg = dbGoods.getName() + "还未上架，请重新选择";
                }else if(dbGoods.getStatus().equals(PointsMallGoods.STATUS_OFF_SHELF)){
                    errorMsg = dbGoods.getName() + "已下架，请重新选择";
                }else if(dbGoods.getStatus().equals(PointsMallGoods.STATUS_SELL_OUT)){
                    errorMsg = dbGoods.getName() + "已售罄，请重新选择";
                }
                throw new StoneCustomerException(errorMsg);
            }
            //计算单品对应规格的价格
            BigDecimal specOptionPrice = BigDecimal.ZERO;
            List<String> nameList = new ArrayList<>();
            Map<String, Object> map = GsonUtils.toMap(orderDetail.getSpecList());
            for(String key : map.keySet()){
                nameList.add((String) map.get(key));
            }
            //正常情况下nameList不能为空，为空也要做特殊处理
            if(!nameList.isEmpty()){
                specOptionPrice = goodsSpecificationOptionFeignClient.selectSumPriceByGoodsIdAndName(orderDetail.getGoodsId(), nameList);
            }
            //计算单品的总价
            BigDecimal price = dbGoods.getPrice().add(specOptionPrice);
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(orderDetail.getNumber()));
            //订单总金额累加
            goodsTotalPrice = goodsTotalPrice.add(subtotal);
            goodsTotalQuantity = goodsTotalQuantity + orderDetail.getNumber();

            //填充订单主图
            if(StringUtils.isBlank(param.getFirstGoodsMainImage())){
                param.setFirstGoodsMainImage(dbGoods.getMainImage());
            }
            //填充订单商品详情信息
            orderDetail.setPrice(price);
            orderDetail.setSubtotal(subtotal);
            orderDetail.setGoodsName(dbGoods.getName());
            orderDetail.setMainImage(dbGoods.getMainImage());
        }

        //计算未使用优惠前的最终价格(商品总价)
        finalPrice = goodsTotalPrice;
        log.debug("\n\n》》》 商品总价 = " + goodsTotalPrice);
        log.debug("\n\n》》》 商品总数量 = " + goodsTotalQuantity);
        log.debug("\n\n》》》 未使用优惠前的最终价格 = " + finalPrice);

        //2、判断优惠卷是否满足使用条件
        Integer couponsMemberRelationId = param.getCouponsMemberRelationId();
        //使用优惠券时优惠的金额
        BigDecimal subtractPrice = BigDecimal.ZERO;
        if (couponsMemberRelationId != null) {
            //查询出使用的优惠券信息
            PointsMallCouponsMemberRelation dbPointsMallCouponsMemberRelation = couponsMemberRelationFeignClient.selectPointsMallCouponsMemberRelationByPrimaryKey(couponsMemberRelationId);
            Map couponsMap = couponsFeignClient.selectCouponsAndGoodsByPrimaryKey(dbPointsMallCouponsMemberRelation.getCouponsId());
            PointsMallCoupons dbPointsMallCoupons = (PointsMallCoupons) couponsMap.get("coupons");
            if(PointsMallCoupons.TYPE_DISCOUNT.equals(dbPointsMallCoupons.getPreferentialType())){
                //折扣优惠券

                //下单商品是否满足此优惠券使用条件的标识
                Boolean isMeetCouponsOfGoods = false;
                //下单商品中的最高价格
                BigDecimal subtractNum = BigDecimal.ZERO;
                //优惠券选择优惠的商品
                PointsMallOrderDetail couponsDiscountOrderDetail = null;

                //查询出优惠券关联的所有商品
                List<Integer> goodsIdList = couponsGoodsRelationFeignClient.getGoodsIdByCouponsId(dbPointsMallCouponsMemberRelation.getCouponsId());
                for (PointsMallOrderDetail orderDetail : param.getOrderDetailList()) {
                    //判断商品是否能够使用此优惠卷 且 是否应用于最高价格的商品(不包括包装费)
                    if (goodsIdList.contains(orderDetail.getGoodsId())) {
                        isMeetCouponsOfGoods = true;

                        //将当前价格 与 最高价格进行比较
                        if(orderDetail.getPrice().compareTo(subtractNum) == 1){
                            subtractNum = orderDetail.getPrice();
                            couponsDiscountOrderDetail = orderDetail;
                        }
                    }
                }
                //TODO(MARK) - 关于优惠券是否应用于最高价格的商品，这个如果前端判断错误，后端不单独给与提醒 -- 统一用订单实付款计算错误提醒即可
                if(!isMeetCouponsOfGoods){
                    throw new StoneCustomerException("所选优惠券不满足使用条件，请刷新重试");
                }

                log.debug("\n\n》》》 优惠券选择优惠的商品价格：" + subtractNum.toString());
                log.debug("\n\n》》》 优惠券折扣额度：" + dbPointsMallCoupons.getDiscountAmount().toString());

                //计算使用优惠券时优惠的金额
                subtractPrice = subtractNum.multiply((BigDecimal.ONE.subtract(((PointsMallCoupons) couponsMap.get("coupons")).getDiscountAmount()))).setScale(Quantity.INT_2, BigDecimal.ROUND_HALF_UP);
                finalPrice = finalPrice.subtract(subtractPrice);
                //标识订单商品详情
                couponsDiscountOrderDetail.setIsUsedCoupons(true);
                couponsDiscountOrderDetail.setCouponsDiscountPrice(subtractPrice);

            }else if(PointsMallCoupons.TYPE_FULL_REDUCTION.equals(((PointsMallCoupons)couponsMap.get("coupons")).getPreferentialType())){
                //TODO-满减优惠券的使用逻辑待定，初步探讨是满减规则或满减优惠券两者选其一，像奶茶这种产品下单金额比较小，所以估计用不着满减优惠券；
                //优惠券为满减卷类型
                //这里要依据使用满减规则后的最终价格来进行判断
                /*if(finalPrice.compareTo((BigDecimal) couponsMap.get("limitedPrice"))>=0){
                    subtractPrice = (BigDecimal) couponsMap.get("reducedPrice");
                    finalPrice = finalPrice.subtract(subtractPrice);
                }else{
                    basicResult.setSuccess(false);
                    basicResult.setCode(BasicResultCode.ERR);
                    basicResult.setMessage("优惠卷使用错误");
                    return basicResult;
                }*/
            }
            param.setCouponsId(dbPointsMallCouponsMemberRelation.getCouponsId());
            param.setCouponsMemberRelationId(couponsMemberRelationId);
            param.setCouponsDescription(((PointsMallCoupons)couponsMap.get("coupons")).getName() + "-优惠" + subtractPrice + "元");
            log.debug("\n\n》》》 使用优惠券时优惠的金额：" + subtractPrice.toString());
        }

        //3、判断满减规则是否满足使用条件
        //TODO-后端不判断订单应使用哪个满减规则，只对前端传递的满减规则进行是否满足使用条件判断
        if (param.getFullReductionRuleId() != null) {
            PointsMallFullReductionRule dbFullReductionRule = fullReductionRuleFeignClient.selectByPrimaryKey(param.getFullReductionRuleId());
            if(finalPrice.compareTo(dbFullReductionRule.getLimitedPrice()) >= 0){
                finalPrice = finalPrice.subtract(dbFullReductionRule.getReducedPrice());
                param.setFullReductionRuleId(dbFullReductionRule.getId());
                param.setFullReductionRuleDescription(dbFullReductionRule.getName());
                param.setLimitedPrice(dbFullReductionRule.getLimitedPrice());
                param.setReducedPrice(dbFullReductionRule.getReducedPrice());
                log.debug("\n\n》》》 使用满减规则优惠金额：" + dbFullReductionRule.getReducedPrice());
                log.debug("\n\n》》》 使用满减规则后的最终价格：" + finalPrice);
            }else{
                throw new StoneCustomerException("满减规则不满足使用条件，请刷新页面重试");
            }
        }

        //判断前端的最终价格和后端的最终价格是否一致
        //如果最终价格计算出来是负数，则要手动赋值为0
        finalPrice = (finalPrice.compareTo(BigDecimal.ZERO)==-1) ? BigDecimal.ZERO : finalPrice.setScale(Quantity.INT_2, BigDecimal.ROUND_HALF_UP);
        log.debug("\n\n》》》 前端计算的实付款：" + param.getActualPrice().toString());
        log.debug("\n\n》》》 后端计算的实付款：" + finalPrice);
        if(param.getActualPrice().compareTo(finalPrice) != 0){
            throw new StoneCustomerException("订单实付款计算错误，请刷新页面重试");
        }

        //订单描述信息
        String description = param.getOrderDetailList().get(0).getGoodsName();
        description += (goodsTotalQuantity > 1) ? ("&nbsp;&nbsp;等" + goodsTotalQuantity + "件") : "";
        //填充订单信息
        param.setDescription(description);
        param.setGoodsTotalQuantity(goodsTotalQuantity);
        param.setGoodsTotalPrice(goodsTotalPrice);
        param.setActualPrice(finalPrice);
        param.setCouponsDiscountPrice(subtractPrice);
    }

    @Override
    public List<PointsMallOrder> selectByExample(PointsMallOrderExample example) {
        return null;
    }

    @Override
    public PointsMallOrder selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public void updateByExampleSelective(PointsMallOrder record, PointsMallOrderExample example) {

    }

    @Override
    public void updateByPrimaryKeySelective(PointsMallOrder record) {

    }

    @Override
    public Page<PointsMallOrderResult> getListByPageWithAsc(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public Page<Map<String, Object>> getListByPageWithDesc(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public PointsMallOrder selectByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public void closeOverdueOrder(Long id) {

    }

    @Override
    public void updateByFinishOverdueOrder() {

    }

    @Override
    public void printRceceipts(Long id) {

    }

    @Override
    public int batchUpdateIsPrintedTrue(List<Long> idList) {
        return 0;
    }

    @Override
    public Integer getNextQueueNo() {
        return null;
    }

    @Override
    public Page<PointsMallOrderResult> getListByTodayOrderWithAsc(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public void paymentNotify(String outTradeNo) {

    }

    @Override
    public void updateByChangeToDeliveryPayNotify(String changeToDeliveryOutTradeNo) {

    }

    @Override
    public Map countOrder(PointsMallOrderParam order) {
        return null;
    }

    @Override
    public int selectLatelyMonthlySalesByShopId(Date startTime, Date endTime, Integer shopId) {
        return 0;
    }

    @Override
    public BigDecimal selectSumMerchantIncome(PointsMallOrderParam order, Date startTime, Date endTime) {
        return null;
    }

    @Override
    public BigDecimal selectSumActualPrice(PointsMallOrderParam order, Date startTime, Date endTime) {
        return null;
    }

    @Override
    public int selectCountCompleted(PointsMallOrderParam order, Date startTime, Date endTime) {
        return 0;
    }

    @Override
    public boolean getIsAllowApplyRefund(PointsMallOrderParam order) {
        return false;
    }

    @Override
    public boolean getIsAllowApplyAfterSales(PointsMallOrderParam order) {
        return false;
    }

    @Override
    public boolean getIsShowLogistics(PointsMallOrderParam order) {
        return false;
    }

    @Override
    public Page<Map<String, Object>> getAfterSalesListByPageWithAsc(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public void updatePayOrderFrozenBalanceOfMerchant() {

    }

    @Override
    public void remindOvertimeOrder() {

    }

    @Override
    public List<Map<String, Object>> selectStatisticOrder(Integer shopId) {
        return null;
    }

    @Override
    public String selectStartDatePointsMallOrder(Integer shopId) {
        return null;
    }

    @Override
    public int selectCountPayers(PointsMallOrderParam order, Date startTime, Date endTime) {
        return 0;
    }

    @Override
    public int selectCountOrderPeoples(PointsMallOrderParam order, Date startTime, Date endTime) {
        return 0;
    }

    @Override
    public void updateRefundStatus(String out_trade_no) {

    }

    @Override
    public void syncOrderLogisticsInfo() {

    }

    @Override
    public int selectCountPaid(PointsMallOrderParam order, Date startTime, Date endTime) {
        return 0;
    }

    @Override
    public void batchUpdateIsPrintedTrue(PointsMallOrderParam param) {

    }

    @Override
    public Map selectAllTabWaitHandleNum(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public List<PointsMallOrder> waitPrintPointsMallOrderList(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public void auditAfterSalesOrder(PointsMallOrderParam param) {

    }

    @Override
    public Map statistic(PointsMallOrderParam param) throws ParseException {
        return null;
    }

    @Override
    public void deliver(PointsMallOrderParam param) {

    }

    @Override
    public void updateLogisticsNo(PointsMallOrderParam param) {

    }

    @Override
    public void updateByAdmin(PointsMallOrderParam param) {

    }

    @Override
    public PointsMallOrderVo selectById(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public void cancelOrder(PointsMallOrderParam param) {

    }

    @Override
    public void applyRefundUndelivered(PointsMallOrderParam param) {

    }

    @Override
    public void onlyRefund(PointsMallOrderParam param) {

    }

    @Override
    public void returnGoodsWithRefund(PointsMallOrderParam param) {

    }

    @Override
    public void confirmReceipt(PointsMallOrderParam param) {

    }

    @Override
    public void delete(PointsMallOrderParam param) {

    }

    @Override
    public Map selectRefundProcess(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public Map selectTabNumber(PointsMallOrderParam param) {
        return null;
    }

    @Override
    public String selectCommissionReward(PointsMallOrderParam param) {
        return null;
    }
}