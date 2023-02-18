package com.radcns.bird_plus.config.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * password 인코딩시 사용할 클래스
 * <pre> passwordEncoder의 구현체를 만들고 컴포넌트 어노테이션을 사용하면 </pre>
 * <pre> passwordEncoder 사용시 자동으로 이 클래스를 쓰게 된다. </pre>
 * @author oozu1
 *
 */
@Component
public class PBKDF2Encode implements PasswordEncoder {
	@Value("${jjwt.password.encoder.secret}")
    private String secret;

    @Value("${jjwt.password.encoder.iteration}")
    private Integer iteration;

    @Value("${jjwt.password.encoder.keylength}")
    private Integer keylength;

    /**
     * @param cs password
     * @return encoded password
     */
    @Override
    public String encode(CharSequence cs) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                                            .generateSecret(new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, keylength))
                                            .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean matches(CharSequence cs, String string) {
        return encode(cs).equals(string);
    }
}
