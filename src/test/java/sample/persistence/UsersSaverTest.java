package sample.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sample.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class UsersSaverTest
{
	private UsersSaver usersSaver;
	private Collection<User> usersData = Lists.newArrayList(
			new User("exampleLogin", "somePrivateHash1".getBytes(), "somePublicHash11".getBytes()),
			new User("exampleLogin2", "somePrivateHash2".getBytes(), "somePublicHash22".getBytes()));

	@BeforeEach
	void createUsersLoader() {
		this.usersSaver = new UsersSaver();
	}

	@Test
	void shouldNotChangeContentsOfPrivateKeyUsersFile() throws IOException {
		Map<String, Object> before = readFile("users/privateKeyUsersTest.json");
		usersSaver.save(usersData);
		Map<String, Object> after = readFile("users/privateKeyUsersTest.json");
		assertThat(before).isEqualTo(after);
	}

	@Test
	void shouldNotChangeContentsOfPublicKeyUsersFile() throws IOException {
		Map<String, Object> before = readFile("users/publicKeyUsersTest.json");
		usersSaver.save(usersData);
		Map<String, Object> after = readFile("users/publicKeyUsersTest.json");
		assertThat(before).isEqualTo(after);
	}

	private Map<String, Object> readFile(String path) throws IOException {
		return new ObjectMapper().readValue(Files.readAllBytes(Paths.get(path)), new TypeReference<Map<String, Object>>(){
		});
	}


}
