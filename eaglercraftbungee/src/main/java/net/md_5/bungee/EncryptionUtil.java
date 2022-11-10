// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.md_5.bungee.protocol.packet.PacketFCEncryptionResponse;
import net.md_5.bungee.protocol.packet.PacketFDEncryptionRequest;

public class EncryptionUtil
{
    private static final Random random;
    public static KeyPair keys;
    private static SecretKey secret;
    
    public static PacketFDEncryptionRequest encryptRequest() {
        final String hash = BungeeCord.getInstance().config.isOnlineMode() ? Long.toString(EncryptionUtil.random.nextLong(), 16) : "-";
        final byte[] pubKey = EncryptionUtil.keys.getPublic().getEncoded();
        final byte[] verify = new byte[4];
        EncryptionUtil.random.nextBytes(verify);
        return new PacketFDEncryptionRequest(hash, pubKey, verify);
    }
    
    public static SecretKey getSecret(final PacketFCEncryptionResponse resp, final PacketFDEncryptionRequest request) throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, EncryptionUtil.keys.getPrivate());
        final byte[] decrypted = cipher.doFinal(resp.getVerifyToken());
        if (!Arrays.equals(request.getVerifyToken(), decrypted)) {
            throw new IllegalStateException("Key pairs do not match!");
        }
        cipher.init(2, EncryptionUtil.keys.getPrivate());
        return new SecretKeySpec(cipher.doFinal(resp.getSharedSecret()), "AES");
    }
    
    public static Cipher getCipher(final int opMode, final Key shared) throws GeneralSecurityException {
        final Cipher cip = Cipher.getInstance("AES/CFB8/NoPadding");
        cip.init(opMode, shared, new IvParameterSpec(shared.getEncoded()));
        return cip;
    }
    
    public static PublicKey getPubkey(final PacketFDEncryptionRequest request) throws GeneralSecurityException {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(request.getPublicKey()));
    }
    
    public static byte[] encrypt(final Key key, final byte[] b) throws GeneralSecurityException {
        final Cipher hasher = Cipher.getInstance("RSA");
        hasher.init(1, key);
        return hasher.doFinal(b);
    }
    
    public static SecretKey getSecret() {
        return EncryptionUtil.secret;
    }
    
    static {
        random = new Random();
        EncryptionUtil.secret = new SecretKeySpec(new byte[16], "AES");
        try {
            EncryptionUtil.keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
