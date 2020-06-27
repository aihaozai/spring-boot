package com.example.myproject.common.utils;

import com.example.myproject.common.pojo.MenuTree;
import com.example.myproject.entity.sys.view.PersonMenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-11 15:23
 **/
public class MenuTreeUtil {
    public static String Pid = "0";
    protected MenuTreeUtil() {

    }
    public static List<MenuTree> buildMenuTree(String Pid,List<PersonMenuView> personMenuViews){
        List<MenuTree> menuTreeList = new ArrayList<>();
        for (PersonMenuView view : personMenuViews){
            if(view.getPid().equals(Pid)) {
                MenuTree newMenuTree = getMenu(view);
                List<PersonMenuView> pmv = new ArrayList<>(personMenuViews);
                pmv.remove(view);
                newMenuTree.setChildrens(buildMenuTree(newMenuTree.getMenuId(),pmv));
                menuTreeList.add(newMenuTree);
            }
        }
        return menuTreeList;
    }

    private static List<MenuTree> buildChildMenuTree(MenuTree menuTree, List<PersonMenuView> personMenuViews){
        List<MenuTree> menuTreeList =new ArrayList<>();
        for (PersonMenuView view : personMenuViews){
            if(menuTree.getMenuId().equals(view.getPid())) {
                MenuTree newMenuTree = getMenu(view);
                List<PersonMenuView> pmv = new ArrayList<>(personMenuViews);
                pmv.remove(view);
                newMenuTree.setChildrens(buildChildMenuTree(newMenuTree,pmv));
                menuTreeList.add(newMenuTree);
            }
        }
        return menuTreeList;
    }

    private static MenuTree getMenu(PersonMenuView view){
        MenuTree menuTree = new MenuTree();
        menuTree.setId(view.getId());
        menuTree.setPid(view.getPid());
        menuTree.setMenuId(view.getMenuId());
        menuTree.setMenuName(view.getMenuName());
        menuTree.setMenuType(view.getMenuType());
        menuTree.setMenuUrl(view.getMenuUrl());
        menuTree.setMenuIcon(view.getMenuIcon());
        menuTree.setSort(view.getSort());
        return  menuTree;
    }
    //去掉重复菜单
    public static List<PersonMenuView> distinctMenuTree(List<PersonMenuView> personMenuViews){
        List<PersonMenuView> personMenuViewList = new ArrayList<>();
        List<String> menuIdList =new ArrayList<>();
        for (PersonMenuView pmv:personMenuViews){
            if(!menuIdList.contains(pmv.getMenuId())) {
                for (PersonMenuView pmv2 : personMenuViews) {
                    if (pmv.getMenuId().equals(pmv2.getMenuId())&&!pmv.getId().equals(pmv2.getId())) {
                        personMenuViewList.add(pmv2);
                        menuIdList.add(pmv2.getMenuId());
                    }
                }
            }
        }
        personMenuViews.removeAll(personMenuViewList);
        return personMenuViews;
    }
}
