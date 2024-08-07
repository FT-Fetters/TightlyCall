package xyz.ldqc.tightcall.protocol.http.support.multipart;

import java.io.InputStream;

/**
 * @author Fetters
 */
public class MultipartFile extends AbstractMultipartContent {

    private InputStream inputStream;

    private String fileName;

    private String contentType;

    private long size;

    public MultipartFile(String name, InputStream inputStream, String fileName, String contentType, long size) {
        super(name);
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
