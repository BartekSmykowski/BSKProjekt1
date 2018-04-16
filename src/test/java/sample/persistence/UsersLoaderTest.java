package sample.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import sample.model.User;

import java.util.Collection;

import static com.google.common.truth.Truth.assertThat;

class UsersLoaderTest {
	private UsersLoader usersLoader;

	@BeforeEach
	void createUsersLoader() {
		this.usersLoader = new UsersLoader();
	}

	@Disabled
	@Test
	void shouldLoadTwoUsers() {
		Collection<User> users = usersLoader.loadUsers();
		assertThat(users).hasSize(2);
		assertThat(users).contains(new User("exampleLogin", "somePrivateHash1".getBytes(), "somePublicHash11".getBytes()));
		assertThat(users).contains(new User("exampleLogin2", "somePrivateHash2".getBytes(), "somePublicHash22".getBytes()));
	}
}
