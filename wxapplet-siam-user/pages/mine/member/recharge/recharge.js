import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import dateHelper from '../../../../utils/date-helper';
import systemStatus from '../../../../utils/system-status';
var pageNo = 1,pageSize = 20;
Page({
  /**
   * 页面的初始数据
   */
  data:{
    currentTab:0,
    modeList:[{
      modeId:0,
      modeName:"充值"
    },{
      modeId:1,
      modeName:"查余额"
    },{
      modeId:2,
      modeName:"我的记录"
    }],
    checkValue:0,
    list:[]
  },
  getHeight() {
    //获取用户手机系统信息
    var that = this;
    wx.getSystemInfo({
      success: function(res) {
        let winHeight = res.windowHeight; //获取高度
        //获取class为settlement-view并减去这个高度，自适应scrollview的高度
        const query = wx.createSelectorQuery()
        query.select('.bottom-view').boundingClientRect()
        query.selectViewport().scrollOffset()
        query.exec(function(res) {
          console.log(winHeight - res[0].height)
          that.setData({
            winHeight: winHeight - res[0].height - 160
          });
        })
      }
    });
  },
  getUserInfo: function (e) {
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        result.data.typeVipText = systemStatus.typeVipText(result.data.type);
        result.data.isVip = result.data.type == 1 ? false : true;
        this.setData({
          userInfo: result.data
        })
      }
    })
  },
  getVipRechargeDenominationList: function (e) {
    toastService.showLoading("正在加载...");
    https.request('/api-user/rest/vipRechargeDenomination/list', {
      pageNo: -1,
      pageSize: 20
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        console.log(result)
        result.data.records.forEach((item, index) => {
          item.checked = false;
          if(index==0){
            item.checked = true;
          }
          item.description=item.description.replace("↵","\n");
        });
        this.setData({
          list: result.data.records
        });
        this.getHeight();
      }
    })
  },
  radioChange(e) {
    console.log(e)
    let checkValue = e.detail.value;
    for (let value in this.data.list) {
      this.data.list[value].checked = false;
    }
    this.data.list[checkValue].checked = true;
    this.setData({
      list: this.data.list,
      checkValue:checkValue
    })
  },
  // 滑动切换tab
  bindSlideChange: function(e) {
    this.setData({
      currentTab: e.detail.current
    });
    // this.getHeight();
  },
  //点击切换 
  clickTab: function(e) {
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      this.setData({
        currentTab: e.target.dataset.current
      });
      // this.getHeight();
    }
  },
  bindPay(e){
    toastService.showLoading("正在加载...", true);
    var vipRechargeDenominationId=this.data.list[this.data.checkValue].id;
    var total_fee=this.data.list[this.data.checkValue].price;
    if(this.data.list[this.data.checkValue].isSale){
      total_fee=this.data.list[this.data.checkValue].salePrice;
    }
    var that=this;
    authService.getOpenId().then(openId => {
      if (openId) {
        https.request('/api-order/rest/member/wxPay/toPay4Applet', {
          openid: openId,
          type: 2,
          vipRechargeDenominationId: vipRechargeDenominationId,
          total_fee: total_fee
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
                // that.getUserInfo();
                wx.navigateBack(1);
                toastService.showSuccess("支付成功", true);
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
  },
  bindRechargeRecord(e){
    wx.navigateTo({
      url: '../record/record',
    })
  },
  bindMoneyBalance(){
    wx.navigateTo({
      url: '../../balance/index/index',
    })
  },
  bindIntegralBalance(){
    wx.navigateTo({
      url: '../../integral/integral',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getVipRechargeDenominationList();
    this.getUserInfo();
    
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