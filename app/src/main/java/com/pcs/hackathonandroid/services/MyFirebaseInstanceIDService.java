package com.pcs.hackathonandroid.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by mufaddalgulshan on 11/03/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInstanceIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

//        RestClient.getInstance(this).get(Api.class)
//                .getSenderId(APP_NAME)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::handleSenderResponse, this::handleError);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

//    private void handleSenderResponse(Device device) {
//        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_name),
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("sender_id", device.senderId);
//    }
//
//    private void handleError(Throwable throwable) {
//        throwable.printStackTrace();
//    }
//
//    /**
//     * Persist token to third-party servers.
//     * <p>
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private void sendRegistrationToServer(String token) {
//
//        String model = Build.MODEL;
//        String version = Build.VERSION.RELEASE;
//        String uid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String carrierName = manager.getNetworkOperatorName();
//        String countryCode = manager.getSimCountryIso().toUpperCase();
//        String phoneNumber = manager.getLine1Number();
//        String deviceId = manager.getDeviceId();
//        String subscriberId = manager.getSubscriberId();
//
//        RestClient.getInstance(this).get(Api.class)
//                .getDevice(token, APP_NAME, model, version, uid, carrierName, countryCode,
//                        phoneNumber, deviceId, subscriberId, "ANDROID", "NA")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::handleGetDeviceResponse, this::handleError);
//
//        RestClient.getInstance(this).get(Api.class)
//                .updateDevice(token, uid)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(device -> Log.d(TAG, "Registration " + device.res),
//                        this::handleError);
//    }
//
//    private void handleGetDeviceResponse(Device device) {
//        if (device.appDisplay != null && device.appPhone != null &&
//                device.appNote != null) {
//            String[] res_display = device.appDisplay.split(";");
//            String[] res_phone = device.appPhone.split(";");
//            String[] res_note = device.appNote.split(";");
//            for (int i = 0; i < res_display.length; i++) {
//                Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
//                ContentValues contentValues = new ContentValues();
//                Uri rawContactUri = getContentResolver()
//                        .insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
//                // Get the newly created contact raw id.
//                long rawContactId = ContentUris.parseId(rawContactUri);
//
//                insertContactDisplayName(addContactsUri, rawContactId, res_display[i]);
//                insertContactPhoneNumber(addContactsUri, rawContactId, res_phone[i]);
//                insertContactNote(addContactsUri, rawContactId, res_note[i]);
//            }
//        }
//    }
//
//    private void insertContactDisplayName(Uri uri, long rawContactId, String displayName) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//        contentValues.put(ContactsContract.Data.MIMETYPE,
//                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
//        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);
//        getContentResolver().insert(uri, contentValues);
//    }
//
//    private void insertContactPhoneNumber(Uri uri, long rawContactId, String phoneNumber) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//        contentValues.put(ContactsContract.Data.MIMETYPE,
//                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
//        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE,
//                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
//        getContentResolver().insert(uri, contentValues);
//    }
//
//    private void insertContactNote(Uri uri, long rawContactId, String note) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//        contentValues.put(ContactsContract.Data.MIMETYPE,
//                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//        contentValues.put(ContactsContract.CommonDataKinds.Note.NOTE, note);
//        getContentResolver().insert(uri, contentValues);
//    }
}
