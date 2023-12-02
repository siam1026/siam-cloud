import GlobalConfig from '../../../utils/global-config';
import https from '../../../utils/http';
import toastService from '../../../utils/toast.service';
import authService from '../../../utils/auth';
import utilHelper from '../../../utils/util';
import CustomPage from '../../../base/CustomPage';
import dateHelper from '../../../utils/date-helper';
var app = getApp();
//声明全局变量 
var proListToTop = [],
  isInitShow = true,
  winHeight = 0,
  totalPrice = 0,
  totalNum = 0, menuList = [];

CustomPage({

  /**
   * 页面的初始数据
   */
  data: {
    staticImg: app.globalData.staticImg,
    currentActiveIndex: 0,
    menuList: [],
    imageTip: "../../assets/common/icon-internet-error.png",
    indicatorDots: true,
    autoplay: true,
    interval: 5000,
    duration: 1000,
    //beforeColor: "white",//指示点颜色,
    afterColor: "white", //当前选中的指示点颜色
    extClass: 'weui-dialog-ext-index',
    businessModes: [{
      modeName: "点餐",
      modeId: 0
    }, {
      modeName: "评价",
      modeId: 1
    }, {
      modeName: "商家",
      modeId: 2
    }],
    currentTab: 0,
    data: {
      dialog1: false,
      dialog2: false
    },
    deliveryPrice: 0,
    shoppingCartDialog: false,
    specificationsDialog: false,
    activeTab: 0,
    rate: 5,
    disabled: true,
    priceDifference: 0,
    isStartDeliveryPrice: false,
    appraiseFocus: false,
    buttons: [],
    appraiseFocus: false,
    showConfirmBar: false,
    shoppingCartList: [],
    isShopDetail:false,
    isActivityDialog:false,
    isOutofDeliveryRangeDialog:false
  },
  closeShoppingCart: function () {
    this.setData({
      shoppingCartDialog: false
    });
  },
  close: function () {
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
  // 滑动切换tab
  bindSlideChange: function (e) {
    var that = this;
    that.setData({
      currentTab: e.detail.current
    });
  },
  //点击切换 
  clickTab: function (e) {
    var that = this;
    this.setData({
      shoppingCartDialog: false,
      specificationsDialog: false
    })
    if (that.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      });
    }
  },
  autoHeight() {
    var that = this;
    isInitShow = false;
    const query = wx.createSelectorQuery();
    let windowHeight = app.globalData.systemInfoSync.windowHeight;
    console.log(windowHeight)
    var height = 0;
    query.selectAll('.swiper-tabs-choice,.content-manjian').boundingClientRect(function (rect) {
      console.log(rect)
      let height = windowHeight > 630 ? rect[0].height - 10 : rect[0].height + 25;
      that.setData({
        winHeight: windowHeight - height,
        leftHeight: windowHeight - height,
        bussinessHeight: windowHeight - height
      })
    }).exec();
    // query.selectAll('.swiper-tabs-choice').boundingClientRect(function (rect) {
    //   console.log(rect)
    //   let height = (windowHeight > 600 ? windowHeight - 10 : windowHeight - 44)
    //   that.setData({
    //     winHeight: height,
    //     leftHeight: height
    //   })
    // }).exec();
  },
  commodityDetailTap(e) {
    console.log(e.currentTarget.dataset.id)
    wx.navigateTo({
      url: '../detail/detail?id=' + e.currentTarget.dataset.id + "&shopId=" + this.data.shopId,
    })
  },
  getCarouselList() {
    https.request('/api-goods/rest/advertisement/list', {
      type: 2,
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
  radioChange(e) {
    //console.log(e)
    authService.checkIsLogin().then(result => {
      if (result) {
        let checkValue = e.currentTarget.dataset.radioindex;
        let deliveryData = JSON.stringify(this.data.deliveryData);
        wx.navigateTo({
          url: '../../address/choose/choose?radioIndex=' + checkValue +
            '&deliveryData=' + deliveryData + '&pageType=menu',
        })
        return;
      }
      app.checkIsAuth("scope.userInfo");
    });
  },
  scroll(e) {
    //console.log(e)
    //获取当前滑动距离顶部的距离高度
    let scrollTop = e.detail.scrollTop;
    //console.log(scrollTop)
    //获取右侧菜单的高度
    let scrollArr = proListToTop;
    //当距离顶部的高度大于等于右侧菜单的item高度
    let index = 0;
    if (scrollTop >= (scrollArr[scrollArr.length - 1])) {
      return
    } else {
      for (let i = 0; i < scrollArr.length; i++) {
        if (scrollTop >= 0 && scrollTop < scrollArr[0]) {
          index = 0;
        } else if (scrollTop >= scrollArr[i - 1] && scrollTop < scrollArr[i]) {
          index = i
        } else if (scrollTop <= 0) {

        }
      }
    }
    this.setData({
      currentActiveIndex: index
    })
  },
  selectMenuTap(e) {
    var index = e.currentTarget.dataset.index;
    console.log(index)
    this.setData({
      toView: 'NAV' + index.toString(),
      currentActiveIndex: index
    })
  },
  getAllRects() {
    // 获取商品数组的位置信息
    let menuHeight = 0;
    wx.createSelectorQuery().selectAll('.commodity-item-view').boundingClientRect(function (rects) {
      rects.forEach(function (rect) {
        menuHeight += rect.height;
        proListToTop.push(menuHeight);
      })
      toastService.hideLoading();
    }).exec();
  },
  getShopInfo() {
    https.request('/api-goods/rest/shop/detail', {
      id: this.data.shopId,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      if (result.success && result.data) {
        if (result.data.isOutofDeliveryRange) {
          this.setData({
            isOutofDeliveryRangeDialog:true
          }) 
        }
        this.getFullReductionRule(result.data)
      }
    })
  },
  //获取满减规则
  getFullReductionRule(shopInfo) {
    //console.log(shopInfo)
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
    shopInfo.shop.firstPoster = GlobalConfig.ossUrl + shopInfo.shop.firstPoster;
    shopInfo.shop.shopWithinImgs = shopWithinImgs;
    //shopInfo.shop.isOperating = false;
    app.getIsItNear(shopInfo.shop.startTime,shopInfo.shop.endTime);
    this.setData({
      shopInfo: shopInfo,
      fullPriceReduction: utilHelper.toFixed(fullPriceReduction, 2),
      fullReductionRuleName: fullReductionRuleName,
      fullPriceReductionIsHidden: fullPriceReductionIsHidden
    });
    authService.checkIsLogin().then(result => {
      if (result) {
        this.getShoppingCartList();
      }
    });
  },
  getMenuList() {
    toastService.showLoading();
    var self = this;
    https.request('/api-goods/rest/menu/listWithGoods', {
      shopId: this.data.shopId
    }).then(result => {
      toastService.hideLoading();
      if (result.success && result.data) {
        //console.log(result.data)
        result.data.forEach((aitem, index) => {
          aitem.goodsList.forEach(bitem => {
            bitem.isShopCart = false;
            bitem.mainImage = GlobalConfig.ossUrl + bitem.mainImage;
          })
        })
        menuList = result.data;
        this.setData({
          menuList: menuList
        })
      }
    })
  },
  getAppraiseList() {
    https.request('/api-order/rest/member/appraise/list', {
      shopId: this.data.shopId,
      pageNo: -1,
      pageSize: 20
    }).then(result => {
      if (result.success && result.data) {

        result.data.records.forEach((aitem, index) => {
          //console.log(aitem.imagesUrl)
          aitem.imagesUrls = [];
          if (aitem.imagesUrl) {
            aitem.imagesUrl.split(",").forEach(item => {
              item = GlobalConfig.ossUrl + item;
              aitem.imagesUrls.push(item);
            });
          }
          if(aitem.replyPageInfo){
            aitem.replyPageInfo.list.forEach((bitem, bIndex) => {
              let goodt = (bitem.isGiveLike == 0) ? "点赞" : "取消点赞";
              let type = (bitem.isGiveLike == 0) ? "good" : "deletegood";
              let good = goodt + (bitem.giveLikeCount > 0 ? (' ' + bitem.giveLikeCount) : '') + "";
              //console.log("获取是否点赞============" + goodt)
              bitem.selectTexts = [{
                "name": "复制",
                "type": "copy",
                "bind": "handleCopy"
              }, {
                "name": good,
                "type": type,
                "bind": ""
              }];
              //console.log(bitem)
              if (bitem.replierType == 2) {
                bitem.username = "商家回复"
              }
              // if(app.globalData.loginUserInfo.id==bitem.memberId){
              //   bitem.selectTexts.push({
              //     "name": "删除",
              //     "type": "delete",
              //     "bind": ""
              //   })
              // }
              bitem.placement = 'top';
            })
          }
          
          aitem.createTime = dateHelper.fmtDate(aitem.createTime);
        });
        //console.log(result.data.records)
        this.setData({
          appraiseList: result.data.records
        })
      }
    })
  },
  getShoppingCartList() {
    var that = this;
    https.request('/api-goods/rest/member/shoppingCart/list', {
      shopId: this.data.shopId,
      pageNo: -1,
      pageSize: 20
    }).then(result => {
      toastService.hideLoading();
      if (result.success && result.data) {
        var packingCharges = 0,
          totalNum = 0,
          totalPrice = 0;
        menuList.forEach((menu, menuIndex) => {
          menu.goodsList.forEach((goods, goodsIndex) => {
            let number = 0;
            menuList[menuIndex].goodsList[goodsIndex].number = number;
            result.data.records.forEach((result, carIndex) => {
              if (goods.goodsId == result.goodsId) {
                number = number + result.number;
                menuList[menuIndex].goodsList[goodsIndex].isShopCart = true;
                result.menuIndex = menuIndex;
                result.goodsIndex = goodsIndex;
                menuList[menuIndex].goodsList[goodsIndex].number = number;
                menuList[menuIndex].goodsList[goodsIndex].cartId = result.id;
              }
            })
          })
        })
        this.data.merchantRecommendGoods.forEach((merchants, merchanIndex) => {
          let merchant = 0;
          merchants.number = merchant;
          result.data.records.forEach((result, carIndex) => {
            if (merchants.goodsId == result.goodsId) {
              merchant = merchant + result.number;
              merchants.isShopCart = true;
              merchants.number = merchant;
              merchants.cartId = result.id;
            }
          });
        });
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
          //console.log(result)
        })

        menuList = menuList;
        totalPrice = utilHelper.toFixed(totalPrice, 2);
        var isStartDeliveryPrice = false, priceDifference = 0;

        this.data.shopInfo.shop.startDeliveryPrice=(this.data.shopInfo.shop.startDeliveryPrice?this.data.shopInfo.shop.startDeliveryPrice:0);
        if (totalPrice + packingCharges >= this.data.shopInfo.shop.startDeliveryPrice) {
          isStartDeliveryPrice = true;
        }
        priceDifference = this.data.shopInfo.shop.startDeliveryPrice - (totalPrice + packingCharges);
        this.setData({
          menuList: menuList,
          merchantRecommendGoods: this.data.merchantRecommendGoods,
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
          fullPriceReductionIsHidden: fullPriceReductionIsHidden
        })
      }
    })
  },
  getMerchantRecommendGoods() {
    https.request('/api-goods/rest/merchantRecommendGoods/list', {
      pageNo: -1,
      pageSize: 1,
      shopId: this.data.shopId
    }).then(result => {
      if (result.success) {
        result.data.records.forEach(function (item, index) {
          item.mainImage = GlobalConfig.ossUrl + item.mainImage;
          item.isShopCart = false;
        })

        this.setData({
          merchantRecommendGoods: result.data.records
        });
      }
    })
  },
  bindInputAppraise(e) {
    this.setData({
      appraise: e.detail.value
    })
  },
  viewImage: function (event) {
    var imgs = event.currentTarget.dataset.imgs; //获取data-src
    var index = event.currentTarget.dataset.index; //获取data-currentimg
    var src = imgs[index];

    //图片预览
    wx.previewImage({
      current: src, // 当前显示图片的http链接
      urls: imgs // 需要预览的图片http链接列表
    })
  },
  appraiseTap(e) {
    let appraiseId = e.currentTarget.dataset.appraiseid;
    let userName = e.currentTarget.dataset.username;
    let replyType = 1;
    let replyId = "";
    let appraiseIndex = e.currentTarget.dataset.appraiseindex;
    let appraiseJson = {
      appraiseId: this.data.appraiseId,
      userName: this.data.userName,
      replyType: this.data.replyType,
      appraiseIndex: this.data.appraiseIndex,
      replyId: this.data.replyId
    };

    this.setData({
      appraiseId: appraiseId,
      userName: userName,
      replyId: replyId,
      replyType: replyType,
      appraiseIndex: appraiseIndex,
      appraiseJson: appraiseJson,
      content: appraiseId == appraiseJson.appraiseId ? this.data.content : "",
      appraiseFocus: true
    });
    this.getAppraiseTextareaHeight();
  },
  getAppraiseTextareaHeight() {
    let that = this;
    const query = wx.createSelectorQuery();
    query.select(".pinglun-bottom").boundingClientRect();
    query.selectViewport().scrollOffset();
    query.exec(function (res) {
      console.log()
      that.setData({
        appraiseHeight: res[0].height
      });
    });
    console.log(this.data.appraiseHeight)
  },
  handleTap(e) {
    console.log(e)
    //this.setData({ evt: e })
  },
  handleCurrencyTap(e) {
    console.log(e)
    this.setData({
      appraiseId: e.currentTarget.dataset.appraiseid,
      appraiseIndex: e.currentTarget.dataset.appraiseindex
    })

    if (e.detail.type == "deletegood") {
      this.deleteLikeCountTap(e);
    }
    if (e.detail.type == "good") {
      this.giveLikeCountTap(e);
    }
    if (e.detail.type == "delete") {
      let url = "";
      if (e.currentTarget.dataset.type == "1") {
        url = "/api-order/rest/member/appraise/delete";
      }
      if (e.currentTarget.dataset.type == "2") {
        url = "/api-user/rest/member/reply/delete";
      }
      this.deleteAppraise(e, url);
    }
  },
  deleteAppraise(e, url) {
    toastService.showLoading();
    let replyId = e.currentTarget.dataset.replyid;
    let type = e.currentTarget.dataset.type;
    https.request(url, {
      id: replyId,
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        if (type == "1") {
          this.data.appraiseList[appraiseIndex].giveLikeCount -= 1;
          this.data.appraiseList[appraiseIndex].isGiveLike = true;
        }

        this.setData({
          appraiseList: this.data.appraiseList
        });
      }
    })
  },
  giveLikeCountTap(e) {
    toastService.showLoading();
    this.setData({
      appraiseId: e.currentTarget.dataset.appraiseid,
      appraiseIndex: e.currentTarget.dataset.appraiseindex
    })
    let appraiseId = e.currentTarget.dataset.appraiseid;
    let appraiseIndex = e.currentTarget.dataset.appraiseindex;
    let replyId = e.currentTarget.dataset.replyid;
    let type = e.currentTarget.dataset.type;
    let data = {
      appraiseId: appraiseId,
      type: type,
    }
    if (replyId) {
      data.replyId = replyId;
    }
    https.request('/api-user/rest/member/giveLike/insert', data).then(result => {
      toastService.hideLoading();
      if (result.success) {
        if (type == "1") {
          this.data.appraiseList[appraiseIndex].giveLikeCount += 1;
          this.data.appraiseList[appraiseIndex].isGiveLike = true;
        }
        this.getReplyByAppraiseId();
        // this.setData({
        //   appraiseList: this.data.appraiseList
        // });
      }
    })
  },
  deleteLikeCountTap(e) {
    this.setData({
      appraiseId: e.currentTarget.dataset.appraiseid,
      appraiseIndex: e.currentTarget.dataset.appraiseindex
    })
    toastService.showLoading();
    let appraiseId = e.currentTarget.dataset.appraiseid;
    let appraiseIndex = e.currentTarget.dataset.appraiseindex;
    let replyId = e.currentTarget.dataset.replyid;
    let type = e.currentTarget.dataset.type;
    let data = {
      appraiseId: appraiseId,
      type: type,
    }
    if (replyId) {
      data.replyId = replyId;
    }
    https.request('/api-user/rest/member/giveLike/delete', data).then(result => {
      if (result.success) {
        toastService.hideLoading();
        if (type == "1") {
          this.data.appraiseList[appraiseIndex].giveLikeCount -= 1;
          this.data.appraiseList[appraiseIndex].isGiveLike = false;
        }
        this.getReplyByAppraiseId();
        // this.setData({
        //   appraiseList: this.data.appraiseList
        // });
      }
    })
  },
  bindblur(e) {
    let content = e.detail.value;
    if (this.data.appraiseId == this.data.appraiseJson.appraiseId) {
      content = this.data.content;
    }
    this.setData({
      appraiseFocus: false,
      content: content
    })
  },
  replyTap(e) {
    let replyType = 2;
    let appraiseId = e.currentTarget.dataset.appraiseid;
    let replyId = e.currentTarget.dataset.replyid;
    let userName = e.currentTarget.dataset.username;
    let appraiseIndex = e.currentTarget.dataset.appraiseindex;
    console.log("我点击了回复，我回复====" + userName);
    let appraiseJson = {
      appraiseId: this.data.appraiseId,
      replyType: this.data.replyType,
      replyId: this.data.replyId,
      userName: this.data.userName,
      appraiseIndex: this.data.appraiseIndex
    };
    this.setData({
      appraiseFocus: true,
      appraiseId: appraiseId,
      replyId: replyId,
      userName: userName,
      replyType: replyType,
      appraiseJson: appraiseJson,
      appraiseIndex: appraiseIndex,
      content: appraiseId == appraiseJson.appraiseId && replyId == appraiseJson.replyId ? this.data.content : ""
    })
  },
  bindconfirm(e) {
    let appraise = this.data.appraise;
    if (!appraise) {
      toastService.showToast("请输入您的回复内容");
      return
    }
    toastService.showLoading();
    https.request('/api-user/rest/member/reply/insert', {
      appraiseId: this.data.appraiseId,
      replyId: this.data.replyId,
      replyType: this.data.replyType,
      content: appraise,
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        this.setData({
          appraise: ''
        })
        this.getReplyByAppraiseId();
      }
    })
  },
  getReplyByAppraiseId(e) {
    https.request('/api-user/rest/member/reply/list', {
      appraiseId: this.data.appraiseId,
      pageNo: -1,
      pageSize: 20
    }).then(result => {
      toastService.hideLoading();
      if (result.success && result.data) {
        result.data.records.forEach((bitem, bIndex) => {
          let goodt = (bitem.isGiveLike == 0) ? "点赞" : "取消点赞";
          let type = (bitem.isGiveLike == 0) ? "good" : "deletegood";
          let good = goodt + (bitem.giveLikeCount > 0 ? (' ' + bitem.giveLikeCount) : '') + "";

          bitem.selectTexts = [{
            "name": "复制",
            "type": "copy",
            "bind": "handleCopy"
          }, {
            "name": good,
            "type": type,
            "bind": ""
          }];
          if (bitem.replierType == 2) {
            bitem.username = "商家回复"
          }
          // if(app.globalData.loginUserInfo.id==bitem.memberId){
          //   bitem.selectTexts.push({
          //     "name": "删除",
          //     "type": "delete",
          //     "bind": ""
          //   })
          // }
          bitem.placement = 'top';
        })

        this.data.appraiseList[this.data.appraiseIndex].replyPageInfo.list = result.data.records;

        this.setData({
          appraiseList: this.data.appraiseList
        })
      }
    })
  },
  openSpecifications(e) {
    toastService.showLoading();
    this.setData({
      specificationsDialog: true,
      goodsId: e.currentTarget.dataset.goodsid
    });
    toastService.hideLoading();
    this.getCommodityDetails(e.currentTarget.dataset.goodsid);
  },
  closeSpecifications: function () {
    this.getShoppingCartList();
    this.setData({
      specificationsDialog: false
    });
  },
  getCommodityDetails(id) {
    https.request('/api-goods/rest/goods/selectById', {
      id: id,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      
      if (result.success && result.data) {
        //获取商品的详细图片，转换以轮播图的数据格式
        //console.log(result.data)
        result.data.mainImage = GlobalConfig.ossUrl + result.data.mainImage
        this.setData({
          goodsInfo: result.data,
          priceAfter:result.data.price
        })
        this.selectByGoodsId(id);
      }
    })
  },
  selectByGoodsId(goodsId) {
    console.log(this.data)
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
    let price = this.data.goodsInfo.price;
    let specListString = "";
    for (let key in specList) {
      for (let keyof in specList[key]) {
        console.log(specList[key][keyof].price)
        if (specList[key][keyof].checked) {
          price = price + specList[key][keyof].price;
          specListString = (specListString ? specListString + "/" : specListString) + specList[key][keyof].name;
        }
      }
    }
    let totalPrice = price * this.data.commodityNum;
    let fullPriceReduction = 0,
      fullReductionRuleName = "",
      fullPriceReductionIsHidden = false,
      limitedPrice = 0;
    //console.log(this.data.fullReductionRuleList)
    for (let i = 0; i < this.data.shopInfo.fullReductionRuleList.length; i++) {
      //console.log(this.data.fullReductionRuleList[i].limitedPrice)
      if (totalPrice >= this.data.shopInfo.fullReductionRuleList[i].limitedPrice) {
        if (this.data.shopInfo.fullReductionRuleList[i].limitedPrice > limitedPrice) {
          limitedPrice = this.data.shopInfo.fullReductionRuleList[i].limitedPrice;
          fullPriceReduction = (totalPrice + this.data.data.packingCharges) - this.data.shopInfo.fullReductionRuleList[i].reducedPrice;
          fullReductionRuleName = this.data.shopInfo.fullReductionRuleList[i].name;
          fullPriceReductionIsHidden = true;
        }
      }
    }

    // for (var key in specList) {
    //   console.log(specList[key])
    //   specListString = (specListString ? specListString + "/" : specListString) + specList[key];
    // }
    //console.log(specList)
    this.setData({
      specList: specList,
      specListString: specListString,
      priceAfter: price,
      // totalPrice: utilHelper.toFixed(totalPrice, 2),
      // fullPriceReduction: utilHelper.toFixed(fullPriceReduction, 2),
      // fullPriceReductionIsHidden: fullPriceReductionIsHidden,
      // fullReductionRuleName: fullReductionRuleName
    })
  },
  insertShoppingCart(e) {
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
            toastService.hideLoading();
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
      toastService.hideLoading();
      //重新计算被选中的商品的总金额
      totalNum++;
      self.getShoppingCartList();
    })
  },
  updateNumber(id, number, type, callbak) {
    authService.checkIsLogin().then(result => {
      console.log(result)
      if (!result) {
        app.checkIsAuth("scope.userInfo");
        return
      }
      https.request('/api-goods/rest/member/shoppingCart/updateNumber', {
        id: id,
        number: number,
        type: type
      }).then(result => {
        if (result.success) {
          callbak();
        }
      })
    });
    
  },
  goToPay() {
    authService.checkIsLogin().then(result => {
      if (!result) {
        app.checkIsAuth("scope.userInfo");
        return
      }
      //判断店铺是否打烊
      let startTime = this.data.shopInfo.shop.startTime;
      let endTime = this.data.shopInfo.shop.endTime;
      let isOperating = this.data.shopInfo.shop.isOperating;

      app.getIsBusiness(startTime, endTime, isOperating).then(result => {
        if (!result) {
          return
        }
        this.toPay();
      })
    });
  },
  toPay(){
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
    });
  },
  connectedTap() {
    this.onLoad();
  },
  gotoShop(e) {
    app.isRemindNewPeople().then(result => {
      if (result) {
        this.setData({
          dialogShow: false,
          maskClosable: true,
        });
      }
    });
  },
  openConfirm() {
    this.setData({
      dialogShow: true,
      maskClosable: false,
    })
  },
  close(e) {
    console.log(e)
    this.setData({
      specificationsDialog: false,
      shoppingCartDialog: false
    })
  },
  onTabCLick(e) {
    const index = e.detail.index
    console.log('tabClick', index)
  },
  isShopDetailTap(){
    this.setData({
      isShopDetail:true
    })
  },
  isPromotionTap(){
    this.setData({
      isActivityDialog:true
    })
  },
  onChange(e) {
    const index = e.detail.index
    //console.log('change', index)
  },
  contactBussinessTap() {
    wx.makePhoneCall({
      phoneNumber: this.data.shopInfo.shop.contactPhone
    })
  },
  getSystemUsageRecord() {
    https.request('/api-goods/rest/systemUsageRecord/insert', {
      shopId: this.data.shopId,
      type: "intoShop"
    }).then(result => {
      if (result.success) {

      }
    })
  },
  editAddress(){
    this.setData({
      isOutofDeliveryRangeDialog:false
    })
    wx.navigateTo({
      url: '../../address/replace/replace?jump_page=menu_index&addressType='+true+'&shopId='+this.data.shopInfo.shop.id,
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 确保页面数据已经刷新完毕~
    authService.checkIsLogin().then(result => {
      console.log(result)
      if (result) {
        return;
      }
    });
    this.setData({
      shopId: options.id,
      screenHeight:app.globalData.systemInfoSync.screenHeight
    });
    this.topViewHeight();
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    console.log("onReady")
    var that = this;
    // toastService.showLoading();
    // let timeout = setTimeout(() => {
    //   that.getAllRects();
    //   clearTimeout(timeout);
    // }, 1000);
    this.getCarouselList();
    this.getSystemUsageRecord();
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // app.getMemberInfo().then(result => {
    //   if (result.isNewPeople && result.isRemindNewPeople) {
    //     this.openConfirm();
    //   }
    // });

    var that = this;
    this.getShopInfo();
    this.getAppraiseList();
    this.getMerchantRecommendGoods();
    //this.setData(app.deliveryAndSelfTaking);
    //this.getShoppingCartList();
    // let timeout = setTimeout(() => {
    //   that.autoHeight();
    //   clearTimeout(timeout);
    // }, 1000)
    this.getMenuList();
    wx.getNetworkType({
      success: function (res) {
        //console.log(res.networkType)
        let networkType = res.networkType;
        if (networkType != 'none') {
          
        }
        that.setData({
          netWorkType: res.networkType == 'none' ? true : false
        })
      }
    });
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    this.setData({
      dialogShow: false,
      maskClosable: true,
      proListToTop: []
    })
  },

  /** 
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () { },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getMenuList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
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

  },
  topViewHeight() {
    var _this = this;
    isInitShow = false;
    const query = wx.createSelectorQuery();
    let windowHeight = app.globalData.systemInfoSync.windowHeight;
    let screenHeight = app.globalData.systemInfoSync.screenHeight;
    let statusBarHeight=app.globalData.systemInfoSync.statusBarHeight * 2;
    console.log(windowHeight)
    var height = 0;
    query.selectAll('#page-top-view,#d_p_s,#shopping-cart-detail').boundingClientRect(function (rect) {
      console.log(rect)
      _this.setData({
        'pageTopView':rect[0].height*2,
        'dps':rect[1].height*2,
        'shoppingCartDetail':rect[2].height*2,
        'contentHeight':screenHeight-(rect[1].height*2)-(rect[2].height*2)
      })
    }).exec();

  
    console.log(app.globalData.systemInfoSync);
  },
  onPageScroll(e) {
    console.log(e);
    if((e.scrollTop)>=(this.data.pageTopView-10)){
      this.setData({
        ifScroll:true
      })
    }else{
      this.setData({
        ifScroll:false
      })
    }
  }
  // onPageScroll(e) {
  //   console.log(e);
  //   var height=(this.data.pageTopHeight);
  //   height=(height*2)-13;
  //   console.log(height)
  //   if (e.scrollTop >= (height)) {
  //     if (!this.data.scrollTop) {
  //       this.setData({
  //         scrollTop: true
  //       })
  //     }
  //   }else{
  //     if (this.data.scrollTop) {
  //       this.setData({
  //         scrollTop: false
  //       })
  //     }
  //   }
  //   // if (windowHeight < 600) {
  //   //   height = 173;
  //   // } else if (windowHeight > 700) {
  //   //   height = 49;
  //   // } else if (windowHeight < 620) {
  //   //   height = 51;
  //   // } else {
  //   //   height = 183;
  //   // }
  //   // if (e.scrollTop > height) {
  //   //   if (!this.data.scrollTop) {
  //   //     this.setData({
  //   //       scrollTop: true
  //   //     })
  //   //   }
  //   // } else {
  //   //   if (this.data.scrollTop) {
  //   //     this.setData({
  //   //       scrollTop: false
  //   //     })
  //   //   }
  //   // }
  // }
})