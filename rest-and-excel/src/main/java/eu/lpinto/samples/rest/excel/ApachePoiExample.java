package eu.lpinto.samples.rest.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This example code shows how to handle a xslx file.
 * This concrete example shows how to open print and save a new file.
 *
 * Example based on Viral Patel post @ http://viralpatel.net/blogs/java-read-write-excel-file-apache-poi/comment-page-1/
 *
 * @author Luís Pinto <code>mail@lpinto.eu</code>
 */
public class ApachePoiExample {

    public static void main(final String[] args) throws FileNotFoundException, IOException {
        try {

            FileInputStream file = new FileInputStream(new File("Book1.xlsx"));


            XSSFWorkbook workbook = new XSSFWorkbook(file); //Get the workbook instance for XLS file
            XSSFSheet sheet = workbook.getSheetAt(0);       //Get first sheet from the workbook

            /* Iterate through each rows from first sheet */
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                /* For each row, iterate through each columns */
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t");
                            break;
                    }
                }
                System.out.println("");
            }
            file.close();

            FileOutputStream out = new FileOutputStream(new File("Book2.xlsx"));
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
