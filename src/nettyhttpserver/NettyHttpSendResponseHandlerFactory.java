/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nettyhttpserver;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.concurrent.TimeUnit;
import nettyhttpserver.response.NettyHttpResponseHandler;
import nettyhttpserver.response.watcher.Info;

/**
 *
 * @author Roman
 */
class NettyHttpSendResponseHandlerFactory {

    private ChannelHandlerContext ctx;
    private DefaultHttpRequest request;
    private boolean keepAlive;
    private int ID = 0;

    public NettyHttpSendResponseHandlerFactory(ChannelHandlerContext ctx, DefaultHttpRequest request) {
        this.request = request;
        this.ctx = ctx;
    }

    void sendResponse() {
        String uri = request.getUri();
        String[] segments = uri.split("[\\/\\?]");

        String className = segments[1];
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        keepAlive = isKeepAlive(request);
        System.out.println("3");

        FullHttpResponse response = NettyHttpResponseHandler.getResponse(className, queryStringDecoder);
        ID = Integer.parseInt(request.headers().get("ID"));
        Info.setSendBytes(ID,response.content().array().length + response.headers().entries().toString().getBytes().length);
        writeResponse(response, className);
    }

    private void writeResponse(final FullHttpResponse response, String className) {
        
        if (className.equals("hello")) {
            final EventLoop loop = ctx.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    if (!keepAlive) {
                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                    } else {
                        response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                        ctx.writeAndFlush(response);
                    }
                }
            }, 10, TimeUnit.SECONDS);
        } else {
            if (!keepAlive) {
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
        }
    }
}
