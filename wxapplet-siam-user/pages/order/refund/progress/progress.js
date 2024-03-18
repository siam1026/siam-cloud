import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import dateHelper from '../../../../utils/date-helper';
import systemStatus from '../../../../utils/system-status';
import GlabalConfig from '../../../../utils/global-config';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    refundReasonApply: [{
      value: 1, name: "信息填写错误"
    }, {
      value: 2, name: "送达时间选错了"
    },
    { value: 3, name: "买错了/买少了" },
    { value: 4, name: "商家缺货" },
    { value: 5, name: "商家联系我取消" },
    { value: 6, name: "配送太慢" },
    { value: 7, name: "骑手联系我取消" },
    {
      value: 8, name: "我不想要了"
    },{
      value: 9, name: "商家通知我卖完了",checked:false
    }, {
      value: 10, name: "商家沟通态度差",checked:false
    },
    { value: 11, name: "骑手沟通态度差",checked:false },
    { value: 12, name: "送太慢了，等太久了",checked:false },
    { value: 13, name: "商品撒漏/包装破损",checked:false },
    { value: 14, name: "商家少送商品",checked:false },
    { value: 15, name: "商家送错商品",checked:false },
    {
      value: 16, name: "口味不佳/个人感受不好",checked:false
    }, { value: 17, name: "餐品内有异物",checked:false }
      , { value: 18, name: "食用后引起身体不适",checked:false }
      , { value: 19, name: "商品变质/有异味",checked:false }
      , { value: 20, name: "其他",checked:false }],
  },
  getSelectRefundProcess(orderId) {
    toastService.showLoading();
    https.request('/api-order/rest/member/order/selectRefundProcess', {
      id: orderId
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.orderRefundGoodsList.forEach((item, index) => {
          let specListAnalysis = "";
          for (var key in JSON.parse(item.specList)) {
            specListAnalysis = (specListAnalysis ? specListAnalysis + "/" : specListAnalysis) + JSON.parse(item.specList)[key];
          }
          item.specListAnalysis = specListAnalysis;
          item.mainImage = GlabalConfig.ossUrl + item.mainImage;
        });
        result.data.orderRefundProcessList.forEach((item, index) => {
          item.createTime=dateHelper.fmtDate(item.createTime);
        });
        this.data.refundReasonApply.forEach((item,index)=>{
          if(result.data.orderRefund.refundReason==item.value){
            result.data.orderRefund.refundReasonText=item.name
          }
        })
        result.data.orderRefund.refundAccountText=systemStatus.refundAccountText(result.data.orderRefund.refundAccount);
        this.setData({
          orderRefund: result.data.orderRefund,
          orderRefundGoodsList: result.data.orderRefundGoodsList,
          orderRefundProcessList: result.data.orderRefundProcessList,
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getSelectRefundProcess(options.orderId);
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