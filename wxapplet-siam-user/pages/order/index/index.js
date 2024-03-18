import https from '../../../utils/http';
import toastService from '../../../utils/toast.service';
import dateHelper from '../../../utils/date-helper';
import systemStatus from '../../../utils/system-status';
import authService from '../../../utils/auth';
import GlabalConfig from '../../../utils/global-config';
var pageNo = 1,
  pageSize = 10,
  pageMallNo = 1,
  pageMallSize = 10,
  id, index, isEndList = false, isEndMallList = false;
//获取应用实例
const app = getApp()
Page({
  /**
   * 页面的初始数据
   */
  data: {
    currentTab: 0,
    orderTab: 1,
    winWidth: 0,
    winHeight: 0,
    shopOrderTabList: [{
      modeId: 0,
      modeType: "all",
      modeName: "全部"
    }, {
      modeId: 1,
      modeType: "waitPayment",
      modeName: "待付款"
    },
    {
      modeId: 2,
      modeType: "waitReceived",
      modeName: "待收货"
    },
    {
      modeId: 3,
      modeType: "waitPickedUp",
      modeName: "待自提"
    },
    {
      modeId: 4,
      modeType: "afterSales",
      modeName: "退款/售后"
    }
    ],
    pointsOrderTabList: [{
      modeId: 0,
      modeType: "all",
      modeName: "全部"
    }, {
      modeId: 1,
      modeType: "waitPayment",
      modeName: "待付款"
    },
    {
      modeId: 2,
      modeType: "waitDelivered",
      modeName: "待发货"
    },
    {
      modeId: 3,
      modeType: "waitReceived",
      modeName: "待收货"
    },
    {
      modeId: 4,
      modeType: "afterSales",
      modeName: "退款/售后"
    }
    ],
    list: [],
    mallList: [],
    tabList: [{
      modeId: 0,
      modeName: "外卖订单"
    }, {
      modeId: 1,
      modeName: "商城订单"
    }],
    currentOrderTab: 0,
    keyWords: "",
    inputShowed: false,
    inputVal: "",
    i: 0,
    tabType: ""
  },
  getHeight() {
    //获取用户手机系统信息
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        let winHeight = res.windowHeight; //获取高度
        //获取class为settlement-view并减去这个高度，自适应scrollview的高度
        const query = wx.createSelectorQuery()
        query.select('.swiper-content').boundingClientRect()
        query.selectViewport().scrollOffset()
        query.exec(function (res) {
          console.log(winHeight - res[0].height)
          that.setData({
            winHeight: winHeight - res[0].height
          });
        })
      }
    });
  },
  // 滑动切换tab
  bindSlideChange: function (e) {
    console.log(e)
    // if (e.detail.current == 1) {
    //   if (this.data.mallList.length <= 0) {
    //     this.getMallOrderList();
    //   }
    // }
    pageNo = 1;
    this.setData({
      currentOrderTab: e.detail.current
    });
    if (this.data.currentTab == 0) {
      this.setData({
        modeType:this.data.shopOrderTabList[e.detail.current].modeType
      })
      this.getOrderList(this.data.shopOrderTabList[e.detail.current].modeType, this.data.keyWords);
    }
    if (this.data.currentTab == 1) {
      this.setData({
        modeType:this.data.pointsOrderTabList[e.detail.current].modeType
      })
      this.getMallOrderList(this.data.pointsOrderTabList[e.detail.current].modeType, this.data.keyWords);
    }
  },
  //点击切换 
  clickTab: function (e) {
    console.log(e.target.dataset.current)
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      // 显示加载图标
      this.setData({
        currentTab: e.target.dataset.current,
        currentOrderTab: 0
      });
      if (e.target.dataset.current == 0) {
        this.setData({
          modeType:this.data.shopOrderTabList[0].modeType
        })
        this.getOrderList(this.data.pointsOrderTabList[0].modeType, this.data.keyWords);
      }
      if (e.target.dataset.current == 1) {
        console.log(this.data.pointsOrderTabList[0].modeType);
        this.setData({
          modeType:this.data.pointsOrderTabList[0].modeType
        })
        this.getMallOrderList(this.data.pointsOrderTabList[0].modeType, this.data.keyWords);
      }
    }
  },
  //点击切换 
  clickOrderTab: function (e) {
    console.log(e)
    console.log(e.currentTarget.dataset.current)
    if (this.data.currentOrderTab === e.currentTarget.dataset.current) {
      return false;
    } else {
      pageNo = 1;
      // 显示加载图标
      this.setData({
        currentOrderTab: e.currentTarget.dataset.current
      });
      //console.log(this.data.pointsOrderTabList[e.target.dataset.current].modeType);
      // if (this.data.currentTab == 0) {
      //   this.setData({
      //     modeType:this.data.shopOrderTabList[e.target.dataset.current].modeType
      //   })
      //   this.getOrderList(this.data.shopOrderTabList[e.target.dataset.current].modeType, this.data.keyWords);
      // }
      // if (this.data.currentTab == 1) {
      //   this.setData({
      //     modeType:this.data.pointsOrderTabList[e.target.dataset.current].modeType
      //   })
      //   this.getMallOrderList(this.data.pointsOrderTabList[e.target.dataset.current].modeType, this.data.keyWords);
      // }
    }
  },
  orderDetailsTap(e) {
    id = e.currentTarget.dataset.id;
    index = e.currentTarget.dataset.index;
    wx.navigateTo({
      url: '../detail/detail?id=' + e.currentTarget.dataset.id,
    })
  },
  orderMallDetailsTap(e) {
    id = e.currentTarget.dataset.id;
    index = e.currentTarget.dataset.index;
    console.log(this.data.modeType)
    wx.navigateTo({
      url: '../../internal/mall/order/detail/detail?id=' + e.currentTarget.dataset.id,
    })
  },
  evaluateTip(e) {
    id = e.currentTarget.dataset.id;
    let shopId = e.currentTarget.dataset.shopid;
    wx.navigateTo({
      url: '../evaluate/evaluate?orderId=' + id + '&shopId=' + shopId,
    })
  },
  goToDrink() {
    wx.switchTab({
      url: '../../menu/index/index',
    })
  },
  getOrderList(tabType, keyWords) {
    toastService.showLoading("正在加载...");
    https.request('/api-order/rest/member/order/list', {
      pageNo: pageNo,
      pageSize: pageSize,
      tabType: tabType,
      keyWords: keyWords
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.records.forEach(res => {
          res.createTime = dateHelper.fmtDate(res.createTime);
          res.statusText = systemStatus.statusText(res.status);
          res.paymentModeText = systemStatus.paymentModeText(res.paymentMode);
          res.shopLogoImg= GlabalConfig.ossUrl+res.shopLogoImg;
        })
        this.setData({
          list: result.data.records
        })
      }
    })
  },
  getMallOrderList(tabType, keyWords) {
    toastService.showLoading("正在加载...");
    https.request('/api-mall/rest/member/pointsMall/order/list', {
      pageNo: pageMallNo,
      pageSize: pageMallSize,
      tabType: tabType,
      keyWords: keyWords
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        result.data.records.forEach(res => {
          res.createTime = dateHelper.fmtDate(res.createTime);
          res.statusText = systemStatus.statusMallText(res.status);
          res.paymentModeText = systemStatus.paymentModeText(res.paymentMode);
          res.firstGoodsMainImage= GlabalConfig.ossUrl+res.firstGoodsMainImage;
        })
        this.setData({
          mallList: result.data.records
        })
      }
    })
  },
  search: function (keyWords) {
    console.log(keyWords)
    return new Promise((resolve, reject) => {
      //判断大标签页
      if (this.data.currentTab == 0) {
        this.getOrderList(this.data.shopOrderTabList[this.data.currentOrderTab].modeType, keyWords);
      }
      if (this.data.currentTab == 1) {
        this.getMallOrderList(this.data.pointsOrderTabList[this.data.currentOrderTab].modeType, keyWords);
      }
      resolve([])
      this.setData({
        keyWords: keyWords
      })
    })
  },
  selectResult: function (e) {
    console.log('select result', e.detail)
  },
  searchInputClear(e) {
    console.log(e);
    if (this.data.currentTab == 0) {
      this.getOrderList(this.data.shopOrderTabList[this.data.currentOrderTab].modeType, "");
    }
    if (this.data.currentTab == 1) {
      this.getMallOrderList(this.data.pointsOrderTabList[this.data.currentOrderTab].modeType, "");
    }
    this.setData({
      keyWords:""
    })
  },
  selectTabNumber(){
    https.request('/api-order/rest/member/order/selectTabNumber', {}).then(result => {
      if (result.success) {
        this.data.shopOrderTabList.forEach((item,index)=>{
          if(item.modeType!="all"){
            for (let keyof in result.data) {
              if(keyof.indexOf(item.modeType)!=-1){
                console.log(keyof)
                console.log(result.data[keyof])
                item.number=result.data[keyof];
                continue
              }
           }
          }
        })
        console.log(this.data.shopOrderTabList)
        this.setData({
          shopOrderTabList: this.data.shopOrderTabList
        })
      }
    })
  },
  selectMallTabNumber(){
    https.request('/api-mall/rest/member/pointsMall/order/selectTabNumber', {}).then(result => {
      if (result.success) {
        this.data.pointsOrderTabList.forEach((item,index)=>{
          if(item.modeType!="all"){
            for (let keyof in result.data) {
              if(keyof.indexOf(item.modeType)!=-1){
                console.log(keyof)
                console.log(result.data[keyof])
                item.number=result.data[keyof];
                continue
              }
           }
          }
        })
        console.log(this.data.shopOrderTabList)
        this.setData({
          pointsOrderTabList: this.data.pointsOrderTabList
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    pageNo = 1;
    this.getHeight();
    // this.getMallOrderList("", "");
    this.setData({
      search: this.search.bind(this),
      currentTab:options.currentTab,
      currentOrderTab:options.currentOrderTab,
      modeType:options.modeType
    })
    this.selectTabNumber();
    this.selectMallTabNumber();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  getOrderDetail() {
    https.request('/api-order/rest/member/order/selectById', {
      id: id
    }).then(result => {
      if (result.success) {
        const status = result.data.order.status;
        const createTime = result.data.order.createTime;
        result.data.order.statusText = systemStatus.statusText(status);
        result.data.order.createTime = dateHelper.formatDate(createTime);
        this.setData({
          ["list[" + index + "]"]: result.data.order,
          isOperation: false
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    authService.checkIsLogin().then(result => {
      if (result) {
        console.log(this.data.modeType);
        if (this.data.currentTab == 0) {
          console.log("如果pageNo乘以pageSize不等于pageSize====" + pageNo * pageSize);
          console.log("如果pageNo乘以pageSize不等于pageSize====" + pageSize);
          let no = pageNo;
          if ((pageNo * pageSize) != pageSize) {
            pageSize = no * pageSize;
            pageNo = 1;
            console.log("获取pageSize数" + pageSize)
          }
          this.getOrderList(this.data.modeType, "");
          if (this.data.isOperation) {
            this.getOrderDetail(this.data.modeType, "");
          }
        }
        if (this.data.currentTab == 1) {
          console.log("如果pageNo乘以pageSize不等于pageSize====" + pageMallNo * pageMallSize);
          console.log("如果pageNo乘以pageSize不等于pageSize====" + pageMallSize);
          let no = pageMallNo;
          if ((pageMallNo * pageMallSize) != pageMallSize) {
            pageMallSize = no * pageMallSize;
            pageMallNo = 1;
            console.log("获取pageSize数" + pageMallSize)
          }
          this.getMallOrderList(this.data.modeType, "");
          if (this.data.isOperation) {
            // this.getOrderDetail();
          }
        }
        return
      }
      app.checkIsAuth("scope.userInfo");
    })

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
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    if (this.data.currentTab == 0) {
      pageNo = 1;
      pageSize = 10;
      isEndList = false;
      this.getOrderList(this.data.shopOrderTabList[this.data.currentOrderTab].modeType,this.data.keyWords);
    }
    if (this.data.currentTab == 1) {
      pageMallNo = 1;
      pageMallSize = 10;
      isEndMallList = false;
      this.getMallOrderList(this.data.pointsOrderTabList[this.data.currentOrderTab].modeType,this.data.keyWords);
    }
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    toastService.showLoading("正在加载...");
    
    if (this.data.currentTab == 0) {
      if (isEndList) {
        toastService.showToast("没有更多啦~");
        return;
      }
      pageNo = pageNo + 1;
      pageSize = 10;
      https.request('/api-order/rest/member/order/list', {
        pageNo: pageNo,
        pageSize: pageSize,
        tabType: this.data.pointsOrderTabList[this.data.currentOrderTab].modeType,
        keyWords: this.data.keyWords
      }).then(result => {
        toastService.hideLoading();
        if (result.success) {
          if (result.data.records.length > 0) {
            result.data.records.forEach(res => {
              res.createTime = dateHelper.formatDate(res.createTime);
              res.statusText = systemStatus.statusText(res.status);
              res.shopLogoImg= GlabalConfig.ossUrl+res.shopLogoImg;
              this.data.list.push(
                res
              )
            })
            this.setData({
              list: this.data.list
            })
            return;
          }
          isEndList = true;
          toastService.showToast("没有更多啦~");
          pageNo = pageNo - 1;
        }
      })
    }
    if (this.data.currentTab == 1) {
      if (isEndMallList) {
        toastService.showToast("没有更多啦~");
        return;
      }
      pageMallNo = pageMallNo + 1;
      pageMallSize = 10;
      https.request('/api-mall/rest/member/pointsMall/order/list', {
        pageNo: pageMallNo,
        pageSize: pageMallSize,
        tabType: this.data.pointsOrderTabList[this.data.currentOrderTab].modeType,
        keyWords: this.data.keyWords
      }).then(result => {
        toastService.hideLoading();
        if (result.success) {
          if (result.data.records.length > 0) {
            result.data.records.forEach(res => {
              res.createTime = dateHelper.formatDate(res.createTime);
              res.statusText = systemStatus.statusText(res.status);
              res.firstGoodsMainImage= GlabalConfig.ossUrl+res.firstGoodsMainImage;
              this.data.mallList.push(
                res
              )
            })
            this.setData({
              mallList: this.data.mallList
            })
            return;
          }
          isEndMallList = true;
          toastService.showToast("没有更多啦~");
          pageMallNo = pageMallNo - 1;
        }
      })
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})