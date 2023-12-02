import https from '../../../../../utils/http';
import toastService from '../../../../../utils/toast.service';
import utils from '../../../../../utils/util';
import base64 from '../../../../../utils/base64';
Page({

   /**
    * 页面的初始数据
    */
   data: {
      isTip: false,
      showDialog: false,
      showPayPwdInput: false, //是否展示密码输入层
      pwdVal: '', //输入的密码
      payFocus: false, //文本框焦点
      adjustPosition: false,
      holdKeyboard: true,
      serverFeeDialog:false,
      isSatisfy:true,
      selectCurrentDialog:false
   },
   selectCurrentSetting(){
      if(this.data.current){
        this.setData({
          selectCurrentDialog:true
       });
       return
      }
      https.request('/api-goods/rest/setting/selectCurrent', {}).then(result => {
         if (result.success) {
          result.data.commissionRule=result.data.commissionRule.replace("↵","\n")
            this.setData({
               current:result.data,
               selectCurrentDialog:true
            })
         }
      })
   },
   getUserInfo: function (e) {
      https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
         if (result.success) {
            this.setData({
               userInfo: result.data
            })
         }
      })
   },
   bindInputMoney(e) {
      // console.log(e)
      let value = e.detail.value;
      if (!utils.verifyNumber(value)) {
         this.setData({
            inputMoney: ''
         })
         return
      }
      if (value.length >= 2) {
         if(value==0||value==0.0){
            if (value[1] == 0) {
               this.setData({
                  inputMoney: this.data.userInfo.inviteRewardAmount
               })
               return
            }
         }
      }

      this.data.isTip = false;
      // console.log(this.data.inputMoney)
      var valueSpilt=value.split(".");
      if(valueSpilt.length>2){
         value = this.data.inputMoney;
      }else{
         if (valueSpilt[1]) {
            if (valueSpilt[1].length > 2) {
               value = this.data.inputMoney;
            }
         }
         if (value > this.data.userInfo.inviteRewardAmount) {
            this.data.isTip = true;
         }
      }

      this.data.isTip = value >= 0 ? true : false;

      this.setData({
         inputMoney: value,
         isTip: this.data.isTip
      })
   },
   bindAllWithDrawal(e) {
      this.setData({
         inputMoney: this.data.userInfo.inviteRewardAmount,
         isTip: this.data.userInfo.inviteRewardAmount > 0 ? true : false
      })
   },
   bindWithDrawal() {
      if (!this.data.inputMoney && !this.data.isTip) {
         toastService.showToast("输入金额超过奖励金额");
         return
      }
      var self = this;
      wx.checkIsSupportSoterAuthentication({
         success(authentication) {
            console.log("获取本机支持的 SOTER 生物认证方式", authentication);
            // res.supportMode = [] 不具备任何被SOTER支持的生物识别方式
            // res.supportMode = ['fingerPrint'] 只支持指纹识别
            // res.supportMode = ['fingerPrint', 'facial'] 支持指纹识别和人脸识别
            let supportMode = '';
            let key = 0;
            for (let key in authentication.supportMode) {
               if (authentication.supportMode[key] == "fingerPrint") {
                  supportMode = '请使用指纹解锁';
               }
               if (authentication.supportMode[key] == "facial") {
                  supportMode = '请使用面部解锁';
               }
            }
            console.log("SOTER支持的生物识别方式={},{}", supportMode, authentication.supportMode[key])
            console.log("SOTER支持的生物识别方式=", authentication.supportMode[key]);
            wx.checkIsSoterEnrolledInDevice({
               checkAuthMode: authentication.supportMode[key],
               success(device) {
                  console.log("获取设备内是否录入如指纹等生物信息的接口", device);
                  if (device.isEnrolled) {
                     console.log("开始 SOTER 生物认证");
                     wx.startSoterAuthentication({
                        requestAuthModes: authentication.supportMode,
                        challenge: self.data.inputMoney,
                        authContent: supportMode,
                        success(res) {
                           console.log("SOTER 生物认证成功", res);
                           https.request('/api-user/rest/member/memberWithdrawRecord/inviteRewardAmount/insert', {
                              withdrawAmount: self.data.inputMoney,
                              paymentMode: 1
                           }).then(result => {
                              if (result.success) {
                                 toastService.showSuccess(result.message);
                                 setTimeout(time => {
                                    wx.navigateBack(1);
                                    clearTimeout(time);
                                 }, 2000);
                              }
                           });
                        }
                     });
                  }
               }
            });
         }
      });
   },
   bindWithDrawalPWd(e) {
      if(this.data.inputMoney<1){
         toastService.showToast("奖励金累计到(≥)1.00元才可以提现");
         return
      }
      
      this.selectCurrent();
   },
   bindContinueWithDrawalPWd(){
      this.setData({
         serverFeeDialog:false
      });
      if (!this.data.userInfo.realName) {
         toastService.showModal(null, "请先确认真实姓名后操作", function confirm() {
            wx.navigateTo({
               url: '../../../../mine/userinfo/userinfo',
            });
         }, null);
         return;
      }
      if (!this.data.inputMoney) {
         toastService.showToast("输入金额为空");
         return
      }
      if (this.data.inputMoney > this.data.userInfo.inviteRewardAmount) {
         toastService.showToast("输入金额为空输入金额超过奖励金额");
         return
      }
      if (!this.data.userInfo.paymentPassword) {
         toastService.showModal("", "请先设置平台支付密码!", function comfirm() {
            wx.navigateTo({
               url: '../../../../mine/security/index/index',
            });
         }, function () {
            toastService.showToast("设置平台支付密码之后提现");
         });
         return
      }
      toastService.showLoading();
      this.setData({
         showPayPwdInput: true,
         payFocus: true
      });
      toastService.hideLoading();
   },
   inputPwd: function (e) {
      this.setData({
         pwdVal: e.detail.value
      });
      if (e.detail.value.length >= 6) {
         toastService.showLoading("正在加载...");
         this.withDrawal();
      }
   },
   withDrawal() {
      var password = base64.encode(this.data.pwdVal);
      this.setData({
         pwdVal: '',
         payFocus: true
      });
      https.request('/api-user/rest/member/memberWithdrawRecord/inviteRewardAmount/insert', {
         withdrawAmount: this.data.inputMoney,
         paymentMode: 1,
         paymentPassword: password
      }).then(result => {
         toastService.hideLoading();
         if (result.success) {
            this.hidePwdLayer();
            toastService.showSuccess(result.message);
            setTimeout(time => {
               wx.navigateBack(1);
               clearTimeout(time);
            }, 2000);
         }
      });
   },
   selectCurrent() {
      toastService.showLoading("正在加载");
      https.request('/api-goods/rest/setting/selectCurrent', {}).then(result => {
         toastService.hideLoading();
         if (result.success) {
            // console.log(this.data.inputMoney)
            let satisfyFee=utils.toFixed(Number(this.data.inputMoney)*(result.data.memberWithdrawFee/100));
            let memberWithdrawFeeAfter=memberWithdrawFeeAfter+this.data.inputMoney;
            let isSatisfyFee=true;
            if(this.data.userInfo.inviteRewardAmount-satisfyFee<this.data.inputMoney){
               isSatisfyFee=false;
            }
            this.setData({
               satisfyFee:satisfyFee,
               isSatisfyFee:isSatisfyFee,
               memberWithdrawFeeAfter:utils.toFixed(this.data.inputMoney-satisfyFee),
               serverFeeDialog:true
            })
         }
      });
   },
   /**
    * 获取焦点
    */
   getFocus: function () {
      this.setData({
         payFocus: true
      });
   },
   bindtouchend(e) {
      console.log("触摸结束");
      this.setData({payFocus: true});
   },
   balancePayFail() {
      toastService.showError("提现失败", true);
      this.hidePwdLayer();
   },
   hidePwdLayer() {
      this.setData({showPayPwdInput: false,payFocus: false,pwdVal: ''});
   },
   showPwdLayer() {
      this.setData({showPayPwdInput: true,payFocus: true,pwdVal: ''});
   },
   /**
    * 隐藏支付密码输入层
    */
   forgetThePassword: function () {
      wx.navigateTo({
         url: "../../../../mine/security/index/index"
      })
   },
   bindWithdrawRecord(){
      wx.navigateTo({
        url: '../detail/detail',
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
      console.log(this.data.showPayPwdInput)
      if (this.data.showPayPwdInput) {
         this.setData({ payFocus: true,pwdVal:'' });
      }
      this.getUserInfo();
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