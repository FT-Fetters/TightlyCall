package xyz.ldqc.tightcall.protocol.http;

import com.alibaba.fastjson2.JSON;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xyz.ldqc.tightcall.protocol.http.support.multipart.AbstractMultipartContent;
import xyz.ldqc.tightcall.protocol.http.support.multipart.MultipartFile;
import xyz.ldqc.tightcall.util.StringUtil;

/**
 * @author Fetters
 */
public class HttpNioRequest {

    private static final byte[] CRLF = "\r\n".getBytes(StandardCharsets.UTF_8);

    private String method;
    private HttpMethodEnum definedMethod;
    private URI uri;
    private String protocol;
    private Map<String, String> headers;
    private String body;
    private String connectIp;
    private int connectPort;
    private Map<String, AbstractMultipartContent> multipartContentMap;

    private Map<String, String> param;

    public static HttpNioRequest parse(String httpStr) {
        HttpNioRequest httpNioRequest = new HttpNioRequest();

        String[] lines = httpStr.split("\r\n");

        // 解析请求行
        parseRequestLine(httpNioRequest, lines[0]);

        // 解析请求头
        parseHeader(httpNioRequest, lines);

        // 解析url参数
        parseParam(httpNioRequest);

        return httpNioRequest;
    }

    public static HttpNioRequest parse(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        HttpNioRequest httpNioRequest = new HttpNioRequest();
        int l = 0;
        // 解析请求行
        l = findSequence(data, 0, CRLF);
        if (l == -1) {
            return null;
        }
        String firstLine = new String(data, 0, l);
        parseRequestLine(httpNioRequest, firstLine);

        // 解析请求头
        while (true) {
            int i = findSequence(data, l + 1, CRLF);
            if (i - l == 2) {
                // 找到空行，请求头结束
                l = i;
                break;
            }
            String line = new String(data, l + CRLF.length, i - l - CRLF.length);
            doParseHeader(httpNioRequest, line);
            l = i;
        }
        // 解析url参数
        parseParam(httpNioRequest);
        // 解析剩余请求体数据
        if (l + 2 >= data.length) {
            return httpNioRequest;
        }
        handleBodyData(httpNioRequest, data, l);
        return httpNioRequest;
    }

    private static void handleBodyData(HttpNioRequest request, byte[] data, int l) {
        Map<String, String> headersMap = request.getHeaders();
        String contentType = headersMap.get(ResponseHeaderEnum.CONTENT_TYPE.toString());
        String defaultBody = new String(data, l, data.length - l);
        if (StringUtil.isBlank(contentType)) {
            request.body = defaultBody;
            return;
        }
        if (contentType.contains(ContentTypeEnum.MULTIPART_FORM_DATA.getValue())) {
            handleMultipartFormDataBody(request, data, l, contentType);
        } else {
            request.body = defaultBody;
        }
    }

    private static class MultipartDefined {

        private String disposition;

        private Map<String, String> dispositionMap;

        private String type;

        private byte[] data;

        private int dataLeft;

        private int dataRight;

        private int headerEnd;
    }

    private static void handleMultipartFormDataBody(HttpNioRequest request, byte[] data, int l, String contentType) {
        String boundary = "--" + contentType.substring(contentType.indexOf("boundary=") + 9);
        List<Integer> boundaryIndexes = new ArrayList<>();
        while (true) {
            int index = findSequence(data, l, boundary.getBytes());
            if (index == -1) {
                break;
            }
            boundaryIndexes.add(index);
            l = index + boundary.length();
        }
        for (int i = 0; i < boundaryIndexes.size(); i++) {
            int left = boundaryIndexes.get(i) + boundary.length();
            if (i + 1 > boundaryIndexes.size() - 1) {
                break;
            }
            int right = boundaryIndexes.get(i + 1);
            MultipartDefined multipartDefined = new MultipartDefined();
            multipartDefined.data = data;
            multipartDefined.dataLeft = left;
            multipartDefined.dataRight = right;
            handleMultipartFormSingle(request, multipartDefined);
        }
    }

    private static void handleMultipartFormSingle(HttpNioRequest request, MultipartDefined multipartDefined) {
        Map<String, String> partHeaders = new HashMap<>(4);
        int headerEnd = 0;
        int l = multipartDefined.dataLeft;
        while (l < multipartDefined.dataRight) {
            int index = findSequence(multipartDefined.data, l + CRLF.length, CRLF);
            if (index == -1) {
                break;
            }
            String line = new String(multipartDefined.data, l + CRLF.length, index - l - CRLF.length);
            l = index;
            if (line.trim().isEmpty()) {
                headerEnd = index + CRLF.length;
                break;
            }
            String[] lineSplit = line.split(":");
            if (lineSplit.length == 2) {
                partHeaders.put(lineSplit[0], lineSplit[1]);
            }
        }
        multipartDefined.headerEnd = headerEnd;
        if (partHeaders.containsKey(ResponseHeaderEnum.CONTENT_DISPOSITION.toString())) {
            multipartDefined.disposition = partHeaders.get(ResponseHeaderEnum.CONTENT_DISPOSITION.toString());
        }
        if (partHeaders.containsKey(ResponseHeaderEnum.CONTENT_TYPE.toString())) {
            multipartDefined.type = partHeaders.get(ResponseHeaderEnum.CONTENT_TYPE.toString());
        }

        multipartDefined.dispositionMap = getDispositionMap(multipartDefined.disposition);

        if (isMultipartFile(multipartDefined)){
            handleMultipartFile(request, multipartDefined);
            return;
        }
    }

    private static Map<String, String> getDispositionMap(String disposition) {
        Map<String, String> map = new HashMap<>(4);
        String[] split = disposition.split(";");
        for (String s : split) {
            String[] kv = s.split("=");
            if (kv.length == 2) {
                String value = kv[1].trim();
                if (value.startsWith("\"") && value.endsWith("\"")){
                    value = value.substring(1, value.length() - 1);
                }
                map.put(kv[0].trim(), value);
            }
        }
        return map;
    }

    private static boolean isMultipartFile(MultipartDefined multipartDefined) {
        return multipartDefined.dispositionMap.containsKey("filename");
    }

    private static void handleMultipartFile(HttpNioRequest request, MultipartDefined multipartDefined){
        Map<String, String> dispositionMap = multipartDefined.dispositionMap;
        String multipartName = dispositionMap.get("name");
        String fileName = dispositionMap.get("filename");
        int size =multipartDefined.dataRight - multipartDefined.headerEnd - CRLF.length;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(multipartDefined.data,
            multipartDefined.headerEnd, size);
        MultipartFile multipartFile = new MultipartFile(
            multipartName, inputStream, fileName, multipartDefined.type, size);
        if (request.multipartContentMap == null) {
            request.multipartContentMap = new HashMap<>(16);
        }
        request.multipartContentMap.put(multipartName, multipartFile);
    }

    private static int findSequence(byte[] buffer, int offset, byte[] sequence) {
        for (int i = offset; i <= buffer.length - sequence.length; i++) {
            boolean found = true;
            for (int j = 0; j < sequence.length; j++) {
                if (buffer[i + j] != sequence[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 解析请求行
     */
    private static void parseRequestLine(HttpNioRequest request, String firstLine) {
        String[] requestLineParts = firstLine.split(" ");
        request.method = requestLineParts[0];
        request.definedMethod = HttpMethodEnum.parse(request.method);
        request.uri = URI.create(requestLineParts[1]);
        request.protocol = requestLineParts[2];
    }

    private static void parseParam(HttpNioRequest request) {
        String query = request.uri.getQuery();
        request.param = new HashMap<>(16);
        if (query == null) {
            return;
        }
        String[] params = query.split("&");
        for (String param : params) {
            String[] kv = param.split("=");
            String k = kv[0];
            String v;
            if (kv.length == 1) {
                v = "";
            } else {
                v = kv[1];
            }
            request.param.put(k, v);
        }
    }

    private static void parseHeader(HttpNioRequest request, String[] lines) {
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().isEmpty()) {
                // 空行之后的内容为请求体
                StringBuilder bodyBuilder = new StringBuilder();
                for (int j = i + 1; j < lines.length; j++) {
                    bodyBuilder.append(lines[j]).append("\r\n");
                }
                request.body = bodyBuilder.toString().trim();
                break;
            } else {
                doParseHeader(request, line);
            }
        }
    }

    private static void doParseHeader(HttpNioRequest request, String line) {
        // 解析请求头的键值对
        int colonIndex = line.indexOf(":");
        if (colonIndex > 0) {
            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();
            request.putHeader(key, value);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public URI getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public synchronized void putHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>(16);
        }
        this.headers.put(key, value);
    }

    @Override
    public String toString() {
        return JSON.toJSON(this).toString();
    }

    public Map<String, String> getParam() {
        return param;
    }

    public HttpMethodEnum getDefinedMethod() {
        return definedMethod;
    }

    public void resetUri(String uri) {
        this.uri = URI.create(uri);
    }

    public String getConnectIp() {
        return connectIp;
    }

    public Map<String, AbstractMultipartContent> getMultipartContentMap() {
        return multipartContentMap;
    }

    public void setConnectIp(String connectIp) {
        this.connectIp = connectIp;
    }

    public int getConnectPort() {
        return connectPort;
    }

    public void setConnectPort(int connectPort) {
        this.connectPort = connectPort;
    }
}
