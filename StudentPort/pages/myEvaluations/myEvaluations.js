const { evaluationApi } = require('../../utils/api');

Page({
  data: {
    evaluations: [],
    loading: true
  },

  onLoad: function () {
    this.getMyEvaluations();
  },

  // 获取我的评价列表
  getMyEvaluations: async function () {
    let userInfo = wx.getStorageSync('userInfo');
    console.log('当前用户信息:', userInfo);
    
    // 检查用户是否已登录
    if (!userInfo || !userInfo.studentId) {
      console.log('用户未登录或studentId不存在');
      this.setData({
        loading: false,
        evaluations: []
      });
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }

    try {
      console.log('请求参数:', { studentId: userInfo.studentId });
      const res = await evaluationApi.getMyEvaluations();
      console.log('获取评价列表结果:', res);
      
      if (res.code === 200 && res.data) {
        this.setData({
          evaluations: res.data,
          loading: false
        });
      } else {
        this.setData({
          evaluations: [],
          loading: false
        });
        wx.showToast({
          title: res.msg || '获取评价列表失败',
          icon: 'none'
        });
      }
    } catch (error) {
      console.error('获取评价列表错误:', error);
      this.setData({
        loading: false,
        evaluations: []
      });
      wx.showToast({
        title: '获取评价列表失败',
        icon: 'none'
      });
    }
  },

  // 跳转到菜品详情
  navigateToDishDetail: function (e) {
    const dishId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/evaluation/evaluation?id=${dishId}`
    });
  },
  
  // 下拉刷新
  onPullDownRefresh: function() {
    this.getMyEvaluations();
    wx.stopPullDownRefresh();
  }
}); 