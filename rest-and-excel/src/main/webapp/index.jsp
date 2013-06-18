<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>

<!-- Based on MKyong example @ http://www.mkyong.com/webservices/jax-rs/file-upload-example-in-resteasy/  -->
<html>
    <body>
        <h1>JAX-RS Upload Form</h1>

        <!-- NOTE: this form assumes that this project runs on port 8080 under rest-and-excel path -->
        <form action="http://localhost:8080/rest-and-excel/excel/upload" method="post" enctype="multipart/form-data">

            <p>
                Select a file : <input type="file" name="uploadedFile" size="50" />
            </p>

            <input type="submit" value="Upload It" />
        </form>

    </body>
</html>
