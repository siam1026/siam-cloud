// component.js
Component({
  properties:{
    isShow:{
      type:Boolean,
      value:false
    },
    src:{
      type:String,
      value:""
    },
    iconfont:{
      type:String,
      value:""
    },
    buttonText:{
      type: String,
      value: ""
    },
    top:{
      type:Number,
      value:0
    },
    bottom:{
      type: Number,
      value: 0
    },
    button:{
      type:Boolean,
      value: false
    },
    text:{
      type: Boolean,
      value: false
    },
    tipText:{
      type: String,
      value: ""
    },
    classText:{
      type: String,
      value: ""
    }
  },
  data: {
    from: 'component',

  },
  ready() {
    //console.log('in component --> ', this.data.from)
  },
  methods: {
    bindTap: function (){
      // 使用 triggerEvent 方法触发自定义组件事件，指定事件名、detail对象和事件选项
      this.triggerEvent('bindTap')
    }
  }
})