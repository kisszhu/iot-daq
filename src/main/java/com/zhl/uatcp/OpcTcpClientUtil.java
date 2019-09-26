package com.zhl.uatcp;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.*;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.EndpointUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program iot-daq
 * @description:OPC UA 客户端读写操作
 * @author: meilong
 * @create: 2019/08/03 08:31
 */
public class OpcTcpClientUtil {

    public static String[] read(UATcpParam param, NodeId[] nodeIds) throws ServiceResultException, UnknownHostException {
        int len = nodeIds.length;
        String[] resValue = new String[len];
        SessionChannel mySession = getSession(param);

        if (mySession == null) {
            return null;
        }
        try {
            ReadValueId[] nodesToRead = new ReadValueId[len];
            for (int i = 0; i < len; i++) {
                ReadValueId r = new ReadValueId(nodeIds[i], Attributes.Value, null, null);
                nodesToRead[i] = r;
            }

            ReadResponse res = mySession.Read(null, null, TimestampsToReturn.Neither, nodesToRead);
            DataValue[] data = res.getResults();
            for (int i = 0; i < len; i++) {
                String val = data[i].getValue().getValue().toString();
                resValue[i] = val;
            }
            return resValue;
        } finally {
            mySession.close();
            mySession.closeAsync();
        }

    }


    public static String[] write(UATcpParam param, WriteValue[] writeValues) throws ServiceResultException, UnknownHostException {
        int len = writeValues.length;

        String[] resValue = new String[len];
        SessionChannel mySession = getSession(param);
        if (mySession == null) {
            return null;
        }
        try {
            WriteResponse res = mySession.Write(null, writeValues);
            StatusCode[] data = res.getResults();
            for (int i = 0; i < len; i++) {
                resValue[i] = data[i].getValue().toString();
            }
            return resValue;
        } finally {
            mySession.close();
            mySession.closeAsync();
        }


    }

    public static SessionChannel getSession(UATcpParam param) throws ServiceResultException, UnknownHostException {

        SessionChannel mySession = null;

        Application myClientApplication = new Application();

        Client myClient = new Client(myClientApplication);

        if (param.getSecurityMode() != null && !param.getSecurityMode().equals(MessageSecurityMode.None)) {

            String hostName = InetAddress.getLocalHost().getHostName();

            String applicationName = param.getApplicationName();

            String applicationUri = "urn:" + hostName + ":OPCUA:" + applicationName;

            KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(applicationName, hostName, applicationUri);

            myClientApplication.setApplicationUri(applicationUri);

            myClientApplication.addApplicationInstanceCertificate(myClientApplicationInstanceCertificate);

            EndpointDescription[] endpoints = myClient.discoverEndpoints(param.getUrl());

            endpoints = EndpointUtil.selectByProtocol(endpoints, "opc.tcp");

            endpoints = EndpointUtil.selectByMessageSecurityMode(endpoints, param.getSecurityMode());

            if (param.getSecurityPolicie() != null) {
                endpoints = EndpointUtil.selectBySecurityPolicy(endpoints, param.getSecurityPolicie());
            }
            endpoints = EndpointUtil.sortBySecurityLevel(endpoints);

            if (endpoints.length == 0) {
                return null;
            }
            EndpointDescription endpoint = endpoints[endpoints.length - 1];
            mySession = myClient.createSessionChannel(endpoint);
        } else {
            mySession = myClient.createSessionChannel(param.getUrl());
        }

        //用户名密码验证֤
        if (param.getAuthentication() == 0) {
            mySession.activate();
        } else if (param.getAuthentication() == 1) {
            mySession.activate(param.getUserName(), param.getPassword());
        } else {
            mySession.activate();
        }
        return mySession;
    }

    public static MessageSecurityMode getMessageSecurityMode(int type) {
        MessageSecurityMode mode = null;
        switch (type) {
            case 1:
                mode = MessageSecurityMode.None;
                break;
            case 2:
                mode = MessageSecurityMode.Sign;
                break;
            case 3:
                mode = MessageSecurityMode.SignAndEncrypt;
                break;
            default:
                mode = MessageSecurityMode.None;
                break;
        }
        return mode;
    }

    public static SecurityPolicy getSecurityPolicy(int type) {
        SecurityPolicy policy = null;
        switch (type) {
            case 1:
                policy = SecurityPolicy.BASIC128RSA15;
                break;
            case 2:
                policy = SecurityPolicy.BASIC256;
                break;
            case 3:
                policy = SecurityPolicy.BASIC256SHA256;
                break;
            default:
                policy = SecurityPolicy.BASIC128RSA15;
                break;
        }
        return policy;
    }

    public static int getNs(String nodeIdStr) {
        String[] addrArr = nodeIdStr.split(";");
        String[] nsArr = addrArr[0].split("=");
        return Integer.parseInt(nsArr[1]);
    }

    public static String getId(String nodeIdStr) {
        String[] addrArr = nodeIdStr.split(";");
        String[] idArr = addrArr[1].split("=");
        return idArr[1];
    }

    public static IdType getIdType(String nodeIdStr) {
        String[] addrArr = nodeIdStr.split(";");
        String[] addr = addrArr[1].split("=");
        String type = addr[0];
        if (type.equals("i")) {
            return IdType.Numeric;
        }
        if (type.equals("s")) {
            return IdType.String;
        }
        if (type.equals("g")) {
            return IdType.Guid;
        }
        if (type.equals("b")) {
            return IdType.Opaque;
        }
        return IdType.String;
    }
}
