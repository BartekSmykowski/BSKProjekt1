package sample.ciphering.cipherers.AES;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sample.Settings;
import sample.ciphering.key.generation.InitialVectorGenerator;
import sample.ciphering.key.generation.RandomBytesGenerator;
import sample.ciphering.key.generation.SessionKeyGenerator;
import sample.model.EncodingModes;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AESCiphererTest {

    private byte[] sessionKey;
    private byte[] initialVector;
    private byte[] randomData;

    @BeforeEach
    void setup(){
        InitialVectorGenerator initialVectorGenerator = new InitialVectorGenerator(Settings.INITIAL_VECTOR_SIZE);
        initialVector = initialVectorGenerator.generate();

        SessionKeyGenerator sessionKeyGenerator = new SessionKeyGenerator(Settings.SESSION_KEY_SIZE);
        sessionKey = sessionKeyGenerator.generate();

        RandomBytesGenerator randomBytesGenerator = new RandomBytesGenerator(Settings.DATA_PACKET_SIZE);
        randomData = randomBytesGenerator.generate();
    }

    @Test
    void decodingEncodedDataWithECB_shouldReturnOriginalData(){
        AESCipherer ciphererEncode = AESCiphererFactory.produce(EncodingModes.ECB, sessionKey, initialVector);

        byte[] encoded = ciphererEncode.encode(randomData);

        AESCipherer ciphererDecode = AESCiphererFactory.produce(EncodingModes.ECB, sessionKey, initialVector);

        byte[] decoded = ciphererDecode.decode(encoded);

        assertArrayEquals(randomData, decoded);
    }

    @Test
    void decodingEncodedDataWithCBC_shouldReturnOriginalData(){
        AESCipherer ciphererEncode = AESCiphererFactory.produce(EncodingModes.CBC, sessionKey, initialVector);

        byte[] encoded = ciphererEncode.encode(randomData);

        AESCipherer ciphererDecode = AESCiphererFactory.produce(EncodingModes.CBC, sessionKey, initialVector);

        byte[] decoded = ciphererDecode.decode(encoded);

        assertArrayEquals(randomData, decoded);
    }

    @Test
    void decodingEncodedDataWithCFB_shouldReturnOriginalData(){
        AESCipherer ciphererEncode = AESCiphererFactory.produce(EncodingModes.CFB, sessionKey, initialVector);

        byte[] encoded = ciphererEncode.encode(randomData);

        AESCipherer ciphererDecode = AESCiphererFactory.produce(EncodingModes.CFB, sessionKey, initialVector);

        byte[] decoded = ciphererDecode.decode(encoded);

        assertArrayEquals(randomData, decoded);
    }

    @Test
    void decodingEncodedDataWithOFB_shouldReturnOriginalData(){
        AESCipherer ciphererEncode = AESCiphererFactory.produce(EncodingModes.OFB, sessionKey, initialVector);

        byte[] encoded = ciphererEncode.encode(randomData);

        AESCipherer ciphererDecode = AESCiphererFactory.produce(EncodingModes.OFB, sessionKey, initialVector);

        byte[] decoded = ciphererDecode.decode(encoded);

        assertArrayEquals(randomData, decoded);
    }

    @Test
    void encodingBlockOfDataWithECB_shouldReturnProperLengthOutputBlock(){
        AESCipherer ciphererEncode = AESCiphererFactory.produce(EncodingModes.ECB, sessionKey, initialVector);

        byte[] encoded = ciphererEncode.encode(randomData);

        assertEquals(Settings.OUT_PACKET_SIZE, encoded.length);

    }

    @Test
    void decodingBlockOfDataWithECB_shouldReturnProperLengthOutputBlock(){
        AESCipherer cipherer = AESCiphererFactory.produce(EncodingModes.ECB, sessionKey, initialVector);

        byte[] encoded = cipherer.encode(randomData);

        byte[] decoded = cipherer.decode(encoded);

        assertEquals(Settings.DATA_PACKET_SIZE, decoded.length);

    }

    @Test
    void encodingBlockOfDataWithOFB_shouldReturnProperLengthOutputBlock(){
        AESCipherer ciphererEncode = AESCiphererFactory.produce(EncodingModes.OFB, sessionKey, initialVector);

        byte[] encoded = ciphererEncode.encode(randomData);

        assertEquals(Settings.OUT_PACKET_SIZE, encoded.length);

    }

    @Test
    void encodingBlockOfDataWithCFB_shouldReturnProperLengthOutputBlock(){
        AESCipherer ciphererEncode = AESCiphererFactory.produce(EncodingModes.CFB, sessionKey, initialVector);

        byte[] encoded = ciphererEncode.encode(randomData);

        assertEquals(Settings.OUT_PACKET_SIZE, encoded.length);

    }

    @Test
    void encodingBlockOfDataWithCBC_shouldReturnProperLengthOutputBlock(){
        AESCipherer ciphererEncode = AESCiphererFactory.produce(EncodingModes.CBC, sessionKey, initialVector);

        byte[] encoded = ciphererEncode.encode(randomData);

        assertEquals(Settings.OUT_PACKET_SIZE, encoded.length);

    }

}