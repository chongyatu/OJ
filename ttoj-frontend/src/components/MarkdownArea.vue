<template>
  <div>
    <mavon-editor v-model="content"
                  :subfield="subfield"
                  :defaultOpen="defaultOpen"
                  :toolbarsFlag="toolbarsFlag"
                  :editable="editable"
                  :scrollStyle="scrollStyle"
                  :boxShadow="boxShadow"
                  :ishljs="true"
                  ref="md"
                  :previewBackground="bgc"
                  style="min-height: 0px;min-width: 0px;width: 100%"
                  fontSize="fontSize"
    />
  </div>
</template>

<script>
export default {
  name: "MarkdownArea",
  props: {
    content: {
      type: String,
      default: ''
    },
    bgc:{
      type: String,
      default: '#fff'
    },
    fontSize:{
      type: String,
      default: '14px'
    }
  },
  data() {
    return {
      subfield: false,// 单双栏模式
      defaultOpen: 'preview',//edit： 默认展示编辑区域 ， preview： 默认展示预览区域
      editable: false,
      toolbarsFlag: false,
      scrollStyle: false,
      boxShadow: false,//边框
    }
  }
}
</script>

<style scoped>

</style>
