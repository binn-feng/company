<template>
  <div :class="['content_title', { 'border-class': needBorder }]">
    <div class="title">
      <span class="content_text">
        <slot name="title" />
      </span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    needBorder: {
      type: Boolean,
      default: true
    }
  }
}
</script>

<style scoped lang="scss">
.content_title {
  height: 48px;
  background-color: #fff;
  font-size: 17px;
  font-weight: 600;
  padding: 2px 0 0px 1px;
  // margin-left: 16px;
  margin-right: 16px;
  // line-height: 31px;
  .title {
    // margin-left: 55px;
    .content-text {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 50%;
      margin-left: 50px;
      // margin-top: 20px;
    }
  }
}
.border-class {
  border-bottom: 1px solid #eee;
}
</style>
