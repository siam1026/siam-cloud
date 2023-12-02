import toastService from '../../../../utils/toast.service';
//获取应用实例
const app = getApp();
var inviterId;
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
    // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    console.log(app.globalData.userInfo)
    if (app.globalData.userInfo) {
      wx.redirectTo({
        url: '../choose/choose?inviterId='+inviterId
      })
    } else {
      wx.getUserProfile({
        desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
        success: (res) => {
          console.log(res)
          if (res.userInfo) {
            app.globalData.userInfo = res.userInfo;
            this.setData({
              userInfo: res.userInfo,
              hasUserInfo: true
            })
            wx.redirectTo({
              url: '../choose/choose?inviterId='+inviterId
            })
          }
        },fail(){
          app.globalData.userInfo = null;
          wx.redirectTo({
            url: '../choose/choose?inviterId='+inviterId
          })
        }
      })
    }
  },
  bindLoginTap(e) {
    //用户信息已授权
    toastService.showLoading();
    if (this.data.hasUserInfo) {
      //console.log(this.data)
      toastService.hideLoading();
      wx.redirectTo({
        url: '../choose/choose?inviterId='+inviterId
      })
    } else {//用户信息未授权
      toastService.hideLoading();
      if (e.detail.userInfo) {
        app.globalData.userInfo = e.detail.userInfo
        this.userInfo = e.detail.userInfo;
        this.hasUserInfo = true;
        wx.redirectTo({
          url: '../choose/choose?inviterId='+inviterId
        })
      } else {
        this.userInfo = null;
        this.hasUserInfo = false;
      }
      this.setData({
        userInfo: this.userInfo,
        hasUserInfo: this.hasUserInfo
      })
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if(options.inviterId){
      inviterId=options.inviterId;
    }
    console.log("{获取用户信息页面},options.inviterId = " + options.inviterId);
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