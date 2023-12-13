package com.siam.package_order.service_impl.internal.distributed_transaction;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.constant.RocketMQConst;
import com.siam.package_common.entity.LocalMsg;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_common.util.GenerateNo;
import com.siam.package_common.util.JsonUtils;
import com.siam.package_common.util.RedisUtils;
import com.siam.package_goods.feign.internal.PointsMallShoppingCartFeignApi;
import com.siam.package_order.entity.DeliveryAddress;
import com.siam.package_order.entity.internal.PointsMallOrder;
import com.siam.package_order.entity.internal.PointsMallOrderDetail;
import com.siam.package_order.mapper.internal.PointsMallOrderMapper;
import com.siam.package_order.model.example.internal.PointsMallOrderExample;
import com.siam.package_order.model.param.internal.PointsMallOrderParam;
import com.siam.package_order.model.result.internal.PointsMallOrderResult;
import com.siam.package_order.model.vo.internal.PointsMallOrderVo;
import com.siam.package_order.service.DeliveryAddressService;
import com.siam.package_order.service.internal.PointsMallOrderDetailService;
import com.siam.package_order.service.internal.PointsMallOrderService;
import com.siam.package_promotion.feign.internal.PointsMallCouponsMemberRelationFeignApi;
import com.siam.package_user.auth.cache.MemberSessionManager;
import com.siam.package_user.entity.Member;
import com.siam.package_user.feign.MemberFeignApi;
import com.siam.package_user.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
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
 * 分布式事务-本地消息表
 *
 * @return
 * @author 暹罗
 */
@Slf4j
@Service
public class LocalMsg_PointsMallOrderServiceImpl implements PointsMallOrderService {

    public static final String USER_ORDER_TOKEN_PREFIX = "order:token:";

    @Autowired
    private PointsMallOrderMapper pointsMallOrderMapper;

    @Autowired
    private PointsMallOrderDetailService orderDetailService;

    @Autowired
    private MemberFeignApi memberFeignApi;

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Autowired
    private PointsMallShoppingCartFeignApi shoppingCartFeignApi;

    @Autowired
    private PointsMallCouponsMemberRelationFeignApi couponsMemberRelationFeignApi;

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
    public PointsMallOrder insert(PointsMallOrderParam param) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        //TODO(MARK) - 购物车下单、直接购买两种形式可以不拆分成两个接口
        //TODO(MARK) - 系统默认免运费
        param.setDeliveryFee(BigDecimal.ZERO);
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        Member dbMember = memberFeignApi.selectByPrimaryKey(loginMember.getId()).getData();

//        //校验防重令牌是否正确 -- 保证该接口的幂等性
//        String script = "if redis.call('get', KEYS[1]) == ARGV[1]  then  return redis.call('del', KEYS[1])  else  return 0 end";
//        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(USER_ORDER_TOKEN_PREFIX + loginMember.getId()), param.getOrderToken());
//        if(result == 0){
//            throw new StoneCustomerException("请勿重复提交");
//        }

        //校验：如果是从购物车下单的 那么需要校验购物车数据是否存在 以及购物车数据是否属于当前登录用户
        if(param.getShoppingCartIdList()!=null && !param.getShoppingCartIdList().isEmpty()){
            int count = shoppingCartFeignApi.countByIdListAndMemberId(param.getShoppingCartIdList(), loginMember.getId()).getData();
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

//        //1、校验前端的实付款金额是否正确
//        this.validatePrice(param);

        //生成订单编号
        String orderNo = GenerateNo.getOrderNo();
        //生成订单取餐号
        Integer queueNo = getNextQueueNo();
        //生成支付截止时间(五分钟内未付款的订单会被自动关闭)
        Date paymentDeadline = DateUtilsPlus.addMinutes(new Date(), Quantity.INT_5);

        //2、添加订单记录
        PointsMallOrder insertOrder = new PointsMallOrder();
        insertOrder.setMemberId(loginMember.getId());
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
            /*goodsFeignApi.decreaseStock(goodsId, number);*/
        }

        //保存本地消息表
        LocalMsg localMsg = new LocalMsg();
        localMsg.setId(dbOrder.getId().toString());
        localMsg.setContent(JSON.toJSONString(dbOrder));
        localMsg.setStatus(0);
        localMsg.setSendTime(new Date());
        List<LocalMsg> localMsgList = new ArrayList<>();
        if(redisUtils.hasKey("localMsgList")){
            localMsgList = (List<LocalMsg>) JsonUtils.toObject(redisUtils.get("localMsgList").toString(), List.class);
        }
        localMsgList.add(localMsg);
        redisUtils.set("localMsgList", localMsgList);

        //4、如果是从购物车下单的 那么下单成功后需要删除购物车数据  注意用批量删除
        if(param.getShoppingCartIdList()!=null && param.getShoppingCartIdList().size()>0){
            shoppingCartFeignApi.batchDeleteByIdList(param.getShoppingCartIdList());
        }

        //5、修改是否为新用户标识
        if(dbMember.getIsNewPeople()){
            dbMember.setIsNewPeople(false);
            dbMember.setIsRemindNewPeople(false);
            memberFeignApi.updateByPrimaryKeySelective(dbMember);
        }

        //模拟程序出错
//        int i = 10 / 0;

        //6、更新优惠卷的使用状态
        if (param.getCouponsMemberRelationId() != null) {
            couponsMemberRelationFeignApi.updateCouponsUsed(param.getCouponsMemberRelationId(),true);
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