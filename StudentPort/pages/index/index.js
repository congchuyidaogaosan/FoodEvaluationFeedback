const { dishApi, noticeApi, userApi } = require('../../utils/api');

Page({
  data: {
    notices: [],
    categories: [],
    dishes: [],
    currentCategory: null,
    loading: false,
    searchKeyword: '',
    isLogin: false
  },

  onLoad: function() {
    this.checkLogin();
    this.getNotices();
    this.getDishCategories();
  },

  // 检查登录状态
  checkLogin: function() {
    const token = wx.getStorageSync('token');
    if (token) {
      this.setData({ isLogin: true });
      this.getUserInfo();
    } else {
      this.wxLogin();
    }
  },

  // 微信登录
  wxLogin: function() {
    wx.login({
      success: async (res) => {
        if (res.code) {
          try {
            const loginRes = await userApi.wxLogin(res.code);
            wx.setStorageSync('token', loginRes.token);
            this.setData({ isLogin: true });
            this.getUserInfo();
          } catch (error) {
            wx.showToast({
              title: '登录失败',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  // 获取用户信息
  getUserInfo: async function() {
    try {
      const res = await userApi.getUserInfo();
      this.setData({ userInfo: res.data });
    } catch (error) {
      console.error('获取用户信息失败:', error);
    }
  },

  // 获取通知列表
  getNotices: async function() {
    try {
      const res = await noticeApi.getNotices();
      this.setData({ notices: res.data });
    } catch (error) {
      wx.showToast({
        title: '获取通知失败',
        icon: 'none'
      });
    }
  },

  // 获取菜品分类
  async getDishCategories() {
    try {
      const res = await dishApi.getCategories();
      this.setData({
        categories: res.data
      });
      // 默认选中第一个分类
      if (res.data.length > 0) {
        this.setData({
          currentCategory: res.data[0].id
        });
        this.getDishesByCategory(res.data[0].id);
      }
    } catch (error) {
      console.error('获取分类失败:', error);
      wx.showToast({
        title: '获取分类失败',
        icon: 'none'
      });
    }
  },

  // 根据分类获取菜品
  async getDishesByCategory(categoryId) {
    this.setData({ loading: true });
    try {
      const res = await dishApi.getDishesByCategory(categoryId);
      this.setData({
        dishes: res.data,
        loading: false
      });
    } catch (error) {
      console.error('获取菜品失败:', error);
      wx.showToast({
        title: '获取菜品失败',
        icon: 'none'
      });
      this.setData({ loading: false });
    }
  },

  // 搜索菜品
  async onSearch(e) {
    const keyword = e.detail.value;
    this.setData({ searchKeyword: keyword });
    
    if (!keyword) {
      // 如果搜索框为空，显示当前分类的菜品
      if (this.data.currentCategory) {
        this.getDishesByCategory(this.data.currentCategory);
      }
      return;
    }

    this.setData({ loading: true });
    try {
      const res = await dishApi.searchDishes(keyword);
      this.setData({
        dishes: res.data,
        loading: false
      });
    } catch (error) {
      console.error('搜索菜品失败:', error);
      wx.showToast({
        title: '搜索失败',
        icon: 'none'
      });
      this.setData({ loading: false });
    }
  },

  // 切换分类
  onCategoryTap(e) {
    const categoryId = e.currentTarget.dataset.id;
    this.setData({
      currentCategory: categoryId
    });
    this.getDishesByCategory(categoryId);
  },

  // 导航到菜品详情页
  navigateToDetail: function(e) {
    const dishId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/dishDetail/dishDetail?id=${dishId}`
    });
  },

  // 跳转到通知详情
  navigateToNoticeDetail: function(e) {
    const noticeId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/notice/notice?id=${noticeId}`
    });
  }
}); 