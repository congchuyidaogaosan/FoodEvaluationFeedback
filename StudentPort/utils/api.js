const baseUrl = 'http://localhost:8084'; // 替换为您的实际后端API地址

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
  wxLogin: (code) => request('/WXLogin/setCode?code='+code, 'POST'),
  // 获取用户信息
  getUserInfo: () => request('/Student/info', 'GET'),
  // 更新用户信息
  updateUserInfo: (data) => request('/Student/update', 'POST', data)
};

// 菜品相关API
const dishApi = {
  // 获取特价菜品列表
  getSpecialDishes: (params) => request('/dishes/special', 'GET', params),
  
  // 获取菜品分类列表
  getCategories: () => request('/DishCategory', 'GET'),
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
  submitEvaluation: (data) => {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.studentId) {
      return Promise.reject(new Error('用户未登录'));
    }
    const evalData = {
      ...data,
      studentId: userInfo.studentId,
      tasteScore: data.taste || 5,
      colorScore: data.color || 5,
      quantityScore: data.quantity || 5,
      pricePerformanceScore: data.pricePerformance || 5
    };
    return request('/evaluations/save', 'POST', evalData);
  },
  // 获取我的评价列表
  getMyEvaluations: () => {
    const userInfo = wx.getStorageSync('userInfo');
    console.log('API中获取的用户信息:', userInfo);
    if (!userInfo || !userInfo.studentId) {
      console.error('用户信息不存在或没有studentId');
      return Promise.reject(new Error('用户未登录'));
    }
    return request('/evaluations/my', 'POST', { studentId: userInfo.studentId });
  },
  // 获取菜品评价列表
  getDishEvaluations: (dishId) => request('/evaluations/dish/' + dishId, 'GET'),
  // 删除评价
  deleteEvaluation: (evalId) => request('/evaluations/del/' + evalId, 'DELETE')
};

// 反馈相关API
const feedbackApi = {
  // 提交反馈
  submitFeedback: (content) => {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.studentId) {
      return Promise.reject(new Error('用户未登录'));
    }
    return request('/notices/save', 'POST', {
      content: content,
      studentId: userInfo.studentId,
      isHandled: 0
    });
  },
  // 获取我的反馈列表
  getFeedbackList: () => {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.studentId) {
      return Promise.reject(new Error('用户未登录'));
    }
    return request('/notices/my', 'GET', { studentId: userInfo.studentId });
  },
  // 获取反馈详情
  getFeedbackDetail: (feedbackId) => request('/notices/' + feedbackId, 'GET')
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