package com.gzcc.controller.sponsor;

import com.gzcc.utils.QRCodeUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ASUS on 2019/12/4.
 */
@RestController
public class ActivityController {

//    @ApiOperation(value = "活动签到",notes = "返回二维码给学生进行扫码签到")
//    public ResponseEntity<String> activitySignIn() throws Exception {
//
//
//        // 存放在二维码中的内容
//        String text = "我是小铭";
//        // 嵌入二维码的图片路径
//        String imgPath = "G:/qrCode/dog.jpg";
//        // 生成的二维码的路径及名称
//        String destPath = "G:/qrCode/qrcode/jam.jpg";
//        //生成二维码
//        QRCodeUtil.encode(text, imgPath, destPath, true);
//        // 解析二维码
//        String str = QRCodeUtil.decode(destPath);
//        // 打印出解析出的内容
//        System.out.println(str);
//
//    }
//    @ApiOperation(value = "活动签退",notes = "后端接收前端扫码发送的")
//    public ResponseEntity<String> activitySignOut(){
//
//    }
}
