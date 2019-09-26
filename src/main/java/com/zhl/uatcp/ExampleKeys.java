package com.zhl.uatcp;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.utils.CertificateUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;


/**
 * @program iot-daq
 * @description:用来生成客户端凭证
 * @author: meilong
 * @create: 2019/08/03 08:29
 */
public class ExampleKeys {

    private static final String PRIVKEY_PASSWORD = "Opc.Ua";

    public static KeyPair getCert(String applicationName, String hostName, String applicationUri) throws ServiceResultException {
        File certFile = new File(applicationName + ".der");
        File privKeyFile = new File(applicationUri + ".pem");
        try {
            Cert myCertificate = Cert.load(certFile);
            PrivKey myPrivateKey = PrivKey.load(privKeyFile, PRIVKEY_PASSWORD);
            return new KeyPair(myCertificate, myPrivateKey);
        } catch (CertificateException e) {
            throw new ServiceResultException(e);
        } catch (IOException e) {
            try {
                KeyPair keys = CertificateUtils.createApplicationInstanceCertificate(applicationName, null, applicationUri, 3650, hostName);
                keys.getCertificate().save(certFile);
                keys.getPrivateKey().save(privKeyFile);
                return keys;
            } catch (Exception e1) {
                throw new ServiceResultException(e1);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceResultException(e);
        } catch (InvalidKeyException e) {
            throw new ServiceResultException(e);
        } catch (InvalidKeySpecException e) {
            throw new ServiceResultException(e);
        } catch (NoSuchPaddingException e) {
            throw new ServiceResultException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new ServiceResultException(e);
        } catch (IllegalBlockSizeException e) {
            throw new ServiceResultException(e);
        } catch (BadPaddingException e) {
            throw new ServiceResultException(e);
        } catch (InvalidParameterSpecException e) {
            throw new ServiceResultException(e);
        }
    }
}

