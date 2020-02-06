package com.example.myproject.common.utils;

import com.example.myproject.entity.Organ;
import com.example.myproject.common.pojo.SelectTree;
import com.example.myproject.common.pojo.SelectTreeNoChildren;

import java.util.ArrayList;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-01 12:06
 **/
public class SelectTreeUtil {
    public static String Pid = "0";
    protected SelectTreeUtil() {}
    public static List buildTree(String organId,List<Organ> organs){
        List selectTreeList = new ArrayList<>();
        for (Organ organ : organs){
            if(organ.getPid().equals(organId)) {
                List<Organ> organList = new ArrayList<>(organs);
                organList.remove(organ);
                List list = buildTree(organ.getOrganId(),organList);
                if(list.size()>0){
                    selectTreeList.add(getSelectTree(organ,list));
                }else {
                    selectTreeList.add(getSelectTreeNoChildren(organ));
                }
            }
        }
        return selectTreeList;
    }

    private static SelectTree getSelectTree(Organ organ,List<SelectTree> list){
        SelectTree selectTree = new SelectTree();
        selectTree.setId(organ.getOrganId());
        selectTree.setName(organ.getOrganName());
        selectTree.setChildren(list);
        return  selectTree;
    }
    private static SelectTreeNoChildren getSelectTreeNoChildren(Organ organ){
        SelectTreeNoChildren selectTree = new SelectTreeNoChildren();
        selectTree.setId(organ.getOrganId());
        selectTree.setName(organ.getOrganName());
        return  selectTree;
    }
}
