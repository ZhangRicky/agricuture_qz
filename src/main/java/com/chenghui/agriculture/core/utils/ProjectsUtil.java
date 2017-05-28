package com.chenghui.agriculture.core.utils;

import java.util.Calendar;

public class ProjectsUtil {

	/**
	 * 自动生成项目唯一编号
	 * @param id
	 * @return
	 */
    public static String getProjectNumberNo(Long id){
        Calendar cal = Calendar.getInstance();
        String code = "00000" + id;
        return cal.get(Calendar.YEAR) + code.substring(code.length() - 5);
    }
}
