package sample.controller;

import javafx.scene.control.Label;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterSceneController {
	public TextField loginTextField;
	public PasswordField passwordField;
	public Label registerLabel;


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
		registerLabel.setText("Rejestruje");
		if(isLoginUsed(loginTextField.getText())){
			registerLabel.setText("Login zajęty.");
			return;
		}
		if(!isPasswordValid(passwordField.getText())){
			registerLabel.setText("Złe hasło.");
			return;
		}
		User user = createUserFromInput();
		saveUser(user);
		registerLabel.setText("Zarejestrowano");
	}

	private boolean isPasswordValid(String password) {
		String passwordRegexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!*@#$%^&+=])(?=\\S+$).{8,}$";
		Pattern pattern = Pattern.compile(passwordRegexp);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	private User createUserFromInput() {
		KeyPair keyPair = new RsaKeyGenerator(Settings.RSA_KEY_SIZE).generate();
		byte[] privateKey = keyPair.getPrivate().getEncoded();
		byte[] publicKey = keyPair.getPublic().getEncoded();

		String password = passwordField.getText();
		byte[] hashedPasswordBytes = new SHA256Hasher().hash(password.getBytes());

		AESCipherer cipherer = new ECBAESCipherer(hashedPasswordBytes);
		byte[] encodedPrivateKey = cipherer.encode(privateKey);

		String login = loginTextField.getText();
		return new User(login, encodedPrivateKey, publicKey);
	}

	private boolean isLoginUsed(String login){
		Collection<User> users = new UsersLoader().loadUsers();
		for(User usr : users) {
			if (usr.getLogin().equals(login)) {
				return true;
			}
		}
		return false;
	}

	private void saveUser(User user) {
		Collection<User> users = new UsersLoader().loadUsers();
		users.add(user);
		new UsersSaver().save(users);
	}
}
