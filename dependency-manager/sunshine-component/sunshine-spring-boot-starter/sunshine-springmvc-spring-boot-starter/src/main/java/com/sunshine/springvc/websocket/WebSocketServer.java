package com.sunshine.springvc.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;


public class WebSocketServer {

    public static void main(String[] args) throws Exception {
        new WebSocketServer().bind(9090);
    }

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            //websocket协议本身是基于Http协议的，所以需要Http解码器
                            ch.pipeline().addLast("http-codec",new HttpServerCodec());
                            //以块的方式来写的处理器
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            //netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
                            ch.pipeline().addLast(new HttpObjectAggregator(1024*1024*1024));
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws",null,true,65535));
                            // ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                            ch.pipeline().addLast(new WebSocketServerHandler(port));

                        }
                    });

            ChannelFuture sync = bootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }




}


class WebSocketServerHandler extends ChannelInboundHandlerAdapter {

    private WebSocketServerHandshaker handshaker;

    private final int port;

    WebSocketServerHandler(int port) {
        this.port = port;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }



    public void channelReadComplete(ChannelHandlerContext context) throws  Exception
    {

        context.flush();
    }
    /*
     http
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {


        WebSocketServerHandshakerFactory webFactory=new WebSocketServerHandshakerFactory("ws://127.0.0.1:"+port,null,false);

        if(req instanceof CloseWebSocketFrame)
        {
            handshaker.close(ctx.channel(),(CloseWebSocketFrame) req.retain());
            if(handshaker==null)
            {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());

            }
            else
            {
                handshaker.handshake(ctx.channel(),req);
            }
        }

    }

    /*
     webSocket
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        if (frame instanceof CloseWebSocketFrame)
        {
            handshaker.close(ctx.channel(),(CloseWebSocketFrame)frame.retain());
            return;
        }

        if(frame instanceof TextWebSocketFrame)
        {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if(!(frame instanceof  TextWebSocketFrame))
        {

        }

        String request=((TextWebSocketFrame) frame).text();
        ctx.channel().write(request+"欢迎Netty WebSocket");
    }

}
