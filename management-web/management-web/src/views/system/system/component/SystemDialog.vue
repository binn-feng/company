<template>  
    <el-dialog :visible.sync="visible" title="系统管理">  
      <el-form :model="currentSystem">  
        <el-form-item label="系统名称" prop="system" :rules="[{ required: true, message: '请输入系统名称', trigger: 'blur' }]">  
          <el-input v-model="currentSystem.system"></el-input>  
        </el-form-item>  
        <el-form-item label="类型" prop="type" :rules="[{ required: true, message: '请输入类型', trigger: 'blur' }]">  
          <el-input v-model="currentSystem.type"></el-input>  
        </el-form-item>  
        <el-form-item label="Client ID" prop="clientId" :rules="[{ required: true, message: '请输入Client ID', trigger: 'blur' }]">  
          <el-input v-model="currentSystem.clientId"></el-input>  
        </el-form-item>  
        <el-form-item label="Redirect URL" prop="redirectUrl" :rules="[{ required: true, message: '请输入Redirect URL', trigger: 'blur' }]">  
          <el-input v-model="currentSystem.redirectUrl"></el-input>  
        </el-form-item>  
        <el-form-item label="封面图片">  
          <el-upload  
            class="upload-demo"  
            action=""  
            :show-file-list="false"  
            :on-change="handleImageChange"  
          >  
            <el-button size="small" type="primary">{{ isEditing ? '更换封面图片' : '添加封面图片' }} </el-button>  
          </el-upload>  
          <img v-if="currentSystem.coverImage" :src="currentSystem.coverImage" alt="封面" class="cover-image-preview" />  
        </el-form-item>  
      </el-form>  
      <span slot="footer" class="dialog-footer">  
        <el-button @click="visible = false">取 消</el-button>  
        <el-button type="primary" @click="save">确 定</el-button>  
      </span>  
    </el-dialog>  
  </template>  
  
  <script>  
  export default {  
    props: {  
      visible: {  
        type: Boolean,  
        required: true,  
      },  
      isEditing: {  
        type: Boolean,  
        default: false,  
      },  
      currentSystem: {  
        type: Object,  
        required: true,  
      },  
    },  
    methods: {  
      handleImageChange(file) {  
        this.currentSystem.coverImage = URL.createObjectURL(file.raw);  
      },  
      save() {  
        this.$emit('save', this.currentSystem);  
        this.visible = false;  
      },  
    },  
  };  
  </script>  
  
  <style scoped>  
  .cover-image-preview {  
    width: 100px;  
    height: 100px;  
    margin-top: 10px;  
  }  
  
  .upload-demo {  
    margin-bottom: 10px;  
  }  
  </style>