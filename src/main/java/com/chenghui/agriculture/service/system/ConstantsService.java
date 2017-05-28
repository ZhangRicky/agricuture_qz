package com.chenghui.agriculture.service.system;

import java.util.List;

import com.chenghui.agriculture.model.Constants;
import com.chenghui.agriculture.service.GenericService;

public interface ConstantsService extends GenericService<Constants, Long> {
	List<Constants> getXxlx();
	List<Constants> getXxydw();
	List<Constants> getJmlx();
	
	List<Constants> getXxlxType();	//类型查询
	void updateConstants(Constants constants);
	void updateJuminConstants(Constants constants);
	void updateXxDanWeiConstants(Constants constants);
	Long addConstants(Constants constants);
	Long addJuminConstants(Constants constants);
	Long addXxDanWeiConstants(Constants constants);
	List<Constants> getJuminglxType();
	void resourceConstants(Long constants_id);
	void removeConstants(Long constants_id);
	void removeXxDanWeiConstants(Long constants_id);
	void removeJuminConstants(Long constants_id);
	List<Constants> getXxDanWeiType();
}
