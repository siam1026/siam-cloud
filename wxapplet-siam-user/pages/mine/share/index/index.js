import https from '../../../../utils/http';
import toastService from '../../../../utils/toast.service';
import authService from '../../../../utils/auth';
import GlobalConfig from '../../../../utils/global-config';
import dateHelper from '../../../../utils/date-helper';
//获取应用实例
const app = getApp();
// var pageNo = -1, pageSize = 20, prevList = [];
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [],
    activityRulesDialog: false,
    loading: false,
    color: '#000',
    background: 'rgba(0,0,0,0)',
    show: true,
    animated: false,
    extClass: 'weui-navigation-bar-custom',
    goodShareDialog: false,
    createGoodImgDialog: false,
    indicatorDots: true,
    autoplay: true,
    interval: 10000,
    duration: 1000,
    //beforeColor: "white",//指示点颜色,
    afterColor: "#f1a142", //当前选中的指示点颜色  
    opacity: 0.4,
    saveDialogShow: false,
    buttons: [{
      text: '取消'
    }, {
      text: '上一张'
    }, {
      text: '下一张'
    }, {
      text: '保存'
    }],
    saveIndex: 0,
    canvasHidden: false
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
  bindActivityRules() {
    this.setData({
      activityRulesDialog: true
    });
    this.getInviteFriendsActivityRule();
  },
  getDeliveryAddressList() {
    toastService.showLoading();
    https.request('/api-user/rest/memberInviteRelation/getMemberListByInviterId', {
      pageNo: -1,
      pageSize: 10
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        this.setData({
          inviteRelationList: result.data.records,
          total: result.data.total
        })
      }
    })
  },
  getAdvertisementList() {
    toastService.showLoading();
    https.request('/api-goods/rest/member/advertisement/list', {
      type: 4,
      pageNo: -1,
      pageSize: 20
    }).then(result => {
      if (result.success) {
        var viewImages = [];
        result.data.records.forEach(function (item, index) {
          console.log(item)
          item.mainImageUrl = GlobalConfig.ossUrl + item.imagePath;
          viewImages.push(item.mainImageUrl);
        })
        this.setData({
          advertisementList: result.data.records,
          viewImages: viewImages
        });
        
        //this.getAccessToken();
      }
      toastService.hideLoading();
    })
  },
  getInviteFriendsActivityRule() {
    toastService.showLoading();
    https.request('/api-goods/rest/setting/selectCurrent', {}).then(result => {
      if (result.success) {
        console.log(result);
        let inviteFriendsActivityRule=result.data.inviteFriendsActivityRule.replace("↵","\n");
        this.setData({
          inviteFriendsActivityRule:inviteFriendsActivityRule
        })
      }
      toastService.hideLoading();
    })
  },
  getAccessToken(e) {
    var appid = 'wx2e1a8193d3ed12fe'; //填写微信小程序appid 
    var secret = '2774e3a86dc30fbf1ac63d81b56f2291'; //填写微信小程序secret 
    var _this = this;
    wx.request({
      url: 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=' + appid + '&secret=' + secret,
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        console.log(res)
        _this.getCode(res.data.access_token);
      }
    })

  },
  getCode(ACCESS_TOKEN) {
    console.log(ACCESS_TOKEN);
    var _this = this;
    wx.request({
      url: 'https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=' + ACCESS_TOKEN,
      data: {
        scene: "id=18"
      },
      method: "POST",
      responseType: 'arraybuffer', //这个是转化成base64需要加的
      success: function (res) {
        console.log(res);
        var array = wx.base64ToArrayBuffer(res.data)
        var base64 = wx.arrayBufferToBase64(res.data)
        console.log(base64)
        _this.setData({
          qrCode: "data:image/jpeg;base64," + base64
        })
      }
    })
  },
  bindReward(e) {
    wx.navigateTo({
      url: '../reward/reward',
    })
  },
  goodShareDialog() {
    this.setData({
      goodShareDialog: true
    })
  },
  createGoodImgDialog() {
    this.getAdvertisementList();
    this.setData({
      goodShareDialog: false,
      createGoodImgDialog: true
    })
  },
  // 滑动切换tab
  bindSlideChange: function (e) {
    console.log(e)
    this.setData({
      saveIndex: e.detail.current
    });
  },
  saveDialogShow() {
    var _this = this;
    toastService.showLoading("保存中...");
    wx.getImageInfo({
      src: _this.data.advertisementList[_this.data.saveIndex].mainImageUrl,
      success: function (res) {
        var path = res.path;
        wx.saveImageToPhotosAlbum({
          filePath: path,
          success(res) {
            toastService.hideLoading();
            toastService.showSuccess("保存相册成功");
            _this.saveImageToPhotosAlbum();
            console.log(res)
          },
          fail(res) {
            toastService.hideLoading();
            toastService.showError("保存相册失败");
            console.log(res)
          }
        })
      }
    });
  },
  createGoodImgSave(e) {
    console.log(e)
    if (e.detail.index == 0) {
      console.log("点击了", e.detail.item.text);
      this.setData({
        saveDialogShow: false,
        saveIndex: 0
      })
    }
    if (e.detail.index == 1) {
      console.log("点击了", e.detail.item.text);
      this.setData({
        saveIndex: this.data.saveIndex == 0 ? this.data.advertisementList.length - 1 : this.data.saveIndex - 1
      })
    }
    if (e.detail.index == 2) {
      console.log("点击了", e.detail.item.text);
      this.setData({
        saveIndex: this.data.saveIndex == this.data.advertisementList.length - 1 ? 0 : this.data.saveIndex + 1
      })
    }
    if (e.detail.index == 3) {
      console.log("点击了", e.detail.item.text);
      wx.getImageInfo({
        src: this.data.advertisementList[this.data.saveIndex].mainImageUrl,
        success: function (res) {
          var path = res.path;
          wx.saveImageToPhotosAlbum({
            filePath: path,
            success(res) {
              toastService.showToast("保存相册成功");
              console.log(res)
            },
            fail(res) {
              toastService.showToast("保存相册失败");
              console.log(res)
            }
          })
        }
      })
    }
  },
  viewImage: function (e) {
    var index = e.currentTarget.dataset.index; //获取data-currentimg
    var src = this.data.viewImages[index];

    //图片预览
    wx.previewImage({
      current: src, // 当前显示图片的http链接
      urls: this.data.viewImages // 需要预览的图片http链接列表
    })
  },
  getHeight() {
    //获取用户手机系统信息
    var that = this;
    wx.getSystemInfo({
      success: function(res) {
        let winHeight = res.windowHeight; //获取高度
        that.setData({
          winHeight: winHeight,
          winWidth:res.windowWidth
        });
        
      }
    });
  },
  //保存至相册
  saveImageToPhotosAlbum: function () {
    console.log("保存中");
    this.getHeight();
    var that = this;
    //2. canvas绘制文字和图片
    const ctx = wx.createCanvasContext('share');
    //这里很重要，主要就是布局
    console.log(this.data.advertisementList[this.data.saveIndex].mainImageUrl)
    const mainImageUrl = this.data.advertisementList[this.data.saveIndex].mainImageUrl;
    wx.getImageInfo({
      src: mainImageUrl,
      success(res) {
        console.log(res)
        ctx.drawImage(res.path, 0, 0, res.width/3, res.height/3);
        ctx.drawImage(that.data.qrCode, that.data.winWidth-140, (res.height/3), 100, 100);
        ctx.fillText('姓名：江鹏', 10, (res.height/3)+40);
        ctx.fillText('人物匹配：猛男', 10, (res.height/3)+60);
        ctx.fillText('近期预言：给我涨，给我红，冲冲冲！', 10, (res.height/3)+80);
        ctx.setFontSize(13);
        ctx.setFillStyle('#5e7436');
        ctx.stroke()
        ctx.draw(false, function () {
          // 3. canvas画布转成图片
          wx.canvasToTempFilePath({
            x: 0,
            y: 0,
            width: that.data.winWidth,
            height: that.data.winHeight,
            destWidth: that.data.winWidth,
            destHeight: that.data.winHeight,
            canvasId: 'share',
            success: function (res) {
              console.log(res);
              that.setData({
                shareImgSrc: res.tempFilePath,
                canvasHidden:false
              })
              if (!res.tempFilePath) {
                wx.showModal({
                  title: '提示',
                  content: '图片绘制中，请稍后重试',
                  showCancel: false
                })
              }
              //4. 当用户点击分享到朋友圈时，将图片保存到相册
              wx.saveImageToPhotosAlbum({
                filePath: res.tempFilePath,
                success(res) {
                  console.log(res);
                  wx.showModal({
                    title: '图片保存成功',
                    content: '图片成功保存到相册了，去发圈噻~',
                    showCancel: false,
                    confirmText: '好哒',
                    confirmColor: '#72B9C3',
                    success: function (res) {
                      if (res.confirm) {
                        console.log('用户点击确定');
                      }
                      that.setData({
                        canvasHidden: true
                      })
                    }
                  })
                }
              })
            },
            fail: function (res) {
              console.log(res)
            }
          })
        });
      }
    })


  },
  /**
   * 生命周期函数--监听页面加载 
   */
  onLoad: function (options) {
    console.log(options.inviterId)
    this.setData({
      inviterId: options.inviterId
    });

    this.setData({
      statusBarHeight: app.globalData.systemInfoSync.statusBarHeight * 2
    })
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
    authService.checkIsLogin().then(result => {
      if (result) {
        this.getDeliveryAddressList();
        this.getUserInfo();
        return
      }
      app.checkIsAuth("scope.userInfo");
    });
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
  getTimestamp(){
    var timestamp=dateHelper.getTimestamp();
    console.log(timestamp)
    this.setData({
      timestamp:timestamp
    })
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function (options) {
    // console.log(123);
    // var memberId = this.data.data.id;
    var inviterId = this.data.userInfo.id;
    console.log(inviterId)
    var timestamp=dateHelper.getTimestamp();
    console.log(`https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/bussiness/share-invite/share_wx.png?v=${timestamp}`)
    var shareObj = {
      title: "你的好友送了你一张3折优惠券，快去领取吧～",
      // path: '/pages/insert-share-invite/insert-share-invite?inviterId=' + inviterId,
      path: '/pages/login/choose/choose?inviterId=' + inviterId,
      //自定义图片路径，可以是本地文件路径、代码包文件路径或者网络图片路径，支持PNG及JPG，不传入 imageUrl 则使用默认截图。显示图片长宽比是 5:4
      imageUrl: `https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/data/images/bussiness/share-invite/share_wx.png?v=${timestamp}`
    };
    // 默认是当前页面，必须是以‘/’开头的完整路径};
    // 来自页面内的按钮的转发
    // if (options.from == 'button') {
    //   shareObj = {
    //     //默认是小程序的名称(可以写slogan等)
    //     title: "周二了，要不要来杯陨石拿铁？我请你~", 
    //     imageUrl: '/assets/logo/logo.jpg',
    //     success: function (res) {
    //       if (res.errMsg == 'shareAppMessage:ok') {

    //       }
    //     },
    //     fail: function () {
    //       if (res.errMsg == 'shareAppMessage:fail cancel') {
    //         //用户取消转发

    //       } else if (res.errMsg == 'shareAppMessage:fail') {
    //         //转发失败，其中 detail message 为详细失败信息

    //       }
    //     }
    //   };
    //   shareObj.path = '/pages/invite-friends/invite-friends?memberId=' + this.data.data.id;
    // }
    return shareObj;
  }
})