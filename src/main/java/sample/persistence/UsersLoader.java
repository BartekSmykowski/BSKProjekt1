package sample.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import sample.Settings;
import sample.exception.CannotReadUsersException;
import sample.model.User;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersLoader {
	private final ObjectMapper objectMapper = new ObjectMapper();

	public Collection<User> loadUsers() {
		Map<String, byte[]> privateKeyUsers = loadUsers(Settings.USERS_WITH_PRIVATE_KEY_PATH);
		Map<String, byte[]> publicKeyUsers = loadUsers(Settings.USERS_WITH_PUBLIC_KEY_PATH);
		return privateKeyUsers.entrySet().stream()
				.map(loginKeyPair -> createUser(loginKeyPair, publicKeyUsers))
				.collect(Collectors.toList());
	}

	private User createUser(Map.Entry<String, byte[]> loginKeyPair, Map<String, byte[]> publicKeyUsers) {
		return new User(loginKeyPair.getKey(), loginKeyPair.getValue(), publicKeyUsers.get(loginKeyPair.getKey()));
	}

	private Map<String, byte[]> loadUsers(String path) {
		try {
			File usersSource = new File(System.getProperty("user.dir") + '\\' + path);
			Map<String, String> loginStringKeyMap = objectMapper.readValue(usersSource, new TypeReference<Map<String, String>>() {
			});
			return loginStringKeyMap.entrySet().stream()
					.map(this::decodeKey)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		} catch (IOException e) {
			throw new CannotReadUsersException(e.getMessage());
		}
	}

	private Map.Entry<String, byte[]> decodeKey(Map.Entry<String, String> entry)
	{
		try {
			return new AbstractMap.SimpleEntry<>(entry.getKey(), Base64.decode(entry.getValue().getBytes()));
		}
		catch (Base64DecodingException e) {
			throw new CannotReadUsersException(e.getMessage());
		}
	}
}
