const { feedbackApi } = require('../../utils/api');
const app = getApp();

Page({
  data: {
    feedbackList: [],
    feedbackType: 'suggestion',
    feedbackContent: '',
    loading: true,
    submitting: false
  },

  onLoad: function() {
    if (!app.checkLogin()) return;
    this.getFeedbackList();
  },

  onShow: function() {
    if (!app.checkLogin()) return;
    this.getFeedbackList();
  },

  // 获取反馈列表
  async getFeedbackList() {
    try {
      const res = await feedbackApi.getFeedbackList();
      this.setData({
        feedbackList: res.data,
        loading: false
      });
    } catch (error) {
      console.error('获取反馈列表失败:', error);
      this.setData({ loading: false });
    }
  },

  // 选择反馈类型
  onTypeChange: function(e) {
    this.setData({
      feedbackType: e.detail.value
    });
  },

  // 输入反馈内容
  onContentInput: function(e) {
    this.setData({
      feedbackContent: e.detail.value
    });
  },

  // 提交反馈
  async submitFeedback() {
    if (!app.checkLogin()) return;

    if (!this.data.feedbackContent.trim()) {
      wx.showToast({
        title: '请输入反馈内容',
        icon: 'none'
      });
      return;
    }

    this.setData({ submitting: true });

    try {
      await feedbackApi.submitFeedback({
        type: this.data.feedbackType,
        content: this.data.feedbackContent
      });

      wx.showToast({
        title: '提交成功',
        icon: 'success'
      });

      this.setData({
        feedbackContent: '',
        feedbackType: 'suggestion'
      });

      this.getFeedbackList();
    } catch (error) {
      console.error('提交反馈失败:', error);
      wx.showToast({
        title: '提交失败，请重试',
        icon: 'none'
      });
    } finally {
      this.setData({ submitting: false });
    }
  }
}); 