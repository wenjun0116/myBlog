<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>发票详细</span>
      </div>
      <el-menu :default-active="activeIndex"  class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <el-menu-item index="0">发票信息</el-menu-item>
        <el-menu-item index="1">订单信息</el-menu-item>
      </el-menu>
      <!-- 发票信息 -->
      <div style="margin-top: 10px" v-if="isShowInvoiceOrder">
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>发票信息</span>
          </div>
          <div class="text item">
            <span>{{ '申请时间：' + invoice.createTime }}</span><span>{{ '发票类型：' + invoice.invoiceTypeName}}</span>
          </div>
          <div class="text item">
            <span>{{ '发票状态：' + invoice.invoiceStatusName }}</span>
          </div>
          <div class="text item">
            <span>{{ '发票抬头：' + invoice.invoiceTitle }}</span>
          </div>
          <div class="text item">
            {{ '纳税人识别号：' + invoice.identifyNumber }}
          </div>
          <div class="text item">
            {{ '注册地址：' + invoice.registryAddress }}
          </div>
          <div class="text item">
            {{ '注册银行：' + invoice.registryBank}}
          </div>
          <div class="text item">
            {{ '注册银行账号：' + invoice.bankNumber }}
          </div>
          <div class="text item">
            {{ '发票内容：' + invoice.content}}
          </div>
          <div class="text item">
            {{ '发票金额：' + invoice.money  + '￥'}}
          </div>
        </el-card>
        <el-card shadow="never" style="margin-top: 10px">
          <div slot="header" class="clearfix">
            <span>收件人信息</span>
          </div>
          <div class="text item">
            <span>{{ '收件人：' + invoice.consigneeName }}</span> <span>{{ '手机号：' + invoice.consigneePhone }}</span>
          </div>
          <div class="text item">
            {{ '收件地址：' + invoice.consigneeAddress }}
          </div>
        </el-card>
        <div style="margin-top: 10px;" v-show="invoiceStatus" v-has-permi="['service:invoice:editStatus']">
          <el-button type="danger" size="small" @click="invoiceSuccess">开票成功</el-button>
          <el-button plain size="small" @click="invoiceError">开票失败</el-button>
        </div>
      </div>

      <!-- 订单信息 -->
      <div style="margin-top: 10px" v-else>
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>订单详情</span>
          </div>
          <div class="text item">
            <span>{{ '订单号：' + invoice.order.orderNumber }}</span> <span>{{ '订单创建时间：' + invoice.order.createTime }}</span> <span>{{ '订单状态：' + orderStatusFormat(invoice.order) }}</span>
          </div>
          <div class="text item">
            <span>{{ '订单总金额：' + orderAmountFormat(invoice.order) }}</span> <span>{{ '交易流水号：' + invoice.order.serialNumber }}</span>
          </div>
        </el-card>
        <el-card shadow="never" style="margin-top: 10px">
          <div slot="header" class="clearfix">
            <span>商品信息</span>
          </div>
          <el-table
            :data="orderGoods"
            style="width: 100%">
            <el-table-column prop="orderGoodsId" label="订单行号">
            </el-table-column>
            <el-table-column label="商品" width="500px">
              <template slot-scope="scope">
                <el-row :gutter="10">
                  <el-col :span="6">
                    <el-image :src="goodsMainImageUrl(scope.row.goods)" style="width: 100%"/>
                  </el-col>
                  <el-col :span="18">
                    <p style="font-weight: 600;">{{ scope.row.goods.goodsName }}</p>
                    <p>商品编码：{{ scope.row.goods.goodsCode }}</p>
                    <p>品牌：{{ scope.row.goods.goodsBrand }}</p>
                  </el-col>
                </el-row>
              </template>
            </el-table-column>
            <el-table-column prop="" label="单价" :formatter=" unitPriceFormat"></el-table-column>
            <el-table-column prop="goodsNumber" label="数量"></el-table-column>
            <el-table-column label="订单行金额" :formatter="calculateGoodsPricesFormat"></el-table-column>
            <el-table-column
              label="订单状态"
              :formatter="orderGoodsStatusFormat"
            >
            </el-table-column>
            <el-table-column
              label="物流状态"
              :formatter="logisticsStatusFormat">
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script>
import {
  getInvoiceInfo,
  updateInvoiceStatus
} from '@/api/serivce/invoice'

import { orderAmount } from '@/utils/demo'

export default {
  name: "TicketInfo",
  data() {
    return {
      activeIndex: '0',
      isShowInvoiceOrder: true,
      invoiceStatus: true,
      // 订单状态字典查询
      orderStatusOptions: [],
      // 订单类型
      orderTypeOptions: [],
      // 物流状态
      logisticsStatusOptions: [],
      // 订单商品列表
      orderGoods: null,
      // 发票详情
      invoice: null,
      // 加载层信息
      loading: [],
    }
  },
  created() {
    const invoiceId = this.$route.params && this.$route.params.invoiceId
    this.getInvoice(invoiceId)
    this.openLoading()
    // 订单状态选择字典查询
    this.getDicts('xy_order_status').then(res => {
      this.orderStatusOptions = res.data
    })
    // 订单类型选择字典查询
    this.getDicts('xy_order_type').then(res => {
      this.orderTypeOptions = res.data
    })
    // 物流状态选择字典查询
    this.getDicts('xy_logistics_logisticsStatus').then(res => {
      this.logisticsStatusOptions = res.data
    })
  },
  methods: {
    /** 获取发票详细 */
    getInvoice(invoiceId) {
      getInvoiceInfo(invoiceId).then(res => {
        this.invoice = res.data
        this.orderGoods = res.data.order.xyOrderGoods
        this.invoiceStatus = res.data.invoiceStatusName !== '成功'
        this.loading.close()
      })
    },
    /** 菜单选择 */
    handleSelect() {
      this.isShowInvoiceOrder = !this.isShowInvoiceOrder
    },
    /** 字典订单状态字典翻译 */
    orderStatusFormat(val) {
      return this.selectDictLabel(this.orderStatusOptions, val.orderStatus)
    },
    /** 字典订单类型字典翻译 */
    orderTypeFormat(val) {
      return this.selectDictLabel(this.orderTypeOptions, val.orderType)
    },
    /** 字典订单行状态字典翻译 */
    orderGoodsStatusFormat(val) {
      return this.selectDictLabel(this.orderStatusOptions, val.orderGoodsStatus)
    },
    /** 字典物流状态字典查询 */
    logisticsStatusFormat(val) {
      return this.selectDictLabel(this.logisticsStatusOptions, val.xyLogistics.logisticsStatus)
    },
    /** 计算订单金额 */
    orderAmountFormat(row) {
      return '￥' + orderAmount(row)
    },
    /** 显示商品图片 */
    goodsMainImageUrl(row, column) {
      return process.env.VUE_APP_BASE_API + row.goodsImage
    },
    /** 商品价格 */
    unitPriceFormat(row) {
      return '￥' + row.goods.referencePrice
    },
    /** 计算商品价格 */
    calculateGoodsPricesFormat(row) {
      return '￥' + row.goods.referencePrice * row.goodsNumber
    },
    /** 开票成功按钮 */
    invoiceSuccess() {
      const invoice = {
        invoiceId: this.invoice.invoiceId,
        invoiceStatusId: 101
      }
      this.$confirm(
        '是否确认开票成功?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return updateInvoiceStatus(invoice)
        })
        .then(() => {
          this.getInvoice(invoice.invoiceId);
          this.msgSuccess("操作成功");
        })
        .catch(function () {});
    },
    /** 开票失败 */
    invoiceError() {
      const invoice = {
        invoiceId: this.invoice.invoiceId,
        invoiceStatusId: 102
      }
      this.$confirm(
        '是否确认开票失败?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return updateInvoiceStatus(invoice)
        })
        .then(() => {
          this.getInvoice(invoice.invoiceId);
          this.msgSuccess("操作成功");
        })
        .catch(function () {});
    },
    // 打开加载层
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: "拼命读取中",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    }
  }
}
</script>

<style>
.el-card__body .text.item {
  font-size: 14px;
  margin-top: 10px;
}
.el-card__body .text.item span {
  margin-right: 30px;
}
</style>
