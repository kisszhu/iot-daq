package com.zhl.uatcp;

/**
 * @program iot-daq
 * @description:测试客户端类
 * @author: meilong
 * @create: 2019/08/03 08:32
 */

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.WriteValue;

import java.net.UnknownHostException;

public class TestOpcTcpClientUtil {

    private final static String url = "opc.tcp://Atiger:53530/OPCUA/SimulationServer";

    private static void testRead() throws ServiceResultException, UnknownHostException {
        NodeId[] nodeIds = new NodeId[2];

        nodeIds[0] = NodeId.get(IdType.String, 5, "Random1");
        nodeIds[1] = NodeId.get(IdType.String, 5, "Counter1");

        UATcpParam param = new UATcpParam();
        param.setUrl(url);
        param.setAuthentication(0);
        //param.setUserName("admin");
        //param.setPassword("admin");
        param.setSecurityMode(MessageSecurityMode.None);
        //param.setSecurityPolicie(SecurityPolicy.BASIC128RSA15);

        String[] result = OpcTcpClientUtil.read(param, nodeIds);
        for (String rs : result) {
            System.out.println(rs);
        }
    }


    private static void testWrite() throws ServiceResultException, UnknownHostException {
        UATcpParam param = new UATcpParam();
        param.setUrl(url);
        param.setAuthentication(0);
        //param.setUserName("admin");
        //param.setPassword("admin");

        param.setSecurityMode(MessageSecurityMode.None);
        //param.setSecurityPolicie(SecurityPolicy.BASIC128RSA15);

        NodeId nodeId1 = NodeId.get(IdType.String, 5, "Random1");
        NodeId nodeId2 = NodeId.get(IdType.String, 5, "Counter1");

        WriteValue v1 = new WriteValue(nodeId1, Attributes.Value, null, new DataValue(new Variant(new Double(0.5))));
        WriteValue v2 = new WriteValue(nodeId2, Attributes.Value, null, new DataValue(new Variant(300)));

        WriteValue[] nodesToWrite = {v1, v2};
        String[] result = OpcTcpClientUtil.write(param, nodesToWrite);
        for (String rs : result) {
            System.out.println(rs);
        }
    }

    private static void testReadNodeStr() throws UnknownHostException, ServiceResultException {
        UATcpParam param = new UATcpParam();
        param.setUrl(url);
        param.setAuthentication(0);
        //param.setUserName("admin");
        //param.setPassword("admin");

        param.setSecurityMode(MessageSecurityMode.None);
        //param.setSecurityPolicie(SecurityPolicy.BASIC128RSA15);

        String nodeIdStr1 = "ns=5;s=Random1";
        String nodeIdStr2 = "ns=5;s=Counter1";

        NodeId[] nodeIds = new NodeId[2];


        nodeIds[0] = NodeId.get(OpcTcpClientUtil.getIdType(nodeIdStr1), OpcTcpClientUtil.getNs(nodeIdStr1), OpcTcpClientUtil.getId(nodeIdStr1));
        nodeIds[1] = NodeId.get(OpcTcpClientUtil.getIdType(nodeIdStr2), OpcTcpClientUtil.getNs(nodeIdStr2), OpcTcpClientUtil.getId(nodeIdStr2));

        String[] result = OpcTcpClientUtil.read(param, nodeIds);
        for (String rs : result) {
            System.out.println(rs);
        }

    }

    public static void main(String[] args) {
        try {
            testWrite();
            testRead();
            testReadNodeStr();
        } catch (ServiceResultException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}