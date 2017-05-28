package com.chenghui.agriculture.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.chenghui.agriculture.model.Area;

public class AreaUtil{
	public static final Map<String, Set<Area>> areaNameMap = new HashMap<>();
	public static final Map<Long, Area> areaMap = new HashMap<>();

	public static Area getAreaByAreaName(String city, String countyLevelCity, String town, String village){
		if (areaNameMap.isEmpty() || areaMap.isEmpty()) {
			return null;
		}
		if (StringUtils.isNotEmpty(village)) {
			Set<Area> areas = areaNameMap.get(village.trim());
			if (areas!=null && areas.size() > 0) {
				if (areas.size() ==1) {
					return areas.iterator().next();
				}
				
				for (Area area: areas) {
					if (town.equals(areaMap.get(area.getParentCode()).getAreaName())) {
						return area;
					}
				}
			}
		}else if (StringUtils.isNotEmpty(town)) {
			Set<Area> areas = areaNameMap.get(town.trim());
			if (areas!=null && areas.size() > 0) {
				if (areas.size() ==1) {
					return areas.iterator().next();
				}
				
				for (Area area: areas) {
					if (countyLevelCity.equals(areaMap.get(area.getParentCode()).getAreaName())) {
						return area;
					}
				}
			}
		}else if (StringUtils.isNotEmpty(countyLevelCity)) {
			Set<Area> areas = areaNameMap.get(countyLevelCity.trim());
			if (areas!=null && areas.size() > 0) {
				if (areas.size() ==1) {
					return areas.iterator().next();
				}
				
				for (Area area: areas) {
					if (city.equals(areaMap.get(area.getParentCode()).getAreaName())) {
						return area;
					}
				}
			}
		}
		return null;
	}

}
