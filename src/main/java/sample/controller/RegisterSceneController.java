package sample.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Settings;
import sample.exception.CannotRegisterUserException;
import sample.model.User;
import sample.persistence.UsersLoader;
import sample.persistence.UsersSaver;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class RegisterSceneController {
	public TextField loginTextField;
	public PasswordField passwordField;

	public RegisterSceneController() {
		Field field;
		try {
			field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
			field.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

			field.set(null, false);

		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalArgumentException
				| IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void mainMenu() {
		ScenesManager.setScene(ScenesNames.MENU);
	}

	public void register() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(Settings.RSA_KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.genKeyPair();
			byte[] privateKey = keyPair.getPrivate().getEncoded();
			byte[] publicKey = keyPair.getPublic().getEncoded();

			String password = passwordField.getText();
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashedPasswordBytes = digest.digest(password.getBytes());

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKeySpec = new SecretKeySpec(hashedPasswordBytes, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

			byte[] encodedPrivateKey = cipher.doFinal(privateKey);
			System.out.println("Public key: " + Arrays.toString(publicKey));
			System.out.println("Encoded private key: " + Arrays.toString(encodedPrivateKey));

			User user = new User(loginTextField.getText(), encodedPrivateKey, publicKey);
			saveUser(user);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new CannotRegisterUserException(e);
		}
	}

	private void saveUser(User user) {
		Collection<User> users = new UsersLoader().loadUsers();
		users.add(user);
		new UsersSaver().save(users);
	}
}
