package sample.ciphering.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hasher
{
	private final MessageDigest hasher;

	public SHA256Hasher()
	{
		hasher = tryCreatingHasher();
	}

	private MessageDigest tryCreatingHasher()
	{
		try
		{
			return MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}

	public byte[] hash(byte[] data)
	{
		return hasher.digest(data);
	}
}
