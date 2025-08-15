const toastService = require("utils/toast.service.js");
import https from "utils/http.js";
import authService from 'utils/auth';
import utilsHelper from 'utils/util';
import dateHelper from 'utils/date-helper';
var amapFile = require('utils/gaode-libs/amap-wx.js');
var config = require('utils/gaode-libs/config.js');
App({
  onLaunch: function () {
    // 展示本地存储能力 
    // var logs = wx.getStorageSync('logs') || [] 
    // logs.unshift(Date.now()) 
    // wx.setStorageSync('logs', logs) 

    // 登录 
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId 
        this.globalData.code = res.code;
      }
    })
    // 获取用户信息 
    // wx.getSetting({ 
    //   success: res => { 
    //     if (res.authSetting['scope.userInfo']) { 
    //       // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框 
    //       wx.getUserProfile({ 
    //         success: res => {
    //           console.log(res)
    //           // 可以将 res 发送给后台解码出 unionId 
    //           this.globalData.userInfo = res.userInfo 

    //           // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回 
    //           // 所以此处加入 callback 以防止这种情况 
    //           if (this.userInfoReadyCallback) { 
    //             this.userInfoReadyCallback(res); 
    //           } 
    //         } 
    //       }) 
    //     } 
    //   } 
    // }) 
    this.getSystemInfo();
    this.getUserInfo();
    this.getRegeoLocation();
    this.getShoppingCarNumber();
  },
  onHide() {

  },
  onShow: function () {

  },
  getRegeoLocation() {
    var key = config.Config.key;
    var myAmapFun = new amapFile.AMapWX({
      key: key
    });
    let self = this;
    // this.getDeliveryAddressList(); 
    myAmapFun.getRegeo({
      success: function (getRegeo) {
        console.log(getRegeo)
        if (getRegeo) {
          self.deliveryAndSelfTaking.location = getRegeo[0].longitude + "," + getRegeo[0].latitude
        }
        https.request("/api-user/rest/member/updateLastUseAddress", {
          lastUseAddress: getRegeo[0].regeocodeData.formatted_address
        }).then(result => {
          toastService.hideLoading();
          if (result.success) {
            //console.log(result) 
          }
        })
      },
      fail: function (info) {
        //toastService.showLoading(); 
        // wx.showModal({title:info.errMsg}) 
      }
    })
  },
  getSettingInfo() {
    var that = this;
    wx.getSetting({
      success(res) {
        //console.log(res) 
        if (res.authSetting['scope.userLocation']) {
          that.globalData.authSetting = res.authSetting;
          //console.log(that.globalData.authSetting) 
        }
      }
    })
  },
  getSystemInfo() {
    var res = wx.getSystemInfoSync();
    this.globalData.systemInfoSync = res;
  },
  getLoginCode() {
    return new Promise((fulfil, reject) => {
      wx.login({
        success: res => {
          if (res.code) {
            fulfil(res.code);
          }
        }
      })
    })
  },
  weChatLogin(phoneNumber, openId, inviterId) {
    console.log("app.js -> inviterId = " + inviterId);
    console.log(this.globalData.userInfo)
    var userInfo = this.globalData.userInfo;
    if (!userInfo) {
      wx.getUserInfo({
        success: res => {
          this.memberWeChatLogin(phoneNumber, openId, inviterId, res.userInfo);
        }
      })

    } else {
      this.memberWeChatLogin(phoneNumber, openId, inviterId, userInfo);
    }
  },
  memberWeChatLogin(phoneNumber, openId, inviterId, userInfo) {
    https.request("/api-user/rest/member/weChat/login", {
      mobile: phoneNumber,
      headImg: userInfo.avatarUrl,
      username: userInfo.nickName,
      sex: userInfo.gender,
      openId: openId,
      inviterId: inviterId ? inviterId : ''
    }).then(result => {
      if (result.success) {
        authService.setToken(result.data.token);
        authService.setOpenId(openId);
        authService.setPhoneNumber(phoneNumber);
        toastService.showSuccess(result.message);
        this.globalData.isNewPeople = result.data.isNewPeople;
        this.getUserInfo();
        let timeout = setTimeout(() => {
          //如果是邀请链接直接跳转到首页，如果是进入用户后退一页 
          if (inviterId) {
            wx.switchTab({
              url: '/pages/index/index',
            })
          } else {
            var pages = getCurrentPages();
            if (pages && pages.length > 0) {
              wx.navigateBack(1);
            } else {
              wx.switchTab({
                url: '/pages/index/index',
              })
            }
          }
          clearTimeout(timeout);
        }, 300);
      }
    })
  },
  getMemberInfo: function (e) {
    return new Promise((fulfil, reject) => {
      https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
        if (result.success) {
          fulfil({
            isNewPeople: result.data.isNewPeople,
            isRemindNewPeople: result.data.isRemindNewPeople
          });
        }
      })
    })
  },
  getUserInfo() {
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        //console.log(result.data); 
        this.globalData.loginUserInfo = result.data;
        //console.log(this.globalData.userInfo); 
      }
    })
  },
  bindInDevelopment() {
    toastService.showModal(null, "敬请期待~", null, null, false)
  },
  getIsBusiness(startTime, endTime, isOperating) {
    return new Promise((fulfil, reject) => {
      let isBusiness = dateHelper.differenceTime(startTime, endTime);
      console.log("实际后端传值isOperating======》" + isOperating);
      console.log("计算商家的关店时间isBusiness====》" + isBusiness);
      if (!isOperating || !isBusiness) {
        fulfil(false);
        toastService.showModal(null, "店铺休息中，目前不能下单", function confirm() {
          return
        }, null, false);
        return
      }
      fulfil(true);
    })
  },
  getIsItNear(startTime, endTime) {
    let itNear = dateHelper.itNearTime(startTime, endTime);
    console.log("是否相差30分钟===》" + itNear);
    console.log("商家开门时间======》" + startTime);
    console.log("商家打烊时间====》" + endTime);
    if (itNear) {
      if (!this.deliveryAndSelfTaking.isItNear) {
        this.deliveryAndSelfTaking.isItNear = true;
        toastService.showModal(null, "店铺临近打烊或已打烊", function confirm() {
          return
        }, null, false);
      }
    }
  },
  isRemindNewPeople() {
    return new Promise((fulfil, reject) => {
      https.request('/api-user/rest/member/updateIsRemindNewPeople').then(result => {
        console.log(result)
        if (result.success) {
          fulfil(true);
        }
        fulfil(false)
      })
    })
  },
  checkIsAuth(authSetting, params) {
    toastService.showModal(
      null,
      '当前未登录，确定去登录吗?',
      function confirm() {
        wx.getSetting({
          success: (res) => {
            console.log(res)
            console.log(res.authSetting[authSetting]);
            if (res.authSetting[authSetting]) {
              let url = '/pages/login/choose/choose';
              if (params) {
                if (params.inviterId) {
                  url = url + (params.inviterId ? "?inviterId=" + params.inviterId : "");
                }
              }
              wx.navigateTo({
                url: url,
              })
            } else {
              let url = '/pages/internal/login/authorization/authorization';
              if (params) {
                if (params.inviterId) {
                  url = url + (params.inviterId ? "?inviterId=" + params.inviterId : "");
                }
              }
              wx.redirectTo({
                url: url
              });
            }
          }
        })
      })
  },
  getDeliveryAddressList() {
    https.request('/api-user/rest/member/deliveryAddress/list', {
      pageNo: -1,
      pageSize: 10
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.records.forEach((item, index) => {
          if (item.isDefault) {
            this.deliveryAndSelfTaking.deliveryAddress = item;
          }
        });
      }
    })
  },
  getShoppingCarNumber() {
    authService.checkIsLogin().then(result => {
      if (result) {
        https.request('/api-mall/rest/member/pointsMall/shoppingCart/list', {
          pageNo: -1,
          pageSize: 20
        }).then(result => {
          if (result.success) {
            const items = result.data.records;
            let totalNum = 0;
            for (let i = 0; i < items.length; i++) {
              totalNum = totalNum + items[i].number;
            }
            if (totalNum) {
              wx.setTabBarBadge({
                index: 2,
                text: String(totalNum)
              })
              return
            }
            wx.removeTabBarBadge({
              index: 2,
            })
          }
        })
        return;
      }
    })
  },

  globalData: {
    userInfo: null,
    appVersion: 5.28
  },
  deliveryAndSelfTaking: {
    modeList: [{
      id: 0,
      radioName: "配送",
      title: "配送信息",
      tipName: "送达",
      checked: true
    }, {
      id: 1,
      radioName: "自取",
      tipName: "可取",
      title: "自提门店",
      mode: [{
          id: 0,
          name: "店内用餐",
          checked: true
        },
        {
          id: 0,
          name: "自提带走",
          checked: false
        }
      ],
      checked: false
    }],
    radioIndex: 0,
    feeData: 0,
    shopAddress: null,
    currentTab: 0,
    isReducedDeliveryPrice: false,
    reducedDeliveryTotalPrice: 0,
    isItNear: false,
    isThereADiscount: false,
    location: '',
    isOutofDeliveryRange: false,
    regeoInfo: null
  }
})