<template>
  <div class="login-container">
    <div class="title-logo"></div>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <div class="login-box">
        <div class="title-container">
          <h3 class="title">Hello!</h3>
          <h3 class="sub-title">欢迎登录北光管理系统</h3>
        </div>
        <el-form-item prop="username">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input ref="username" v-model="loginForm.username" placeholder="请输入用户名" name="username" type="text" tabindex="1" auto-complete="on" />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType" placeholder="请输入密码" name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>

        <el-button :loading="loginloading" type="primary" class="loginBtn" @click.native.prevent="handleLogin">登录</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }]
      },
      loginloading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      this.passwordType = this.passwordType === 'password' ? '' : 'password'
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    async handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loginloading = true
          this.$store.dispatch('Login', this.loginForm).then(() => {
            this.loginloading = false
            this.$message.success('登录成功！')
            this.$router.push({ path: this.redirect || '/' })
          }).catch(() => {
            this.loginloading = false
          })
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-box {
    position: relative;
    left: 98px;
    top: 89px;
    width: 350px;
    height: 100%;
  }

  .login-form {
    background: url('~@/assets/images_login.png') no-repeat center right;
    background-color: #FFFFFF;
    position: absolute;
    width: 960px;
    height: 560px;
    max-width: 100%;
    padding: 0;
    top: 50%;
    left: 50%;
    margin-top: -260px;
    margin-left: -480px;
    overflow: hidden;
    border-radius: 15px;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 28px;
      color: #3D3D3D;
      margin: 0px 0px 15px;
      text-align: left;
      font-weight: bold;
    }

    .sub-title {
      font-size: 18px;
      color: $dark_gray;
      margin: 0px auto 40px auto;
      text-align: left;
      font-weight: regular;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}

@media (max-width:768px) {
  .login-container {
    .login-form {
      background: url('~@/assets/trans.png') no-repeat center right;
      background-color: #FFFFFF;
      position: absolute;
      display: inline-block;
      width: 95%;
      height: 460px;
      max-width: 100%;
      padding: 0;
      top: 50%;
      left: auto;
      margin-top: -210px;
      margin-left: 10px;
      overflow: hidden;
      border-radius: 15px;
    }

    .login-box {
      position: relative;
      margin: 0px auto;
      left: 20px;
      top: 20px;
      width: 350px;
      height: 100%;
    }

    .loginarea {
      width: 80%;
    }

    .el-form-item {
      width: 90%;
    }

    .loginBtn {
      width: 90%;
    }
  }
}
</style>
<style lang="scss">
.login-container .loginarea {
  width: 315px;
}

.login-container .loginsarea .el-input {
  display: inline-block;
  height: 47px;
  width: 100%;
}

.login-container .el-input input {
  color: #000 !important;
  caret-color: #000 !important;
}

.login-container .loginBtn {
  width: 100%;
  margin-bottom: 30px;
}

// .login-container .el-popper{
//  min-width: 380px !important;
// }
</style>
