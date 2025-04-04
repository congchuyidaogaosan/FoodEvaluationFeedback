const { evaluationApi } = require('../../utils/api');

Page({
  data: {
    evaluations: [],
    loading: true
  },

  onLoad: function() {
    this.getMyEvaluations();
  },

  // 获取我的评价列表
  getMyEvaluations: async function() {
    try {
      const res = await evaluationApi.getMyEvaluations();
      this.setData({ 
        evaluations: res.data,
        loading: false
      });
    } catch (error) {
      wx.showToast({
        title: '获取评价列表失败',
        icon: 'none'
      });
    }
  },

  // 跳转到菜品详情
  navigateToDishDetail: function(e) {
    const dishId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/evaluation/evaluation?id=${dishId}`
    });
  }
}); 