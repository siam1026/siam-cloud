import toastService from '../utils/toast.service'
module.exports = {
  isCertification: function (){
    let isCertification = wx.getStorageSync("isCertification");
    wx.showModal({
      content: '请完善您的基本信息',
      success(res) {
        if (res.confirm) {
          wx.navigateTo({
            url: '../update_info/update_info',
          })
          return
        } else if (res.cancel) {
          wx.navigateBack(1);
        }
      }
    })
  },
  isUserName: function () {
    wx.showModal({
      content: '请设置您的昵称',
      success(res) {
        if (res.confirm) {
          wx.navigateTo({
            url: '../update_name/update_name',
          })
        } else if (res.cancel) {
          wx.navigateBack(1);
        }
      }
    })
  },
  isToken:function(){
    let token = wx.getStorageSync("token");
    if (!token) {
      wx.reLaunch({
        url: '../pages/mine/index/index',
      })
    }
  }
}


