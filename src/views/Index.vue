<template>
  <div class="index-wrapper">
    <h2 class="index-title">文字图片转换</h2>
    <div class="add-img" v-show="imgFile.Id == undefined">
      <i class="iconfont icon-jia" @click="addImg()"></i>
      <input type="file" @change="selectImg()" multiple accept="image/*" ref="file" v-show="false">
    </div>
    <div class="view-img" v-show="imgFile.Id != undefined">
      <i class="iconfont icon-chahao" @click="removeImg()"></i>
      <img :src="imgFile.src" alt="">
    </div>
    <div class="quality-wrapper">
      <div class="quality-one" @click="chooseQuality(1)">
        <i class="iconfont icon-danxuan selected" v-if="quality == 1"></i>
        <i class="iconfont icon-danxuan1" v-else></i>
        标清
      </div>
      <div class="quality-two" @click="chooseQuality(2)">
        <i class="iconfont icon-danxuan selected" v-if="quality == 2"></i>
        <i class="iconfont icon-danxuan1" v-else></i>
        高清
      </div>
      <div class="quality-three" @click="chooseQuality(3)">
        <i class="iconfont icon-danxuan selected" v-if="quality == 3"></i>
        <i class="iconfont icon-danxuan1" v-else></i>
        超清
      </div>
    </div>
    <div class="text-wrapper">
      <input class="img-text" type="text" placeholder="请输入一个需要转换的字符"  maxlength="1" ref="imgText">
    </div>
    <button class="submit-btn" @click="uploadImg()">开始转换</button>
    <transition name="result">
      <div class="result-wrapper" v-show="resultShow">
        <div class="result-img">
          <img :src="resultUrl" alt="">
        </div>
        <p class="saveImg">长按保存图片到本地</p>
        <div class="result-btn-group">
          <button class="cancel" @click="cancelHandel()">返回</button>
        </div>
      </div>
    </transition>
    <p class="remarkCode">陕ICP备18020289号-1</p>
  </div>
</template>
<script>
import axios from 'axios';
import { Toast } from 'mint-ui';
import { Indicator } from 'mint-ui';
import { MessageBox } from 'mint-ui';
export default {
  data(){
    return{
      quality: 1,
      imgFile: {},
      resultShow: false,
      resultUrl: ''
    }
  },
  methods: {
    chooseQuality(index){
      this.quality = index;
    },
    addImg(){
      this.$refs.file.click();
    },
    selectImg(){
      let file = this.$refs.file.files[0];
      console.log(file);
      //将图片文件转成Base64
      let fileItem = {
        Id: 1,
        name: file.name,
        size: file.size,
        file: file
      };
      let reader = new FileReader();
      console.log(reader);
      reader.onloadend = (e) => {
        this.getBase64(e.target.result).then((url) => {
            this.$set(fileItem, 'src', url)
        })
      }
      reader.readAsDataURL(file);
      this.imgFile = fileItem;
      console.log(this.imgFile);  
    },
    getBase64(url) {
      let self = this;
      let Img = new Image(),
      dataURL = "";
      Img.src = url;
      let p = new Promise(function(resolve, reject) {
        Img.onload = function() {
          let canvas = document.createElement("canvas"), 
            width = Img.width,
            height = Img.height;
          let ratio = width / height,
            maxLength = 1000,
            newHeight = height,
            newWidth = width;
          if (width > maxLength || height > maxLength) {
            if (width > height) {
              newWidth = maxLength;
              newHeight = maxLength / ratio;
            } else {
              newWidth = maxLength * ratio;
              newHeight = maxLength;
            }
          }
          canvas.width = newWidth;
          canvas.height = newHeight;
          canvas.getContext("2d").drawImage(Img, 0, 0, newWidth, newHeight);
          dataURL = canvas.toDataURL("image/jpeg", 0.5);
          resolve(dataURL);
        };
      });
      return p;
    },
    uploadImg(){
      if(this.imgFile.Id != undefined){
        Indicator.open();
        let img = this.imgFile.file;
        let quality = this.quality;
        let text = this.$refs.imgText.value;
        //定义formdata
        let formData=new FormData();
        formData.append('img',img);
        formData.append('quality',quality);
        formData.append('text',text);

        axios.post('http://imgword.codeplus.club/UserController?action=upload', formData, {
          headers: {'Content-Type': 'multipart/form-data'}
        }).then(res => {
          console.log(res);
          Indicator.close();
          if(res.data.message == "上传成功"){
            this.resultUrl = res.data.targetFile;
            this.resultShow = true;
          } else{
            MessageBox('提示', res.data.message);
          }
        })
      } else{
        Toast({
          message: '请先上传图片',
          position: 'bottom',
          duration: 3000
        });
      }
    },
    removeImg(){
      this.$refs.file.value = "";
      this.imgFile = {};
    },
    cancelHandel(){
      this.$refs.file.value = "";
      this.imgFile = {};
      this.resultShow = false;
      this.$refs.imgText.value = '';
    }
  }
}
</script>
<style>
  @import '../assets/fonts/iconfont.css';
  .index-wrapper{
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    padding: 14px;
    overflow: hidden;
    background: url('../assets/images/bgImg.jpeg') no-repeat center;
    background-size: cover;
  }
  .index-title{
    font-size: 18px;
    text-align: center;
    color: #353535;
    text-shadow: 2px 3px 4px #9e9e9e;
  }
  .add-img{
    width: 84px;
    height: 84px;
    border: 2px dashed #585858;
    border-radius: 4px;
    margin: 40px auto;
    text-align: center;
  }
  .add-img i{
    display: block;
    margin: 16px auto;
    font-size: 48px;
    color: #585858;
  }
  .quality-wrapper{
    display: flex;
    padding: 0 20px;
  }
  .quality-wrapper div{
    flex: 1;
    font-size: 14px;
    color: #292929;
    text-align: center;
  }
  .quality-wrapper div i{
    margin-right: 2px;
  }
  .selected{
    color: #00beff !important;
    font-size: 16px !important;
  }
  .img-text{
    display: block;
    width: 210px;
    height: 32px;
    line-height: 32px;
    border-radius: 4px;
    outline: none;
    border: 1px solid #f1f1f1;
    margin: 20px auto;
    text-align: center;
    font-size: 14px;
    background-color: rgba(255, 255, 255, .6);
  }
  .submit-btn{
    display: block;
    width: 240px;
    height: 38px;
    background-color: #2a92ff;
    color: #fff;
    font-size: 16px;
    margin: 48px auto 10px;
    border-radius: 4px;
    outline: none;
    border: none;
  }
  .view-img{
    position: relative;
    width: 128px;
    height: 128px;
    overflow: hidden;
    margin: 20px auto;
  }
  .view-img i{
    position: absolute;
    top: 4px;
    right: 4px;
    font-size: 24px;
    color: #da0000;
  }
  .view-img img{
    display: block;
    margin: 0 auto;
    width: 128px;
    height: 128px;
  }
  .result-wrapper{
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    padding: 14px;
    overflow: hidden;
    z-index: 99;
    background-color: rgba(0, 0, 0, .8);
    display: flex;
    flex-direction: column;
  }
  .result-img{
    flex: 1;
    overflow: hidden;
  }
  .result-img img{
    display: block;
    width: 78%;
    margin: 40px auto 20px;
    box-shadow: 0 0 8px #949494;
  }
  .saveImg{
    font-size: 14px;
    color: #fff;
    text-align: center;
  }
  .result-btn-group{
    padding: 6px 0 34px;
    text-align: center
  }
  .cancel{
    width: 106px;
    height: 40px;
    font-size: 16px;
    border: none;
    border-radius: 4px;
    outline: none;
    background-color: #fff;
  }
  .remarkCode{
    width: 100%;
    position: absolute;
    bottom: 0;
    font-size: 12px;
    color: #7575756e;
    text-align: center;
  }
  /* 过渡动画 */
  .result-enter-active, .result-leave-active{
    transition: all 0.5s;
  }
  .result-enter, .result-leave-to{
    transform: scale(0);
  }
</style>