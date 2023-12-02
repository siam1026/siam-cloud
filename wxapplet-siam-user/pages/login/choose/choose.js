import toastService from '../../../utils/toast.service';
import https from '../../../utils/http';
import utils from '../../../utils/util';
//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    canIUseGetUserProfile:false
  },
  getPhoneNumber: function (e) {
    //是否授权过用户信息
    // if (e.detail.iv && e.detail.encryptedData) {
    //   app.getLoginCode().then(code => {
    //     toastService.showLoading("正在登录...", true);
    //     https.request("/api-user/rest/wxLogin/getWxPhone", {
    //       wxCode: code,
    //       wxIV: e.detail.iv,
    //       encryptedData: e.detail.encryptedData
    //     }).then(result => {
    //       toastService.hideLoading();
    //       if (result.success) {
    //         app.weChatLogin(result.data.phoneNumber, result.data.openid, this.data.inviterId);
    //       }
    //     })
    //   })
    //   //app.wxLogin(e.detail.iv, e.detail.encryptedData, app.globalData.code);
    //   //this.wxLogin(e.detail.iv, e.detail.encryptedData,app.globalData.code);
    //   return
    // }
    toastService.showToast("暂不支持微信登录，请选择验证码登录");
  },
  wxLogin(iv, encryptedData, code) {
    // session_key 已经失效，需要重新执行登录流程\
    // 检查登录态是否过期
    https.request("/api-user/rest/wxLogin/getWxPhone", {
      wxCode: code,
      wxIV: iv,
      encryptedData: encryptedData
    }).then(result => { 
      toastService.hideLoading();
      if (result.success) {
        toastService.showLoading("正在登录...", true);
        app.weChatLogin(result.data.phoneNumber, result.data.openid, this.data.inviterId);
      }
    })
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
    // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
      wx.getUserProfile({
        desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
        success: (res) => {
          console.log(res)
          if (res.userInfo) {
            app.globalData.userInfo = res.userInfo;
            this.setData({
              userInfo: res.userInfo,
              hasUserInfo: true,
              canIUseGetUserProfile:true
            });
          }
        },fail(res){
          console.log(res)
          app.globalData.userInfo = null;
        }
      })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options.scene);
    if(options.scene){
      options.inviterId=options.scene.split("%3D")[1];
    }
    console.log(options)
    //判断是否有邀请者id传递过来
    if(options.inviterId){
      this.setData({
        inviterId: options.inviterId
      })
    }
    console.log("options.inviterId = " + options.inviterId);
    
    //app.onLaunch(); 
    console.log(app.globalData.userInfo)
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true,
        canIUseGetUserProfile:true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      // app.checkIsAuth("scope.userInfo",{inviterId:options.inviterId});
      // wx.getUserInfo({
      //   success: res => {
      //     app.globalData.userInfo = res.userInfo;
      //     this.setData({
      //       userInfo: res.userInfo,
      //       hasUserInfo: true
      //     })
      //   }
      // })
    }
  },
  verificationCodeTap() {
    //如果邀请者id不为空，就携带到下一个跳转的页面
    if(this.data.inviterId){
      console.log("choose-log-in.js -> 邀请者不为空");
      wx.navigateTo({
        url: '../../login/code/code?inviterId='+this.data.inviterId,        
      })
    }else{
      console.log("choose-log-in.js -> 邀请者为空");
      wx.navigateTo({
        url: '../../login/code/code?inviterId',
      })      
    }
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
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        app.globalData.code = res.code;
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