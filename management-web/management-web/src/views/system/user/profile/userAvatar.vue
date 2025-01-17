<template>
  <div>
    <div class="user-profile">
      <div class="box-center">
        <pan-thumb :image='require("@/assets/image.png")' :height="'100px'" :width="'100px'" :hoverable="false">
          <div class="pan-text">{{ name }}</div>
          <!-- <span style="text-align:center">{{ name }}</span> -->
        </pan-thumb>
      </div>
      <div class="box-center">
        <div class="user-name text-center">{{ name }}</div>
        <!-- <div class="user-role text-center text-muted">{{ user.role | uppercaseFirst }}</div> -->
      </div>
    </div>
    <div class="user-bio-section-header"><svg-icon icon-class="education" /><span>个人信息</span></div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import PanThumb from '@/components/PanThumb'

export default {
  components: { PanThumb },
  data() {
    return {
      previews: {},
      resizeHandler: null
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  methods: {
  }
}
</script>

<style scoped lang="scss">
.box-center {
  margin: 0 auto;
  display: table;
}

.user-profile {
  margin-bottom: 20px;
  .user-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .user-role {
    padding-top: 10px;
    font-weight: 400;
    font-size: 14px;
  }

  .box-social {
    padding-top: 30px;

    .el-table {
      border-top: 1px solid #dfe6ec;
    }
  }

  .user-follow {
    padding-top: 20px;
  }
}

.user-bio-section-header {
  border-bottom: 1px solid #dfe6ec;
  padding-bottom: 10px;
  margin-bottom: 10px;
  font-weight: bold;
  font-size: 14px;
  padding: 15px 0;
  color: #606266;
  span {
    padding-left: 8px;
  }
}
</style>
