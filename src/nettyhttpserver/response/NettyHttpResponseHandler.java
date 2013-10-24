/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nettyhttpserver.response;

/**
 *
 * @author Roman
 */
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;

public class NettyHttpResponseHandler {

    public static FullHttpResponse getResponse(String className, QueryStringDecoder qsd) {
        if (className.equals("hello")) {
           
            return new HelloHttpResponse().getResponse();
        }
        if (className.equals("redirect")) {
            return new RedirectHttpResponse().getResponse(qsd);
        }
        if (className.equals("status")) {
            return new StatusHttpResponse().getResponse();
        }
        return new NettyDefaultResponse().getResponse();
    }
}
