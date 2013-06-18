package eu.lpinto.samples.rest.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * Based on both examples provided,
 * this class shows how to receive a xlsx file from a REST service, and turn into Java data.
 *
 * @author Luís Pinto <code>mail@lpinto.eu</code>
 */
@Path("excel")
public class UploadExcelFile {

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {

        String fileName = "";

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        for (InputPart inputPart : inputParts) {

            MultivaluedMap<String, String> header = inputPart.getHeaders();
            fileName = getFileName(header);

            try {
                XSSFWorkbook workbook;
                try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {
                    workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = workbook.getSheetAt(0);
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
                }

                FileOutputStream out = new FileOutputStream(new File("/home/lpinto/Book2.xlsx"));
                workbook.write(out);
                out.close();



            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }

        return Response.status(200)
                .entity("uploadFile is called, Uploaded file name : " + fileName).build();

    }

    /**
     * header sample
     * {
     * Content-Type=[image/png],
     * Content-Disposition=[form-data; name="file"; filename="filename.extension"]
     * }
     * */
    //get uploaded filename, is there a easy way in RESTEasy?
    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }
}
