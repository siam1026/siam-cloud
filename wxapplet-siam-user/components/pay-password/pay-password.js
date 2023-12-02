// component.js
Component({
  properties: {
    isShow: {
      type: Boolean,
      value: false
    },
    pwdVal: {
      type: Number,
      value: ''
    },
    payFocus: {
      type: Boolean,
      value: false
    },
  },
  data: {
    from: 'component',
  },
  ready() {
    //console.log('in component --> ', this.data.from)
  },
  methods: {
    /**
   * 显示支付密码输入层
   */
    showInputLayer: function () {
      this.setData({ showPayPwdInput: true, payFocus: true });
    },
    /**
     * 隐藏支付密码输入层
     */
    hidePayLayer: function () {

      var val = this.data.pwdVal;

      this.setData({ showPayPwdInput: false, payFocus: false, pwdVal: '' }, function () {
        wx.showToast({
          title: val,
        })
      });

    },
    /**
     * 获取焦点
     */
    getFocus: function () {
      this.setData({ payFocus: true });
    },
    /**
     * 输入密码监听
     */
    inputPwd: function (e) {
      this.setData({ pwdVal: e.detail.value });

      if (e.detail.value.length >= 6) {
        this.hidePayLayer();
      }
    }
  }
})