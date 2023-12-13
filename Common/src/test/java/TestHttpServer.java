import org.junit.Test;
import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.protocol.http.HttpNioRequest;
import xyz.ldqc.tightcall.protocol.http.HttpNioResponse;
import xyz.ldqc.tightcall.protocol.http.HttpVersionEnum;
import xyz.ldqc.tightcall.server.HttpServerApplication;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;

import java.util.concurrent.locks.LockSupport;

public class TestHttpServer {

    @Test
    public void testHttpServerApplication(){
        HttpServerApplication httpServer = HttpServerApplication.builder()
                .bind(8080)
                .execNum(4)
                .executor(NioServerExec.class)
                .chain(new DefaultChannelChainGroup())
                .boot();
        LockSupport.park();
    }

    @Test
    public void testHttpNioRequest(){
        String request = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Microsoft Edge\";v=\"120\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6\r\n" +
                "Cookie: Phpstorm-f2872d7d=c5a4220c-2a1a-44cf-8801-5798fa29bdcc\r\n" +
                "\r\n";
        HttpNioRequest httpNioRequest = HttpNioRequest.parse(request);
    }

    @Test
    public void testHttpNioResponse(){
        //language=JSON
        String body = "{\"json\": \"testJsonBody\"}";
        HttpNioResponse ok = HttpNioResponse.builder()
                .version(HttpVersionEnum.HTTP_1_1)
                .code(200)
                .msg("OK")
                .body(body.getBytes())
                .build();
        String responseString = ok.toResponseString();
        System.out.println(responseString);
    }
}
