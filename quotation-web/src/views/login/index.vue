<template>
    <div class="login-container">
        <div class="title-logo"></div>
        <a-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
            label-position="left">
            <div class="login-box">
                <div class="title-container">
                    <h3 class="title">Hello!</h3>
                    <h3 class="sub-title">欢迎登录报价系统</h3>
                </div>
                <a-form-item name="username">
                    <div class="input-container">
                        <span class="svg-container">
                            <UserOutlined />
                        </span>
                        <a-input v-model:value="loginForm.username" placeholder="请输入用户名" name="username" />
                    </div>

                </a-form-item>

                <a-form-item name="password">
                    <div class="input-container">
                        <span class="svg-container">
                            <LockOutlined />
                        </span>
                        <a-input-password v-model:value="loginForm.password" :type="passwordType" placeholder="请输入密码"
                            name="password" @keyup.enter="handleLogin" />
                        <span class="show-pwd" @click="showPwd">
                            <eye-outlined v-if="passwordType === 'password'" />
                            <eye-invisible-outlined v-else />
                        </span>
                    </div>

                </a-form-item>

                <a-button :loading="loginLoading" type="primary" class="loginBtn" @click="handleLogin">登录</a-button>
            </div>
        </a-form>
    </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';

const router = useRouter();
const store = useStore();

const loginForm = ref({
    username: '',
    password: '',
});

const loginRules = {
    username: [{ required: true, message: '请输入用户名' }],
    password: [{ required: true, message: '请输入密码' }],
};

const loginLoading = ref(false);
const passwordType = ref('password');
const loginError = ref('');

const showPwd = () => {
    passwordType.value = passwordType.value === 'password' ? 'text' : 'password';
};

const handleLogin = async () => {
    loginLoading.value = true;
    loginError.value = '';

    try {
        await store.dispatch('login/login', {
            username: loginForm.value.username,
            password: loginForm.value.password,
        });
        await store.dispatch('login/fetchUserInfo'); // 登录后获取用户信息
        // 登录成功后跳转到首页
        nextTick(() => {
            router.push({ name: 'Dashboard' });
        });
    } catch (error) {
        loginError.value = '登录失败，请检查用户名和密码';
    } finally {
        loginLoading.value = false;
    }
};
</script>
  
<style scoped>
.login-container {
    height: 100%;
    background: url('~@/assets/images_bg.png');
    background-size: cover;
    background-color: #283443;
    overflow: hidden;
}

.title-logo {
    background: url('~@/assets/images_logo.png') no-repeat;
    height: 50px;
    margin-top: 20px;
    margin-left: 20px;
}

.login-box {
    position: relative;
    left: 98px;
    top: 89px;
    width: 350px;
    height: 100%;
}
.input-container {  
    display: flex; 
    align-items: center;
    margin-bottom: 15px; 
}  

.svg-container {  
    padding: 0 5px; 
    color: #889aa4;  
    font-size: 15px; 
} 

.login-form {
    background: url('~@/assets/images_login.png') no-repeat center right;
    background-color: #FFFFFF;
    position: absolute;
    width: 768px;
    height: 448px;
    max-width: 100%;
    padding: 0;
    top: 50%;
    left: 50%;
    margin-top: -224px;
    margin-left: -384px;
    overflow: hidden;
    border-radius: 15px;
}

.svg-container {
    padding: 6px 5px 6px 15px;
    color: #889aa4;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
}

.title-container {
    position: relative;
}

.title {
    font-size: 28px;
    color: #3D3D3D;
    margin: 0px 0px 15px;
    text-align: left;
    font-weight: bold;
}

.sub-title {
    font-size: 18px;
    color: #889aa4;
    margin: 0px auto 40px auto;
    text-align: left;
    font-weight: regular;
}

.show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: #889aa4;
    cursor: pointer;
    user-select: none;
}

@media (max-width: 768px) {
    .login-container {
        height: 100%;
        background: url('~@/assets/images_bg.png');
        background-size: cover;
        background-color: #283443;
        overflow: hidden;
    }

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

    .loginBtn {
        width: 100%;
    }
}
</style>