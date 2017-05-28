//左侧数-数据
window.cityTreeListDS = [
	{
		text:"江苏省",
		expanded:true,
		items: [
			{
				text: "徐州市",
				expanded:true,
				items:[
					{
						text:"市辖区"
					}, {
						text:"云龙"
					}, {
						text:"徐州区"
					}, {
						text:"铜山"
					}, {
						text:"徐州北"
					}, {
						text:"九里"
					}, {
						text:"金山桥"
					}, {
						text:"铜山北"
					}, {
						text:"沛县"
					}, {
						text:"丰县"
					}, {
						text:"徐州区"
					}
				]
			},{
				text: "无锡市",
				expanded:true,
				items:[
					{
						text:"崇安区",
						expanded:true,
						items:[{
								text:"286683 崇安-现代环保"
							}, {
								text:"286687"
							}, {
								text:"286686"
							}, {
								text:"286685"
							}, {
								text:"286684"
							}, {
								text:"286688"
							}
						]
					}, {
						text:"南长区",
						expanded:true,
						items:[{
								text:"286670"
							}, {
								text:"286672"
							}
						]
					}, {
						text:"北塘区",
						expanded:true,
						items:[{
								text:"286682"
							}, {
								text:"286717"
							}, {
								text:"286718"
							}
						]
					}, {
						text:"江阴市",
						expanded:true,
						items: [{
								text:"286573"
							}, {
								text:"286575"
							}, {
								text:"286574"
							}, {
								text:"286576"
							}, {
								text:"286577"
							}, {
								text:"286578"
							}, {
								text:"286579"
							}, {
								text:"286580"
							}, {
								text:"286581"
							}, {
								text:"286582"
							}
						]
					}, {
						text:"宜兴市",
						expanded:true,
						items: [{
								text:"286599"
							}, {
								text:"286606"
							}, {
								text:"286607"
							}, {
								text:"286604"
							}, {
								text:"286605"
							}, {
								text:"286602"
							}, {
								text:"286603"
							}, {
								text:"286600"
							}
						]
					}
				]
			}
		]
	}
];


//角色管理 -- 绑定用户 数据
window.bindingUserTreeListDS = [
	{
		text:"南通市用户",
		expanded:true,
		items: [
			{
				text: "管理员"
			},{
				text: "南通市"
			},{
				text: "如东县"
			},{
				text: "启东市"
			},{
				text: "如皋市"
			},{
				text: "通州市"
			},{
				text: "海门市"
			},{
				text: "无锡"
			},{
				text: "省公司账号"
			},{
				text: "测试"
			},{
				text: "海安县"
			}
		]
	}
];

//角色管理 -- 绑定 系统功能 数据
window.bindingSiteMenuTreeListDS = [
	{
		text:"系统管理员",
		expanded:true,
		items: [
			{
				text: "故障监控",
				expanded:true,
				items:[
					{
						text:"告警监控",
						expanded:true,
						items:[
							{
								text: "基站重要告警"
							},{
								text: "全网超个数门限告警"
							},{
								text: "基站级别超个数门限告警"
							},{
								text: "历史告警查询"
							},{
								text: "当前激活告警查询"
							},{
								text: "告警统计报告"
							}
						]
					}, {
						text:"基站状态监控",
						items:[
							{
								text: "基站运行状态"
							},{
								text: "故障基站列表"
							}
						]
					}
				]
			}
		]
	}
];

//角色管理 -- 绑定 地区 数据
window.bindingAreaTreeListDS = [
	{
		text:"北京省",
		expanded:true,
		items: [
			{
				text: "北京市"
			},{
				text: "南通市"
			},{
				text: "启东市"
			},{
				text: "如皋市"
			},{
				text: "通州市"
			},{
				text: "海门市"
			},{
				text: "无锡"
			}
		]
	}
];
