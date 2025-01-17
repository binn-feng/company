export default function (Vue) {
  Vue.directive('cut-name', {
    inserted:function(el,bindings) {
      if(bindings.value){
        el.textContent =  el.textContent.length > 14 ? el.textContent.substring(0, bindings.value) + "..." : el.textContent;
      }else{
        el.textContent =  el.textContent.length > 14 ? el.textContent.substring(0, 14) + "..." : el.textContent; 
      }
    }
  })
}