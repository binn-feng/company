<template>
  <div v-if="isExternal" :style="styleExternalIcon" class="svg-external-icon svg-icon" v-bind="listeners" />
  <svg v-else :class="svgClass" aria-hidden="true" v-bind="listeners">
    <use :xlink:href="iconName" />
  </svg>
</template>

<script>
import { defineComponent, computed, toRefs } from 'vue';

export default defineComponent({
  name: 'SvgIcon',
  props: {
    iconClass: {
      type: String,
      required: true
    },
    className: {
      type: String,
      default: ''
    }
  },
  setup(props, { attrs }) {
    const { iconClass, className } = toRefs(props);

    // 计算是否为外部图标
    const isExternal = computed(() => /^(https?:|mailto:|tel:)/.test(iconClass.value));

    // 计算 iconName
    const iconName = computed(() => `#icon-${iconClass.value}`);

    // 计算 svgClass
    const svgClass = computed(() => {
      return className.value ? `svg-icon ${className.value}` : 'svg-icon';
    });

    // 计算 styleExternalIcon
    const styleExternalIcon = computed(() => ({
      mask: `url(${iconClass.value}) no-repeat 50% 50%`,
      '-webkit-mask': `url(${iconClass.value}) no-repeat 50% 50%`
    }));

    return {
      isExternal,
      iconName,
      svgClass,
      styleExternalIcon,
      listeners: attrs // 获取所有父组件传递的事件监听
    };
  }
});
</script>

<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}


.svg-external-icon {
  background-color: currentColor;
  mask-size: cover !important;
  display: inline-block;
}
</style>
