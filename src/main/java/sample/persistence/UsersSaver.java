package sample.persistence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import sample.Settings;
import sample.exception.CannotSaveUsersException;
import sample.model.User;

public class UsersSaver {
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public void save(Collection<User> users) {
		Map<String, byte[]> privateKeyUsers = users.stream()
				.collect(Collectors.toMap(User::getLogin, User::getEncodedPrivateRsaKey));
		save(privateKeyUsers, Settings.USERS_WITH_PRIVATE_KEY_PATH);

		Map<String, byte[]> publicKeyUsers = users.stream()
				.collect(Collectors.toMap(User::getLogin, User::getPublicRsaKey));
		save(publicKeyUsers, Settings.USERS_WITH_PUBLIC_KEY_PATH);
	}
	
	private void save(Map<String, byte[]> loginKeyMap, String path)
	{
		Map<String, String> convertedToSave = convertToFileSaveAbleFormat(loginKeyMap);
		try(PrintWriter writer = new PrintWriter(path))
		{
			writer.print(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(convertedToSave));
		} catch (FileNotFoundException | JsonProcessingException e) {
			throw new CannotSaveUsersException(e.getMessage());
		}
	}

	private Map<String, String> convertToFileSaveAbleFormat(Map<String, byte[]> loginKeyMap)
	{
		return loginKeyMap.entrySet().stream()
				.map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), Base64.encode(entry.getValue())))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
