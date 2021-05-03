package net.za.dawes.wintexprotocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: Main hostname port pin");
            System.exit(1);
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        final String pin = args[2];
        System.out.println("Connecting to " + host + ":" + port + ", using PIN " + pin);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new WintexCodec(), new LoggingHandler(LogLevel.INFO), new WintexClientHandler(pin.getBytes()));
                }
            });
            
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
