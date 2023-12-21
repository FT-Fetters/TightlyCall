package xyz.ldqc.tightcall.protocol.http;

/**
 * @author Fetters
 */
public enum HttpCodeEnum {

  /**
   * 100 Continue
   */
  CONTINUE(100, "Continue"),

  /**
   * 101 Switching Protocols
   */
  SWITCHING_PROTOCOLS(101, "Switching Protocols"),

  /**
   * 102 Processing
   */
  PROCESSING(102, "Processing"),

  /**
   * 200 OK
   */
  OK(200, "OK"),

  /**
   * 201 Created
   */
  CREATED(201, "Created"),

  /**
   * 202 Accepted
   */
  ACCEPTED(202, "Accepted"),

  /**
   * 204 No Content
   */
  NO_CONTENT(204, "No Content"),

  /**
   * 206 Partial Content
   */
  PARTIAL_CONTENT(206, "Partial Content"),

  /**
   * 300 Multiple Choices
   */
  MULTIPLE_CHOICES(300, "Multiple Choices"),

  /**
   * 301 Moved Permanently
   */
  MOVED_PERMANENTLY(301, "Moved Permanently"),

  /**
   * 302 Found
   */
  FOUND(302, "Found"),

  /**
   * 304 Not Modified
   */
  NOT_MODIFIED(304, "Not Modified"),

  /**
   * 307 Temporary Redirect
   */
  TEMPORARY_REDIRECT(307, "Temporary Redirect"),

  /**
   * 400 Bad Request
   */
  BAD_REQUEST(400, "Bad Request"),

  /**
   * 401 Unauthorized
   */
  UNAUTHORIZED(401, "Unauthorized"),

  /**
   * 403 Forbidden
   */
  FORBIDDEN(403, "Forbidden"),

  /**
   * 404 Not Found
   */
  NOT_FOUND(404, "Not Found"),

  /**
   * 405 Method Not Allowed
   */
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  /**
   * 408 Request Timeout
   */
  REQUEST_TIMEOUT(408, "Request Timeout"),

  /**
   * 409 Conflict
   */
  CONFLICT(409, "Conflict"),

  /**
   * 415 Unsupported Media Type
   */
  UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

  /**
   * 429 Too Many Requests
   */
  TOO_MANY_REQUESTS(429, "Too Many Requests"),

  /**
   * 500 Internal Server Error
   */
  INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

  /**
   * 501 Not Implemented
   */
  NOT_IMPLEMENTED(501, "Not Implemented"),

  /**
   * 502 Bad Gateway
   */
  BAD_GATEWAY(502, "Bad Gateway"),

  /**
   * 503 Service Unavailable
   */
  SERVICE_UNAVAILABLE(503, "Service Unavailable"),

  /**
   * 504 Gateway Timeout
   */
  GATEWAY_TIMEOUT(504, "Gateway Timeout"),

  /**
   * 505 HTTP Version Not Supported
   */
  HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");
  private final int code;

  private final String msg;

  HttpCodeEnum(int code, String msg){
    this.code = code;
    this.msg = msg;
  }

  public String getMsg(){
    return this.msg;
  }

  public int getCode(){
    return this.code;
  }

}
