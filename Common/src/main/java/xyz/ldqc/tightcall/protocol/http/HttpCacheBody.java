package xyz.ldqc.tightcall.protocol.http;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.util.StringUtil;

/**
 * @author Fetters
 */
public class HttpCacheBody {

    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";

    private static final int CRLF_LEN = 2;

    private List<byte[]> lines;

    private AbstractByteData cacheByteData;

    private String method;

    private int contentLen;

    private byte[] contentData;

    private AbstractByteData preData;

    /**
     * 是否添加结束
     */
    private boolean end;

    /**
     * 请求头结束
     */
    private boolean headEnd;

    public HttpCacheBody() {
        lines = new ArrayList<>();
        cacheByteData = new SimpleByteData();
        preData = new SimpleByteData();
        end = false;
        headEnd = false;
        contentLen = -1;
    }

    public boolean isEnd() {
        return end;
    }

    public void append(byte[] data) {
        if (isEnd()) {
            return;
        }
        handlePreCache();
        checkMethod();
        if (headEnd && POST_METHOD.equals(method)) {
            // 头部结束且请求方法为POST则获取数据部分
            checkContentLength();
            appendContent(data);
        } else if (!headEnd) {
            // 添加头部数据
            appendHead(data);
        }
    }

    /**
     * 处理之前缓存的数据
     */
    private void handlePreCache() {
        if (lines.isEmpty() && !preData.isEmpty()) {
            append(preData.readBytes());
        }
    }

    private void checkMethod() {
        if (StringUtil.isBlank(method) && lines.size() > 1) {
            byte[] first = lines.get(0);
            String line = new String(first).trim();
            if (line.startsWith(GET_METHOD)) {
                method = GET_METHOD;
            } else {
                method = POST_METHOD;
            }

        }
    }

    /**
     * 添加数据体部分数据，如果数据还没达到ContentLen则先缓存，等相等了再放入contentData
     */
    private void appendContent(byte[] bytes) {
        int dataLen = bytes.length;
        if (cacheByteData.remaining() + dataLen <= contentLen) {
            cacheByteData.writeBytes(bytes);
            if (cacheByteData.remaining() == contentLen) {
                end = true;
                contentData = cacheByteData.readBytes();
            }
        } else {
            int writeLen = contentLen - cacheByteData.remaining();
            cacheByteData.writeBytes(bytes, 0, writeLen);
            end = true;
            contentData = cacheByteData.readBytes();
            preData.writeBytes(bytes, writeLen, dataLen - writeLen);
        }

    }

    /**
     * 检查内容长度
     */
    private void checkContentLength() {
        if (contentLen == -1) {
            initContentLength();
        }
    }

    /**
     * 初始化内容长度
     */
    private void initContentLength() {
        for (byte[] bytes : lines) {
            String line = new String(bytes);
            if (line.contains(ResponseHeaderEnum.CONTENT_LENGTH.getKey())) {
                contentLen = Integer.parseInt(line.split(":")[1].trim());
                return;
            }
        }
        contentLen = 0;
    }

    private void appendHead(byte[] data) {
        int len = data.length;
        int l = 0;
        for (int i = 0; i < len; i++) {
            // 如果不是\r\n则跳过
            if (i + 1 >= len || data[i] != '\r' || data[i + 1] != '\n') {
                continue;
            }
            if (i - l == 0) {
                // 如果是空行代表头部结束到数据部分
                headEndLine(data, i);
                return;
            }
            SimpleByteData byteData = new SimpleByteData();
            // 判断是否在cache中是否有数据，如果有则先把缓存中的数据加入
            if (!cacheByteData.isEmpty()) {
                byteData.writeBytes(cacheByteData.readBytes());
            }
            byteData.writeBytes(data, l, i - l);
            l = i + 2;
            // 加入到新列
            byte[] bytes = byteData.readBytes();
            lines.add(bytes);
        }
        if (l < data.length) {
            cacheByteData.writeBytes(data, l, data.length - l);
        }
    }

    /**
     * 处理结束头部部分
     */
    private void headEndLine(byte[] data, int i) {
        int len = data.length;
        headEnd = true;
        lines.add(new byte[0]);
        checkMethod();
        if (GET_METHOD.equals(method)) {
            end = true;
        } else if (POST_METHOD.equals(method) && i + CRLF_LEN <= len) {
            SimpleByteData byteData = new SimpleByteData(data);
            byteData.readBytes(i + 2);
            byte[] remByte = byteData.readBytes();
            append(remByte);
        }
    }

    public byte[] readRequestData() {
        if (!end) {
            return new byte[0];
        }
        SimpleByteData byteData = new SimpleByteData();
        for (byte[] line : lines) {
            byteData.writeBytes(line).writeBytes("\r\n".getBytes(StandardCharsets.UTF_8));
        }
        byteData.writeBytes(contentData);
        clean();
        return byteData.readBytes();
    }

    private void clean() {
        lines.clear();
        contentData = null;
        end = false;
        method = null;
        contentLen = -1;
        headEnd = false;
    }


}
