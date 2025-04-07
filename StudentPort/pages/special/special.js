const { dishApi } = require('../../utils/api');

Page({
  data: {
    specialDishes: [],
    loading: false,
    currentPage: 1,
    pageSize: 10,
    hasMore: true
  },

  onLoad: function() {
    this.getSpecialDishes();
  },

  // 获取特价菜品列表
  async getSpecialDishes() {
    // 如果没有更多数据或正在加载，直接返回
    if (!this.data.hasMore || this.data.loading) {
      return;
    }
    
    this.setData({ loading: true });
    try {
      const res = await dishApi.getSpecialDishes({
        page: this.data.currentPage,
        pageSize: this.data.pageSize
      });
      
      const newDishes = res.data || [];
      const hasMore = newDishes.length === this.data.pageSize;
      
      this.setData({
        specialDishes: [...this.data.specialDishes, ...newDishes],
        hasMore: hasMore,
        currentPage: this.data.currentPage + 1,
        loading: false
      });
    } catch (error) {
      console.error('获取特价菜品失败:', error);
      wx.showToast({
        title: '获取特价菜品失败',
        icon: 'none'
      });
      this.setData({ loading: false });
    }
  },

  // 下拉刷新
  onPullDownRefresh: function() {
    this.setData({
      specialDishes: [],
      currentPage: 1,
      hasMore: true,
      loading: false
    });
    this.getSpecialDishes().then(() => {
      wx.stopPullDownRefresh();
    });
  },

  // 上拉加载更多
  onReachBottom: function() {
    if (this.data.hasMore && !this.data.loading) {
      this.getSpecialDishes();
    }
  },

  // 跳转到菜品详情
  navigateToDetail: function(e) {
    const dishId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/dishDetail/dishDetail?id=${dishId}`
    });
  }
}); 