import GlobalConfig from '../../../utils/global-config';
import https from '../../../utils/http';
import authService from '../../../utils/auth';
import toastService from '../../../utils/toast.service';
import utilHelper from '../../../utils/util';
import dateHelper from '../../../utils/date-helper';
//获取应用实例 
const app = getApp()
var totalPrice = 0, totalNum = 0;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    carouselUrls: [],
    indicatorDots: true,
    autoplay: true,
    interval: 5000,
    duration: 1000,
    //beforeColor: "white",//指示点颜色,
    afterColor: "white", //当前选中的指示点颜色
    commodityNum: 1,
    totalPrice: 0,
    shoppingCartDialog: false,
    specificationsDialog: false,
    userInfo: app.globalData.loginUserInfo,
    priceDifference: 0,
    shoppingCartList: [],

  },
  getTimestamp(){
    var timestamp=dateHelper.getTimestamp();
    console.log(timestamp)
    this.setData({
      timestamp:timestamp
    })
  },
  radioChange(e) {
    //获取复选框选中的下标值
    var checkValue = e.detail.value;
    //获取第一级分类的下标值
    let firstIndex = e.currentTarget.dataset.firstindex;
    //获取所有分类信息
    let specList = this.data.specList;
    //console.log(specList)
    //遍历分类信息给第一级分类为false，提交的时候对应各级分类
    for (var j in specList[firstIndex]) {
      specList[firstIndex][j].checked = false;
    }
    //给选中的第二级分类的checked设置为true
    for (var i in checkValue) {
      specList[firstIndex][checkValue[i]].checked = true;
      //console.log(specList[firstIndex][checkValue[i]])
    }
    let price = this.data.data.price;
    for (let key in specList) {
      for (let keyof in specList[key]) {
        //console.log(specList[key][keyof].price)
        if (specList[key][keyof].checked) {
          price = price + specList[key][keyof].price;
        }
      }
    }
    // let goodsSpecs = {};
    // for(let i=0;i<specList.length;i++){
    //   for (let j = 0; j < specList[i].length;j++){
    //     //拼接查询规格等的json数据格式，查询商品规格等对应的价格
    //     if (specList[i][j].checked) {
    //       goodsSpecs[specList[i].spec] = specList[i][j].name;
    //     }
    //   }
    // }
    let totalPrice = price * this.data.commodityNum;
    let fullPriceReduction = 0, fullReductionRuleName = "", fullPriceReductionIsHidden = false, limitedPrice = 0;
    for (let i = 0; i < this.data.fullReductionRuleList.length; i++) {
      //console.log(this.data.fullReductionRuleList[i].limitedPrice)
      if (totalPrice >= this.data.fullReductionRuleList[i].limitedPrice) {
        if (this.data.fullReductionRuleList[i].limitedPrice > limitedPrice) {
          limitedPrice = this.data.fullReductionRuleList[i].limitedPrice;
          fullPriceReduction = (totalPrice + this.data.data.packingCharges) - this.data.fullReductionRuleList[i].reducedPrice;
          fullReductionRuleName = this.data.fullReductionRuleList[i].name;
          fullPriceReductionIsHidden = true;
        }
      }
    }
    this.setData({
      specList: specList,
      priceAfter: price,
      totalPrice: utilHelper.toFixed(totalPrice, 2),
      fullPriceReduction: utilHelper.toFixed(fullPriceReduction, 2),
      fullPriceReductionIsHidden: fullPriceReductionIsHidden,
      fullReductionRuleName: fullReductionRuleName
    })
  },
  //事件处理函数
  /*点击减号*/
  bindMinus: function (e) {
    toastService.showLoading();
    var that = this;
    let cartId = e.currentTarget.dataset.cartid;
    let number = e.currentTarget.dataset.number;
    //点击减号后,当前商品的数量小于1,就进行删除该商品
    //重新计算被选中的商品的总金额
    totalPrice = 0;
    if (number == 1) {
      toastService.hideLoading();
      toastService.showModal(null, "确定不要这个了吗？",
        function confirm() {
          toastService.showLoading();
          that.updateNumber(cartId, 1, 0, function callback() {
            totalNum--;
            if (that.data.shoppingCartList.length == 1) {
              that.setData({
                shoppingCartDialog: false
              });
            }
            that.getShoppingCartList();
          })
        }
      )
      return
    }
    this.updateNumber(cartId, 1, 0, function callback() {
      totalNum--;
      that.getShoppingCartList();
    })
  },
  /*点击加号*/
  bindPlus: function (e) {
    toastService.showLoading();
    var self = this;
    let numList = e.currentTarget.dataset.num.split(",");
    let items = this.data.shoppingCartList;
    totalPrice = 0;
    items[numList[0]].number = Number(numList[1]) + 1; //当前商品的数量+1
    if (items[numList[0]].disable) {
      return
    }
    this.updateNumber(items[numList[0]].id, 1, 1, function callback() {
      //重新计算被选中的商品的总金额
      totalNum++;
      self.getShoppingCartList();
    })
  },

  updateNumber(id, number, type, callbak) {
    https.request('/api-goods/rest/member/shoppingCart/updateNumber', {
      id: id,
      number: number,
      type: type
    }).then(result => {
      if (result.success) {
        callbak();
      }
    })
  },
  insertShoppingCart(e) {
    authService.checkIsLogin().then(result => {
      toastService.showLoading();
      if (result) {
        let goodsSpecs = {};
        let specList = this.data.specList;
        for (let key in specList) {
          for (let keyof in specList[key]) {
            //拼接查询规格等的json数据格式，查询商品规格等对应的价格
            if (specList[key][keyof].checked) {
              goodsSpecs[key] = specList[key][keyof].name;
            }
          }
        }
        //console.log(goodsSpecs)
        let data = {
          goodsId: this.data.goodsId,
          specList: JSON.stringify(goodsSpecs),
          shopId: this.data.shopInfo.shop.id
        }
        https.request('/api-goods/rest/member/shoppingCart/insert', data, authService.getToken()).then(result => {
          if (result.success) {
            this.setData({
              specificationsDialog: false
            });
            this.getShoppingCartList();
            //wx.navigateBack(1)

          }
        })
        return;
      }
      app.checkIsAuth("scope.userInfo");
    })
  },
  goToPay() {
    let self = this;
    authService.checkIsLogin().then(result => {
      if (result) {
        //判断店铺是否打烊
        let startTime = this.data.shopInfo.shop.startTime;
        let endTime = this.data.shopInfo.shop.endTime;
        let isOperating = this.data.shopInfo.shop.isOperating;
        app.getIsBusiness(startTime, endTime, isOperating).then(result => {
          if (!result) {
            return
          }
          self.toPay();
        });
        return
      }
      app.checkIsAuth("scope.userInfo");
    })
  },
  toPay() {
    var list = this.data.shoppingCartList;
    var orderDetail = {};
    orderDetail.actualPrice = this.data.totalPrice;
    orderDetail.shopId = this.data.shopId;
    orderDetail.orderDetailList = [];
    for (var key in list) {
      orderDetail.orderDetailList.push({
        goodsId: list[key].goodsId,
        specList: list[key].specList,
        number: list[key].number,
        goodsName: list[key].goodsName,
        restructure: list[key].restructure,
        price: list[key].price,
        id: list[key].id,
        packingCharges: list[key].packingCharges
      });
    }
    wx.navigateTo({
      url: '../pay/pay?orderDetail=' + JSON.stringify(orderDetail) + "&payType=store",
    })
  },
  autoHeigth() {
    var that = this;
    //获取用户手机系统信息
    wx.getSystemInfo({
      success: function (res) {
        let winHeight = res.windowHeight; //获取高度
        //获取class为settlement-view并减去这个高度，自适应scrollview的高度
        const query = wx.createSelectorQuery()
        query.select('.shopping-cart-detail').boundingClientRect()
        query.selectViewport().scrollOffset()
        query.exec(function (res) {
          that.setData({
            winHeight: winHeight - (res[0].height + 5)
          })
        })
      }
    });
  },
  closeShoppingCart: function () {
    this.setData({
      shoppingCartDialog: false
    });
  },
  openShoppingCart() {
    authService.checkIsLogin().then(result => {
      if (result) {
        if (this.data.shoppingCartList.length > 0) {
          this.setData({
            shoppingCartDialog: this.data.shoppingCartDialog ? false : true
          });
        }
        return
      }
      app.checkIsAuth("scope.userInfo");
    });
  },
  openSpecifications(e) {
    this.setData({
      specificationsDialog: true,
      goodsId: e.currentTarget.dataset.goodsid
    });
    this.getCommodityDetails(e.currentTarget.dataset.goodsid);
  },
  getShopInfo() {
    https.request('/api-goods/rest/shop/detail', {
      id: this.data.shopId,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      if (result.success && result.data) {
        
        this.getFullReductionRule(result.data)
      }
    })
  },
  //获取满减规则
  getFullReductionRule(shopInfo) {
    console.log(shopInfo)
    let fullPriceReduction = 0,
      fullReductionRuleName = "",
      fullPriceReductionIsHidden = false,
      limitedPrice = 0;
    for (let i = 0; i < shopInfo.fullReductionRuleList.length; i++) {
      if (totalPrice + this.data.packingCharges >= shopInfo.fullReductionRuleList[i].limitedPrice) {
        if (shopInfo.fullReductionRuleList[i].limitedPrice > limitedPrice) {
          limitedPrice = shopInfo.fullReductionRuleList[i].limitedPrice;
          fullPriceReduction = totalPrice - shopInfo.fullReductionRuleList[i].reducedPrice;
          fullReductionRuleName = shopInfo.fullReductionRuleList[i].name;
          fullPriceReductionIsHidden = true;
        }
      }
    }
    var shopWithinImgs = shopInfo.shop.shopWithinImg.split(",");
    for (let i = 0; i < shopWithinImgs.length; i++) {
      shopWithinImgs[i] = GlobalConfig.ossUrl + shopWithinImgs[i];
    }
    shopInfo.shop.shopLogoImg = GlobalConfig.ossUrl + shopInfo.shop.shopLogoImg;
    shopInfo.shop.shopWithinImgs = shopWithinImgs;
    //shopInfo.shop.isOperating = false;
    //shopInfo.shop.isOutofDeliveryRange = true;
    app.getIsItNear(shopInfo.shop.startTime, shopInfo.shop.endTime);
    //isOutofDeliveryRange true为超出范围
    //isOperatingOfShop false为打烊
    this.setData({
      shopInfo: shopInfo,
      fullPriceReduction: utilHelper.toFixed(fullPriceReduction, 2),
      fullReductionRuleName: fullReductionRuleName,
      fullPriceReductionIsHidden: fullPriceReductionIsHidden,
      isItNear:app.deliveryAndSelfTaking.isItNear
    });
    this.getShoppingCartList();
  },
  getShoppingCartList() {
    https.request('/api-goods/rest/member/shoppingCart/list', {
      shopId: this.data.shopId,
      pageNo: -1,
      pageSize: 20
    }).then(result => {
      toastService.hideLoading();
      if (result.success && result.data) {
        var packingCharges = 0,
          totalNum = 0,
          totalPrice = 0,
          number = 0;
        this.data.data.isShopCart = false;
        result.data.records.forEach((result, index) => {
          let specList = "";
          for (var key in JSON.parse(result.specList)) {
            specList = (specList ? specList + "/" : specList) + JSON.parse(result.specList)[key];
          }
          result.restructure = specList;
          totalNum = totalNum + result.number;
          result.goodsPrices = utilHelper.toFixed((Number(result.goodsPrice) * result.number), 2);
          totalPrice += result.price * result.number; //初始化被选中的商品的总金额
          result.disable = result.goodsStatus == 1 || result.goodsStatus == 3 || result.goodsStatus == 4 ? true : false;
          packingCharges = result.goodsStatus == 1 || result.goodsStatus == 3 || result.goodsStatus == 4 ? packingCharges : packingCharges + (result.packingCharges * result.number);

          if (this.data.data.id == result.goodsId) {
            number = number + result.number;
            this.data.data.isShopCart = true;
            this.data.data.cartId = result.id;
            this.data.data.number = number;
          }
        })
        totalPrice = utilHelper.toFixed(totalPrice, 2);
        var isStartDeliveryPrice = false, priceDifference = 0;

        this.data.shopInfo.shop.startDeliveryPrice = (this.data.shopInfo.shop.startDeliveryPrice ? this.data.shopInfo.shop.startDeliveryPrice : 0);
        if (totalPrice + packingCharges >= this.data.shopInfo.shop.startDeliveryPrice) {
          isStartDeliveryPrice = true;
        }
        console.log("获取当前店铺的起送费====",this.data.shopInfo.shop.startDeliveryPrice)
        priceDifference = this.data.shopInfo.shop.startDeliveryPrice - (totalPrice + packingCharges);
        this.setData({
          data: this.data.data,
          totalNum: totalNum,
          priceDifference: utilHelper.toFixed(priceDifference, 2),
          isStartDeliveryPrice: isStartDeliveryPrice,
          shoppingCartList: result.data.records,
          packingCharges: packingCharges,
          totalPrice: utilHelper.toFixed((totalPrice + packingCharges), 2)
        });
        this.getShopCartFullReductionRule();
      }
    })
  },
  //获取满减规则
  getShopCartFullReductionRule() {
    https.request('/api-goods/rest/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1,
      shopId: this.data.shopId
    }).then(result => {
      if (result.success) {
        let fullPriceReduction = 0,
          fullReductionRuleName = "",
          fullPriceReductionIsHidden = false,
          limitedPrice = 0;
        for (let i = 0; i < result.data.records.length; i++) {
          if (this.data.totalPrice >= result.data.records[i].limitedPrice) {
            if (result.data.records[i].limitedPrice > limitedPrice) {
              limitedPrice = result.data.records[i].limitedPrice;
              fullPriceReduction = this.data.totalPrice - result.data.records[i].reducedPrice;
              fullReductionRuleName = result.data.records[i].name;
              fullPriceReductionIsHidden = true;
            }
          }
        }
        this.setData({
          fullPriceReduction: utilHelper.toFixed(fullPriceReduction, 2),
          fullReductionRuleName: fullReductionRuleName,
          fullPriceReductionIsHidden: fullPriceReductionIsHidden,
          fullReductionRuleList: result.data.records
        })
      }
    })
  },
  getCommodityDetails(id) {
    https.request('/api-goods/rest/goods/selectById', {
      id: id,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      if (result.success && result.data) {
        //获取商品的详细图片，转换以轮播图的数据格式
        let carouselUrls = result.data.subImages.split(",");
        for (let i = 0; i < carouselUrls.length; i++) {
          this.data.carouselUrls.push(GlobalConfig.ossUrl + carouselUrls[i])
        }
        result.data.mainImage = GlobalConfig.ossUrl + result.data.mainImage;
        this.setData({
          data: result.data,
          carouselUrls: this.data.carouselUrls,
          priceAfter:result.data.price
        });
        this.selectByGoodsId(id)
      }
    })
  },
  selectByGoodsId(goodsId) {
    https.request('/api-goods/rest/goodsSpecificationOption/selectByGoodsId', {
      goodsId: goodsId
    }).then(result => {
      if (result.success && result.data) {
        // if(!result.data){
        //   return;
        // }
        //重新设置商品的规格等数据的格式
        //let goodsSpecs = {};
        let specList = result.data;
        let price = this.data.data.price;
        let specListString = "";
        for (let key in specList) {
          let isChecked = true;
          for (let keyof in specList[key]) {
            //拼接查询规格等的json数据格式，查询商品规格等对应的价格
            specList[key][keyof].checked = false;
            //设置每个规格的第一个选项为选中，当库存为0时则选中下一个
            if (specList[key][keyof].stock == 1 && isChecked) {
              specList[key][keyof].checked = true;
              //选中的规格价钱在商品价钱的基础上累加
              price = price + specList[key][keyof].price;
              specListString = (specListString ? specListString + "/" : specListString) + specList[key][keyof].name;
              isChecked = false;
            }
          }
        }
        this.setData({
          specListString: specListString,
          specList: specList,
        })
      }
    })
  },
  insertStore() {
    authService.checkIsLogin().then(result => {
      if (result) {
        let goodsSpecs = {};
        let specList = this.data.specList;
        for (let key in specList) {
          for (let keyof in specList[key]) {
            //拼接查询规格等的json数据格式，查询商品规格等对应的价格
            if (specList[key][keyof].checked) {
              goodsSpecs[key] = specList[key][keyof].name;
            }
          }
        }
        //console.log(goodsSpecs)
        let data = {
          goodsId: this.data.data.id,
          specList: JSON.stringify(goodsSpecs),
          number: this.data.commodityNum
        }
        https.request('/api-goods/rest/member/shoppingCart/insert', data, authService.getToken()).then(result => {
          if (result.success) {
            toastService.showSuccess("加入成功");
            //wx.navigateBack(1)

          }
        })
        return;
      }
      app.checkIsAuth("scope.userInfo");
    })
  },
  businessTap(e) {
    wx.navigateTo({
      url: '../../menu/index/index?id=' + e.currentTarget.dataset.id,
    })
  },
  getGoodsCollect(goodsId){
    https.request('/api-goods/rest/member/goodsCollect/selectByGoodsId', {
      goodsId: goodsId
    }).then(result => {
      if (result.success) {
        this.setData({
          collectInfo:result.data,
          isCollect:result.data?true:false
        })
      }
    })
  },
  bindInsertCollect(e){
    https.request('/api-goods/rest/member/goodsCollect/insert', {
      goodsId: e.currentTarget.dataset.goodsid
    }).then(result => {
      if (result.success) {
        this.setData({
          isCollect:true
        })
        toastService.showToast("收藏成功");
      }
    })
  },
  bindDeleteCollect(e){
    https.request('/api-goods/rest/member/goodsCollect/delete', {
      goodsId: e.currentTarget.dataset.goodsid
    }).then(result => {
      if (result.success) {
        this.setData({
          isCollect:false
        })
        toastService.showToast("取消成功");
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载 
   */
  onLoad: function (options) {
    this.autoHeigth();
    //console.log(app.globalData.loginUserInfo)
    this.setData({
      shopId: options.shopId,
      userInfo: app.globalData.loginUserInfo
    });
    this.getCommodityDetails(options.id);
    this.getGoodsCollect(options.id);
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
    this.getShopInfo();
    this.getTimestamp();
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