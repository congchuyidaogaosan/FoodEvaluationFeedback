const { feedbackApi } = require('../../utils/api');
const app = getApp();

Page({
  data: {
    feedbackList: [],
    feedbackType: 'suggestion',
    content: '',
    loading: true,
    submitting: false
  },

  onLoad: function() {
    if (!app.checkLogin()) return;
    this.loadFeedbackList();
  },

  onShow: function() {
    if (!app.checkLogin()) return;
    this.loadFeedbackList();
  },

  // 加载反馈列表
  async loadFeedbackList() {
    this.setData({ loading: true });
    try {
      const res = await feedbackApi.getFeedbackList();
      if (res.code === 200) {
        this.setData({
          feedbackList: res.data || []
        });
      }
    } catch (error) {
      console.error('获取反馈列表失败:', error);
      wx.showToast({
        title: '获取反馈列表失败',
        icon: 'none'
      });
    } finally {
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
  onContentChange: function(e) {
    this.setData({
      content: e.detail.value
    });
  },

  // 提交反馈
  async submitFeedback() {
    if (!app.checkLogin()) return;

    if (!this.data.content.trim()) {
      wx.showToast({
        title: '请输入反馈内容',
        icon: 'none'
      });
      return;
    }

    this.setData({ submitting: true });

    try {
      const res = await feedbackApi.submitFeedback(this.data.content);
      if (res.code === 200) {
        wx.showToast({
          title: '提交成功',
          icon: 'success'
        });

        this.setData({
          content: ''
        });

        // 刷新反馈列表
        this.loadFeedbackList();
      } else {
        throw new Error(res.msg || '提交失败');
      }
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