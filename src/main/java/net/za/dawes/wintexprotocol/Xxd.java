package net.za.dawes.wintexprotocol;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Xxd {

    protected static void printLineNumber(PrintStream stream, int idx) {
        stream.printf("%08x:", idx);
    }

    protected static void printHexCharOrSpace(PrintStream stream, byte[] byteBuffer, int offset) {
        if (offset < byteBuffer.length) {
            stream.printf("%02x", byteBuffer[offset]);
        } else {
            stream.printf("  ");
        }
    }

    protected static void printHexBytes(PrintStream stream, byte[] byteBuffer, int offset) {
        int bytes = byteBuffer.length;
        for (int j = 0; j < 8; ++j) {
            stream.printf(" ");
            printHexCharOrSpace(stream, byteBuffer, offset + 2 * j);
            printHexCharOrSpace(stream, byteBuffer, offset + 2 * j + 1);
        }
    }

    protected static void printPrintableCharacters(PrintStream stream, byte[] byteBuffer, int offset) {
        int bytes = byteBuffer.length;
        for (int j = 0; j < 16; ++j) {
            if (offset + j < bytes) {
                byte b = byteBuffer[offset + j];
                if (b < 32 || b > 126) {
                    stream.printf(".");
                } else {
                    stream.append((char) b);
                }
            } else {
                stream.append(' ');
            }
        }
    }

    public static String dump(byte[] byteBuffer) {
        return dump(byteBuffer, 0);
    }

    public static String dump(byte[] byteBuffer, int off) {
        return dump(byteBuffer, off, byteBuffer.length);
    }

    public static String dump(byte[] byteBuffer, int startOffset, int length) {
        if (length < 0)
            length = byteBuffer.length - startOffset + length;
        ByteArrayOutputStream bstream = new ByteArrayOutputStream();
        try (PrintStream stream = new PrintStream(bstream)) {
            for (int idx = 0; idx < Math.min(length, byteBuffer.length - startOffset); idx += 16) {
//                printLineNumber(stream, idx + startOffset);
                printHexBytes(stream, byteBuffer, idx + startOffset);
                stream.printf("  ");
                printPrintableCharacters(stream, byteBuffer, idx + startOffset);
                stream.printf("\n");
            }
        }
        return bstream.toString();
    }
}