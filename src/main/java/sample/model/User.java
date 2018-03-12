package sample.model;

import lombok.Data;

@Data
public class User {
	private final String login;
	private final String encodedPrivateRsaKey;
	private final String publicRsaKey;
}
