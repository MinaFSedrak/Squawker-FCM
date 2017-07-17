package minasedrak.squawker.fcm;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by MinaSedrak on 7/17/2017.
 */

public class SquawkFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String LOG_TAG = SquawkFirebaseInstanceIdService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

//        Toast.makeText(this, refreshedToken, Toast.LENGTH_SHORT).show();

        Log.i(LOG_TAG, "refreshed Token : " + refreshedToken);

    }
}
