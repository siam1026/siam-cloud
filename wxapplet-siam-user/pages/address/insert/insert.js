import https from '../../../utils/http';
import toastService from '../../../utils/toast.service';
import utils from '../../../utils/util';
import stringService from '../../../utils/string-service';
//获取应用实例
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    sexRadio: [
      {
        id: 0,
        name: "先生",
        checked: true
      },
      {
        id: 1,
        name: "女士",
        checked: false
      }
    ],
    tagRadio: [
      {
        id: 0,
        name: "家",
        checked: true
      },
      {
        id: 1,
        name: "公司",
        checked: false
      },
      {
        id: 2,
        name: "学校",
        checked: false
      }
    ],
    sexKey: 0,
    region: [],
    isDefault: 0
  },
  bindPhoneBlur(e) {
    let phone = e.detail.value;
    let isMobile = utils.verifyPhone(phone)
    if (!isMobile) {
      toastService.showToast("手机号不正确");
      return
    }
  },

  sexRadioChange(e) {
    let checkedValue = e.detail.value;
    for (let key in this.data.sexRadio) {
      this.data.sexRadio[key].checked = false;
    }
    this.data.sexRadio[checkedValue].checked = true;
    this.setData({
      sexKey: checkedValue,
      sexRadio: this.data.sexRadio
    })
  },
  tagRadioChange(e) {
    let checkedValue = e.detail.value;
    for (let key in this.data.tagRadio) {
      this.data.tagRadio[key].checked = false;
    }
    this.data.tagRadio[checkedValue].checked = true;
    this.setData({
      tagKey: checkedValue,
      tagRadio: this.data.tagRadio
    })
  },
  bindRegionChange: function (e) {
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
  },
  isDefaultChange(e) {
    this.setData({
      isDefault: e.detail.value.length == 0 ? 0 : 1
    })
  },
  formSubmit(e) {
    //console.log(e)
    var res = e.detail.value;
    if (stringService.isEmpty(res.realname)) {
      toastService.showToast("联系人姓名为必填");
      return;
    }

    if (stringService.isEmpty(res.phone)) {
      toastService.showToast("手机号为必填");
      return;
    }

    let isMobile = utils.verifyPhone(res.phone);
    if (!isMobile) {
      toastService.showToast("手机号不正确");
      return
    }

    if (stringService.isEmpty(res.street)) {
      toastService.showToast("门牌号为必填");
      return;
    }
    console.log(this.data)
    res.province = this.data.region[0];
    res.city = this.data.region[1];
    res.area = this.data.region[2];
    res.isDefault = this.data.isDefault;
    res.longitude = this.data.longitude;
    res.latitude = this.data.latitude;
    https.request('/api-user/rest/member/deliveryAddress/insert', res).then(result => {
      if (result.success) {
        toastService.showSuccess(result.message, true, 1000);
        var pages = getCurrentPages();
        //var currPage = pages[pages.length - 1];   //当前页面
        var prevPage = pages[pages.length - 2];  //上一个页面
        //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
        /* var prevData = prevPage.data.addressList;
        res.id=result.data;
        prevData.push(res);
        prevPage.setData({
          addressList: prevData,
        }) */
        prevPage.data.addressList = [];
        prevPage.getDeliveryAddressList();
        wx.navigateBack(1);
      } else {
        showToast.showToast(result.message)
      }
    })
  },
  getRegeoAddress(e) {
    wx.navigateTo({
      url: '../../address/search/search',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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