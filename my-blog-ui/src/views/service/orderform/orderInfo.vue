<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>订单详情</span>
      </div>
      <div class="text item order-status">
        <span>{{'当前订单状态：' + orderStatusFormat(order) }}</span>
      </div>
      <el-card shadow="never" style="margin-top: 20px">
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>订单详情</span>
          </div>
          <div class="text item">
            <span>{{'订单号： ' + order.orderNumber }}</span>  <span>{{  '创建时间： ' + order.createTime }}</span>  <span>{{  '订单类型： ' + orderTypeFormat(order) }}</span>
          </div>
          <div class="text item">
            <span>{{'订单状态： ' + orderStatusFormat(order) }}</span> <span>{{'订单总金额：' + orderAmountFormat(order) }}</span>
          </div>
          <div class="text item">
            <span>{{'交易流水号： ' + order.serialNumber }}</span>
          </div>
        </el-card>
        <el-card shadow="never" style="margin-top: 10px">
          <div slot="header" class="clearfix">
            <span>采购方信息</span>
          </div>
          <div class="text item">
            <span>{{'用户帐号： ' + order.enterprise }}</span>  <span>{{'采购企业： ' + order.enterpriseName }}</span>
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
            <el-table-column prop="" label="单价" :formatter="unitPriceFormat"></el-table-column>
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
        <el-card shadow="never" style="margin-top: 10px">
          <div slot="header" class="clearfix">
            <span>收货人信息</span>
          </div>
          <div class="text item">
            <span>{{'收货人： ' + order.consigneeName }}</span>  <span>{{'手机号： ' + order.consigneePhone }}</span>  <span>{{'座机号：' + order.plane }}</span>
          </div>
          <div class="text item">
            <span>{{'收货地址： ' + order.consigneeAddress }}</span>
          </div>
        </el-card>
        <el-card shadow="never" style="margin-top: 10px">
          <div slot="header" class="clearfix">
            <span>发票信息</span>
          </div>
          <div class="text item">
            <span>{{'发票抬头： '+order.xyInvoice.invoiceTitle }}</span>
          </div>
          <div class="text item">
            <span>{{'发票类型： ' +order.xyInvoice.invoiceTypeName}}</span>  <span>{{'发票内容： ' +order.xyInvoice.content}}</span>
          </div>
          <div class="text item">
            <span>{{'发纳税人识别号： '+order.xyInvoice.identifyNumber }}</span>  <span>{{'注册地址： '+order.xyInvoice.registryAddress }}</span>
          </div>
          <div class="text item">
            <span>{{ '注册银行：' +order.xyInvoice.registryBank}}</span>   <span>{{'注册银行账号：'+order.xyInvoice.bankNumber}}</span>
          </div>
          <div class="text item">
            <span>{{'发票状态： ' +order.xyInvoice.invoiceStatusName}}</span>  <span>{{'链接地址： ' +order.xyInvoice.consigneeAddress}}</span>
          </div>
        </el-card>
        <el-card shadow="never" style="margin-top: 10px">
          <div slot="header" class="clearfix">
            <span>备注信息</span>
          </div>
          <div class="text item">
            备注内容：
            <el-tooltip class="item" effect="dark" :content="order.descriptor" placement="top">
              <el-badge is-dot class="item">备注信息内容</el-badge>
            </el-tooltip>
          </div>
        </el-card>
      </el-card>
      <div style="margin-top: 10px" v-show="orderStatus.siShowBut" v-has-permi="['service:orderform:editStatus']">
        <el-button type="danger" size="small" @click="mainBtn">{{ orderStatus.butMain }}</el-button>
        <el-button size="small" plain @click="btn">{{ orderStatus.but }}</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import {
  getOrderInfo,
  updateOrderStatus
} from '@/api/serivce/order'

import { orderAmount, orderStatus } from '@/utils/demo'

export default {
  name: "OrderInfo",
  data() {
    return {
      // 订单商品列表
      orderGoods: null,
      // 订单详情
      order: null,
      // 订单状态字典查询
      orderStatusOptions: [],
      // 订单类型
      orderTypeOptions: [],
      // 物流状态
      logisticsStatusOptions: [],
      // 订单操作
      orderStatus: null,
      // 加载层
      loading: []
    }
  },
  created() {
    const orderId = this.$route.params && this.$route.params.orderId
    this.getOrder(orderId)
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
    /** 查询订单详情 */
    getOrder(orderId) {
      getOrderInfo(orderId).then(res => {
        this.order = res.data
        this.orderGoods = res.data.xyOrderGoods
        this.orderStatus = orderStatus(res.data.orderStatus)
        this.loading.close()
      })
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
    goodsMainImageUrl(row, column) {
      return process.env.VUE_APP_BASE_API + row.goodsImage
    },
    unitPriceFormat(row) {
      return '￥' + row.goods.referencePrice
    },
    calculateGoodsPricesFormat(row) {
      return '￥' + row.goods.referencePrice * row.goodsNumber
    },
    mainBtn() {
      const order = {
        orderId: this.order.orderId,
        orderStatus: this.orderStatus.mainValue
      }
      this.$confirm(
        '是否' + this.orderStatus.butMain + '?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return updateOrderStatus(order)
        })
        .then(() => {
          this.getOrder(order.orderId)
          this.msgSuccess("修改成功");
        })
        .catch(function () {});
    },
    btn() {
      const order = {
        orderId: this.order.orderId,
        orderStatus: this.orderStatus.mainValue
      }
      this.$confirm(
        '是否' + this.orderStatus.but + '?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function() {
          return updateOrderStatus(order)
        }).then(() => {
          this.getOrder(order.orderId)
          this.msgSuccess("操作成功")
        }).catch(function() {  })
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
.order-status {
  padding: 20px;
  border-radius: 4px;
  background: rgb(217, 236, 255);
}
.el-card__body .text.item {
  font-size: 14px;
  margin-top: 10px;
}
.el-card__body .text.item span {
  margin-right: 30px;
}
</style>
