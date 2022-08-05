package com.smart.iot.parking.biz;

import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 *
 * @author Mr.AG
 * @email 
 *@version 2022-08-24 18:05:24
 */
@Service
public class PositionBiz{
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public ParkingAreaBiz parkingAreaBiz;
    @Autowired
    public ParkingIoBiz parkingIoBiz;
    @Autowired
    public ParkingComponentDataBiz parkingComponentDataBiz;
    @Autowired
    public ParkingComponentBiz parkingComponentBiz;

    @Value("${rpc.qr_code_url}")
    private String qr_code_url;
    public void generate_qr_code(HttpServletResponse response, String componentType,String component_id) throws Exception {
        ParkingArea area = new ParkingArea();
        String type = "";
        String path = "";
        if ("parkingSpace".equals(componentType)) {
            ParkingSpace space = parkingSpaceBiz.selectById(component_id);
            area = parkingAreaBiz.selectById(space.getAreaId());
            type = "车位_" + space.getSpaceNum();
            path = path + "parking/parkingSpace/mergeOne/"+space.getSpaceId();
        } else if ("parkingIo".equals(componentType)) {
            ParkingIo io = parkingIoBiz.selectById(component_id);
            area = parkingAreaBiz.selectById(io.getParkingAreaId());
            if (io.getParkingIoType().equals(BaseConstants.parkingIoType.entrance)) {
                type = "parkingIo";
            } else {
                type = "parkingIo";
            }
            path = path + "parking/parkingIo/mergeOne/"+io.getParkingIoId();
        }else if ("componentData".equals(componentType)) {
            ParkingComponentData component_data = parkingComponentDataBiz.selectById(component_id);
            area = parkingAreaBiz.selectById(component_data.getParkingAreaId());
            ParkingComponent component = parkingComponentBiz.selectById(component_data.getParkingComponentId());
            type = "自定义_" + component.getComponentId();
            path = path + "parking/componentData/mergeOne/"+component_data.getComponentDataId();
        }
        String name = area.getAreaName() + "_" + type;
        String destPath = "";
        // 载入图像
        BufferedImage buffImg = null;
        try {
            destPath = QRCodeUtil.encode(path, name, "C:\\", true);
            buffImg = ImageIO.read(new FileInputStream(destPath));
        } catch (Exception e) {

        }
        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        response.reset();
        try {
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(name.getBytes("gb2312"), "ISO8859-1") + ".jpeg");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        try {
            ImageIO.write(buffImg, "jpeg", sos);
            sos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
