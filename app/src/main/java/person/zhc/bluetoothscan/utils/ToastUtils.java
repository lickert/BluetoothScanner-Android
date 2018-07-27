package person.zhc.bluetoothscan.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by huanchen on 2018/7/25.
 */

public class ToastUtils {

    public static void showShort(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
