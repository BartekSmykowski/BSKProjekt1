package sample.ciphering.cipherers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sample.Settings;
import sample.ciphering.key.generation.RandomBytesGenerator;
import sample.ciphering.key.generation.RsaKeyGenerator;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertEquals;


class RSACiphererTest {

    private byte[] randomData;
    private byte[] privateKey;
    private byte[] publicKey;

    @BeforeEach
    void setupData(){
        RandomBytesGenerator randomBytesGenerator = new RandomBytesGenerator(Settings.SESSION_KEY_SIZE);
        randomData = randomBytesGenerator.generate();
        RsaKeyGenerator rsaKeyGenerator = new RsaKeyGenerator(Settings.RSA_KEY_SIZE);
        KeyPair pair = rsaKeyGenerator.generate();
        privateKey = pair.getPrivate().getEncoded();
        publicKey = pair.getPublic().getEncoded();
    }

    @Test
    void decodingEncodedData_shouldReturnOriginalData(){
        RSACipherer rsaCiphererPublic = new RSACipherer(KeyTypes.PUBLIC, publicKey);

        byte[] encoded = rsaCiphererPublic.encode(randomData);

        RSACipherer rsaCiphererPrivate = new RSACipherer(KeyTypes.PRIVATE, privateKey);

        byte[] decoded = rsaCiphererPrivate.decode(encoded);

        String expected = new String(randomData);
        String actual = new String(decoded);
        assertEquals(expected, actual);

    }


}