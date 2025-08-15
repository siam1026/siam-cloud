import https from '../../../utils/http';
import authService from '../../../utils/auth';
import toastService from '../../../utils/toast.service';
import dateHelper from '../../../utils/date-helper';
import systemStatus from '../../../utils/system-status';
//获取应用实例
const app = getApp()
Page({
  data: {
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    couponsNum: 0,
    tabList: [{
      modeId: 0,
      modeName: "外卖订单"
    }, {
      modeId: 1,
      modeName: "商城订单"
    }],
    shopOrderTabList: [{
      modeId: 1,
      modeType: "waitPayment",
      modeName: "待付款",
      icon: "icondaifukuan"
    },
    {
      modeId: 2,
      modeType: "waitReceived",
      modeName: "待收货",
      icon: "icondaifahuo1"
    },
    {
      modeId: 3,
      modeType: "waitPickedUp",
      modeName: "待自提",
      icon: "icondaifahuo"
    },
    {
      modeId: 4,
      modeType: "afterSales",
      modeName: "退款/售后",
      icon: "icontuikuan"
    }
    ],
    pointsOrderTabList: [{
      modeId: 1,
      modeType: "waitPayment",
      modeName: "待付款",
      icon: "icondaifukuan"
    },
    {
      modeId: 2,
      modeType: "waitDelivered",
      modeName: "待发货",
      icon: "icondaifahuo1"
    },
    {
      modeId: 3,
      modeType: "waitReceived",
      modeName: "待收货",
      icon: "icondaifahuo"
    },
    {
      modeId: 4,
      modeType: "afterSales",
      modeName: "退款/售后",
      icon: "icontuikuan"
    }
    ],
    currentTab: 0,
    isVipImages: [],
    appVersion: app.globalData.appVersion
  },
  // 滑动切换tab
  bindSlideChange: function (e) {
    this.setData({
      currentTab: e.detail.current
    });

  },
  getTimestamp() {
    var timestamp = dateHelper.getTimestamp();
    console.log(timestamp)
    this.setData({
      timestamp: timestamp
    })
  },
  //点击切换 
  clickTab: function (e) {
    console.log(e.target.dataset.current)
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      // 显示加载图标
      this.setData({
        currentTab: e.target.dataset.current
      });
    }
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
    // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    console.log(app.globalData.userInfo)
    if (app.globalData.userInfo) {
      wx.navigateTo({
        url: '../../login/choose/choose'
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
            wx.navigateTo({
              url: '../../login/choose/choose'
            })
          } else {
            
          }
        },fail(res){
          console.log(res)
          app.globalData.userInfo = null;
            wx.navigateTo({
              url: '../../login/choose/choose'
            })
        }
      })
    }
  },
  bindUserinfoTap() {
    wx.navigateTo({
      url: '../userinfo/userinfo'
    })
  },
  getUserInfo: function (e) {
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        result.data.typeVipText = systemStatus.typeVipText(result.data.type);
        result.data.statusVipText = systemStatus.statusVipText(result.data.vipStatus);
        result.data.vipStartTime = dateHelper.formatDate(result.data.vipStartTime);
        result.data.vipEndTime = dateHelper.formatDate(result.data.vipEndTime);
        this.data.isVipImages = [];
        if (result.data.type == 1) {
          this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/user_1.png");
          this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/user_2.png");
          this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/user_3.png");
        } else {
          this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/vip_1.png");
          this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/vip_2.png");
          this.data.isVipImages.push("https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/business/mine_page/vip_3.png");
        }
        this.setData({
          data: result.data,
          userInfo: this.data.userInfo,
          isVipImages: this.data.isVipImages
        })
      }
    })
  },
  // getCouponsSelectCounts() {
  //   https.request('/api-promotion/rest/member/couponsMemberRelation/selectCounts').then(result => {
  //     if (result.success) {
  //       this.getPointsCouponsSelectCounts(result.data)
  //     }
  //   })
  // },
  // getPointsCouponsSelectCounts(couponsNum) {
  //   https.request('/api-mall/rest/member/pointsMall/couponsMemberRelation/selectCounts').then(result => {
  //     if (result.success) {
  //       this.setData({
  //         couponsNum: result.data+couponsNum
  //       })
  //     }
  //   })
  // },
  onLoad: function () {

    this.setData({
      statusBarHeight: app.globalData.systemInfoSync.statusBarHeight * 2
    })
    if (app.globalData.userInfo) {
      console.log(1)
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      console.log(2)
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      console.log(3)
      // 在没有 open-type=getUserInfo 版本的兼容处理
      
    }
  },
  getRequestSubscribeMessage() {
    console.log(111)
    let use = wx.canIUse("requestSubscribeMessage");
    console.log(use)
    if (typeof wx.requestSubscribeMessage === 'function') {
      console.log(true)
    }
    wx.requestSubscribeMessage({
      tmplIds: ['N63hQksq6Rp3XlrBySkligk-pSvvpJ5fCovwUkwHVH4',
        'eF29ugG7ZKKKHCDH2Nk48Q2JCH1qKDLBMX2LnAzCz-w',
        'CE3V7tzt4-PSsf8s-xZg731zHtDEkQmBOedcEEv3cx8'],
      success(res) {
        console.log(res)
      },
      fail(error) {
        console.log(error)
      }
    })
  },
  bindAccountBalance() {
    wx.navigateTo({
      url: '../balance/index/index',
    })
  },
  bindMemberCenter() {
    wx.navigateTo({
      url: '../member/index/index',
    })
  },
  onShow() {

    authService.checkIsLogin().then(result => {
      console.log(result)
      if (result) {
        this.getUserInfo();
        // this.getCouponsSelectCounts();
        return;
      }
      this.setData({
        data: null,
        userInfo: null
      })
    });
    this.getTimestamp();
  },
  bindInDevelopment() {
    app.bindInDevelopment();
  },
  bindOrderInfo(e) {
    wx.navigateTo({
      url: '../../order/index/index?currentTab=0&modeType=all&currentOrderTab=0',
    })
  }
})