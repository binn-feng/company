const currentFormData = [
  {
    senderInfo: [],
    carbonCopyInfo: [
      { nickName: '穆立源', default: true, userId: '110' },
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '需进行成本报价 - {商机主题} - {日期}',
    emailTemplate: '{商机主题} 需进行成本报价，请尽快完成。\n\n点击{报价系统链接}进行成本报价'
  },
  {
    senderInfo: [],
    carbonCopyInfo: [
      { nickName: '穆立源', default: true, userId: '110' },
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '已完成成本报价 - {商机主题} - {售前部门} – {报价类型}{版本号} – {日期}',
    emailTemplate: '{售前部门} 已完成{商机主题}成本报价，总成本为：{成本总额}，请尽快完成销售报价\n\n点击{报价系统链接}进行销售报价'
  },
  {
    senderInfo: [],
    carbonCopyInfo: [
      { nickName: '穆立源', default: true, userId: '110' },
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '需更新成本报价 - {商机主题} - {日期}',
    emailTemplate: '{商机主题}，\n\n所属销售：{所属销售}，由于{重新报价说明}，需重新进行成本报价，请尽快完成。\n\n点击{报价系统链接} 进行成本报价更新'
  },
  {
    senderInfo: [
      { nickName: '王总', default: true, userId: '115' }
    ],
    carbonCopyInfo: [
      { nickName: '穆立源', default: true, userId: '110' },
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '对外销售报价审批申请 - {商机主题} – {日期}',
    emailTemplate: '{商机主题} ，所属销售：{所属销售}，所属售前：{所属售前}\n\n评估总成本为：{成本总额}，销售对外报价：{销售对外报价}，项目利润率：{项目报价利润率}\n继续跟进原因：{继续跟进原因}\n\n请点击{报价系统链接}进行审批'
  },
  {
    senderInfo: [
      { nickName: '王总', default: true, userId: '115' }
    ],
    carbonCopyInfo: [
      { nickName: '穆立源', default: true, userId: '110' },
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '{销售报价审批结果} - {商机主题} - 销售报价 – {日期}',
    emailTemplate: '{商机主题}\n\n评估总成本为：{成本总额}，销售对外报价：{销售对外报价}，项目利润率：{项目报价利润率}\n报价审批结果：{销售报价审批结果}\n\n\n点击{报价系统链接}查看详情'
  },
  {
    senderInfo: [
      { nickName: '王总', default: true, userId: '115' }
    ],
    carbonCopyInfo: [
      { nickName: '穆立源', default: true, userId: '110' },
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '签约申请 - {商机主题} – {日期}',
    // emailTemplate: '{商机主题}，所属销售：{所属销售}，所属售前：{所属售前}\n\n评估总成本为：{成本总额)\n北光合同总金额：{北光合同总金额}，北光合同金额(不含硬件)：{北光合同金额不含硬件部分}\n北光总成本利润率：{总成本利润率}，成本利润率(不含硬件)：{成本利润率}\n\n\n继续签约原因：{继续签约原因}\n\n请点击{报价系统链接}进行签约审批'
    emailTemplate: '{签约申请详情图片}\n\n请点击{报价系统链接}进行签约审批'
  },
  {
    senderInfo: [],
    carbonCopyInfo: [
      { nickName: '穆立源', default: true, userId: '110' },
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '{签约申请审批结果} - {商机主题} 签约申请 – {日期}',
    emailTemplate: '{商机主题}\n\n评估总成本为：{成本总额}，销售对外报价：{销售对外报价}，项目利润率：{项目利润率}\n签约申请审批结果：{签约申请审批结果}\n\n\n点击{报价系统链接}查看详情'
  },
  {
    senderInfo: [
      { nickName: '穆立源', default: true, userId: '110' }
    ],
    carbonCopyInfo: [
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '不再继续跟进 - {商机主题} - {日期}',
    emailTemplate: '{商机主题} ，所属销售：{所属销售}，所属售前：{所属售前}\n\n成本评估类型：{无法，粗略，详细}\n评估总成本为：{成本总额}\n计划销售对外报价：{销售对外报价}，项目利润率(不含外采)：{项目报价利润率}\n\n建议不再继续跟进\n丢单理由：{丢单理由}，请审批确认\n\n点击{报价系统链接}查看详情，进行审批'
  },
  {
    senderInfo: [
      { nickName: '穆立源', default: true, userId: '110' }
    ],
    carbonCopyInfo: [
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '丢单 - {商机主题} – {日期}',
    emailTemplate: '{商机主题} ，所属销售：{所属销售}，所属售前：{所属售前}\n\n成本评估类型：{无法，粗略，详细}\n评估总成本为：{成本总额}\n\n销售对外报价：{销售对外报价}，项目利润率(不含外采)：{项目报价利润率}，报价审批结果：{报价申请结果}\n预计签约金额：{申请签约金额}，项目利润率(不含外采)：{项目签约利润率}，签约申请审批结果：{签约申请审批结果}\n\n申请进行丢单处理，请审批，以下为具体丢单原因：\n丢单原因：{丢单原因}\n\n点击{报价系统链接}查看详情，进行审批'
  },
  {
    senderInfo: [
      { nickName: '穆立源', default: true, userId: '110' }
    ],
    carbonCopyInfo: [
      { nickName: '龚夏婷', default: true, userId: '113' },
      { nickName: '于子涵', default: true, userId: '101' },
      { nickName: '夏群', default: true, userId: '100' }
    ],
    emailSubject: '丢单信息汇总 - {时间周期}',
    emailTemplate: '附件为 {时间周期} 期间，预计不再继续跟进的丢单信息汇总，请您下载附件查看。\n\n点击{管理系统链接}查看详情'
  }
]

export default currentFormData
