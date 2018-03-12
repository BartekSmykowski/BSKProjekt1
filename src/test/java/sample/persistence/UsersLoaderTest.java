package sample.persistence;

import static com.google.common.truth.Truth.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sample.model.User;

public class UsersLoaderTest {
	private UsersLoader usersLoader;
	
	@BeforeEach
	public void createUsersLoader() {
		this.usersLoader = new UsersLoader();
	}
	
	@Test
	public void shouldLoadTwoUsers() {
		Collection<User> users = usersLoader.loadUsers();
		assertThat(users).hasSize(2);
		assertThat(users).contains(new User("exampleLogin", "somePrivateHash", "somePublicHash"));
		assertThat(users).contains(new User("exampleLogin2", "somePrivateHash2", "somePublicHash2"));
	}
}
