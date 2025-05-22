const { userApi } = require('../../utils/api');

Page({
  data: {
    userInfo: null,
    isLogin: false
  },

  onLoad: function() {
    this.checkLogin();
  },

  // 检查登录状态
  checkLogin: function() {
    const token = wx.getStorageSync('token');
    if (token) {
      this.setData({ isLogin: true });
      // this.getUserInfo();
    }
  },

  // 微信登录
  wxLogin: function() {
    wx.login({
      success: async (res) => {
        if (res.code) {
          try {
            const loginRes = await userApi.wxLogin(res.code);
            console.log(
              loginRes
            );
            wx.setStorageSync('token', loginRes.data.token);
            wx.setStorageSync('userInfo', loginRes.data.userInfo);

            this.setData({ isLogin: true,userInfo:loginRes.userInfo });
            // this.getUserInfo();
            wx.showToast({
              title: '登录成功',
              icon: 'success'
            });
            // 登录成功后返回上一页
            // setTimeout(() => {
            //   wx.navigateBack();
            // }, 1500);
            console.log("11")
            wx.redirectTo({
              url: 'page/index/index'
            })

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

  // 退出登录
  logout: function() {
    wx.removeStorageSync('token');
    this.setData({
      isLogin: false,
      userInfo: null
    });
    wx.showToast({
      title: '已退出登录',
      icon: 'success'
    });
  }
}); 