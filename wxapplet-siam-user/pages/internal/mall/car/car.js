var base64 = require("../../../../components/images/base64");
import GlobalConfig from '../../../../utils/global-config';
import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
const app = getApp();
var winHeight = 0,
  totalPrice = 0,
  totalNum = 0;
Page({
  data: {
    icon: base64.icon20,
    slideButtons: [{
      src: '/assets/common/icon-del.svg', // icon的路径
    }],
    items: [
      // { value: 'USA', name: '美国', num: 1, price: 30, disable:true },
    ],
    minusStatus: 'disable',
    disabled: ""
  },
  onLoad: function (option) {

  },
  onShow: function () {
    toastService.showLoading();
    authService.checkIsLogin().then(result => {
      this.getLoveIt();
      if (result) {
        this.getShoppingCartList();
        return;
      }
      toastService.hideLoading();
      this.setData({
        items: []
      })
      this.initData();
      app.checkIsAuth("scope.userInfo");
    });
    console.log(this.data)
  },
  initData() {
    const items = this.data.items;
    totalNum = 0, totalPrice = 0;
    for (let i = 0; i < items.length; i++) {
      items[i].checked = true;
      if (items[i].disable) {
        items[i].checked = false;
      }
      if (items[i].checked) {
        totalPrice += items[i].price * items[i].number; //初始化被选中的商品的总金额
      }
      totalNum = totalNum + items[i].number;
    }
    this.setTabBarBadge(totalNum);
    this.setData({
      items: items,
      totalPrice: totalPrice
    })
    this.getFullReductionRule();
    this.getAutoHeight();
  },
  getAutoHeight() {
    //获取用户手机系统信息
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        winHeight = res.windowHeight; //获取高度
        //console.log(winHeight)
        //获取class为settlement-view并减去这个高度，自适应scrollview的高度
        const query = wx.createSelectorQuery()
        query.select('.settlement-view').boundingClientRect()
        query.selectViewport().scrollOffset()
        if (query) {
          query.exec(function (res) {
            console.log(res)
            winHeight = winHeight - (res[0] ? res[0].height : 0);
            that.setData({
              winHeight: winHeight
            })
          })
          return
        }
        that.setData({
          winHeight: winHeight
        })
      }
    });
  },
  slideButtonTap(e) {
    //console.log('slide button tap', e)
    var that = this;
    let items = that.data.items;
    let cheakedBoxIndex = e.currentTarget.dataset.checkboxindex; //商品下标
    let number = items[cheakedBoxIndex].number; //该商品的数量
    let id = items[cheakedBoxIndex].id; //该商品的id
    if (items[cheakedBoxIndex].disable) {
      that.updateNumber(id, number, 0, function callback() {
        items.splice(cheakedBoxIndex, 1); //删除商品
        that.setData({
          items: items
        });
        totalNum -= number;
        that.setTabBarBadge(totalNum);
        
      })
      return
    }

    toastService.showModal(null, "确定不要这个了吗？",
      function confirm() {
        that.updateNumber(id, number, 0, function callback() {
          totalPrice = totalPrice - (items[cheakedBoxIndex].price * number); //总价格减去当前商品数量总和
          items.splice(cheakedBoxIndex, 1); //删除商品
          that.setData({
            items: items,
            totalPrice: totalPrice
          });
          that.getAutoHeight();
          that.getFullReductionRule();
          console.log(number);
          totalNum -= number;
          console.log(totalNum);
          that.setTabBarBadge(totalNum);
        });
      }, null
    );
  },
  checkboxChange(e) {
    //console.log('checkbox发生change事件，携带value值为：', e)
    const items = this.data.items; //获取该用户的所有商品列表
    const values = e.detail.value; //获取点击选中或取消选中的下标值

    //遍历分类信息给第一级分类为false，提交的时候对应各级分类
    for (var j in items) {
      items[j].checked = false;
    }
    totalPrice = 0
    let totalNum = 0,
      fullPriceReduction = 0,
      packingCharges = 0;

    for (var i in values) {
      items[values[i]].checked = true; //给选中的第二级分类的checked设置为true
      //console.log(items[values[i]].price + "------------" + items[values[i]].number)
      totalPrice = totalPrice + (items[values[i]].price * items[values[i]].number);
      fullPriceReduction = fullPriceReduction + (items[values[i]].price * items[values[i]].number);
      totalNum = totalNum + items[values[i]].number;
      packingCharges = packingCharges + (items[values[i]].packingCharges * items[values[i]].number);
    }
    this.setTabBarBadge(totalNum); //设置角标
    this.setData({
      items,
      totalPrice: totalPrice,
      fullPriceReduction: fullPriceReduction,
      packingCharges: packingCharges
    });
    this.getFullReductionRule();
  },
  //事件处理函数
  /*点击减号*/
  bindMinus: function (e) {
    var that = this;
    let numList = e.currentTarget.dataset.num.split(","); //获取购物车的列表下表和商品数量
    let items = this.data.items; //获取购物车列表信息
    //点击减号后则当前商品的数量-1,如果这个商品不能被操作则return
    if (items[numList[0]].disable) {
      return
    }
    if (!items[numList[0]].checked) {
      toastService.showToast("未选择商品");
      return;
    }

    //点击减号后,当前商品的数量小于1,就进行删除该商品
    // items[numList[0]].number = Number(numList[1]) - 1;
    let number = items[numList[0]].number - 1;
    //重新计算被选中的商品的总金额
    totalPrice = 0;
    let packingCharges = items[numList[0]].packingCharges; //获取该点击的商品的包装费
    for (let i = 0; i < items.length; i++) {
      if (items[i].checked) {
        //如果被选中则进行总额计算（已经计算出的价格+商品价格*商品的数量)
        if (numList[0] == i) {
          //console.log(number)
          totalPrice = totalPrice + (items[i].price * number);
        } else {
          //console.log(items[i].price * items[i].number)
          totalPrice = totalPrice + (items[i].price * items[i].number);
        }
      }
    }
    if (numList[1] == 1) {
      toastService.showModal(null, "确定不要这个了吗？",
        function confirm() {
          that.updateNumber(items[numList[0]].id, 1, 0, function callback() {
            items.splice(Number(numList[0]), 1); //列表减一
            totalNum--;
            that.setData({
              items: items,
              totalPrice: totalPrice,
              packingCharges: that.data.packingCharges - packingCharges
            })
            that.setTabBarBadge(totalNum);
            that.getFullReductionRule();
            that.getAutoHeight();
          })
        }
      )
      return
    }
    //点击减号后,当前商品的数量小于1,就进行删除该商品
    items[numList[0]].number = Number(numList[1]) - 1;
    this.updateNumber(items[numList[0]].id, 1, 0, function callback() {
      totalNum--;
      that.setTabBarBadge(totalNum);
      that.setData({
        items: items,
        totalPrice: totalPrice,
        packingCharges: that.data.packingCharges - packingCharges //所有商品包装费减去当前点击的商品的包装费
      })
      that.getFullReductionRule();
    })
  },
  /*点击加号*/
  bindPlus: function (e) {
    var self = this;
    let numList = e.currentTarget.dataset.num.split(",");
    let items = this.data.items;
    totalPrice = 0;
    
    if (items[numList[0]].disable) {
      return
    }
    if (!items[numList[0]].checked) {
      toastService.showToast("未选择商品");
      return;
    }
    items[numList[0]].number = Number(numList[1]) + 1; //当前商品的数量+1
    let packingCharges = items[numList[0]].packingCharges; //获取该点击的商品的包装费;
    this.updateNumber(items[numList[0]].id, 1, 1, function callback() {
      //重新计算被选中的商品的总金额
      for (let i = 0; i < items.length; i++) {
        if (items[i].checked) {
          //如果被选中则进行总额计算（已经计算出的价格+商品价格*商品的数量)
          totalPrice = totalPrice + (items[i].price * items[i].number);
        }
      }
      totalNum++;
      self.setTabBarBadge(totalNum);
      self.setData({
        items: items,
        totalPrice: totalPrice,
        packingCharges: self.data.packingCharges + (packingCharges)
      });
      self.getFullReductionRule();
    })
  },
  setTabBarBadge(num) {
    if (num) {
      wx.setTabBarBadge({
        index: 2,
        text: String(num)
      })
      return
    }
    wx.removeTabBarBadge({
      index: 2,
    })
  },
  goToPay() {
    //判断店铺是否打烊
    var list = this.data.items;
    var orderDetail = {};
    orderDetail.actualPrice = this.data.totalPrice;
    orderDetail.packingCharges = this.data.packingCharges;
    orderDetail.orderDetailList = [];
    var isTap=false;
    for (var key in list) {
      if (list[key].checked) {
        isTap=true;
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
    }
    if(isTap){
      wx.navigateTo({
        url: '../pay/pay?orderDetail=' + JSON.stringify(orderDetail) + "&payType=store",
      });
      return;
    }
    this.goToTap();
  },
  goToDrink() {
    wx.switchTab({
      url: '../../mall/index/index',
    })
  },
  commodityDetailTap(e) {
    wx.navigateTo({
      url: '../detail/detail?id=' + e.currentTarget.dataset.id,
    })
  },
  getShoppingCartList() {
    https.request('/api-mall/rest/member/pointsMall/shoppingCart/list', {
      pageNo: -1,
      pageSize: 20
    }).then(result => {
      if (result.success) {
        var packingCharges = 0;
        result.data.records.forEach(result => {
          if (result.mainImage) {
            result.mainImage = GlobalConfig.ossUrl + result.mainImage;
          }
          let specList = "";
          for (var key in JSON.parse(result.specList)) {
            specList = (specList ? specList + "/" : specList) + JSON.parse(result.specList)[key];
          }
          console.log(result)
          result.restructure = specList;
          result.disable = result.goodsStatus == 1 || result.goodsStatus == 3 || result.goodsStatus == 4 ? true : false;
          packingCharges = result.goodsStatus == 1 || result.goodsStatus == 3 || result.goodsStatus == 4 ? packingCharges : packingCharges + (result.packingCharges * result.number);
        })
        //console.log(result.data.records)
        this.setData({
          items: result.data.records,
          packingCharges: packingCharges
        });
        //if (result.data.records.length > 0) {
        this.initData();
        //}
      }
    })
  },
  //获取满减规则
  getFullReductionRule() {
    toastService.showLoading("正在加载...", true);
    https.request('/api-mall/rest/pointsMall/fullReductionRule/list', {
      pageNo: -1,
      pageSize: 1
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        let fullPriceReduction = 0,
          fullReductionRuleName = "",
          fullPriceReductionIsHidden = false,
          limitedPrice = 0;
        for (let i = 0; i < result.data.records.length; i++) {
          if (totalPrice + this.data.packingCharges >= result.data.records[i].limitedPrice) {
            if (result.data.records[i].limitedPrice > limitedPrice) {
              limitedPrice = result.data.records[i].limitedPrice;
              fullPriceReduction = totalPrice - result.data.records[i].reducedPrice;
              fullReductionRuleName = result.data.records[i].name;
              fullPriceReductionIsHidden = true;
            }
          }
        }
        this.setData({
          fullPriceReduction: fullPriceReduction,
          fullReductionRuleName: fullReductionRuleName,
          fullPriceReductionIsHidden: fullPriceReductionIsHidden
        })
      }
    })
  },
  getLoveIt() {
    toastService.showLoading();
    https.request('/api-mall/rest/pointsMall/goods/guessLikeGoodsList', {}).then(result => {
      toastService.hideLoading();
      if (result.success) {
        //console.log(result.data)
        if (result.success) {
          result.data.forEach(function (item, index) {
            item.mainImage = GlobalConfig.ossUrl + item.mainImage;
          })
          this.setData({
            likeList: result.data
          });
        }
      }
    })
  },
  updateNumber(id, number, type, callbak) {
    https.request('/api-mall/rest/member/pointsMall/shoppingCart/updateNumber', {
      id: id,
      number: number,
      type: type
    }).then(result => {
      if (result.success) {
        callbak();
      }
    })
  },
  goToTap() {
    toastService.showModal(null, "至少购买一件商品", null, null, false)
  },
});