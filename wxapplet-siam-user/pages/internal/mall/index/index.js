import GlobalConfig from '../../../../utils/global-config';
import https from '../../../../utils/http';
import toastService from '../../../../utils/toast.service';
import authService from '../../../../utils/auth';
const app = getApp();
var pageNo = 1,
  pageSize = 10;
Page({

   /**
    * 页面的初始数据
    */
   data: {
      indicatorDots: true,
      autoplay: true,
      interval: 5000,
      duration: 1000,
      //beforeColor: "white",//指示点颜色,
      afterColor: "#f1a142", //当前选中的指示点颜色  
      opacity: 0.4,
      recommendGoodsList:[],
      specList:[],
      isMore:false
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
   getCarouselList() {
      https.request('/api-goods/rest/advertisement/list', {
         type: 3,
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
   getClassifyList() {
      https.request('/api-goods/rest/pointsMall/menu/list', {
         pageNo: 1,
         pageSize: 8
      }).then(result => {
         if (result.success) {
            result.data.records.forEach(function (item, index) {
               item.iconUrl = GlobalConfig.ossUrl + item.icon;
            })
            this.setData({
               classifyList: result.data.records
            });
         }
      })
   },
   getRecommendGoodsList() {
      toastService.showLoading();
      https.request('/api-goods/rest/pointsMall/goods/recommendGoodsList', {
         pageNo: pageNo,
         pageSize: pageSize
      }).then(result => {
         toastService.hideLoading();
         wx.stopPullDownRefresh();
         if (result.success) {
            result.data.records.forEach(function (item, index) {
               item.mainImage = GlobalConfig.ossUrl + item.mainImage;
            })
            this.data.recommendGoodsList=[];
            this.setData({
               recommendGoodsList: result.data.records,
               isMore:result.data.hasNextPage?false:true
            });
         }
      })
   },
   commodityDetailTap(e) {
      console.log(e.currentTarget.dataset.id)
      wx.navigateTo({
         url: '../detail/detail?id=' + e.currentTarget.dataset.id,
      })
   },
   bindToMenu(e) {
      wx.navigateTo({
         url: '../menu/menu',
      })
   },
   getSystemUsageRecord() {
      https.request('/api-goods/rest/systemUsageRecord/insert', {
         type: "intoPointsMall"
      }).then(result => {
         if (result.success) {

         }
      })
   },
   openSpecifications(e) {
      this.selectByGoodsId(e.currentTarget.dataset.index);
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
      let price = this.data.goods.price;
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

      this.setData({
         specList: specList,
         priceAfter:price,
         specListString: specListString
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
               goodsId: e.currentTarget.dataset.goodsid,
               specList: JSON.stringify(goodsSpecs)
            }
            toastService.hideLoading();
            https.request('/api-goods/rest/member/pointsMall/shoppingCart/insert', data, authService.getToken()).then(result => {
               if (result.success) {
                  toastService.showToast("加入购物车成功");
                  this.setData({
                     specificationsDialog: false
                  });
                  app.getShoppingCarNumber();
               }
            })
            return;
         }
         toastService.hideLoading();
         app.checkIsAuth("scope.userInfo");
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
   selectByGoodsId(index) {
      let goods = this.data.recommendGoodsList[index];
      https.request('/api-goods/rest/pointsMall/goodsSpecificationOption/selectByGoodsId', {
         goodsId: goods.id
      }).then(result => {
         if (result.success) {
            //重新设置商品的规格等数据的格式
            //let goodsSpecs = {};
            let specList = result.data;
            let price = goods.price;
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
            console.log(goods);
            console.log(specList);
            this.setData({
               specListString: specListString?specListString:"暂无规格",
               specList: JSON.stringify(specList)=="{}"?null:specList,
               goods: goods,
               specificationsDialog: true,
               priceAfter:price
            })
         }
      })
   },
   /**
    * 生命周期函数--监听页面加载
    */
   onLoad: function (options) {
      pageNo=1;
      this.getSystemUsageRecord();
      this.getRecommendGoodsList();
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
      //pageNo=1;
      app.getShoppingCarNumber();
      this.getClassifyList();
      //this.getRecommendGoodsList();
      this.getCarouselList();
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
      pageNo=1;
      this.getRecommendGoodsList();
     
   },

   /**
    * 页面上拉触底事件的处理函数
    */
   onReachBottom: function () {
      console.log("1111111111")
      toastService.showLoading("正在加载...");
      var that=this;
      pageNo = pageNo + 1;
      https.request('/api-goods/rest/pointsMall/goods/recommendGoodsList', {
         pageNo: pageNo,
         pageSize: pageSize
      }).then(result => {
         toastService.hideLoading();
         if (result.success) {
            console.log(result.data)
            if(result.data.records.length<=0){
               pageNo=pageNo-1;
               that.setData({
                  isMore:result.data.hasNextPage?false:true
               })
               //toastService.showToast("没有更多啦~");
               return;
            }
            result.data.records.forEach(function (item, index) {
               item.mainImage = GlobalConfig.ossUrl + item.mainImage;
               that.data.recommendGoodsList.push(item);
            });
            console.log(this.data.recommendGoodsList)
            this.setData({
               recommendGoodsList: this.data.recommendGoodsList,
               isMore:result.data.hasNextPage?false:true
            });
         }
      });
   },

   /**
    * 用户点击右上角分享
    */
   onShareAppMessage: function () {

   }
})