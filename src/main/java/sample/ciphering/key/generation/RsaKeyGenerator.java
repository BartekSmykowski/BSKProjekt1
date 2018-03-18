package sample.ciphering.key.generation;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RsaKeyGenerator
{
	private final KeyPairGenerator keyPairGenerator;

	public RsaKeyGenerator(int rsaKeySize)
	{
		keyPairGenerator = tryCreatingRsaGenerator();
		keyPairGenerator.initialize(rsaKeySize);
	}

	private KeyPairGenerator tryCreatingRsaGenerator()
	{
		try {
			return KeyPairGenerator.getInstance("RSA");
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public KeyPair generate()
	{
		return keyPairGenerator.generateKeyPair();
	}
}
