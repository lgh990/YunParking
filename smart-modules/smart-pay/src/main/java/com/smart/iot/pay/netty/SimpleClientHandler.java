package com.smart.iot.pay.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    private byte[] returnData;

    /**
     * 本方法用于接收服务端发送过来的消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("SimpleClientHandler.channelRead");
//        ByteBuf result = (ByteBuf) msg;
//        byte[] result1 = new byte[result.readableBytes()];
//        result.readBytes(result1);
//        System.out.println("Server message:" + new String(result1));
//        result.release();
        byte[] params = (byte[])msg;
        byte[] returnData = new byte[100];

        //STX
        int stx1 = params[0];
        int stx2 = params[1];

        //RSCTL
        int rsctl = params[2];
        rsctl++;

        //LEN
        int len1h = params[4]&0xFF;
        int len2h = (params[3]&0xFF)<<8;
        int len = len1h+len2h;

        //CMD
        int cmd = params[5]&0xFF;

        //data
        int status = params[6];

        //校验异或值
        if ((int)params[params.length-1] == getXor(params)){
            //data
            switch (cmd){
                case 240:
                    if(status == 0){
                        log.info("=========================etc心跳帧======================");
                    }else if (status == 1){
                        log.info("========================etc心跳有误======================");
                    }
                    break;
                case 241:
                    if(status==0){
                        String OBUID = Integer.toHexString((params[7]&0xFF)+((params[8]&0xFF)<<8)+((params[9]&0xFF)<<16)+((params[10]&0xFF)<<24));
                        int stationType = params[11];
                        int transId = params[19]+params[18]+params[17]+params[16]+params[15]+params[14]+params[13]+params[12];
                        String vehiclePlate = getString(params,20,31);
                        String cardId = getString(params,32,51);
                        int carType = params[52];
                        int carColor = params[53];
                    }
                    break;
                case 242:
                    if(status==0){
                        String OBUID = Integer.toHexString((params[7]&0xFF)+((params[8]&0xFF)<<8)+((params[9]&0xFF)<<16)+((params[10]&0xFF)<<24));
                        int stationType = params[11];
                        String cardId = getString(params,12,31);
                        String vehiclePlate = getString(params,32,43);
                        String transId = getString(params,44,51);
                        double account = getDouble(params,52,55);
                    }
                    break;
                case 230:
                    if(status == 0){
                        log.info("=========================停止交易=========================");
                    }else if (status == 1){
                        log.info("=========================开始交易=========================");
                    }
                    break;
            }
        }

    }

    /**
     * 本方法用于处理异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 本方法用于向服务端发送信息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf encoded = ctx.alloc().buffer(4 * returnData.length);
        encoded.writeBytes(returnData);
        ctx.write(encoded);
        ctx.flush();
    }

    public byte[] getHandBytes(int length){
        byte[] bytes = new byte[length];
        //STX
        bytes[0] = (byte)0xAA;
        bytes[1] = (byte)0x55;
        //RSCTL
        bytes[2] = 0x00;
        //len
        bytes[3] = (byte)(length&0xFF);
        bytes[4] = (byte)((length&0xFF00)>>8);

        return bytes;
    }

    /**
     * c6指令数据包装
     * @param flag 开始结束标识
     */
    public void c6(int flag){
        byte[] c6 = getHandBytes(8);
        //CMD
        c6[5] = (byte)0xC6;
        //data
        if(flag == 0){
            c6[6] = 0x00;
        }else if(flag ==1){
            c6[6] = 0x01;
        }
        //BCC
        c6[c6.length-1] = (byte)getXor(c6);
    }

    /**
     * e0数据包装
     * @param status
     * @param restart
     */
    public void e0(int status,int restart){
        byte[] bytes = getHandBytes(13);
        //CMD
        bytes[5] = (byte)0xE0;
        //DATA
        bytes[6] = (byte) status;
        getUnixTime(bytes,7);
        bytes[11] = (byte) restart;
        //BCC
        bytes[bytes.length-1] = (byte)getXor(bytes);
    }


    public void e1(int status,byte[] transid,byte[] obuid,byte stationType,byte[] vehiclePlate){
        byte[] bytes = getHandBytes(65);
        //CMD
        bytes[5] = (byte)0xE1;
        //DATA
        bytes[6] = (byte) status;

        //obuid(7-10)
        int k = 7;
        for (int i=0;i<obuid.length;i++){
            bytes[k] = obuid[i];
            k++;
        }

        //station type(11)
        bytes[11] = stationType;

        //vehicle plate(12-23)
        k = 12;
        for (int i=0;i<vehiclePlate.length;i++){
            bytes[k] = vehiclePlate[i];
            k++;
        }

        //transid(24-31)
        k = 24;
        for (int i=0;i<vehiclePlate.length;i++){
            bytes[k] = vehiclePlate[i];
            k++;
        }

//        //Entry_Time(40-43)
//        bytes[40];
//        bytes[41];
//        bytes[42];
//        bytes[43];

        if(stationType==0x00){

//        //entry transid(32-39)
//        bytes[32];
//        bytes[33];
//        bytes[34];
//        bytes[35];
//        bytes[36];
//        bytes[37];
//        bytes[38];
//        bytes[39];

//        //account payable(44-47)
//        bytes[44];
//        bytes[45];
//        bytes[46];
//        bytes[47];
//
//        //account real(48-51)
//        bytes[48];
//        bytes[49];
//        bytes[50];
//        bytes[51];
//
//        //amount discount(52-55)
//        bytes[52];
//        bytes[53];
//        bytes[54];
//        bytes[55];

        }


        //trans time
        getUnixTime(bytes,56);

        //BCC
        bytes[bytes.length-1] = (byte)getXor(bytes);
    }

    public void e2(int status,byte[] vehiclePlate,byte[] transid){
        byte[] bytes = getHandBytes(27);
        //CMD
        bytes[5] = (byte)0xE2;
        //DATA
        bytes[6] = (byte) status;

        //result
        bytes[7] = 0x00;

        //transid (8-15)
        int k = 8;
        for (int i=0;i<transid.length;i++){
            bytes[k] = transid[i];
            k++;
        }

        //vehicle plate(16-27)
        int j = 16;
        for (int i=0;i<vehiclePlate.length;i++){
            bytes[j] = vehiclePlate[i];
            j++;
        }

        //BCC
        bytes[bytes.length-1] = (byte)getXor(bytes);

    }

    /**
     * 获取异或值
     * @param params
     * @return
     */
    public int getXor(byte[] params){
        int a = params[0]^params[1];
        for (int i = 2; i < params.length-1; i++) {
            a = a^params[i];
        }
        return a;
    }

    /**
     * 获取车牌字符串
     * @param params
     * @param startPoint
     * @param endPoint
     * @return
     */
    public String getString(byte[] params,int startPoint,int endPoint){
        String carCodenew = "";
        byte[] bytes = new byte[endPoint-startPoint];
        for(int i = new Integer(endPoint);i>=startPoint;i--){
            int a = 0;
            bytes[a] = params[i];
            a++;
        }
        try {
            carCodenew = new String(bytes,"GB2312");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return carCodenew;
    }

    /**
     * 获取double数据
     * @param params
     * @param startPoint
     * @param endPoint
     * @return
     */
    public double getDouble(byte[] params,int startPoint,int endPoint) {
        byte[] bytes = new byte[endPoint-startPoint];
        for(int i = new Integer(endPoint);i>=startPoint;i--){
            int a = 0;
            bytes[a] = params[i];
            a++;
        }
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (bytes[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    /**
     * 获取16进制当前时间
     * @return
     */
    public byte[] getUnixTime(byte[] bytes,int startPoint){
        Date date = new Date();
        long longDate = date.getTime();
        bytes[startPoint]=(byte) (longDate&0xFF);
        bytes[startPoint++]=(byte) ((longDate&0xFF00)>>8);
        bytes[startPoint++]=(byte) ((longDate&0xFF0000)>>16);
        bytes[startPoint++]=(byte) ((longDate&0xFF000000)>>24);
        return bytes;
    }

}
