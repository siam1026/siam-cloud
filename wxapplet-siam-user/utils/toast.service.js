import GlobalConfig from './global-config'
module.exports = {
  showToast: function (title = '', duration) {
    wx.showToast({
      title: title,
      icon: 'none',
      duration: duration ? duration : 3000
    })
  },
  /**
   * 成功
   */
  showSuccess: function (title, mask = true, duration = 3000) {
    wx.showToast({
      title: title,
      mask: mask,
      image: '/assets/images/success.png',
      duration: duration
    })
  },
  /**
   * 警告
   */
  showWarning: function (title, duration = 3000) {
    wx.showToast({
      title: title,
      image: '/assets/images/warning.png',
      duration: duration
    })
  },
  /**
   * 错误
   */
  showError: function (title, mask = false, duration = 3000) {
    wx.showToast({
      title: title,
      mask: mask,
      image: '/assets/images/error.png',
      duration: duration
    })
  },
  /**
   * 显示加载
   */
  showLoading: function (title = '正在加载...', mask = true) {
    wx.showLoading({
      title: title,
      mask: mask
    })
  },
  /**
   * 隐藏加载
   */
  hideLoading: function () {
    wx.hideLoading();
  },
  /**
   * 显示模态窗口
   */
  showModal: function (title=null,content = null, confirm = null, cancel = null,showCancel=true) {
    wx.showModal({
      title: title ? title :'温馨提示',
      content: content || '',
      showCancel:showCancel,
      success(res) {
        if (res.confirm) {
          //console.log('用户点击确定');
          confirm && confirm();
        } else if (res.cancel) {
          //console.log('用户点击取消');
          cancel && cancel();
        }
      }
    })
  }
}