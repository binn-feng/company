
  // costing
  // 模拟保存接口
  Mock.mock('/costing/save', 'post', (options) => {
    const { form } = JSON.parse(options.body);
    return {
      code: 200,
      message: '保存成功',
      data: {} 
    };
  });