webpackJsonp([1],{"1Zab":function(t,e){},"8Z9+":function(t,e){},NHnr:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i("7+uW"),n={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",{attrs:{id:"app"}},[e("router-view")],1)},staticRenderFns:[]};var s=i("VU/8")({name:"App"},n,!1,function(t){i("8Z9+")},null,null).exports,o=i("/ocq"),l=i("//Fk"),c=i.n(l),r=i("mtWM"),u=i.n(r),d=i("Au9i"),m=i.n(d),v={data:function(){return{quality:1,imgFile:{},resultShow:!1,resultUrl:""}},methods:{chooseQuality:function(t){this.quality=t},addImg:function(){this.$refs.file.click()},selectImg:function(){var t=this,e=this.$refs.file.files[0];console.log(e);var i={Id:1,name:e.name,size:e.size,file:e},a=new FileReader;console.log(a),a.onloadend=function(e){t.getBase64(e.target.result).then(function(e){t.$set(i,"src",e)})},a.readAsDataURL(e),this.imgFile=i,console.log(this.imgFile)},getBase64:function(t){var e=new Image,i="";return e.src=t,new c.a(function(t,a){e.onload=function(){var a=document.createElement("canvas"),n=e.width,s=e.height,o=n/s,l=s,c=n;(n>1e3||s>1e3)&&(n>s?(c=1e3,l=1e3/o):(c=1e3*o,l=1e3)),a.width=c,a.height=l,a.getContext("2d").drawImage(e,0,0,c,l),i=a.toDataURL("image/jpeg",.5),t(i)}})},uploadImg:function(){var t=this;if(void 0!=this.imgFile.Id){d.Indicator.open();var e=this.imgFile.file,i=this.quality,a=this.$refs.imgText.value,n=new FormData;n.append("img",e),n.append("quality",i),n.append("text",a),u.a.post("http://imgword.codeplus.club/UserController?action=upload",n,{headers:{"Content-Type":"multipart/form-data"}}).then(function(e){console.log(e),d.Indicator.close(),"上传成功"==e.data.message?(t.resultUrl=e.data.targetFile,t.resultShow=!0):Object(d.MessageBox)("提示",e.data.message)})}else Object(d.Toast)({message:"请先上传图片",position:"bottom",duration:3e3})},removeImg:function(){this.imgFile={}},cancelHandel:function(){this.imgFile={},this.resultShow=!1,this.$refs.imgText.value=""}}},f={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"index-wrapper"},[i("h2",{staticClass:"index-title"},[t._v("文字图片转换")]),t._v(" "),i("div",{directives:[{name:"show",rawName:"v-show",value:void 0==t.imgFile.Id,expression:"imgFile.Id == undefined"}],staticClass:"add-img"},[i("i",{staticClass:"iconfont icon-jia",on:{click:function(e){t.addImg()}}}),t._v(" "),i("input",{directives:[{name:"show",rawName:"v-show",value:!1,expression:"false"}],ref:"file",attrs:{type:"file",multiple:"",accept:"image/*"},on:{change:function(e){t.selectImg()}}})]),t._v(" "),i("div",{directives:[{name:"show",rawName:"v-show",value:void 0!=t.imgFile.Id,expression:"imgFile.Id != undefined"}],staticClass:"view-img"},[i("i",{staticClass:"iconfont icon-chahao",on:{click:function(e){t.removeImg()}}}),t._v(" "),i("img",{attrs:{src:t.imgFile.src,alt:""}})]),t._v(" "),i("div",{staticClass:"quality-wrapper"},[i("div",{staticClass:"quality-one",on:{click:function(e){t.chooseQuality(1)}}},[1==t.quality?i("i",{staticClass:"iconfont icon-danxuan selected"}):i("i",{staticClass:"iconfont icon-danxuan1"}),t._v("\n      标清\n    ")]),t._v(" "),i("div",{staticClass:"quality-two",on:{click:function(e){t.chooseQuality(2)}}},[2==t.quality?i("i",{staticClass:"iconfont icon-danxuan selected"}):i("i",{staticClass:"iconfont icon-danxuan1"}),t._v("\n      高清\n    ")]),t._v(" "),i("div",{staticClass:"quality-three",on:{click:function(e){t.chooseQuality(3)}}},[3==t.quality?i("i",{staticClass:"iconfont icon-danxuan selected"}):i("i",{staticClass:"iconfont icon-danxuan1"}),t._v("\n      超清\n    ")])]),t._v(" "),i("div",{staticClass:"text-wrapper"},[i("input",{ref:"imgText",staticClass:"img-text",attrs:{type:"text",placeholder:"请输入一个需要转换的字符",maxlength:"1"}})]),t._v(" "),i("button",{staticClass:"submit-btn",on:{click:function(e){t.uploadImg()}}},[t._v("开始转换")]),t._v(" "),i("transition",{attrs:{name:"result"}},[i("div",{directives:[{name:"show",rawName:"v-show",value:t.resultShow,expression:"resultShow"}],staticClass:"result-wrapper"},[i("div",{staticClass:"result-img"},[i("img",{attrs:{src:t.resultUrl,alt:""}})]),t._v(" "),i("p",{staticClass:"saveImg"},[t._v("长按保存图片到本地")]),t._v(" "),i("div",{staticClass:"result-btn-group"},[i("button",{staticClass:"cancel",on:{click:function(e){t.cancelHandel()}}},[t._v("返回")])])])])],1)},staticRenderFns:[]};var p=i("VU/8")(v,f,!1,function(t){i("1Zab")},null,null).exports;a.default.use(o.a);var g=new o.a({routes:[{path:"*",name:"Index",component:p},{path:"/",name:"Index",component:p},{path:"/index",name:"Index",component:p}]});i("d8/S");a.default.config.productionTip=!1,a.default.use(m.a),new a.default({el:"#app",router:g,components:{App:s},template:"<App/>"})},"d8/S":function(t,e){}},["NHnr"]);
//# sourceMappingURL=app.ee64aa23c1c5b4e922e5.js.map