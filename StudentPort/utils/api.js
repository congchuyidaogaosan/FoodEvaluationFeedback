const baseUrl = 'http://localhost:8080/api'; // 替换为您的实际后端API地址

const request = (url, method, data) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + url,
      method: method,
      data: data,
      header: {
        'content-type': 'application/json',
        'Authorization': wx.getStorageSync('token') || ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(res);
        }
      },
      fail: (err) => {
        reject(err);
      }
    });
  });
};

// 用户相关API
const userApi = {
  // 微信登录
  wxLogin: (code) => request('/user/wx-login', 'POST', { code }),
  // 获取用户信息
  getUserInfo: () => request('/user/info', 'GET'),
  // 更新用户信息
  updateUserInfo: (data) => request('/user/update', 'POST', data)
};

// 菜品相关API
const dishApi = {
  // 获取特价菜品列表
  getSpecialDishes: (params) => request('/dishes/special', 'GET', params),
  // 获取菜品分类列表
  getCategories: () => request('/dishes/categories', 'GET'),
  // 根据分类获取菜品列表
  getDishesByCategory: (categoryId) => request('/dishes/category/' + categoryId, 'GET'),
  // 获取菜品详情
  getDishDetail: (dishId) => request('/dishes/' + dishId, 'GET'),
  // 搜索菜品
  searchDishes: (keyword) => request('/dishes/search', 'GET', { keyword })
};

// 评价相关API
const evaluationApi = {
  // 提交评价
  submitEvaluation: (data) => request('/evaluations', 'POST', data),
  // 获取我的评价列表
  getMyEvaluations: () => request('/evaluations/my', 'GET'),
  // 获取菜品评价列表
  getDishEvaluations: (dishId) => request('/evaluations/dish/' + dishId, 'GET'),
  // 删除评价
  deleteEvaluation: (evalId) => request('/evaluations/' + evalId, 'DELETE')
};

// 反馈相关API
const feedbackApi = {
  // 提交反馈
  submitFeedback: (content) => request('/feedback', 'POST', { content }),
  // 获取我的反馈列表
  getMyFeedback: () => request('/feedback/my', 'GET'),
  // 获取反馈详情
  getFeedbackDetail: (feedbackId) => request('/feedback/' + feedbackId, 'GET')
};

// 通知相关API
const noticeApi = {
  // 获取通知列表
  getNotices: () => request('/notices', 'GET'),
  // 获取通知详情
  getNoticeDetail: (noticeId) => request('/notices/' + noticeId, 'GET')
};

module.exports = {
  userApi,
  dishApi,
  evaluationApi,
  feedbackApi,
  noticeApi
}; 