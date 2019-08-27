package com.idobro.kilovoltmetr_dosimetr.utils;

public final class ByteToIntConverter {
    public static int getUnsignedInt(byte b) {
        return b & 0xFF;
    }

    public static int getUnsignedInt(byte b0, byte b1) {
        int result;
        result = (b1 & 0xFF) << 8 | (b0 & 0xFF);

        return result;
    }
}
