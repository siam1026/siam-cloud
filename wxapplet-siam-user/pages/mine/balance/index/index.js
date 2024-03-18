import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import dateHelper from '../../../../utils/date-helper';
import systemStatus from '../../../../utils/system-status';
var pageNo=1,pageSize=10;
Page({
 
  /**
   * 页面的初始数据
   */
  data: {
    list:[],
    specificationsDialog:false
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
  getMemberBillingRecordList() {
    toastService.showLoading("正在加载...")
    https.request('/api-user/rest/member/billingRecord/list', {
      pageNo: pageNo,
      pageSize: pageSize,
      coinType:2
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.records.forEach((item,index)=>{
          item.createTime = dateHelper.fmtDate(item.createTime);
          item.statusText=systemStatus.payStatusText(item.status);
          this.data.list.push(item);
        })
        this.setData({
          isLastPage:result.data.isLastPage,
          list: this.data.list
        })
      }
    })
  },
  getUserInfo: function (e) {
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        this.setData({
          userInfo: result.data
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getHeight();
    this.getUserInfo();
    this.getMemberBillingRecordList();
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
    if(this.data.isLastPage){
      toastService.showToast("没有更多啦~");
      return;
    }
    pageNo = pageNo + 1;
    this.getVipRechargeRecordList();
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})