<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="searchMsg"> 
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
			<el-table-column prop="username" label="用户名称"></el-table-column>
			<el-table-column prop="orderNo" label="订单编号">
				<template scope="scope">
					<span><a href="javascript:void(0)" @click="goOrderDetail(scope.row.orderId)" style="color:blue;">{{scope.row.orderNo}}</a></span>
				</template>  		  
			</el-table-column>
			<el-table-column prop="content" label="评价内容"></el-table-column>
			<el-table-column prop="level" label="店铺打分">
				<template scope="scope">
					<span>{{scope.row.level}}星</span>
				</template>    
			</el-table-column>
			<el-table-column prop="replyList" label="回复数量">
				<template scope="scope">
					<span>{{scope.row.replyPage.records.length}}个</span>
				</template>    		  
			</el-table-column>
			<el-table-column prop="giveLikeCount" label="点赞数量">
				<template scope="scope">
					<span>{{scope.row.giveLikeCount}}个</span>
				</template>  		  
			</el-table-column>
			<el-table-column prop="createTime" label="评价时间" :formatter="formatTime" width="190"></el-table-column>
			<el-table-column label="操作" fixed="right" width="150">
				<template slot-scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">回复</el-button>
          			<!-- <el-button size="small" @click="handleEdit(scope.$index, scope.row)">查看</el-button> -->
					<!-- <el-button type="danger" size="small" @click="handleDel(scope.row.id)">删除</el-button> -->
				</template>
				<!-- scope.$index, scope.row -->
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<!-- <el-button type="danger" @click="batchRemove" :stock="this.sels.length===0">批量删除</el-button> -->
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="searchMsg.pageSize" :total="total" style="float:right;">
			</el-pagination>
		</el-col>

		<!--编辑界面-->
		<el-dialog title="编辑" :visible.sync="editFormVisible" @close="closeDialog" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="150px" style="width: 80%;" :rules="editFormRules" ref="editForm">
				<el-form-item label="回复内容" prop="content">
					<el-input type="textarea" :rows="6" v-model="editForm.content"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
	export default {
		data() {
			return {
				searchMsg: {
					pageNo: 1,
					pageSize: 20
				},
				
				list: [],
				total: 0,
				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					content: [{ required: true, message: '请输入回复内容', trigger: 'blur' }],
				},
				//编辑界面数据
				editForm: {
					content: ''			
				}
			}
		},
		methods: {
			cellStyle({row, column, rowIndex, columnIndex}){
				return "text-align:center";
			},
			headerCellStyle({row, rowIndex}){
				return "text-align:center";
			},			
			closeDialog() {
				this.$refs['editForm'].resetFields();
			},
			formatTime(row, column) {
				let date = new Date(row[column.property]);
				let str = this.$utils.formatDate(date, 'yyyy-MM-dd hh:mm:ss');
				// 处理数据库中时间为NULL场景
				if(row[column.property]==undefined || str=="1970-01-01 08:00:00"){
					str = "-";
				}
        		return str;
			},
			formatType (row, column) { // 0=正常/启用  1=禁用
				return row.stock == 1 ? '有货' : '无货';
			},
			addUnit(row, column) { // 添加单位
				return (row[column.property] || 0) + '元'
			},	
			goOrderDetail(orderDetailId) {
				if(orderDetailId) {
					this.$router.push({path:'orderDetail', query:{id: orderDetailId}})
				}
			},
			handleCurrentChange(val) {
				this.searchMsg.pageNo = val;
				this.getList();
			},
			getList(pageNoParam) { // 获取列表
				if(pageNoParam){
				this.searchMsg.pageNo = pageNoParam;
				}
				let vue = this
				let param = Object.assign(vue.searchMsg);
				
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

				vue.$http.post(vue, '/api-order/rest/merchant/appraise/list', param,
					(vue, data) => {
						vue.list = data.data.records
						vue.total = data.data.total
						vue.listLoading = false;
					},(error, data)=> {
						// alert(data);
						vue.listLoading = false;
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)
			},
			handleDel (id) { // 删除
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					// this.listLoading = true;
					let vue = this;
				
					vue.$http.delete(vue, '/api-order/rest/merchant/appraise/delete', {"id" : id},
						function(vue, data) {
							vue.$message({
								showClose: true,
								message: data.message,
								type: 'success'
							});
							vue.getList()
						}, function(error, data) {
							vue.$message({
								showClose: true,
								message: data.message,
								type: 'error'
							});
						}
					)
				}).catch(() => {});
			},
			handleEdit: function (index, row) { // 显示编辑界面
				this.editFormVisible = true;
				if(row){
					this.editForm = {
						appraiseId: row.id,
					}
				}else{
					this.editForm = {
						name: '',			
					}
				}

				//不用这种方式回写，这种方式会导致修改提交的时候所有属性都提交上来
				// if(row){
				// 	this.editForm = Object.assign({}, row);
				// 	this.editForm.selectedOptions2 = [row.province, row.city, row.area];	
				// }else{
				// 	this.editForm = {}
				// }							
			},
			editSubmit: function () { // 编辑
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						// this.editLoading = true;
						let vue = this
						let param = Object.assign({}, this.editForm);
						param.replyType = 1;

						let url = '/api-order/rest/merchant/reply/insert';
						vue.$http.post(vue, url, param,
						(vue, data) => {
							// this.editLoading = false;
							vue.$message({
							showClose: true,
							message: data.message,
							type: 'success'
							});
							
							vue.getList()
							vue.editFormVisible = false;
						}, (error, data) => {
							vue.editFormVisible = false;
							vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
							});
						}
						)	
					}
				});
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

</style>