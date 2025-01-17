import { Dialog, Drawer } from 'element-ui'

const DialogPatched = {
  extends: Dialog,
  data() {
    return {
      wrapperMouseDowned: false
    }
  },
  mounted() {
    this.$el.onmousedown = (e) => {
      this.wrapperMouseDowned =
        e.target.classList.contains('el-dialog__wrapper')
    }
  },
  methods: {
    handleWrapperClick() {
      if (!this.closeOnClickModal || !this.wrapperMouseDowned) return
      this.handleClose()
    }
  }
}

const DrawerPatched = {
  extends: Drawer,
  data() {
    return {
      wrapperMouseDowned: false
    }
  },
  mounted() {
    this.$el.onmousedown = (e) => {
      this.wrapperMouseDowned = e.target.classList.contains(
        'el-drawer__container'
      )
    }
  },
  methods: {
    handleWrapperClick() {
      if (this.wrapperClosable && this.wrapperMouseDowned) {
        this.closeDrawer()
      }
    }
  }
}

export default {
  install(Vue) {
    Vue.component(Dialog.name, DialogPatched)
    Vue.component(Drawer.name, DrawerPatched)
  }
}
