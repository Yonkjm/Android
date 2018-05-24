package ihm.si3.polytech.projetnote.visualisationincident;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import ihm.si3.polytech.projetnote.R;
import ihm.si3.polytech.projetnote.notused.DownloadImagesTask;
import ihm.si3.polytech.projetnote.utility.Mishap;

public class DetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    static public final int REQUEST_LOCATION = 1;
    Mishap mishap;

    private GoogleMap mMap;

    private TextView title;
    private ImageView photo;
    private TextView etat;
    private TextView description;
    private ImageView usrPicture;
    private TextView usrName;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void loadMishap() {
        title.setText(mishap.getTitle());
        if(mishap.getImageUrl() != null) {
            try {
                photo.setImageBitmap(MyRecyclerAdapter.decodeFromFirebaseBase64(mishap.getImageUrl()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        etat.setText(mishap.getPriority().toString());
        description.setText(mishap.getDescription());
        DownloadImagesTask downloadImagesTask = new DownloadImagesTask(usrPicture);
        downloadImagesTask.execute(mishap.getUrlPicture());
        usrName.setText(mishap.getAuthor());
        date.setText(mishap.getDate());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mishap = (Mishap) getIntent().getSerializableExtra("Mishap");

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng localisation = new LatLng(mishap.getxPos(), mishap.getyPos());
        mMap.addMarker(new MarkerOptions().position(localisation).title(mishap.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(localisation));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            startBeermay(); // <-- Start Beemray here
        }

        title = this.findViewById(R.id.title);
        photo = this.findViewById(R.id.imageView4);
        etat = this.findViewById(R.id.etat);
        description = this.findViewById(R.id.description);
        usrPicture = this.findViewById(R.id.person_picture);
        usrName = this.findViewById(R.id.person_username);
        date = this.findViewById(R.id.date);

        loadMishap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBeermay(); // <-- Start Beemray here
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }

    private void startBeermay() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            // Show rationale and request permission.
        }

    }


}