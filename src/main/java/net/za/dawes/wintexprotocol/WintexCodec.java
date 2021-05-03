package net.za.dawes.wintexprotocol;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class WintexCodec extends ByteToMessageCodec<WintexMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, WintexMessage msg, ByteBuf out) throws Exception {
        out.writeByte(msg.getData().length + 3); // length + type + data + checksum
        out.writeByte(msg.getType());
        out.writeBytes(msg.getData());
        out.writeByte(calculateChecksum(msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = (int) in.getByte(in.readerIndex()) & 0xFF; // total length of message, including the length and checksum
        if (in.readableBytes() < len)
            return;
        in.readByte(); // consume the length byte we previously peeked at
        byte type = in.readByte();
        byte[] data = new byte[len - 3];
        in.readBytes(data);
        byte checksum = in.readByte();
        WintexMessage msg = new WintexMessage(type, data);
        if (checksum == calculateChecksum(msg)) {
            out.add(msg);
        } else {
            throw new RuntimeException("Wintex message checksum didn't match calculation: " + msg + ", expected "
                    + calculateChecksum(msg) + ", got " + checksum);
        }
    }

    private byte calculateChecksum(WintexMessage msg) {
        byte checksum = (byte) (3 + msg.getData().length);
        checksum += msg.getType();
        byte[] data = msg.getData();
        for (int i = 0; i < data.length; i++) {
            checksum += data[i];
        }
        return (byte) (checksum ^ 0xFF);
    }

}
