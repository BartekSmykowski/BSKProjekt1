package sample;

public class Settings {
	public static final String USERS_WITH_PRIVATE_KEY_PATH = "users/privateKeyUsers.json";
	public static final String USERS_WITH_PUBLIC_KEY_PATH = "users/publicKeyUsers.json";
	public static final int RSA_KEY_SIZE = 2048;
	public static final int SESSION_KEY_SIZE = 32;
	public static final int INITIAL_VECTOR_SIZE = 16;
	public static final int DATA_PACKET_SIZE = 128;
	public static final int OUT_PACKET_SIZE = (DATA_PACKET_SIZE/16 + 1)*16;


	public static final String TEST_ORIGINAL_FILE = "testFiles/testOriginal.txt";
	public static final String TEST_ENCODED_FILE_NAME = "encoded";
	public static final String TEST_ENCODED_FILE_DIRECTORY = "testFiles";
	public static final String TEST_DECODED_FILE_NAME = "decoded";
	public static final String TEST_DECODED_FILE_SAVE_DIRECTORY = "testFiles";

  }
