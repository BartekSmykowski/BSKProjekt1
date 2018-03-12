package sample.persistence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sample.Settings;
import sample.exception.CannotSaveUsersException;
import sample.model.User;

public class UsersSaver {
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public void save(Collection<User> users) {
		Map<String, String> privateKeyUsers = users.stream()
				.collect(Collectors.toMap(User::getEncodedPrivateRsaKey, User::getPublicRsaKey));
		save(privateKeyUsers, Settings.USERS_WITH_PRIVATE_KEY_PATH);

		Map<String, String> publicKeyUsers = users.stream()
				.collect(Collectors.toMap(User::getEncodedPrivateRsaKey, User::getPublicRsaKey));
		save(publicKeyUsers, Settings.USERS_WITH_PUBLIC_KEY_PATH);
	}
	
	private void save(Map<String, String> keyLoginPair, String path)
	{
		try(PrintWriter writer = new PrintWriter(path))
		{
			writer.print(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(keyLoginPair));
		} catch (FileNotFoundException | JsonProcessingException e) {
			throw new CannotSaveUsersException(e.getMessage());
		}
	}
}
