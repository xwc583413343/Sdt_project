import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
public class GetExcelInfo {
    /*
    public static void main(String[] args) {
        GetExcelInfo obj = new GetExcelInfo();
        // 此处为我创建Excel路径：E:/zhanhj/studysrc/jxl下
        File file = new File("/Users/weichaoxie/IdeaProjects/Sdt_project/test.xls");
        obj.readExcel(file);
    }
    */
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public static Vector<Point> readExcel(File file) {
        Vector<Point> datas=new Vector<>();
        try {

            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 1; i < sheet.getRows(); i++) {
                    // sheet.getColumns()返回该页的总列数
                    String cellinfo=sheet.getCell(8,i).getContents();
                    //System.out.println(cellinfo);
                    double tempData=Double.parseDouble(cellinfo);
                    datas.add(new Point(i,tempData));
                    /*
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        System.out.println(cellinfo);
                    }
                    */
                }
            }
            return datas;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datas;
    }
}
