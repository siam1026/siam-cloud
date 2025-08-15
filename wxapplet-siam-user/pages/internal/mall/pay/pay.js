import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import systemStatus from '../../../../utils/system-status';
import dateHelper from '../../../../utils/date-helper';
import utilHelper from '../../../../utils/util';
import base64 from '../../../../utils/base64';
const app = getApp(); 
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderToken: '',
    time: '10:00',
    isChoose: false, //是否选择派送方式
    isFirstShop: false, //是否选择的是门店
    isFirstAddress: false,
    inputLength: 0,
    deliveryData: {},
    title: "请选择支付方式",
    paymentModes: [{
      checked: false,
      value: 1,
      text: '微信支付',
      show: true,
      icon: 'iconwechat_pay'
    }, {
      checked: true,
      value: 2,
      text: '平台积分',
      show: true,
      icon: 'iconjifen'
    }],
    paymentModeIndex: 1,
    dialogShow: false,
    addressDialog: false,
    remarks: "",
    isVipDialogShow:false
  },
  getTimestamp(){
    var timestamp=dateHelper.getTimestamp();
    console.log(timestamp)
    this.setData({
      timestamp:timestamp
    })
  },
  openAddressDialog(e) {
    this.getDeliveryAddressList();
    this.setData({
      addressDialog: true
    })
  },
  getDeliveryAddressList() {
    https.request('/api-user/rest/member/deliveryAddress/list', {
      pageNo: -1,
      pageSize: 10
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        this.setData({
          addressList: result.data.records
        })
      }
    })
  },
  radioChangeAddress(e) {
    console.log(e)
    this.setData({
      addrIndex: e.detail.value
    })
  },
  choosePayModeTap() {
    let paymentModes = this.data.paymentModes;
    let actualPrice = !this.data.data.fullPriceReductionIsHidden && !this.data.data.couponsIsHidden && !this.data.deliveryAndSelfTaking.isThereADiscount ? this.data.data.actualPrice : this.data.data.fullPriceReduction;
    console.log("积分余额=", this.data.userInfo.points);
    console.log("平台余额=", this.data.userInfo.balance)
    console.log("需支付金额=", actualPrice)
    paymentModes[1].desc = "";
    paymentModes[1].isBindTap = true;
    if (actualPrice > this.data.userInfo.points) {
      console.log("积分不足")
      paymentModes[1].desc = "积分不足";
      paymentModes[1].isBindTap = false;
    }
    if (!this.data.userInfo.paymentPassword) {
      paymentModes[1].desc = "未设置支付密码,去设置";
      paymentModes[1].isBindTap = false;
    }
    // this.setData({
    //   dialogShow: true,
    //   maskClosable: false,
    //   title: "请选择支付方式",
    //   paymentModes: paymentModes
    // })
    toastService.hideLoading();
    console.log(paymentModes)
    this.setData({
      paymentModes: paymentModes
    })
  },
  close(){
    this.setData({
      isVipDialogShow: false
    })
    toastService.showLoading();
    var _this=this;
    https.request('/api-mall/rest/member/pointsMall/order/insert', _this.data.isPayJson).then(result => {
      toastService.hideLoading();
      if (result.success) {
        // toastService.showLoading("正在支付...", true);
        //console.log(result.data)
        _this.toPay4Applet(result.data.id, result.data.orderNo, result.data.actualPrice);
      }
    })
  },
  goToRecharge(e){
    this.setData({
      isVipDialogShow:false
    })
    wx.navigateTo({
      url: '/pages/mine/member/recharge/recharge',
    })
  },
  closeDialog: function () {
    this.setData({
      dialogShow: false
    })
  },
  radioChangePayment(e) {
    console.log(e)
    var that = this;
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
      
      for(let key in paymentModes){
        paymentModes[key].checked=false;
      }
      paymentModes[e.detail.value].checked=true;
      that.setData({
        paymentModes:paymentModes,
        paymentModeIndex:e.detail.value
      });
    });
  },
  confirmChooseAddress(e) {
    console.log(e)
    app.deliveryAndSelfTaking.deliveryAddress = this.data.addressList[this.data.addrIndex];
    this.setData({
      addressDialog: false,
      "deliveryAndSelfTaking.deliveryAddress": this.data.addressList[this.data.addrIndex]
    })
  },
  insertAddress() {
    wx.navigateTo({
      url: '/pages/address/insert/insert',
    })
  },
  editAddress(e) {
    wx.navigateTo({
      url: "/pages/address/edit/edit?detail=" + JSON.stringify(e.currentTarget.dataset.data),
    })
    this.setData({
      refreshKey: e.currentTarget.dataset.key
    })
  },
  remarksInput(e) {
    this.setData({
      inputLength: e.detail.value.length,
      remarks: e.detail.value
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
        setTimeout(time=>{
          this.choosePayModeTap();
          clearTimeout(time);
        },1000);
      }
    })
  },
  createOrderToken: function (e) {
    toastService.showLoading();
    https.request('/api-mall/rest/member/pointsMall/order/createOrderToken', {}).then(result => {
      if (result.success) {
          this.data.orderToken = result.data;
      }
    })
  },  
  //this.getFullReductionRule(data);
  getCouponsMemberRelation(prePageData) {
    console.log(prePageData)
    toastService.showLoading();
    https.request('/api-mall/rest/member/pointsMall/couponsMemberRelation/list', {
      pageNo: -1,
      pageSize: 20,
      isUsed: 0,
      isExpired: 0,
      isValid: 0,
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        let afterDiscounts = [];
        var availableCouponSize = 0;
        result.data.records.forEach(res => {
          //判断优惠券是否过期和是否已经使用
          //if (!res.couponsMemberRelationMap.isExpired && !res.couponsMemberRelationMap.isUsed) {
          //console.log(this.data)
          //判断是否已经满减，如果已经满减就取满减之后的字段值，反之取总额
          //if (this.data.data.actualPrice && !this.data.data.fullPriceReduction) {

          //判断当前优惠券是折扣还是满减券,1等于折扣,2等于满减
          if (res.couponsMemberRelationMap.preferentialType == 1) {
            availableCouponSize++;
            //遍历当前订单的商品
            prePageData.orderDetailList.forEach(order => {
              console.log(order)
              console.log(order.price)
              //遍历当前优惠券绑定的优惠商品
              res.goodsList.forEach(goods => {
                console.log(order.price)
                //console.log(goods)
                //console.log(order)
                //判断当前订单的商品是否等于优惠券绑定的优惠商品
                //等于则进行优惠
                if (order.goodsId == goods.id) {
                  // console.log(order.price)
                  // console.log(order.number)
                  // console.log(res.couponsMemberRelationMap.discountAmount != 0 ? utilHelper.toFixed((order.price / order.number) - ((order.price / order.number) * res.couponsMemberRelationMap.discountAmount), 2) : (order.price / order.number))
                  afterDiscounts.push({
                    id: res.couponsMemberRelationMap.id,
                    price: res.couponsMemberRelationMap.discountAmount != 0 ? utilHelper.toFixed((order.price) - (order.price) * res.couponsMemberRelationMap.discountAmount, 2) : (order.price),
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
            });
            return
          }
          //判断如果是优惠券满减的话这就进行优惠券的总价满减
          //console.log(res)
          if (res.couponsMemberRelationMap.preferentialType == 2) {
            if (prePageData.fullPriceReduction) {
              if (prePageData.fullPriceReduction >= res.couponsMemberRelationMap.limitedPrice) {
                //console.log(res.couponsMemberRelationMap.limitedPrice)
                afterDiscounts.push({
                  id: res.couponsMemberRelationMap.id,
                  couponsId: res.couponsMemberRelationMap.couponsId,
                  price: utilHelper.toFixed(prePageData.fullPriceReduction, 2) - res.couponsMemberRelationMap.reducedPrice,
                  couponsName: res.couponsMemberRelationMap.couponsName,
                  preferentialType: systemStatus.preferentialTypeText(res.couponsMemberRelationMap.preferentialType),
                  isExpired: res.couponsMemberRelationMap.isExpired,
                  isUsed: res.couponsMemberRelationMap.isUsed,
                  isValid: res.couponsMemberRelationMap.isValid
                });
              }
              return
            }
            if (prePageData.actualPrice >= res.couponsMemberRelationMap.limitedPrice) {
              afterDiscounts.push({
                id: res.couponsMemberRelationMap.id,
                couponsId: res.couponsMemberRelationMap.couponsId,
                price: prePageData.actualPrice - res.couponsMemberRelationMap.reducedPrice,
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
        prePageData.couponsIsHidden = false;
        prePageData.actualFullPriceReduction = prePageData.actualPrice;
        console.log(prePageData.actualPrice)
        if (afterDiscounts.length > 0) {
          var afterDiscountPrice = 0,
            price = 0,
            afterDiscountList;
          //遍历所有可以使用优惠券的商品，并计算出最大的优惠券
          afterDiscounts.forEach(afterDiscount => {
            price = Number(afterDiscount.price);
            if (Number(price) >= afterDiscountPrice) {
              afterDiscountPrice = price;
              afterDiscountList = afterDiscount;
            }
          });
          //console.log(this.data.data.fullPriceReduction)
          //console.log(afterDiscountPrice)
          //let fullPriceReduction = prePageData.fullPriceReduction ? (prePageData.fullPriceReduction - afterDiscountPrice) : (prePageData.actualPrice - afterDiscountPrice)
          //console.log(prePageData.packingCharges)
          //获取总金额（加配送费）减去优惠券的最后优惠价格
          let fullPriceReduction = (Number(prePageData.actualPrice) + Number(prePageData.packingCharges)) - afterDiscountPrice;
          prePageData.couponsIsHidden = true; //设置使用优惠券
          prePageData.fullPriceReduction = utilHelper.toFixed(fullPriceReduction, 2);
          //设置使用优惠券后价格属性，满减会使用到
          prePageData.actualFullPriceReduction = prePageData.fullPriceReduction;
          // prePageData.actualPrice=fullPriceReduction;
          console.log(prePageData.actualFullPriceReduction)
          this.setData({
            afterDiscount: afterDiscountList,
            availableCouponSize: availableCouponSize,
            "data.fullPriceReduction": utilHelper.toFixed(fullPriceReduction <= 0 ? 0 : fullPriceReduction, 2),
            "data.couponsIsHidden": true,
            // "data.afterDiscount":afterDiscountPrices
          });
        }
        //调用满减优惠券方法（先试用优惠券再满减）
        this.getFullReductionRule(prePageData);
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
    afterDiscount.limitedPrice = this.data.data.limitedPrice;
    afterDiscount.fullReductionRuleName = this.data.data.fullReductionRuleName;
    afterDiscount.fullReductionRuleId = this.data.data.fullReductionRuleId;
    afterDiscount.fullPriceReductionIsHidden = this.data.data.fullPriceReductionIsHidden;
    // afterDiscount.packingCharges = this.data.data.packingCharges;
    afterDiscount.type = 2;
    //console.log(afterDiscount)
    wx.navigateTo({
      url: '../../mine/coupons/coupons?prevData=' + JSON.stringify(afterDiscount),
    })
  },
  selectCommissionReward(){
    https.request('/api-mall/rest/member/pointsMall/order/selectCommissionReward',{
      actualPrice:!this.data.data.fullPriceReductionIsHidden&&!this.data.data.couponsIsHidden?this.data.data.actualPrice:this.data.data.fullPriceReduction
    }).then(result => {
      if (result.success) {
        this.setData({
          tipReward:result.data
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
    console.log(app.deliveryAndSelfTaking)
    this.setData({
      deliveryAndSelfTaking: app.deliveryAndSelfTaking
    });
    
    var prePageData = JSON.parse(options.orderDetail);
    prePageData.actualPrice = prePageData.actualPrice + 0;
    let time = dateHelper.formatTime("hms");
    //this.getFullReductionRule(data);
    this.getCouponsMemberRelation(prePageData);
    
    this.setData({ 
      payType: options.payType,
      time: time,
      initData: prePageData
    });
    
    // this.getShopAddressList();
    // setTimeout(out=>{
    //   this.getCouponsMemberRelation();
    // },1000)

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
    
    this.setData(app.deliveryAndSelfTaking);
    this.getLoginMemberInfo();
    this.createOrderToken();
    setTimeout((time)=>{
      this.selectCommissionReward();
      clearTimeout(time);
    },1500);
    this.getTimestamp();
  },
  /**
   * 获取焦点
   */
  getFocus: function () {
    this.setData({
      payFocus: true
    });
  },
  inputPwd: function (e) {
    this.setData({
      pwdVal: e.detail.value
    });
    if (e.detail.value.length >= 6) {
      toastService.showLoading("正在加载...");
      this.pointsPay();
      // if(this.data.paymentModes[this.data.paymentModeIndex].value==1){
      //   this.balancePay();
      // }
      // if(this.data.paymentModes[this.data.paymentModeIndex].value==2){
      //   this.pointsPay();
      // }
    }
  },
  balancePay() {
    var password = base64.encode(this.data.pwdVal);
    this.setData({
      pwdVal: '',
      payFocus: true
    });
    https.request('/api-order/rest/member/platformPay/byBalance', {
      openid: this.data.balanceOpenId,
      type: 4,
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
            url: '../../mall/order/detail/detail?id=' + this.data.balanceId,
          });
          clearTimeout(timeout);
        }, 1000);
      }
    })
  },
  pointsPay() {
    var password = base64.encode(this.data.pwdVal);
    this.setData({
      pwdVal: '',
      payFocus: true
    });
    https.request('/api-mall/rest/member/pointsMall/platformPay/byPoints', {
      openid: this.data.balanceOpenId,
      type: 4,
      paymentMode: 3,
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
            url: '../../mall/order/detail/detail?id=' + this.data.balanceId,
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
        url: '../../mall/order/detail/detail?id=' + this.data.balanceId,
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
  weChatPayTap() {
    //判断店铺是否打烊
    var data = {};
    if (this.data.deliveryAndSelfTaking.deliveryAddress == null) {
      toastService.showToast("请选择配送地址");
      return;
    }
    data.deliveryAddressId = this.data.deliveryAndSelfTaking.deliveryAddress.id;
    data.deliveryFee = this.data.deliveryAndSelfTaking.feeData;

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
    data.orderDetailList = orderDetailList;
    data.actualPrice = this.data.data.fullPriceReductionIsHidden || this.data.data.couponsIsHidden ? this.data.data.fullPriceReduction : this.data.data.actualPrice;
    // data.packingCharges = this.data.data.packingCharges;
    data.remark = this.data.remarks;
    data.shoppingWay = Number(this.data.radioIndex + 1);
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
    let addressMode = "配送地址：" + this.data.deliveryAndSelfTaking.deliveryAddress.province + this.data.deliveryAndSelfTaking.deliveryAddress.city + this.data.deliveryAndSelfTaking.deliveryAddress.area + this.data.deliveryAndSelfTaking.deliveryAddress.street;
    // toastService.showModal("积分商城订单", "订单确认后将无法更改!\r\n确认下单吗?", function comfirm() {

    //防重令牌
    data.orderToken = this.data.orderToken;

    if(this.data.userInfo.type==1){
      this.setData({
        isVipDialogShow:true,
        isPayJson:data
      });
    }else{
      toastService.showLoading();
      https.request('/api-mall/rest/member/pointsMall/order/insert', data).then(result => {
        toastService.hideLoading();
        if (result.success) {
          // toastService.showLoading("正在支付...", true);
          //console.log(result.data)
          that.toPay4Applet(result.data.id, result.data.orderNo, result.data.actualPrice);
        }
      })
    }
    // }, null)
  },
  toPay4Applet(id, orderNo, actualPrice) {
    toastService.showLoading("正在加载...", true);
    authService.getOpenId().then(openId => {
      //console.log(openId)
      // if (!openId) {
      //   toastService.showToast("登录用户错误，请重新登录");
      //   return
      // }
      openId=this.data.userInfo.openId;
      if (this.data.paymentModes[this.data.paymentModeIndex].value == 1) {
        this.weChatPay(id, orderNo, actualPrice, openId);
      }
      if (this.data.paymentModes[this.data.paymentModeIndex].value == 2 ||
        this.data.paymentModes[this.data.paymentModeIndex].value == 3) {
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
      type: 4,
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
                url: '../../mall/order/detail/detail?id=' + id,
              });
              clearTimeout(timeout);
            }, 1000)
          },
          fail(res) {
            toastService.showError("支付失败", true);
            let timeout = setTimeout(() => {
              wx.redirectTo({
                url: '../../mall/order/detail/detail?id=' + id,
              });
              clearTimeout(timeout);
            }, 1000)
          }
        })
      }
    })
  },
  // toPay4Applet(id, orderNo, actualPrice) {
  //   toastService.showLoading("正在加载...", true);
  //   authService.getOpenId().then(openId => {
  //     //console.log(openId)
  //     if (openId) {
  //       https.request('/api-order/rest/member/order/cancelOrder', {
  //         openid: openId,
  //         type: 1,
  //         out_trade_no: orderNo,
  //         total_fee: this.data.data.fullPriceReductionIsHidden ? this.data.data.fullPriceReduction : actualPrice
  //       }).then(result => {
  //         toastService.hideLoading();
  //         if (result.success) {
  //           //console.log(result)
  //           wx.requestPayment({
  //             appId: result.data.appid,
  //             timeStamp: result.data.timeStamp,
  //             nonceStr: result.data.nonceStr,
  //             package: result.data.package,
  //             signType: 'MD5',
  //             paySign: result.data.paySign,
  //             success(res) {
  //               toastService.showSuccess("支付成功", true);
  //               let timeout = setTimeout(() => {
  //                 wx.redirectTo({
  //                   url: '../order-details/order-details?id=' + id,
  //                 });
  //                 clearTimeout(timeout);
  //               }, 1000)
  //             },
  //             fail(res) {
  //               toastService.showError("支付失败", true);
  //               let timeout = setTimeout(() => {
  //                 wx.redirectTo({
  //                   url: '../order-details/order-details?id=' + id,
  //                 });
  //                 clearTimeout(timeout);
  //               }, 1000)
  //             }
  //           })
  //         }
  //       })
  //       return
  //     }
  //     toastService.showToast("登录用户错误，请重新登录");
  //   });
  // },
  //获取满减规则
  getFullReductionRule(data) {
    console.log(data)
    toastService.showLoading();
    https.request('/api-mall/rest/pointsMall/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        //this.getCouponsMemberRelation();
        //获取配送费，配送费不作为满减条件
        let feeData = 0;

        // if (!this.data.isPreviousPage) {
        //   data.packingCharges = 0;
        //   for (var key in data.orderDetailList) {
        //     //data.orderDetailList[key].price = data.orderDetailList[key].price * data.orderDetailList[key].number;
        //     data.packingCharges = data.packingCharges + (data.orderDetailList[key].packingCharges * data.orderDetailList[key].number);
        //   }
        // }

        data.actualPrice = data.actualPrice;
        data.fullPriceReductionAfter = data.actualPrice;
        data.discountPrice = 0;
        data.fullPriceReduction = data.fullPriceReduction;
        data.actualFullPriceReduction = data.actualFullPriceReduction;
        data.fullPriceReductionIsHidden = false;
        data.limitedPrice = 0;
        console.log(data.actualFullPriceReduction)
        for (let i = 0; i < result.data.records.length; i++) {
          //console.log(result.data.records[i].limitedPrice)
          //总价格减去配送费大于满减金额则进行满减操作（配送费不计算入内）
          if (Number(data.actualFullPriceReduction - feeData) >= result.data.records[i].limitedPrice) {
            //判断当前满减价格limitedPrice和上一个满减价格对比，如果大于就进行认证
            if (result.data.records[i].limitedPrice > data.limitedPrice) {
              data.limitedPrice = result.data.records[i].limitedPrice;
              data.fullPriceReduction = data.actualFullPriceReduction ? utilHelper.toFixed((data.actualFullPriceReduction - result.data.records[i].reducedPrice), 2) : data.actualFullPriceReduction;
              data.fullReductionRuleName = result.data.records[i].name;
              data.fullReductionRuleId = result.data.records[i].id;
              data.fullPriceReductionIsHidden = true;
              data.fullPriceReductionAfter = utilHelper.toFixed((data.actualFullPriceReduction - result.data.records[i].reducedPrice), 2);
            }
          }
        }
        this.setData({
          data
        })
      }
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