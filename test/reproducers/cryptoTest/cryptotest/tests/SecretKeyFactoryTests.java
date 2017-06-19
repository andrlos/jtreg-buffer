package cryptotest.tests;

import cryptotest.utils.AlgorithmInstantiationException;
import cryptotest.utils.AlgorithmRunException;
import cryptotest.utils.AlgorithmTest;
import cryptotest.utils.TestResult;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

public class SecretKeyFactoryTests extends AlgorithmTest {
    private Random random = new SecureRandom(new byte[]{6, 6, 6});

    public static void main(String[] args) {
        TestResult r = new SecretKeyFactoryTests().mainLoop();
        System.out.println(r.getExplanation());
        System.out.println(r.toString());
        r.assertItself();
    }

    @Override
    protected void checkAlgorithm(Provider.Service service, String alias) throws AlgorithmInstantiationException,
            AlgorithmRunException {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(alias, service.getProvider());
            KeySpec keySpec;

            // order of conditions is important!
            if (service.getAlgorithm().contains("PBE")) {
                keySpec = new PBEKeySpec(new char[]{'h', 'e', 's', 'l', 'o'});
            } else if (service.getAlgorithm().contains("DESede")) {
                keySpec = new DESedeKeySpec(generateBytes(24));
            } else if (service.getAlgorithm().contains("DES")) {
                keySpec = new DESKeySpec(generateBytes(8));
            } else if (service.getAlgorithm().contains("PBKDF2")) {
                keySpec = new PBEKeySpec(new char[]{'h', 'e', 's', 'l', 'o'}, generateBytes(8), 1, 1);
            } else if (service.getAlgorithm().contains("AES")) {
                keySpec = new SecretKeySpec(generateBytes(16), service.getAlgorithm());
            } else if (service.getAlgorithm().contains("ARCFOUR")) {
                keySpec = new SecretKeySpec(generateBytes(8), service.getAlgorithm());
            } else  {
                keySpec = null;
            }

            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

            if (secretKey == null || secretKeyFactory.translateKey(secretKey) == null) {
                throw new UnsupportedOperationException("Generated key is null for " + service.getAlgorithm() + " in"
                        + service.getProvider().getName());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new AlgorithmInstantiationException(e);
        } catch (UnsupportedOperationException | InvalidKeySpecException | InvalidKeyException e) {
            throw new AlgorithmRunException(e);
        }
    }

    private byte[] generateBytes(int length) {
        byte[] key = new byte[length];
        random.nextBytes(key);
        return key;
    }
}
