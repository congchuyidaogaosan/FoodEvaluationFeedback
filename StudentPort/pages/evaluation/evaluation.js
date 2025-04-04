const { evaluationApi, dishApi } = require('../../utils/api');

Page({
  data: {
    dishId: null,
    dishInfo: null,
    scores: {
      taste: 5,
      color: 5,
      quantity: 5,
      pricePerformance: 5
    },
    comment: '',
    submitting: false,
    loading: true
  },

  onLoad: function(options) {
    if (options.id) {
      this.setData({ dishId: options.id });
      this.getDishInfo();
    }
  },

  // 获取菜品信息
  getDishInfo: async function() {
    try {
      const res = await dishApi.getDishDetail(this.data.dishId);
      this.setData({ 
        dishInfo: res.data,
        loading: false
      });
    } catch (error) {
      wx.showToast({
        title: '获取菜品信息失败',
        icon: 'none'
      });
    }
  },

  // 评分变化
  onScoreChange: function(e) {
    const { type } = e.currentTarget.dataset;
    const { value } = e.detail;
    this.setData({
      [`scores.${type}`]: value
    });
  },

  // 评论内容变化
  onCommentChange: function(e) {
    this.setData({
      comment: e.detail.value
    });
  },

  // 提交评价
  submitEvaluation: async function() {
    if (this.data.submitting) return;

    this.setData({ submitting: true });

    try {
      await evaluationApi.submitEvaluation({
        dishId: this.data.dishId,
        ...this.data.scores,
        comment: this.data.comment
      });

      wx.showToast({
        title: '评价成功',
        icon: 'success'
      });

      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    } catch (error) {
      wx.showToast({
        title: '评价失败',
        icon: 'none'
      });
    } finally {
      this.setData({ submitting: false });
    }
  }
}); 