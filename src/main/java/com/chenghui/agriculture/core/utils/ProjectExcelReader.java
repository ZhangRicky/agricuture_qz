package com.chenghui.agriculture.core.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.model.Constants;
/**
 * poi 读取excel 支持2003 --2007 及以上文件
 *
 * @author sunny
 * @version V 2.0
 * @CreatTime 2013-11-19 @
 */
public class ProjectExcelReader {
	
	public static String ROOT_PATH = "";
	
	//keysRow数据行行号，表示列的英文名称，在数据向数据库写数据时使用
	private static final int keysRowNumber = 0;
	//数据开始行号
	private static final int firstRowNumber = 1;
	//数据开始列号
	private static final int firstColumnNumber = 0;
	//sheet数量
	private static final int SHEET_COUNT = 1;
	
	private static final Map<String, String> headerMap = new HashMap<String, String>();
	
	static{
		headerMap.put("文号", "referenceNumber");
		headerMap.put("项目唯一编号", "projectNumber");
		headerMap.put("资金年度", "fundYear");
		headerMap.put("地州名", "city");
		headerMap.put("县名", "countyLevelCity");
		headerMap.put("乡镇名", "town");
		headerMap.put("村名", "village");
		headerMap.put("专项名称", "subjectName");
		headerMap.put("资金类明细", "fundType");
		headerMap.put("项目名称", "projectName");
		headerMap.put("批复文号", "approvalNumber");
		headerMap.put("总资金(万元)", "totalFund");
		headerMap.put("财政资金(万元)", "financeFund");
		headerMap.put("自筹资金(万元)", "selfFinancing");
		headerMap.put("整合资金(万元)", "integrateFund");
		headerMap.put("扶持农户数(户)", "coveredFarmerNumber");
		headerMap.put("扶持人口数(人)", "coveringNumber");
		headerMap.put("贫困农户", "povertyStrickenFarmerNumber");
		headerMap.put("一般农户", "povertyGeneralFarmer");
//		headerMap.put("贫困农户", "povertyPoorFarmer");
		headerMap.put("低收入困难农户", "povertyLowIncomeFarmer");
		headerMap.put("贫困人口", "povertyStrickenPeopleNumber");
		headerMap.put("一般人口", "povertyGeneralPeople");
//		headerMap.put("贫困人口", "povertyPoorPeople");
		headerMap.put("低收入人口", "povertyLowIncomePeople");
		headerMap.put("财政资金建设规模及内容", "scaleAndContent");
		headerMap.put("实施单位", "carryOutUnit");
		headerMap.put("项目负责人", "chargePerson");
		headerMap.put("完成期限(月)", "deadline");
		headerMap.put("后补文号", "standbyNumber");
		headerMap.put("到县资金(万元)", "fundToCountry");
		headerMap.put("审核状态", "approveState");
		headerMap.put("创建用户ID", "createUser");
		headerMap.put("创建时间", "createTime");
		headerMap.put("录入状态", "inputStatus");
		headerMap.put("当前审核部门", "approvingDepartment");
	}
	
    /**
     * 合并方法，读取excel文件
     * 根据文件名自动识别读取方式
     * 支持97-2013格式的excel文档
     *
     * @param fileName
     *            上传文件名
     * @param file
     *            上传的文件
     * @return 返回列表内容格式：
     *  每一行数据都是以对应列的表头为key 内容为value 比如 excel表格为：
     * ===============
     *  A | B | C | D
     * ===|===|===|===
     *  1 | 2 | 3 | 4
     * ---|---|---|---
     *  a | b | c | d
     * ---------------
     * 返回值 map：
     *   map1:   A:1 B:2 C:3 D:4
     *   map2:   A:a B:b C:d D:d
     * @throws java.io.IOException
     */
    public static List<Map<String, Object>> readExcel(String fileName,MultipartFile file) throws Exception{
        //准备返回值列表
        List<Map<String, Object>> valueList=new ArrayList<Map<String, Object>>();
        String filepathtemp="/mnt/b2b/tmp";//缓存文件目录
        String ExtensionName=getExtensionName(fileName);
        String tmpFileName= System.currentTimeMillis()+"."+ExtensionName;
//      String filepathtemp= ServletActionContext.getServletContext().getRealPath(tempSavePath);//strut获取项目路径
        File filelist = new File(filepathtemp);
        if  (!filelist .exists()  && !filelist .isDirectory())
        {
            filelist .mkdir();
        }
        String filePath = filepathtemp+System.getProperty("file.separator")+tmpFileName;
        File tmpfile = new File(filePath);
        //拷贝文件到服务器缓存目录（在项目下）
//        copy(file,tmpfile);//stuts用的方法
        copy(file, filepathtemp,tmpFileName);//spring mvc用的方法
        //System.out.println("后缀名："+ExtensionName);
        if(ExtensionName.equalsIgnoreCase("xls")){
            valueList=readExcel2003(filePath);
        }else if(ExtensionName.equalsIgnoreCase("xlsx")) {
            valueList=readExcel2007(filePath);
        }
        //删除缓存文件
        tmpfile.delete();
        return valueList;
    }
    /**
     * 读取Excel文件类容到List<Map>对象，支持2003，2007格式
     * @param file
     * @return List<Map>
     * @throws Exception
     */
    public static List<Map<String, Object>> readExcel(File file) throws Exception{
    	//准备返回值列表
    	List<Map<String, Object>> valueList=new ArrayList<Map<String, Object>>();
    	String ExtensionName=getExtensionName(file.getName());
    	if(ExtensionName.equalsIgnoreCase("xls")){
    		valueList=readExcel2003(file);
    	}else if(ExtensionName.equalsIgnoreCase("xlsx")) {
    		valueList=readExcel2007(file);
    	}
    	
    	return valueList;
    }
    
  
    /**
     * 读取97-2003格式
     * @param file
     * @return
     * @throws IOException
     */
	public static List<Map<String, Object>> readExcel2003(File file) throws IOException{
    	return readExcel2003(new FileInputStream(file));
    }

    /**
     * 读取97-2003格式
     * @param filePath
     * @return
     * @throws IOException
     */
	public static List<Map<String, Object>> readExcel2003(String filePath) throws IOException{
    	return readExcel2003(new FileInputStream(filePath));
    }
    /**
     * 读取97-2003格式
     * @param filePath 文件路径
     * @throws java.io.IOException
     */
    public static List<Map<String, Object>> readExcel2003(FileInputStream fis) throws IOException{
        //返回结果集
        List<Map<String, Object>> valueList=new ArrayList<Map<String, Object>>();
        Set<Integer> notNullColumnIndexs = new HashSet<Integer>();
        try {
            HSSFWorkbook wookbook = new HSSFWorkbook(fis);  // 创建对Excel工作簿文件的引用
            for (int sheetIndex = 0; sheetIndex < SHEET_COUNT; sheetIndex++) {
            	HSSFSheet sheet = wookbook.getSheetAt(sheetIndex);   // 在Excel文档中，第一张工作表的缺省索引是0
                int rows = sheet.getLastRowNum(); // 获取到sheet页的最后一行行号­
                Map<Integer,String> keys=new HashMap<Integer, String>();
                int cells=0;
                // 遍历行­（第1行  表头） 准备Map里的key
                HSSFRow firstRow = sheet.getRow(keysRowNumber);
                if (firstRow != null  && firstRow.getPhysicalNumberOfCells() > 0) {
                    // 获取到Excel文件中的所有的列
                    cells = firstRow.getPhysicalNumberOfCells();
                    // 遍历列
                    for (int j = firstRow.getFirstCellNum()+firstColumnNumber; j < cells; j++) {
                        // 获取到列的值­
                        try {
                            HSSFCell cell = firstRow.getCell(j);
                            keys.put(j,headerMap.get(getCellValue(cell)));
                            if ("rowNum".equals(cell.toString()) 
                            		|| "idNumber".equals(cell.toString()) 
                            		|| "creditRule_id".equals(cell.toString())
                            		|| "mrfzValue".equals(cell.toString())) {
								notNullColumnIndexs.add(j);
							}
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                // 遍历行­（从第二行开始）
                for (int i = sheet.getFirstRowNum() + firstRowNumber; i <= rows; i++) {
                    // 读取左上端单元格(从第二行开始)
                    HSSFRow row = sheet.getRow(i);
                    // 行不为空
                    if (row != null && row.getPhysicalNumberOfCells()>0) {
                        //准备当前行 所储存值的map
                        Map<String, Object> val=new HashMap<String, Object>();
                        boolean isValidRow = false;
                        // 遍历列
                        for (int j = row.getFirstCellNum()+firstColumnNumber; j < cells; j++) {
                            // 获取到列的值­
                            try {
                                HSSFCell cell = row.getCell(j);
                                String cellValue = getCellValue(cell);
                                val.put(keys.get(j),cellValue);
                                if(!isValidRow && cellValue!=null && cellValue.trim().length()>0 && !notNullColumnIndexs.contains(j)){
                                    isValidRow = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //第I行所有的列数据读取完毕，放入valuelist
                        if(isValidRow){
                            valueList.add(val);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fis.close();
        }
        return valueList;
    }
    
    /**
     * 读取2007-2013格式
     * @param file
     * @return
     * @throws IOException
     */
	public static List<Map<String, Object>> readExcel2007(File file) throws IOException{
    	return readExcel2007(new FileInputStream(file));
    }
    /**
     * 读取2007-2013格式
     * @param filePath
     * @return
     * @throws IOException
     */
	public static List<Map<String, Object>> readExcel2007(String filePath) throws IOException{
    	return readExcel2007(new FileInputStream(filePath));
    }
    /**
     * 读取2007-2013格式
     * @param filePath 文件路径
     * @return
     * @throws java.io.IOException
     */
    public static List<Map<String, Object>> readExcel2007(FileInputStream fis) throws IOException{
        List<Map<String, Object>> valueList=new ArrayList<Map<String, Object>>();
        Set<Integer> notNullColumnIndexs = new HashSet<Integer>();
        try {
            XSSFWorkbook xwb = new XSSFWorkbook(fis);   // 构造 XSSFWorkbook 对象，strPath 传入文件路径
            for (int sheetIndex = 0; sheetIndex < SHEET_COUNT; sheetIndex++) {
            	XSSFSheet sheet = xwb.getSheetAt(sheetIndex);            // 读取第一章表格内容
                FormulaEvaluator evaluator = xwb.getCreationHelper().createFormulaEvaluator();
                // 定义 row、cell
                XSSFRow row;
                // 循环输出表格中的第一行内容   表头
                Map<Integer, String> keys=new HashMap<Integer, String>();
                row = sheet.getRow(keysRowNumber);
                if(row !=null){
                    for (int j = row.getFirstCellNum()+firstColumnNumber; j <row.getPhysicalNumberOfCells(); j++) {
                        // 通过 row.getCell(j).toString() 获取单元格内容，
                        if(row.getCell(j)!=null){
                            if(!row.getCell(j).toString().isEmpty()){
                                keys.put(j, row.getCell(j).toString());
                                if ("rowNum".equals(row.getCell(j).toString()) 
                                		|| "idNumber".equals(row.getCell(j).toString()) 
                                		|| "creditRule_id".equals(row.getCell(j).toString()) 
                                		|| "mrfzValue".equals(row.getCell(j).toString())) {
    								notNullColumnIndexs.add(j);
    							}
                            }
                        }else{
                            keys.put(j, "K-R1C"+j+"E");
                        }
                    }
                }
                // 循环输出表格中的从第二行开始内容
                for (int i = sheet.getFirstRowNum() + firstRowNumber; i < sheet.getPhysicalNumberOfRows(); i++) {
                    row = sheet.getRow(i);
                    if (row != null) {
                        boolean isValidRow = false;
                        Map<String, Object> val = new HashMap<String, Object>();
                        for (int j = row.getFirstCellNum() + firstColumnNumber; j < row.getPhysicalNumberOfCells(); j++) {
                            XSSFCell cell = row.getCell(j);
                            if (cell != null) {
                                String cellValue = null;
                                switch (cell.getCellType()) {
    							case XSSFCell.CELL_TYPE_NUMERIC:
    								if(DateUtil.isCellDateFormatted(cell)){
                                        cellValue = new DataFormatter().formatRawCellContents(cell.getNumericCellValue(), 0, "yyyy-MM-dd HH:mm:ss");
                                    }
                                    else{
                                        cellValue = String.valueOf(cell.getNumericCellValue());
                                    }
    								break;
    							case XSSFCell.CELL_TYPE_FORMULA:
    								cellValue = evaluator.evaluate(cell).getStringValue();
    								break;
    							default:
    								cellValue = cell.toString();
    								break;
    							}
                                if(cellValue!=null&&cellValue.trim().length()<=0){
                                	if (notNullColumnIndexs.contains(j)) {
    									break;
    								}
                                    cellValue=null;
                                }
                                val.put(keys.get(j), cellValue);
                                if(!isValidRow && cellValue!= null && cellValue.trim().length()>0 && !notNullColumnIndexs.contains(j)){
                                    isValidRow = true;
                                }
                            }
                        }
                        // 第I行所有的列数据读取完毕，放入valuelist
                        if (isValidRow) {
                            valueList.add(val);
                        }
                    }
                }
			}
            
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fis.close();
        }
        return valueList;
    }
    /**
     * 文件操作 获取文件扩展名
     *
     * @Author: sunny
     * @param filename
     *            文件名称包含扩展名
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
    /** -----------上传文件,工具方法--------- */
    private static final int BUFFER_SIZE = 2 * 1024;
    /**
     *
     * @param src
     *            源文件
     * @param dst
     *            目标位置
     */
    @SuppressWarnings("unused")
	private static void copy(File src, File dst) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst),
                    BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 上传copy文件方法(for MultipartFile)
     * @param savePath 在linux上要保存完整路径
     * @param newname 新的文件名称， 采用系统时间做文件名防止中文报错的问题
     * @throws Exception
     */
    public static void copy(MultipartFile file,String savePath,String newname) throws Exception {
        try {
            File targetFile = new File(savePath,newname);
            if (!targetFile.exists()) {
                //判断文件夹是否存在，不存在就创建
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String getCellValue(HSSFCell cell) {
        DecimalFormat df = new DecimalFormat("#");
        String cellValue="";
        if (cell == null)
            return "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellValue=sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                    break;
                }
                cellValue=df.format(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_STRING:
                cellValue=String.valueOf(cell.getStringCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
            	cellValue = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell).getStringValue();
            	if (cellValue == null) {
            		cellValue = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell).getNumberValue() + "";
				}
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                cellValue="";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellValue=String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                cellValue=String.valueOf(cell.getErrorCellValue());
                break;
        }
        if((cellValue!=null&&cellValue.trim().length()<=0) ||"null".equals(cellValue)){
            cellValue="";
        }
        return cellValue;
    }
    public static void main(String[] args) {
    	try {
			List<Map<String, Object>> result = readExcel2007("F:\\20150805_NameList.xlsx");
			for(Map<String, Object> map : result){
				Set<String> keys = map.keySet();
				for(Object key :keys){
					System.out.println(key+":" + map.get(key));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	public static File generateTemplateRules(File templateFile, Constants deptLevel){
		String ExtensionName=getExtensionName(templateFile.getAbsolutePath());
		if(ExtensionName.equalsIgnoreCase("xls")){
			return generateCreditRulesByUser2003(templateFile, deptLevel);
		}else if(ExtensionName.equalsIgnoreCase("xlsx")) {
			return generateCreditRulesByUser2007(templateFile, deptLevel);
		}
		return null;
	}
	private static File generateCreditRulesByUser2003(File templateFile, Constants deptLevel) {
		try {
			File generateTempalteFile = new File(templateFile.getAbsolutePath().replace(".xls", com.chenghui.agriculture.core.utils.DateUtil.getCurrentDateTime2()+".xlsx"));
			String sheetName = "credit_rules";
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templateFile));
			if (workbook.getSheet(sheetName) != null) {//务必确保［credit_rules］页是第4个sheet
				workbook.removeSheetAt(4);
			}
			HSSFSheet sheet0 = workbook.getSheet("守信行为加分项");
			HSSFSheet sheet1 = workbook.getSheet("一票否决信息");
			HSSFSheet sheet2 = workbook.getSheet("失信减分项");
			HSSFSheet sheet4 = workbook.createSheet(sheetName);
			int len0 = 0, len1 = 0, len2 = 0 ;
			String formula0,formula1,formula2;
			sheet4.setColumnWidth(0, 15000);
			sheet4.setColumnWidth(1, 15000);
			sheet4.setColumnWidth(2, 15000);
//			for(CreditRules creditRules : creditRulesList){
//				String rules_content = "";
//				if (creditRules.getXxlxInfo().getCode().equals(Constants.CREDIT_XXLX_CODE_SHOUXINJF)){
//					rules_content = creditRules.getId()+"."+creditRules.getXxnr()+"分值为："+creditRules.getFzValue();
//					HSSFRow xssfRow = sheet4.getRow(len0);
//					if (xssfRow == null) {
//						xssfRow = sheet4.createRow(len0);
//					}
//					len0++;
//					HSSFCell xssfCell = xssfRow.createCell(0,HSSFCell.CELL_TYPE_STRING);
//					xssfCell.setCellValue(rules_content);
//				} else if (creditRules.getXxlxInfo().getCode().equals(Constants.CREDIT_XXLX_CODE_YPFJ)) {
//					rules_content = creditRules.getId()+"."+creditRules.getXxnr();
//					HSSFRow xssfRow = sheet4.getRow(len1);
//					if (xssfRow == null) {
//						xssfRow = sheet4.createRow(len1);
//					}
//					len1++;
//					HSSFCell xssfCell = xssfRow.createCell(1,HSSFCell.CELL_TYPE_STRING);
//					
//					xssfCell.setCellValue(rules_content);
//				} else if (creditRules.getXxlxInfo().getCode().equals(Constants.CREDIT_XXLX_CODE_SHIXINJF)){
//					rules_content = creditRules.getId()+"."+creditRules.getXxnr()+"分值为："+creditRules.getFzValue();
//					HSSFRow xssfRow = sheet4.getRow(len2);
//					if (xssfRow == null) {
//						xssfRow = sheet4.createRow(len2);
//					}
//					len2++;
//					HSSFCell xssfCell = xssfRow.createCell(2,HSSFCell.CELL_TYPE_STRING);
//					xssfCell.setCellValue(rules_content);
//				}
//				
//			}
			formula0 = "="+sheetName+"!$A$1:$A$"+ (len0 == 0 ? 1 : len0);
			formula1 = "="+sheetName+"!$B$1:$B$"+ (len1 == 0 ? 1 : len1);
			formula2 = "="+sheetName+"!$C$1:$C$"+ (len2 == 0 ? 1 : len2);
	        setDataValidationList2003(sheet0, formula0, (short)4,(short)300,(short)3,(short)3);
	        setDataValidationList2003(sheet1, formula1, (short)4,(short)300,(short)3,(short)3);
	        setDataValidationList2003(sheet2, formula2, (short)4,(short)300,(short)3,(short)3);
	        workbook.setSheetHidden(4, true);
			FileOutputStream fileOutputStream = new FileOutputStream(generateTempalteFile);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
			return generateTempalteFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	private static File generateCreditRulesByUser2007(File templateFile, Constants deptLevel) {
		try {
			File generateTempalteFile = new File(templateFile.getAbsolutePath().replace(".xlsx", com.chenghui.agriculture.core.utils.DateUtil.getCurrentDateTime2()+".xlsx"));
			String sheetName = "credit_rules";
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(templateFile));
			if (workbook.getSheet(sheetName) != null) {//务必确保［credit_rules］页是第4个sheet
				workbook.removeSheetAt(4);
			}
			XSSFSheet sheet0 = workbook.getSheet("守信行为加分项");
			XSSFSheet sheet1 = workbook.getSheet("一票否决信息");
			XSSFSheet sheet2 = workbook.getSheet("失信减分项");
			XSSFSheet sheet4 = workbook.createSheet(sheetName);
			int len0 = 0, len1 = 0, len2 = 0 ;
			String formula0,formula1,formula2;
			sheet4.setColumnWidth(0, 15000);
			sheet4.setColumnWidth(1, 15000);
			sheet4.setColumnWidth(2, 15000);
//			for(CreditRules creditRules : creditRulesList){
//				String rules_content = "";
//				if (creditRules.getXxlxInfo().getCode().equals(Constants.CREDIT_XXLX_CODE_SHOUXINJF)){
//					rules_content = creditRules.getId()+"."+creditRules.getXxnr()+"分值为："+creditRules.getFzValue();
//					XSSFRow xssfRow = sheet4.getRow(len0);
//					if (xssfRow == null) {
//						xssfRow = sheet4.createRow(len0);
//					}
//					len0++;
//					XSSFCell xssfCell = xssfRow.createCell(0,XSSFCell.CELL_TYPE_STRING);
//					xssfCell.setCellValue(rules_content);
//				} else if (creditRules.getXxlxInfo().getCode().equals(Constants.CREDIT_XXLX_CODE_YPFJ)) {
//					rules_content = creditRules.getId()+"."+creditRules.getXxnr();
//					XSSFRow xssfRow = sheet4.getRow(len1);
//					if (xssfRow == null) {
//						xssfRow = sheet4.createRow(len1);
//					}
//					len1++;
//					XSSFCell xssfCell = xssfRow.createCell(1,XSSFCell.CELL_TYPE_STRING);
//					
//					xssfCell.setCellValue(rules_content);
//				} else if (creditRules.getXxlxInfo().getCode().equals(Constants.CREDIT_XXLX_CODE_SHIXINJF)){
//					rules_content = creditRules.getId()+"."+creditRules.getXxnr()+"分值为："+creditRules.getFzValue();
//					XSSFRow xssfRow = sheet4.getRow(len2);
//					if (xssfRow == null) {
//						xssfRow = sheet4.createRow(len2);
//					}
//					len2++;
//					XSSFCell xssfCell = xssfRow.createCell(2,XSSFCell.CELL_TYPE_STRING);
//					xssfCell.setCellValue(rules_content);
//				}
//				
//			}
			formula0 = "="+sheetName+"!$A$1:$A$"+ (len0 == 0 ? 1 : len0);
			formula1 = "="+sheetName+"!$B$1:$B$"+ (len1 == 0 ? 1 : len1);
			formula2 = "="+sheetName+"!$C$1:$C$"+ (len2 == 0 ? 1 : len2);
	        setDataValidationList2007(sheet0, formula0, (short)4,(short)300,(short)3,(short)3);
	        setDataValidationList2007(sheet1, formula1, (short)4,(short)300,(short)3,(short)3);
	        setDataValidationList2007(sheet2, formula2, (short)4,(short)300,(short)3,(short)3);
	        workbook.setSheetHidden(4, true);
			FileOutputStream fileOutputStream = new FileOutputStream(generateTempalteFile);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
			return generateTempalteFile;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	private static void setDataValidationList2003(HSSFSheet sheet, String formula, short firstRow, short endRow, short firstCol, short endCol) {
		// 加载下拉列表内容
//		DVConstraint constraint = DVConstraint.createExplicitListConstraint((String[]) dropdownList.toArray(new String[dropdownList.size()]));
		DVConstraint constraint = DVConstraint.createFormulaListConstraint(formula);
		// 设置数据有效性加载在哪个单元格上。

		// 四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,  endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);

		sheet.addValidationData(data_validation_list);
	}
	
	private static void setDataValidationList2007(XSSFSheet sheet, String formula, short firstRow, short endRow,short firstCol, short endCol) {
//		= "=$A$1:$A$"+ len;
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,  endRow, firstCol, endCol);
		XSSFDataValidationHelper targetHelp = new XSSFDataValidationHelper(sheet);
		XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint)targetHelp.createFormulaListConstraint(formula);
		XSSFDataValidation validation = (XSSFDataValidation) targetHelp.createValidation(constraint, regions);
		sheet.addValidationData(validation);
	}

}
