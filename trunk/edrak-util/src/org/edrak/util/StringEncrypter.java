package org.edrak.util;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;


public class StringEncrypter
{
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";

    public static final String DES_ENCRYPTION_SCHEME = "DES";

   
    //public static final String DEFAULT_ENCRYPTION_KEY = "123456789012345678901234";
    public static final String DEFAULT_ENCRYPTION_KEY = "452yriuylsfjhg;[;rrgeu0'";
    private KeySpec keySpec;

    private SecretKeyFactory keyFactory;

    private Cipher cipher;

    private static final String UNICODE_FORMAT = "UTF-8";

    private static StringEncrypter _instance;
    
    

    public synchronized static StringEncrypter getInstance()
    {
        if (_instance == null)
        {
            try
            {
                _instance = new StringEncrypter(DESEDE_ENCRYPTION_SCHEME);
            }
            catch (EncryptionException e)
            {
                e.printStackTrace();
            }
        }

        return _instance;
    }

    private StringEncrypter(String encryptionScheme) throws EncryptionException
    {
        this(encryptionScheme, DEFAULT_ENCRYPTION_KEY);
    }

    private StringEncrypter(String encryptionScheme, String encryptionKey) throws EncryptionException
    {

        if (encryptionKey == null) throw new IllegalArgumentException("encryption key was null");
        if (encryptionKey.trim().length() < 24) throw new IllegalArgumentException("encryption key was less than 24 characters");

        try
        {   
             
            byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);

            if (encryptionScheme.equals(DESEDE_ENCRYPTION_SCHEME))
            {
                keySpec = new DESedeKeySpec(keyAsBytes);
            }
            else if (encryptionScheme.equals(DES_ENCRYPTION_SCHEME))
            {
                keySpec = new DESKeySpec(keyAsBytes);
            }
            else
            {
                throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
            }
            keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
            cipher = Cipher.getInstance(encryptionScheme);
        }
        catch (InvalidKeyException e)
        {
            throw new EncryptionException(e);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new EncryptionException(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new EncryptionException(e);
        }
        catch (NoSuchPaddingException e)
        {
            throw new EncryptionException(e);
        }
    }

    public String encrypt(String unencryptedString) throws EncryptionException
    {
        if (unencryptedString == null || unencryptedString.trim().length() == 0) throw new IllegalArgumentException("unencrypted string was null or empty");
        try
        {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] ciphertext = cipher.doFinal(cleartext);

           
            return bytes2String(Base64UrlSafe.encodeBase64(ciphertext));
        }
        catch (Exception e)
        {
            throw new EncryptionException(e);
        }
    }

    public String decrypt(String encryptedString) throws EncryptionException
    {
        if (encryptedString == null || encryptedString.trim().length() <= 0) throw new IllegalArgumentException("encrypted string was null or empty");
        try
        {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            byte[] cleartext = Base64UrlSafe.decodeBase64(encryptedString.getBytes());;
            byte[] ciphertext = cipher.doFinal(cleartext);
            return bytes2String(ciphertext);
        }
        catch (Exception e)
        {
            throw new EncryptionException(e);
        }
    }

    private static String bytes2String(byte[] bytes)
    {
        try
        {
            return new String(bytes, UNICODE_FORMAT);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    public static class EncryptionException extends Exception
    {
        /**
	 * 
	 */
	private static final long serialVersionUID = -6939850839908197265L;

	public EncryptionException(Throwable t)
        {
            super(t);
        }
    }

}
