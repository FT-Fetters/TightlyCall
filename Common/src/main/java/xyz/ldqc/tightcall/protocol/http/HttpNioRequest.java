package xyz.ldqc.tightcall.protocol.http;

import com.alibaba.fastjson2.JSON;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import xyz.ldqc.tightcall.exception.HttpNioRequestParseException;

/**
 * @author Fetters
 */
public class HttpNioRequest {

  private String method;
  private URI uri;
  private String protocol;
  private Map<String, String> headers;
  private String body;

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

  /**
   * 解析请求行
   */
  private static void parseRequestLine(HttpNioRequest request, String firstLine) {
    String[] requestLineParts = firstLine.split(" ");
    request.method = requestLineParts[0];
    request.uri = URI.create(requestLineParts[1]);
    request.protocol = requestLineParts[2];
  }

  private static void parseParam(HttpNioRequest request) {
    String query = request.uri.getQuery();
    String[] params = query.split("&");
    request.param = new HashMap<>(params.length);
    for (String param : params) {
      String[] kv = param.split("=");
      if (kv.length != 2) {
        throw new HttpNioRequestParseException("Parse param error");
      }
      String k = kv[0];
      String v = kv[1];
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
}
