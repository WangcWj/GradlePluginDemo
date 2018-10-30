package plugin.gradle.com.gradleplugindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String,String> maps =new HashMap<>();
        maps.put("1","2");
        maps.put("2","2");
        maps.put("3","2");
        maps.put("4","2");

        Iterator<String> iterator = maps.keySet().iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            Log.e("WANG","MainActivity.onCreate."+next );
        }

        Iterator<Map.Entry<String, String>> iterator1 = maps.entrySet().iterator();
        Map.Entry<String, String> next = iterator1.next();
        Log.e("WANG","MainActivity.onCreate."+next.getKey() );

    }
}
