package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateLensManager();
    }

    private void populateLensManager(){
        LensManager manager = LensManager.getInstance();
        manager.add(new Lens("Nikkor", 2.8, 24));
        manager.add(new Lens("Sony", 1.8, 35));
        manager.add(new Lens("Canon", 3.5, 80));
//        String[] items = {"Test", "Test2"};
        //items[0] = manager.getLens(0).toString();
//        Lens lens = manager.getLens(0);

        ArrayAdapter<LensManager> adapter = new ArrayAdapter<LensManager>(
                this,
                R.layout.lens_item,
                Collections.singletonList(manager));

        ListView list = (ListView) findViewById(R.id.listViewLens);
        list.setAdapter(adapter);
    }
}
