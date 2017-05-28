package com.chenghui.agriculture.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Department;
import com.chenghui.agriculture.model.tree.TreeNode;

public class TreeUtil{

	public static List<TreeNode> buildTreeNodeList(List<TreeNode> treeNodeList){
		List<TreeNode> buildTreeNodeList = new ArrayList<TreeNode>();
		
		Map<Long, TreeNode> allIdMap = new LinkedHashMap<Long, TreeNode>();
		for(TreeNode node: treeNodeList){
			allIdMap.put(node.getId(), node);
		}
		
		treeNodeList = null;
		Set<Entry<Long, TreeNode>> entrySet = allIdMap.entrySet();
		for(Entry<Long, TreeNode> entry : entrySet){
			Long pid = entry.getValue().getPid();
			TreeNode node = allIdMap.get(pid);
			if(pid == null || node == null){
				entry.getValue().setExpanded(true);
				buildTreeNodeList.add(entry.getValue());
			}else {
				List<TreeNode> children = node.getItems();
				if(children == null){
					children = new ArrayList<TreeNode>();
					node.setItems(children);
					node.setHasChildren(true);
				}
				if(entry.getValue().isChecked()){
					node.setExpanded(true);
				}
				children.add(entry.getValue());
			}
		}
		allIdMap = null;
		return buildTreeNodeList;
	}
	
	public static List<TreeNode> convertTreeNodeList(List<Department> departmentList){
		List<TreeNode> treeNodeList = null;
		if(departmentList != null && departmentList.size() >0){
			treeNodeList = new ArrayList<TreeNode>();
			for(Department dept: departmentList){
				TreeNode treeNode = convertTreeNode(dept);
				if(treeNode != null){
					treeNodeList.add(treeNode);
				}
			}
		}
		return treeNodeList;
	}
	
	/**
	 * 绑定地区管理
	 * @param departmentList
	 * @return
	 */
	public static List<TreeNode> convertTreeNodeLists(List<Area> areaList){
		List<TreeNode> treeNodeList = null;
		if(areaList != null && areaList.size() >0){
			treeNodeList = new ArrayList<TreeNode>();
			for(Area area: areaList){
				TreeNode treeNode = convertTreeNodes(area);
				if(treeNode != null){
					treeNodeList.add(treeNode);
				}
			}
		}
		return treeNodeList;
	}
	
	private static TreeNode convertTreeNode(Department dept){
		TreeNode treeNode = null;
		if(dept != null){
			treeNode = new TreeNode();
			treeNode.setId(dept.getDeptId());
			treeNode.setText(dept.getDeptName());
			treeNode.setPid(dept.getParentCode());
			treeNode.setChecked(dept.isInUse());
			treeNode.setHasChildren(false);
			
			Map<String, Object> map = new HashMap<String, Object>();
			treeNode.setAttributes(map);
		}
		return treeNode;
	}

	/**
	 * 绑定地区
	 * @param dept
	 * @return
	 */
	private static TreeNode convertTreeNodes(Area area){
		TreeNode treeNode = null;
		if(area != null){
			treeNode = new TreeNode();
			treeNode.setId(area.getId());
			treeNode.setText(area.getAreaName());
			treeNode.setPid(area.getParentCode());
			treeNode.setChecked(area.isInUse());
			treeNode.setHasChildren(false);
			
			Map<String, Object> map = new HashMap<String, Object>();
			treeNode.setAttributes(map);
		}
		return treeNode;
	}
	
}
