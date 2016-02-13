package com.kyntsevichvova.chat.server;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by petuh on 2/12/2016.
 * <p>
 * Kek Lelpas Object Notation
 */
public class KLON {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    private HashMap<String, byte[]> map = new HashMap<>();

    public KLON() {
    }

    /**
     * Parse bytes to KLON object
     *
     * @param bytes
     * @return KLON object with data
     * @throws IndexOutOfBoundsException in cause of wrong data
     */

    public static KLON parseBytes(byte[] bytes) {
        KLON klon = new KLON();
        int it = 0;
        while (it < bytes.length) {
            int keyLength = bytes[it++];
            byte[] keyBytes = new byte[keyLength];
            System.arraycopy(bytes, it, keyBytes, 0, keyLength);
            it += keyLength;
            String key = new String(keyBytes, CHARSET);
            int valueLength = bytesToInt(bytes[it], bytes[it + 1], bytes[it + 2], bytes[it + 3]);
            it += 4;
            byte[] value = new byte[valueLength];
            System.arraycopy(bytes, it, value, 0, valueLength);
            it += valueLength;
            klon.putBytes(key, value);
        }
        return klon;
    }

    public static byte[] intToBytes(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i >> 24);
        bytes[1] = (byte) (i >> 16);
        bytes[2] = (byte) (i >> 8);
        bytes[3] = (byte) i;
        return bytes;
    }

    public static int bytesToInt(byte... bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    public KLON putBytes(String key, byte[] value) {
        map.put(key, value);
        return this;
    }

    public KLON putString(String key, String value, Charset charset) {
        if (key.length() > 255) key = key.substring(0, 255);
        return putBytes(key, value.getBytes(charset));
    }

    public KLON putString(String key, String value) {
        return putString(key, value, CHARSET);
    }

    public KLON remove(String key) {
        map.remove(key);
        return this;
    }

    public byte[] getBytes(String key) {
        return map.get(key);
    }

    public String getString(String key, Charset charset) {
        return new String(getBytes(key), charset);
    }

    public String getString(String key) {
        return getString(key, CHARSET);
    }

    public byte[] toBytes() {
        int len = 0;
        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            len += 5 + entry.getKey().getBytes(CHARSET).length + entry.getValue().length;
        }
        byte[] arr = new byte[len];
        int it = 0;
        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            arr[it++] = (byte) entry.getKey().length();
            byte[] key = entry.getKey().getBytes(CHARSET);
            System.arraycopy(key, 0, arr, it, key.length);
            it += key.length;
            byte[] value = entry.getValue();
            System.arraycopy(intToBytes(value.length), 0, arr, it, 4);
            it += 4;
            System.arraycopy(value, 0, arr, it, value.length);
            it += value.length;
        }
        return arr;
    }

    public byte[] toFullBytes() {
        byte[] bytes = toBytes();
        int length = bytes.length;
        byte[] full = new byte[length + 4];
        System.arraycopy(intToBytes(length), 0, full, 0, 4);
        System.arraycopy(bytes, 0, full, 4, length);
        return full;
    }
}