import GlobalConfig from '../../../utils/global-config';
import https from '../../../utils/http';
import authService from '../../../utils/auth';
import toastService from '../../../utils/toast.service';
//获取应用实例
const app = getApp();
var pageNo = -1,
  pageSize = 20,prevList=[];
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [],
    isUpdate: false, //是否修改
    isInsert: false,
    isDelete: false
  },
  selfAdaption() {
    var that = this;
    let winHeight = 0;
    let height = 0;
    wx.getSystemInfo({
      success: function(res) {
        //获取到用户的手机信息
        winHeight = res.windowHeight;
        // 获取需要减去的dom结构的高度信息
        wx.createSelectorQuery().selectAll('.self-adaption').boundingClientRect(function(rects) {
          rects.forEach(function(rect, index) {
            height = height + rect.height;
          })
          that.setData({
            winHeight: winHeight - height
          })
        }).exec()
      }
    });
  },
  getDeliveryAddressList() {
    toastService.showLoading();
    https.request('/api-user/rest/member/deliveryAddress/list', {
      pageNo: -1,
      pageSize: 10
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        this.setData({
          addressList: result.data.records
        })
      }
    })
  },
  editAddressTap(e) {
    wx.navigateTo({
      url: "../edit/edit?detail=" + JSON.stringify(e.currentTarget.dataset.data),
    })
    this.setData({
      refreshKey: e.currentTarget.dataset.key
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    pageNo = 1;
    this.getDeliveryAddressList();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {
    this.selfAdaption();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    // if (this.data.isOperation){
    //   //this.getDeliveryAddressList();
      
    //   this.data.list[this.data.refreshKey] = this.data.refreshData;
    //   this.setData({
    //     list: this.data.list,
    //     isOperation: false
    //   })
    // }
    
    // if (this.data.isUpdate) {
    //   this.data.list.forEach(key => {
    //     if (this.data.refreshData.isDefault == 1) {
    //       key.isDefault = 0;
    //     }
    //   })
    //   this.data.list[this.data.refreshKey] = this.data.refreshData;
    //   this.setData({
    //     list: this.data.list,
    //     isUpdate: false
    //   })
    // }
    // if (this.data.isInsert) {
    //   toastService.showLoading("正在加载...", true);
    //   this.setData({
    //     list: [],
    //     isInsert: false
    //   })
    //   this.getDeliveryAddressList();
    // }
    // if (this.data.isDelete) {
    //   this.data.list.splice(this.data.refreshKey, 1);
    //   this.setData({
    //     list: this.data.list,
    //     isDelete: false
    //   })
    // }
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
    //pageNo = 1;
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();

    this.data.list = [];
    // 设置数组元素
    this.getDeliveryAddressList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    // // 显示加载图标
    // toastService.showLoading();
    // // 页数+1
    // pageNo = pageNo + 1;
    // https.request('/api-user/rest/member/deliveryAddress/list', {
    //   pageNo: pageNo,
    //   pageSize: pageSize
    // }).then(result => {
    //   toastService.hideLoading();
    //   if (result.success) {
    //     var list = this.data.list;
    //     if (result.data.records.length > 0) {
    //       result.data.records.forEach(res => {
    //         this.data.list.push(
    //           res
    //         )
    //       })
    //       this.setData({
    //         list: this.data.list
    //       });
    //       return false;
    //     }
    //     toastService.showToast("没有更多啦~");
    //     pageNo = pageNo - 1;
    //   }
    // })
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})