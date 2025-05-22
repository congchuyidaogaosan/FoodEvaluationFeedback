const { dishApi, evaluationApi } = require('../../utils/api');

Page({
  data: {
    dishId: null,
    dishInfo: {},
    evaluations: [],
    loading: true
  },

  onLoad: function (options) {
    if (options.id) {
      const dishId = options.id;
      this.setData({ dishId });
      this.loadDishData(dishId);
    } else {
      wx.showToast({
        title: '参数错误',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }
  },

  // 加载菜品及评价数据
  loadDishData: async function (dishId) {
    try {
      // 显示加载中
      wx.showLoading({
        title: '加载中...',
      });

      // 获取菜品详情
      const dishRes = await dishApi.getDishDetail(dishId);
      console.log('菜品详情:', dishRes);

      if (dishRes.code === 200 && dishRes.data) {
        this.setData({
          dishInfo: dishRes.data
        });
      } else {
        wx.showToast({
          title: '菜品不存在',
          icon: 'none'
        });
      }

      // 获取评价列表
      const evalRes = await evaluationApi.getDishEvaluations(dishId);
      console.log('评价列表:', evalRes);

      if (evalRes.code === 200 && evalRes.data) {
        this.setData({
          evaluations: evalRes.data || []
        });
      }
    } catch (error) {
      console.error('加载菜品数据错误:', error);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    } finally {
      this.setData({ loading: false });
      wx.hideLoading();
    }
  },

  // 跳转至评价页面
  navigateToEvaluation: function () {
    // 检查是否登录
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.studentId) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/login/login'
        });
      }, 1500);
      return;
    }

    wx.navigateTo({
      url: `/pages/evaluation/evaluation?id=${this.data.dishId}`
    });
  },

  // 下拉刷新
  onPullDownRefresh: function () {
    if (this.data.dishId) {
      this.loadDishData(this.data.dishId);
    }
    wx.stopPullDownRefresh();
  },

  // 分享
  onShareAppMessage: function () {
    return {
      title: this.data.dishInfo.dishName || '校园食堂美食',
      path: `/pages/dishDetail/dishDetail?id=${this.data.dishId}`
    };
  }
}); 