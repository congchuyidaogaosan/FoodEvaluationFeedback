const { userApi } = require('../../utils/api');

const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'

Page({
  data: {
    userInfo: {
      avatarUrl: defaultAvatarUrl,
      nickName: '',
      studentNumber: ''
    },
    hasUserInfo: false,
    loading: true,
    canIUseGetUserProfile: wx.canIUse('getUserProfile'),
    canIUseNicknameComp: wx.canIUse('input.type.nickname')
  },

  onLoad: function () {
    this.checkUserInfo();
  },

  onShow: function () {
    // 每次页面显示时重新获取用户信息
    this.checkUserInfo();
  },

  // 检查用户信息
  async checkUserInfo() {
    const token = wx.getStorageSync('token');
    if (token) {
      try {
        const res = await userApi.getUserInfo();
        this.setData({
          userInfo: {
            ...this.data.userInfo,
            ...res.data
          },
          hasUserInfo: res.data.nickName && res.data.avatarUrl,
          loading: false
        });
      } catch (error) {
        console.error('获取用户信息失败:', error);
        this.setData({ loading: false });
      }
    } else {
      this.setData({ loading: false });
    }
  },

  // 获取用户信息
  getUserProfile() {
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: (res) => {
        this.setData({
          userInfo: {
            ...this.data.userInfo,
            avatarUrl: res.userInfo.avatarUrl,
            nickName: res.userInfo.nickName
          },
          hasUserInfo: true
        });
        // 更新用户信息到服务器
        this.updateUserInfo();
      },
      fail: (err) => {
        console.error('获取用户信息失败:', err);
        wx.showToast({
          title: '获取用户信息失败',
          icon: 'none'
        });
      }
    });
  },

  // 选择头像
  onChooseAvatar(e) {
    const { avatarUrl } = e.detail;
    this.setData({
      'userInfo.avatarUrl': avatarUrl,
      hasUserInfo: this.data.userInfo.nickName && avatarUrl
    });
    // 更新用户信息到服务器
    this.updateUserInfo();
  },

  // 输入昵称
  onInputChange(e) {
    const nickName = e.detail.value;
    this.setData({
      'userInfo.nickName': nickName,
      hasUserInfo: nickName && this.data.userInfo.avatarUrl
    });
    // 更新用户信息到服务器
    this.updateUserInfo();
  },

  // 更新用户信息到服务器
  async updateUserInfo() {
    try {
      await userApi.updateUserInfo({
        avatarUrl: this.data.userInfo.avatarUrl,
        nickName: this.data.userInfo.nickName
      });
    } catch (error) {
      console.error('更新用户信息失败:', error);
    }
  },

  // 跳转到个人中心
  navigateToProfile() {
    wx.navigateTo({
      url: '/pages/profile/profile'
    });
  },

  // 跳转到我的评价
  navigateToMyEvaluations: function () {
    wx.navigateTo({
      url: '/pages/myEvaluations/myEvaluations'
    });
  },

  // 跳转到我的反馈
  navigateToFeedback: function () {
    wx.navigateTo({
      url: '/pages/feedback/feedback'
    });
  },

  // 退出登录
  logout: function () {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token');
          wx.reLaunch({
            url: '/pages/login/login'
          });
        }
      }
    });
  }
}); 