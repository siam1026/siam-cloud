import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import dateHelper from '../../../../utils/date-helper';
import systemStatus from '../../../../utils/system-status';
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  getvipRechargeRecordDetailInfo(id) {
    toastService.showLoading("正在加载...")
    https.request('/api-user/rest/member/vipRechargeRecord/detail', {
      id: id
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.createTime = dateHelper.fmtDate(result.data.createTime);
        result.data.statusText=systemStatus.payStatusText(result.data.status);
        result.data.channelText=systemStatus.rchargeChannelText(result.data.channel);
        this.setData({
          info: result.data
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    this.getvipRechargeRecordDetailInfo(options.id);
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