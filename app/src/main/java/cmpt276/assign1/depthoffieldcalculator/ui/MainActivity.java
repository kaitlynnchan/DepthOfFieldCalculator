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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(view -> {}
            );
    }

    private void populateLensManager(){
        LensManager manager = LensManager.getInstance();
        manager.add(new Lens("Nikkor", 2.8, 24));
        manager.add(new Lens("Sony", 1.8, 35));
        manager.add(new Lens("Canon", 3.5, 80));

        String[] items = new String[manager.getSize()];
        for(int i = 0; i < manager.getSize(); i++){
            items[i] = manager.getLens(i).toString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.lens_item,
                items); //Collections.singletonList(manager.)

        ListView list = findViewById(R.id.listViewLens);
        list.setAdapter(adapter);
    }
}
