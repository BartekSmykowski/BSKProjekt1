package sample.persistence;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sample.Settings;
import sample.exception.CannotReadUsersException;
import sample.model.User;

public class UsersLoader {
	private final ObjectMapper objectMapper = new ObjectMapper();

	public Collection<User> loadUsers() {
		Map<String, String> privateKeyUsers = loadUsers(Settings.USERS_WITH_PRIVATE_KEY_PATH);
		Map<String, String> publicKeyUsers = loadUsers(Settings.USERS_WITH_PUBLIC_KEY_PATH);
		return privateKeyUsers.entrySet().stream().map(loginKeyPair -> createUser(loginKeyPair, publicKeyUsers))
				.collect(Collectors.toList());
	}

	private User createUser(Map.Entry<String, String> loginKeyPair, Map<String, String> publicKeyUsers) {
		return new User(loginKeyPair.getKey(), loginKeyPair.getValue(), publicKeyUsers.get(loginKeyPair.getKey()));
	}

	private Map<String, String> loadUsers(String path) {
		URL users = getClass().getResource("/" + Settings.USERS_WITH_PRIVATE_KEY_PATH);
		try {
			return objectMapper.readValue(users, new TypeReference<Map<String, String>>() {
			});
		} catch (IOException e) {
			throw new CannotReadUsersException(e.getMessage());
		}
	}
}
