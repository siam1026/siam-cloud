<template>
	<section>
		<div class="todayBox">
			<div class="totdayBlock blue">
				<p class="todayBlockTitle">入账金额</p>
				<p class="todayBlockNum">
					<span>￥{{todayList.incomeAmount || 0}}</span>
				</p>
			</div>
			<div class="totdayBlock green">
				<p class="todayBlockTitle">出账金额</p>
				<p class="todayBlockNum">
					<span>￥{{todayList.expendAmount || 0}}</span>
				</p>
			</div>
		</div>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="searchMsg" size="small">
				<el-form-item>
					<el-input v-model="searchMsg.orderNo" placeholder="请输入订单编号搜索" clearable></el-input>
				</el-form-item>
				<el-form-item>
					<el-select v-model="searchMsg.type" placeholder="请选择账单类型搜索" clearable>
						<el-option label="用户下单" :value="1"></el-option>
						<el-option label="商家提现" :value="2"></el-option>    
						<el-option label="商家提现失败退回" :value="3"></el-option>
						<el-option label="商家自配送-配送费收入" :value="6"></el-option>
						<el-option label="用户一分钟内取消订单" :value="7"></el-option>
						<el-option label="用户一分钟内取消订单-配送费退回" :value="8"></el-option>
						<el-option label="用户申请退款" :value="9"></el-option>
						<el-option label="用户申请退款-配送费退回" :value="10"></el-option>
					</el-select>
				</el-form-item>		
				<el-form-item>
					<el-date-picker
						v-model="searchMsg.startDate_tmp"
						value-format="timestamp"
						format="yyyy/MM/dd"
						type="datetime"
						placeholder="选择开始日期">
					</el-date-picker>
					<el-date-picker
						v-model="searchMsg.endDate_tmp"
						value-format="timestamp"
						format="yyyy/MM/dd"
						type="datetime"
						placeholder="选择结束日期">
					</el-date-picker>          
				</el-form-item>                    
				<el-form-item>
					<el-button type="primary" @click="getList(1)">查询</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="list" highlight-current-row v-loading="listLoading" style="width: 100%;" :cell-style="cellStyle" :header-cell-style="headerCellStyle">
			<!-- <el-table-column type="index" label="序号" width="50">
				<template scope="scope">
					<span>{{(searchMsg.pageNo - 1) * searchMsg.pageSize + scope.$index + 1}}</span>
				</template>		
			</el-table-column> -->
			<el-table-column prop="shopName" label="店铺名称"></el-table-column>
			<el-table-column prop="type" label="账单类型" :formatter="formatType"></el-table-column>
			<!-- <el-table-column prop="operateType" label="操作类型" :formatter="formatOperateType"></el-table-column> -->
            <!-- <el-table-column prop="coinType" label="货币类型" :formatter="formatCoinType"></el-table-column> -->
			<!-- <el-table-column prop="number" label="增减数值" :formatter="addUnitFixedTwo"></el-table-column> -->
			<el-table-column prop="number" label="金额(元)">
				<template scope="scope">
					<span v-if="scope.row.operateType==1" style="color: red; font-weight: bold">+{{changeDecimal(scope.row.number, 2)}}</span>
                    <span v-else-if="scope.row.operateType==2" style="color: green; font-weight: bold">-{{changeDecimal(scope.row.number, 2)}}</span>
               </template>				
			</el-table-column>			
            <el-table-column prop="message" label="记录消息"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" :formatter="formatTime"></el-table-column>
		</el-table>

		<el-col :span="24" class="toolbar">
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="searchMsg.pageSize" :total="total" style="float:right;">
			</el-pagination>
		</el-col>
	</section>
</template>

<script>
import { type } from 'os';
	export default {
		data() {
			return {
				todayList:{}, //今日数据
                searchMsg:{
                    pageNo:1,
                    pageSize:20
                },
				list: [],
                total:0,
                listLoading:false
			}
		},
		methods: {
			changeDecimal (val, scale) { // 小数点后保留2位小数，不足自动补0
				// let vue = this;
				// val = vue.$utils.changeDecimal(val, scale);
				return val;
			},				
			cellStyle({row, column, rowIndex, columnIndex}){
				return "text-align:center";
			},
			headerCellStyle({row, rowIndex}){
				return "text-align:center";
			},						
			formatTime(row, column) {
				let date = new Date(row[column.property]);
				return this.$utils.formatDate(date, 'yyyy-MM-dd hh:mm:ss')
            },
            formatType(row,column){
				switch(row.type){
					case 1:
						return '用户下单';
					case 2:
						return '商家提现';
					case 3:
						return '商家提现失败退回';
					case 4:
						return '自取订单改为配送，收入减少';
					case 5:
						return '自取订单改为配送，收入增加';
					case 6:
						return '商家自配送-配送费收入';
					case 7:
						return '用户一分钟内取消订单';
					case 8:
						return '用户一分钟内取消订单-配送费退回';
					case 9:
						return '用户申请退款';
					case 10:
						return '用户申请退款-配送费退回';							
				}
            },  
			formatOperateType(row,column){
                return row.operateType == 1 ? '增加' : '减少';
            },
            formatCoinType(row,column){
                return row.coinType == 1 ? '余额' : '';
			},
			addUnitFixedTwo(row, column) { // 添加单位
				return Number((row[column.property] || 0)).toFixed(2) + '元'
			},			
						getList(pageNoParam) { // 获取列表
				if(pageNoParam){
				this.searchMsg.pageNo = pageNoParam;
				} // 获取列表
				let vue = this
				let param = Object.assign({}, vue.searchMsg);

				param.coinType = 1; //查询余额账单

				if(param.orderNo!=undefined && param.orderNo!=""){
					param.message = "订单号%" + param.orderNo;
				}

				//暂时不做时间前后的判断
				if(param.startDate_tmp == undefined){
					delete param.startCreateTime;
				// param.startDate = "1970/01/01";
				}else{
					//注意：前端似乎没有HH，只有hh，写HH无法解析
					let startDate_tmp = new Date(param.startDate_tmp);
					startDate_tmp.setHours(0);
					startDate_tmp.setMinutes(0);
					startDate_tmp.setSeconds(0);	          
					param.startCreateTime = this.$utils.formatDate(startDate_tmp, 'yyyy/MM/dd hh:mm:ss');
				}
				if(param.endDate_tmp == undefined){
					delete param.endCreateTime;
				// var curDate = new Date();
				// var preDate = new Date(curDate.getTime() - 24*60*60*1000); //前一天
				// var nextDate = new Date(curDate.getTime() + 24*60*60*1000); //后一天          
				// param.endDate = this.$utils.formatDate(nextDate, 'yyyy/MM/dd');
				}else{
					let endDate_tmp = new Date(param.endDate_tmp);
					endDate_tmp.setHours(23);
					endDate_tmp.setMinutes(59);
					endDate_tmp.setSeconds(59);	                    
					param.endCreateTime = this.$utils.formatDate(endDate_tmp, 'yyyy/MM/dd hh:mm:ss');
				}

				vue.listLoading = true;
				vue.$http.post(vue, '/api-user/rest/admin/merchantBillingRecord/list',param,
					(vue, data) => {
						vue.list = data.data.records
						vue.total = data.data.total
						vue.listLoading = false;
					},(error, data)=> {
						vue.listLoading = false;
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)
				vue.$http.post(vue, '/api-user/rest/admin/merchantBillingRecord/statisticalAmount', param,
					(vue, data) => {
						//统计入账、出账的金额						
						vue.todayList.incomeAmount = Number(data.data.incomeAmount).toFixed(2);
						vue.todayList.expendAmount = Number(data.data.expendAmount).toFixed(2);
						this.$forceUpdate();
					},(error, data)=> {
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)		
			},
			handleCurrentChange(val){
				this.searchMsg.pageNo = val;
				this.getList();
			}
		},
		mounted() {
			this.getList();
			//开启订单自动打印定时器
			this.$orderPrint.init();     			
		}
	}

</script>

<style scoped>
.editForm .el-input {
  width: 420px;
}
.standForm .el-input {
  width: 200px;
}
.avatar-uploader .el-upload {
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 148px;
    height: 148px;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 148px;
    height: 148px;
    line-height: 148px;
    text-align: center;
  }
  .avatar {
    width: 148px;
    height: 148px;
    display: block;
  }
  .editForm .minInput {
    width: 240px;
  }

/* Start 余额信息模块 */
  .content{
    width: 100%;
    height: 100%;
    padding-top: 30px;
    overflow: hidden;
    /* background-color: #E7F1FA; */
  }
  .todayTitle{
    font-size:20px;
    font-weight:bold;
    color:rgba(51,51,51,1);
    margin-right: 16px;
  }
  .todayBox{
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-top: 24px;
    margin-bottom: 60px;
    display: -webkit-inline-box;    
  }
  .totdayBlock{
    width: 20%;
    color: #fff;
    padding-top: 30px;
    padding-bottom: 30px;
    border-radius:4px;
    cursor: pointer;
    margin-right: 20px;
  }
  .orange{
    background-color: #F78A71;
  }
  .yellow{
    background-color: #F8B548;
  }
  .green{
    background-color: #3FD7E4;
  }
  .blue{
    background-color: #05ACEC;
  }
  .todayBlockTitle{
    margin-left: 18px;
    /* color: rgba(0, 0, 0, 0.45); */
    font-weight: 700;
    font-size: 14px;
  }
  .todayBlockNum{
    /* text-align: center; */
    font-size: 34px;
    line-height: 34px;
    margin-top: 20px;
    margin-left: 18px;
  }
  .searchBox{
    margin-top: 10px;
  }

  
  .searchTab{
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
  }
  .searchTab span{
    font-size: 16px;
    padding-right: 10px;
  }
  .searchBtn{
    margin-left: 10px;
  }
  .descBox{
    margin-top: 40px;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }
  .descTab{
    width: 20%;
    color: #fff;
    padding-top: 30px;
    padding-bottom: 100px;
    border-radius:4px;
    border-top: 2px solid #E8AB45;
    background-color: aliceblue;
    cursor: pointer;
  }
  .descTitle{
    font-size:16px;
    color:rgba(153,153,153,1);
    margin-left: 18px;
  }
  .descNum{
    text-align: center;
    font-size:34px;
    /* font-weight:bold; */
    color:rgba(51,51,51,1);
    margin-top: 50px;
  }
  .todayBlockCompare {
    font-size: 16px;
      color: black;
  }
  .todayBlockYesterday {
    margin-left: 18px;
    margin-top: 26px;
    font-size: 14px;
    line-height: 1.5715;
  }
/* End 余额信息模块 */  
</style>