<template>
    <a-row class="user-profile" gutter={16}>
        <a-col :span="8" class="about-me-col">
            <a-card class="about-me" title="关于我" bordered>
                <div class="user-info">
                    <div>
                        <img src="../../assets/image.png" alt="用户头像" class="avatar" />
                    </div>
                    <div class="divider"></div>

                    <div class="info">
                        <div class="text-muted">
                            <span>用户名：</span>
                            <span>{{ user.userName }}</span>
                        </div>
                        <div class="text-muted">
                            <span>姓名：</span>
                            <span>{{ user.nickName }}</span>
                        </div>
                        <div class="text-muted">
                            <span>角色：</span>
                            <span>{{ user.roles[0].roleName }}</span>
                        </div>
                        <div class="text-muted">
                            <span>联系电话：</span>
                            <span v-if='user.phonenumber !== ""'>{{ user.phonenumber }}</span>
                            <span v-else>暂无</span>
                        </div>
                        <div class="text-muted">
                            <span>人员状态：</span>
                            <span :class="{ 'status': user.status === '0', 'inactive': user.status === '1' }">
                                {{ user.status === '0' ? '在职' : '离职' }}
                            </span>
                        </div>
                        <div class="text-muted">
                            <span>邮件地址：</span>
                            <span>{{ user.email }}</span>
                        </div>
                        <div class="text-muted">
                            <span>创建时间:</span>
                            <span>{{ user.createTime }}</span>
                        </div>
                        <div class="text-muted">
                            <span>更新时间:</span>
                            <span>{{ user.updateTime }}</span>
                        </div>
                    </div>
                </div>
            </a-card>
        </a-col>

        <a-col :span="16" class="settings-col">
            <a-card class="settings" title="个人设置" bordered>
                <a-tabs v-model:activeKey="activeTab" @change="handleTabChange">
                    <a-tab-pane key="settings" tab="个人设置">
                        <a-form @submit.prevent="handleUpdate">
                            <a-form-item label="姓名" :rules="[{ required: true, message: '请输入姓名' }]">
                                <a-input size="large" v-model="form.name" placeholder="请输入姓名" />
                            </a-form-item>
                            <a-form-item label="邮箱" :rules="[{ required: true, type: 'email', message: '请输入有效的邮箱' }]">
                                <a-input size="large" v-model="form.email" placeholder="请输入邮箱" />
                            </a-form-item>
                            <a-form-item label="手机号码">
                                <a-input size="large" v-model="form.phone" placeholder="请输入手机号码" />
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" html-type="submit">更新</a-button>
                            </a-form-item>
                        </a-form>
                    </a-tab-pane>
                    <a-tab-pane key="password" tab="密码修改">
                        <a-form @submit.prevent="handlePasswordChange">
                            <a-form-item label="旧密码" :rules="[{ required: true, message: '请输入旧密码' }]" label-position="top">
                                <a-input-password size="large" v-model="form.oldPassword" placeholder="请输入旧密码" />
                            </a-form-item>
                            <a-form-item label="新密码" :rules="[{ required: true, message: '请输入新密码' }]">
                                <a-input-password size="large" v-model="form.newPassword" placeholder="请输入新密码" />
                            </a-form-item>
                            <a-form-item label="确认密码" :rules="[{ required: true, message: '请确认新密码' }]">
                                <a-input-password size="large" v-model="form.confirmPassword" placeholder="请确认新密码" />
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" html-type="submit">修改密码</a-button>
                            </a-form-item>
                        </a-form>
                    </a-tab-pane>
                </a-tabs>
            </a-card>
        </a-col>
    </a-row>
</template>  

<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue';

// Placeholder user data  
// const user = ref({  
//     avatar: 'https://via.placeholder.com/100',  
//     username: 'northern_lights',  
//     name: 'Northern Lights',  
//     role: '开发者',  
//     phone: '123-456-7890',  
//     status: '在职',  
//     email: 'example@example.com',  
//     createdAt: '2023-01-01',  
//     updatedAt: '2023-10-01',  
// });  
const user = computed(() => {
    const storedUser = localStorage.getItem('user');
    return storedUser ? JSON.parse(storedUser) : null; // 将字符串转换为对象，如果没有数据则返回 null
});
const form = ref({
    name: user.value.name,
    email: user.value.email,
    phone: user.value.phone,
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
});

// Reactive property to track screen size  
const isDesktop = ref(window.innerWidth >= 768);
const activeTab = ref('settings');

// Utility function to debounce resize event handling  
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Function to update screen size  
function updateScreenSize() {
    isDesktop.value = window.innerWidth >= 768;
}

// Set up event listeners on mount  
onMounted(() => {
    window.addEventListener('resize', debounce(updateScreenSize, 100)); // Debounce with a 100ms wait  
});

// Clean up event listeners on unmount  
onBeforeUnmount(() => {
    window.removeEventListener('resize', updateScreenSize);
});

function handleTabChange(key) {
    activeTab.value = key;
}

function handleUpdate() {
    console.log('更新信息:', form.value);
}

function handlePasswordChange() {
    if (form.value.newPassword !== form.value.confirmPassword) {
        console.error('新密码与确认密码不匹配');
        return;
    }
    console.log('修改密码:', form.value);
}  
</script>  

<style scoped>  .user-profile {
      padding: 20px;
      background-color: white;
      border-top: 1px solid #f2ebeb;
  }

  .about-me-col {
      margin-bottom: 20px;
      padding: 10px;
  }

  .settings-col {
      padding: 10px;
  }

  .user-info {
      display: flex;
      flex-direction: column;
      align-items: center;
  }

  .avatar {
      width: 100px;
      height: 100px;
      /* border-radius: 50%; */
      margin-bottom: 10px;
  }

  .info {
      line-height: 1.5;
  }

  .text-muted {
      color: #777;
      margin-bottom: 20px;
  }

  .status {
      color: green;
  }

  .inactive {
      color: red;
  }

  .divider {
      width: 100%;
      height: 1px;
      background-color: #e8e8e8;
      margin: 20px 0;
  }

  @media (max-width: 767px) {
      .user-profile {
          padding: 10px;
      }

      .about-me-col,
      .settings-col {
          /* span: 24; Full width on smaller screens   */
          width: 100%;
      }

      .info {
          text-align: center;
          /* Center align on small screens */
      }
  }
</style>