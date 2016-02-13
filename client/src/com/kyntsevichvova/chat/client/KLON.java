package com.kyntsevichvova.chat.client;

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
            int valueLength = bytes[it++] << 24;
            valueLength += bytes[it++] << 16;
            valueLength += bytes[it++] << 8;
            valueLength += bytes[it++];
            byte[] value = new byte[valueLength];
            System.arraycopy(bytes, it, value, 0, valueLength);
            it += valueLength;
            klon.putBytes(key, value);
        }
        return klon;
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
            len += 5 + entry.getKey().length() + entry.getValue().length;
        }
        byte[] arr = new byte[len];
        int it = 0;
        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            arr[it++] = (byte) entry.getKey().length();
            byte[] key = entry.getKey().getBytes(CHARSET);
            System.arraycopy(key, 0, arr, it, key.length);
            it += key.length;
            byte[] value = entry.getValue();
            int valueLength = value.length;
            arr[it++] = (byte) (valueLength >> 24);
            arr[it++] = (byte) (valueLength >> 16);
            arr[it++] = (byte) (valueLength >> 8);
            arr[it++] = (byte) valueLength;
            System.arraycopy(value, 0, arr, it, valueLength);
            it += valueLength;
        }
        return arr;
    }

    public byte[] toFullBytes() {
        byte[] bytes = toBytes();
        int length = bytes.length;
        byte[] full = new byte[length + 4];
        full[0] = (byte) (length >> 24);
        full[1] = (byte) (length >> 16);
        full[2] = (byte) (length >> 8);
        full[3] = (byte) length;
        System.arraycopy(bytes, 0, full, 4, length);
        return full;
    }
}
