import com.alibaba.druid.filter.config.ConfigTools;

public class DruidTest {

    public static void main(String[] args) throws Exception {
        String[] arr = ConfigTools.genKeyPair(512);
        System.out.println("privateKey:"+arr[0]);
        System.out.println("publicKey:"+arr[1]);
        String encryptStr = ConfigTools.encrypt(arr[0],"123456");
        System.out.println("encryptStr:"+encryptStr);
        System.out.println("decryptStr:"+ConfigTools.decrypt(arr[1],encryptStr));

    }
}
