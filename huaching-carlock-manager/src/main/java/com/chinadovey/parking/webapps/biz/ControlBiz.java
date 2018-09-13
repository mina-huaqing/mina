package com.chinadovey.parking.webapps.biz;
/**
 * 车锁管控业务层（包括网关、车锁的配置以及控制）
 * 请不要对这里代码进行增删改操作，谢谢！
 * @author feng
 *
 */
public interface ControlBiz{
	
	/**
	 * 车位锁控制1 表示开，2 表示关
	 * @param slaveId  车锁编号
	 * @param acton  指令
	 * @return
	 */
	public Integer operate(String slaveId,int action);
	/**
	 * 车位锁配置3表示自动，4表示手动
	 * @param slaveId  车锁编号
	 * @param acton  指令
	 * @return
	 */
	public Integer autoOrHand(String slaveId,int action);
	
	/**
	 * 车锁-网关关系的配置
	 * @param dasId    网关编号
	 * @param slaveId  车锁编号
	 * @param serial   串口编号
	 * @return
	 */
	public Integer carlockConfig(String dasId,String slaveId,String serial);
	/**
	 * 车锁-网关关系的配置的删除
	 * @param dasId    网关编号
	 * @param slaveId  车锁编号
	 * @return
	 */
	public Integer carlockDelConfig(String dasId,String slaveId);
	/**
	 * 网关串口的配置  
	 * @param dasId 网关编号
	 * @param wireA 无线模块A
	 * @param channelA 信道A
	 * @param wireB 无线模块B
	 * @param channelB 信道B
	 * @return
	 * 0表示 串口1,2都配置成功
	 * 1表示 串口1配置成功
	 * 2表示 串口2配置成功
	 * 3表示 串口1,2都配置失败
	 */
	public Integer  gatewayConfig(String dasId,String wireA,String channelA,String wireB,String channelB);
	/**
	 * 修改 车位锁有车、无车的状态
	 * @param dasId  网关编号
	 * @param slaveId  车位锁编号
	 * @param action  指令  00H:无车    01H:有车
	 * @return  0:设置成功   1:设置失败
	 */
	public Integer updateCarState(String dasId,String slaveId,int action);
	/**
	 * 重启网关
	 * @param dasId  网关编号
	 * @return  0:重启成功   1:重启失败
	 */
	public Integer rebootGateway(String dasId);
	/**
	 * 喇叭 打开
	 * @param dasId  网关编号
	 * @param slaveId  车位锁编号
	 * @param action  指令  00H:无车    01H:有车
	 * @return  0:设置成功   1:设置失败
	 */
	public Integer soundControl(String dasId,String slaveId,int action);
	/**
	 * 灯效打开
	 * @param dasId  网关编号
	 * @param slaveId  车位锁编号
	 * @param action  指令  00H:无车    01H:有车
	 * @return  0:设置成功   1:设置失败
	 */
	public Integer lightControl(String dasId,String slaveId,int action);
	/**
	 * 喇叭、灯效打开
	 * @param dasId  网关编号
	 * @param slaveId  车位锁编号
	 * @param action  指令  00H:无车    01H:有车
	 * @return  0:设置成功   1:设置失败
	 */
	public Integer soundLightControl(String dasId,String slaveId,int action);
	


}
