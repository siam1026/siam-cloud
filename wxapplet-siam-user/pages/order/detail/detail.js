import https from '../../../utils/http';
import authService from '../../../utils/auth';
import toastService from '../../../utils/toast.service';
import dateHelper from '../../../utils/date-helper';
import systemStatus from '../../../utils/system-status';
import base64 from '../../../utils/base64';
var id = '';
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    refundReasonRapidly: [{
        value: 1,
        name: "信息填写错误"
      }, {
        value: 2,
        name: "送达时间选错了"
      },
      {
        value: 3,
        name: "买错了/买少了"
      },
      {
        value: 4,
        name: "商家缺货"
      },
      {
        value: 5,
        name: "商家联系我取消"
      },
      {
        value: 6,
        name: "配送太慢"
      },
      {
        value: 7,
        name: "骑手联系我取消"
      },
      {
        value: 8,
        name: "我不想要了"
      }
    ],
    refundStatusTexts: [{
        value: 1,
        name: "退款申请已提交"
      },
      {
        value: 2,
        name: "等待商家处理"
      },
      {
        value: 3,
        name: "商家拒绝退款"
      },
      {
        value: 4,
        name: "等待平台处理"
      },
      {
        value: 5,
        name: "平台拒绝退款，退款已关闭"
      },
      {
        value: 6,
        name: "退款已关闭"
      },
      {
        value: 7,
        name: "退款成功"
      },
    ],
    choiceReasonDialog: false,
    choiceReasonApplyDialog: false,
    buttons: [{
      text: '取消'
    }, {
      text: '确定'
    }],
    showPayPwdInput: false, //是否展示密码输入层
    pwdVal: '', //输入的密码
    payFocus: false, //文本框焦点
    adjustPosition: false
  },
  getLoginMemberInfo: function (e) {
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        result.data.typeVipText = systemStatus.typeVipText(result.data.vipType);
        result.data.statusVipText = systemStatus.statusVipText(result.data.vipStatus);
        result.data.vipStartTime = dateHelper.formatDate(result.data.vipStartTime);
        result.data.vipEndTime = dateHelper.formatDate(result.data.vipEndTime);
        this.setData({
          userInfo: result.data
        })
      }
    })
  },
  deliveryAddressChange(e) {
    let deliveryData = {
      deliveryAddress: 0
    };
    let self = this;
    authService.checkIsLogin().then(result => {
      if (result) {
        //判断店铺是否打烊
        let startTime = this.data.shopInfo.shop.startTime;
        let endTime = this.data.shopInfo.shop.endTime;
        let isOperating = this.data.shopInfo.shop.isOperating;
        app.getIsBusiness(startTime, endTime, isOperating).then(result => {
          if (!result) {
            return
          }
          console.log(this.data)
          wx.navigateTo({
            url: '../../address/choose/choose?radioIndex=0&pageType=orderDetail&orderNo=' +
              this.data.order.orderNo + '&orderId=' + this.data.order.id + "&deliveryData=" + JSON.stringify(deliveryData) + "&shopId=" + this.data.order.shopId + "&reducedDeliveryPrice=" + this.data.shopInfo.shop.reducedDeliveryPrice,
          });
        });
        return
      }
      app.checkIsAuth("scope.userInfo");
    });
  },
  openConfirm() {
    let paymentModes = [{
      value: 1,
      text: '微信支付',
      icon: 'iconwechat_pay',
      show: true,
    }, {
      value: 2,
      text: '平台余额',
      icon: 'iconyue',
      show: true,
    }];
    console.log("余额=", this.data.userInfo.balance)
    console.log("需支付金额=", this.data.order.actualPrice)
    if (this.data.order.actualPrice > this.data.userInfo.balance) {
      paymentModes[1].desc = "余额不足";
      paymentModes[1].isBindTap = true;
    }
    if (!this.data.userInfo.paymentPassword) {
      paymentModes[1].desc = "未设置支付密码,去设置";
      paymentModes[1].isBindTap = false;
    }
    this.setData({
      dialogShow: true,
      maskClosable: false,
      title: "请选择支付方式",
      paymentModes: paymentModes
    })
  },
  closeDialog: function () {
    this.setData({
      dialogShow: false
    })
  },
  btnClick(e) {
    console.log(e);

    this.closeDialog();
  },
  paymentModeChange(e) {
    console.log(e)
    console.log(this.data.paymentModes[e.detail.value].name)
    this.setData({
      paymentMode: this.data.paymentModes[e.detail.value].mode
    })
  },
  goToPay(e) {
    var that = this;
    authService.getOpenId().then(openId => {
      console.log(openId)
      that.closeDialog();
      // if (!openId) {
      //   toastService.showToast("登录用户错误，请重新登录");
      //   return
      // }
      console.log(that.data.order);
      that.setData({
        paymentModeIndex: e.detail.index
      })
      if (e.detail.value == 1) {
        console.log("微信支付");
        toastService.showToast("暂不支持微信支付，请选择余额支付/积分支付");
        return
        //toastService.showLoading();
        //that.weChatPay(id, openId);
      }
      if (e.detail.value == 2) {
        console.log("余额支付")
        if (!that.data.userInfo.paymentPassword) {
          wx.navigateTo({
            url: '../../mine/security/index/index',
          });
          return
        }

        toastService.showLoading();
        that.setData({
          showPayPwdInput: true,
          payFocus: true
        });
        toastService.hideLoading();
        that.setData({
          balanceId: id,
          balanceOpenId: openId,
        })
      }
    }, null);
  },
  close(e) {
    console.log(e)
  },
  weChatPay(id, openId) {
    var that = this;
    let pages = getCurrentPages();
    let prevPage = pages[pages.length - 2]; //上一个页面
    https.request('/api-order/rest/member/wxPay/toPay4Applet', {
      openid: openId,
      type: 1,
      out_trade_no: this.data.order.orderNo,
      total_fee: this.data.order.actualPrice
    }).then(result => {
      if (result.success) {
        toastService.hideLoading();
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
              that.getOrderDetail(id);
              prevPage.getOrderDetail(id);
              clearTimeout(timeout);
            }, 1000);
          },
          fail(res) {
            toastService.showError("支付失败", true);
            let timeout = setTimeout(() => {
              that.getOrderDetail(id);
              clearTimeout(timeout);
            }, 1000);
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
    let pages = getCurrentPages();
    let prevPage = pages[pages.length - 2]; //上一个页面
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
      out_trade_no: this.data.order.orderNo,
      total_fee: this.data.order.actualPrice
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {

        toastService.showSuccess("支付成功", true);
        this.hidePwdLayer();
        let timeout = setTimeout(() => {
          this.getOrderDetail(this.data.balanceId);
          prevPage.getOrderDetail(this.data.balanceId);
          clearTimeout(timeout);
        }, 1000);
      }
    })
  },
  bindtouchend(e) {
    console.log("触摸结束")
    this.showPwdLayer();
  },
  balancePayFail() {
    this.hidePwdLayer();
    toastService.showError("支付失败", true);
    // let timeout = setTimeout(() => {

    //   this.getOrderDetail(this.data.balanceId);
    //   clearTimeout(timeout);
    // }, 1000);
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
    this.setData({
      isForgetThePassword: true
    })
    wx.navigateTo({
      url: "../../mine/security/index/index"
    })
  },
  cancelOrder() {
    let that = this;
    toastService.showModal(null, "确定取消这个订单吗?", function () {
      https.request('/api-order/rest/member/order/cancelOrder', {
        id: id
      }).then(result => {
        if (result.success) {
          toastService.showSuccess("取消成功", true);
          // var pages = getCurrentPages();
          // var prevPage = pages[pages.length - 2]; //上一个页面
          //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
          // prevPage.setData({
          //   isOperation: true
          // })
          let timeout = setTimeout(() => {
            //wx.navigateBack(1);
            that.getOrderDetail(id);
            clearTimeout(timeout);
          }, 1500)
        }
      })
    }, null);
  },
  isAllowCancelNoReason() {
    this.setData({
      choiceReasonDialog: this.data.choiceReasonDialog ? false : true
    });
  },
  applyRefund() {
    // this.setData({
    //   choiceReasonApplyDialog: this.data.choiceReasonApplyDialog ? false : true
    // });
    wx.navigateTo({
      url: '../refund/apply/apply?orderId=' + this.data.order.id,
    })
  },
  radioChange(e) {
    console.log(e)
    this.setData({
      cancelOrderNoReason: e.detail.value
    })
  },
  cancelOrderNoReason() {
    toastService.showLoading();
    this.setData({
      choiceReasonDialog: false
    })
    https.request('/api-order/rest/member/order/cancelOrderNoReason', {
      id: this.data.order.id,
      orderRefund:{
        refundReason: this.data.cancelOrderNoReason
      }
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        toastService.showSuccess("取消成功");
        this.getOrderDetail(this.data.order.id);
      }
    })
  },
  cancelOrderNoReasonApply() {
    toastService.showLoading();
    var orderRefundGoodsListStr = [];
    this.data.orderDetailList((item, index) => {
      orderRefundGoodsListStr.push({
        orderDetailId: item.id,
        number: item.number
      })
    })

    https.request('/api-order/rest/member/order/applyRefund', {
      id: this.data.order.id,
      orderRefundGoodsListStr: orderRefundGoodsListStr,
      orderRefund:{
        refundReason: this.data.cancelOrderNoReason
      }
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        toastService.showSuccess("操作成功");
        this.getOrderDetail(this.data.order.id);
      }
    })
  },
  bindRefundProcess() {
    wx.navigateTo({
      url: '../refund/progress/progress?orderId=' + this.data.order.id,
    })
  },
  getOrderDetail(id) {
    toastService.showLoading();
    https.request('/api-order/rest/member/order/selectById', {
      id: id
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        const status = result.data.order.status;
        const createTime = result.data.order.createTime;
        result.data.order.statusText = systemStatus.statusText(status);
        result.data.order.createTime = dateHelper.fmtDate(createTime);
        result.data.order.paymentModeText = systemStatus.paymentModeText(result.data.order.paymentMode);
        //解析订单商品的规格
        result.data.orderDetailList.forEach(orderDetailList => {
          let specListAnalysis = "";
          for (var key in JSON.parse(orderDetailList.specList)) {
            specListAnalysis = (specListAnalysis ? specListAnalysis + "/" : specListAnalysis) + JSON.parse(orderDetailList.specList)[key];
          }
          orderDetailList.specListAnalysis = specListAnalysis;
          //console.log(orderDetailList)
        })
        for (let i in this.data.refundStatusTexts) {
          if (result.data.order.refundStatus == this.data.refundStatusTexts[i].value) {
            result.data.order.refundStatusText = this.data.refundStatusTexts[i].name;
          }

        }
        this.setData({
          order: result.data.order,
          orderDetailList: result.data.orderDetailList,
        });
        this.getShopInfo(result.data.order.shopId);
      }
    })
  },
  contactBussinessTap() {
    wx.makePhoneCall({
      phoneNumber: this.data.order.shopContactPhone
    })
  },
  evaluateTip(e) {
    id = e.currentTarget.dataset.id;
    let shopId = e.currentTarget.dataset.shopid;
    wx.navigateTo({
      url: '../evaluate/evaluate?orderId=' + id + '&shopId=' + shopId,
    })
  },
  getShopInfo(shopId) {
    https.request('/api-goods/rest/shop/detail', {
      id: shopId,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      if (result.success && result.data) {
        this.setData({
          shopInfo: result.data
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    id = options.id;
    this.getOrderDetail(id);
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
    if (this.data.isForgetThePassword || this.data.showPayPwdInput) {
      console.log(this.data.paymentModes)
      console.log(this.data.paymentModeIndex)
      if (this.data.paymentModes[this.data.paymentModeIndex].value == 2) {
        this.showPwdLayer();
        this.setData({
          isForgetThePassword: false
        })
      }
    }
    this.getLoginMemberInfo();
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