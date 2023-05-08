package com.csrcb.design.esquery;

import java.util.List;
import java.util.Map;

public class EsSqlResult {
    private List<Map<String, String>> columns;
    private List<List<Object>> rows;
    private String cursor;

    public List<Map<String, String>> getColumns() {
        return columns;
    }

    public void setColumns(List<Map<String, String>> columns) {
        this.columns = columns;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
