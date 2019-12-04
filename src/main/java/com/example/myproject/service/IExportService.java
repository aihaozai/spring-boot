package com.example.myproject.service;

import java.util.List;
import java.util.Map;

public interface IExportService {
    List<Map<String, Object>> find (String tableName);
}
