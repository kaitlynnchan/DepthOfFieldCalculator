package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

/**
 * Launch Activity
 * Displays lens in a list View
 * Allows user to add lens by the floating action button
 * User can click the a lens to launch calculate activity
 */
public class MainActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ADD_LENS = 42;
    public static final int RESULT_CODE_CALCULATE_DOF = 43;
    public static final String SHARED_PREFERENCE = "shared preference";
    public static final String EDITOR_LENS_LIST = "lens manager";

    private LensManager manager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = LensManager.getInstance();
        loadSharedPreferences();
        populateListView();
        registerClickCallback();

        FloatingActionButton btnAdd = findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LensDetailsActivity.makeLaunchIntent(MainActivity.this, false);
                MainActivity.this.startActivityForResult(intent, RESULT_CODE_ADD_LENS);
            }
        });

    }

    private void loadSharedPreferences(){
        // Code inspired by: https://codinginflow.com/tutorials/android/save-arraylist-to-sharedpreferences-with-gson
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        String json = sharedPreferences.getString(EDITOR_LENS_LIST, null);
        Type type = new TypeToken<List<Lens>>() {}.getType();
        Gson gson = new Gson();
        List<Lens> lensListShared = gson.fromJson(json, type);

        if(lensListShared == null || lensListShared.size() <= 0){
            populateLensManager();
        } else{
            manager.setLenses(lensListShared);
        }
    }

    private void populateLensManager(){
        // Lens(make, maxAperture, focalLength [mm], iconID)
        manager.add(new Lens("Canon", 1.8, 50, R.drawable.icon_picture));
        manager.add(new Lens("Tamron", 2.8, 90, R.drawable.icon_picture));
        manager.add(new Lens("Sigma", 2.8, 200, R.drawable.icon_picture));
        manager.add(new Lens("Nikon", 4.0, 200, R.drawable.icon_picture));
    }

    private void populateListView(){
        ArrayAdapter<Lens> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.listViewLens);
        list.setAdapter(adapter);

        saveSharedPreferences();
        setupEmptyListView(list);
    }

    private class MyListAdapter extends ArrayAdapter<Lens>{
        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_view_lens, manager.getLenses());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view_lens, parent, false);
            }

            Lens currentLens = manager.get(position);

            TextView makeText = itemView.findViewById(R.id.item_textViewMake);
            makeText.setText(currentLens.getMake());

            TextView focalLengthText = itemView.findViewById(R.id.item_textViewFocalLength);    // [mm]
            focalLengthText.setText(currentLens.getFocalLength() + "mm");

            TextView apertureText = itemView.findViewById(R.id.item_textViewAperture);
            apertureText.setText("F" + currentLens.getMaxAperture());

            ImageView icon = itemView.findViewById(R.id.item_imageViewIcon);
            icon.setImageResource(currentLens.getIconID());

            return itemView;
        }
    }

    private void saveSharedPreferences(){
        // Code taken from: https://codinginflow.com/tutorials/android/save-arraylist-to-sharedpreferences-with-gson
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(manager.getLenses());
        editor.putString(EDITOR_LENS_LIST, json);
        editor.apply();
    }

    private void setupEmptyListView(ListView list) {
        TextView textTitle = findViewById(R.id.emptyList_textTitle);
        TextView text1 = findViewById(R.id.emptyList_text1);
        ImageView error = findViewById(R.id.emptyList_imageError);
        if(manager.getSize() <= 0){
            list.setVisibility(View.INVISIBLE);
            textTitle.setVisibility(View.VISIBLE);
            text1.setVisibility(View.VISIBLE);
            error.setVisibility(View.VISIBLE);
        } else{
            list.setVisibility(View.VISIBLE);
            textTitle.setVisibility(View.INVISIBLE);
            text1.setVisibility(View.INVISIBLE);
            error.setVisibility(View.INVISIBLE);
        }
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.listViewLens);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = CalculateDepthOfFieldActivity.makeLaunchIntent(
                        MainActivity.this, position);
                startActivityForResult(intent, RESULT_CODE_CALCULATE_DOF);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            return;
        }

        switch (requestCode){
            case RESULT_CODE_ADD_LENS:
                String make = data.getStringExtra(LensDetailsActivity.EXTRA_USER_MAKE);
                int focalLength = data.getIntExtra(LensDetailsActivity.EXTRA_USER_FOCAL_LENGTH, 0);    // [mm]
                double aperture = data.getDoubleExtra(LensDetailsActivity.EXTRA_USER_APERTURE, 0);
                int iconID = data.getIntExtra(LensDetailsActivity.EXTRA_USER_ICON_ID, 0);

                // Lens(make, maxAperture, focalLength [mm], iconID)
                manager.add(new Lens(make, aperture, focalLength, iconID));
                populateListView();
                break;
            case RESULT_CODE_CALCULATE_DOF:
                populateListView();
                break;
            default:
                assert false;
        }
    }

}
