package com.example.myproject.controller.sys;


import com.example.myproject.common.Helper.ExportHelper;
import com.example.myproject.common.Helper.JasperHelper;
import com.example.myproject.service.sys.IExportService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JMSCADMIN on 2019/5/29.
 */
@Controller
@RequestMapping("export")
public class ExportController   {
    @Autowired
    private IExportService exportServiceImpl;

    @Autowired
    private ExportHelper exportUtil;

    @RequestMapping(value = "/exp", method = RequestMethod.GET)
    @ResponseBody
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        String fff = request.getParameter("name");
        System.out.println(fff);
        Map<String, String> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        map.put("name", "gggg");
        map1.put("name", "gggg1");
        list.add(map);
        list.add(map1);
        exportUtil.expRecords(request, response, list, "demo.xls", "wwww");
        return "OK";
    }

    @RequestMapping(value = "/expPdf", method = RequestMethod.GET)
    @ResponseBody
    public String exportPdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = "ACTIVITY";
        List<Map<String, Object>> mapList = exportServiceImpl.find(tableName);
        List list = new ArrayList();
        list.add("temp");
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(mapList);
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("tableName", tableName+"表");
        String path = new ClassPathResource("export/jasper/"+"database.jasper").getFile().getPath();
        JasperHelper.showPdf("导出测试", path, request, response, objectHashMap, jrDataSource);
        return "OK";
    }


}
