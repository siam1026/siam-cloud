import GlobalConfig from '../../../utils/global-config';
import https from '../../../utils/http';
import toastService from '../../../utils/toast.service';
var submitImages = [];
const app = getApp();
Page({

  /**
   * 页面的初始数据 
   */
  data: {
    rate: 0,
    disabled: false,
    isChoiceRate: false,
    value: 0,
    contentPlaceholder: "餐点味道好，包装也仔细，下次还会点",
    uploaderImages: []
  },
  getOrderDetail(id) {
    https.request('/api-order/rest/member/order/selectById', {
      id: id
    }).then(result => {
      toastService.hideLoading();
      if (result.success) {

      }
    })
  },
  getShopInfo(shopId) {
    https.request('/api-merchant/rest/shop/detail', {
      id: shopId,
      position: app.deliveryAndSelfTaking.location
    }).then(result => {
      if (result.success && result.data) {
        result.data.shop.shopLogoImg = GlobalConfig.ossUrl + result.data.shop.shopLogoImg;
        this.setData({
          shopInfo: result.data
        });
      }
    })
  },
  bindTextarea(e) {
    this.setData({
      contentTextarea: e.detail.value
    })
  },
  handleTap(e) {
    if (e.detail.value > 0) {
      this.setData({
        isChoiceRate: true,
        choiceRate: e.detail.value
      })
    }
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
  // uploadImage: function (e) {
  //   if (this.data.uploaderImages.length == 3) {
  //     toastService.showToast("最多只能上传三张图片");
  //     return
  //   };
  //   https.uploadFile({ url: '/api-util/rest/member/uploadSingleImage' }).then(result => {
  //     if (result.success) {
  //       toastService.showLoading("上传中...");
  //       let image = GlobalConfig.ossUrl + result.data;
  //       this.data.uploaderImages.push(image);
  //       submitImages.push(result.data);
  //       this.setData({
  //         uploaderImages: this.data.uploaderImages
  //       });
  //       toastService.hideLoading();
  //       toastService.showSuccess('上传成功', 1000);
  //     }
  //   })
  // },
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
    const url = GlobalConfig.baseUrl + '/api-util/rest/member/uploadSingleImage';
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
  submitEvaluate() {
    console.log("评价等级======" + this.data.choiceRate);
    if (!this.data.choiceRate) {
      toastService.showToast("请选择评价等级");
      return
    }
    let title = "确定提交评价吗?";
    let content = this.data.contentTextarea;
    if (!content) {
      title = "不填写评价会默认提交文本框内的内容哦~";
      content = this.data.contentPlaceholder;
    }
    console.log("评价内容为=====>"+content);
    let self = this;
    toastService.showModal(null, title, function () {
      https.request('/api-order/rest/member/appraise/insert', {
        orderId: self.data.orderId,
        shopId: self.data.shopId,
        appraiseType: 1,
        content: content,
        imagesUrl: submitImages,
        level: self.data.choiceRate
      }).then(result => {
        if (result.success) {
          toastService.showSuccess('提交成功', 1000);
          wx.navigateBack(1)
        }
      });
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      orderId: options.orderId,
      shopId: options.shopId
    })
    this.getOrderDetail(options.orderId);
    this.getShopInfo(options.shopId);
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