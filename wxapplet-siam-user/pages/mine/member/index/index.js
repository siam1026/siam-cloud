import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import dateHelper from '../../../../utils/date-helper';
import systemStatus from '../../../../utils/system-status';
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isVipImages:[],
    appVersion:app.globalData.appVersion
  },
  getTimestamp(){
    var timestamp=dateHelper.getTimestamp();
    console.log(timestamp)
    this.setData({
      timestamp:timestamp
    })
  },
  selectCurrent() {
    https.request('/api-util/rest/setting/selectCurrent', {}).then(result => {
      if (result.success) {
        result.data.vipRule = result.data.vipRule.replace("↵", "\n")
        this.setData({
          current: result.data
        })
      }
    })
  },
  bindInvite() {
    wx.navigateTo({
      url: `../../share/index/index?inviterId=${this.data.data.id}`,
    })
  },
  getUserInfo: function (e) {
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        result.data.typeVipText = systemStatus.typeVipText(result.data.type);
        if(this.data.isVipImages.length<=0){
          if(result.data.type==1){
            this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/user_1.png");
            this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/user_2.png");
            this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/user_3.png");
          }else{
            this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/vip_1.png");
            this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/vip_2.png");
            this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/vip_3.png");
          }
        }
        
        result.data.isVip = result.data.type == 1 ? false : true;
        this.setData({
          data: result.data,
          isVipImages:this.data.isVipImages
        })
      }
    })
  },
  bindReward() {
    wx.navigateTo({
      url: '../../../mine/share/reward/reward',
    })
  },
  bindOrders() {
    wx.navigateTo({
      url: '../record/record',
    })
  },
  bindBalance() {
    wx.navigateTo({
      url: '../../balance/index/index',
    })
  },
  bindCoupons() {
    wx.navigateTo({
      url: '../../../mine/coupons/coupons',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.selectCurrent();
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
    authService.checkIsLogin().then(result => {
      if (result) {
        this.getUserInfo();
        return;
      }
      this.setData({
        data: null,
        userInfo: null
      })
    })
  },
  bindVipRecharge() {
    wx.navigateTo({
      url: '../recharge/recharge',
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