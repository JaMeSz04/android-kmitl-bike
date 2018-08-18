package architecture.com.jimi;

//Translated

import android.util.Log;

/**
 * Created by ChenShouYin on 2018\7\4 0004.
 */
public class NativeController {
    // Used to load the 'jimi-native-lib' library on application startup.
    static {
        System.loadLibrary("jimi-native-lib");
    }

    /**
     * 获取上升指令 [Get rising instruction]
     *
     * @return
     */
    private static native String getCmdDisuoUp();

    private static native byte[] getCmdDisuoUp2();

    /**
     * 巴西单车锁 加密解密key [Brazilian Bike lock encryption and decryption key]
     *
     * @return
     */
    public static native byte[] getEncyptKey();

    /**
     * 巴西单车锁 获取令牌指令 [Brazilian Bike lock get token instruction]
     *
     * @return
     */
    public static native byte[] getTokenCmd();

    public static native String getClientCharacteristicConfig();

    public static native String getBltServerUUID();

    public static native String getReadDataUUID();

    public static native String getWriteDataUUID();

    public static native String getLockReadwriteUuid();
}
