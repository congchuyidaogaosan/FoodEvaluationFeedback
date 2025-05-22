const { evaluationApi, dishApi } = require('../../utils/api');

Page({
  data: {
    dishId: null,
    dishInfo: null,
    scores: {
      taste: 5,
      color: 5,
      quantity: 5,
      pricePerformance: 5
    },
    comment: '',
    submitting: false,
    loading: true
  },

  onLoad: function(options) {
    if (options.id) {
      this.setData({ dishId: options.id });
      this.getDishInfo();
    }
  },

  // 获取菜品信息
  getDishInfo: async function() {
    try {
      const res = await dishApi.getDishDetail(this.data.dishId);
      console.log('获取到的菜品信息:', res.data);
      
      // 确保图片URL存在
      if (res.code === 200 && res.data) {
        // 如果url为空，设置一个默认图片
        if (!res.data.url) {
          res.data.url = '../../images/default-dish.png';
          console.log('菜品图片URL为空，使用默认图片');
        }
        
        this.setData({ 
          dishInfo: res.data,
          loading: false
        });
        
        console.log('设置后的dishInfo:', this.data.dishInfo);
      } else {
        console.error('菜品数据异常:', res);
        this.setData({ loading: false });
        wx.showToast({
          title: '获取菜品信息失败',
          icon: 'none'
        });
      }
    } catch (error) {
      console.error('获取菜品信息错误:', error);
      this.setData({ loading: false });
      wx.showToast({
        title: '获取菜品信息失败',
        icon: 'none'
      });
    }
  },

  // 图片加载失败处理
  onImageError: function(e) {
    console.error('菜品图片加载失败，使用默认图片');
    // 设置默认图片
    this.setData({
      'dishInfo.url': '../../images/default-dish.png'
    });
  },

  // 评分变化
  onScoreChange: function(e) {
    const { type } = e.currentTarget.dataset;
    const { value } = e.detail;
    this.setData({
      [`scores.${type}`]: value
    });
  },

  // 评论内容变化
  onCommentChange: function(e) {
    this.setData({
      comment: e.detail.value
    });
  },

  // 提交评价
  submitEvaluation: async function() {
    if (this.data.submitting) return;

    this.setData({ submitting: true });

    try {
      await evaluationApi.submitEvaluation({
        dishId: this.data.dishId,
        taste: this.data.scores.taste,
        color: this.data.scores.color,
        quantity: this.data.scores.quantity,
        pricePerformance: this.data.scores.pricePerformance,
        comment: this.data.comment
      });

      wx.showToast({
        title: '评价成功',
        icon: 'success'
      });

      // 返回上一页并刷新数据
      setTimeout(() => {
        const pages = getCurrentPages();
        const prevPage = pages[pages.length - 2]; // 获取上一个页面对象
        
        // 如果上一页是菜品详情页，调用刷新方法
        if (prevPage && prevPage.route === 'pages/dishDetail/dishDetail') {
          prevPage.loadDishData(this.data.dishId);
        }
        
        wx.navigateBack();
      }, 1500);
    } catch (error) {
      console.error('提交评价失败:', error);
      wx.showToast({
        title: '评价失败',
        icon: 'none'
      });
    } finally {
      this.setData({ submitting: false });
    }
  }
}); 