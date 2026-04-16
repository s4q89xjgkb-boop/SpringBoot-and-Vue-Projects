<template>
  <div class="dashboard-container">
    <div class="card welcome-card">
      <h1>欢迎使用本系统</h1>
    </div>
    
    <!-- 统计信息 -->
    <div class="stats-container">
      <div class="stats-card" style="background: #f6ffed; border-left: 4px solid #52c41a;">
        <div class="stats-icon"><el-icon><Reading /></el-icon></div>
        <div class="stats-info">
          <div class="stats-label">攻略文章总数</div>
          <div class="stats-value">{{ data.stats.totalArticles }}</div>
        </div>
      </div>
      <div class="stats-card" style="background: #fff7e6; border-left: 4px solid #faad14;">
        <div class="stats-icon"><el-icon><ChatDotRound /></el-icon></div>
        <div class="stats-info">
          <div class="stats-label">帖子总数</div>
          <div class="stats-value">{{ data.stats.totalPosts }}</div>
        </div>
      </div>
      <div class="stats-card" style="background: #fff1f0; border-left: 4px solid #f5222d;">
        <div class="stats-icon"><el-icon><User /></el-icon></div>
        <div class="stats-info">
          <div class="stats-label">用户总数</div>
          <div class="stats-value">{{ data.stats.totalUsers }}</div>
        </div>
      </div>
    </div>
    
    <div class="card notice-card">
      <div class="card-title">系统公告</div>
      <el-timeline>
        <el-timeline-item :timestamp="item.time" placement="top" v-for="item in data.noticeData" :key="item.id" class="timeline-item">
            <div class="notice-title">{{item.title}}</div>
            <p class="notice-content">{{item.content}}</p>
        </el-timeline-item>
      </el-timeline>
    </div>
    
    <div class="charts-container">
      <div class="card chart-card">
        <div class="chart-wrapper" id="pie"></div>
      </div>
      <div class="card chart-card">
        <div class="chart-wrapper" id="line"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

.card {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.welcome-card h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

/* 统计信息容器样式 */
.stats-container {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

/* 统计卡片样式 */
.stats-card {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: #f0f9ff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  flex: 1;
  min-width: 250px;
}

.stats-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

/* 统计图标样式 */
.stats-icon {
  font-size: 40px;
  color: #2c82ff;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 统计信息样式 */
.stats-info {
  flex: 1;
}

/* 统计标签样式 */
.stats-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

/* 统计数值样式 */
.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.notice-card {
  width: 100%;
  max-width: 800px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
}

.timeline-item {
  --el-timeline-item-dot-bg-color: #52c41a !important;
  margin-bottom: 16px;
}

.notice-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
}

.notice-content {
  margin: 0;
  color: #606266;
  line-height: 1.5;
}

.charts-container {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  margin-top: 20px;
}

.chart-card {
  flex: 1;
  min-width: 300px;
  height: 450px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-wrapper {
  width: 100%;
  height: 400px;
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 10px;
  }
  
  .charts-container {
    flex-direction: column;
  }
  
  .chart-card {
    width: 100%;
  }
}
</style>

<script setup>
import {reactive, onMounted} from "vue";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import * as echarts from "echarts";
import {Reading, ChatDotRound, User} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  loadNotice: [],
  noticeData: [],
  stats: {
    totalArticles: 0,
    totalPosts: 0,
    totalUsers: 0
  }
})
const loadNotice = () => {
  request.get('/notice/selectAll').then(res =>{
    if(res.code === '200'){
      data.noticeData = res.data
      if(data.noticeData.length > 4){
        data.noticeData = data.noticeData.slice(0,4)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 加载统计数据
const loadStats = () => {
  // 加载攻略文章总数
  request.get('/introduction/selectAll').then(res => {
    if (res.code === '200' || res.code === 200) {
      data.stats.totalArticles = res.data?.length || 0;
    }
  }).catch(err => {
    console.error('加载攻略文章总数失败:', err);
  });
  
  // 加载帖子总数
  request.get('/post/selectAll').then(res => {
    if (res.code === '200' || res.code === 200) {
      data.stats.totalPosts = res.data?.length || 0;
    }
  }).catch(err => {
    console.error('加载帖子总数失败:', err);
  });
  
  // 加载用户总数
  request.get('/user/selectAll').then(res => {
    if (res.code === '200' || res.code === 200) {
      data.stats.totalUsers = res.data?.length || 0;
    }
  }).catch(err => {
    console.error('加载用户总数失败:', err);
  });
}
const loadPie = () =>{
  request.get('/echarts/pie').then(res =>{
    if(res.code === '200'){}
    let chartDom = document.getElementById('pie')
    let myChart = echarts.init(chartDom)
    pieOptions.series[0].data = res.data
    myChart.setOption(pieOptions)
  })
}
const loadBar = () => {
  request.get('/echarts/Bar').then(res =>{
    if(res.code === '200'){}
    let chartDom = document.getElementById('bar')
    let myChart = echarts.init(chartDom)
    barOptions.xAxis.data = res.data.xAxis
    barOptions.series[0].data = res.data.yAxis
    myChart.setOption(pieOptions)
  })
}
const loadLine = () => {
  request.get('/echarts/line').then(res =>{
    if(res.code === '200'){}
    let chartDom = document.getElementById('line')
    let myChart = echarts.init(chartDom)
    lineOptions.xAxis.data = res.data.xAxis
    lineOptions.series[0].data = res.data.yAxis
    myChart.setOption(lineOptions)
  })
}
onMounted(() => {
  loadNotice();
  loadStats();
  loadPie();
  loadLine();
});
// 饼图
let pieOptions = {
  title: {
    text: '不同分类下用户发布游戏攻略帖子的数量', // 主标题
    subtext: '统计维度：攻略分类', // 副标题
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量占比', // 鼠标移上去显示内容
      type: 'pie',
      radius: '50%',
      center: ['50%', '60%'],
      data: []
    }
  ]
}
// 柱状图
let barOptions = {
  title: {
    text: '不同用户发布帖子数量Top5', // 主标题
    subtext: '统计维度：用户昵称', // 副标题
    left: 'center'
  },
  grid : {   // ---------------------------增加这个属性，bottom就是距离底部的距离
    top: '20%',
    bottom : '20%'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 示例数据：统计的维度（横坐标）
    name: '用户昵称',
    axisLabel: {
      show: true, // 是否显示刻度标签，默认显示
      interval: 0, // 坐标轴刻度标签的显示间隔，在类目轴中有效；默认会采用标签不重叠的策略间隔显示标签；可以设置成0强制显示所有标签；如果设置为1，表示『隔一个标签显示一个标签』，如果值为2，表示隔两个标签显示一个标签，以此类推。
      rotate: -60, // 刻度标签旋转的角度，在类目轴的类目标签显示不下的时候可以通过旋转防止标签之间重叠；旋转的角度从-90度到90度
      inside: false, // 刻度标签是否朝内，默认朝外
      margin: 6, // 刻度标签与轴线之间的距离
    },
  },
  yAxis: {
    type: 'value',
    name: '攻略数量',
  },
  tooltip: {
    trigger: 'item',
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'bar',
      itemStyle: {
        normal: {
          color: function () {
            return "#" + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16);
          }
        },
      },
    }
  ]
};
// 平滑折线图
let lineOptions = {
  title: {
    text: '最近一周每天平台用户发布的帖子数量',
    subtext: '统计维度：最近一周',
    left: 'center'
  },
  legend: {
    data: [],
    template:""
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  tooltip: {
    trigger: 'item'
  },
  xAxis: {
    name: '日期',
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    name: '攻略数量',
    type: 'value'
  },
  series: [
    {
      name: '攻略数量',
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line',
      smooth: true,
      markLine: {
        data: [{ type: 'average', name: '最近一周场攻略发布数量平均值' }]
      },
      markPoint: {
        data: [
          { type: 'max', name: '最大值' },
          { type: 'min', name: '最小值' }
        ]
      },
    },
  ]
};
</script>

