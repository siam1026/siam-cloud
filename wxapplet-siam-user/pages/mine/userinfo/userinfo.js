import GlobalConfig from '../../../utils/global-config';
import https from '../../../utils/http';
import authService from '../../../utils/auth';
import toastService from '../../../utils/toast.service';
import utils from '../../../utils/util';
//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    buttons: [{
      text: '取消'
    }, {
      text: '确定'
    }]
  },
  getUserInfo() {
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        result.data.email = result.data.email ? result.data.email : "";
        result.data.realName = result.data.realName ? result.data.realName : "";
        this.setData({
          data: result.data
        })
      }
    })
  },
  bindWx(e) {
    var that = this;
    toastService.showModal(null, "确定解除与微信账号的关联关系吗？", function confirm() {
      https.request('/api-user/rest/member/removeBindingWx', {
        mobile: e.currentTarget.dataset.mobile,
        isbindwx: e.currentTarget.dataset.isbindwx
      }).then(result => {
        if (result.success) {
          toastService.showToast(result.message);
          that.setData({
            "data.isBindWx": false
          })
        }
      })
    })
  },
  openEmail() {
    this.setData({
      dialogShowEmail: true,
      email: this.data.data.email
    })
  },
  openRealName(){
    this.setData({
      dialogShowRealName: true,
      realName: this.data.data.email
    })
  },
  openConfirm() {
    this.setData({
      dialogShow: true,
      maskClosable: false,
      modeList: [{
        name: "男",
        value: 1,
        checked: this.data.data.sex == 1 ? true : false
      }, {
        name: "女",
        value: 2,
        checked: this.data.data.sex == 2 ? true : false
      }]
    })
  },
  radioChange(e) {
    this.setData({
      sex: e.detail.value
    })
  },
  bindOutLogTap() {
    toastService.showModal(null, "确定退出登录吗？",
      function confirm() {
        https.request('/api-user/rest/member/logout', {}).then(result => {
          if (result.success) {
            authService.deleteToken();
            authService.deleteOpenId();
            wx.navigateBack(1);
          }
        })
      }
    )
  },
  tapdialogButton: function (e) {
    if (e.detail.item.text == "确定") {
      https.request('/api-user/rest/member/update', {
        sex: this.data.sex,
      }).then(result => {
        if (result.success) {
          toastService.showToast(result.message);
          this.setData({
            "data.sex": this.data.sex
          })
        }
      })
    }
    this.setData({
      dialogShow: false
    })
  },
  tapdialogEmailButton(e) {
    if (e.detail.item.text == "确定") {
      let email = this.data.email;
      if (email == this.data.data.email) {
        toastService.showToast("请输入不同的邮箱号码");
        return;
      }
      let isEmail = utils.verifyEmail(email);

      if (!isEmail) {
        toastService.showToast("邮箱号码格式错误");
        return
      }
      https.request('/api-user/rest/member/update', {
        email: this.data.email,
      }).then(result => {
        if (result.success) {
          toastService.showToast(result.message);
          this.setData({
            "data.email": email
          })
        }
      })
    }
    this.setData({
      dialogShowEmail: false
    })
  },
  tapdialogRealNameButton(e) {
    if (e.detail.item.text == "确定") {
      let realName = this.data.realName;
      if(!realName){
        toastService.showToast("请输入真实姓名");
        return
      }
      https.request('/api-user/rest/member/update', {
        realName: realName,
      }).then(result => {
        if (result.success) {
          toastService.showToast(result.message);
          this.setData({
            "data.realName": realName
          })
        }
      })
    }
    this.setData({
      dialogShowRealName: false
    })
  },
  bindInputEmail(e) {
    this.setData({
      email: e.detail.value
    })
  },
  bindInputRealName(e){
    this.setData({
      realName: e.detail.value
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
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
      // wx.getUserInfo({
      //   success: res => {
      //     app.globalData.userInfo = res.userInfo
      //     this.setData({
      //       userInfo: res.userInfo,
      //       hasUserInfo: true
      //     })
      //   }
      // })
    };
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
    this.getUserInfo();
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