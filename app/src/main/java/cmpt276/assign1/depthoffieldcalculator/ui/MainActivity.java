package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ADD_LENS = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateLensList();

        FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddLensActivity.class);
            startActivityForResult(intent, RESULT_CODE_ADD_LENS);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            String message = "Canceled";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode){
            case RESULT_CODE_ADD_LENS:
                String make = data.getStringExtra(AddLensActivity.EXTRA_USER_MAKE);
                int focalLength = data.getIntExtra(AddLensActivity.EXTRA_USER_FOCAL_lENGTH, 0);
                double aperture = data.getDoubleExtra(AddLensActivity.EXTRA_USER_APERTURE, 0);
                String message = "Added lens: " + make + " " + focalLength + "mm F" + aperture;
//                Snackbar.make(MainActivity.this, message, Snackbar.LENGTH_LONG);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                break;
        }

    }

    private void populateLensList(){
        LensManager manager = LensManager.getInstance();
        manager.add(new Lens("Nikkor", 2.8, 24));
        manager.add(new Lens("Sony", 1.8, 35));
        manager.add(new Lens("Canon", 3.5, 80));

        String[] items = new String[manager.getSize()];
        for(int i = 0; i < manager.getSize(); i++){
            items[i] = manager.getLens(i).toString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.lens_item, items //Collections.singletonList(manager.)
        );

        ListView list = findViewById(R.id.listViewLens);
        list.setAdapter(adapter);
    }


    // Create updateUI()
}
