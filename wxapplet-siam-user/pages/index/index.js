import GlobalConfig from '../../utils/global-config';
const show = require("../../utils/toast.service.js");
import https from '../../utils/http';
var amapFile = require('../../utils/gaode-libs/amap-wx.js');
var config = require('../../utils/gaode-libs/config.js');
import authService from '../../utils/auth';
//获取应用实例 
const app = getApp()

Page({
  data: {
    indicatorDots: true,
    autoplay: true,
    interval: 5000,
    duration: 1000,
    //beforeColor: "white",//指示点颜色,
    afterColor: "#f1a142", //当前选中的指示点颜色  
    previousmargin: '60px', //前边距
    nextmargin: '60px', //后边距
    opacity: 0.4,
    extClass: 'weui-dialog-ext-index',
    scrollTop: false,
    noDataTip: "../../assets/common/no-data.png",
    shopList: [],
    isActivityDialog:false
  },
  onLoad: function () {
    this.setData({
      statusBarHeight: app.globalData.systemInfoSync.statusBarHeight * 2
    });
    this.getCarouselList();
    this.getRegeoInit();
  },
  onShow: function () {
    //判断是否显示新用户弹窗
    // app.getMemberInfo().then(result => {
    //   if (result.isNewPeople && result.isRemindNewPeople) {
    //     this.openConfirm();
    //   }
    // });
    //app.getShoppingCarNumber();
  },
  onPullDownRefresh(){
    this.getCarouselList();
    this.getRegeo();
    setTimeout(outtime=>{
      // 隐藏导航栏加载框
      wx.hideNavigationBarLoading();
      // 停止下拉动作
      wx.stopPullDownRefresh();
      clearTimeout(outtime)
    },1000);
  },
  getRegeo() {
    var self = this;
    var key = config.Config.key;
    var myAmapFun = new amapFile.AMapWX({
      key: key
    });
    myAmapFun.getRegeo({
      success: function (getRegeo) {
        console.log(getRegeo)
        if (!app.deliveryAndSelfTaking.regeoInfo) {
          self.setData({
            regeoInfo: getRegeo[0].regeocodeData.pois[0]
          });
          app.deliveryAndSelfTaking.location=getRegeo[0].longitude+","+getRegeo[0].latitude;
          app.deliveryAndSelfTaking.regeoInfo=getRegeo[0].regeocodeData.pois[0]
        }else{
          app.deliveryAndSelfTaking.location=app.deliveryAndSelfTaking.location;
          self.setData({
            regeoInfo: app.deliveryAndSelfTaking.regeoInfo
          });
        }
        show.showLoading();
        self.getShopList();
        self.getRecommendGoodsList();
      },
      fail: function (info) {
        //toastService.showLoading();
        // wx.showModal({title:info.errMsg})
      }
    })
  },
  getRegeoInit() {
    var self=this;
    if (!app.deliveryAndSelfTaking.regeoInfo) {
      var addressInfo={
        'name': "麓谷小镇",
        'address': "岳麓大道尖山路口北300米",
        'location': "112.885538,28.232363"
      };
      self.setData({
        regeoInfo: addressInfo
      });
      app.deliveryAndSelfTaking.location=addressInfo.location;
      app.deliveryAndSelfTaking.regeoInfo=addressInfo
    }else{
      app.deliveryAndSelfTaking.location=app.deliveryAndSelfTaking.location;
      self.setData({
        regeoInfo: app.deliveryAndSelfTaking.regeoInfo
      });
    }
    show.showLoading();
    self.getShopList();
    self.getRecommendGoodsList();
      
  },
  shoppingAddressTap() {
    wx.navigateTo({
      url: `../address/replace/replace?jump_page=index`,
    })
  },
  showDialog: function () {
    this.setData({
      dialogvisible: true
    })
  },
  gotoShop(e) {
    app.isRemindNewPeople().then(result => {
      if (result) {
        this.setData({
          dialogShow: false,
          maskClosable: true,
        });
      }
      wx.switchTab({
        url: '../menu/index/index',
      })
    });
  },
  businessTap(e) {
    wx.navigateTo({
      url: '../menu/index/index?id=' + e.currentTarget.dataset.id,
    })
  },
  searchBusinessTap(e) {
    console.log(this.data)
    wx.navigateTo({
      url: '../menu/search/search?location=' + app.deliveryAndSelfTaking.location,
    })
  },
  commodityDetailTap(e) {
    wx.navigateTo({
      url: '../menu/detail/detail?id=' + e.currentTarget.dataset.id + "&shopId=" + e.currentTarget.dataset.shopid,
    })
  },
  carouseCommodityDetailTap(e) {
    let _this=this;
    authService.checkIsLogin().then(result => {
      console.log(result)
      if (result) {
        _this.getUserInfo(e);
        return;
      }
      app.checkIsAuth("scope.userInfo");
    });
  },
  getUserInfo: function (e) {
    https.request('/api-user/rest/member/getLoginMemberInfo', {}).then(result => {
      if (result.success) {
        wx.navigateTo({
          url: e.currentTarget.dataset.imagelinkurl
        })
      }else{
        app.checkIsAuth("scope.userInfo");
      }
    })
  },
  bindInDevelopment() {
    app.bindInDevelopment();
  },
  getRecommendGoodsList() {
    https.request('/api-goods/rest/goods/homePage/recommendGoodsList',{
      pageNo: -1,
      pageSize: 5,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      if (result.success) {
        result.data.forEach(function (item, index) {
          item.mainImage = GlobalConfig.ossUrl + item.mainImage;
        })
        this.setData({
          recommendGoodsList: result.data
        });
      }
    })
  },
  getShopList() {
    if(app.deliveryAndSelfTaking.location){
      https.request('/api-merchant/rest/shop/list', {
        pageNo: -1,
        pageSize: 5,
        position: app.deliveryAndSelfTaking.location
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
          })
          this.setData({
            shopList: result.data.records
          })
          authService.checkIsLogin().then(result => {
            if (!result) {
              return
            }
            this.getShoppingCartList();
          });
        }
      })
    };
  },
  getShoppingCartList() {
    this.data.shopList.forEach((item, index) => {
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
            shopList: this.data.shopList
          })
        }
      })
    });
  },
  //获取满减规则
  getFullReductionRule(shopList) {
    shopList.forEach((item, index) => {
      https.request('/api-promotion/rest/fullReductionRule/list', {
        pageNo: -1,
        pageSize: 1,
        shopId: item.id
      }).then(result => {
        if (result.success) {
          shopList[index].reductionRules = result.data.records;
          this.setData({
            shopList: shopList
          })
        }
      })
    })
  },
  getCarouselList() {
    https.request('/api-promotion/rest/advertisement/list', {
      type: 1,
      pageNo: -1,
      pageSize: 20
    }).then(result => {
      if (result.success) {
        result.data.records.forEach(function (item, index) {
          item.imagePath = GlobalConfig.ossUrl + item.imagePath;
        })
        this.setData({
          carouselUrls: result.data.records
        });
      }
    })
  },
  openConfirm() {
    this.setData({
      dialogShow: true,
      maskClosable: false,
    })
  },
  close() {
    app.isRemindNewPeople().then(result => {
      if (result) {
        this.setData({
          dialogShow: false,
          maskClosable: true,
        });
      }
    });
  },
  isPromotionTap(e){
    let shopIndex=e.currentTarget.dataset.shopindex;
    console.log(shopIndex)
    this.setData({
      isActivityDialog:true,
      shopIndex:shopIndex
    })
  },
  onHide() {
    this.setData({
      dialogShow: false,
      maskClosable: true
    })
  }
})