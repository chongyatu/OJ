<template>
  <div style="min-height: 100vh;
              height: 100%;
              width: 100%;
              background-color: #fff;
              ">
    <Header id="header"/>
    <router-view style="padding-top: 46px; height: 100vh"/>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: 'Layout',
  components: {
    Header,
  },
  onShow() {
    console.log(this.$route);
  },
}
</script>

<style>

</style>
