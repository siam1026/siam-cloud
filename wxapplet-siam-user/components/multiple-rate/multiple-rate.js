Component({
  behaviors: [],
  properties: {
    rate: {
      type: String,
      value: '0'
    },
    icon: {
      type: String,
      value: 'star'
    },
    disabled: {
      type: Boolean,
      value: false
    }
  },
  data: {
    starArr: [],
    isHandleTap:false
  },

  attached: function () {
    this.getStarArr()
  },
  moved: function () {
  },
  detached: function () {
  },

  methods: {
    getStarArr: function () {
      let starArr = [];
      for (var i = 0; i < this.data.rate; i++) {
        starArr.push(this.data.icon);
      }
      for (let j = 0; j < 5 - this.data.rate; j++) {
        starArr.push(this.data.icon + '-o');
      }
      this.setData({
        starArr: starArr
      })
    },
    handleTap: function (e) {
      
      if (this.data.disabled) {
        console.log(this.data.rate)
        return;
      }
      this.setData({
        rate: Number(e.currentTarget.dataset.index) + 1
      })
      console.log(this.data.rate)
      this.triggerEvent('change', { value: Number(e.currentTarget.dataset.index) + 1 });
      this.getStarArr()
    }
  }
})
