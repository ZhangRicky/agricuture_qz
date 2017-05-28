package com.chenghui.agriculture.controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.vo.ReportProjects;
import com.chenghui.agriculture.service.projectManage.ProjectsService;
import com.chenghui.agriculture.service.projectManage.StatisticalChartService;
import com.chenghui.agriculture.service.system.AreaService;


/**
 * 项目统计分析
 * @author Mark
 *
 */
@RequestMapping("/statisticalReport")
@RestController
public class StatisticalChartController {
	private static Integer FinalYear = 0;		//统计项目个数的年份
	private static Integer villageFianceYear = 0;	//统计资金时用到
	
	private final static Logger logger = LoggerFactory.getLogger(StatisticalChartController.class); 
	
	@Autowired
	private ProjectsService projectsService;
	
	@Autowired
	private StatisticalChartService statisticalChartService;
	
	@Autowired
	private AreaService areaService;

	/**
	 * 项目中的饼图数据
	 * @param projects_id
	 * @return
	 */
	@RequestMapping(value="/projectsReport/{projects_id:\\d+}", method = RequestMethod.GET)
	public String projectsChart(@PathVariable Long projects_id){
		Projects pro = projectsService.get(projects_id);
		double bk = pro.getBkTotal()== null ? 0 : pro.getBkTotal();	
		double bz = pro.getBzTotal() == null ? 0 : pro.getBzTotal();
		String str = "";
		str = 	"<chart caption='项目资金使用情况' decimals='2' formatNumberScale='0' bgAngle='360' numbersuffix='万元' theme='fint' showBorder='1' legendIconScale = '0' bgColor='99CCFF,FFFFFF'>"+ 			
					"<set name='已拨款金额' value='"+bk+"' color='AFD8F8'/>"+  
					"<set name='可拨款金额' value='"+(pro.getTotalFund() - bk)+"' color='98FB98'/>"+
					"<set name='已报账金额' value='"+(bz)+"' color='D2691E'/>"+
					"<set name='可报账金额' value='"+(bk-bz)+"' color='006400'/>"+
					"<set name='项目结余金额' value='"+(pro.getBalance()==null ? 0 : pro.getBalance())+"' color='009966'/>"+				
			  	"</chart>";	
		return str;
	}
	
	/**
	 * 统计乡镇产业的占比
	 */
	@RequestMapping(value="/findReportByColumn3D/{year:\\d+}",method = RequestMethod.GET)
	public StringBuffer findByChanye(@PathVariable Integer year){
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		StringBuffer strs = new StringBuffer();
		strs.append("<chart caption='产业资金占比统计' subCaption=' '  formatNumberScale='0'  decimals='2' bgAngle='360' numbersuffix='万元' "
				+ "showValues='1' showLegend='1' showPercentValues='1' showPercentInTooltip='0' "
				+ "showHoverEffect='0' legendBorderAlpha='0' legendShadow='1' " 
				+ " theme='fint'> ");
		//产业类型
		String[] str = new String[]{"种养产业","基础设施","培训","贴息","经济组织","其他"};	//项目类型
		List<ReportProjects> list =statisticalChartService.getReportByIndustry(year);
		for(ReportProjects pro :list){
				switch(pro.getProjectType()){
					case 1:
						strs.append("<set label='"+str[0]+"' value='"+pro.getTotalFund()+"'/>");
						break;
					case 2:
						strs.append("<set label='"+str[1]+"' value='"+pro.getTotalFund()+"'/>");
						break;					
					case 3:
						strs.append("<set label='"+str[2]+"' value='"+pro.getTotalFund()+"'/>");
						break;					
					case 4:
						strs.append("<set label='"+str[3]+"' value='"+pro.getTotalFund()+"'/>");
						break;					
					case 5:
						strs.append("<set label='"+str[4]+"' value='"+pro.getTotalFund()+"'/>");
						break;	
					case 6:
						strs.append("<set label='"+str[5]+"' value='"+pro.getTotalFund()+"'/>");
						break;	
			}
		}
		strs.append("</chart>");
		return strs;
	}
	

	/**
	 * 统计各乡镇资金年度比较
	 */
	@RequestMapping(value="/findReportByScrollCombiDY2D/{townYear:\\d+}",method = RequestMethod.GET)
	public StringBuffer findByTownOfProjects(@PathVariable Integer townYear ){
		villageFianceYear = townYear;
		StringBuffer str = new StringBuffer();
		List<ReportProjects> list = statisticalChartService.findByProjectOfTown(townYear);
		System.out.println(list);
		str.append("<chart caption='乡镇项目资金年度统计分析' subCaption=' '  formatNumberScale='0' decimals='2' bgAngle='360' "
				+ "yAxisName='资金单位:万元' showValues='1' bgColor='0099CC,FFFFFF' valueFontColor='ffffff' "
				+ "bgAlpha='20' showBorder='1' theme='fint' labelDisplay='auto' useRoundEdges='1' divLineIsDashed='1' "
				+ "showExportDataMenuItem='1'> ");
		str.append("<dataset seriesName='"+townYear+"年'>");
		for( ReportProjects pro : list){
			str.append("<set label='"+pro.getTown()+"' value='"+pro.getTotalFund()+"' link='j-findByVillageFinance-"+pro.getTown()+"'/>");
		}
		str.append("</dataset></chart>");
		System.out.println(str);
		return str;
	}

	/**
	 * 指定镇下各村的资金统计
	 */
	@RequestMapping(value="/reportByVillageFinanceCount/{town}",method = RequestMethod.GET)
	public StringBuffer reportByVillageFinanceCount(@PathVariable String town ){
		StringBuffer chars = new StringBuffer();
		List<ReportProjects> list = statisticalChartService.reportByVillageFinanceCount(town,villageFianceYear);
		if(list.size()==0){
			return chars.append("null");
		}
		chars.append("<chart caption='项目资金分部统计分析' formatNumberScale='0' yAxisName='资金单位:万元' decimals='2'"
				+ " showValues='1' useRoundEdges='1' showSum='1' >");
		chars.append("<categories>");
		for(ReportProjects rep : list){
			chars.append(" <category label='"+rep.getVillage()+"'/>");
		}
		chars.append("</categories>");
		
		chars.append("<dataset seriesName='项目所属地区'>");
		for(ReportProjects rep : list){
			chars.append("<set value='"+(rep.getTotalFund()== 0 ? "" : rep.getTotalFund())+"'/>");
		}
		chars.append("</dataset>");
		chars.append("</chart> ");
		//System.out.println(chars);
		return chars;
	}
	
	
	
	/**
	 * 获取乡镇的清单
	 */
	@RequestMapping(value="/getList_ForTown/{sort:\\d+}/{fzid:\\d+}",method = RequestMethod.GET)
	public List<Area> getDepartmentList_ForReport(@PathVariable  Integer sort,@PathVariable Long fzid) {
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		List<Area> departList =statisticalChartService.getDepartListBySort_ForReport(sort,fzid,shiroUser);	
		return departList;
	}
	

	
	/**
	 * 查询已下达项目
	 * @param townId	乡镇ID
	 * @param villageId	村级ID
	 * @param year		年份
	 * @return
	 */
	@RequestMapping(value="/reportForVillage/{townId:\\d+}/{villageId:\\d+}/{year:\\d+}",method=RequestMethod.GET)
	public StringBuffer reportByTownAndVillage(@PathVariable Long townId,@PathVariable Long villageId,@PathVariable String year){
		StringBuffer strs = new StringBuffer();
		List<Projects> list = statisticalChartService.getReportByVillage(townId,villageId,year);
		if(list.size() == 0){
			return strs.append("null");
		}
		Area area = areaService.get(villageId);
		strs.append("<chart caption='"+year+"年"+area.getAreaName()+"项目资金的统计情况' formatNumberScale='0' decimals='2' xAxisName='项目名称' PYAxisName='项目资金(单位:万元)' SYAxisName='百分比 %' useRoundEdges='1' showValues='1' >");
		strs.append("<categories>");
		for(Projects p :list){
			strs.append("<category label='"+p.getProjectName()+"' />");
		}
		strs.append("</categories>");

		strs.append("<dataset seriesName='已下达项目'>");
		for(Projects p :list){
			strs.append("<set value='"+p.getTotalFund()+"' />");
		}
		strs.append("</dataset>");
		strs.append("<dataset seriesName='项目报账率'>");
		for(Projects p :list){
			strs.append("<set value='"+p.getFinancebiLv() == null ? 0 : p.getFinancebiLv()+"' />");
		}
		strs.append("</dataset></chart> ");
		return strs;
	}
	
	/**
	 * 查询已完成的项目
	 * @author zgz
	 * @param townId	乡镇ID
	 * @param villageId	村级ID
	 * @param year	统计年份
	 */
	@RequestMapping(value="/reportForProjectsFinlished/{townId:\\d+}/{villageId:\\d+}/{year:\\d+}",method=RequestMethod.GET)
	public StringBuffer reportForProjectsFinlished(@PathVariable Long townId,@PathVariable Long villageId,@PathVariable String year){
		StringBuffer chartData = new StringBuffer();
		Area area = areaService.get(villageId);
		List<Projects> list = statisticalChartService.reportForProjectsFinlished(townId,villageId,year);
		if(list.size() == 0){
			return chartData.append("null");
		}
		chartData.append("<chart caption='"+year+"年"+area.getAreaName()+"完成项目的统计情况' decimals='2' formatNumberScale='0' useRoundEdges='1' xAxisName='项目名称' yAxisName='项目资金(单位:万元)'   showValues='1'  >");
		chartData.append("<categories>");
		for(Projects p :list){
			chartData.append("<category label='"+p.getProjectName()+"' />");
		}
		chartData.append("</categories>");
		chartData.append("<dataset seriesName='完成项目'>");
		for(Projects p :list){
			chartData.append("<set value='"+p.getTotalFund()+"' />");
		}
		chartData.append("</dataset></chart> ");
		//System.out.println(chartData);
		return chartData;

	}
	
	/**
	 * 项目个数的统计
	 * @param year	统计年份
	 */
	@RequestMapping(value="/reportByProjectsCount/{year:\\d+}",method=RequestMethod.GET)
	public StringBuffer reportByProjectsCount(@PathVariable Integer year){
		FinalYear =  year;
		StringBuffer chars = new StringBuffer();
		String title = "";
		if(year == 0){
			Calendar cal = Calendar.getInstance();
			year = cal.get(Calendar.YEAR);
			title = year +"年";	
		}
		List<ReportProjects> list = statisticalChartService.getReportByProjectsCount(year);
		chars.append("<chart caption='"+title+"区域项目统计情况' decimals='0' formatNumberScale='0' xAxisName='项目所属地区' yAxisName='项目数量(单位:个)' divLineIsDashed='1' bgColor='0099CC,00CC66' bgAlpha='20' showBorder='1' showValues='1' numbersuffix='' >");
		for(ReportProjects pro :list){
			chars.append("<set label='"+pro.getTown()+"' value='"+pro.getProjectCount()+"' link='j-findByVillage-"+pro.getTown()+"' />");
		}
		chars.append("</chart> ");
		return chars;
	}
	
	/**
	 * 查询指定镇下面所有村的项目
	 * @author zgx
	 * @param 乡镇名称
	 */
	@RequestMapping(value="/reportByVillageOfProjects/{town}",method=RequestMethod.GET)
	public StringBuffer reportByVillageOfProjects(@PathVariable String town){
		StringBuffer chars = new StringBuffer();
		List<ReportProjects> list = projectsService.findByTown(town,FinalYear);
		if(list.size() == 0){
			return chars.append("null");
		}
		chars.append("<chart caption='项目分部统计分析' decimals='0' formatNumberScale='0' xAxisName='项目所属地区' yAxisName='项目数量(单位:个)' showBorder='1'  showValues='1' numbersuffix='个'  showSum='1' >");
		chars.append("<categories>");
		for(ReportProjects rep : list){
			chars.append(" <category label='"+rep.getVillage()+"' />");
		}
		chars.append("</categories>");
		
		chars.append("<dataset>");
		for(ReportProjects rep : list){
			chars.append("<set value='"+rep.getProjectCount()+"'/>");
		}
		chars.append("</dataset>");
		chars.append("</chart> ");
		return chars;
	}
	
	/**
	 * 加载下拉选中项目的列表
	 * @author zgx
	 * @version 1.0
	 * @param townId	乡镇ID
	 */
	@RequestMapping(value="/projectNameByTown/{townId:\\d+}",method=RequestMethod.GET)
	public List<Projects> findByProjectsList(@PathVariable Long townId){
		List<Projects> list = projectsService.findbyOneToMany(townId);	
		return list;
	}
	
	/**
	 * 按项目名称统计
	 * @author zgx
	 * @param townId 乡镇ID
	 * @param projectNameId 项目ID
	 */
	@RequestMapping(value="/reportByProjectsName/{townId:\\d+}/{projectNameId:\\d+}",method=RequestMethod.GET)
	public StringBuffer reportByProjectsName(@PathVariable Long townId,@PathVariable Long projectNameId){
		StringBuffer charts = new StringBuffer();
		Projects p = projectsService.get(projectNameId);
		List<Projects> list = statisticalChartService.getBySameProjectName(townId,projectNameId);
		if(list.size() == 0){
			return charts.append("null");
		}
		charts.append("<chart caption='"+p.getProjectName()+"' subcaption='项目分布统计分析' formatNumberScale='0'  xaxisname='项目分布地区' PYaxisname='资金情况(单位:万元)' SYAxisName='覆盖农户(单位:户)' numberSuffix='万元' showBorder ='1'  sNumberSuffix='户' decimals='2'>");
		
		System.out.println(list);
		charts.append("<categories font='Arial' fontSize='12' fontColor='000000'>");
		for(Projects pro : list){
			charts.append("<category label='"+pro.getVillage()+"'/>");
		}
		charts.append("</categories>");
		charts.append("<dataset seriesName='项目剩余金额' >");
		for(Projects pro : list){
			Double lost = pro.getTotalFund() - (pro.getFinancebiLv()==null ? 0 : Double.parseDouble(pro.getFinancebiLv()));
			charts.append("<set value='"+lost +"'/>");
		}
		charts.append("</dataset>");
		//报账金额
		charts.append("<dataset seriesName='项目报账金额' color='F6BD0F' showValues='1'>");
		for(Projects pro : list){
			charts.append("<set value='"+pro.getFinancebiLv() == null ? "0" : pro.getFinancebiLv() +"'/>");	
		}
		charts.append("</dataset>");
		charts.append("<dataset seriesName='项目覆盖农户' parentYAxis='S' >");
		for(Projects pro : list){
			charts.append("<set value='"+pro.getCoveredFarmerNumber() == null ? 0 : pro.getCoveredFarmerNumber() +"'/>");
		}
		charts.append("</dataset>");
		charts.append("<dataset seriesName='项目覆盖贫困户' parentYAxis='S'>");
		for(Projects pro : list){
			charts.append("<set value='"+pro.getPovertyStrickenPeopleNumber()==null ? 0 : pro.getPovertyStrickenPeopleNumber()+"'/>");	
			
		}
		charts.append("</dataset>");
		charts.append("</chart>");
		System.out.println(charts);
		return charts;
	}
	
}
