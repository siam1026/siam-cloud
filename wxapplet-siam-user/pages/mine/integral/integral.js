import https from '../../../utils/http';
import authService from '../../../utils/auth';
import toastService from '../../../utils/toast.service';
import dateHelper from '../../../utils/date-helper';
import systemStatus from '../../../utils/system-status';
var pageNo = 1,
  pageSize = 10;
Page({
 
  /**
   * 页面的初始数据
   */
  data: {
    // modeList: [{
    //   modeName: "积分明细",
    //   modeId: 0
    // }
    // , {
    //   modeName: "兑换记录",
    //   modeId: 1
    // }
    // ],
    loading: false,
    color: '#000',
    background: 'rgba(0,0,0,0)',
    show: true,
    animated: false,
    extClass:'weui-navigation-bar-custom',
    currentTab: 0,
    exchangeList: [],
    button: false,
    integralList:[],
    specificationsDialog:false
  },
  openSpecifications(e) {
    console.log(e)
    this.setData({
      specificationsDialog: true
    });
    this.recordSelectById(e.currentTarget.dataset.id);
  },
  recordSelectById(id){
    https.request('/api-user/rest/member/billingRecord/selectById', {
      id: id
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.createTime = dateHelper.fmtDate(result.data.createTime);
        this.setData({
          recordInfo:result.data
        })
      }
    })
  },
  getMemberBillingRecord(e) {
    https.request('/api-user/rest/member/billingRecord/list', {
      pageNo: pageNo,
      pageSize: pageSize,
      coinType: 1
    }).then(result => {
      if (result.success) {
        result.data.records.forEach(res => {
          res.createTime = dateHelper.fmtDate(res.createTime);
          res.memberIntegralTypeText = systemStatus.memberIntegralTypeText(res.memberIntegralTypeText);
        });
        this.setData({
          integralList: result.data.records
        })
        this.getHeight();
      }
    })
  },
  getHeight() {
    //获取用户手机系统信息
    var that = this;
    wx.getSystemInfo({
      success: function(res) {
        let winHeight = res.windowHeight; //获取高度
        //获取class为settlement-view并减去这个高度，自适应scrollview的高度
        const query = wx.createSelectorQuery()
        query.select('.balance-views').boundingClientRect()
        query.selectViewport().scrollOffset()
        query.exec(function(res) {
          that.setData({
            winHeight: winHeight - res[0].height
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
  // 滑动切换tab
  bindSlideChange: function(e) {
    var that = this;
    that.setData({
      currentTab: e.detail.current
    });
  },
  //点击切换 
  clickTab: function(e) {
    var that = this;
    if (that.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      });
    }
  },
  bindUnreceivedPoints(){
    wx.navigateTo({
      url: '../unreceived/integral/list/list',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.getUserInfo();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {
    this.getMemberBillingRecord();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {
    pageNo = 1;
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    //
    this.getMemberBillingRecord();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    pageNo = pageNo + 1;
    toastService.showLoading("正在加载...");
    https.request('/api-user/rest/member/billingRecord/list', {
      pageNo: pageNo,
      pageSize: pageSize,
      coinType: 1
    }).then(result => {
      if (result.success) {
        this.getHeight();
        toastService.hideLoading();
        if (result.data.records.length > 0) {
          result.data.records.forEach(res => {
            res.createTime = dateHelper.formatISODate(res.createTime);
            res.memberIntegralTypeText = systemStatus.memberIntegralTypeText(res.memberIntegralTypeText);
            this.data.integralList.push(
              res
            )
          });
          this.setData({
            integralList: this.data.integralList
          })
          return
        }
        toastService.showToast("没有更多啦~")
        pageNo = pageNo - 1;
      }
    })
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})