<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 商品类目数据 -->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="categoryName"
            placeholder="请输入商品类目名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px" />
        </div>
        <div class="head-container">
          <el-tree
            :data="goodsCategory"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree" default-expand-all
            @node-click="handleNodeClick" />
        </div>
      </el-col>
      <!-- 商品数据 -->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" v-show="showSearch"  :inline="true">
          <el-form-item label="商品类型">
            <el-select
              v-model="queryParams.goodsType"
              clearable
              size="small"
              style="width: 240px">
              <el-option
                v-for="goodsTypeDict in goodsTypeOptions"
                :key="goodsTypeDict.dictValue"
                :label="goodsTypeDict.dictLabel"
                :value="goodsTypeDict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="商品状态">
            <el-select
              v-model="queryParams.goodsStatus"
              clearable
              size="small"
              style="width: 240px">
              <el-option
                v-for="goodsStatusDict in goodsStatusOptions"
                :key="goodsStatusDict.dictValue"
                :label="goodsStatusDict.dictLabel"
                :value="goodsStatusDict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="商品名称">
            <el-input
              v-model="queryParams.goodsName"
              placeholder="请输入搜索的关键字"
              clearable
              size="small"
              style="width: 240px" />
          </el-form-item>
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-has-permi="['service:goods:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-has-permi="['service:goods:edit']"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-has-permi="['service:goods:remove']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['service:goods:export']"
            >导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 商品列表 -->
        <el-table  v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="商品" prop="goodsCode" align="middle" width="470px">
            <template slot-scope="scope">
              <el-row :gutter="10">
                <el-col :span="6">
                  <el-image :src="goodsMainImageUrl(scope.row)" style="width: 100%"/>
                </el-col>
                <el-col :span="18">
                  <p style="font-weight: 600;">{{ scope.row.goodsName }}</p>
                  <p>商品编码：{{ scope.row.goodsCode }}</p>
                  <p>品牌：{{ scope.row.goodsBrand }}</p>
                </el-col>
              </el-row>
            </template>
          </el-table-column>
          <el-table-column label="商品类目" align="center" prop="category" width="200px" />
          <el-table-column label="商品类型" align="center" prop="goodsType" :formatter="statusFormat" />
          <el-table-column label="商品参考价" align="center" prop="referencePrice" width="100px" />
          <el-table-column label="商品折扣率" align="center" prop="discountRate" width="100px" :formatter="discountRate" />
          <el-table-column label="商品优惠价" align="center" prop="preferentialPrice" width="100px" />
          <el-table-column label="商品状态" align="center" prop="goodsStatus" :formatter="goodsStatusFormat" />
          <el-table-column label="商品库存" align="center" prop="inventoryStatus" :formatter="inventoryStatusFormat" />
          <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['service:goods:edit']">编辑</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['service:goods:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pageination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-form-item label="商品编码" prop="goodsCode">
            <el-input v-model="goodsCode" :disabled="true"  style="width: 280px" />
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品名称" prop="goodsName">
              <el-input v-model="form.goodsName" placeholder="请填写商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品类目" prop="categoryId">
              <treeselect  v-model="form.categoryId" :options="goodsCategory" :disable-branch-nodes="true" :show-count="true"  placeholder="请选择商品类目" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品品牌" prop="goodsBrand">
              <el-input v-model="form.goodsBrand" placeholder="请填写商品品牌" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品型号" prop="goodsModel">
              <el-input v-model="form.goodsModel" placeholder="请填写商品型号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品重量" prop="weight">
              <el-input v-model="form.weight" placeholder="请填写商品重量(已克为单位)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售单位" prop="goodsUnit">
              <el-input v-model="form.goodsUnit" placeholder="请填写销售单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品库存" prop="inventory">
              <el-input v-model.number="form.inventory" placeholder="请填写商品库存" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存状态" prop="inventoryStatus">
              <el-select v-model="form.inventoryStatus"  placeholder="请选择商品库存状态">
                <el-option  v-for="inventoryStatus in goodsInventoryStatus" :key="inventoryStatus.dictValue" :label="inventoryStatus.dictLabel" :value="inventoryStatus.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="参考价" prop="referencePrice">
              <el-input v-model.number="form.referencePrice" placeholder="请填写商品参考价" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="折扣率" prop="discountRate">
              <el-input v-model.number="form.discountRate" placeholder="请填写商品折扣率(%)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="优惠价" prop="preferentialPrice">
              <el-input v-model.number="form.preferentialPrice" placeholder="请填写商品优惠价" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="商品图片">
            <el-upload
              class="upload-demo"
              ref="goodsImage"
              list-type="picture-card"
              :headers="uploadGoodsImage.headers"
              :action="uploadGoodsImage.url + '?isMainImages=' + uploadGoodsImage.isMainImages + '&goodsCode=' + goodsCode"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :on-success="handleFileSuccess"
              :auto-upload="false">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
            </el-upload>
            <el-checkbox v-model="uploadGoodsImage.isMainImages" /> 是否设置为主图
            <el-dialog :visible.sync="goodsImageDialogVisible" append-to-body>
              <img width="100%" :src="goodsImageUrl" alt="">
            </el-dialog>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="条形码">
            <el-upload
              ref="goodsBarCodeImage"
              class="avatar-uploader"
              :headers="uploadGoodsBarCode.headers"
              :action="uploadGoodsBarCode.url + '?goodsId=' + goodsId"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品参数" prop="goodsParameter">
              <el-input v-model="form.goodsParameter" placeholder="请填写商品参数" size="small" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参数值" prop="parameterValue">
              <el-input-number v-model.number="form.parameterValue" size="mini" controls-position="right" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="描述信息" prop="descriptor">
            <el-input type="textarea" autosize placeholder="请输入内容" v-model="form.descriptor"></el-input>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="物流模板" prop="logisticsId">
            <el-select v-model="form.logisticsId" clearable size="small">
              <el-option v-for="logistics in logisticsSelect" :key="logistics.logisticsId" :label="logistics.logisticsName" :value="logistics.logisticsId" />
            </el-select>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="商品状态" prop="goodsStatus">
            <el-select v-model="form.goodsStatus" clearable>
              <el-option
                v-for="goodsStatusDict in goodsStatusOptions"
                :key="goodsStatusDict.dictValue"
                :label="goodsStatusDict.dictLabel"
                :value="goodsStatusDict.dictValue" />
            </el-select>
          </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 认</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getGoodsCode,
  listGoods,
  addGoods,
  getGoods,
  updateGoods,
  delGoods,
  exportGoods
} from '@/api/serivce/goods'
import { getToken } from '@/utils/auth'
import { treeselect as categoryTreeselect } from '@/api/serivce/category'
import { getLogisticsSelect } from '@/api/serivce/logistics'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: "Goods",
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      goodsId: '',
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 商品表格数据
      goodsList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 商品类目名称
      categoryName: undefined,
      // 商品类型数据字典
      goodsTypeOptions: [],
      // 商品状态数据字典
      goodsStatusOptions: [],
      // 商品图片上传
      uploadGoodsImage: {
        // 是否是主图
        isMainImages: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/service/goods/goodsImage',
      },
      uploadGoodsBarCode: {
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/service/goods/goodsBarCode',
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // 商品类目编号
        categoryId: undefined,
        // 商品类型
        goodsType: undefined,
        // 商品状态
        goodsStatus: undefined,
        // 商品名称
        goodsName: undefined
      },
      // 表单验证
      rules: {
        goodsName: [
          { required: true, message: '请填写商品名称', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请选择商品类目', trigger: 'blur' }
        ],
        goodsBrand: [
          { required: true, message: '请填写商品品牌', trigger: 'blur' }
        ],
        goodsModel: [
          { required: true, message: '请填写商品型号', trigger: 'blur' }
        ],
        weight: [
          { required: true, message: '请填写商品重量(已克为单位)', trigger: 'blur' }
        ],
        goodsUnit: [
          { required: true, message: '请填写商品销售单位', trigger: 'blur' }
        ],
        inventory: [
          { required: true, message: '请填写商品库存', trigger: 'blur' },
          { type: 'number', message: '库存必须是数字' }
        ],
        inventoryStatus: [
          { required: true, message: '请选择商品库存状态', trigger: 'blur' }
        ],
        referencePrice: [
          { required: true, message: '请填写商品参考价格', trigger: 'blur' },
          { type: 'number', message: '参考价格必须是数字' }
        ],
        discountRate: [
          { required: true, message: '请填写商品折扣率', trigger: 'blur' },
          { type: 'number', message: '折扣率必须是数字' }
        ],
        preferentialPrice: [
          { required: true, message: '请填写商品优惠价', trigger: 'blur' },
          { type: 'number', message: '优惠价必须是数字' }
        ],
        goodsParameter: [
          { required: true, message: '请填写商品参数', trigger: 'blur' }
        ],
        parameterValue: [
          { required: true, message: '请填写商品参数值', trigger: 'blur' },
          { type: 'number', message: '参数值必须是数字' }
        ],
        descriptor: [
          { required: true, message: '请填写商品描述', trigger: 'blur' }
        ],
        logisticsId: [
          { required: true, message: '请选择物流模板', trigger: 'blur' }
        ],
        goodsStatus: [
          { required: true, message: '请选择商品状态', trigger: 'blur' }
        ]
      },
      // 商品编码
      goodsCode: undefined,
      // 表单参数
      form: {  },
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 商品库存状态
      goodsInventoryStatus: [],
      // 商品类目
      goodsCategory: [],
      // 物流下拉列表
      logisticsSelect: [],
      imageUrl: undefined,
      isMainImages: false,
      goodsImageUrl: '',
      goodsImageDialogVisible: false,
      disabled: false
    }
  },
  watch: {
    // 根据名称筛选类目树
    categoryName(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getList()
    /** 加载商品类型 select */
    this.getDicts('xy_goods_type').then(res => {
      this.goodsTypeOptions = res.data
    })
    /** 加载商品状态 select */
    this.getDicts('xy_goods_status').then(res => {
      this.goodsStatusOptions = res.data
    })
    /** 加载商品库存状态 select */
    this.getDicts('xy_goods_inventory_status').then(res => {
      this.goodsInventoryStatus = res.data
    })
    this.getCategory()
    this.getLogistics()
  },
  methods: {
    /** 获取商品列表 */
    getList() {
      this.loading = true
      listGoods(this.queryParams).then((res) => {
        this.goodsList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    /** 查询商品类目树结构 */
    getCategory() {
      categoryTreeselect().then(res => {
        this.goodsCategory = res.data
      })
    },
    /** 商品图片显示 */
    goodsMainImageUrl(row, column) {
      return process.env.VUE_APP_BASE_API + row.goodsImage
    },
    // 商品类型字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.goodsTypeOptions, row.goodsType)
    },
    // 汇算折扣率
    discountRate(row, column) {
      let discountRate = (row.discountRate / 100).toFixed(2)
      return discountRate += ' %' ;
    },
    // 商品状态字典翻译
    goodsStatusFormat(row, column) {
      return this.selectDictLabel(this.goodsStatusOptions, row.goodsStatus)
    },
    // 商品库存状态字典翻译
    inventoryStatusFormat(row, column) {
      return this.selectDictLabel(this.goodsInventoryStatus, row.inventoryStatus)
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.categoryId = data.id
      this.getList()
    },
    /** 查询物流 select */
    getLogistics() {
      getLogisticsSelect().then(res => {
        this.logisticsSelect = res.data
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.goodsId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 添加商品按钮 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加商品'
      /** 获取商品编号 */
      getGoodsCode().then(res => {
        this.goodsCode = res.data
        this.form.goodsCode = res.data
      })
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.goodsId = row.goodsId || this.ids
      const goodsId = this.goodsId
      getGoods(goodsId).then((res) => {
        this.form = res.data
        this.open = true
        this.goodsCode = res.data.goodsCode
        this.title = '修改用户'
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const goodsId = row.goodsId || this.ids
      this.$confirm(
        '是否确认删除用户编号为"' + goodsId + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(function() {
        return delGoods(goodsId)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm("是否确认导出所有商品数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function() {
        return exportGoods(queryParams)
      }).then((res) => {
        this.download(res.msg)
      }).catch(function() {})
    },
    handleRemove(file) {
      console.log(file);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handlePreview(file) {
      console.log(file)
      this.goodsImageDialogVisible = true
      this.goodsImageUrl = file.url
    },
    /** 上传单击按钮 */
    submitUpload() {
      this.$refs.goodsImage.submit()
    },
    /** 商品图片上传成功 */
    handleFileSuccess(res, file) {
      console.log(res)
      this.getList()
      this.$refs.goodsImage.clearFiles()
      this.uploadGoodsImage.isMainImages = false
    },
    /** 条纹码上传成功 */
    handleAvatarSuccess(res, file) {
      console.log(res)
      this.imageUrl = URL.createObjectURL(file.raw);
      this.$refs.goodsBarCodeImage.clearFiles()
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.goodsId != undefined) {  // 修改
            updateGoods(this.form).then((res) => {
              if (res.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {  // 新增
            addGoods(this.form).then(res => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList()
            })
          }
        }
      })
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 清空表单 */
    reset() {
      if (this.$refs.menu != undefined) {
        this.$refs.menu.setCheckedKeys([])
      }
      this.form = {
        goodsId: undefined,
        goodsCode: undefined,
        goodsName: undefined,
        categoryId: undefined,
        goodsBrand: undefined,
        goodsModel: undefined,
        weight: undefined,
        goodsUnit: undefined,
        inventory: undefined,
        inventoryStatus: undefined,
        referencePrice: undefined,
        discountRate: undefined,
        preferentialPrice: undefined,
        goodsParameter: undefined,
        parameterValue: 0,
        logisticsId: undefined,
        goodsStatus: undefined
      }
      this.uploadGoodsImage.isMainImages = false
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.goodsName = undefined
      this.queryParams.goodsStatus = undefined
      this.queryParams.pageNum = undefined
      this.queryParams.pageSize = 10
      this.queryParams.goodsType = undefined
      this.queryParams.categoryId = undefined
      this.handleQuery()
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
td div p {
  margin: 6px;
}
</style>
