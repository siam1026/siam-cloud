// const Login = () => import( './pages/Login.vue')
const LoginMain = () => import('./pages/LoginMain.vue')
const NotFound = () => import('./pages/404.vue')
const Home = () => import('./pages/Home.vue')
const Main = () => import('./pages/Main.vue')
const Home_empty = () => import('./pages/Home-empty.vue')

const login = () => import('./pages/login/login.vue')
const QuickLogin = () => import('./pages/login/QuickLogin.vue')
const setPassword = () => import('./pages/login/setPassword.vue')

// 基础数据模块

//banner管理

//空页面
const emptyList = () => import('./pages/basicModule/emptyList.vue')

//用户管理
const memberList = () => import('./pages/memberManage/memberList.vue')
const purchasedMemberList = () => import('./pages/memberManage/purchasedMemberList.vue')
const unPurchasedMemberList = () => import('./pages/memberManage/unPurchasedMemberList.vue')

//商品管理
const menuList = () => import('./pages/goodsManage/menuList.vue')
const goodsList = () => import('./pages/goodsManage/goodsList.vue')
const addGoods = () => import('./pages/goodsManage/addGoods.vue')
const editGoods = () => import('./pages/goodsManage/editGoods.vue')
const goodsAccessoriesList = () => import('./pages/goodsManage/goodsAccessoriesList.vue')
const rawmaterialList = () => import('./pages/goodsManage/rawmaterialList.vue')
const goodsRawmaterialRelationList = () => import('./pages/goodsManage/goodsRawmaterialRelationList.vue')
// const specificationList = () => import( './pages/goodsManage/specificationList.vue')
// const stockList = () => import( './pages/goodsManage/stockList.vue')


//订单管理
const waitHandleOrderList = () => import('./pages/orderManage/waitHandleOrderList.vue')
const forHereTabsChild1 = () => import('./pages/orderManage/forHereTabsChild1.vue')
const takeOutTabsChild1 = () => import('./pages/orderManage/takeOutTabsChild1.vue')
const takeOutTabsChild2 = () => import('./pages/orderManage/takeOutTabsChild2.vue')
const forHereTabsChild2 = () => import('./pages/orderManage/forHereTabsChild2.vue')
const refundOrderList = () => import('./pages/orderManage/refundOrderList.vue')
const forHereTabsChild3 = () => import('./pages/orderManage/forHereTabsChild3.vue')
const orderDetail = () => import('./pages/orderManage/orderDetail.vue')
//const unpaidOrderList = () => import( './pages/MallManage/unpaidOrderList.vue')
const forHereOrderList = () => import('./pages/orderManage/forHereOrderList.vue')
const takeOutOrderList = () => import('./pages/orderManage/takeOutOrderList.vue')
const todayOrderList = () => import('./pages/orderManage/todayOrderList.vue')
const appraiseList = () => import('./pages/orderManage/appraiseList.vue')

//促销管理
const fullReductionRuleList = () => import('./pages/promotionManage/fullReductionRuleList.vue')
const couponsList = () => import('./pages/promotionManage/couponsList.vue')
const paperworkPushList = () => import('./pages/promotionManage/paperworkPushList.vue')

//会员管理
const memberWithdrawRecord = () => import('./pages/promotionManage/memberWithdrawRecord.vue')
//门店管理
const shopList = () => import('./pages/shopManage/shopList.vue')
const shopListOfApplySettled = () => import('./pages/shopManage/shopListOfApplySettled.vue')
const merchantWithdrawRecord = () => import('./pages/shopManage/merchantWithdrawRecord.vue')
const shopListOfApplyChangeData = () => import('./pages/shopManage/shopListOfApplyChangeData.vue')

//轮播图管理
const advertisementList = () => import('./pages/shopDecoration/advertisementList.vue')
const addAdvertisementList = () => import('./pages/shopDecoration/addAdvertisementList.vue')
const editAdvertisementList = () => import('./pages/shopDecoration/editAdvertisementList.vue')

//系统设置
const settingList = () => import('./pages/systemSetting/settingList.vue')

//数据中心
const statisticGraph = () => import('./pages/basicModule/statisticGraph.vue')

//财务报表
const merchantWithdrawRecord_success = () => import('./pages/accountModule/merchantWithdrawRecord.vue')
const merchantBillingRecord = () => import('./pages/accountModule/merchantBillingRecord.vue')

let routes = [
  // {
  //     path: '/login',
  //     component: Login,
  //     name: '',
  //     hidden: true
  // },
  {
    path: '/login',
    component: LoginMain,
    name: '',
    hidden: true,
    children: [
      { path: '/', component: login, pagename: '登录', icon: 'el-icon-search', hidden: true },
      { path: '/QuickLogin', component: QuickLogin, pagename: '手机验证登录', icon: 'el-icon-search', hidden: true },
      { path: '/setPassword', component: setPassword, pagename: '忘记密码', icon: 'el-icon-search', hidden: true },
    ]
  },
  {
    path: '/404',
    component: NotFound,
    name: '',
    hidden: true
  },
  {
    path: '/',
    component: Home,
    name: '数据中心',
    iconCls: 'el-icon-house',
    leaf: true,//只有一个节点
    children: [
      { path: '/statisticGraph', component: statisticGraph, name: '数据中心' },//实时数据
    ]
  },
  {
    path: '/',
    component: Home,
    name: '用户管理',
    iconCls: 'el-icon-user',
    children: [
      {
        path: '/memberList', component: memberList, name: '用户列表',
        leaf: true,//只有一个节点
        children: [
          { path: '/memberList', component: memberList, name: '用户列表' },
        ]
      },
      {
        path: '/purchasedMemberList', component: purchasedMemberList, name: '已购买用户',
        leaf: true,//只有一个节点
        children: [
          { path: '/purchasedMemberList', component: purchasedMemberList, name: '已购买用户' },
        ]
      },
      {
        path: '/unPurchasedMemberList', component: unPurchasedMemberList, name: '未购买用户',
        leaf: true,//只有一个节点
        children: [
          { path: '/unPurchasedMemberList', component: unPurchasedMemberList, name: '未购买用户' },
        ]
      },
    ]
  },
  {
    path: '/',
    component: Home,
    name: '订单管理',
    iconCls: 'el-icon-s-order',
    children: [
      {
        path: '/todayOrderList', component: todayOrderList, name: '订单查询',
        leaf: true,//只有一个节点
        children: [
          { path: '/todayOrderList', component: todayOrderList, name: '订单查询' },
        ]
      },
      {
        path: '/refundOrderList', component: refundOrderList, name: '售后订单',
        leaf: true,//只有一个节点
        children: [
          { path: '/refundOrderList', component: refundOrderList, name: '售后订单' },
        ]
      },
      // { path: '/emptyList', component: emptyList, name: '评论列表' },
      // { path: '/forHereOrderList', component: forHereOrderList, name: '自取订单' },
      // { path: '/takeOutOrderList', component: takeOutOrderList, name: '外卖订单' },          
      // { path: '/unpaidOrderList', component: unpaidOrderList, name: '未付款' },
      // { path: '/waitHandleOrderList', component: waitHandleOrderList, name: '待处理订单' },
      // { path: '/forHereTabsChild1', component: forHereTabsChild1, name: '待自取订单' },
      // { path: '/takeOutTabsChild1', component: takeOutTabsChild1, name: '待配送订单' },
      // { path: '/takeOutTabsChild2', component: takeOutTabsChild2, name: '已配送订单' },
      // { path: '/forHereTabsChild2', component: forHereTabsChild2, name: '已完成订单' },       
      // { path: '/refundOrderList', component: refundOrderList, name: '售后处理' },          
      // { path: '/forHereTabsChild3', component: forHereTabsChild3, name: '已取消订单' },          
      {
        path: '/orderDetail', component: orderDetail, name: '查看订单详情', hidden: true,
        leaf: true,//只有一个节点
        children: [
          { path: '/orderDetail', component: orderDetail, name: '查看订单详情' },
        ]
      },
      {
        path: '/appraiseList', component: appraiseList, name: '评价管理',
        leaf: true,//只有一个节点
        children: [
          { path: '/appraiseList', component: appraiseList, name: '评价管理' },
        ]
      },
    ]
  },
  {
    path: '/',
    component: Home,
    name: '商家中心',
    iconCls: 'el-icon-house',
    children: [
      {
        path: '/shopList', component: shopList, name: '已入驻商家',
        leaf: true,//只有一个节点
        children: [
          { path: '/shopList', component: shopList, name: '已入驻商家' },
        ]
      },
      {
        path: '/shopListOfApplySettled', component: shopListOfApplySettled, name: '申请开店商家',
        leaf: true,//只有一个节点
        children: [
          { path: '/shopListOfApplySettled', component: shopListOfApplySettled, name: '申请开店商家' },
        ]
      },
      {
        path: '/merchantWithdrawRecord', component: merchantWithdrawRecord, name: '申请提现商家',
        leaf: true,//只有一个节点
        children: [
          { path: '/merchantWithdrawRecord', component: merchantWithdrawRecord, name: '申请提现商家' },
        ]
      },
      {
        path: '/shopListOfApplyChangeData', component: shopListOfApplyChangeData, name: '申请变更资料商家',
        leaf: true,//只有一个节点
        children: [
          { path: '/shopListOfApplyChangeData', component: shopListOfApplyChangeData, name: '申请变更资料商家' },
        ]
      },
    ]
  },
  {
    path: '/',
    component: Home,
    name: '营销中心',
    iconCls: 'el-icon-house',
    children: [
      {
        path: '/couponsList', component: couponsList, name: '优惠券',
        leaf: true,//只有一个节点
        children: [
          { path: '/couponsList', component: couponsList, name: '优惠券' },
        ]
      },
      // {
      //   path: '/emptyList', component: emptyList, name: '积分',
      //   leaf: true,//只有一个节点
      //   children: [
      //     { path: '/emptyList', component: emptyList, name: '积分' },
      //   ]
      // },
      {
        path: '/advertisementList', component: advertisementList, name: '海报',
        leaf: true,//只有一个节点
        children: [
          { path: '/advertisementList', component: advertisementList, name: '海报' },
        ]
      },
      {
        path: '/addAdvertisementList', component: addAdvertisementList, name: '新增海报', hidden: true,
        leaf: true,//只有一个节点
        children: [
          { path: '/addAdvertisementList', component: addAdvertisementList, name: '新增海报' },
        ]
      },
      {
        path: '/editAdvertisementList', component: editAdvertisementList, name: '编辑海报', hidden: true,
        leaf: true,//只有一个节点
        children: [
          { path: '/editAdvertisementList', component: editAdvertisementList, name: '编辑海报' },
        ]
      },      
      //{ path: '/memberWithdrawRecord', component: memberWithdrawRecord, name: '佣金提现列表' },
      {
        path: '/paperworkPushList', component: paperworkPushList, name: '文案推送',
        leaf: true,//只有一个节点
        children: [
          { path: '/paperworkPushList', component: paperworkPushList, name: '文案推送' },
        ]
      },
      // { path: '/emptyList', component: emptyList, name: '帐户信息' },
    ]
  },
  {
    path: '/',
    component: Home,
    name: '财务报表',
    iconCls: 'el-icon-user',
    children: [
      {
        path: '/merchantBillingRecord', component: merchantBillingRecord, name: '商家交易明细',
        leaf: true,//只有一个节点
        children: [
          { path: '/merchantBillingRecord', component: merchantBillingRecord, name: '商家交易明细' },
        ]
      },//交易明细          
      {
        path: '/merchantWithdrawRecord_success', component: merchantWithdrawRecord_success, name: '商家提现记录',
        leaf: true,//只有一个节点
        children: [
          { path: '/merchantWithdrawRecord_success', component: merchantWithdrawRecord_success, name: '商家提现记录' },
        ]
      },
      {
        path: '/memberWithdrawRecord', component: memberWithdrawRecord, name: '用户奖励金提现记录',
        leaf: true,//只有一个节点
        children: [
          { path: '/memberWithdrawRecord', component: memberWithdrawRecord, name: '用户奖励金提现记录' },
        ]
      },
    ]
  },
  {
    path: '/',
    component: Home,
    name: '系统配置',
    iconCls: 'el-icon-setting',
    children: [
      {
        path: '/settingList', component: settingList, name: '基础数据配置',
        leaf: true,//只有一个节点
        children: [
          { path: '/settingList', component: settingList, name: '基础数据配置' },
        ]
      },
      // { path: '/emptyList', component: emptyList, name: '支付设置' },
    ]
  },
  {
    path: '*',
    hidden: true,
    redirect: { path: '/404' }
  }
];

export default routes;
