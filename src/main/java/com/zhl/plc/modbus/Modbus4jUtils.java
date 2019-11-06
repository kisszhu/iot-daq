package com.zhl.plc.modbus;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.locator.BaseLocator;

/**
 * @program iot-daq
 * @description:
 * @author: meilong
 * @create: 2019/11/05 17:37
 */
public class Modbus4jUtils {

    /**
     * 定义Modbus工厂
     */
    static ModbusFactory modbusFactory;

    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    /**
     * 获取master
     *
     * @return
     * @throws ModbusInitException
     */
    public static ModbusMaster getMaster() throws ModbusInitException {
        // 指定IP地址和端口
        IpParameters params = new IpParameters();
        params.setHost("localhost");
        params.setPort(502);

        // modbusFactory.createRtuMaster(wapper); //RTU 协议
        // modbusFactory.createUdpMaster(params);//UDP 协议
        // modbusFactory.createAsciiMaster(wrapper);//ASCII 协议

        // 使用TCP协议
        ModbusMaster master = modbusFactory.createTcpMaster(params, false);
        master.init();
        return master;
    }

    /**
     * 读取功能码01类型的数据
     * Coil：线圈
     * 数据类型：ON：1、OFF：0
     * 读取[01 Coil Status 0x]类型 开关数据
     *
     * @param slaveId slaveId
     * @param offset  位置
     * @return 读取值
     * @throws ModbusTransportException 异常
     * @throws ErrorResponseException   异常
     * @throws ModbusInitException      异常
     */
    public static Boolean readCoilStatus(int slaveId, int offset)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 从站偏号、偏移位置
        BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
        Boolean value = getMaster().getValue(loc);
        return value;
    }

    /**
     * 读取功能码02类型的数据
     * Input：读离散量输入
     * 数据类型：ON：1、OFF：0
     * 读取[02 Input Status 1x]类型 开关数据
     *
     * @param slaveId
     * @param offset
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public static Boolean readInputStatus(int slaveId, int offset)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 从站偏号、偏移位置
        BaseLocator<Boolean> loc = BaseLocator.inputStatus(slaveId, offset);
        Boolean value = getMaster().getValue(loc);
        return value;
    }

    /**
     * 读取功能码03类型的数据
     * holding：读保持寄存器
     * 数据类型：指定数据类型（可以是float类型）
     * 读取[03 Holding Register类型 2x]模拟量数据
     *
     * @param slaveId  slave Id
     * @param offset   位置
     * @param dataType 数据类型,来自com.serotonin.modbus4j.code.DataType
     * @return
     * @throws ModbusTransportException 异常
     * @throws ErrorResponseException   异常
     * @throws ModbusInitException      异常
     */
    public static Number readHoldingRegister(int slaveId, int offset, int dataType)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 从站偏号、偏移位置、数据类型
        BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, dataType);
        Number value = getMaster().getValue(loc);
        return value;
    }

    /**
     * 读取功能码04类型的数据
     * input：读输入寄存器
     * 数据类型；指定数据类型（可以是float类型）
     * 读取[04 Input Registers 3x]类型 模拟量数据
     *
     * @param slaveId  slaveId
     * @param offset   位置
     * @param dataType 数据类型,来自com.serotonin.modbus4j.code.DataType
     * @return 返回结果
     * @throws ModbusTransportException 异常
     * @throws ErrorResponseException   异常
     * @throws ModbusInitException      异常
     */
    public static Number readInputRegisters(int slaveId, int offset, int dataType)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 从站偏好、偏移位置、数据类型
        BaseLocator<Number> loc = BaseLocator.inputRegister(slaveId, offset, dataType);
        Number value = getMaster().getValue(loc);
        return value;
    }

    /**
     * 批量读取使用方法
     *
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public static void batchRead() throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 批量读取
        BatchRead<Integer> batch = new BatchRead<Integer>();

        batch.addLocator(0, BaseLocator.holdingRegister(1, 1, DataType.FOUR_BYTE_FLOAT));
        batch.addLocator(1, BaseLocator.inputStatus(1, 0));
        batch.setContiguousRequests(false);

        // 添加多个要读取的变量地址，一起发送
        ModbusMaster master = getMaster();
        BatchResults<Integer> results = master.send(batch);
        System.out.println(results.getValue(0));
        System.out.println(results.getValue(1));
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        try {

            // 01测试
            Boolean v011 = readCoilStatus(1, 0);
            Boolean v012 = readCoilStatus(1, 1);
            Boolean v013 = readCoilStatus(1, 6);
            System.out.println("v011:" + v011);
            System.out.println("v012:" + v012);
            System.out.println("v013:" + v013);

            // 02测试
            Boolean v021 = readInputStatus(1, 0);
            Boolean v022 = readInputStatus(1, 1);
            Boolean v023 = readInputStatus(1, 2);
            System.out.println("v021:" + v021);
            System.out.println("v022:" + v022);
            System.out.println("v023:" + v023);

            // 03测试
            Number v031 = readHoldingRegister(1, 1, DataType.FOUR_BYTE_FLOAT);
            Number v032 = readHoldingRegister(1, 3, DataType.FOUR_BYTE_FLOAT);
            System.out.println("v031:" + v031);
            System.out.println("v032:" + v032);

            // 04测试
            Number v041 = readInputRegisters(1, 0, DataType.FOUR_BYTE_FLOAT);
            Number v042 = readInputRegisters(1, 2, DataType.FOUR_BYTE_FLOAT);
            System.out.println("v041:" + v041);
            System.out.println("v042:" + v042);

            // 批量读取
            batchRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
