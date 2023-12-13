package xyz.ldqc.tightcall.chain.support.http;

import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.support.AbstractChannelPostHandlerOutBoundChain;
import xyz.ldqc.tightcall.protocol.http.HttpNioRequest;
import xyz.ldqc.tightcall.protocol.http.HttpNioResponse;
import xyz.ldqc.tightcall.protocol.http.HttpVersionEnum;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * @author Fetters
 */
public class ChannelHttpPostHandlerOutBoundChain extends AbstractChannelPostHandlerOutBoundChain {

    public ChannelHttpPostHandlerOutBoundChain() {
        this.logger = LoggerFactory.getLogger(ChannelHttpPostHandlerOutBoundChain.class);
    }

    @Override
    public void doHandler(Channel channel, Object obj) {
        if (obj instanceof HttpNioRequest) {
            writeDefaultResponse(channel, ((HttpNioRequest) obj));
            return;
        }
        if (!(obj instanceof HttpNioResponse)) {
            return;
        }
        HttpNioResponse httpResponse = (HttpNioResponse) obj;


        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: 12\r\n" +
                "\r\n" +
                "Hello World!";
        doWrite(((SocketChannel) channel), response.getBytes());
    }

    private void writeDefaultResponse(Channel channel, HttpNioRequest request) {
        String body = request.toString();
        HttpNioResponse response = HttpNioResponse.builder()
                .version(HttpVersionEnum.HTTP_1_1)
                .code(200)
                .msg("OK")
                .body(body.getBytes())
                .build();
        doWrite(((SocketChannel) channel), response.toResponseString().getBytes());
    }
}
