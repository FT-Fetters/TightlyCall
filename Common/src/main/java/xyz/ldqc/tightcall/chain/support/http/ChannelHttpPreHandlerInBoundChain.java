package xyz.ldqc.tightcall.chain.support.http;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.chain.support.AbstractChannelPreHandlerInBoundChain;
import xyz.ldqc.tightcall.protocol.http.HttpCacheBody;
import xyz.ldqc.tightcall.protocol.http.HttpNioRequest;

import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author Fetters
 */
public class ChannelHttpPreHandlerInBoundChain extends AbstractChannelPreHandlerInBoundChain {

    private static final Logger log = LoggerFactory.getLogger(ChannelHttpPreHandlerInBoundChain.class);

    private final Map<SocketChannel, HttpCacheBody> cache = new HashMap<>();

    @Override
    public void doHandler(Channel channel, Object obj) {
        // 判断是否是SocketChannel
        if (!(channel instanceof SocketChannel)) {
            return;
        }
        if (!(obj instanceof SelectionKey)){
            throw new RuntimeException("obj must be selection key");
        }
        SelectionKey selectionKey = (SelectionKey) obj;
        SocketChannel socketChannel = ((SocketChannel) channel);
        handleSocketChannel(socketChannel, selectionKey);
    }

    private void handleSocketChannel(SocketChannel channel, SelectionKey selectionKey){
        String requestData = getRequestData(channel, selectionKey);
        if (requestData == null){
            log.info("disconnect");
            return;
        }
        if (requestData.isBlank()){
            return;
        }
        HttpNioRequest httpNioRequest = handleRequestData(requestData);
        nextChain.doChain(channel, httpNioRequest);
    }

    private String getRequestData(SocketChannel channel, SelectionKey selectionKey){
        SimpleByteData requestData = new SimpleByteData(1024);
        AbstractByteData byteData;
        int readLen;
        do{
            byteData = readDataFromChanel(channel);
            if (byteData == null){
                selectionKey.cancel();
                return null;
            }
            readLen = byteData.remaining();
            requestData.writeBytes(byteData.readBytes());
        }while (readLen > 0);
        cache.putIfAbsent(channel, new HttpCacheBody());
        HttpCacheBody httpCacheBody = cache.get(channel);
        httpCacheBody.append(requestData.readBytes());
        if (httpCacheBody.isEnd()){
            return new String(httpCacheBody.readRequestData());
        }
        return "";
    }

    private HttpNioRequest handleRequestData(String requestData){
        HttpNioRequest httpNioRequest = HttpNioRequest.parse(requestData);
        String method = httpNioRequest.getMethod();
        String uri = httpNioRequest.getUri().toString();
        String protocol = httpNioRequest.getProtocol();
        log.debug("method: {}, uri: {}, protocol: {}", method, uri, protocol);
        return httpNioRequest;
    }
}
