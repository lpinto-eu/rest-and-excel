package eu.lpinto.samples.rest.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * This service provides an operation that receives a file throw a REST service,
 *
 * Example based on MKyong example: http://www.mkyong.com/webservices/jax-rs/file-upload-example-in-resteasy/
 *
 * @author Luís Pinto <code>mail@lpinto.eu</code>
 */
@Path("file")
public class UploadFileService {

    private static final String FILES_PATH = "/tmp/files";

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    public Response uploadFile(final MultipartFormDataInput input) {

        String fileName = "";

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        for (InputPart inputPart : inputParts) {

            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();

                /*  */
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);

                /*
                 * Save file
                 */
                fileName = getFileName(header);
                fileName = FILES_PATH + fileName;
                writeFile(bytes, fileName);

                System.out.println("Faved file " + fileName + " on " + FILES_PATH);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }

        return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();

    }

    /**
     * Extracts the file name from a headers Map.
     *
     * <p><code>header sample
     * {
     * Content-Type=[image/png],
     * Content-Disposition=[form-data; name="file"; filename="filename.extension"]
     * }
     */
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

    /**
     * Saves a byte[] to filesystem.
     *
     * @param content  The file binary to save.
     * @param filename File that will be written.
     *
     * @throws IOException if file cannot be written.
     */
    private void writeFile(final byte[] content, final String filename) throws IOException {
        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileOutputStream outStrean = new FileOutputStream(file)) {
            outStrean.write(content);
            outStrean.flush();
        }

    }
}
