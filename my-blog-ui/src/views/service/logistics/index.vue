<template>
  <div class="app-container">
    <el-container class="logistics_content">
      <el-aside v-show="showSearch">
        <el-header>筛选</el-header>
        <el-form ref="queryForm" :model="queryParams" status-icon label-width="100px" class="demo-ruleForm">
          <el-form-item label="物流默认">
            <el-select v-model="queryParams.isDefult" clearable placeholder="请选择">
              <el-option v-for="dict in isDefultOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="物流状态">
            <el-select v-model="queryParams.logisticsStatus" clearable placeholder="请选择">
              <el-option v-for="dict in logisticsStatusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="搜索" prop="logistics_name">
            <el-input v-model="queryParams.logisticsName" placeholder="请输入关键字"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-aside>
      <el-main>
        <el-header>
          <span>物流列表</span>
          <el-button type="primary" style="margin-left:30px;" v-hasPermi="['service:logistics:add']" @click="handleAdd" size="mini">新增</el-button>
        </el-header>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
        <el-table v-loading="loading" :data="logisticsList" style="width: 100%; margin-top: 10px;">
          <el-table-column prop="logisticsName" align="center" label="模板名称">
          </el-table-column>
          <el-table-column prop="isDefult" align="center" label="是否默认" :formatter="isDefultFormat">
          </el-table-column>
          <el-table-column prop="logisticsStatus" align="center" label="状态" :formatter="statusFormat">
          </el-table-column>
          <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:user:edit']">修改</el-button>
              <el-button v-if="scope.row.userId !== 1" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:user:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
    <!-- 分页 -->
    <pagination v-if="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <!-- 添加或修改物流对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body @close="dialogClose" @open="dialogOpen">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="模板名称" :label-width="formLabelWidth" prop="logisticsName">
          <el-input v-model="form.logisticsName" autocomplete="off" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item label="物流类型" :label-width="formLabelWidth" prop="logisticsType">
          <el-radio-group v-model="form.logisticsType" @change="isCustom">
            <el-radio v-for="dict in typeOptions" :key="dict.dictValue" :label="dict.dictValue">{{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <div v-show="isType">
          <el-divider content-position="left">包邮配送区域</el-divider>
          <el-form-item label="发货区域" :label-width="formLabelWidth">
            <el-select v-model="form.shipAreaCityId" placeholder="省/直辖市" @change="shipAreaCity" style="width: 140px;">
              <el-option
                v-for="item in areaCityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id" />
            </el-select>
            <el-select v-model="form.shipCityId" placeholder="市" @change="shipCity" style="width: 140px;">
              <el-option
                v-for="item in shipCityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id" />
            </el-select>
            <el-select v-model="form.shipCountyId" placeholder="区县" style="width: 140px;">
              <el-option
                v-for="item in shipCountyOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="收货区域" :label-width="formLabelWidth">

            <el-select v-model="form.shippingAreaCityId" placeholder="省/直辖市" @change="shippingAreaCity" style="width: 140px;">
              <el-option
                v-for="item in areaCityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id" />
            </el-select>

            <el-select v-model="form.shippingCityId" placeholder="市" @change="shippingCity" style="width: 140px;">
              <el-option
                v-for="item in shippingCityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id" />
            </el-select>

            <el-select v-model="form.shippingCountyId" placeholder="区县" style="width: 140px;">
              <el-option
                v-for="item in shippingCountyOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id" />
            </el-select>
          </el-form-item>
          <el-divider content-position="left">付费配送区域</el-divider>
          <el-form-item :label-width="formLabelWidth">
            <el-button type="primary" size="mini">添加方案</el-button>
          </el-form-item>
          <el-form-item label="【方案一】" :label-width="formLabelWidth"></el-form-item>
          <el-form-item label="配送区域" :label-width="formLabelWidth"></el-form-item>
          <el-form-item label="物流类型" :label-width="formLabelWidth">
            <el-radio-group v-model="form.chargeType" @change="isCharge">
              <el-radio v-for="dict in chargeTypeOptions" :key="dict.dictValue" :label="dict.dictValue">{{ dict.dictLabel }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="配送费用" :label-width="formLabelWidth">
            <div v-if="isChargeType">
              <div>
                <span>首 {{ isChargeType }}</span>
                <el-input-number v-model="form.firstGoods" size="mini">
                </el-input-number>
                <span>{{ unit }}</span>
                <span style="margin-left: 50px;">邮 费</span>
                <el-input-number v-model="form.firstGoodsMoney" size="mini"/> 元
              </div>
              <br>
              <div>
                <span>续 {{ isChargeType }}</span>
                <el-input-number v-model="form.duplicate" size="mini">
                </el-input-number>
                <span>{{ unit }}</span>
                <span style="margin-left: 50px;">邮费</span>
                <el-input-number v-model="form.duplicateMoney" size="mini"/> 元
              </div>
            </div>
          </el-form-item>
        </div>
        <el-form-item class="inputLogistics-button">
          <el-button type="primary" @click="submitForm">确认</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  listLogistics,
  getLogistics,
  addLogistics,
  updateLogistics,
  delLogistics
} from '@/api/serivce/logistics'
import {
  chinaAreaCitySelect,
  selectChildren,
} from '@/api/serivce/china'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: "Logistics",
  components: { Treeselect },
  data() {
    return {
      // 显示搜索条件
      showSearch: true,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 物流表单列表
      logisticsList: [],
      isType: false,
      isChargeType: '件',
      unit: '件',
      //查询表单
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        //是否默认
        isDefult: '',
        //状态
        logisticsStatus: '',
        //搜索名
        logisticsName: ''
      },
      //新增物流表单
      form: {
        //主键
        logisticsId: undefined,
        //物流名
        logisticsName: undefined,
        //类型
        logisticsType: '0',
        // 发货地址 省直辖市ID
        shipAreaCityId: undefined,
        // 发货地址 市ID
        shipCityId: undefined,
        // 发货地址 区县ID
        shipCountyId: undefined,
        // 收货地址 省直辖市ID
        shippingAreaCityId: undefined,
        // 收货地址 市ID
        shippingCityId: undefined,
        // 收货地址 区县ID
        shippingCountyId: undefined,
        //物流收费类型    0表示按件数收费,1表示按重量收费
        chargeType: '0',
        //首件 or 首重
        firstGoods: 0,
        //首件邮费 or 首重邮费
        firstGoodsMoney: 0,
        //续件 or 续重
        duplicate: 0,
        //续件邮费 or 续重邮费
        duplicateMoney: 0,
        //是否默认      0表示默认,1表示自定义
        isDefult: '0',
        //物流状态ID
        logisticsStatusId: undefined,
      },
      //form表单宽度
      formLabelWidth: '120px',
      // 物流默认 select
      isDefultOptions: [],
      // 物流状态 select
      logisticsStatusOptions: [],
      // 物流类型 单选
      typeOptions: [],
      // 物流收费类型 单选
      chargeTypeOptions: [],
      // 表单验证
      rules: {
        logisticsName: [
          { required: true, message: "模板称不能为空", trigger: "blur" },
        ],
        logisticsType: [
          { required: true, message: "请选择物流类型", trigger: "blur" },
        ]
      },
      // 省直辖市
      areaCityOptions: undefined,
      // 发货地址 市下拉列表
      shipCityOptions: undefined,
      // 发货地址 区县下拉列表
      shipCountyOptions: undefined,
      // 收货地址 市下拉列表
      shippingCityOptions: undefined,
      // 收货地址 区县下拉列表
      shippingCountyOptions: undefined,
    };
  },
  created() {
    this.getList()
    this.getChinaAreaCitySelect()
    /** 加载物流默认select列表 */
    this.getDicts('xy_logistics_isDefult').then(res => {
      this.isDefultOptions = res.data
    })
    /** 加载物流状态select列表 */
    this.getDicts('xy_logistics_logisticsStatus').then(res => {
      this.logisticsStatusOptions = res.data
    })
    /** 加载物流类型单选 */
    this.getDicts('xy_logistics_type').then(res => {
      this.typeOptions = res.data
    })
    /** 加载物流收费方式单选 */
    this.getDicts('xy_logistics_charges').then(res => {
      this.chargeTypeOptions = res.data
    })
  },
  methods: {
    /** ==================== 地址加载 ===================== */
    /** 加载省直辖市 */
    getChinaAreaCitySelect() {
      chinaAreaCitySelect().then(res => {
        this.areaCityOptions = res.data
      })
    },
    /** 发货地址 通过省/直辖市ID 加载市级下拉选择列表 */
    shipAreaCity(val) {
      selectChildren(val).then(res => {
        this.shipCityOptions = res.data
        this.form.shipCityId = this.shipCityOptions[0].id
        selectChildren(this.form.shipCityId).then(res => {
          this.shipCountyOptions = res.data
          this.form.shipCountyId = (this.shipCountyOptions.length > 0 ? this.shipCountyOptions[0].id : undefined)
        })
      })
    },
    /** 发货地址 通过市ID 加载区县下拉选择列表 */
    shipCity(val) {
      selectChildren(val).then(res => {
        this.shipCountyOptions = res.data
        this.form.shipCountyId = (this.shipCountyOptions.length > 0 ? this.shipCountyOptions[0].id : undefined)
      })
    },
    /** 收货地址 通过省/直辖市ID 加载市级下拉选择列表 */
    shippingAreaCity(val) {
      selectChildren(val).then(res => {
        this.shippingCityOptions = res.data
        this.form.shippingCityId = this.shippingCityOptions[0].id
        selectChildren(this.form.shippingCityId).then(res => {
          this.shippingCountyOptions = res.data
          this.form.shippingCountyId = (this.shippingCountyOptions.length > 0 ? this.shippingCountyOptions[0].id : undefined)
        })
      })
    },
    /** 收货地址 通过市ID 加载区县下拉选择列表 */
    shippingCity(val) {
      selectChildren(val).then(res => {
        this.shippingCountyOptions = res.data
        this.form.shippingCountyId = (this.shippingCountyOptions.length > 0 ? this.shippingCountyOptions[0].id : undefined)
      })
    },
    /** ====================   ===================== */

    /** 获取物流列表 */
    getList() {
      this.loading = true
      listLogistics(this.queryParams).then(res => {
        this.loading = false
        this.logisticsList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    // 字典默认状态字典翻译
    isDefultFormat(row, column) {
      return this.selectDictLabel(this.isDefultOptions, row.isDefult)
    },
    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.logisticsStatusOptions, row.logisticsStatus)
    },
    /** 新增按钮 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加物流'
    },
    /** 修改按钮 */
    handleUpdate(row) {
      this.reset()
      getLogistics(row.logisticsId).then(res => {
        this.form = res.data
        if (this.form.logisticsType != 0) {
          this.shipAreaCity(this.form.shipAreaCityId)
          this.shipCity(this.form.shipCityId)
          this.shippingAreaCity(this.form.shippingAreaCityId)
          this.shippingCity(this.form.shippingCityId)
        }
        this.open = true
        this.title = '修改物流'
      })
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm(
        '是否确认删除物流编号为"' + row.logisticsId + '"的数据项?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(function() {
          return delLogistics(row.logisticsId);
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {  })
    },
    /** 确认按钮 */
    submitForm: function() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.logisticsId != undefined) { // 修改
            updateLogistics(this.form).then(res => {
              if (res.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {  // 新增
            addLogistics(this.form).then((res) => {
              if (res.code === 200) {
                this.msgSuccess('新增成功')
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        logisticsId: undefined,
        logisticsName: undefined,
        logisticsType: '0',
        // 发货地址 省直辖市ID
        shipAreaCityId: undefined,
        // 发货地址 市ID
        shipCityId: undefined,
        // 发货地址 区县ID
        shipCountyId: undefined,
        // 发货地址 省直辖市ID
        shippingAreaCityId: undefined,
        // 发货地址 市ID
        shippingCityId: undefined,
        // 发货地址 区县ID
        shippingCountyId: undefined,
        chargeType: '0',
        firstGoods: 0,
        firstGoodsMoney: 0,
        first_repeat: 0,
        first_repeat_money: 0,
        duplicate: 0,
        duplicateMoney: 0,
        isDefult: '0',
        logisticsStatusId: undefined,
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.pageSize = 10
      this.queryParams.logisticsName = undefined
      this.queryParams.pageNum = 1
      this.queryParams.isDefult = undefined
      this.queryParams.logisticsStatus = undefined
      this.handleQuery()
    },
    isCustom(val) {
      this.isType = (val === '0') ? false : true
    },
    isCharge(val) {
      this.isChargeType = (val !== '0') ? '重' : '件'
      this.unit = (val !== '0') ? 'kg' : '件'
    },
    dialogClose() {
      this.isType = false
      this.isChargeType = '件'
      // 发货地址 市下拉列表
      this.shipCityOptions = undefined
      // 发货地址 区县下拉列表
      this.shipCountyOptions = undefined
      // 收货地址 市下拉列表
      this.shippingCityOptions = undefined
      // 收货地址 区县下拉列表
      this.shippingCountyOptions = undefined
    },
    dialogOpen() {
      this.isType = this.form.logisticsType !== '0'
    }
  }
}
</script>

<style>
.el-select.el-select--medium {
  margin-right: 10px;
}
</style>

<style>
.logistics_content{
  margin: 18px;
  height: 100%;
}
.el-aside,.el-main{
  background: white;
  border: 1px solid rgba(228, 228, 228, 1);
  height: 100%;
}
.el-main{
  margin-left: 10px;
  padding: 8px;
}
.table-page,.inputLogistics-button{
  text-align: center;
}
</style>
