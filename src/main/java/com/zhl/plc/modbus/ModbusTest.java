package com.zhl.plc.modbus;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.*;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * @program iot-daq
 * @description:
 * @author: meilong
 * @create: 2019/07/22 08:49
 */
public class ModbusTest {

    /**
     * 批量写数据到保持寄存器
     *
     * @param ip      从站IP
     * @param port    modbus端口
     * @param slaveId 从站ID
     * @param start   起始地址偏移量
     * @param values  待写数据
     */
    public static void modbusWTCP(String ip, int port, int slaveId, int start, short[] values) {
        ModbusFactory modbusFactory = new ModbusFactory();
        // 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502
        IpParameters params = new IpParameters();
        params.setHost(ip);
        // 设置端口，默认502
        if (502 != port) {
            params.setPort(port);
        }
        ModbusMaster tcpMaster = null;
        // 参数1：IP和端口信息 参数2：保持连接激活
        tcpMaster = modbusFactory.createTcpMaster(params, true);
        try {
            tcpMaster.init();
            System.out.println("=======初始化成功========");
        } catch (ModbusInitException e) {
            System.out.println("初始化异常");
        }
        try {
            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, start, values);
            WriteRegistersResponse response = (WriteRegistersResponse) tcpMaster.send(request);
            if (response.isException()) {
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            } else {
                System.out.println("Success");
            }
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
    }


    /**
     * 读保持寄存器上的内容
     *
     * @param ip        从站IP
     * @param port      modbus端口
     * @param start     起始地址偏移量
     * @param readLenth 待读寄存器个数
     * @return
     */
    public static ByteQueue modbusTCP(String ip, int port, int start, int readLenth) {
        ModbusFactory modbusFactory = new ModbusFactory();
        // 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502
        IpParameters params = new IpParameters();
        params.setHost(ip);
        //设置端口，默认502
        if (502 != port) {
            params.setPort(port);
        }
        ModbusMaster tcpMaster = null;
        tcpMaster = modbusFactory.createTcpMaster(params, true);
        try {
            tcpMaster.init();
            System.out.println("========初始化成功=======");
        } catch (ModbusInitException e) {
            e.printStackTrace();
        }
        ModbusRequest modbusRequest = null;
        try {
            //功能码03   读取保持寄存器的值
            modbusRequest = new ReadHoldingRegistersRequest(1, start, readLenth);
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        ModbusResponse modbusResponse = null;
        try {
            modbusResponse = tcpMaster.send(modbusRequest);
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        ByteQueue byteQueue =
                new ByteQueue(1024);
        modbusResponse.write(byteQueue);
        System.out.println("功能码:" + modbusRequest.getFunctionCode());
        System.out.println("从站地址:" + modbusRequest.getSlaveId());
        System.out.println("收到的响应信息大小:" + byteQueue.size());
        System.out.println("收到的响应信息值:" + byteQueue);
        return byteQueue;
    }

    public static void main(String[] args) {
        String ip = "192.168.1.12";
        int port = 502;
        int start = 0;
        int readLength = 9;
        ByteQueue queue = modbusTCP(ip, port, start, readLength);
        System.out.println(queue.toString());
    }
}