package com.chenghui.agriculture.mvc;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.chenghui.agriculture.core.utils.DateUtil;
import com.chenghui.agriculture.core.utils.FileOperateUtil;

/**
 * 导出数据抽象类，可自定义标题，列名称，列宽
 * @author yudq
 *
 * @param <E>
 * @date 2015-11-27 11:34:00
 */
public abstract class SimpleListExcelView<E> extends AbstractExcelView {
	
	protected Map<Integer, String> headers = new HashMap<>();
	protected Map<Integer, Integer> colWidth = new HashMap<>();
	protected String title = "";
	protected String listName = "";
	

	public SimpleListExcelView() {
		super();
		init();
	}
	
	/**
	 * 初始化文档标题，列名称，列宽度，列表名称
	 */
	public abstract void init();
	
	/**
	 * 填充数据到excel
	 * @param excelSheet
	 * @param workbook
	 * @param recordsList
	 */
	public abstract void setExcelRows(HSSFSheet excelSheet, HSSFWorkbook workbook, List<E> recordsList);
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String downloadFileName = title+"-"+DateUtil.getCurrentDateTime2();
		 // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", FileOperateUtil.encodeFilename(request, downloadFileName));
        response.setHeader(headerKey, headerValue);
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");

 		HSSFSheet excelSheet = workbook.createSheet(title);
 		setExcelTitle(excelSheet, workbook);
 		setExcelHeader(excelSheet, workbook);
		setColumnWidth(excelSheet);
		List<E> crecordsList = (List<E>) model.get(listName);
		setExcelRows(excelSheet, workbook, crecordsList);
		
	}

	/**
	 * 设置标题内容及样式
	 * @param excelSheet
	 * @param workbook
	 */
	public void setExcelTitle(HSSFSheet excelSheet, HSSFWorkbook workbook) {
		HSSFRow excelTitleRow = excelSheet.createRow(0);
		for(Integer colIndex: headers.keySet()){
			excelTitleRow.createCell(colIndex);
		}
		//设置行高
		excelTitleRow.setHeight((short)800);
		//合并单元格
		CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, headers.size()-1);
		excelSheet.addMergedRegion(cellRangeAddress);
		//填充标题内容
		HSSFCell cellTitle = excelTitleRow.getCell(0);
		cellTitle.setCellValue(title);
		//设置标题样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex()); //设置背景色
		cellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex()); //设置背景色
		//设置标题字体
		HSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)24);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		cellTitle.setCellStyle(cellStyle);
	}
	
	/**
	 * 设置列名称及样式
	 * @param excelSheet
	 * @param workbook
	 */
	public void setExcelHeader(HSSFSheet excelSheet, HSSFWorkbook workbook) {
		HSSFRow excelHeader = excelSheet.createRow(1);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置居中
		cellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex()); //设置背景色
		cellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex()); //设置背景色
		HSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)16);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		excelHeader.setRowStyle(cellStyle);
		for(Integer colIndex: headers.keySet()){
			HSSFCell headerCell = excelHeader.createCell(colIndex);
			headerCell.setCellStyle(cellStyle);
			headerCell.setCellValue(headers.get(colIndex));
		}
	}
	
	/**
	 * 设置列宽度
	 * @param excelSheet
	 */
	public void setColumnWidth(HSSFSheet excelSheet) {
		for(Integer colIndex: colWidth.keySet()){
			excelSheet.setColumnWidth(colIndex, colWidth.get(colIndex));
		}
	}
}