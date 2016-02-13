import com.kyntsevichvova.chat.server.KLON;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by petuh on 2/13/2016.
 */
public class KLONTest {
    @Test
    public void storingTest() {
        KLON klon = new KLON();
        String key = "";
        for (int i = 0; i < 30; i++) key += "KeY";
        String value = "";
        for (int i = 0; i < 2500; i++) {
            value += (char) ('a' + (i % 26));
        }
        klon.putString(key, value);
        byte[] bytes = klon.toBytes();
        KLON klon2 = KLON.parseBytes(bytes);
        assertEquals(value, klon2.getString(key));
    }

    public String generateString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append((char) ('a' + random.nextInt(30)));
        return sb.toString();
    }

    @Test
    public void randomStorageTest() {
        KLON klon = new KLON();
        String[] keys = new String[250];
        Random random = new Random();
        for (int i = 0; i < keys.length; i++) {
            String key = generateString(random, i + 1);
            int valueLength = random.nextInt(100) + 1;
            String value = generateString(random, valueLength);
            keys[i] = key;
            klon.putString(key, value);
        }
        byte[] bytes = klon.toBytes();
        KLON klon2 = KLON.parseBytes(bytes);
        for (String key : keys) assertEquals(klon2.getString(key), klon.getString(key));
    }

}
