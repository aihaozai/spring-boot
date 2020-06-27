package com.example.myproject.service.business.impl;

import com.example.myproject.common.annotation.TargetDataSource;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.entity.User;
import com.example.myproject.service.IExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-28 11:33
 **/
@Service
public class ExportServiceImpl implements IExportService {
    @Autowired
    private AllDao.MenuRoleDao menuRoleDao;

    @TargetDataSource(name = "orcl")
    @Override
    public List<Map<String, Object>> find(String tableName) {
        String sql ="select" +
                " ut.COLUMN_NAME COLUMN_NAME," +
                "（Concat(Concat( ut.DATA_TYPE,'('),Concat( ut.DATA_LENGTH,')'))) DATA_TYPE," +
                "      case ut.NULLABLE" +
                "        when 'Y' then  cast( '是' as varchar2(3))" +
                "           when 'N' then cast( '否' as varchar2(3))" +
                "             end NULLABLE ," +
                "               uc.COMMENTS COMMENTS" +
                " from user_tab_columns  ut" +
                " inner JOIN user_col_comments uc" +
                " on ut.TABLE_NAME  = uc.table_name and ut.COLUMN_NAME = uc.column_name and ut.TABLE_NAME='"+tableName+"'" +
                " order by ut.Table_Name";
        return menuRoleDao.getListMapBySQLQuery(sql);
    }
}
