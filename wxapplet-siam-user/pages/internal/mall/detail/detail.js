import GlobalConfig from '../../../../utils/global-config';
import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import utilHelper from '../../../../utils/util';
import dateHelper from '../../../../utils/date-helper';
//获取应用实例 
const app = getApp()
var totalPrice = 0,
  totalNum = 0;
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
    chooseType: "init"
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
    let goodsSpecs = {};
    let specListString = "";
    for (let key in specList) {
      for (let keyof in specList[key]) {
        //console.log(specList[key][keyof].price)
        if (specList[key][keyof].checked) {
          price = price + specList[key][keyof].price;
          specListString = (specListString ? specListString + "/" : specListString) + specList[key][keyof].name;

        }
      }
    }
    console.log(specListString)
    let totalPrice = price * this.data.commodityNum;
    let fullPriceReduction = 0,
      fullReductionRuleName = "",
      fullPriceReductionIsHidden = false,
      limitedPrice = 0;
    for (let i = 0; i < this.data.fullReductionRuleList.length; i++) {
      //console.log(this.data.fullReductionRuleList[i].limitedPrice)
      if (totalPrice >= this.data.fullReductionRuleList[i].limitedPrice) {
        if (this.data.fullReductionRuleList[i].limitedPrice > limitedPrice) {
          limitedPrice = this.data.fullReductionRuleList[i].limitedPrice;
          fullPriceReduction = (totalPrice + 0) - this.data.fullReductionRuleList[i].reducedPrice;
          fullReductionRuleName = this.data.fullReductionRuleList[i].name;
          fullPriceReductionIsHidden = true;
        }
      }
    }
    this.setData({
      specList: specList,
      specListString: specListString,
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
    // if(!goodsSpecs){
    //   toastService.showToast("请选择规格");
    //   return
    // }
    toastService.showLoading();
    authService.checkIsLogin().then(result => {
      toastService.hideLoading();
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
          specList: JSON.stringify(goodsSpecs)
        }
        https.request('/api-goods/rest/member/pointsMall/shoppingCart/insert', data, authService.getToken()).then(result => {
          if (result.success) {
            toastService.showToast("加入购物车成功");
            this.setData({
              specificationsDialog: false
            });
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
        self.toPay();
        return
      }
      app.checkIsAuth("scope.userInfo");
    })
  },
  toPay() {
    var data = this.data.data;
    var specList = this.data.specList;
    var orderDetail = {},
      specLists = {};
    var restructure = "";
    //console.log(specList)
    if (data.status == 4) {
      toastService.showToast("商品已售罄");
      return
    }
    //拼接数据
    for (let key in specList) {
      for (let keyof in specList[key]) {
        //拼接查询规格等的json数据格式，查询商品规格等对应的价格
        if (specList[key][keyof].checked) {
          specLists[key] = specList[key][keyof].name;
          restructure = (restructure ? restructure + "/" : restructure) + specList[key][keyof].name
        }
      }
    }
    //设置和在购物车提交时的数据格式一样
    orderDetail.actualPrice = this.data.totalPrice;
    orderDetail.packingCharges = 0;
    orderDetail.orderDetailList = [];
    orderDetail.orderDetailList.push({
      goodsId: data.id,
      specList: JSON.stringify(specLists),
      number: this.data.commodityNum,
      goodsName: data.name,
      restructure: restructure,
      price: this.data.priceAfter,
      packingCharges: 0
    })
    console.log(orderDetail)
    wx.navigateTo({
      url: '../pay/pay?orderDetail=' + JSON.stringify(orderDetail),
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

  //获取满减规则
  getFullReductionRule(commodityDetails) {
    https.request('/api-goods/rest/pointsMall/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1
    }).then(result => {
      if (result.success) {
        let fullPriceReduction = 0,
          fullReductionRuleName = "",
          fullPriceReductionIsHidden = false,
          limitedPrice = 0;
        let totalPrice = commodityDetails.price + 0;
        for (let i = 0; i < result.data.records.length; i++) {
          if (totalPrice >= result.data.records[i].limitedPrice) {
            if (result.data.records[i].limitedPrice > limitedPrice) {
              limitedPrice = result.data.records[i].limitedPrice;
              fullPriceReduction = totalPrice - result.data.records[i].reducedPrice;
              fullReductionRuleName = result.data.records[i].name;
              fullPriceReductionIsHidden = true;
            }
          }
        }
        this.setData({
          fullPriceReduction: utilHelper.toFixed(fullPriceReduction, 2),
          fullReductionRuleName: fullReductionRuleName,
          fullPriceReductionIsHidden: fullPriceReductionIsHidden,
          fullReductionRuleList: result.data.records,
          totalPrice: commodityDetails.price
        })
      }
    })
  },
  getCommodityDetails(id) {
    https.request('/api-goods/rest/pointsMall/goods/selectById', {
      id: id
    }).then(result => {
      if (result.success && result.data) {
        //获取商品的详细图片，转换以轮播图的数据格式
        let carouselUrls = result.data.subImages.split(",");
        this.data.carouselUrls = []
        for (let i = 0; i < carouselUrls.length; i++) {
          this.data.carouselUrls.push(GlobalConfig.ossUrl + carouselUrls[i])
        }
        this.data.detailImages = []
        if (result.data.detailImages) {
          let detailImages = result.data.detailImages.split(",");
          for (let i = 0; i < detailImages.length; i++) {
            this.data.detailImages.push(GlobalConfig.ossUrl + detailImages[i])
          }
          result.data.mainImage = GlobalConfig.ossUrl + result.data.mainImage;
        }

        this.setData({
          data: result.data,
          carouselUrls: this.data.carouselUrls,
          detailImages: this.data.detailImages,
          priceAfter:result.data.price
        });
        this.selectByGoodsId(id);
        this.getFullReductionRule(result.data);
        this.chooseSpecifications();
      }
    })
  },

  openSpecifications(e) {
    console.log(e)
    this.setData({
      specificationsDialog: true,
      goodsId: this.data.data.id,
      chooseType: e.currentTarget.dataset.type
    });
    this.selectByGoodsId(this.data.data.id);
  },
  selectByGoodsId(goodsId) {
    https.request('/api-goods/rest/pointsMall/goodsSpecificationOption/selectByGoodsId', {
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
          specListString: specListString ? specListString : "暂无规格",
          specList: JSON.stringify(specList) == "{}" ? null : specList,
          priceAfter:price
        })
      }
    })
  },
  chooseSpecifications(e) {
    toastService.showLoading();
    var _this=this;
    authService.checkIsLogin().then(result => {
      toastService.hideLoading();
      if (result) {
        let goodsSpecs = {};
        let specList = _this.data.specList;
        console.log(_this.data)
        let price = _this.data.data.price;
        for (let key in specList) {
          for (let keyof in specList[key]) {
            //拼接查询规格等的json数据格式，查询商品规格等对应的价格
            if (specList[key][keyof].checked) {
              goodsSpecs[key] = specList[key][keyof].name;
              //选中的规格价钱在商品价钱的基础上累加
              price = price + specList[key][keyof].price;
            }
          }
        }
        //console.log(goodsSpecs)
        let data = {
          goodsId: this.data.goodsId,
          specList: JSON.stringify(goodsSpecs)
        }
        _this.setData({
          repData: JSON.stringify(goodsSpecs),
          specificationsDialog: false
        });
        console.log(_this.data.chooseType)
        if (this.data.chooseType == "pay") {
          _this.goToPay();
        } else if(_this.data.chooseType=="car"){
          _this.insertShoppingCart();
        }else{
          
        }
        toastService.hideLoading();
        return
      }
      this.setData({
        specificationsDialog: false
      });
      app.checkIsAuth("scope.userInfo");
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
  getGoodsCollect(goodsId) {
    https.request('/api-goods/rest/member/pointsMall/goodsCollect/selectByGoodsId', {
      goodsId: goodsId
    }).then(result => {
      if (result.success) {
        this.setData({
          collectInfo: result.data,
          isCollect: result.data ? true : false
        })
      }
    })
  },
  bindInsertCollect(e) {
    authService.checkIsLogin().then(result => {
      if (result) {
        https.request('/api-goods/rest/member/pointsMall/goodsCollect/insert', {
          goodsId: e.currentTarget.dataset.goodsid
        }).then(result => {
          if (result.success) {
            this.setData({
              isCollect: true
            })
            toastService.showToast("收藏成功");
          }
        });
        return
      }
      app.checkIsAuth("scope.userInfo");
    });
  },
  bindDeleteCollect(e) {
    authService.checkIsLogin().then(result => {
      if (result) {
        https.request('/api-goods/rest/member/pointsMall/goodsCollect/delete', {
          goodsId: e.currentTarget.dataset.goodsid
        }).then(result => {
          if (result.success) {
            this.setData({
              isCollect: false
            })
            toastService.showToast("取消成功");
          }
        })
        return
      }
      app.checkIsAuth("scope.userInfo");
    });
  },
  bindShare(e) {
    authService.checkIsLogin().then(result => {
      if (result) {
        wx.navigateTo({
          url: '../../../mine/share/index/index?inviterId=' + this.data.userInfo.id,
        })
      } else {
        app.checkIsAuth("scope.userInfo");
      }
    });
  },
  /**
   * 生命周期函数--监听页面加载 
   */
  onLoad: function (options) {
    this.autoHeigth();
    //console.log(app.globalData.loginUserInfo)
    this.setData({
      userInfo: app.globalData.loginUserInfo
    })
    this.getCommodityDetails(options.id);
    this.getGoodsCollect(options.id);
    // this.chooseSpecifications();
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