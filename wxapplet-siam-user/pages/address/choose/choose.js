import https from '../../../utils/http';
import dateHelper from '../../../utils/date-helper';
import toastService from '../../../utils/toast.service';
import utilsHelper from '../../../utils/util';
import authService from '../../../utils/auth';
//获取应用实例
const app = getApp();
var heightList = [],
  pageNo = 1, shopPageNo = 1,
  pageSize = 20, shopPageSize = 20;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    addressList: [],
    shopAddressList: [],
    modeList: [{
      modeName: "外卖配送",
      modeId: 0
    }],
    isInsert: false
  },
  // 滑动切换tab
  bindSlideChange: function (e) {
    var that = this;
    that.setData({
      currentTab: e.detail.current
    });
    //自适应手机高度
    that.selfAdaption();
  },
  //点击切换 
  clickTab: function (e) {
    var that = this;
    if (that.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      });
      //自适应手机高度
      that.selfAdaption();
    }
  },
  getHasShopDelivery(deliveryAddressId) {
    return new Promise((fulfil, reject) => {
      https.request('/common/selectHasShopDelivery', {
        deliveryAddressId: deliveryAddressId
      }).then(result => {
        if (result.success) {
          fulfil(result.data);
        }
      })
    })
  },
  // 订单详情我要配送方法
  confirmChoiceDelivery(e) {
    var key = e.currentTarget.dataset.index;
    //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
    var shopAddress = this.data.addressList[key];

    // this.getHasShopDelivery(shopAddress.id).then(result => {
    //   if (!result) {
    //     toastService.showToast("当前暂时没有门店可以进行配送");
    //     return
    //   }
    https.request('/api-order/rest/common/selectDeliveryFeee', {
      deliveryAddressId: shopAddress.id,
      shopId: this.data.shopId
    }).then(result => {
      if (result.success) {
        var that = this;
        var pages = getCurrentPages();
        var prevPage = pages[pages.length - 2]; //上一个页面
        var prevToPage = pages[pages.length - 3]; //上一个页面
        var payPrice=result.data-Number(this.data.reducedDeliveryPrice);
        if(payPrice>0){
          toastService.showModal("", "确认支付" + payPrice + "元配送费吗？", function () {
            authService.getOpenId().then(openId => {
              //console.log(that.data.orderId)
              if (openId) {
                toastService.showLoading();
                https.request('/api-order/rest/member/wxPay/toPay4Applet', {
                  openid: openId,
                  type: 3,
                  out_trade_no: that.data.orderNo,
                  total_fee: payPrice,
                  deliveryAddressId: shopAddress.id,
                  shopId: that.data.shopId
                }).then(result => {
                  toastService.hideLoading();
                  if (result.success) {
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
                          prevPage.getOrderDetail(that.data.orderId);
                          prevToPage.setData({
                            isOperation: true
                          })
                          clearTimeout(timeout);
                          wx.navigateBack(1);
                        }, 1000);
                      },
                      fail(res) {
                        toastService.showError("支付失败", true);
                      }
                    })
                  }
                })
                return
              }
              toastService.showToast("登录用户错误，请重新登录");
            });
          }, null);
        }
      }
    })
    // })
  },
  //选择用户地址
  confirmChoice(e) {
    //console.log(e)
    var key = e.currentTarget.dataset.index;
    //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
    var shopAddress = this.data.addressList[key];
    // this.getHasShopDelivery(shopAddress.id).then(result => {
    //   if (!result) {
    //     toastService.showToast("当前暂时没有门店可以进行配送");
    //     return
    //   }
    //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
    for (let index in app.deliveryAndSelfTaking.modeList) {
      app.deliveryAndSelfTaking.modeList[index].checked = false;
      if (index == this.data.currentTab) {
        app.deliveryAndSelfTaking.modeList[index].checked = true;
      }
    }
    // console.log(app.deliveryAndSelfTaking)
    if (this.data.pageType == 'pay') {
      let feeData = app.deliveryAndSelfTaking.reducedDeliveryTotalPrice ? app.deliveryAndSelfTaking.reducedDeliveryTotalPrice : 0;
      let pages = getCurrentPages();
      let prevPage = pages[pages.length - 2]; //上一个页面
      console.log(prevPage)
      //如果重复选择地址则不进行任何操作
      if (this.data.chooseId == shopAddress.id) {
        return
      }
      //选择地址和更换地址进行配送费的加减操作
      https.request('/api-order/rest/common/selectDeliveryFee', {
        deliveryAddressId: shopAddress.id,
        shopId: this.data.shopId
      }).then(result => {
        if (result.success) {
          //如果选择了不同的地址就进行减去上一次选择的地址的配送费，加上本次选择的地址的配送费（总价一起操作）
          //prevPage.data.data.actualPrice = prevPage.data.data.actualPrice - (feeData) + result.data;
          let priceFee=0;
          console.log("获取配送金额====>"+feeData);
          console.log("获取选择的配送金额====>"+result.data);
          if(feeData>result.data){
            priceFee=feeData-result.data;
            prevPage.data.data.actualPrice=prevPage.data.data.actualPrice-priceFee;
          }else{
            priceFee=result.data-feeData;
            prevPage.data.data.actualPrice=prevPage.data.data.actualPrice+priceFee;
          }
          
          prevPage.data.data.fullPriceReduction = utilsHelper.toFixed(prevPage.data.data.fullPriceReduction - (feeData) + result.data, 2);
          prevPage.data.data.feeData = result.data;
          app.deliveryAndSelfTaking.radioIndex = this.data.currentTab;
          app.deliveryAndSelfTaking.deliveryAddress = shopAddress;
          app.deliveryAndSelfTaking.feeData = result.data;
          app.deliveryAndSelfTaking.reducedDeliveryTotalPrice = result.data;
          app.deliveryAndSelfTaking.isThereADiscount = false;
          if (app.deliveryAndSelfTaking.feeData != result.data) {
            app.deliveryAndSelfTaking.isThereADiscount = true;
          }
          let time = dateHelper.formatTime("hms");
          console.log("用户地址配送费==========" + app.deliveryAndSelfTaking.feeData)
          console.log("最终用户需支付的配送费==========" + feeData)
          //prevPage.getShopAddressList();
          // //刷新获取满减
          prevPage.getFullReductionRuleMode(prevPage.data.data);
          console.log(prevPage.data)
          // //刷新获取新人优惠券
          //prevPage.getCouponsMemberRelation();
          prevPage.setData({
            deliveryAndSelfTaking: app.deliveryAndSelfTaking,
            shopAddress: this.data.shopAddress,
            data: prevPage.data.data,
            oldAddressId: this.data.addressList[key].id,
            time: time,
            isPreviousPage: true,
            isJs: true
          })
          this.setData({
            itemId: this.data.addressList[key].id
          })
          wx.navigateBack(1);
          return
        }
        app.deliveryAndSelfTaking.feeData = 0;
        prevPage.setData({ deliveryAndSelfTaking: app.deliveryAndSelfTaking })
      });
      return
    }

    //选择地址和更换地址进行配送费的加减操作
    https.request('/api-order/rest/common/selectDeliveryFee', {
      deliveryAddressId: shopAddress.id,
      shopId: this.data.shopId
    }).then(result => {
      if (result.success) {
        app.deliveryAndSelfTaking.radioIndex = this.data.currentTab;
        app.deliveryAndSelfTaking.deliveryAddress = shopAddress;
        app.deliveryAndSelfTaking.feeData = result.data;
        prevPage.setData({ deliveryAndSelfTaking: app.deliveryAndSelfTaking })
        wx.navigateBack(1);
      }
    });
    // })
  },
  editAddressTap(e) {
    wx.navigateTo({
      url: "../edit/edit?detail=" + JSON.stringify(e.currentTarget.dataset.data),
    })
    this.setData({
      refreshKey: e.currentTarget.dataset.key
    })
  },
  selfAdaption() {
    var that = this;
    let winHeight = 0;
    let height = 0;
    wx.getSystemInfo({
      success: function (res) {
        //获取到用户的手机信息
        winHeight = res.windowHeight;
        // 获取需要减去的dom结构的高度信息
        wx.createSelectorQuery().selectAll('.self-adaption').boundingClientRect(function (rects) {
          rects.forEach(function (rect, index) {
            height = height + rect.height;
          })
          that.setData({
            winHeight: winHeight - height
          })
        }).exec()
      }
    });
  },
  getShopAddressList() {
    https.request('/api-merchant/rest/shop/detail', {
      id: this.data.shopId,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      if (result.success) {
        this.setData({
          shopAddress: result.data
        })
      }
    })
  },
  getDeliveryAddressList() {
    https.request('/api-user/rest/member/deliveryAddress/list', {
      pageNo: -1,
      pageSize: 10
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        var list = this.data.addressList;
        result.data.records.forEach(key => {
          // if(key.isDefault){
          //   this.deliveryAndSelfTaking.deliveryAddress = key;
          // }
          list.push(key);
        })
        this.setData({
          addressList: list
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    let deliveryAndSelfTaking = JSON.parse(options.deliveryData)
    console.log(deliveryAndSelfTaking)
    this.setData({
      reducedDeliveryPrice: options.reducedDeliveryPrice,
      currentTab: options.radioIndex,
      shopId: options.shopId,
      pageType: options.pageType,
      chooseId: deliveryAndSelfTaking.deliveryAddress ? deliveryAndSelfTaking.deliveryAddress.id : '',
      orderNo: options.pageType == "orderDetail" ? options.orderNo : '',
      orderId: options.pageType == "orderDetail" ? options.orderId : ''
    })
    this.getDeliveryAddressList();
    this.getShopAddressList();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () { //自适应手机高度
    this.selfAdaption();
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
    // // 显示加载图标
    // toastService.showLoading('正在加载...');
    // if (this.data.currentTab == 1) {
    //   // 页数+1
    //   pageNo = pageNo + 1;

    //   this.getDeliveryAddressList();
    //   return
    // }
    // shopPageNo = shopPageNo + 1;
    // this.getShopAddressList();
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})