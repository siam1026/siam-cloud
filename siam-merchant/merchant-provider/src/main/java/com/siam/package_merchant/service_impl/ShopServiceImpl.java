package com.siam.package_merchant.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_util.feign.SettingFeignApi;
import com.siam.package_util.entity.Setting;
import com.siam.package_merchant.entity.Shop;
import com.siam.package_merchant.model.example.ShopExample;
import com.siam.package_merchant.mapper.ShopMapper;
import com.siam.package_merchant.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private SettingFeignApi settingFeignApi;

    @Override
    public int insert(Shop record) {
        return shopMapper.insert(record);
    }

    @Override
    public int insertSelective(Shop record) {
        return shopMapper.insertSelective(record);
    }

    @Override
    public int deleteByExample(ShopExample example) {
        return shopMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return shopMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Shop record) {
        return shopMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Shop> selectByExample(ShopExample example) {
        return shopMapper.selectByExample(example);
    }

    @Override
    public Shop selectByPrimaryKey(Integer id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public int countByExample(ShopExample example) {
        return shopMapper.countByExample(example);
    }

    @Override
    public Page<Shop> getListByPage(int pageNo, int pageSize, Shop shop) {
        Page<Shop> page = shopMapper.getListByPage(new Page(pageNo, pageSize), shop);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getMapListByPageJoinMerchant(int pageNo, int pageSize, Shop shop) {
        Page<Map<String, Object>> page = shopMapper.getMapListByPageJoinMerchant(new Page(pageNo, pageSize), shop);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, Shop shop) {
        Page<Map<String, Object>> page = shopMapper.getMapListByPage(new Page(pageNo, pageSize), shop);
        return page;
    }

    @Override
    public Shop selectByName(String name) {
        return shopMapper.selectByName(name);
    }

    @Override
    public Shop selectByMerchantId(Integer merchantId) {
        return shopMapper.selectByMerchantId(merchantId);
    }

    /**
     * 返回值 为0 代表 "您的收货地址超出配送范围"
     * 返回值 为-1 代表 "您的配送距离超出XX公里，不予配送"
     * 返回值 大于0 代表 计算正常
     * @author 暹罗
     */
    @Override
    public BigDecimal selectDeliveryFee(Shop shop, BigDecimal deliveryDistance) {

        Setting setting = settingFeignApi.selectCurrent().getData();

        //如果距离为0，则代表百度地图没有计算结果
        //还有一种情况会造成距离为0，那就是起点和终点相等--这种情况也算作地址填写错误
        if(deliveryDistance.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }
        //超出5.5公里则不予配送
        if(deliveryDistance.compareTo(setting.getDeliveryDistanceLimit()) > 0){
            return BigDecimal.valueOf(-1);
        }

        //配送费计算规则：起送价1.5元(0~1KM)，每增加1KM加1元
        //得按照行车距离-骑行来计算，不能按照直线距离
        BigDecimal basicPrice = setting.getDeliveryStartingPrice();
        BigDecimal increasedPrice = setting.getDeliveryKilometerPrice();

        BigDecimal deliveryFee = BigDecimal.ZERO;
        if(deliveryDistance.compareTo(BigDecimal.ZERO)>=0 && deliveryDistance.compareTo(BigDecimal.ONE)<=0){
            deliveryFee = basicPrice;
        }else{
            //增加的价格要按照天花板取整
            BigDecimal sumIncreasedPrice = deliveryDistance.subtract(BigDecimal.ONE).multiply(increasedPrice).setScale(0, BigDecimal.ROUND_CEILING);
            deliveryFee = basicPrice.add(sumIncreasedPrice);
        }
        return deliveryFee;
    }

    @Override
    public Page<Shop> selectByDistance(int pageNo, int pageSize, BigDecimal positionLongitude, BigDecimal positionLatitude, BigDecimal deliveryDistanceLimit) {
        Shop shop = new Shop();
        shop.setPositionLongitude(positionLongitude);
        shop.setPositionLatitude(positionLatitude);
        shop.setDeliveryDistanceLimit(deliveryDistanceLimit);
        Page<Shop> page = shopMapper.selectByDistance(new Page(pageNo, pageSize), shop);
        return page;
    }

    @Override
    public Page<Map<String, Object>> selectMapByDistance(int pageNo, int pageSize, BigDecimal positionLongitude, BigDecimal positionLatitude, BigDecimal deliveryDistanceLimit) {
        Shop shop = new Shop();
        shop.setPositionLongitude(positionLongitude);
        shop.setPositionLatitude(positionLatitude);
        shop.setDeliveryDistanceLimit(deliveryDistanceLimit);
        Page<Map<String, Object>> page = shopMapper.selectMapByDistance(new Page(pageNo, pageSize), shop);
        return page;
    }
}