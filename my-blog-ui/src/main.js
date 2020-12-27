import Vue from 'vue'
import App from './App.vue'
//导入插件
import Cookies from 'js-cookie'
// css 样式重置
import 'normalize.css/normalize.css' 
import Element from 'element-ui'
import './assets/styles/element-variables.scss'
// 总体 css
import '@/assets/styles/index.scss'
// demo css
import '@/assets/styles/demo.scss'
import store from './store'
import router from './router'
import permission from './directive/permission'

import './assets/icons' // icon
import './permission' // permission control
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, download, handleTree } from '@/utils/demo'
import Pagination from '@/components/Pagination'
//自定义表格工具扩展
import RightToolbar from '@/components/RightToolbar'

// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree
Vue.config.productionTip = false

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}
// 全局挂载组件
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)

Vue.use(permission)

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
