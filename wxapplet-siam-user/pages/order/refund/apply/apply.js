import https from '../../../../utils/http';
import authService from '../../../../utils/auth';
import toastService from '../../../../utils/toast.service';
import dateHelper from '../../../../utils/date-helper';
import systemStatus from '../../../../utils/system-status';
import GlobalConfig from '../../../../utils/global-config';
import util from '../../../../utils/util';
const app = getApp();
var submitImages = [],
  orderRefundGoodsListStr = [],
  isCouponsDiscountPrice;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    uploaderImages: [],
    choiceReasonApplyDialog: false,
    notSelected: false,
    isSelectAll: true,
    refundReasonApply: [{
        value: 9,
        name: "商家通知我卖完了",
        checked: false
      }, {
        value: 10,
        name: "商家沟通态度差",
        checked: false
      },
      {
        value: 11,
        name: "骑手沟通态度差",
        checked: false
      },
      {
        value: 12,
        name: "送太慢了，等太久了",
        checked: false
      },
      {
        value: 13,
        name: "商品撒漏/包装破损",
        checked: false
      },
      {
        value: 14,
        name: "商家少送商品",
        checked: false
      },
      {
        value: 15,
        name: "商家送错商品",
        checked: false
      },
      {
        value: 16,
        name: "口味不佳/个人感受不好",
        checked: false
      }, {
        value: 17,
        name: "餐品内有异物",
        checked: false
      }, {
        value: 18,
        name: "食用后引起身体不适",
        checked: false
      }, {
        value: 19,
        name: "商品变质/有异味",
        checked: false
      }, {
        value: 20,
        name: "其他",
        checked: false
      }
    ],
  },
  getOrderDetail(orderId) {
    toastService.showLoading();
    https.request('/api-order/rest/member/order/selectById', {
      id: orderId,
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {
        const status = result.data.order.status;
        const createTime = result.data.order.createTime;
        result.data.order.statusText = systemStatus.statusText(status);
        result.data.order.createTime = dateHelper.fmtDate(createTime);
        let actualPrice = 0;
        //解析订单商品的规格
        result.data.orderDetailList.forEach(orderDetailList => {
          let specListAnalysis = "";
          for (var key in JSON.parse(orderDetailList.specList)) {
            specListAnalysis = (specListAnalysis ? specListAnalysis + "/" : specListAnalysis) + JSON.parse(orderDetailList.specList)[key];
          }
          orderDetailList.specListAnalysis = specListAnalysis;
          orderDetailList.mainImage = GlobalConfig.ossUrl + orderDetailList.mainImage;
          orderDetailList.checked = true;
          orderDetailList.oldNumber = orderDetailList.number;
          //actualPrice=actualPrice+(orderDetailList.price*orderDetailList.number);
        })
        result.data.order.oldActualPrice = result.data.order.actualPrice;
        //result.data.order.actualPrice=actualPrice;
        this.setData({
          order: result.data.order,
          orderDetailList: result.data.orderDetailList
        })
      }
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
  uploadImage: function (e) {
    wx.showActionSheet({
      itemList: ['拍照', '从手机相册选择'],
      success: (res) => {
        wx.chooseImage({
          count: res.tapIndex == 0 ? 1 : 1,
          sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
          sourceType: [res.tapIndex == 0 ? 'camera' : 'album'],
          success: (result) => {
            let filePath = result.tempFilePaths[0];

            this.uploadImageInner(filePath, (data) => {
              let image = GlobalConfig.ossUrl + data.data;
              this.data.uploaderImages.push(image);
              submitImages.push(data.data);
              this.setData({
                uploaderImages: this.data.uploaderImages
              });
            })
          }
        })
      }
    });
  },
  uploadImageInner: function (filePath, callback) {
    toastService.showLoading('正在上传...', true); //http://192.168.1.12:8081
    const url = GlobalConfig.baseUrl + '/api-user/rest/member/uploadSingleImage';
    https.uploadImage({
      url: url,
      filePath: filePath,
      success: (data) => {
        toastService.showSuccess('上传成功');
        callback && callback(data);
      },
      fail: (error) => {
        console.log('上传失败-->' + error)
        toastService.showError('上传失败');
      },
      complete: () => {
        //toastService.hideLoading();
      }
    }).then(uploadTask => {
      uploadTask.onProgressUpdate((res) => {
        console.log('上传进度', res.progress)
        console.log('已经上传的数据长度', res.totalBytesSent)
        console.log('预期需要上传的数据总长度', res.totalBytesExpectedToSend)
      })
    })
  },
  closeImage(e) {
    let self = this;
    toastService.showModal(null, "确定要删除这张图片吗?", function confirm() {
      self.data.uploaderImages.splice(e.currentTarget.dataset.index, 1);
      submitImages.splice(e.currentTarget.dataset.index, 1);
      self.setData({
        uploaderImages: self.data.uploaderImages
      });
    });
  },
  isAllowApplyRefund() {

    this.setData({
      choiceReasonApplyDialog: this.data.choiceReasonApplyDialog ? false : true,
      refundReasonApply: this.data.refundReasonApply
    });
  },
  cancelOrderNoReasonApply() {
    if (!this.data.cancelOrderNoReason) {
      toastService.showToast("请选择退款原因");
      return
    }
    this.data.refundReasonApply[this.data.cancelOrderNoReason].checked = true;
    this.setData({
      notSelected: true,
      choiceReasonApplyDialog: this.data.choiceReasonApplyDialog ? false : true,
      oldCancelOrderNoReason: this.data.cancelOrderNoReason,
      refundReasonApply: this.data.refundReasonApply
    })
  },
  radioChange(e) {
    this.setData({
      cancelOrderNoReason: e.detail.value
    })
  },
  bindSelectAll(e) {
    let isSelectAll = this.data.isSelectAll ? false : true;
    let actualPrice = 0;
    console.log("点击全选按钮框变化的值===》" + isSelectAll);
    if (isSelectAll) {
      this.data.orderDetailList.forEach((item, index) => {
        item.checked = true;
        item.number = item.oldNumber;
        actualPrice = this.data.order.oldActualPrice;
      });
    }
    if (!isSelectAll) {
      this.data.orderDetailList.forEach((item, index) => {
        item.checked = false;
        actualPrice = 0;
      });
    }
    this.data.order.actualPrice = util.toFixed(actualPrice, 2);

    console.log(this.data)
    this.setData({
      orderDetailList: this.data.orderDetailList,
      order: this.data.order,
      isSelectAll: isSelectAll
    })
  },
  checkboxChange(e) {
    let checkeds = e.detail.value;
    let items = this.data.orderDetailList;
    let oldActualPrice = this.data.order.oldActualPrice;

    //遍历商品的状态都设置为未选择
    for (var order in items) {
      items[order].checked = false;
    }
    //1、获取已选择的商品总额度
    let actualPrice = 0,
      fullActualPrice = 0;
    //满足满减金额
    let limitedPrice = this.data.order.limitedPrice;
    console.log("满足满减金额limitedPrice===>" + limitedPrice)
    //满减价格
    let reducedPrice = this.data.order.reducedPrice;
    console.log("满足满减价格reducedPrice===>" + reducedPrice)
    let packingCharges = 0;
    let goodsTotalQuantity = 0;
    for (var i in checkeds) {
      items[checkeds[i]].checked = true;
      //商品的价格
      let price = util.toFixed((items[checkeds[i]].price * items[checkeds[i]].number), 2);
      if (items[checkeds[i]].isUsedCoupons) {
        price = price - items[checkeds[i]].couponsDiscountPrice;
      }
      //获取当前商品的包装费并加上选择的商品的包装费
      packingCharges = util.toFixed((packingCharges + (items[checkeds[i]].packingCharges * items[checkeds[i]].number)), 2);
      //计算后的总额加上当前商品的价格
      actualPrice = util.toFixed((actualPrice + price), 2);
      //判断如果存在满减则进行商品分摊
      if (this.data.order.fullReductionRuleId) {
        let limitedPrice_price = util.toFixed((reducedPrice / limitedPrice), 2);
        console.log("商品价格除以满减价格===>" + limitedPrice_price)
        let sharePrice = util.toFixed((price * limitedPrice_price), 2);
        console.log(items[checkeds[i]].goodsName + "==x" + items[checkeds[i]].number + "=========>" + sharePrice);
        fullActualPrice = util.toFixed(((util.toFixed(fullActualPrice + sharePrice, 2))), 2);
      }
      goodsTotalQuantity += items[checkeds[i]].number;
    }
    actualPrice += packingCharges;
    console.log("获取已选择的商品加上包装费的额度====>" + actualPrice);
    //4、然后遍历已选择的商品，查询是否使用了优惠券，获取第三部的额度
    //如果没使用则不做任何操作
    //如果使用了优惠券则加上优惠券的价格、自动退回优惠券
    // for (let i in items) {
    //   if (items[i].isUsedCoupons&&items[i].checked) {
    //     console.log("获取优惠券金额"+items[i].couponsDiscountPrice)
    //     actualPrice -= items[i].couponsDiscountPrice;
    //   }
    // }

    //我现在部分退款，我退40元，其不满足满减规则，那么需要计算满减优惠分摊金额40*(0.4)=16元，40-16=24元，
    //那么其它三件商品是花费了56元，而三件商品的原价其实是60元，所以这三件商品其实总共分摊了满减优惠金额4元
    if (actualPrice >= limitedPrice) {
      actualPrice = actualPrice - reducedPrice;
    } else {
      actualPrice -= fullActualPrice;
    }

    //actualPrice=actualPrice-this.data.order.reducedPrice;
    console.log("获取已选择的商品总额度====>" + actualPrice);
    console.log("获取已选择的商品包装费====>" + packingCharges)
    console.log("获取已选择的满减后的额度====>" + fullActualPrice);
    console.log("获取满减之后的额度====>" + actualPrice);
    this.data.order.actualPrice = util.toFixed(actualPrice, 2);
    if (goodsTotalQuantity == this.data.order.goodsTotalQuantity) {
      this.data.order.actualPrice += this.data.order.deliveryFee;
    }
    this.setData({
      orderDetailList: items,
      order: this.data.order,
      isSelectAll: checkeds.length != this.data.orderDetailList.length ? false : true
    })
  },
  /*点击减号*/
  bindMinus: function (e) {
    toastService.showLoading();
    let goodsId = e.currentTarget.dataset.goodsid;
    let number = e.currentTarget.dataset.number;
    let self = this;
    let actualPrice = 0,
      packingCharges = 0,
      fullActualPrice = 0;
    let oldActualPrice = this.data.order.oldActualPrice;
    //满足满减金额
    let limitedPrice = this.data.order.limitedPrice;
    //满减价格
    let reducedPrice = this.data.order.reducedPrice;
    let checkedBox = 0;
    this.data.orderDetailList.forEach((item, index) => {
      if (item.checked) {
        if (item.goodsId == goodsId) {
          item.number--;
          checkedBox = index;
        }
        //商品的价格
        let price = util.toFixed((item.price * item.number), 2);
        if (item.isUsedCoupons) {
          price = price - item.couponsDiscountPrice;
        }
        //获取当前商品的包装费并加上选择的商品的包装费
        packingCharges = packingCharges + (item.packingCharges * item.number);
        //计算后的总额加上当前商品的价格
        actualPrice = actualPrice + price;

        if (this.data.order.fullReductionRuleId) {
          let limitedPrice_ = util.toFixed((reducedPrice / limitedPrice), 2);
          let sharePrice = util.toFixed((price * limitedPrice_), 2);
          console.log(item.goodsName + "==x" + item.number + "=========>" + sharePrice);
          fullActualPrice = util.toFixed((fullActualPrice + sharePrice), 2);
        }
      }
    })
    console.log("获取点击的下标值===>" + checkedBox);
    console.log("获取已选商品的总额度===>" + actualPrice);
    console.log("获取已选商品的包装费===>" + packingCharges);
    console.log("获取已选商品的分摊费===>" + fullActualPrice);
    actualPrice += packingCharges;

    console.log("是否进行优惠券加减的操作====>" + self.data.orderDetailList[checkedBox].isUsedCoupons)
    // if (self.data.orderDetailList[checkedBox].isUsedCoupons
    //   &&self.data.orderDetailList[checkedBox].checked) {
    //     //self.data.orderDetailList[checkedBox].isUsedCoupons=false;
    //   actualPrice = actualPrice - self.data.orderDetailList[checkedBox].couponsDiscountPrice;
    // }

    if (actualPrice >= limitedPrice) {
      actualPrice = actualPrice - reducedPrice;
    } else {
      actualPrice -= fullActualPrice;
    }


    self.data.order.actualPrice = util.toFixed(actualPrice, 2);

    // if (actualPrice > oldActualPrice) {
    //   self.data.order.actualPrice = oldActualPrice;
    // } else {
    //   self.data.order.actualPrice = util.toFixed(actualPrice,2);
    // }
    console.log("总金额加上包装费减去分摊费 总金额===>" + actualPrice);
    if (number == 1) {
      self.data.orderDetailList[checkedBox].checked = false;
      self.data.orderDetailList[checkedBox].number = number;
      self.setData({
        order: self.data.order,
        orderDetailList: self.data.orderDetailList,
        isSelectAll: false
      });
      toastService.hideLoading();
      return
    }

    self.data.orderDetailList[checkedBox].number = number - 1;
    self.setData({
      orderDetailList: self.data.orderDetailList,
      order: self.data.order
    });
    toastService.hideLoading();
  },
  /*点击加号*/
  bindPlus: function (e) {
    toastService.showLoading();
    let goodsId = e.currentTarget.dataset.goodsid;
    let number = e.currentTarget.dataset.number;
    let oldNumber = e.currentTarget.dataset.oldnumber;
    let index = e.currentTarget.dataset.index;
    console.log("当前商品的选择数量====>" + number);
    console.log("当前商品的原始数量====>" + oldNumber)
    if (oldNumber == number && this.data.orderDetailList[index].checked) {
      toastService.showToast("您只点了" + number + "份哦");
      return
    }
    let actualPrice = 0,
      packingCharges = 0,
      fullActualPrice = 0,
      goodsTotalQuantity = 0;
    let oldActualPrice = this.data.order.oldActualPrice;
    //满足满减金额
    let limitedPrice = this.data.order.limitedPrice;
    //满减价格
    let reducedPrice = this.data.order.reducedPrice;
    let checkedBox = 0;
    let self = this;
    this.data.orderDetailList.forEach((item, index) => {
      if (item.goodsId == goodsId) {
        if (!item.checked) {
          item.number--;
          item.checked = true;
        } else {
          number += 1;
        }
      }
      if (item.checked) {
        if (item.goodsId == goodsId) {
          item.number++;
          checkedBox = index;
        }
        //商品的价格
        let price = util.toFixed((item.price * item.number), 2);
        if (item.isUsedCoupons) {
          price = price - item.couponsDiscountPrice;
        }
        //获取当前商品的包装费并加上选择的商品的包装费
        packingCharges = packingCharges + (item.packingCharges * item.number);
        //计算后的总额加上当前商品的价格
        actualPrice = util.toFixed((actualPrice + price), 2);

        if (this.data.order.fullReductionRuleId) {
          let limitedPrice_ = util.toFixed((reducedPrice / limitedPrice), 2);
          let sharePrice = util.toFixed((price * limitedPrice_), 2);
          console.log(item.goodsName + "==x" + item.number + "=========>" + sharePrice);
          fullActualPrice = util.toFixed((fullActualPrice + (util.toFixed(sharePrice, 2))), 2);
        }

        goodsTotalQuantity += item.number;
      }
    });
    console.log("获取点击的下标值===>" + checkedBox);
    console.log("获取已选商品的总额度===>" + actualPrice);
    console.log("获取已选商品的包装费===>" + packingCharges);
    console.log("获取已选商品的分摊费===>" + fullActualPrice);
    actualPrice += packingCharges;

    console.log("是否进行优惠券加减的操作====>" + self.data.orderDetailList[checkedBox].isUsedCoupons)
    // if (!self.data.orderDetailList[checkedBox].isUsedCoupons
    //   &&self.data.orderDetailList[checkedBox].checked) {
    //     console.log("进行了优惠券金额的操作")
    //   //self.data.orderDetailList[checkedBox].isUsedCoupons=true;
    //   actualPrice = actualPrice - self.data.orderDetailList[checkedBox].couponsDiscountPrice;
    // }
    if (actualPrice >= limitedPrice) {
      actualPrice = actualPrice - reducedPrice;
    } else {
      actualPrice -= fullActualPrice;
    }
    // if (actualPrice > oldActualPrice) {
    //   self.data.order.actualPrice = oldActualPrice;
    // } else {
    //   self.data.order.actualPrice = util.toFixed(actualPrice,2);
    // }
    console.log("总金额加上包装费减去分摊费 总金额===>" + actualPrice);
    self.data.order.actualPrice = util.toFixed(actualPrice, 2);
    //如果订单总金额等于支付金额则加上配送费
    console.log("计算选中的商品数量" + goodsTotalQuantity);
    console.log("实际的商品数量" + self.data.order.goodsTotalQuantity)
    if (goodsTotalQuantity == self.data.order.goodsTotalQuantity) {
      self.data.order.actualPrice += self.data.order.deliveryFee;
    }

    self.data.orderDetailList[checkedBox].number = number;
    self.setData({
      orderDetailList: self.data.orderDetailList,
      order: self.data.order,
      isSelectAll: true
    });
    toastService.hideLoading();
  },
  bindRefundReasonDescription(e) {
    this.setData({
      refundReasonDescription: e.detail.value
    })
  },
  submitApplyRefund() {
    orderRefundGoodsListStr = [];
    this.data.orderDetailList.forEach((i, index) => {
      if (i.checked) {
        orderRefundGoodsListStr.push({
          orderDetailId: i.id,
          number: i.number
        })
      }
    });
    if (orderRefundGoodsListStr.length <= 0) {
      toastService.showToast("请选择要退款的商品");
      return;
    }
    if (!this.data.notSelected || !this.data.oldCancelOrderNoReason) {
      toastService.showToast("请选择退款原因");
      return;
    }
    let data = {
      orderRefundGoodsListStr: JSON.stringify(orderRefundGoodsListStr),
      id: this.data.order.id,
      orderRefund:{
        refundReason: this.data.refundReasonApply[this.data.oldCancelOrderNoReason].value,
        refundAmount: this.data.order.actualPrice
      }
    }
    if (submitImages.length > 0) {
      data.orderRefund.evidenceImages = submitImages;
    }
    if (this.data.refundReasonDescription) {
      data.orderRefund.refundReasonDescription = this.data.refundReasonDescription;
    }
    toastService.showLoading();
    https.request('/api-order/rest/member/order/applyRefund', data).then(result => {
      toastService.hideLoading();
      if (result.success) {
        toastService.showSuccess("申请成功");
        // var pages = getCurrentPages();
        // var prevPage = pages[pages.length - 2]; //上一个页面
        // prevPage.getOrderDetail(this.data.order.id);
        wx.navigateBack(1);
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var orderId = options.orderId;
    this.getOrderDetail(orderId);
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
    var pages = getCurrentPages();
    var prevPage = pages[pages.length - 2]; //上一个页面
    prevPage.getOrderDetail(this.data.order.id);
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