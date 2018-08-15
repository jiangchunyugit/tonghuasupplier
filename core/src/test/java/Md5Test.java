import cn.thinkfree.core.security.utils.MultipleMd5;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Md5Test {

    public static void main(String[] args) {
        PasswordEncoder customMd5 = new MultipleMd5();
        String pwd = customMd5.encode("123456");
//        14e1b600b1fd579f47433b88e8d85291
        System.out.println(pwd);

        boolean isTrue = customMd5.matches("123456", pwd);
        System.out.println(isTrue);
    }
}
