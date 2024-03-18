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
  bindVerify(e){
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