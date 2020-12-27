<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryParams" v-show="showSearch"  :inline="true">
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态">
        <el-select
          v-model="queryParams.orderStatus"
          clearable
          size="small"
          style="width: 240px">
          <el-option
            v-for="dict in orderStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item label="支付状态">
        <el-select
          v-model="queryParams.payStatus"
          clearable
          size="small"
          style="width: 240px">
          <el-option
            v-for="dict in payStatussStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单类型">
        <el-select
          v-model="queryParams.orderType"
          clearable
          size="small"
          style="width: 240px">
          <el-option
            v-for="item in orderTypesOptions"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="搜索">
        <el-input
          v-model="queryParams.searchValue"
          placeholder="请输入搜索的关键字"
          clearable
          size="small"
          style="width: 240px" />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" v-has-permi="['service:orderform:query']" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>
    <div>
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <el-menu-item index="-1">全部</el-menu-item>
        <el-menu-item v-for="dict in orderStatusOptions" :key="dict.dictValue" :index="dict.dictValue">
          {{ dict.dictLabel }}
          <span v-if="countOrderStatusTotal(dict.dictValue) > 0">
            ({{ countOrderStatusTotal(dict.dictValue) }})
          </span>
        </el-menu-item>
      </el-menu>
      <el-table :data="orderList" v-loading="loading">
        <el-table-column label="编号" align="center" prop="orderId" />
        <el-table-column label="订单编号" align="center" prop="orderNumber" width="360px" />
        <el-table-column label="下单时间" align="center" prop="createTime" />
        <el-table-column label="运费金额" align="center" :formatter="freightFormat"  />
        <el-table-column label="订单金额" align="center" :formatter="orderAmountFormat" />
        <el-table-column label="订单状态" align="center" :formatter="orderStatusFormat" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100px">
          <template slot-scope="scope">
            <router-link :to="'/orderform/orderInfo/' + scope.row.orderId" v-has-permi="['service:orderform:look']">
              <el-button size="mini" icon="el-icon-view" type="text">详情</el-button>
            </router-link>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-if="total > 0" :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
        />
    </div>
  </div>
</template>

<script>
import {
  getOrderList
} from '@/api/serivce/order'

import {
  calculateFreight,
  orderAmount
} from '@/utils/demo'

export default {
  name: "Order",
  data() {
    return {
      //物流状态
      logisticsStatusOptions: [],
      //支付状态
      payStatussStatusOptions: [],
      //订单状态选择
      orderStatusOptions: [],
      //订单类型选择
      orderTypesOptions: [],
      //默认查询全部
      activeIndex: '-1',
      // 用户展示订单状态列表
      orderStatusList: null,
      // 日期范围
      dateRange: [],
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 订单列表总数
      total: 0,
      // 订单表格数据
      orderList: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        //订单类型
        orderType: undefined,
        //订单状态
        orderStatus: undefined,
        //支付方式
        payment: undefined,
        payStatus: undefined,
        //搜索内容
        searchValue: undefined,
      },
      // 统计订单状态数量
      orderStatusTotal: null,
    }
  },
  created() {
    // 查询全部订单信息
    this.getList()
    // 统计订单状态数量
    this.getOrderStatusTotal()
    // 订单状态选择字典查询
    this.getDicts('xy_order_status').then(res => {
      this.orderStatusOptions = res.data
    })
    // 订单类型选择字典查询
    this.getDicts('xy_order_payStatus_status').then(res => {
      this.payStatussStatusOptions = res.data
    })
    // 订单支付方式字典查询
    this.getDicts('xy_order_type').then(res => {
      this.orderTypesOptions = res.data
    })
  },
  methods: {
    /** 获取订单列表 */
    getList() {
      getOrderList(this.addDateRange(this.queryParams, this.dateRange)).then( res => {
        this.orderList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    /** 获取订单状态列表统计数量 */
    getOrderStatusTotal() {
      getOrderList().then(res => {
        this.orderStatusTotal = res.rows
        console.log(this.orderStatusTotal)
      })
    },
    /** 计算订单状态总数 */
    countOrderStatusTotal(val) {
      let total = 0
      if (this.orderStatusTotal != null) {
        for (let i = 0; i < this.orderStatusTotal.length; i++) {
          if (val === this.orderStatusTotal[i].orderStatus) {
            total++
          }
        }
      }
      return total
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    handleSelect(key, keyPath) {
      if (key === '-1') {
        this.resetQuery()
      } else {
        this.queryParams.orderStatus = key
        this.getList()
        this.queryParams.orderStatus = undefined
      }
    },
    /** 查询表单重置 */
    resetQuery() {
      this.dateRange = []
      this.queryParams.searchValue = undefined
      this.queryParams.orderType = undefined
      this.queryParams.orderStatus = undefined
      this.queryParams.payStatus = undefined
      this.handleQuery()
    },
    /** 字典订单状态字典翻译 */
    orderStatusFormat(row) {
      return this.selectDictLabel(this.orderStatusOptions, row.orderStatus)
    },
    /** 计算运费 */
    freightFormat(row) {
      return '￥' + calculateFreight(row)
    },
    /** 计算订单金额 */
    orderAmountFormat(row) {
      return '￥' + orderAmount(row)
    },
  }
}
</script>

<style scoped>

.xy-order-info>span{
  margin-left: 20px;
  line-height: 40px;
}

</style>
