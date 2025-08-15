import https from '../../../utils/http';
import authService from '../../../utils/auth';
import toastService from '../../../utils/toast.service';
import systemStatus from '../../../utils/system-status';
import dateHelper from '../../../utils/date-helper';
import utilHelper from '../../../utils/util';
import base64 from '../../../utils/base64';
const app = getApp();
var wxNotifyTemplates = [];
Page({

  /**
   * 页面的初始数据
   */
  data: {
    time: '10:00',
    isChoose: false, //是否选择派送方式
    isFirstShop: false, //是否选择的是门店
    isFirstAddress: false,
    inputLength: 0,
    deliveryData: {},
    remarks: "",
    modeTabList: [{
      modeName: "外卖配送",
      modeId: 0
    }, {
      modeName: "门店自取",
      modeId: 1
    }],
    currentTab: 0,
    reducedDeliveryPrice: 0,
    title: "请选择支付方式",
    // paymentModes: [{
    //   checked: true,
    //   value: 1,
    //   text: '微信支付',
    //   icon: 'iconwechat_pay',
    //   show:true,
    // }, {
    //   checked: false,
    //   value: 2,
    //   text: '平台余额',
    //   icon: 'iconyue',
    //   show:true,
    // }],
    paymentModes: [{
      checked: false,
      value: 1,
      text: '微信支付',
      icon: 'iconwechat_pay',
      show: true,
    }, {
      checked: true,
      value: 2,
      text: '平台余额',
      icon: 'iconyue',
      show: true,
    }],
    paymentModeIndex: 1,
    showPayPwdInput: false, //是否展示密码输入层
    pwdVal: '', //输入的密码
    payFocus: false, //文本框焦点
    adjustPosition: false,
    holdKeyboard: true,
    isVipDialogShow: false
  },
  getTimestamp() {
    var timestamp = dateHelper.getTimestamp();
    console.log(timestamp)
    this.setData({
      timestamp: timestamp
    })
  },
  // 滑动切换tab
  bindSlideChange: function (e) {
    var that = this;
    that.setData({
      currentTab: e.detail.current
    });
  },
  //点击切换 
  clickTab: function (e) {
    var that = this;
    app.deliveryAndSelfTaking.radioIndex = e.target.dataset.current;
    let time = dateHelper.formatTime("hms");

    if (that.data.deliveryAndSelfTaking.currentTab === e.target.dataset.current) {
      return false;
    } else {
      let feeData = app.deliveryAndSelfTaking.feeData;
      let actualPrice = this.data.data.actualPrice;
      let fullPriceReduction = this.data.data.fullPriceReduction;
      if (e.target.dataset.current == 1) {
        actualPrice = actualPrice - feeData;
        fullPriceReduction = fullPriceReduction - feeData;
      } else {
        actualPrice = actualPrice + feeData;
        fullPriceReduction = fullPriceReduction + feeData;
      }
      that.setData({
        "deliveryAndSelfTaking.currentTab": e.target.dataset.current,
        time: time,
        "data.actualPrice": utilHelper.toFixed(actualPrice, 2),
        "data.fullPriceReduction": utilHelper.toFixed(fullPriceReduction, 2)
      });
    }
  },
  radioChange(e) {
    let checkValue = e.currentTarget.dataset.radioindex;
    let deliveryData = JSON.stringify(this.data.deliveryAndSelfTaking);
    console.log(this.data)
    wx.navigateTo({
      url: '../../address/choose/choose?radioIndex=' + checkValue + '&deliveryData=' + deliveryData + '&shopId=' + this.data.initData.shopId + '&reducedDeliveryPrice=' + this.data.reducedDeliveryPrice + '&pageType=pay',
    })
  },
  getLoginMemberInfo: function (e) {
    toastService.showLoading();
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        result.data.typeVipText = systemStatus.typeVipText(result.data.vipType);
        result.data.statusVipText = systemStatus.statusVipText(result.data.vipStatus);
        result.data.vipStartTime = dateHelper.formatDate(result.data.vipStartTime);
        result.data.vipEndTime = dateHelper.formatDate(result.data.vipEndTime);

        this.setData({
          userInfo: result.data
        });
        setTimeout(time => {
          this.choosePayModeTap();
          clearTimeout(time);
        }, 1000)
      }
    })
  },
  closeDialog: function () {
    this.setData({
      dialogShow: false
    })
  },
  close() {
    this.setData({
      isVipDialogShow: false
    })
    toastService.showLoading();
    var _this = this;
    https.request('/api-order/rest/member/order/insert', _this.data.isPayJson).then(result => {
      toastService.hideLoading();
      if (result.success) {
        // toastService.showLoading("正在支付...", true);
        //console.log(result.data)
        _this.toPay4Applet(result.data.id, result.data.orderNo, result.data.actualPrice);
      }
    })
  },
  btnClick(e) {
    console.log(e);
    this.closeDialog();
  },
  radioChangeAddress(e) {
    console.log(e)
    var that = this;
    var paymentModeIndex = e.detail.value;
    authService.getOpenId().then(openId => {
      console.log(openId)
      that.closeDialog();
      // if (!openId) {
      //   toastService.showToast("登录用户错误，请重新登录");
      //   return
      // }
      let paymentModes = that.data.paymentModes;
      // if (e.detail.value == 0) {
      //   toastService.showToast("暂不支持微信支付，请选择余额支付/积分支付");
      //   return
      // }
      if (e.detail.value == 1) {
        console.log(that.data.userInfo.paymentPassword);
        if (!that.data.userInfo.paymentPassword) {
          wx.navigateTo({
            url: '../../mine/security/index/index',
          });
          return
        }
        if (!paymentModes[e.detail.value].isBindTap) {
          toastService.showToast(paymentModes[e.detail.value].desc);
          return
        }
      }

      for (let key in paymentModes) {
        paymentModes[key].checked = false;
      }
      paymentModes[e.detail.value].checked = true;
      that.setData({
        paymentModes: paymentModes,
        paymentModeIndex: e.detail.value
      });
    });
  },
  choosePayModeTap() {
    let paymentModes = this.data.paymentModes;
    let paymentModeIndex = this.data.paymentModeIndex;
    let actualPrice = !this.data.data.fullPriceReductionIsHidden && !this.data.data.couponsIsHidden && !this.data.deliveryAndSelfTaking.isThereADiscount ? this.data.data.actualPrice : this.data.data.fullPriceReduction
    console.log("余额=", this.data.userInfo.balance)
    console.log("需支付金额=", actualPrice);
    if (paymentModes[paymentModeIndex].value == 2) {
      paymentModes[paymentModeIndex].desc = "";
      paymentModes[paymentModeIndex].isBindTap = true;
      if (actualPrice > this.data.userInfo.balance) {
        paymentModes[paymentModeIndex].desc = "余额不足";
        paymentModes[paymentModeIndex].isBindTap = false;
      }
      if (!this.data.userInfo.paymentPassword) {
        paymentModes[paymentModeIndex].desc = "未设置支付密码,去设置";
        paymentModes[paymentModeIndex].isBindTap = false;
      }
    }

    // this.setData({
    //   dialogShow: true,
    //   maskClosable: false,
    //   title: "请选择支付方式",
    //   paymentModes: paymentModes
    // })
    toastService.hideLoading();
    this.setData({
      paymentModes: paymentModes
    });
  },
  remarksInput(e) {
    this.setData({
      inputLength: e.detail.value.length,
      remarks: e.detail.value
    })
  },
  getShopAddressList() {
    https.request('/api-merchant/rest/shop/detail', {
      id: this.data.initData.shopId,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      if (result.success) {
        this.setData({
          shopAddress: result.data,
          reducedDeliveryPrice: result.data.shop.reducedDeliveryPrice
        })
        this.getPsf();
      }
    })
  },
  getCouponsMemberRelation(pageData) {
    toastService.showLoading();
    https.request('/api-promotion/rest/member/couponsMemberRelation/list', {
      pageNo: -1,
      pageSize: 20,
      isUsed: 0,
      isExpired: 0,
      isValid: 0,
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        let afterDiscounts = [];
        result.data.records.forEach(res => {
          //判断优惠券是否过期和是否已经使用
          //if (!res.couponsMemberRelationMap.isExpired && !res.couponsMemberRelationMap.isUsed) {
          //console.log(this.data)
          //判断是否已经满减，如果已经满减就取满减之后的字段值，反之取总额
          //if (this.data.data.actualPrice && !this.data.data.fullPriceReduction) {

          //判断当前优惠券是折扣还是满减券,1等于折扣,2等于满减
          if (res.couponsMemberRelationMap.preferentialType == 1) {
            if (res.shopList.length <= 0) {
              return
            }
            res.shopList.forEach((shop, shopIndex) => {
              if (shop.id == this.data.data.shopId) {
                //遍历当前订单的商品
                this.data.data.orderDetailList.forEach((order, orderIndex) => {
                  //source  优惠券发放来源 1=商家中心 2=调度中心
                  //如果是商家中心发放的优惠券，则需要判断关联商品
                  //调度中心发放的优惠券，则无需判断关联商品，所有商品皆可使用
                  console.log(order.price)
                  let orderPrice = order.price;
                  let unitPrice = utilHelper.toFixed(orderPrice - (orderPrice * res.couponsMemberRelationMap.discountAmount), 2);
                  if (res.couponsMemberRelationMap.source == 1) {
                    res.goodsList.forEach(goods => {
                      //判断当前订单的商品是否等于优惠券绑定的优惠商品
                      //等于则进行优惠
                      if (order.goodsId == goods.id) {
                        afterDiscounts.push({
                          id: res.couponsMemberRelationMap.id,
                          price: res.couponsMemberRelationMap.discountAmount != 0 ? unitPrice : unitPrice,
                          goodsId: order.goodsId,
                          couponsId: res.couponsMemberRelationMap.couponsId,
                          couponsName: res.couponsMemberRelationMap.couponsName,
                          preferentialType: systemStatus.preferentialTypeText(res.couponsMemberRelationMap.preferentialType),
                          isExpired: res.couponsMemberRelationMap.isExpired,
                          isUsed: res.couponsMemberRelationMap.isUsed,
                          isValid: res.couponsMemberRelationMap.isValid
                        });
                      }
                    });
                    return
                  }
                  afterDiscounts.push({
                    id: res.couponsMemberRelationMap.id,
                    price: res.couponsMemberRelationMap.discountAmount != 0 ? unitPrice : unitPrice,
                    goodsId: order.goodsId,
                    couponsId: res.couponsMemberRelationMap.couponsId,
                    couponsName: res.couponsMemberRelationMap.couponsName,
                    preferentialType: systemStatus.preferentialTypeText(res.couponsMemberRelationMap.preferentialType),
                    isExpired: res.couponsMemberRelationMap.isExpired,
                    isUsed: res.couponsMemberRelationMap.isUsed,
                    isValid: res.couponsMemberRelationMap.isValid
                  });
                })
              }
            })
          }
          //判断如果是优惠券满减的话这就进行优惠券的总价满减
          //console.log(res)
          if (res.couponsMemberRelationMap.preferentialType == 2) {
            if (this.data.data.fullPriceReduction) {
              if (this.data.data.fullPriceReduction >= res.couponsMemberRelationMap.limitedPrice) {
                //console.log(res.couponsMemberRelationMap.limitedPrice)
                afterDiscounts.push({
                  id: res.couponsMemberRelationMap.id,
                  couponsId: res.couponsMemberRelationMap.couponsId,
                  price: utilHelper.toFixed(this.data.data.fullPriceReduction, 2) - res.couponsMemberRelationMap.reducedPrice,
                  couponsName: res.couponsMemberRelationMap.couponsName,
                  preferentialType: systemStatus.preferentialTypeText(res.couponsMemberRelationMap.preferentialType),
                  isExpired: res.couponsMemberRelationMap.isExpired,
                  isUsed: res.couponsMemberRelationMap.isUsed,
                  isValid: res.couponsMemberRelationMap.isValid
                });
              }
              return
            }
            if (this.data.data.actualPrice >= res.couponsMemberRelationMap.limitedPrice) {
              afterDiscounts.push({
                id: res.couponsMemberRelationMap.id,
                couponsId: res.couponsMemberRelationMap.couponsId,
                price: this.data.data.actualPrice - res.couponsMemberRelationMap.reducedPrice,
                couponsName: res.couponsMemberRelationMap.couponsName,
                preferentialType: systemStatus.preferentialTypeText(res.couponsMemberRelationMap.preferentialType),
                isExpired: res.couponsMemberRelationMap.isExpired,
                isUsed: res.couponsMemberRelationMap.isUsed,
                isValid: res.couponsMemberRelationMap.isValid
              });
            }
          }
          //}
          //}
        })
        //console.log(afterDiscounts)
        //如果优惠券的使用张数大于0张
        let fullPriceReduction = this.data.data.actualPrice;
        var afterDiscountPrice = 0,
          price = 0,
          afterDiscountList, couponsIsHidden = false;
        if (afterDiscounts.length > 0) {
          //遍历所有可以使用优惠券的商品，并计算出最大的优惠券
          afterDiscounts.forEach(afterDiscount => {
            price = afterDiscount.price;
            if (Number(price) >= afterDiscountPrice) {
              afterDiscountPrice = price;
              afterDiscountList = afterDiscount;
            }
          });
          fullPriceReduction = this.data.data.fullPriceReduction ? (this.data.data.fullPriceReduction - afterDiscountPrice) : (this.data.data.actualPrice - afterDiscountPrice);
          couponsIsHidden = true;
        }
        this.setData({
          afterDiscount: afterDiscountList,
          "data.couponsIsHidden": couponsIsHidden,
          "data.fullPriceReduction": utilHelper.toFixed(fullPriceReduction <= 0 ? 0 : fullPriceReduction, 2),
        });
        this.getFullReductionRule();

      }
    })
  },
  onCoupon() {
    let afterDiscount = this.data.afterDiscount;
    afterDiscount.fullPriceReduction = this.data.data.fullPriceReduction;
    afterDiscount.actualPrice = this.data.data.actualPrice;
    afterDiscount.fullPriceReductionIsHidden = this.data.data.fullPriceReductionIsHidden;
    afterDiscount.orderDetailList = this.data.data.orderDetailList;
    afterDiscount.fullPriceReductionAfter = this.data.data.fullPriceReductionAfter;
    afterDiscount.shopId = this.data.data.shopId;
    afterDiscount.limitedPrice = this.data.data.limitedPrice;
    afterDiscount.fullReductionRuleName = this.data.data.fullReductionRuleName;
    afterDiscount.fullReductionRuleId = this.data.data.fullReductionRuleId;
    afterDiscount.fullPriceReductionIsHidden = this.data.data.fullPriceReductionIsHidden;
    afterDiscount.packingCharges = this.data.data.packingCharges;
    afterDiscount.feeData = this.data.deliveryAndSelfTaking.feeData;
    afterDiscount.type = 1;
    //console.log(afterDiscount)
    wx.navigateTo({
      url: '../../mine/coupons/coupons?prevData=' + JSON.stringify(afterDiscount),
    })
  },
  selectCommissionReward() {
    console.log(this.data)
    https.request('/api-order/rest/member/order/selectCommissionReward', {
      actualPrice: !this.data.data.fullPriceReductionIsHidden && !this.data.data.couponsIsHidden ? this.data.data.actualPrice : this.data.data.fullPriceReduction
    }).then(result => {
      if (result.success) {
        this.setData({
          tipReward: result.data
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      deliveryAndSelfTaking: app.deliveryAndSelfTaking
    });
    let data = JSON.parse(options.orderDetail);
    console.log(app.deliveryAndSelfTaking)
    console.log(data)
    if (app.deliveryAndSelfTaking.deliveryAddress && app.deliveryAndSelfTaking.currentTab == 0) {
      //选择地址和更换地址进行配送费的加减操作
      https.request('/api-order/rest/common/selectDeliveryFee', {
        deliveryAddressId: app.deliveryAndSelfTaking.deliveryAddress.id,
        shopId: data.shopId
      }).then(result => {
        console.log(result)
        app.deliveryAndSelfTaking.reducedDeliveryTotalPrice = result.data;
        app.deliveryAndSelfTaking.feeData = result.data;
        app.deliveryAndSelfTaking.actualPrice = data.actualPrice + result.data;
        data.actualPrice = data.actualPrice + result.data;
        this.setData({
          deliveryAndSelfTaking: app.deliveryAndSelfTaking,
          "deliveryAndSelfTaking.isReducedDeliveryPrice": true
        });
        this.getCouponsMemberRelation(data);
      })
    } else {
      // this.setData({
      //   "deliveryAndSelfTaking.deliveryAddress": null
      // });
      //data.actualPrice = data.actualPrice + app.deliveryAndSelfTaking.reducedDeliveryTotalPrice;
      this.getCouponsMemberRelation(data);
    }

    let time = dateHelper.formatTime("hms");
    this.setData({
      data: data,
      payType: options.payType,
      time: time,
      initData: data,
      isJs: true
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    console.log("onReady")
    this.getShopAddressList();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    if (this.data.showPayPwdInput) {
      this.showPwdLayer();
    }
    this.getLoginMemberInfo();
    //console.log(this.data)
    if (app.globalData.loginUserInfo.isRequestWxNotify) {
      this.getWxNotifyTemplate();
    }
    setTimeout((time) => {
      this.selectCommissionReward();
      clearTimeout(time);
    }, 2000);
    this.getTimestamp();
  },
  getWxNotifyTemplate() {
    https.request('/api-order/rest/wxNotifyTemplate/orderModule/list').then(result => {
      if (result.success) {
        console.log("订单模块的模板id===>" + result.data);
        wxNotifyTemplates = result.data;
      }
    });
  },
  updateIsRequestWxNotify() {
    https.request('/api-user/rest/member/updateIsRequestWxNotify').then(result => {
      if (result.success) {
        console.log("修改用户的是否需要请求授权服务通知状态为否成功");

      }
    });
  },
  getRequestSubscribeMessage() {
    let self = this;
    console.log("用户的是否需要请求授权服务通知====>" + app.globalData.loginUserInfo.isRequestWxNotify);

    if (this.data.paymentModeIndex == 1) {
      console.log(this.data.userInfo.paymentPassword);
      if (!this.data.userInfo.paymentPassword) {
        wx.navigateTo({
          url: '../../mine/security/index/index',
        });
        return
      }
    }
    if (app.globalData.loginUserInfo.isRequestWxNotify) {
      wx.requestSubscribeMessage({
        tmplIds: wxNotifyTemplates,
        success(res) {
          console.log("订单模块的模板授权成功")
          console.log(res);
          self.weChatPayTap();
          self.updateIsRequestWxNotify();
        },
        fail(error) {
          console.log("订单模块的模板授权失败")
          console.log(error);
          self.weChatPayTap();
        }
      });
      return
    }
    self.weChatPayTap();
  },
  weChatPayTap() {
    //判断店铺是否打烊
    // app.getIsBusiness().then(result => {
    //   if (!result) {
    //     return
    //   }

    authService.checkIsLogin().then(result => {
      if (result) {
        //判断店铺是否打烊
        let startTime = this.data.shopAddress.shop.startTime;
        let endTime = this.data.shopAddress.shop.endTime;
        let isOperating = this.data.shopAddress.shop.isOperating;
        app.getIsBusiness(startTime, endTime, isOperating).then(result => {
          if (!result) {
            return
          }
          var data = {};
          if (this.data.deliveryAndSelfTaking.currentTab == 1) {
            if (this.data.shopAddress.shop == null) {
              toastService.showToast("请选择门店地址");
              return;
            }
            data.shopId = this.data.shopAddress.shop.id;
          }
          if (this.data.deliveryAndSelfTaking.currentTab == 0) {
            if (this.data.deliveryAndSelfTaking.deliveryAddress == null) {
              toastService.showToast("请选择配送地址");
              return;
            }
            data.deliveryAddressId = this.data.deliveryAndSelfTaking.deliveryAddress.id;
            data.deliveryFee = this.data.deliveryAndSelfTaking.feeData;
          }
          var list = this.data.data.orderDetailList;
          var orderDetailList = [];
          data.shoppingCartIdList = [];
          for (var key in list) {
            orderDetailList.push({
              goodsId: list[key].goodsId,
              goodsName: list[key].goodsName,
              specList: list[key].specList,
              number: list[key].number
            })
            if (this.data.payType) {
              data.shoppingCartIdList.push(list[key].id);
            }
          }
          //console.log(this.data)
          data.orderDetailListStr = JSON.stringify(orderDetailList);
          data.actualPrice = this.data.data.fullPriceReductionIsHidden || this.data.data.couponsIsHidden || this.data.deliveryAndSelfTaking.isThereADiscount ? this.data.data.fullPriceReduction : this.data.data.actualPrice;
          //data.actualPrice = data.actualPrice + this.data.feeData;
          data.remark = this.data.remarks;
          data.shoppingWay = this.data.deliveryAndSelfTaking.currentTab == 0 ? 2 : 1;
          data.shopId = this.data.initData.shopId;
          //console.log(data)
          if (this.data.data.couponsIsHidden) {
            data.couponsId = this.data.afterDiscount.couponsId;
            data.couponsMemberRelationId = this.data.afterDiscount.id;
            data.couponsDescription = this.data.afterDiscount.couponsName;
          }
          //console.log(this.data)
          if (this.data.data.fullReductionRuleId) {
            data.fullReductionRuleId = this.data.data.fullReductionRuleId;
            data.fullReductionRuleDescription = this.data.data.fullReductionRuleName;
          }
          var that = this;
          //获取订单的配送方式
          let orderMode = this.data.modeTabList[this.data.currentTab].modeName;
          let addressMode = this.data.deliveryAndSelfTaking.currentTab == 1 ? orderMode + "门店：" + this.data.shopAddress.shop.name : orderMode + "地址：" + this.data.deliveryAndSelfTaking.deliveryAddress.province + this.data.deliveryAndSelfTaking.deliveryAddress.city + this.data.deliveryAndSelfTaking.deliveryAddress.area + this.data.deliveryAndSelfTaking.deliveryAddress.street;
          // toastService.showModal("【" + orderMode + "】订单", "订单确认后将无法更改!\r\n" + addressMode, function comfirm() {
          //   toastService.showLoading();
          //   https.request('/api-order/rest/member/order/insert', data).then(result => {
          //     toastService.hideLoading();
          //     if (result.success) {
          //       // toastService.showLoading("正在支付...", true);
          //       //console.log(result.data)
          //       that.toPay4Applet(result.data.id, result.data.orderNo, result.data.actualPrice);
          //     }
          //   })
          // }, null)
          if (this.data.userInfo.type == 1) {
            this.setData({
              isVipDialogShow: true,
              isPayJson: data
            });
          } else {
            toastService.showLoading();
            https.request('/api-order/rest/member/order/insert', data).then(result => {
              toastService.hideLoading();
              if (result.success) {
                // toastService.showLoading("正在支付...", true);
                //console.log(result.data)
                that.toPay4Applet(result.data.id, result.data.orderNo, result.data.actualPrice);
              }
            })
          }

        });
        return
      }
      app.checkIsAuth("scope.userInfo");
    });
  },
  toPay4Applet(id, orderNo, actualPrice) {
    toastService.showLoading("正在加载...", true);
    authService.getOpenId().then(openId => {
      //console.log(openId)
      // if (!openId) {
      //   toastService.showToast("登录用户错误，请重新登录");
      //   return
      // }
      openId = this.data.userInfo.openId;
      if (this.data.paymentModes[this.data.paymentModeIndex].value == 1) {
        this.weChatPay(id, orderNo, actualPrice, openId);
      }
      if (this.data.paymentModes[this.data.paymentModeIndex].value == 2) {
        this.setData({
          showPayPwdInput: true,
          payFocus: true
        });
        toastService.hideLoading();
        this.setData({
          balanceId: id,
          balanceOrderNo: orderNo,
          balanceActualPrice: actualPrice,
          balanceOpenId: openId,
        })
        // if (this.data.pwdVal.length >= 6){
        //   this.balancePay(id, orderNo, actualPrice,openId);
        // }
      }
    });
  },
  weChatPay(id, orderNo, actualPrice, openId) {
    https.request('/api-order/rest/member/wxPay/toPay4Applet', {
      openid: openId,
      type: 1,
      paymentMode: 1,
      out_trade_no: orderNo,
      total_fee: this.data.data.fullPriceReductionIsHidden ? this.data.data.fullPriceReduction : actualPrice
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        //console.log(result)
        wx.requestPayment({
          appId: result.data.appid,
          timeStamp: result.data.timeStamp,
          nonceStr: result.data.nonceStr,
          package: result.data.package,
          signType: 'MD5',
          paySign: result.data.paySign,
          success(res) {
            toastService.showSuccess("支付成功", true);
            let timeout = setTimeout(() => {
              wx.redirectTo({
                url: '../../order/detail/detail?id=' + id,
              });
              clearTimeout(timeout);
            }, 1000)
          },
          fail(res) {
            toastService.showError("支付失败", true);
            let timeout = setTimeout(() => {
              wx.redirectTo({
                url: '../../order/detail/detail?id=' + id,
              });
              clearTimeout(timeout);
            }, 1000)
          }
        })
      }
    })
  },
  inputPwd: function (e) {
    this.setData({
      pwdVal: e.detail.value
    });
    if (e.detail.value.length >= 6) {
      toastService.showLoading("正在加载...");
      this.balancePay();
    }
  },
  /**
   * 获取焦点
   */
  getFocus: function () {
    this.setData({
      payFocus: true
    });
  },
  balancePay() {

    var password = base64.encode(this.data.pwdVal);
    this.setData({
      pwdVal: '',
      payFocus: true
    });
    https.request('/api-order/rest/member/platformPay/byBalance', {
      openid: this.data.balanceOpenId,
      type: 1,
      paymentMode: 2,
      paymentPassword: password,
      out_trade_no: this.data.balanceOrderNo,
      total_fee: this.data.data.fullPriceReductionIsHidden ? this.data.data.fullPriceReduction : this.data.balanceActualPrice
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        toastService.showSuccess("支付成功", true);
        this.hidePwdLayer();
        let timeout = setTimeout(() => {
          wx.redirectTo({
            url: '../../order/detail/detail?id=' + this.data.balanceId,
          });
          clearTimeout(timeout);
        }, 1000);
      }
    })
  },
  bindtouchend(e) {
    console.log("触摸结束");
    this.showPwdLayer();
  },
  balancePayFail() {
    this.hidePwdLayer();
    toastService.showError("支付失败", true);
    let timeout = setTimeout(() => {
      wx.redirectTo({
        url: '../../order/detail/detail?id=' + this.data.balanceId,
      });
      clearTimeout(timeout);
    }, 1000);
  },
  hidePwdLayer() {
    this.setData({
      showPayPwdInput: false,
      payFocus: false,
      pwdVal: ''
    });
  },
  showPwdLayer() {
    this.setData({
      showPayPwdInput: true,
      payFocus: true,
      pwdVal: ''
    });
  },
  /**
   * 隐藏支付密码输入层
   */
  forgetThePassword: function () {
    wx.navigateTo({
      url: "../../mine/security/index/index"
    })
  },
  //获取满减规则
  getFullReductionRule(data) {
    toastService.showLoading();
    https.request('/api-promotion/rest/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1,
      shopId: this.data.data.shopId
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        //获取配送费，配送费不作为满减条件
        let feeData = app.deliveryAndSelfTaking.reducedDeliveryTotalPrice ? app.deliveryAndSelfTaking.reducedDeliveryTotalPrice : 0;
        // if (app.deliveryAndSelfTaking.currentTab == 1) {
        //   feeData = app.deliveryAndSelfTaking.feeData ? app.deliveryAndSelfTaking.feeData : 0;
        // }

        if (!this.data.isPreviousPage) {
          this.data.data.packingCharges = 0;
          for (var key in this.data.data.orderDetailList) {
            this.data.data.orderDetailList[key].totalPrice = utilHelper.toFixed(this.data.data.orderDetailList[key].price * this.data.data.orderDetailList[key].number, 2);
            this.data.data.packingCharges = utilHelper.toFixed(this.data.data.packingCharges + (this.data.data.orderDetailList[key].packingCharges * this.data.data.orderDetailList[key].number));
          }
        }
        let reducedDeliveryPrice = 0;
        console.log("获取商家配送费=====" + this.data.reducedDeliveryPrice);
        if (this.data.reducedDeliveryPrice >= feeData) {
          reducedDeliveryPrice = 0;
        } else {
          reducedDeliveryPrice = feeData - this.data.reducedDeliveryPrice;
        }
        console.log("获取用户地址配送费====" + feeData)
        console.log("获取根据商家的配送费得出最终配送费====" + reducedDeliveryPrice);
        console.log("获取优惠券后的价格======" + this.data.data.fullPriceReduction);
        let isReducedDeliveryPrice = false,
          isThereADiscount;
        if (feeData != reducedDeliveryPrice) {
          isReducedDeliveryPrice = true
          isThereADiscount = true;
        }
        this.data.data.actualPrice = this.data.data.actualPrice ? utilHelper.toFixed((Number(this.data.data.actualPrice)), 2) : 0;
        this.data.data.fullPriceReductionAfter = this.data.data.actualPrice;
        this.data.data.discountPrice = 0;
        this.data.data.fullPriceReductionBefore = (this.data.data.fullPriceReduction) > 0 ? utilHelper.toFixed((this.data.data.fullPriceReduction - feeData), 2) : 0;
        console.log("获取减去配送费优惠价格1======" + this.data.data.fullPriceReductionBefore);
        this.data.data.fullPriceReductionIsHidden = this.data.data.fullPriceReductionIsHidden;
        this.data.data.limitedPrice = 0;
        this.data.data.fullPriceReduction = this.data.data.fullPriceReductionBefore;
        for (let i = 0; i < result.data.records.length; i++) {
          //总价格减去配送费大于满减金额则进行满减操作
          if (Number(this.data.data.fullPriceReductionBefore) >= result.data.records[i].limitedPrice) {
            //判断当前满减价格limitedPrice和上一个满减价格对比，如果大于就进行认证
            if (result.data.records[i].limitedPrice > this.data.data.limitedPrice) {
              this.data.data.limitedPrice = result.data.records[i].limitedPrice;
              this.data.data.fullPriceReduction = utilHelper.toFixed((this.data.data.fullPriceReductionBefore - result.data.records[i].reducedPrice), 2);
              this.data.data.fullReductionRuleName = result.data.records[i].name;
              this.data.data.fullReductionRuleId = result.data.records[i].id;
              this.data.data.fullPriceReductionIsHidden = true;
              this.data.data.reducedPrice = result.data.records[i].reducedPrice;
              this.data.data.fullPriceReductionAfter = utilHelper.toFixed((this.data.data.fullPriceReductionBefore - result.data.records[i].reducedPrice), 2);
            }
          }
        }
        //console.log(reducedDeliveryPrice)
        //console.log(this.data.data.fullPriceReduction - feeData);
        if (app.deliveryAndSelfTaking.currentTab == 0) {
          this.data.data.fullPriceReduction = this.data.data.fullPriceReduction + reducedDeliveryPrice;
        }
        console.log("获取最终满减后的价格" + this.data.data.fullPriceReduction)
        this.setData({
          data: this.data.data,
          "deliveryAndSelfTaking.isReducedDeliveryPrice": isReducedDeliveryPrice,
          "deliveryAndSelfTaking.reducedDeliveryTotalPrice": feeData,
          "deliveryAndSelfTaking.feeData": reducedDeliveryPrice,
          "deliveryAndSelfTaking.isThereADiscount": isThereADiscount
        });
        // this.selectCommissionReward();
      }
    })
  },
  //获取满减规则
  getFullReductionRuleMode(data) {
    toastService.showLoading();
    https.request('/api-promotion/rest/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1,
      shopId: data.shopId
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        let feeData = data.feeData;
        let reducedDeliveryTotalPrice = feeData;
        let isReducedDeliveryPrice = false;
        let actualPrice = this.data.data.actualPrice;
        let fullPriceReductionBefore = actualPrice - feeData;
        //data.actualPrice=this.data.data.actualPrice;
        console.log("总商品金额" + actualPrice);
        console.log("总商品金额fullPriceReductionBefore" + fullPriceReductionBefore)
        let couponPrice = this.data.afterDiscount ? this.data.afterDiscount.price : 0;
        let couponAfter = 0;
        let reducedDeliveryPrice = this.data.reducedDeliveryPrice;
        console.log("用户地址配送费======" + feeData)
        console.log("商家配送费======" + reducedDeliveryPrice);
        let reducedPrice = 0;
        if (reducedDeliveryPrice >= data.feeData) {
          couponAfter = utilHelper.toFixed((fullPriceReductionBefore - couponPrice), 2);
          reducedPrice = 0;
        } else {
          reducedPrice = feeData - reducedDeliveryPrice;
          couponAfter = utilHelper.toFixed((fullPriceReductionBefore - couponPrice), 2);
        }
        if (reducedDeliveryTotalPrice != reducedPrice) {
          isReducedDeliveryPrice = true
        }
        //actualPrice = actualPrice-reducedPrice;
        //couponAfter=actualPrice-reducedPrice;
        console.log("计算商家后的配送费======" + reducedPrice)
        console.log("总商品金额+上了配送费===" + actualPrice)
        console.log("优惠券金额=======" + couponPrice)
        console.log("计算后的价格=======" + couponAfter)
        data.limitedPrice = 0;
        data.fullPriceReduction = couponAfter;
        for (let i = 0; i < result.data.records.length; i++) {
          //console.log(result.data.records[i].limitedPrice)
          //总价格减去配送费大于满减金额则进行满减操作
          if (Number(couponAfter) >= result.data.records[i].limitedPrice) {
            //判断当前满减价格limitedPrice和上一个满减价格对比，如果大于就进行认证
            if (result.data.records[i].limitedPrice > data.limitedPrice) {
              data.limitedPrice = result.data.records[i].limitedPrice;
              data.fullPriceReduction = utilHelper.toFixed((couponAfter - result.data.records[i].reducedPrice), 2);
              data.fullReductionRuleName = result.data.records[i].name;
              data.fullReductionRuleId = result.data.records[i].id;
              data.fullPriceReductionIsHidden = true;
              data.reducedPrice = result.data.records[i].reducedPrice;
              data.fullPriceReductionAfter = utilHelper.toFixed((this.data.data.fullPriceReduction - result.data.records[i].reducedPrice), 2);
            }
          }
        }
        console.log("获取最终配送费=========" + reducedPrice)
        console.log("获取最终价格=========" + data.fullPriceReduction)
        data.fullPriceReduction = data.fullPriceReduction + reducedPrice;
        app.deliveryAndSelfTaking.feeData = reducedPrice;
        app.deliveryAndSelfTaking.isThereADiscount = false;
        if (reducedDeliveryTotalPrice != reducedPrice) {
          app.deliveryAndSelfTaking.isThereADiscount = true;
        }
        console.log("获取最终价格再加上配送费=========" + data.fullPriceReduction);
        console.log("获取最终价格=========" + data.actualPrice);
        this.setData({
          data,
          "deliveryAndSelfTaking.reducedDeliveryTotalPrice": reducedDeliveryTotalPrice,
          "deliveryAndSelfTaking.isThereADiscount": app.deliveryAndSelfTaking.isThereADiscount,
          "deliveryAndSelfTaking.isReducedDeliveryPrice": isReducedDeliveryPrice,
          "deliveryAndSelfTaking.feeData": reducedPrice
        });
      }
    })
  },
  getPsf() {
    let isReducedDeliveryPrice = false;
    let feeData = app.deliveryAndSelfTaking.reducedDeliveryTotalPrice;
    let reducedDeliveryTotalPrice = app.deliveryAndSelfTaking.reducedDeliveryTotalPrice;
    console.log("用户地址配送费" + reducedDeliveryTotalPrice)
    if (this.data.reducedDeliveryPrice >= reducedDeliveryTotalPrice) {
      feeData = 0;
    } else {
      feeData = feeData - this.data.reducedDeliveryPrice;
    }
    console.log("用户最终支付的配送费" + feeData)
    if (reducedDeliveryTotalPrice != feeData) {
      isReducedDeliveryPrice = true
    }
    this.setData({
      "deliveryAndSelfTaking.reducedDeliveryTotalPric": reducedDeliveryTotalPrice,
      "deliveryAndSelfTaking.isReducedDeliveryPrice": isReducedDeliveryPrice,
      "deliveryAndSelfTaking.feeData": feeData
    })
  },
  goToRecharge(e) {
    this.setData({
      isVipDialogShow: false
    })
    wx.navigateTo({
      url: '/pages/mine/member/recharge/recharge',
    })
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})