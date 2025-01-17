<template>
   <div class="weather-wrapper">
   <div id="he-plugin-standard"> {{weatherTxt}} </div>
   </div>
</template>

<script>
import resize from './mixins/resize'
export default {
  name: 'Weather',
  mixins: [resize],
  data(){
    return {
      weatherTxt: '天气预报加载中...'
    }
  },
  created() {
    window.WIDGET = {
    "CONFIG": {
      "layout": "2",
      "width": 302,
      "height": 380,
      "background": "2",
      "dataColor": "434343",
      "language": "zh",
      "key": "f787dead634241248b586dd1aa5b735e"
    }
  }
  const self = this;
  this.loadJavaScript(
    'https://widget.qweather.net/standard/static/js/he-standard-common.js?v=2.0',
     null, 
     function() {
       self.weatherTxt = '无法连接外网天气预报，请检查您的网络是否可以连接外网，谢谢';
     }
  );
},
  mounted() {
    this.$nextTick(() => {
      const intervalId = setInterval(() => {
        const plugin = document.getElementById('he-plugin-standard');
          if (plugin) {
            const oldWidth = plugin.style['width'];
            if (oldWidth == '100%') {
              clearInterval(intervalId);
            } else {
              plugin.style['width'] = '100%';
            }
           
          }
      }, 1000)
    })
  },
  methods: {
    loadJavaScript(url, obj, errCallback) {
        let script = document.createElement('script');
        script.onreadystatechange = script.onerror = function() {
            if ( !this.readyState || ( (this.readyState ==='loaded' || this.readyState === 'complete') && !window[obj]) ) {
                errCallback();
            }
        };
        script.type = 'text/javascript';
        script.src = url;
        document.getElementsByTagName('head')[0].appendChild(script);
    }
  }
}
</script>

<style scoped>
.weather-wrapper {
  overflow: hidden;
  height: 380px;
  text-align: center;
  font-weight: bolder;
  font-size: 20px;
}
</style>
