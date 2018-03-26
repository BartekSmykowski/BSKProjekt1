package sample.controller;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Settings;
import sample.ciphering.cipherers.AES.AESCipherer;
import sample.ciphering.cipherers.AES.ECBAESCipherer;
import sample.ciphering.hashing.SHA256Hasher;
import sample.ciphering.key.generation.RsaKeyGenerator;
import sample.model.User;
import sample.persistence.UsersLoader;
import sample.persistence.UsersSaver;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.KeyPair;
import java.util.Collection;

public class RegisterSceneController {
	public TextField loginTextField;
	public PasswordField passwordField;

	static {
		tryInitializingCryptographicLibrary();
	}

	private static void tryInitializingCryptographicLibrary()
	{
		try {
			setCryptographicLibraryAccessible();
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalArgumentException
				| IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static void setCryptographicLibraryAccessible()
			throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException
	{
		Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, false);
	}

	public void mainMenu() {
		ScenesManager.setScene(ScenesNames.MENU);
	}

	public void register() {
		User user = createUserFromInput();
		saveUser(user);
	}

	private User createUserFromInput() {
		KeyPair keyPair = new RsaKeyGenerator(Settings.RSA_KEY_SIZE).generate();
		byte[] privateKey = keyPair.getPrivate().getEncoded();
		byte[] publicKey = keyPair.getPublic().getEncoded();

		String password = passwordField.getText();
		byte[] hashedPasswordBytes = new SHA256Hasher().hash(password.getBytes());

		AESCipherer cipherer = new ECBAESCipherer(hashedPasswordBytes);
		byte[] encodedPrivateKey = cipherer.encode(privateKey);

		return new User(loginTextField.getText(), encodedPrivateKey, publicKey);
	}

	private void saveUser(User user) {
		Collection<User> users = new UsersLoader().loadUsers();
		users.add(user);
		new UsersSaver().save(users);
	}
}
