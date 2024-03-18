import https from '../../../utils/http';
import authService from '../../../utils/auth';
import toastService from '../../../utils/toast.service';
import utils from '../../../utils/util';
import dateHelper from '../../../utils/date-helper';
var interval = null;
//获取应用实例
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    time: '获取验证码',
    currentTime: 60,
    disabled: true,
    disabledCode: true
  },
  getTimestamp() {
    var timestamp = dateHelper.getTimestamp();
    console.log(timestamp)
    this.setData({
      timestamp: timestamp
    })
  },
  phoneKey(e) {
    //判断电话号码的el表达式
    let phone = e.detail.value;
    let isMobile = utils.verifyPhone(phone);
    var data = this.data;
    data.disabledCode = true; //设置“获取验证码”按钮为disable
    //如果通过电话号码的el表达式并且停顿时间等于60秒设置可以重新获取
    if (isMobile && this.data.currentTime == 60) {
      data.disabledCode = false;
    }
    //判断如果验证码输入框不为空并且验证码的长度等于6并且“验证码”按钮不为真则设置可以登录，否则不能登录
    data.disabled = data.code && data.code.length == 6 && !data.disabledCode ? false : true
    this.setData({
      phone: phone,
      disabled: data.disabled,
      disabledCode: data.disabledCode
    })
  },
  codeKey(e) {
    let key = e.detail.value;
    var data = this.data;
    data.disabled = true;
    //判断验证码输入框的长度等于6则设置登录按钮可以点击
    if (key.length == 6) {
      data.disabled = false;
    }
    this.setData({
      code: key,
      disabled: data.disabled
    })
  },
  send(e) {
    console.log(e);
    if (!e.detail.userInfo) {
      return
    }
    //this.onLoad({options:{inviterId:this.data.inviterId}});
    let phone = this.data.phone;
    let isMobile = utils.verifyPhone(phone);
    //电话号码输入有误，弹出模态框提示
    if (!isMobile) {
      toastService.showToast("请输入正确的电话号码");
      return;
    }
    toastService.showLoading("正在发送...", true);
    https.request('/api-util/rest/smsLog/sendMobileCode', {
      mobile: phone,
      type: 'login'
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        toastService.showToast(result.message);
        //设置验证码按钮为不可点击状态，因为要等60秒之后才能进行点击
        this.setData({
          disabledCode: true
        })
        this.getCode();
      }
    })
  },
  getCode: function (options) {
    var that = this;
    var currentTime = this.data.currentTime;
    //设置定时器计算发送验证码之后60秒可以重新发送，一秒请求一次
    interval = setInterval(function () {
      // if (currentTime == 60) {
      //   toastService.hideLoading();
      // }
      currentTime--;
      that.setData({
        time: currentTime + 's',
        currentTime: currentTime
      })
      if (currentTime <= 0) {
        clearInterval(interval); //当60秒过去之后，清除定时器
        that.setData({
          time: '重新获取',
          currentTime: 60,
          disabledCode: false
        })
      }
    }, 1000);
  },
  loginTap() {
    console.log(this.data)
    var _this=this;
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        toastService.showLoading("正在登录...", true);
        let phone = this.data.phone;
        var mobile = /^[1][3,4,5,7,8][0-9]{9}$/;
        var isMobile = mobile.exec(phone);
        //输入有误的话，弹出模态框提示
        if (!isMobile) {
          toastService.showToast("请输入正确的电话号码");
          return;
        }
        if(!this.data.userInfo){
          wx.getUserInfo({
            success: userRes => {
              _this.setData({
                userInfo:userRes.userInfo
              })
              _this.verificationLogin(res.code);
            }
          })
        }else{
          this.verificationLogin(res.code);
        }
      },
      
    })
  },
  verificationLogin(code,userInfo) {
    https.request('/api-user/rest/member/verification/login', {
      mobile: this.data.phone,
      mobileCode: this.data.code,
      headImg: this.data.userInfo.avatarUrl,
      username: this.data.userInfo.nickName,
      sex: this.data.userInfo.gender,
      code: code,
      inviterId: this.data.inviterId ? this.data.inviterId : ''
    }).then(result => {
      if (result.success) {
        authService.setToken(result.data.token);
        authService.setOpenId(result.data.openId);
        authService.setPhoneNumber(this.data.phone);
        app.getUserInfo();
        this.setData({
          disabled: true
        })
        toastService.hideLoading();
        toastService.showSuccess("登录成功");
        let timeout = setTimeout(() => {
          //如果是邀请链接直接跳转到首页，如果是进入用户后退一页
          if (this.data.inviterId) {
            wx.switchTab({
              url: '../../mine/index/index'
            })
          } else {
            wx.navigateBack(2);
          }
          clearTimeout(timeout);
        }, 1000);
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //判断是否有邀请者id传递过来
    if (options.inviterId) {
      this.setData({
        inviterId: options.inviterId
      })
    }
    console.log("options.inviterId = " + options.inviterId);
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
    this.getTimestamp();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    clearTimeout(interval);
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