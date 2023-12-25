package com.siam.package_order.service_impl.internal.distributed_transaction;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.util.RedisUtils;
import com.siam.package_goods.feign.ShoppingCartFeignApi;
import com.siam.package_order.entity.Order;
import com.siam.package_order.mapper.OrderMapper;
import com.siam.package_order.model.example.OrderExample;
import com.siam.package_order.model.param.OrderParam;
import com.siam.package_order.model.vo.OrderVo;
import com.siam.package_order.model.vo.OrderVo2;
import com.siam.package_order.service.OrderDetailService;
import com.siam.package_order.service.OrderService;
import com.siam.package_promotion.feign.CouponsMemberRelationFeignApi;
import com.siam.package_user.auth.cache.MemberSessionManager;
import com.siam.package_user.feign.DeliveryAddressFeignApi;
import com.siam.package_user.feign.MemberFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 分布式事务-本地消息表
 *
 * @return
 * @author 暹罗
 */
@Slf4j
@Service
public class LocalMsg_OrderServiceImpl implements OrderService {

    public static final String USER_ORDER_TOKEN_PREFIX = "order:token:";

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private MemberFeignApi memberFeignApi;

    @Autowired
    private DeliveryAddressFeignApi deliveryAddressFeignApi;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Autowired
    private ShoppingCartFeignApi shoppingCartFeignApi;

    @Autowired
    private CouponsMemberRelationFeignApi couponsMemberRelationFeignApi;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int countByExample(OrderExample example) {
        return 0;
    }

    @Override
    public void delete(OrderParam param) {

    }

    @Override
    public Order insert(OrderParam param) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
//        //TODO(MARK) - 购物车下单、直接购买两种形式可以不拆分成两个接口
//        //TODO(MARK) - 系统默认免运费
//        param.setDeliveryFee(BigDecimal.ZERO);
//        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
//        Member dbMember = memberFeignApi.selectByPrimaryKey(loginMember.getId()).getData();
//
////        //校验防重令牌是否正确 -- 保证该接口的幂等性
////        String script = "if redis.call('get', KEYS[1]) == ARGV[1]  then  return redis.call('del', KEYS[1])  else  return 0 end";
////        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(USER_ORDER_TOKEN_PREFIX + loginMember.getId()), param.getOrderToken());
////        if(result == 0){
////            throw new StoneCustomerException("请勿重复提交");
////        }
//
//        //校验：如果是从购物车下单的 那么需要校验购物车数据是否存在 以及购物车数据是否属于当前登录用户
//        if(param.getShoppingCartIdList()!=null && !param.getShoppingCartIdList().isEmpty()){
//            int count = shoppingCartFeignApi.countByIdListAndMemberId(param.getShoppingCartIdList(), loginMember.getId()).getData();
//            if(count != param.getShoppingCartIdList().size()){
//                throw new StoneCustomerException("购物车数据异常，请刷新页面重新下单");
//            }
//        }
//
//        //根据收货地址id回写联系人信息
//        DeliveryAddress dbDeliveryAddress = deliveryAddressFeignApi.selectByPrimaryKey(param.getDeliveryAddressId());
//        if(dbDeliveryAddress == null){
//            throw new StoneCustomerException("该收货地址不存在");
//        }
//        param.setContactRealname(dbDeliveryAddress.getRealname());
//        param.setContactPhone(dbDeliveryAddress.getPhone());
//        param.setContactProvince(dbDeliveryAddress.getProvince());
//        param.setContactCity(dbDeliveryAddress.getCity());
//        param.setContactArea(dbDeliveryAddress.getArea());
//        param.setContactStreet(dbDeliveryAddress.getStreet());
//        param.setContactSex(dbDeliveryAddress.getSex());
//        param.setContactHouseNumber(dbDeliveryAddress.getHouseNumber());
//        param.setContactLongitude(dbDeliveryAddress.getLongitude());
//        param.setContactLatitude(dbDeliveryAddress.getLatitude());
//
////        //1、校验前端的实付款金额是否正确
////        this.validatePrice(param);
//
//        //生成订单编号
//        String orderNo = GenerateNo.getOrderNo();
//        //生成订单取餐号
//        Integer queueNo = getNextQueueNo();
//        //生成支付截止时间(五分钟内未付款的订单会被自动关闭)
//        Date paymentDeadline = DateUtilsPlus.addMinutes(new Date(), Quantity.INT_5);
//
//        //2、添加订单记录
//        Order insertOrder = new Order();
//        insertOrder.setMemberId(loginMember.getId());
//        insertOrder.setOrderNo(orderNo);
//        insertOrder.setGoodsTotalQuantity(param.getGoodsTotalQuantity());
//        insertOrder.setGoodsTotalPrice(param.getGoodsTotalPrice());
//        insertOrder.setDeliveryFee(param.getDeliveryFee());
//        insertOrder.setActualPrice(param.getActualPrice());
//        insertOrder.setShoppingWay(Quantity.INT_2);
//        insertOrder.setDeliveryAddressId(param.getDeliveryAddressId());
//        insertOrder.setContactRealname(param.getContactRealname());
//        insertOrder.setContactPhone(param.getContactPhone());
//        insertOrder.setContactProvince(param.getContactProvince());
//        insertOrder.setContactCity(param.getContactCity());
//        insertOrder.setContactArea(param.getContactArea());
//        insertOrder.setContactStreet(param.getContactStreet());
//        insertOrder.setContactSex(param.getContactSex());
//        insertOrder.setTradeId(null);
//        insertOrder.setRemark(param.getRemark());
//        insertOrder.setDescription(param.getDescription());
//        insertOrder.setStatus(Quantity.INT_1);
//        insertOrder.setOrderLogisticsId(null);
//        insertOrder.setIsInvoice(false);
//        insertOrder.setInvoiceId(null);
//        insertOrder.setIsDeleted(false);
//        insertOrder.setShopName(param.getShopName());
//        insertOrder.setShopAddress(param.getShopAddress());
//        insertOrder.setCancelReason(null);
//        insertOrder.setPaymentDeadline(paymentDeadline);
//        insertOrder.setCreateTime(new Date());
//        insertOrder.setUpdateTime(new Date());
//        insertOrder.setQueueNo(queueNo);
//        insertOrder.setFullReductionRuleId(param.getFullReductionRuleId());
//        insertOrder.setFullReductionRuleDescription(param.getFullReductionRuleDescription());
//        insertOrder.setCouponsId(param.getCouponsId());
//        insertOrder.setCouponsMemberRelationId(param.getCouponsMemberRelationId());
//        insertOrder.setCouponsDescription(param.getCouponsDescription());
//        insertOrder.setLimitedPrice(param.getLimitedPrice());
//        insertOrder.setReducedPrice(param.getReducedPrice());
//        insertOrder.setCouponsDiscountPrice(param.getCouponsDiscountPrice());
//        insertOrder.setDeliveryWay(param.getDeliveryWay());
//        insertOrder.setIsPayToMerchant(false);
//        insertOrder.setBeforeReducedDeliveryFee(param.getBeforeReducedDeliveryFee());
//        insertOrder.setContactHouseNumber(param.getContactHouseNumber());
//        insertOrder.setContactLongitude(param.getContactLongitude());
//        insertOrder.setContactLatitude(param.getContactLatitude());
//        insertOrder.setFirstGoodsMainImage(param.getFirstGoodsMainImage());
//        orderMapper.insert(insertOrder);
//
//        //获取最新订单对象
//        Order dbOrder = orderMapper.selectById(insertOrder.getId());
//
//        //3、添加订单商品详情记录
//        for(OrderDetail orderDetail : param.getOrderDetailList()){
//            // 添加订单商品详情记录
//            OrderDetail insertDetail = new OrderDetail();
//            insertDetail.setOrderId(dbOrder.getId());
//            insertDetail.setGoodsId(orderDetail.getGoodsId());
//            insertDetail.setGoodsName(orderDetail.getGoodsName());
//            insertDetail.setMainImage(orderDetail.getMainImage());
//            insertDetail.setSpecList(orderDetail.getSpecList());
//            insertDetail.setPrice(orderDetail.getPrice());
//            insertDetail.setNumber(orderDetail.getNumber());
//            insertDetail.setSubtotal(orderDetail.getSubtotal());
//            if(orderDetail.getIsUsedCoupons() != null){
//                insertDetail.setIsUsedCoupons(orderDetail.getIsUsedCoupons());
//                insertDetail.setCouponsDiscountPrice(orderDetail.getCouponsDiscountPrice());
//                insertDetail.setAfterCouponsDiscountPrice(orderDetail.getPrice().subtract(orderDetail.getCouponsDiscountPrice()));
//            }
//            insertDetail.setIsDeleted(false);
//            orderDetailService.insertSelective(insertDetail);
//
//            //TODO - 减少商品库存 (规格的库存该怎么去变化)
//            /*goodsFeignApi.decreaseStock(goodsId, number);*/
//        }
//
//        //保存本地消息表
//        LocalMsg localMsg = new LocalMsg();
//        localMsg.setId(dbOrder.getId().toString());
//        localMsg.setContent(JSON.toJSONString(dbOrder));
//        localMsg.setStatus(0);
//        localMsg.setSendTime(new Date());
//        List<LocalMsg> localMsgList = new ArrayList<>();
//        if(redisUtils.hasKey("localMsgList")){
//            localMsgList = (List<LocalMsg>) JsonUtils.toObject(redisUtils.get("localMsgList").toString(), List.class);
//        }
//        localMsgList.add(localMsg);
//        redisUtils.set("localMsgList", localMsgList);
//
//        //4、如果是从购物车下单的 那么下单成功后需要删除购物车数据  注意用批量删除
//        if(param.getShoppingCartIdList()!=null && param.getShoppingCartIdList().size()>0){
//            shoppingCartFeignApi.batchDeleteByIdList(param.getShoppingCartIdList());
//        }
//
//        //5、修改是否为新用户标识
//        if(dbMember.getIsNewPeople()){
//            dbMember.setIsNewPeople(false);
//            dbMember.setIsRemindNewPeople(false);
//            memberFeignApi.updateByPrimaryKeySelective(dbMember);
//        }
//
//        //模拟程序出错
////        int i = 10 / 0;
//
//        //6、更新优惠卷的使用状态
//        if (param.getCouponsMemberRelationId() != null) {
//            couponsMemberRelationFeignApi.updateCouponsUsed(param.getCouponsMemberRelationId(),true);
//        }
//
//        //加入MQ延时队列，检测并关闭超时未支付的订单，5分钟
//        Message message = new Message("TID_COMMON_MALL", "CLOSE_OVERDUE_ORDER", JSON.toJSONString(dbOrder).getBytes());
//        message.setDelayTimeLevel(RocketMQConst.DELAY_TIME_LEVEL_5M);
//        rocketMQTemplate.getProducer().send(message);
//
//        return dbOrder;

        return null;
    }

    @Override
    public Order insertByMQ(OrderParam param, String transId) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        return null;
    }

    @Override
    public void cancelOrder(OrderParam param) {

    }

    @Override
    public void cancelOrderNoReason(OrderParam param) throws IOException {

    }

    @Override
    public void applyRefund(OrderParam param) throws IOException {

    }

    @Override
    public void confirmReceipt(OrderParam param) {

    }

    @Override
    public List<Order> selectByExample(OrderExample example) {
        return null;
    }

    @Override
    public Order selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public void updateByExampleSelective(Order record, OrderExample example) {

    }

    @Override
    public void updateByPrimaryKeySelective(Order record) {

    }

    @Override
    public Page<Order> getListByPageWithAsc(OrderParam param) {
        return null;
    }

    @Override
    public Page<Map<String, Object>> getListByPageWithDesc(OrderParam param) {
        return null;
    }

    @Override
    public Order selectByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public void closeOverdueOrder(Integer id) {

    }

    @Override
    public void autoCompletedOrder(Integer id) {

    }

    @Override
    public void printRceceipts(Integer id) {

    }

    @Override
    public int batchUpdateIsPrintedTrue(List<Integer> idList) {
        return 0;
    }

    @Override
    public Integer getNextQueueNo() {
        return null;
    }

    @Override
    public Page<Order> getListByTodayOrderWithAsc(OrderParam param) {
        return null;
    }

    @Override
    public void paymentNotify(String outTradeNo) throws IOException, InterruptedException, RemotingException, MQClientException, MQBrokerException {

    }

    @Override
    public void paymentNotifyOfChangeToDelivery(String changeToDeliveryOutTradeNo) {

    }

    @Override
    public Map countOrder(OrderParam order) {
        return null;
    }

    @Override
    public int selectLatelyMonthlySalesByShopId(Date startTime, Date endTime, Integer shopId) {
        return 0;
    }

    @Override
    public BigDecimal selectSumMerchantIncome(OrderParam param) {
        return null;
    }

    @Override
    public BigDecimal selectSumActualPrice(OrderParam param) {
        return null;
    }

    @Override
    public int selectCountCompleted(OrderParam param) {
        return 0;
    }

    @Override
    public int selectCountPaid(OrderParam param) {
        return 0;
    }

    @Override
    public boolean getIsAllowCancelNoReason(OrderParam order) {
        return false;
    }

    @Override
    public boolean getIsAllowApplyRefund(OrderParam order) {
        return false;
    }

    @Override
    public Page<Order> getAfterSalesListByPageWithAsc(OrderParam param) {
        return null;
    }

    @Override
    public void updatePayOrderFrozenBalanceOfMerchant() {

    }

    @Override
    public void remindOvertimeOrder(Integer id) throws IOException {

    }

    @Override
    public List<Map<String, Object>> selectStatisticOrder(Integer shopId) {
        return null;
    }

    @Override
    public String selectStartDateOrder(Integer shopId) {
        return null;
    }

    @Override
    public int selectCountPayers(OrderParam param) {
        return 0;
    }

    @Override
    public int selectCountOrderPeoples(OrderParam param) {
        return 0;
    }

    @Override
    public void updateRefundStatus(String out_trade_no) {

    }

    @Override
    public Map<String, Object> selectSumField(OrderParam order) {
        return null;
    }

    @Override
    public int selectCountUnCompleted(OrderParam order) {
        return 0;
    }

    @Override
    public int selectCountWaitHandle(OrderParam order) {
        return 0;
    }

    @Override
    public OrderVo selectById(OrderParam param) {
        return null;
    }

    @Override
    public OrderVo2 selectRefundProcess(OrderParam param) {
        return null;
    }

    @Override
    public Map selectTabNumber(OrderParam param) {
        return null;
    }

    @Override
    public void batchUpdateIsPrintedTrue(OrderParam param) {

    }

    @Override
    public Map selectAllTabWaitHandleNum(OrderParam param) {
        return null;
    }

    @Override
    public List<Order> waitPrintOrderList(OrderParam param) {
        return null;
    }

    @Override
    public void auditAfterSalesOrder(OrderParam param) {

    }

    @Override
    public Map statistic(OrderParam param) throws ParseException {
        return null;
    }

    @Override
    public boolean save(Order entity) {
        return false;
    }

    @Override
    public boolean saveBatch(Collection<Order> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Order> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean removeById(Serializable id) {
        return false;
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        return false;
    }

    @Override
    public boolean remove(Wrapper<Order> queryWrapper) {
        return false;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return false;
    }

    @Override
    public boolean updateById(Order entity) {
        return false;
    }

    @Override
    public boolean update(Order entity, Wrapper<Order> updateWrapper) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Order> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Order entity) {
        return false;
    }

    @Override
    public Order getById(Serializable id) {
        return null;
    }

    @Override
    public List<Order> listByIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public List<Order> listByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public Order getOne(Wrapper<Order> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Order> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Order> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public int count(Wrapper<Order> queryWrapper) {
        return 0;
    }

    @Override
    public List<Order> list(Wrapper<Order> queryWrapper) {
        return null;
    }

    @Override
    public <E extends IPage<Order>> E page(E page, Wrapper<Order> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<Order> queryWrapper) {
        return null;
    }

    @Override
    public <V> List<V> listObjs(Wrapper<Order> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<Order> queryWrapper) {
        return null;
    }

    @Override
    public BaseMapper<Order> getBaseMapper() {
        return null;
    }
}