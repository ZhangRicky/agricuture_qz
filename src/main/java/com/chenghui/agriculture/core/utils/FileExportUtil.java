package com.chenghui.agriculture.core.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 文件导出操作类
 * @author Ricky
 *
 */
public class FileExportUtil {
	
	public static Map<Integer, String> headers = new HashMap<>();
	public static Map<Integer, Integer> colWidth = new HashMap<>();
	public static String title = "";
	
	/**
	 * 设置标题
	 * @param excelSheet
	 * @param workbook
	 */
	public static void setExcelTitle(XSSFSheet excelSheet, XSSFWorkbook workbook){
		XSSFRow excelTitleRow = excelSheet.createRow(0);
		for(Integer colIndex: headers.keySet()){
			excelTitleRow.createCell(colIndex);
		}
		//设置行高
		excelTitleRow.setHeight((short)800);
		//合并单元格
		CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, headers.size()-1);
		excelSheet.addMergedRegion(cellRangeAddress);
		//填充标题内容
		XSSFCell cellTitle = excelTitleRow.getCell(0);
		cellTitle.setCellValue(title);
		//设置标题样式
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);//设置居中
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex()); //设置背景色
		cellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex()); //设置背景色
		//设置标题字体
		XSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)24);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		cellTitle.setCellStyle(cellStyle);
	}
	
	/**
	 * 设置标题和标题数据
	 */
	public static void setExcelData(){
		headers.put(0, "项目级别");
		headers.put(1, "村（居）");
	//	headers.put(1, "乡（镇、社）");
		headers.put(2, "项目名称");
		headers.put(3, "项目文号");
		headers.put(4, "项目唯一编号");
		headers.put(5, "实施单位");
		headers.put(6, "负责人");
		headers.put(7, "资金年度");
		headers.put(8, "项目专项名称");
		headers.put(9, "项目资金明细");
		headers.put(10, "项目批复文号");
		headers.put(11, "建设规模及内容");
		headers.put(12, "后补文号");
		
		headers.put(13, "项目总资金(万元)");
		headers.put(14, "项目财政资金(万元)");
		headers.put(15, "应报账金额(万元)");
		headers.put(16, "覆盖人数(人)");
		headers.put(17, "扶持贫困人口(人)");
		headers.put(18, "完成期限(月)");
		headers.put(19, "审核状态");
		headers.put(20, "建设方式");
		headers.put(21, "验收状态");
		headers.put(22, "创建用户");
		headers.put(23, "创建时间");
		headers.put(24, "录入状态");
		headers.put(25, "当前审核部门");
		headers.put(26, "备注");
		
		
		colWidth.put(0, 6000);
		colWidth.put(1, 6000);
		colWidth.put(2, 2500);
		colWidth.put(3, 5000);
		colWidth.put(4, 2500);
		colWidth.put(5, 4000);
		colWidth.put(6, 2000);
		colWidth.put(7, 2000);
		colWidth.put(8, 4000);
		colWidth.put(9, 4000);
		colWidth.put(10, 4000);
		colWidth.put(11, 3000);
		colWidth.put(12, 4000);
		
		colWidth.put(13, 4000);
		colWidth.put(14, 4000);
		colWidth.put(15, 4000);
		colWidth.put(16, 4000);
		colWidth.put(17, 4000);
		colWidth.put(18, 4000);
		colWidth.put(19, 4000);
		colWidth.put(20, 4000);
		colWidth.put(21, 4000);
		colWidth.put(22, 4000);
		colWidth.put(23, 4000);
		colWidth.put(24, 4000);
		colWidth.put(25, 4000);
		colWidth.put(26, 4000);

		title = "项目数据";	
	}
	
	/**
	 * 设置列名称及样式
	 * @param excelSheet
	 * @param workbook
	 */
	public static void setExcelHeader(XSSFSheet excelSheet, XSSFWorkbook workbook) {
		XSSFRow excelHeader = excelSheet.createRow(1);
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);//设置居中
		cellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex()); //设置背景色
		cellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex()); //设置背景色
		XSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)16);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		excelHeader.setRowStyle(cellStyle);
		for(Integer colIndex: headers.keySet()){
			XSSFCell headerCell = excelHeader.createCell(colIndex);
			headerCell.setCellStyle(cellStyle);
			headerCell.setCellValue(headers.get(colIndex));
		}
	}
	
	
	/**
	 * 设置列宽度
	 * @param excelSheet
	 */
	public static void setColumnWidth(XSSFSheet excelSheet) {
		for(Integer colIndex: colWidth.keySet()){
			excelSheet.setColumnWidth(colIndex, colWidth.get(colIndex));
		}
	}
	
	public static String encodeFilename(HttpServletRequest request, String filename){
		String encodeFilename = "";
		try {
			String agent = request.getHeader("USER-AGENT");
			if (agent != null && agent.indexOf("MSIE") != -1 || agent != null && agent.indexOf("Trident") != -1) {
				if (filename.contains(".xlsx")) {
					encodeFilename = java.net.URLEncoder.encode(filename, "UTF-8");
				} else {
					encodeFilename = java.net.URLEncoder.encode(filename + ".xlsx", "UTF-8");
				}
				
			}else{
				encodeFilename = new String(filename.getBytes("UTF-8"), "iso-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encodeFilename;
	}
	
	
}
