package sample.persistence;

import static com.google.common.truth.Truth.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sample.model.User;

class UsersLoaderTest {
	private UsersLoader usersLoader;

	@BeforeEach
	void createUsersLoader() {
		this.usersLoader = new UsersLoader();
	}
	
	@Test
	void shouldLoadTwoUsers() {
		Collection<User> users = usersLoader.loadUsers();
		assertThat(users).hasSize(2);
		assertThat(users).contains(new User("exampleLogin", "somePrivateHash1".getBytes(), "somePublicHash11".getBytes()));
		assertThat(users).contains(new User("exampleLogin2", "somePrivateHash2".getBytes(), "somePublicHash22".getBytes()));
	}
}
