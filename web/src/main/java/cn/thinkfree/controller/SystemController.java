package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.ActivationCodeVO;
import cn.thinkfree.database.vo.IndexMenuVO;
import cn.thinkfree.service.cache.RedisService;
import cn.thinkfree.service.index.IndexService;
import cn.thinkfree.service.user.UserService;
import cn.thinkfree.service.utils.ActivationCodeHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Api(description = "系统相关操作")
@RestController
public class SystemController extends AbsBaseController {


    @Autowired
    IndexService indexService;

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;


    /**
     * 获取首页菜单
     * @return
     */
    @ApiOperation(value = "获取菜单",notes = "根据当前登录用户信息获取菜单")
    @GetMapping("/menu")
    @MyRespBody
    public MyRespBundle<List<IndexMenuVO>> menu(){
        List<IndexMenuVO> indexMenuVOS = indexService.listIndexMenu();
        return sendJsonData(ResultMessage.SUCCESS,indexMenuVOS);
    }


    /**
     * 验证激活码
     * @param email
     * @param code
     * @return
     */
    @PostMapping("/validateCode")
    @MyRespBody
    @ApiOperation(value="验证激活码是否有效", notes="验证激活码是否有效,返回:验证成功!\n\r验证失败!")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "email", value = "手机号,邮箱等", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "code", value = "激活码", required = true, dataType = "String")
    })
    public MyRespBundle<String> validateCode(String email,String code){
        String mes = redisService.validate(email,code);
        return sendSuccessMessage(mes);
    }

    /**
     * 获取激活码
     * @return
     */
    @GetMapping("/validateCode")
    @MyRespBody
    @ApiOperation(value="获取激活码", notes="获取激活码,返回激活码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "key", value = "手机号,邮箱等", required = true, dataType = "String"),
    })
    public MyRespBundle<String> validateCode(String key){
        try {
            String code = redisService.saveVerificationCode(key);
            return sendSuccessMessage(code);
        }catch (Exception e){
            return sendFailMessage(ERROR.message);
        }
    }

    /**
     *  简易验证码
     * @return
     */
    @GetMapping("/valicode")
    @ApiOperation(value="验证码", notes="获取验证码")
    public ResponseEntity<byte[]> valicode() throws IOException {
        String randomCode = ActivationCodeHelper.ActivationCode.Mix.code.get();
        byte[] body = createImage(randomCode);

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.valueOf("image/png"));
        bodyBuilder.contentLength(body.length);
        ResponseEntity<byte[]> entity = bodyBuilder.body(body);
        return entity;
    }



    /**
     *  base64格式验证码
     * @return
     */
    @GetMapping("/validateCodeBody")
    @MyRespBody
    @ApiOperation(value="获取验证码和验证码图片", notes="获取验证码和图片")
    public MyRespBundle<ActivationCodeVO>  validateCodeBody() throws IOException {
        String randomCode = ActivationCodeHelper.ActivationCode.Mix.code.get();
        byte[] body = createImage(randomCode);
        String base64 =new BASE64Encoder().encode(body);
        return sendJsonData(ResultMessage.SUCCESS,new ActivationCodeVO(randomCode,base64.replace("\r\n","")));
    }


    /**
     * 生成图片
     * @param code
     * @return
     * @throws IOException
     */
    private byte[] createImage(String code) throws IOException {
        BufferedImage image = new BufferedImage(180, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
        char[] cs = code.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            g2d.drawChars(cs, i, 1, i * 20 + 3, 25);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ImageIO.write(image, "png", out);

        return out.toByteArray();
    }






}
