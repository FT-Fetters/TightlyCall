package xyz.ldqc.tightcall.chain.support.http;

import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.support.AbstractChannelPostHandlerOutBoundChain;
import xyz.ldqc.tightcall.protocol.http.ContentTypeEnum;
import xyz.ldqc.tightcall.protocol.http.HttpNioRequest;
import xyz.ldqc.tightcall.protocol.http.HttpNioResponse;
import xyz.ldqc.tightcall.protocol.http.HttpVersionEnum;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import xyz.ldqc.tightcall.protocol.http.ResponseHeaderEnum;

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
    doWrite(((SocketChannel) channel), httpResponse.toResponseBytes());
  }

  private void writeDefaultResponse(Channel channel, HttpNioRequest request) {
    String body = request.toString();
    HttpNioResponse response = HttpNioResponse.builder()
        .version(HttpVersionEnum.HTTP_1_1)
        .code(200)
        .msg("OK")
        .addHeader(ResponseHeaderEnum.CONTENT_TYPE.getKey(),
            ContentTypeEnum.APPLICATION_JSON.getValue())
        .body(body.getBytes())
        .build();
    doWrite(((SocketChannel) channel), response.toResponseString().getBytes());
  }
}
