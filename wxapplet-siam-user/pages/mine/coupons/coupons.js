import https from '../../../utils/http';
import utilHelper from '../../../utils/util';
import toastService from '../../../utils/toast.service';
import dateHelper from '../../../utils/date-helper';
import systemStatus from '../../../utils/system-status';
const app = getApp();
var pageNo=-1,pageSize=20;
Page({

  /**
   * 页面的初始数据 
   */
  data: {
    couponsList:[],
    currentTab: 0,
    tabList: [{
      modeId: 0,
      modeName: "外卖券"
    }, {
      modeId: 1,
      modeName: "商城券"
    }],
    prevData:""
  },
  getHeight() {
    //获取用户手机系统信息
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        let winHeight = res.windowHeight; //获取高度
        //获取class为settlement-view并减去这个高度，自适应scrollview的高度
        if(that.data.prevData){
          that.setData({
            winHeight: winHeight
          });
        }else{
          const query = wx.createSelectorQuery();
          query.select('.swiper-content').boundingClientRect();
          query.selectViewport().scrollOffset();
          query.exec(function (res) {
            console.log(winHeight - res[0].height)
            that.setData({
              winHeight: winHeight - res[0].height
            });
          })
        }
        
      }
    });
  },
  // 滑动切换tab
  bindSlideChange: function (e) {
    console.log(e)
    // if (e.detail.current == 1) {
    //   if (this.data.mallList.length <= 0) {
    //     this.getMallOrderList();
    //   }
    // }
    this.setData({
      currentTab: e.detail.current
    });
  },
  //点击切换 
  clickTab: function (e) {
    //console.log(e.target.dataset.current)
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      // 显示加载图标
      this.setData({
        currentTab: e.target.dataset.current
      });
    }
  },
  getCouponsMemberRelation() {
    toastService.showLoading();
    https.request('/api-promotion/rest/member/couponsMemberRelation/list', {
      pageNo:pageNo,
      pageSize:pageSize,
      isUsed: 0,
      isExpired: 0,
      isValid: 0,
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.records.forEach(res => {
          res.couponsMemberRelationMap.preferentialTypeText = systemStatus.preferentialTypeText(res.couponsMemberRelationMap.preferentialType);
          res.couponsMemberRelationMap.startTime = dateHelper.formatISODate(res.couponsMemberRelationMap.startTime, "YYYY-MM-DD");
          //res.couponsMemberRelationMap.isRelation = true;
          res.couponsMemberRelationMap.endTime = dateHelper.formatISODate(res.couponsMemberRelationMap.endTime, "YYYY-MM-DD");
          res.couponsMemberRelationMap.isShow = false; //设置每个优惠券的使用规则为隐藏
          //console.log(this.data)
          console.log(this.data.prevData)
          if (this.data.prevData) {
            if (res.couponsMemberRelationMap.id == this.data.prevData.id) {
              res.couponsMemberRelationMap.checked = true;
            }
            //判断优惠券是否过期和是否已经使用
            //if (!res.couponsMemberRelationMap.isExpired && !res.couponsMemberRelationMap.isUsed) {
            //console.log(this.data)
            //判断是否已经满减，如果已经满减就取满减之后的字段值，反之取总额
            //if (this.data.data.actualPrice && !this.data.data.fullPriceReduction) {
            //遍历当前订单的商品
            let afterDiscountPrice = 0;
            res.couponsMemberRelationMap.afterDiscountPrice = 0;
            //判断当前优惠券是折扣还是满减券,1等于折扣,2等于满减
            if (res.couponsMemberRelationMap.preferentialType == 1) {
              res.couponsMemberRelationMap.isRelation = false;
              if(res.shopList.length<=0){
                return
              }
              res.shopList.forEach((shop, shopIndex) => {
                if (shop.id == this.data.prevData.shopId) {
                  this.data.prevData.orderDetailList.forEach(order => {
                    //source  优惠券发放来源 1=商家中心 2=调度中心
                    //如果是商家中心发放的优惠券，则需要判断关联商品
                    //调度中心发放的优惠券，则无需判断关联商品，所有商品皆可使用
                    if (res.couponsMemberRelationMap.source == 1) {
                      //遍历当前优惠券绑定的优惠商品
                      res.goodsList.forEach(goods => {
                        //console.log(goods)
                        if (order.goodsId == goods.id) {
                          //console.log(res.couponsMemberRelationMap)
                          res.couponsMemberRelationMap.isRelation = true;
                        }
                      });
                    }
                    let orderPrice=order.price;
                    let orderNumber=order.number;
                    let unitPrice=utilHelper.toFixed(orderPrice-(orderPrice* res.couponsMemberRelationMap.discountAmount),2);
                    //判断当前订单的商品是否等于优惠券绑定的优惠商品
                    //等于则进行优惠
                    //判断优惠券优惠的最大金额
                    if (orderPrice >= afterDiscountPrice) {
                      //console.log(afterDiscountPrice)
                      afterDiscountPrice = orderPrice;
                      //获取当前的商品折扣后的优惠价格
                      res.couponsMemberRelationMap.afterDiscountPrice = res.couponsMemberRelationMap.discountAmount
                        > 0 ? utilHelper.toFixed(unitPrice, 2) : (unitPrice);
                    }
                    res.couponsMemberRelationMap.isRelation = true;
                  })
                  return
                }
                
              })
            }
            //判断如果是优惠券满减的话这就进行优惠券的总价满减
            //console.log(res)
            if (res.couponsMemberRelationMap.preferentialType == 2) {
              if (this.data.prevData.fullPriceReductionAfter) {
                if (this.data.prevData.fullPriceReductionAfter >= res.couponsMemberRelationMap.limitedPrice) {
                  //console.log(res.couponsMemberRelationMap.limitedPrice)
                  res.couponsMemberRelationMap.afterDiscountPrice = this.data.prevData.fullPriceReductionAfter - res.couponsMemberRelationMap.reducedPrice;
                }
                return
              }
              if (this.data.prevData.actualPrice >= res.couponsMemberRelationMap.limitedPrice) {
                res.couponsMemberRelationMap.afterDiscountPrice = this.data.prevData.actualPrice - res.couponsMemberRelationMap.reducedPrice;
              }
            }
            return
          }
          console.log("从我的进入...")
          res.couponsMemberRelationMap.isRelation = true;
          //}
        })
        this.setData({
          couponsList: result.data.records
        })
      }
    })
  },
  getCouponsMemberPointsMallRelation() {
    toastService.showLoading();
    https.request("/api-mall/rest/member/pointsMall/couponsMemberRelation/list", {
      pageNo:pageNo,
      pageSize:pageSize,
      isUsed: 0,
      isExpired: 0,
      isValid: 0,
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.records.forEach(res => {
          res.couponsMemberRelationMap.preferentialTypeText = systemStatus.preferentialTypeText(res.couponsMemberRelationMap.preferentialType);
          res.couponsMemberRelationMap.startTime = dateHelper.formatISODate(res.couponsMemberRelationMap.startTime, "YYYY-MM-DD");
          //res.couponsMemberRelationMap.isRelation = true;
          res.couponsMemberRelationMap.endTime = dateHelper.formatISODate(res.couponsMemberRelationMap.endTime, "YYYY-MM-DD");
          res.couponsMemberRelationMap.isShow = false; //设置每个优惠券的使用规则为隐藏
          //console.log(this.data)
          console.log(this.data.prevData)
          if (this.data.prevData) {
            if (res.couponsMemberRelationMap.id == this.data.prevData.id) {
              res.couponsMemberRelationMap.checked = true;
            }
            //判断优惠券是否过期和是否已经使用
            //if (!res.couponsMemberRelationMap.isExpired && !res.couponsMemberRelationMap.isUsed) {
            //console.log(this.data)
            //判断是否已经满减，如果已经满减就取满减之后的字段值，反之取总额
            //if (this.data.data.actualPrice && !this.data.data.fullPriceReduction) {
            //遍历当前订单的商品
            let afterDiscountPrice = 0;
            res.couponsMemberRelationMap.afterDiscountPrice = 0;
            //判断当前优惠券是折扣还是满减券,1等于折扣,2等于满减
            if (res.couponsMemberRelationMap.preferentialType == 1) {
              res.couponsMemberRelationMap.isRelation = false;
              if(this.data.prevData){
                this.data.prevData.orderDetailList.forEach(order => {
                  //source  优惠券发放来源 1=商家中心 2=调度中心
                  //如果是商家中心发放的优惠券，则需要判断关联商品
                  //调度中心发放的优惠券，则无需判断关联商品，所有商品皆可使用
                  if (res.couponsMemberRelationMap.source == 1) {
                    //遍历当前优惠券绑定的优惠商品
                    res.goodsList.forEach(goods => {
                      //console.log(goods)
                      if (order.goodsId == goods.id) {
                        //console.log(res.couponsMemberRelationMap)
                        res.couponsMemberRelationMap.isRelation = true;
                      }
                    });
                  }
                  let orderPrice=order.price;
                  let orderNumber=order.number;
                  let unitPrice=utilHelper.toFixed(orderPrice-(orderPrice* res.couponsMemberRelationMap.discountAmount),2);
                  //判断当前订单的商品是否等于优惠券绑定的优惠商品
                  //等于则进行优惠
                  //判断优惠券优惠的最大金额
                  if (orderPrice >= afterDiscountPrice) {
                    //console.log(afterDiscountPrice)
                    afterDiscountPrice = orderPrice;
                    //获取当前的商品折扣后的优惠价格
                    res.couponsMemberRelationMap.afterDiscountPrice = res.couponsMemberRelationMap.discountAmount
                      > 0 ? utilHelper.toFixed(unitPrice, 2) : (unitPrice);
                  }
                  res.couponsMemberRelationMap.isRelation = true;
                })
              }
            }
            //判断如果是优惠券满减的话这就进行优惠券的总价满减
            //console.log(res)
            if (res.couponsMemberRelationMap.preferentialType == 2) {
              if (this.data.prevData.fullPriceReductionAfter) {
                if (this.data.prevData.fullPriceReductionAfter >= res.couponsMemberRelationMap.limitedPrice) {
                  //console.log(res.couponsMemberRelationMap.limitedPrice)
                  res.couponsMemberRelationMap.afterDiscountPrice = this.data.prevData.fullPriceReductionAfter - res.couponsMemberRelationMap.reducedPrice;
                }
                return
              }
              if (this.data.prevData.actualPrice >= res.couponsMemberRelationMap.limitedPrice) {
                res.couponsMemberRelationMap.afterDiscountPrice = this.data.prevData.actualPrice - res.couponsMemberRelationMap.reducedPrice;
              }
            }
            return
          }
          console.log("从我的进入...")
          res.couponsMemberRelationMap.isRelation = true;
          //}
        })
        this.setData({
          couponsPointsList: result.data.records
        })
      }
    })
  },
  onClick(e) {
    //设置当前点击的使用规则为显示
    this.data.couponsList[e.currentTarget.dataset.index].couponsMemberRelationMap.isShow = this.data.couponsList[e.currentTarget.dataset.index].couponsMemberRelationMap.isShow ? false : true;
    this.setData({
      couponsList: this.data.couponsList
    })
  },
  onPointsClick(e) {
    //设置当前点击的使用规则为显示
    this.data.couponsPointsList[e.currentTarget.dataset.index].couponsMemberRelationMap.isShow = this.data.couponsPointsList[e.currentTarget.dataset.index].couponsMemberRelationMap.isShow ? false : true;
    this.setData({
      couponsPointsList: this.data.couponsPointsList
    })
  },
  onImmediateUse(e) {
    if (this.data.prevData) {
      //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
      //console.log(this.data)
      let afterDiscounts = this.data.prevData;
      //console.log(afterDiscounts)
      //console.log(afterDiscounts.fullPriceReductionAfter - afterDiscounts.price)
      afterDiscounts.id = this.data.couponsList[e.currentTarget.dataset.index].couponsMemberRelationMap.id;
      //console.log(this.data.couponsList[e.currentTarget.dataset.index].couponsMemberRelationMap.afterDiscountPrice)
      afterDiscounts.price = this.data.couponsList[e.currentTarget.dataset.index].couponsMemberRelationMap.afterDiscountPrice;
      afterDiscounts.couponsName = this.data.couponsList[e.currentTarget.dataset.index].couponsMemberRelationMap.couponsName;
      afterDiscounts.preferentialTypeText = systemStatus.preferentialTypeText(this.data.couponsList[e.currentTarget.dataset.index].preferentialType)
      console.log(this.data.prevData)
      // prevPage.setData({
      //   afterDiscount: afterDiscounts,
      //   "data.fullPriceReduction": utilHelper.toFixed((this.data.prevData.actualPrice - afterDiscounts.price), 2),
      //   "data.couponsIsHidden": true
      // })
      this.setData({
        afterDiscount: afterDiscounts,
        "prevData.fullPriceReduction": utilHelper.toFixed((this.data.prevData.actualPrice - afterDiscounts.price), 2),
        "prevData.couponsIsHidden": true
      })
      
      this.getFullReductionRule();
      //console.log(prevPage.data)
      //wx.navigateBack(1);
      return
    }
    let couponIndex=e.currentTarget.dataset.index;
    console.log(this.data.couponsList[couponIndex])
    if(this.data.couponsList[couponIndex].shopList.length==1){
      wx.navigateTo({
        url: "../../menu/index/index?id="+this.data.couponsList[couponIndex].shopList[0].id
      });
    }else if(this.data.couponsList[couponIndex].shopList.length>1){
      wx.switchTab({
        url: "/pages/index/index"
      });
    }else{
      wx.switchTab({
        url: "/pages/index/index"
      });
    }
  },
  onImmediatePointsUse(e) {
    if (this.data.prevData) {
      //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
      //console.log(this.data)
      let afterDiscounts = this.data.prevData;
      //console.log(afterDiscounts)
      //console.log(afterDiscounts.fullPriceReductionAfter - afterDiscounts.price)
      afterDiscounts.id = this.data.couponsPointsList[e.currentTarget.dataset.index].couponsMemberRelationMap.id;
      //console.log(this.data.couponsList[e.currentTarget.dataset.index].couponsMemberRelationMap.afterDiscountPrice)
      afterDiscounts.price = this.data.couponsPointsList[e.currentTarget.dataset.index].couponsMemberRelationMap.afterDiscountPrice;
      afterDiscounts.couponsName = this.data.couponsPointsList[e.currentTarget.dataset.index].couponsMemberRelationMap.couponsName;
      afterDiscounts.preferentialTypeText = systemStatus.preferentialTypeText(this.data.couponsPointsList[e.currentTarget.dataset.index].preferentialType)
      console.log(this.data.prevData)
      // prevPage.setData({
      //   afterDiscount: afterDiscounts,
      //   "data.fullPriceReduction": utilHelper.toFixed((this.data.prevData.actualPrice - afterDiscounts.price), 2),
      //   "data.couponsIsHidden": true
      // })
      this.setData({
        afterDiscount: afterDiscounts,
        "prevData.fullPriceReduction": utilHelper.toFixed((this.data.prevData.actualPrice - afterDiscounts.price), 2),
        "prevData.couponsIsHidden": true
      })
      this.getPointsFullReductionRule();
      //console.log(prevPage.data)
      //wx.navigateBack(1);
      return
    }
    let couponIndex=e.currentTarget.dataset.index;
    console.log(this.data.couponsPointsList[couponIndex])
    wx.switchTab({
      url: "../../mall/index/index"
    });
  },
  //获取满减规则
  getFullReductionRule() {
    toastService.showLoading();
    https.request('/api-promotion/rest/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1,
      shopId: this.data.prevData.shopId
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        var pages = getCurrentPages();
        var prevPage = pages[pages.length - 2]; //上一个页面
        //获取配送费，配送费不作为满减条件
        let feeData =app.deliveryAndSelfTaking.reducedDeliveryTotalPrice ? app.deliveryAndSelfTaking.reducedDeliveryTotalPrice : 0;
        let actualPrice = this.data.prevData.actualPrice;
        let fullPriceReductionBefore = actualPrice-feeData;
        let reducedDeliveryPrice=0;
        console.log("总商品价格===》"+this.data.prevData.actualPrice)
        this.data.prevData.actualPrice = this.data.prevData.actualPrice ? utilHelper.toFixed((Number(this.data.prevData.actualPrice)), 2) : 0;
        this.data.prevData.fullPriceReductionAfter = this.data.prevData.actualPrice;
        this.data.prevData.discountPrice = 0;
        this.data.prevData.fullPriceReductionBefore = fullPriceReductionBefore;
        this.data.prevData.fullPriceReductionIsHidden = this.data.prevData.fullPriceReductionIsHidden;
        this.data.prevData.limitedPrice = 0;
        
        if (prevPage.data.reducedDeliveryPrice >= feeData) {
          reducedDeliveryPrice = 0;
        } else {
          reducedDeliveryPrice = feeData - prevPage.data.reducedDeliveryPrice;
        }
        console.log("fullPriceReductionBefore===>"+fullPriceReductionBefore)
        console.log("获取商家配送费===="+ prevPage.data.reducedDeliveryPrice);
        console.log("获取用户地址配送费===="+ feeData);
        console.log("获取商家支付后的配送费===="+ reducedDeliveryPrice);
        console.log("获取价格===="+this.data.prevData.fullPriceReductionBefore);
        console.log("获取优惠券信息===>")
        console.log(this.data.afterDiscount)
        fullPriceReductionBefore-=this.data.afterDiscount.price;
        this.data.prevData.fullPriceReduction=fullPriceReductionBefore;
        console.log("获取优惠券后的金额===》"+fullPriceReductionBefore)
        for (let i = 0; i < result.data.records.length; i++) {
          //总价格减去配送费大于满减金额则进行满减操作
          if (Number(fullPriceReductionBefore) >= result.data.records[i].limitedPrice) {
            //判断当前满减价格limitedPrice和上一个满减价格对比，如果大于就进行认证
            if (result.data.records[i].limitedPrice > this.data.prevData.limitedPrice) {
              this.data.prevData.limitedPrice = result.data.records[i].limitedPrice;
              console.log(result.data.records[i].reducedPrice)
              this.data.prevData.fullPriceReduction = utilHelper.toFixed((fullPriceReductionBefore- result.data.records[i].reducedPrice), 2);
              this.data.prevData.fullReductionRuleName = result.data.records[i].name;
              this.data.prevData.fullReductionRuleId = result.data.records[i].id;
              this.data.prevData.fullPriceReductionIsHidden = true;
              this.data.prevData.fullPriceReductionAfter = utilHelper.toFixed((this.data.prevData.fullPriceReduction - result.data.records[i].reducedPrice), 2);
            }
          }
        }
        console.log("获取满减后的金额===》"+this.data.prevData.fullPriceReduction)
        if (app.deliveryAndSelfTaking.currentTab == 0) {
          this.data.prevData.fullPriceReduction = utilHelper.toFixed(this.data.prevData.fullPriceReduction + reducedDeliveryPrice,2);
        }
        app.deliveryAndSelfTaking.isThereADiscount=false;
        if(feeData!=reducedDeliveryPrice){
          app.deliveryAndSelfTaking.isThereADiscount=true;
        }
        console.log("获取价格===="+this.data.prevData.fullPriceReductionBefore);
        prevPage.setData({
          data:this.data.prevData,
          afterDiscount: this.data.afterDiscount
        });
        wx.navigateBack(1);
      }
    })
  },
  //获取满减规则
  getPointsFullReductionRule() {
    toastService.showLoading();
    https.request('/api-mall/rest/pointsMall/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        var pages = getCurrentPages();
        var prevPage = pages[pages.length - 2]; //上一个页面

        let actualPrice = this.data.prevData.actualPrice;
        let fullPriceReductionBefore = actualPrice;
        let reducedDeliveryPrice=0;
        console.log("总商品价格===》"+this.data.prevData.actualPrice)
        this.data.prevData.actualPrice = this.data.prevData.actualPrice ? utilHelper.toFixed((Number(this.data.prevData.actualPrice)), 2) : 0;
        this.data.prevData.fullPriceReductionAfter = this.data.prevData.actualPrice;
        this.data.prevData.discountPrice = 0;
        this.data.prevData.fullPriceReductionBefore = fullPriceReductionBefore;
        this.data.prevData.fullPriceReductionIsHidden = this.data.prevData.fullPriceReductionIsHidden;
        this.data.prevData.limitedPrice = 0;

        fullPriceReductionBefore-=this.data.afterDiscount.price;
        this.data.prevData.fullPriceReduction=fullPriceReductionBefore;
        console.log("获取优惠券后的金额===》"+fullPriceReductionBefore)
        for (let i = 0; i < result.data.records.length; i++) {
          //总价格减去配送费大于满减金额则进行满减操作
          if (Number(fullPriceReductionBefore) >= result.data.records[i].limitedPrice) {
            //判断当前满减价格limitedPrice和上一个满减价格对比，如果大于就进行认证
            if (result.data.records[i].limitedPrice > this.data.prevData.limitedPrice) {
              this.data.prevData.limitedPrice = result.data.records[i].limitedPrice;
              console.log(result.data.records[i].reducedPrice)
              this.data.prevData.fullPriceReduction = utilHelper.toFixed((fullPriceReductionBefore- result.data.records[i].reducedPrice), 2);
              this.data.prevData.fullReductionRuleName = result.data.records[i].name;
              this.data.prevData.fullReductionRuleId = result.data.records[i].id;
              this.data.prevData.fullPriceReductionIsHidden = true;
              this.data.prevData.fullPriceReductionAfter = utilHelper.toFixed((this.data.prevData.fullPriceReduction - result.data.records[i].reducedPrice), 2);
            }
          }
        }

        console.log("获取价格===="+this.data.prevData.fullPriceReductionBefore);
        prevPage.setData({
          data:this.data.prevData,
          afterDiscount: this.data.afterDiscount
        });
        wx.navigateBack(1);
      }
    })
  },
  getFullReductionRule_() {
    toastService.showLoading();
    https.request('/api-promotion/rest/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1,
      shopId: this.data.prevData.shopId
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        var pages = getCurrentPages();
        var prevPage = pages[pages.length - 2]; //上一个页面
        //获取配送费，配送费不作为满减条件
        let feeData =app.deliveryAndSelfTaking.reducedDeliveryTotalPrice ? app.deliveryAndSelfTaking.reducedDeliveryTotalPrice : 0;
        let reducedDeliveryPrice=0;
        console.log("商品总价----》"+this.data.prevData.actualPrice)
        if (prevPage.data.reducedDeliveryPrice >=feeData) {
          reducedDeliveryPrice = 0;
          console.log("用户需支付的配送费为====>"+reducedDeliveryPrice);
        } else {
          reducedDeliveryPrice = feeData-prevPage.data.reducedDeliveryPrice;
          console.log("用户需支付的配送费为====>"+reducedDeliveryPrice);
        }
        this.data.prevData.actualPrice = this.data.prevData.actualPrice ? utilHelper.toFixed((Number(this.data.prevData.actualPrice)), 2) : 0;
        this.data.prevData.fullPriceReductionAfter = this.data.prevData.actualPrice;
        this.data.prevData.discountPrice = 0;
        this.data.prevData.fullPriceReductionBefore = (this.data.prevData.actualPrice) > 0 ? (this.data.prevData.actualPrice-feeData) : 0;
        this.data.prevData.fullPriceReductionIsHidden = this.data.prevData.fullPriceReductionIsHidden;
        this.data.prevData.limitedPrice = 0;
        console.log("获取商家配送费===="+ prevPage.data.reducedDeliveryPrice);
        console.log("获取用户地址配送费===="+ feeData);
        console.log("获取商家支付后的配送费===="+ reducedDeliveryPrice);
        console.log("获取价格===="+this.data.prevData.fullPriceReductionBefore);
        this.data.prevData.fullPriceReduction=this.data.prevData.fullPriceReductionBefore;
        for (let i = 0; i < result.data.records.length; i++) {
          //总价格减去配送费大于满减金额则进行满减操作
          if (Number(this.data.prevData.fullPriceReductionBefore) >= result.data.records[i].limitedPrice) {
            //判断当前满减价格limitedPrice和上一个满减价格对比，如果大于就进行认证
            if (result.data.records[i].limitedPrice > this.data.prevData.limitedPrice) {
              
              this.data.prevData.limitedPrice = result.data.records[i].limitedPrice;
              this.data.prevData.fullPriceReduction = utilHelper.toFixed((this.data.prevData.fullPriceReductionBefore - result.data.records[i].reducedPrice), 2);
              this.data.prevData.fullReductionRuleName = result.data.records[i].name;
              this.data.prevData.fullReductionRuleId = result.data.records[i].id;
              this.data.prevData.fullPriceReductionIsHidden = true;
              this.data.prevData.fullPriceReductionAfter = utilHelper.toFixed((this.data.prevData.fullPriceReductionBefore - result.data.records[i].reducedPrice), 2);
              //console.log(this.data.prevData.fullPriceReduction)
            }
          }
        }
        console.log("获取最终配送金额====="+reducedDeliveryPrice)
        console.log("获取最终优惠金额====="+this.data.prevData.fullPriceReduction)
        this.data.prevData.fullPriceReduction = this.data.prevData.fullPriceReduction;
        if (app.deliveryAndSelfTaking.currentTab == 0) {
          this.data.prevData.fullPriceReduction = this.data.prevData.fullPriceReduction + reducedDeliveryPrice;
        }
        app.deliveryAndSelfTaking.isThereADiscount=false;
        if(reducedDeliveryPrice!=feeData){
          app.deliveryAndSelfTaking.isThereADiscount=true;
        }
        console.log("获取最终价格===="+this.data.prevData.fullPriceReduction)
        prevPage.setData({
          feeData:reducedDeliveryPrice,
          data:this.data.prevData,
          "data.couponsIsHidden": false
        });
      }
    })
  },
  getPonintsFullReductionRule_() {
    toastService.showLoading();
    https.request('/api-mall/rest/pointsMall/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        var pages = getCurrentPages();
        var prevPage = pages[pages.length - 2]; //上一个页面
        //获取配送费，配送费不作为满减条件
        let feeData =app.deliveryAndSelfTaking.reducedDeliveryTotalPrice ? app.deliveryAndSelfTaking.reducedDeliveryTotalPrice : 0;
        let reducedDeliveryPrice=0;
        console.log("商品总价----》"+this.data.prevData.actualPrice)
        this.data.prevData.actualPrice = this.data.prevData.actualPrice ? utilHelper.toFixed((Number(this.data.prevData.actualPrice)), 2) : 0;
        this.data.prevData.fullPriceReductionAfter = this.data.prevData.actualPrice;
        this.data.prevData.discountPrice = 0;
        this.data.prevData.fullPriceReductionBefore = (this.data.prevData.actualPrice) > 0 ? (this.data.prevData.actualPrice-feeData) : 0;
        this.data.prevData.fullPriceReductionIsHidden = this.data.prevData.fullPriceReductionIsHidden;
        this.data.prevData.limitedPrice = 0;
        
        this.data.prevData.fullPriceReduction=this.data.prevData.fullPriceReductionBefore;
        for (let i = 0; i < result.data.records.length; i++) {
          //总价格减去配送费大于满减金额则进行满减操作
          if (Number(this.data.prevData.fullPriceReductionBefore) >= result.data.records[i].limitedPrice) {
            //判断当前满减价格limitedPrice和上一个满减价格对比，如果大于就进行认证
            if (result.data.records[i].limitedPrice > this.data.prevData.limitedPrice) {
              
              this.data.prevData.limitedPrice = result.data.records[i].limitedPrice;
              this.data.prevData.fullPriceReduction = utilHelper.toFixed((this.data.prevData.fullPriceReductionBefore - result.data.records[i].reducedPrice), 2);
              this.data.prevData.fullReductionRuleName = result.data.records[i].name;
              this.data.prevData.fullReductionRuleId = result.data.records[i].id;
              this.data.prevData.fullPriceReductionIsHidden = true;
              this.data.prevData.fullPriceReductionAfter = utilHelper.toFixed((this.data.prevData.fullPriceReductionBefore - result.data.records[i].reducedPrice), 2);
              //console.log(this.data.prevData.fullPriceReduction)
            }
          }
        }

        console.log("获取最终价格===="+this.data.prevData.fullPriceReduction)
        prevPage.setData({
          data:this.data.prevData,
          "data.couponsIsHidden": false
        });
      }
    })
  },
  onRadioChange(e) {
    var pages = getCurrentPages();
    var prevPage = pages[pages.length - 2]; //上一个页面
    //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
    let afterDiscounts = this.data.prevData;
    // console.log(afterDiscounts)
    afterDiscounts.id = null;
    afterDiscounts.price = 0;
    afterDiscounts.couponsName = null;
    afterDiscounts.couponsDescription = null;
    afterDiscounts.preferentialType = null;
    prevPage.setData({
      afterDiscount: afterDiscounts
    })
    this.getFullReductionRule_();

    this.data.couponsList[e.currentTarget.dataset.index].couponsMemberRelationMap.checked = false;

    this.setData({
      couponsList: this.data.couponsList
    })
  },
  onPointsRadioChange(e) {
    var pages = getCurrentPages();
    var prevPage = pages[pages.length - 2]; //上一个页面
    //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
    let afterDiscounts = this.data.prevData;
    // console.log(afterDiscounts)
    afterDiscounts.id = null;
    afterDiscounts.price = 0;
    afterDiscounts.couponsName = null;
    afterDiscounts.couponsDescription = null;
    afterDiscounts.preferentialType = null;
    prevPage.setData({
      afterDiscount: afterDiscounts
    })
    this.getPonintsFullReductionRule_();

    this.data.couponsPointsList[e.currentTarget.dataset.index].couponsMemberRelationMap.checked = false;

    this.setData({
      couponsPointsList: this.data.couponsPointsList
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    //可以直接取上一个页面的数据
    var prevData = options.prevData;
    console.log(prevData)
    if (prevData) {
      prevData = JSON.parse(prevData);
      console.log(prevData)
      prevData.checked = true;
      this.setData({
        prevData: prevData
      });
      console.log(prevData.type)
      if(prevData.type==2){
        this.getCouponsMemberPointsMallRelation();
      }else{
        this.getCouponsMemberRelation();
      }
      this.setData({
        currentTab:prevData.type-1
      })
    }else{
      
      this.getCouponsMemberRelation();
      this.getCouponsMemberPointsMallRelation();
    }
    this.getHeight();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    //this.getFullReductionRule();
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    //this.getFullReductionRule();
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    // if(this.data.isLastPage){
    //   toastService.showToast("没有更多啦~");
    //   return;
    // }
    // pageNo = pageNo + 1;
    // this.getCouponsMemberRelation();
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})