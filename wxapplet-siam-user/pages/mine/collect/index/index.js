import GlobalConfig from '../../../../utils/global-config';
import https from '../../../../utils/http';
import toastService from '../../../../utils/toast.service';
import authService from '../../../../utils/auth';
import utilHelper from '../../../../utils/util';
import CustomPage from '../../../../base/CustomPage';
import dateHelper from '../../../../utils/date-helper';
var base64 = require("../../../../components/images/base64");
Page({

   /** 
    * 页面的初始数据
    */
   data: {
      inputShowed: false,
      inputVal: "",
      i: 0,
      currentTab: 0,
      modeList: [{
         modeId: 0,
         modeName: "全部"
      }, {
         modeId: 1,
         modeName: "已买过"
      }],
      tabList: [{
         modeId: 0,
         modeName: "外卖收藏"
      }, {
         modeId: 1,
         modeName: "商城收藏"
      }],
      collectTab: 0,
      checkValue: 0,
      isShowCheckbox: false,
      collectList: [],
      icon: base64.icon20,
      slideButtons: [{
         src: '/assets/common/icon-del.svg', // icon的路径
      }],
      isAlls: false,
      searchGoodsName: ""
   },
   // 滑动切换tab
   bindSlideChange: function (e) {
      this.setData({
         currentTab: e.detail.current
      });
      // this.getHeight();
   },
   //点击切换 
   clickTab: function (e) {
      if (this.data.currentTab === e.target.dataset.current) {
         return false;
      } else {

         if (this.data.collectTab == 0) {
            if (e.target.dataset.current == 0) {
               this.getMemberCollect(false, this.data.searchGoodsName);
            } else {
               this.getMemberCollect(true, this.data.searchGoodsName);
            }
         }
         if (this.data.collectTab == 1) {
            if (e.target.dataset.current == 0) {
               this.getPointsMallCollect(false, this.data.searchGoodsName);
            } else {
               this.getPointsMallCollect(true, this.data.searchGoodsName);
            }
         }
         this.setData({
            currentTab: e.target.dataset.current
         });
         // this.getHeight();
      }
   },
   clickCollectTab: function (e) {
      if (this.data.collectTab === e.target.dataset.current) {
         return false;
      } else {
         if (e.target.dataset.current == 0) {
            this.getMemberCollect(false, this.data.searchGoodsName);
         } else {
            this.getPointsMallCollect(false, this.data.searchGoodsName);
         }
         this.setData({
            collectTab: e.target.dataset.current,
            currentTab: 0,
            isAlls:false
         });
         // this.getHeight();
      }
   },
   search: function (value) {
      console.log(value)
      return new Promise((resolve, reject) => {
         //判断大标签页
         if (this.data.collectTab == 0) {
            //判断小标签页
            if (this.data.currentTab == 0) {
               this.getMemberCollect(false, value);
            }
            if (this.data.currentTab == 1) {
               this.getMemberCollect(true, value);
            }
            resolve([])
         }
         if (this.data.collectTab == 1) {
            //判断小标签页
            if (this.data.currentTab == 0) {
               this.getPointsMallCollect(false, value);
            }
            if (this.data.currentTab == 1) {
               this.getPointsMallCollect(true, value);
            }
            resolve([])
         }
         this.setData({
            searchGoodsName: value
         })
      })
   },
   selectResult: function (e) {
      console.log('select result', e.detail)
   },
   searchInputClear(e) {
      console.log(e);
      if (this.data.collectTab == 0) {
         //判断小标签页
         if (this.data.currentTab == 0) {
            this.getMemberCollect(false, "");
         }
         if (this.data.currentTab == 1) {
            this.getMemberCollect(true, "");
         }
      }
      if (this.data.collectTab == 1) {
         //判断小标签页
         if (this.data.currentTab == 0) {
            this.getPointsMallCollect(false, "");
         }
         if (this.data.currentTab == 1) {
            this.getPointsMallCollect(true, "");
         }
      }
   },
   getPointsMallCollect(isBuy, goodsName) {
      let data = {
         pageNo: -1,
         pageSize: 20
      };
      if (isBuy) {
         data.isBuy = true;
      }
      if (goodsName) {
         data.goodsName = goodsName;
      }
      this.data.collectList = [];
      https.request('/api-goods/rest/member/pointsMall/goodsCollect/list', data).then(result => {
         if (result.success) {
            result.data.records.forEach((item, index) => {
               item.mainImage = GlobalConfig.ossUrl + item.mainImage
               this.data.collectList.push(item);

            })
            this.setData({
               collectList: this.data.collectList
            })
         }
      })
   },
   getMemberCollect(isBuy, goodsName) {
      let data = {
         pageNo: -1,
         pageSize: 20
      };
      if (isBuy) {
         data.isBuy = true;
      }
      if (goodsName) {
         data.goodsName = goodsName;
      }
      this.data.collectList = [];
      https.request('/api-goods/rest/member/goodsCollect/list', data).then(result => {
         if (result.success) {
            result.data.records.forEach((item, index) => {
               item.mainImage = GlobalConfig.ossUrl + item.mainImage
               this.data.collectList.push(item);

            })
            this.setData({
               collectList: this.data.collectList
            })
         }
      })
   },
   bindShowCheckbox(e) {
      this.setData({
         isShowCheckbox: this.data.isShowCheckbox ? false : true
      })
   },
   bindPointsMallDetails(e) {
      wx.navigateTo({
         url: '../../../mall/detail/detail?id=' + e.currentTarget.dataset.id,
      })
   },
   bindMemberDetails(e) {
      wx.navigateTo({
         url: '../../../menu/detail/detail?id=' + e.currentTarget.dataset.id+"&shopId="+e.currentTarget.dataset.shopid,
      })
   },
   checkboxChange(e) {
      console.log(e)
      var values = e.detail.value;
      var deleteIds = [];
      for (var key in values) {
         deleteIds.push(this.data.collectList[key].goodsId);
      }
      this.setData({
         goodsIdList: deleteIds,
         isAlls: values.length != this.data.collectList.length ? false : true
      })
   },
   allChange(e) {
      console.log(e)
      var deleteIds = [];
      this.data.collectList.forEach((item, index) => {
         if (!this.data.isAlls) {
            item.checked = true;
            deleteIds.push(item.goodsId);
         } else {
            item.checked = false;
         }
      });
      console.log(deleteIds)
      this.setData({
         collectList: this.data.collectList,
         goodsIdList: deleteIds,
         isAlls: this.data.isAlls ? false : true
      })
   },
   slideButtonTap(e){
      var deleteIds = [];
      let goodsid = e.currentTarget.dataset.goodsid; //商品下标
      deleteIds.push(goodsid)
      this.setData({
         goodsIdList: deleteIds,
      });
      this.batchDelete();
   },
   batchDelete() {
      if (this.data.goodsIdList.length <= 0) {
         toastService.showToast("请选择要操作的数据")
         return
      }
      var that=this;
      toastService.showModal(null, "确定要删除这" + this.data.goodsIdList.length + "条数据吗?", function confirm() {
         let url = "/api-goods/rest/member/pointsMall/goodsCollect/batchDelete"
         if (that.data.collectTab == 0) {
            url = "/api-goods/rest/member/goodsCollect/batchDelete"
         }
         https.request(url, {
            goodsIdList: that.data.goodsIdList
         }).then(result => {
            if (result.success) {
               toastService.showToast(result.message);
               //判断大标签页
               if (that.data.collectTab == 0) {
                  //判断小标签页
                  if (that.data.currentTab == 0) {
                     that.getMemberCollect();
                  }
                  if (that.data.currentTab == 1) {
                     that.getMemberCollect(true);
                  }
               }
               if (that.data.collectTab == 1) {
                  //判断小标签页
                  if (that.data.currentTab == 0) {
                     that.getPointsMallCollect();
                  }
                  if (that.data.currentTab == 1) {
                     that.getPointsMallCollect(true);
                  }
               }
            }
         })
      }, null, true);
   },
   /**
    * 生命周期函数--监听页面加载
    */
   onLoad: function (options) {
      this.setData({
         search: this.search.bind(this)
      });
      this.getMemberCollect();
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