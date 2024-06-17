package xyz.ldqc.tightcall.protocol.http;

import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.exception.HttpNioResponseException;
import xyz.ldqc.tightcall.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

    /**
 * @author Fetters
 */
public class HttpNioResponse {

    private final String version;

    private final Integer code;

    private final String msg;

    private final Map<String, String> header;

    private byte[] body;

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    private HttpNioResponse(String version, Integer code,
                            String msg, Map<String, String> header,
                            byte[] body) {
        this.version = version;
        this.code = code;
        this.msg = msg;
        this.header = header;
        this.body = body;
    }

    public String toResponseString() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder
                .append(getStatusLine())
                .append(getHeaderLies())
                .append("\r\n")
                .append(new String(body));
        return responseBuilder.toString();
    }

    public byte[] toResponseBytes() {
        SimpleByteData byteData = new SimpleByteData();
        byteData.writeBytes(getStatusLine().getBytes());
        byteData.writeBytes(getHeaderLies().getBytes());
        byteData.writeBytes("\r\n".getBytes());
        if (body != null) {
            byteData.writeBytes(body);
        }
        return byteData.readBytes();
    }

    private String getStatusLine() {
        return version + " " + code +
                (StringUtil.isNotBlank(msg) ? (" " + msg) : "") +
                "\r\n";
    }

    private String getHeaderLies() {
        StringBuilder headerBuilder = new StringBuilder();
        for (String key : header.keySet()) {
            String value = header.get(key);
            headerBuilder.append(key).append(":").append(value)
                    .append("\r\n");
        }

        return headerBuilder.toString();
    }

    public void write(String content){
        this.write(content.getBytes());
    }

    public void write(byte[] content){
        SimpleByteData byteData = new SimpleByteData(body);
        byteData.writeBytes(content);
        this.body = byteData.readBytes();
        if (this.body != null) {
            this.header.put(ResponseHeaderEnum.CONTENT_LENGTH.getKey(), String.valueOf(this.body.length));
        }
    }

    public static class ResponseBuilder {

        private HttpVersionEnum version;

        private Integer code;

        private String msg;

        private Map<String, String> header;

        private byte[] body;


        public ResponseBuilder version(HttpVersionEnum version) {
            this.version = version;
            return this;
        }

        public ResponseBuilder code(int code) {
            this.code = code;
            return this;
        }

        public ResponseBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public ResponseBuilder addHeader(String key, String value) {
            if (this.header == null) {
                header = new HashMap<>(16);
            }
            header.put(key, value);
            return this;
        }

        public ResponseBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public ResponseBuilder contentType(ContentTypeEnum type) {
            addHeader(ResponseHeaderEnum.CONTENT_TYPE.getKey(), type.getValue());
            return this;
        }

        public HttpNioResponse build() {
            buildCheck();
            appendDefaultHeader();
            return doBuild();
        }

        private HttpNioResponse doBuild() {
            return new HttpNioResponse(version.getValue(), code, msg, header, body);
        }

        private void appendDefaultHeader() {
            if (header == null) {
                header = new HashMap<>(16);
            }
            // 内容长度
            if (body != null && body.length > 0) {
                header.put(ResponseHeaderEnum.CONTENT_LENGTH.getKey(),
                        String.valueOf(body.length)
                );
            }
            // 默认内容类型
            header.putIfAbsent(ResponseHeaderEnum.CONTENT_TYPE.getKey(), "text/plain");
        }

        /**
         * 构建检查
         */
        private void buildCheck() {
            if (StringUtil.isBlank(version.toString())) {
                throw new HttpNioResponseException("version cannot be empty");
            }

            if (code == null) {
                throw new HttpNioResponseException("code cannot be null");
            }
        }
    }

}
