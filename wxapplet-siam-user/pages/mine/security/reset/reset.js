import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import dateHelper from '../../../../utils/date-helper';
import systemStatus from '../../../../utils/system-status';
import util from '../../../../utils/util';
import base64 from '../../../../utils/base64';
//确认密码输入两次重新进入输入密码
var number = 0;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    showPayPwdInput: false,  //是否展示密码输入层
    pwdVal: '',  //输入的密码
    payFocus: true, //文本框焦点
    isAgain: false,
    adjustPosition:false
  },
  getUserInfo: function (e) {
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
  /**
   * 显示支付密码输入层
   */
  showInputLayer: function () {
    this.setData({ showPayPwdInput: true, payFocus: true });
  },
  /**
   * 隐藏支付密码输入层
   */
  hidePayLayer: function () {

    var val = this.data.pwdVal;

    this.setData({
      showPayPwdInput: false,
      payFocus: true,
      pwdVal: '',
      showPayPwdInput: true,
      oldPassword: val,
      isAgain: true
    });
  },
  /**
   * 获取焦点
   */
  getFocus: function () {
    this.setData({ payFocus: true });
  },
  /**
   * 输入密码监听
   */
  inputPwd: function (e) {
    console.log(e.detail.value)
    if (!util.verifyNumberPassword(Number(e.detail.value))) {
      this.setData({
        payFocus: true,
        pwdVal: this.data.pwdVal,
      })
      toastService.showToast("请数字密码");
      return
    }
    this.setData({
      pwdVal: e.detail.value
    });

    
    if (e.detail.value.length >= 6) {
      if (!this.data.isAgain) {
        this.hidePayLayer();
      } else {
        if (e.detail.value != this.data.oldPassword) {
          this.atypismTip();
          number++;
          if (number == 2) {
            console.log(number);
            number=0;
            this.data.isAgain = false;
            this.setData({
              isAgain:this.data.isAgain
            });
            toastService.showToast("两次密码输入不一致，请重新输入6位数密码");
          }else{
            toastService.showToast("两次密码输入不一致，请重新输入");
          }
          
        } else {
          toastService.showLoading("正在加载...");
          this.comfirmSuccess();
        }
      }
    }
  },
  comfirmSuccess() {
    https.request('/api-user/rest/member/forgetPaymentPassword/step2', {
      paymentPassword: base64.encode(this.data.oldPassword),
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        toastService.showSuccess("设置密码成功");
        let timeout = setTimeout(() => {
          wx.navigateBack(1);
          clearTimeout(timeout);
        }, 2000);
      }
    })
  },
  atypismTip: function () {
    this.setData({
      payFocus: true,
      pwdVal: '',
    });
  },
  bindNext(e) {
    wx.navigateTo({
      url: '../verify/verify',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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