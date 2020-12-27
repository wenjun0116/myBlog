<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="申请时间">
        <div class="block">
          <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
        </div>
      </el-form-item>
      <el-form-item label="发票类型">
        <el-select
          v-model="queryParams.invoiceTypeId"
          clearable
          size="small"
          style="width: 240px" >
          <el-option
            v-for="invoicetype in invoiceTID"
            :key="invoicetype.invoiceTypeId"
            :label="invoicetype.invoiceTypeName"
            :value="invoicetype.invoiceTypeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="发票状态">
        <el-select
          v-model="queryParams.invoiceStatusId"
          clearable
          size="small"
          style="width: 240px">
          <el-option
            v-for="invoicestatus in invoiceSID"
            :key="invoicestatus.invoiceStatusId"
            :label="invoicestatus.invoiceStatusName"
            :value="invoicestatus.invoiceStatusId" />
        </el-select>
      </el-form-item>
      <el-form-item label="搜索">
        <el-input
          v-model="queryParams.searchValue"
          placeholder="请输入搜索的关键字"
          clearable
          size="small"
          style="width: 240px"  />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    <el-table :data="tableData" v-loading="loading" style="width: 100%">
      <el-table-column prop="createTime" label="申请时间"></el-table-column>

      <el-table-column prop="invoiceId" label="订单号"></el-table-column>

      <el-table-column prop="invoiceTypeName" label="发票类型"></el-table-column>

      <el-table-column prop="invoiceTitle" label="发票抬头"></el-table-column>

      <el-table-column prop="money" label="发票金额"></el-table-column>

      <el-table-column prop="consigneeName" label="申请用户"></el-table-column>

      <el-table-column prop="invoiceStatusName" label="发票状态"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <router-link :to="'/invoice/ticketInfo/' + scope.row.invoiceId" v-has-permi="['service:invoice:look']">
            <el-button size="mini" icon="el-icon-view" type="text">详情</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <div class="table-page">
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
  </div>
</template>

<script>
import {
  getInvoicesList,
  queryInvoiceTypeAll,
  queryInvoiceStatusAll
} from '@/api/serivce/invoice'

export default {
  name: "Invoice",
  data() {
    return {
      //申请时间数组
      dateRange: [],
      //默认查询全部
      activeIndex: '1',
      // 遮罩层
      loading: true,
      // 选中数组
      codes: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 1,
        //发票类型ID
        invoiceTypeId: undefined,
        //发票状态
        invoiceStatusId: undefined,
        //搜索内容
        searchValue: undefined,
      },
      //发票类型
      invoiceTID:[],
      //发票状态
      invoiceSID:[],
      //发票集合
      tableData:[],
      //发票总数居
      total:0,
    }
  },
  created() {
    //加载发票类型
    queryInvoiceTypeAll().then( res => {
      this.invoiceTID = res.data
    })
    //加载发票状态
    queryInvoiceStatusAll().then( res => {
      this.invoiceSID = res.data
    })
    this.getList()
  },
  methods:{
    // 获取发票列表
    getList(){
      this.loading = true
      getInvoicesList(this.addDateRange(this.queryParams, this.dateRange)).then( res => {
        this.tableData = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    /** 搜索操作按钮 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置操作按钮 */
    resetQuery() {
      this.dateRange = []
      this.queryParams.invoiceTypeId = undefined
      this.queryParams.invoiceStatusId = undefined
      this.queryParams.searchValue = undefined
      this.getList()
    }
  }
}
</script>

<style scoped>

  .table-page{
    text-align: center;
  }

</style>
