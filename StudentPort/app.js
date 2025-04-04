// app.js
App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })

    this.checkLogin();
  },
  globalData: {
    userInfo: null
  },

  // 全局登录检查
  checkLogin() {
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.showToast({
        title: '请先登录',
        icon: 'none',
        duration: 1500
      });
      wx.redirectTo({
        url: '/pages/login/login'
      });
      return false;
    }
    return true;
  }
})
