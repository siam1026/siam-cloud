import GlobalConfig from '../../../utils/global-config';
const show = require("../../../utils/toast.service.js");
import https from '../../../utils/http';
//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    searchInput: false,
    searchTip: false,
    isActivityDialog:false
  },
  getScrolls() {
    var that = this;
    const query = wx.createSelectorQuery();
    let windowHeight = app.globalData.systemInfoSync.windowHeight;
    query.selectAll('.search-input-views').boundingClientRect(function (rect) {
      let height = rect[0].height;
      that.setData({
        bussinessHeight: windowHeight
      })
    }).exec();
  },
  searchInput(e) {
    console.log(e)
    let value = e.detail.value;
    this.data.searchInput = false
    if (value && value != this.data.searchValue) {
      this.data.searchInput = true;
      this.data.searchTip = true;
    }
    this.setData({
      searchInput: this.data.searchInput,
      searchValue: value,
      searchTip: this.data.searchTip,
      shopList: []
    })
  },
  searchTap() {
    show.showLoading("搜索中...");
    this.getShopList(this.data.searchValue);
  },
  businessTap(e) {
    wx.navigateTo({
      url: '../../menu/index/index?id=' + e.currentTarget.dataset.id,
    })
  },
  radioSearchTap(e) {
    this.setData({
      searchValue: e.currentTarget.dataset.name
    })
    // let searchValue = {
    //   detail: {
    //     value: e.currentTarget.dataset.name
    //   }
    // }
    // this.searchInput(searchValue)
    this.getShopList(e.currentTarget.dataset.name);
  },
  getShopList(name) {
    https.request('/api-goods/rest/shop/search', {
      pageNo: -1,
      pageSize: 5,
      keywords: name,
      position:app.deliveryAndSelfTaking.location
    }).then(result => {
      show.hideLoading();
      if (result.success) {
        result.data.records.forEach((item, index) => {
          item.shopLogoImg = GlobalConfig.ossUrl + item.shopLogoImg;
          item.isfeeData = false;
          if (item.reducedDeliveryPrice > 0) {
            if (item.reducedDeliveryPrice >= item.shopAdditionalVo.deliveryFee) {
              item.feeData = 0;
              item.isfeeData = true;
            } else {
              item.feeData = item.shopAdditionalVo.deliveryFee - item.reducedDeliveryPrice;
              item.isfeeData = true;
            }
          }
          item.goodsList.forEach((goods,goodsIndex)=>{
            goods.mainImage=GlobalConfig.ossUrl+goods.mainImage;
          })
        })
        this.setData({
          searchTip: result.data.records?false:true,
          searchInput:result.data.records?true:false
        });
        this.getShoppingCartList(result.data.records);
      }
    })
  },
  getShoppingCartList(shopList) {
    shopList.forEach((item, index) => {
      https.request('/api-goods/rest/member/shoppingCart/list', {
        shopId: item.id,
        pageNo: -1,
        pageSize: 20
      }).then(result => {
        show.hideLoading();
        if (result.success && result.data) {
          var number = 0;
          result.data.records.forEach((cart, index) => {
            number = number + cart.number;
          });
          
          item.shopCartNums=number;
          this.setData({
            shopList: shopList
          })
        }
      })
    });
  },
  commodityDetailTap(e) {
    wx.navigateTo({
      url: '../../menu/detail/detail?id=' + e.currentTarget.dataset.id + "&shopId=" + e.currentTarget.dataset.shopid,
    })
  },
  isPromotionTap(e){
    let shopIndex=e.currentTarget.dataset.shopindex;
    console.log(shopIndex)
    this.setData({
      isActivityDialog:true,
      shopIndex:shopIndex
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    this.setData({
      location:options.location
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.getScrolls();
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