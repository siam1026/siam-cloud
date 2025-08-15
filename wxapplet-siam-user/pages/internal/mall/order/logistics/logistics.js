import https from '../../../../../utils/http';
import authService from '../../../../../utils/auth';
import toastService from '../../../../../utils/toast.service';
import dateHelper from '../../../../../utils/date-helper';
import systemStatus from '../../../../../utils/system-status';
import GlobalConfig from '../../../../../utils/global-config';
import util from '../../../../../utils/util';
const app = getApp();
let logisticsJson={};
Page({

   /**
    * 页面的初始数据
    */
   data: {
      list:[]
   },
   getOrderDetail(id) {
      toastService.showLoading();
      https.request('/api-mall/rest/member/pointsMall/orderLogistics/list', {
         orderId: id,
         pageNo:-1,
         pageSize:20
      }).then(result => {
         toastService.hideLoading();
         if (result.success) {
            result.data.records.forEach((item,index)=>{
               item.descriptionTime = dateHelper.formatDate(item.descriptionTime);
            })
            this.setData({
               list: result.data.records,
               logisticsJson:logisticsJson
            });
         }
      })
   },
   contactCourier(){
      wx.makePhoneCall({
         phoneNumber: this.data.logisticsJson.courierPhone
      })
   },
   /**
    * 生命周期函数--监听页面加载
    */
   onLoad: function (options) {
      console.log(options.logisticsJson)
      logisticsJson=JSON.parse(options.logisticsJson);
      logisticsJson.logisticsStatusText=systemStatus.logisticsStatusText(logisticsJson.deliveryStatus);
      this.getOrderDetail(logisticsJson.orderId);
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