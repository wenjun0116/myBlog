<template>
  <div>
    <svg-icon icon-class="question" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'DemoDoc',
  data() {
    return {
      url: 'https://gitee.com/zhangtianhy/e-mall/blob/master/%E6%96%87%E6%A1%A3.md'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
