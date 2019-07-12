package cn.tonghua.service.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * excel转换listMap
 */
public class ExcelToListMap {

    public static List<Map<String, String>> analysis(InputStream inputStream, List<TableTitle> tableTitles) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<Integer, TableTitle> titleIndexMap = getIntegerStringMap(tableTitles, sheet);
        for (Row rowContent : sheet) {
            Map contentMap = rowToMap(rowContent, titleIndexMap);
            mapList.add(contentMap);
        }
        return mapList;
    }

    /**
     * 表头转换
     *
     * @param tableTitles
     * @param sheet
     * @return
     */
    private static Map<Integer, TableTitle> getIntegerStringMap(List<TableTitle> tableTitles, Sheet sheet) {
        Map<Integer, TableTitle> titleIndexMap = new HashMap<>();
        Map<String, TableTitle> tableTitleMap = new HashMap<>();
        for (TableTitle tableTitle : tableTitles) {
            tableTitleMap.put(tableTitle.excelTitle, tableTitle);
        }
        Row row = sheet.getRow(0);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String title = row.getCell(i).getStringCellValue();
            if (title == null || title.length() <= 0) {
                continue;
            }
            TableTitle tableTitle = tableTitleMap.get(title);
            if (tableTitle == null) {
                throw new RuntimeException("无效的表头【" + title + "】");
            }
            titleIndexMap.put(i, tableTitle);
        }
        sheet.removeRow(row);
        return titleIndexMap;
    }

    /**
     * 行转map
     *
     * @param row
     * @param titleIndexMap
     * @return
     */
    private static final Map<String, String> rowToMap(Row row, Map<Integer, TableTitle> titleIndexMap) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String content = cell.getStringCellValue();
            TableTitle tableTitle = titleIndexMap.get(i);
            if (tableTitle == null) {
                continue;
            }
            String key = tableTitle.getToTitle();
            try {
                map.put(key, tableTitle.getVal(content));
            } catch (Exception e) {
                throw new RuntimeException("在第【" + (row.getRowNum() + 1) + "】行，第【" + (i + 1) + "】列," + e.getMessage());
            }
        }
        return map;
    }

    public static class TableTitle {
        private String excelTitle;
        private String toTitle;
        private Map<String, String> translation;

        public TableTitle(String excelTitle, String toTitle, String[] keyVals) {
            this.excelTitle = excelTitle;
            this.toTitle = toTitle;
            this.translation = new HashMap<>();
            for (String keyVal : keyVals) {
                translation.put(keyVal.split(":")[0], keyVal.split(":")[1]);
            }
        }

        public TableTitle(String excelTitle, String toTitle, Map<String, String> translation) {
            this.excelTitle = excelTitle;
            this.toTitle = toTitle;
            this.translation = translation;
        }

        public TableTitle(String excelTitle, String toTitle) {
            this.excelTitle = excelTitle;
            this.toTitle = toTitle;
        }

        public TableTitle() {
        }

        public String getExcelTitle() {
            return excelTitle;
        }

        public void setExcelTitle(String excelTitle) {
            this.excelTitle = excelTitle;
        }

        public String getToTitle() {
            return toTitle;
        }

        public void setToTitle(String toTitle) {
            this.toTitle = toTitle;
        }

        public Map<String, String> getTranslation() {
            return translation;
        }

        public void setTranslation(Map<String, String> translation) {
            this.translation = translation;
        }

        public String getVal(String key) {
            if (translation == null) {
                return key;
            }
            String val = translation.get(key);
            if (val == null) {
                throw new RuntimeException("无效的【" + key + "】");
            }
            return val;
        }
    }

}
