package sample.model;

import lombok.Data;

@Data
public class User {
	private final String login;
	private final byte[] encodedPrivateRsaKey;
	private final byte[] publicRsaKey;
}
