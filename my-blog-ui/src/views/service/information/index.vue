<template>
  <el-container class="app-container">
    <el-header>基本信息</el-header>
    <el-main>
      <el-form :model="enterpriseFrom" ref="enterpriseFrom" :rules="rules" inline>
        <el-form-item label="企业名称" prop="enterpriseName">
          <el-input v-model="enterpriseFrom.enterpriseName" placeholder="请输入企业名称" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item label="企业地址">
          <div class="block">
            <el-form-item prop="provinceId" style="float:left;">
              <el-select v-model="enterpriseFrom.provinceId" placeholder="请选择"
                  @change="setprefectureLevelCity">
                <el-option
                  v-for="item in province"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="prefectureLevelCityId">
              <el-select v-model="enterpriseFrom.prefectureLevelCityId" placeholder="请选择"
              @change="setcity">
                <el-option
                  v-for="item in prefectureLevelCity"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="cityId">
              <el-select v-model="enterpriseFrom.cityId" placeholder="请选择">
                <el-option
                  v-for="item in city"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <span style="margin-left:20px;"></span>
          </div>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailedAddress">
            <el-input
              type="text"
              placeholder="请输入详细地址"
              style="width: 400px;"
              maxlength='40'
              v-model="enterpriseFrom.detailedAddress"
              show-word-limit
              ></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="linkman">
          <el-input
            v-model="enterpriseFrom.linkman"
            placeholder="请输入联系人 "
            style="width: 300px;"
            >
          </el-input>
        </el-form-item>
        <el-form-item label="联系人电话" prop="phone">
          <el-input
            v-model="enterpriseFrom.phone"
            placeholder="请输入联系人电话"
            style="width: 300px;"
          >
          </el-input>
        </el-form-item>
        <div style="text-align:center;">
          <el-form-item>
              <el-button type="primary" size="mini" @click="submitForm">保存</el-button>
              <el-button size="mini" @click="reset">重置</el-button>
          </el-form-item>
        </div>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>

import {
  chinaAreaCitySelect,
  selectChildren,
} from '@/api/serivce/china'

import {
  addEnterprise,
} from '@/api/serivce/enterprise'

export default {
  name: "Information",
  data(){
    return {
      //各个省份
      province: undefined,
      //省份对应的地级市
      prefectureLevelCity: undefined,
      //地级市对应的城市
      city: undefined,
      enterpriseFrom: {
        //企业名称
        enterpriseName: undefined,
        //企业所属省份
        provinceId: undefined,
        //企业所属地级市
        prefectureLevelCityId: undefined,
        //企业所属区县
        cityId: undefined,
        //企业详细地址
        detailedAddress: undefined,
        //企业联系人
        linkman: undefined,
        //联系人电话
        phone: undefined,
      },
      //表单验证
      rules: {
        enterpriseName: [
          { required: true, message: "企业名称不能为空", trigger: "blur" },
        ],
        provinceId: [
          { required: true, message: "请选择企业所在省份", trigger: "blur" },
        ],
        prefectureLevelCityId: [
          { required: true, message: "请选择企业所在地级市", trigger: "blur" },
        ],
        cityId: [
          { required: true, message: "请选择企业所在城市", trigger: "blur" },
        ],
        detailedAddress: [
          { required: true, message: "详细地址不能为空", trigger: "blur" },
        ],
        linkman: [
          { required: true, message: "企业联系人不能为空", trigger: "blur" },
        ],
        phone: [
          { required: true, message: "电话号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ]
      }
    }
  },
  created() {
      /** 加载各个省级城市 */
      this.getprovince()
    },
  methods: {
    /** 获取所有的省级城市 */
    getprovince() {
      chinaAreaCitySelect().then( res => {
        this.province = res.data
      })
    },
    /** 选择省份 获取该省份管辖的地级城市 */
    setprefectureLevelCity(val) {
      selectChildren(val).then(res => {
        this.prefectureLevelCity = res.data
        this.enterpriseFrom.prefectureLevelCityId = this.prefectureLevelCity[0].id
        selectChildren(this.enterpriseFrom.prefectureLevelCityId).then(res => {
          this.city = res.data
          this.enterpriseFrom.cityId = (this.city.length > 0 ? this.city[0].id : undefined)
        })
      })
    },
    /** 选择地级市 获取该地级市管辖的城市 */
    setcity(val) {
      selectChildren(val).then(res => {
        this.city = res.data
        this.enterpriseFrom.cityId = (this.city.length > 0 ? this.city[0].id : undefined)
      })
    },
    //新增企业信息
    submitForm() {
      this.$refs['enterpriseFrom'].validate((valid) => {
        if(valid) {
          addEnterprise(this.enterpriseFrom).then( (res) => {
            alert("新增成功")
            this.reset()
          })
        }
      })
    },
    //重置表单数据
    reset() {
      this.enterpriseFrom.enterpriseName = undefined
      this.enterpriseFrom.provinceId = undefined
      this.enterpriseFrom.prefectureLevelCityId = undefined
      this.enterpriseFrom.cityId = undefined
      this.enterpriseFrom.detailedAddress = undefined
      this.enterpriseFrom.linkman = undefined
      this.enterpriseFrom.phone = undefined
    }
  }
}
</script>

<style scoped>

</style>
