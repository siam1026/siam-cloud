import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import dateHelper from '../../../../utils/date-helper';
import systemStatus from '../../../../utils/system-status';
var pageNo=1,pageSize=20;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list:[]
  },
  getVipRechargeRecordList() {
    toastService.showLoading("正在加载...")
    https.request('/api-user/rest/member/vipRechargeRecord/list', {
      pageNo: pageNo,
      pageSize: pageSize
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
  bindDetailInfo(e){
    console.log(e)
    let id=e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '../detail/detail?id='+id,
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getVipRechargeRecordList();
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