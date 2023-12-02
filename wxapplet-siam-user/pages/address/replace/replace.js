var amapFile = require('../../../utils/gaode-libs/amap-wx.js');
var config = require('../../../utils/gaode-libs/config.js');
import toastService from '../../../utils/toast.service';
import https from '../../../utils/http';
var lonlat;
var city, keywords;
const app = getApp();
Page({ 
  data: {
    tips: [],
    isButton: false,
    searchInput:false,
    customItem: ''
  },
  bindInput: function (e) {
    var that = this;
    keywords = e.detail.value;
    if (!keywords) {
      this.setData({
        tips: []
      })
      return
    }
    var key = config.Config.key;
    var myAmapFun = new amapFile.AMapWX({
      key: key
    });
    toastService.showLoading();
    myAmapFun.getInputtips({
      keywords: keywords,
      location: that.data.longitude + "," + that.data.latitude,
      city: city,
      citylimit: true,
      output: "JSON",
      success: function (data) {
        toastService.hideLoading();
        if (data && data.tips) {
          
          that.setData({
            tips: data.tips,
            searchInput:true
          });
        }
      }
    })
  }, 
  getDeliveryAddressTip: function (e) {
    var _this = this;
    keywords = e.currentTarget.dataset.address;
    console.log(keywords);
    var key = config.Config.key;
    var myAmapFun = new amapFile.AMapWX({
      key: key
    });
    toastService.showLoading();
    myAmapFun.getInputtips({
      keywords: keywords.street,
      city: keywords.city,
      citylimit: true,
      output: "JSON",
      success: function (data) {
        if (data && data.tips) {
          console.log(data)
          console.log(keywords)
          // data.tips[0].name='';
          // data.tips[0].address=keywords.street;
          var location;
          for(var i=0;i<data.tips.length;i++){
            console.log(data.tips[i])
            if(data.tips[i].location.length>0){
              
              location=data.tips[i].location;
              break;
            }
          }
          console.log(location)
          app.deliveryAndSelfTaking.location=location?location:keywords.longitude+","+latitude;
          app.deliveryAndSelfTaking.deliveryAddress = e.currentTarget.dataset.address;
          app.deliveryAndSelfTaking.regeoInfo.name='';
          app.deliveryAndSelfTaking.regeoInfo.address=keywords.street;
          //self.prevPage(data.tips[0]);
          if(_this.data.jump_page=='index'){
            var pages = getCurrentPages();
            var prevPage = pages[pages.length - 2];
            prevPage.onLoad();
          }
          wx.navigateBack(1);
        }
      }
    })
  },
  getShopAddressTap(e){
    var _this = this;
    keywords = e.currentTarget.dataset.address;
    console.log(keywords);
    var key = config.Config.key;
    var myAmapFun = new amapFile.AMapWX({
      key: key
    });
    toastService.showLoading();
    myAmapFun.getInputtips({
      keywords: keywords.street,
      city: keywords.city,
      citylimit: true,
      output: "JSON",
      success: function (data) {
        console.log(data)
        if (data && data.tips) {
          let location="";
          for(let i=0;i<data.tips.length;i++){
            if(data.tips[i].location.length>0){
              location=data.tips[i].location;
              app.deliveryAndSelfTaking.location=location;
              console.log(location);
              break;
            }
          }
          
          app.deliveryAndSelfTaking.deliveryAddress = e.currentTarget.dataset.address;
          app.deliveryAndSelfTaking.regeoInfo.name='';
          app.deliveryAndSelfTaking.regeoInfo.address=keywords.street;
          if(_this.data.jump_page=='index'){
            var pages = getCurrentPages();
            var prevPage = pages[pages.length - 2];
            prevPage.getCarouselList();
            prevPage.getRegeo();
          }
          wx.navigateBack(1);
        }
      }
    })
  },
  bindSearch: function (e) {
    keywords = e.currentTarget.dataset.keywords;
    console.log(keywords)
    app.deliveryAndSelfTaking.regeoInfo.name=keywords.name;
    app.deliveryAndSelfTaking.regeoInfo.address=keywords.address;
    app.deliveryAndSelfTaking.location=keywords.location;
    //this.prevPage(keywords);
    if(this.data.jump_page=='index'){
      var pages = getCurrentPages();
      var prevPage = pages[pages.length - 2];
      prevPage.getCarouselList();
      prevPage.getRegeo();
    }
    wx.navigateBack(1);
  },
  prevPage(data) {
    console.log(data)
    var pages = getCurrentPages();
    var prevPage = pages[pages.length - 2]; //上一个页面
    //直接调用上一个页面的 setData() 方法，把数据存到上一个页面中去
    prevPage.setData({
      regeoInfo: data
    });
    if(this.data.jump_page=='index'){
      prevPage.getCarouselList();
      prevPage.getRegeo();
    }wx.navigateBack(1);
  },
  getRegeoAddress() {
    var that = this;
    var key = config.Config.key;
    var myAmapFun = new amapFile.AMapWX({
      key: key
    });
    toastService.showLoading();
    myAmapFun.getRegeo({
      success: function (data) {
        toastService.hideLoading();
        that.setData({
          region: [data[0].regeocodeData.addressComponent.province, data[0].regeocodeData.addressComponent.city, data[0].regeocodeData.addressComponent.district],
          //tips: data[0].regeocodeData.pois,
          sectionLocation:data[0].regeocodeData.pois[0]
        })
        city = data[0].regeocodeData.addressComponent.city;
        //console.log(city)
        that.initData(data[0].regeocodeData.addressComponent.streetNumber.location);
      },
      fail: function (info) {
        //toastService.showLoading();
        //toastService.showToast(info.errMsg)
        // wx.showModal({title:info.errMsg})
      }
    })
  },
  getDeliveryAddressList() {
    toastService.showLoading();
    https.request('/api-order/rest/member/deliveryAddress/list', {
      pageNo: -1,
      pageSize: 10
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        console.log(result.data.records)
        this.setData({
          deliveryAddressList: result.data.records
        })
      }
    })
  },
  getSettingInfo() {
    let that = this;
    wx.getSetting({
      success(res) {
        console.log(res)
        that.setData({
          userLocation: res.authSetting['scope.userLocation'],
          tips:[],
          isOpenSettingInfo:res.authSetting['scope.userLocation']?false:true
        })
        if (res.authSetting['scope.userLocation']) {
          that.getRegeoAddress();
          that.getAutoHeight();
        }
      }
    })
  },
  openSettingInfo() {
    let that = this;
    wx.openSetting({
      success(res) {
        console.log(res)
        that.getSettingInfo();
        // if (res.authSetting['scope.userLocation']) {
        //   that.getRegeoAddress();
        //   that.getAutoHeight();
        // }
      }
    })
  },
  initData(location) {
    var key = config.Config.key;
    var myAmapFun = new amapFile.AMapWX({
      key: key
    });
    myAmapFun.getInputtips({
      location: location,
      city: city,
      citylimit: true,
      success: function (data) {
        if (data && data.tips) {
          that.setData({
            tips: data.tips
          });
        }
      }
    })
  },
  bindRegionChange: function (e) {
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
    var value = { detail: { value: keywords } }
    city = e.detail.value[1];
    this.bindInput(value);
  },
  getAutoHeight() {
    //获取用户手机系统信息
    var winHeight = 0;
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        winHeight = res.windowHeight; //获取高度
        //获取class为settlement-view并减去这个高度，自适应scrollview的高度
        const query = wx.createSelectorQuery()
        query.select('.section').boundingClientRect()
        query.selectViewport().scrollOffset()
        query.exec(function (res) {
          that.setData({
            winHeight: winHeight - (res[0].height),
          })
        })
      }
    });
  },
  onShow: function (e) {
    this.getSettingInfo();
    // this.getRegeoAddress();
    // this.getAutoHeight();
  },
  onLoad(options){
    this.setData({
      jump_page:options.jump_page
    })
    if(options.addressType){
      this.setData({
        addressType:options.addressType,
        shopId:options.shopId
      })
    }
    
    this.getDeliveryAddressList();
  }
})