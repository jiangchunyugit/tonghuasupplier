package cn.thinkfree.core.security.utils;
 

import static org.springframework.security.crypto.util.EncodingUtils.concatenate;
import static org.springframework.security.crypto.util.EncodingUtils.subArray;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A standard {@code PasswordEncoder} implementation that uses SHA-256 hashing with 1024 iterations and a
 * random 8-byte random salt value. It uses an additional system-wide secret value to provide additional protection.
 * <p>
 * The digest algorithm is invoked on the concatenated bytes of the salt, secret and password.
 * <p>
 * If you are developing a new system, {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder} is
 * a better choice both in terms of security and interoperability with other languages.
 *
 * @author Keith Donald
 * @author Luke Taylor
 */
public final class PasswordEncoderForOther implements PasswordEncoder {

	
    private final Digester digester;

    private final byte[] secret;

    private final BytesKeyGenerator saltGenerator;
    

    /**
     * Constructs a standard password encoder with no additional secret value.
     */
    public PasswordEncoderForOther() {
        this("");
    }

    /**
     * Constructs a standard password encoder with a secret value which is also included in the
     * password hash.
     *
     * @param secret the secret key used in the encoding process (should not be shared)
     */
    public PasswordEncoderForOther(CharSequence secret) {
        this("SHA-256", secret);
    }

    public String encode(CharSequence rawPassword) {
        return encode(rawPassword, saltGenerator.generateKey());
     }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
    	// 如果密码为空则认为是第三方登陆
    	byte[] digested = decode( encodedPassword);
     	byte[] salt = subArray(digested, 0, saltGenerator.getKeyLength());
         return matches(digested, digest("".equals(rawPassword)?"(null)":rawPassword, salt));
    }

    // internal helpers

    private PasswordEncoderForOther(String algorithm, CharSequence secret) {
        this.digester = new Digester(algorithm, DEFAULT_ITERATIONS);
        this.secret = Utf8.encode(secret);
        this.saltGenerator = KeyGenerators.secureRandom();
    }

    private String encode(CharSequence rawPassword, byte[] salt) {
        byte[] digest = digest(rawPassword, salt);
        return new String(Hex.encode(digest));
    }

    private byte[] digest(CharSequence rawPassword, byte[] salt) {
        byte[] digest = digester.digest(concatenate(salt, secret, Utf8.encode(rawPassword)));
        return concatenate(salt, digest);
    }

    private byte[] decode(CharSequence encodedPassword) {
        return Hex.decode(encodedPassword);
    }

    /**
     * Constant time comparison to prevent against timing attacks.
     */
    private boolean matches(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < expected.length; i++) {
            result |= expected[i] ^ actual[i];
        }
        return result == 0;
    }

    private static final int DEFAULT_ITERATIONS = 1024;
	
 



}

