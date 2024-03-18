import GlobalConfig from '../../../../utils/global-config';
import https from '../../../../utils/http';
import toastService from '../../../../utils/toast.service';
import authService from '../../../../utils/auth';
import utilHelper from '../../../../utils/util';
import CustomPage from '../../../../base/CustomPage';
import dateHelper from '../../../../utils/date-helper';
var menuList = [];
var app = getApp();
Page({

   /**
    * 页面的初始数据
    */
   data: {
      specificationsDialog: false,
   },
   getMenuList() {
      toastService.showLoading();
      var self = this;
      https.request('/api-mall/rest/pointsMall/menu/listWithGoods',{}).then(result => {
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
   openSpecifications(e) {
      toastService.showLoading();
      this.setData({
         specificationsDialog: true,
         goodsId: e.currentTarget.dataset.goodsid
      });
      this.getCommodityDetails(e.currentTarget.dataset.goodsid);
   },
   getCommodityDetails(id) {
      https.request('/api-mall/rest/pointsMall/goods/selectById', {
         id: id,
         pageNo: -1,
         pageSize: 20
      }).then(result => {
         toastService.hideLoading();
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
   commodityDetailTap(e) {
      console.log(e.currentTarget.dataset.id)
      wx.navigateTo({
         url: '../detail/detail?id=' + e.currentTarget.dataset.id,
      })
   },
   selectByGoodsId(goodsId) {
      console.log(this.data)
      https.request('/api-mall/rest/pointsMall/goodsSpecificationOption/selectByGoodsId', {
         goodsId: goodsId
      }).then(result => {
         if (result.success && result.data) {
            //重新设置商品的规格等数据的格式
            //let goodsSpecs = {};
            let specList = result.data;
            let price = 0;
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
               specListString: specListString?specListString:"暂无规格",
               specList: JSON.stringify(specList)=="{}"?null:specList
            })
         }
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
            //console.log(specList[key][keyof].price)
            if (specList[key][keyof].checked) {
               price = price + specList[key][keyof].price;
               specListString = (specListString ? specListString + "/" : specListString) + specList[key][keyof].name;

            }
         }
      }
      // let totalPrice = price * this.data.commodityNum;
      // let fullPriceReduction = 0,
      //    fullReductionRuleName = "",
      //    fullPriceReductionIsHidden = false,
      //    limitedPrice = 0;
      // //console.log(this.data.fullReductionRuleList)
      // for (let i = 0; i < this.data.shopInfo.fullReductionRuleList.length; i++) {
      //    //console.log(this.data.fullReductionRuleList[i].limitedPrice)
      //    if (totalPrice >= this.data.shopInfo.fullReductionRuleList[i].limitedPrice) {
      //       if (this.data.shopInfo.fullReductionRuleList[i].limitedPrice > limitedPrice) {
      //          limitedPrice = this.data.shopInfo.fullReductionRuleList[i].limitedPrice;
      //          fullPriceReduction = (totalPrice + this.data.data.packingCharges) - this.data.shopInfo.fullReductionRuleList[i].reducedPrice;
      //          fullReductionRuleName = this.data.shopInfo.fullReductionRuleList[i].name;
      //          fullPriceReductionIsHidden = true;
      //       }
      //    }
      // }

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
      // if(!goodsSpecs){
      //   toastService.showToast("请选择规格");
      //   return
      // }
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
               goodsId: this.data.goodsInfo.id,
               specList: JSON.stringify(goodsSpecs)
            }
            https.request('/api-mall/rest/member/pointsMall/shoppingCart/insert', data).then(result => {
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
   /**
    * 生命周期函数--监听页面加载
    */
   onLoad: function (options) {
      this.getMenuList();
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