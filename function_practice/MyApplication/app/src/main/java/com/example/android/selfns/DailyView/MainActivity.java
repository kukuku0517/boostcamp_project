package com.example.android.selfns.DailyView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Background.ContentProviderData;
import com.example.android.selfns.Background.DataUpdateService;
import com.example.android.selfns.Background.GoogleAwareness.GoogleAwarenessService;
import com.example.android.selfns.Background.SNSNotificationService;
import com.example.android.selfns.DailyView.Adapter.CalendarPinAdapter;
import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Detail.DatePinDTO;
import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyGroupDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyUnitDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsUnitDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.RetrofitShareableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyUnitData;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.SmsUnitData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.DetailView.CustomActivity;
import com.example.android.selfns.DetailView.GpsGroupActivity;
import com.example.android.selfns.DetailView.CallActivity;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.DetailView.GpsStillActivity;
import com.example.android.selfns.DetailView.SmsTradeActivity;
import com.example.android.selfns.ExtraView.LicenseActivity;
import com.example.android.selfns.GroupView.PhotoActivity;
import com.example.android.selfns.GroupView.UnitActivity;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.FriendAddEvent;
import com.example.android.selfns.Helper.FriendTagEvent;
import com.example.android.selfns.Helper.ItemComparator;
import com.example.android.selfns.Helper.PinnedHeaderItemDecoration;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.Helper.RetrofitHelper;
import com.example.android.selfns.Interface.CardItemClickListener;
import com.example.android.selfns.Interface.DataReceiveListener;
import com.example.android.selfns.Interface.DatePinClickListener;
import com.example.android.selfns.LoginView.FriendActivity;
import com.example.android.selfns.LoginView.LoginActivity;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.mancj.slideup.SlideUp;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.R.attr.name;


public class MainActivity extends AppCompatActivity implements CardItemClickListener,
        DatePinClickListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rv_day2)
    RecyclerView rv;
    @BindView(R.id.cv_day2)
    MaterialCalendarView cv;
    @BindView(R.id.cv_cardview)
    CardView cardView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar pb;
    @BindView(R.id.tv_progress)
    TextView pbtv;
    @BindView(R.id.logoview)
    ImageView logo;

    //    @BindView(R.id.swipe_layout)
//    SwipyRefreshLayout sl;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout sl;

    private Realm realm;
    private Realm realmAsync;
    private RecyclerView.LayoutManager layoutManager;
    private CalendarPinAdapter adapter;

    private SlideUp slideUp;
    private Context context = this;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG};

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;

    private ArrayList<BaseDTO> items = new ArrayList<>();
    HashMap<Integer, List<FriendDTO>> usersHash = new HashMap<>();

    private int page = 0;

//    DataReceiveListener listener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkStateAndLoad();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PERMISSION_ALL) {
            Log.d("checkstate", String.valueOf(resultCode));
            if (resultCode == RESULT_OK) {
                checkStateAndLoad();
            } else {
                checkPermissions(this, PERMISSIONS); //TODO
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        Glide.with(this).load(R.drawable.facebook_logo_text).into(logo);

        slideUp = new SlideUp.Builder(cardView)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.TOP)
                .build();
        slideUp.hide();
        sl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page++;
                updateItemsFromRealm(System.currentTimeMillis());
                sl.setRefreshing(false);
            }
        });


        initService();
        initRealm();
        initNavBar();
        setCalendar();
        initUserState();
        checkPermissionBeforeLoadingData();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        listener = new DataReceiveListener<String>() {
//            @Override
//            public void onReceive(String response) {
//                for (int i = 0; i < items.size(); i++) {
//                    if (items.get(i) instanceof ShareableDTO && ((ShareableDTO)items.get(i)).getShare()==1) {
//                        ShareableDTO sItem = (ShareableDTO) items.get(i);
//                        final int finalI = i;
//                        RetrofitHelper.getInstance(context).getTaggedFriends(sItem.get_id(), new DataReceiveListener<ArrayList<FriendDTO>>() {
//                            @Override
//                            public void onReceive(ArrayList<FriendDTO> response) {
//                                usersHash.put(finalI,response);
//                                adapter.updateItem(items,usersHash);
//                                adapter.notifyItemChanged(finalI);
//                            }
//                        });
//                    }
//
//                }
//            }
//        };
    }

    private void initNavBar() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);


        View hView = navView.getHeaderView(0);

        TextView email = (TextView) hView.findViewById(R.id.drawer_email);

        TextView name = (TextView) hView.findViewById(R.id.drawer_name);

        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

    }

    private void initUserState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    }

    /**
     * 권한 승인 되어있으면 데이터 로드 한다
     * 아니면 권한 요청한다 ( request code = 1 )
     **/

    private void checkPermissionBeforeLoadingData() {
        if (!checkPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            checkStateAndLoad();
        }
    }

    /**
     * permission check
     * M 미만이면 권한은 승인된것으로 본다 ( check 할필요 x)
     * TODO M 이상일 경우 권한체크를 하고, 취소한 경우 알림 띄워줌
     **/
    public static boolean checkPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                        Toast.makeText(context, "앱 실행을 위해서는 권한을 설정해야 합니다",
                                Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 0 : none
     * 1 : sms
     * 2 : sms + call
     * 3 : sms + call + photo => display호출 가능
     **/
    private void checkStateAndLoad() {
        int state = 0;
        SharedPreferences mPref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        if (!mPref.contains("init")) {
            state = 0;
        } else {
            state = mPref.getInt("init", 0);
        }
        Log.d("fblog", String.valueOf(state));
        switch (state) {
            case 0://TODO 초기화 중지시 처리
            case 1:
            case 2:
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                        new RealmAsync().execute();
                    }
                });
                break;
            case 3:
                items.clear();
                displayRecyclerView();
                mPref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
                long timestamp = mPref.getLong("lastTimestamp", 0);
                try {
                    RetrofitHelper.getInstance(context).getData(timestamp);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                updateItemsFromRealm(System.currentTimeMillis());


                break;
        }
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    private void initService() {
        Intent i = new Intent(this, DataUpdateService.class);
        startService(i);
        if (!isNLServiceRunning()) {
            Intent notifyIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(notifyIntent);
        }
        Intent locationIntent = new Intent(this, GoogleAwarenessService.class);
        startService(locationIntent);
    }

    private boolean isNLServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            String a = SNSNotificationService.class.getName();
            String b = service.service.getClassName();
            if (SNSNotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    private void setCalendar() {
        cv.setDateSelected(DateHelper.getInstance().toDate(System.currentTimeMillis()), true);
        cv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = new Intent(MainActivity.this, DayActivity.class);
                intent.putExtra("date", date.getDate().getTime());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (cardView.getVisibility() == View.VISIBLE) {
            slideUp.hide();
        } else {
            super.onBackPressed();

            finish();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_search:
                Intent intent4 = new Intent(MainActivity.this, SearchActivity.class);

                startActivity(intent4);
                break;

            case R.id.nav_statistic:
                Intent intent3 = new Intent(MainActivity.this, StatsActivity.class);

                startActivity(intent3);
                break;
            case R.id.nav_friend_add_list:
                Intent intent = new Intent(MainActivity.this, FriendActivity.class);

                startActivity(intent);
                break;
            case R.id.nav_license:
                Intent intent5 = new Intent(MainActivity.this, LicenseActivity.class);

                startActivity(intent5);
                break;

            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void updateItemsFromRealm(long millis) {
        items.clear();
        adapter.updateItem(items, usersHash);
        adapter.notifyDataSetChanged();
//        getItemFromFB(millis);
//        long minusDay1 = DateHelper.getInstance().getDayAfter(millis, -1 - 7 * page);
//        long minusDay2 = DateHelper.getInstance().getDayAfter(millis, -2 - 7 * page);
//        long minusDay3 = DateHelper.getInstance().getDayAfter(millis, -3 - 7 * page);
//        long minusDay4 = DateHelper.getInstance().getDayAfter(millis, -4 - 7 * page);
//        long minusDay5 = DateHelper.getInstance().getDayAfter(millis, -5 - 7 * page);
//        long minusDay6 = DateHelper.getInstance().getDayAfter(millis, -6 - 7 * page);
//
//        items.addAll(getItemFromRealm(minusDay6));
//        items.addAll(getItemFromRealm(minusDay5));
//        items.addAll(getItemFromRealm(minusDay4));
//
//        items.addAll(getItemFromRealm(minusDay3));
//        items.addAll(getItemFromRealm(minusDay2));
//        items.addAll(getItemFromRealm(minusDay1));
//        items.addAll(getItemFromRealm(millis));

        for (int i = page; i >= 0; i--) {
            for (int j = 7; j > 0; j--) {
                long minusDay1 = DateHelper.getInstance().getDayAfter(millis, -j + 1 - 7 * i);
                items.addAll(getItemFromRealm(minusDay1));
                Log.d("pagepage", String.valueOf(-j + 1 - 7 * i));
            }
        }


        Collections.sort(items, new ItemComparator());

        adapter.updateItem(items, usersHash);
        adapter.notifyDataSetChanged();


        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof ShareableDTO && ((ShareableDTO) items.get(i)).getShare() == 1) {
                ShareableDTO sItem = (ShareableDTO) items.get(i);
                final int finalI = i;
                RetrofitHelper.getInstance(context).getTaggedFriends(sItem.get_id(), new DataReceiveListener<ArrayList<FriendDTO>>() {
                    @Override
                    public void onReceive(ArrayList<FriendDTO> response) {
                        if (response != null) {

                            usersHash.put(finalI, response);
                            adapter.updateItem(items, usersHash);
                            adapter.notifyItemChanged(finalI);
                        }
                    }
                });
            }

        }
//        FirebaseHelper.getInstance(context).updateDataFromFirebase();


    }

    private ArrayList<BaseDTO> getItemFromRealm(long millis) {

        final ArrayList<BaseDTO> items = new ArrayList<>();
        long today[] = DateHelper.getInstance().getStartEndDate(millis);
        long startMillis = today[0];
        long endMillis = today[1];

        RealmResults<RealmObject> cData = RealmHelper.getInstance().DataHighlightLoad(CallData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> ggData = RealmHelper.getInstance().DataHighlightAndShareLoad(GpsGroupData.class, "end", startMillis, endMillis);
        RealmResults<RealmObject> smsTradeDatas = RealmHelper.getInstance().DataHighlightAndShareLoad(SmsTradeData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> pgData = RealmHelper.getInstance().DataHighlightAndShareLoad(PhotoGroupData.class, "start", startMillis, endMillis - 1);
        RealmResults<RealmObject> customData = RealmHelper.getInstance().DataHighlightAndShareLoad(CustomData.class, "date", startMillis, endMillis);

        RealmResults<RealmObject> nData = RealmHelper.getInstance().DataHighlightLoad(NotifyUnitData.class, "start", startMillis, endMillis);
        RealmResults<RealmObject> sData = RealmHelper.getInstance().DataHighlightLoad(SmsUnitData.class, "start", startMillis, endMillis);

        for (RealmObject cd : nData) {
            items.add(new NotifyUnitDTO((NotifyUnitData) cd));
        }
        for (RealmObject cd : sData) {
            items.add(new SmsUnitDTO((SmsUnitData) cd));
        }


        for (RealmObject cd : customData) {
            items.add(new CustomDTO((CustomData) cd));
        }
        for (RealmObject g : ggData) {
            items.add(new GpsGroupDTO((GpsGroupData) g));
        }
        for (RealmObject c : cData) {
            items.add(new CallDTO((CallData) c));
        }
        for (RealmObject pg : pgData) {
            items.add(new PhotoGroupDTO((PhotoGroupData) pg));
        }
        for (RealmObject std : smsTradeDatas) {
            items.add(new SmsTradeDTO((SmsTradeData) std));
        }

        if (items.size() != 0) {
            DatePinDTO datePinData = new DatePinDTO();
            datePinData.setDate(startMillis);
            items.add(datePinData);
        }
        return items;
    }
//
//
//    private void getItemFromFB(final long millis) {
//        //공유한 posts의 id 가져오기
//        DatabaseReference myRef = FirebaseHelper.getInstance(context).getCurrentUserRef().child("posts");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int size = 0;
//                final int[] count = {0};
//
//                //posts의 count
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    size++;
//                }
//                if (dataSnapshot.getValue() != null) {
//                    //post id로 posts에서 글 찾아오기
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        DatabaseReference postRef = FirebaseHelper.getInstance(context).getPostsRef().child(snapshot.getValue().toString());
//                        final int finalSize = size;
//
//                        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.hasChildren()) {
//                                    int type = Integer.parseInt(dataSnapshot.child("class").getValue().toString());
//                                    BaseDTO item;
//                                    switch (type) {
//                                        case RealmClassHelper.CALL_DATA:
//                                            item = dataSnapshot.child("item").getValue(CallDTO.class);
//                                            break;
//                                        case RealmClassHelper.CUSTOM_DATA:
//                                            item = dataSnapshot.child("item").getValue(CustomDTO.class);
//                                            break;
//                                        case RealmClassHelper.PHOTO_DATA:
//                                            item = dataSnapshot.child("item").getValue(PhotoDTO.class);
//                                            break;
//                                        case RealmClassHelper.PHOTO_GROUP_DATA:
//                                            item = dataSnapshot.child("item").getValue(PhotoGroupDTO.class);
//                                            break;
//                                        case RealmClassHelper.SMS_TRADE_DATA:
//                                            item = dataSnapshot.child("item").getValue(SmsTradeDTO.class);
//                                            break;
//                                        default:
//                                            item = null;
//                                    }
//                                    items.add(item);
//                                    count[0]++;
//
//                                    //posts 갯수만큼 가져온 후 items에 저장
//                                    if (count[0] == finalSize) {
//                                        long minusDay1 = DateHelper.getInstance().getDayAfter(millis, -1);
//                                        long minusDay2 = DateHelper.getInstance().getDayAfter(millis, -2);
//                                        long minusDay3 = DateHelper.getInstance().getDayAfter(millis, -3);
//                                        items.addAll(getItemFromRealm(minusDay3));
//                                        items.addAll(getItemFromRealm(minusDay2));
//                                        items.addAll(getItemFromRealm(minusDay1));
//                                        items.addAll(getItemFromRealm(millis));
//                                        Collections.sort(items, new ItemComparator());
//
//                                        //--------------------------------------------------------------------------//
//
//                                        //friends값 있는 item의 친구리스트 index와 함께 저장하기
//
//                                        final int[] iCount = {0};
//                                        for (int i = 0; i < items.size(); i++) {
//                                            if (items.get(i) instanceof ShareableDTO) {
//                                                ShareableDTO sItem = (ShareableDTO) items.get(i);
//                                                try {
//                                                    JSONArray friends = new JSONArray(sItem.getFriends());
//                                                    final int fsize = friends.length();
//                                                    final int[] fcount = {0};
//
//                                                    for (int j = 0; j < fsize; j++) {
//                                                        final ArrayList<UserDTO> users = new ArrayList<>();
//                                                        JSONObject friend = friends.getJSONObject(j);
//                                                        String uid = friend.get("id").toString();
//                                                        DatabaseReference fRef = FirebaseHelper.getInstance(context).getUserRef(uid);
//                                                        final int finalI = i;
//                                                        fRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                                UserDTO friend = dataSnapshot.child("userDTO").getValue(UserDTO.class);
//                                                                users.add(friend);
//                                                                fcount[0]++;
//                                                                Log.d("###Fire useradd", fcount[0] + " : " + fsize);
//                                                                if (fcount[0] == fsize) {
//                                                                    usersHash.put(finalI, users);
//                                                                    iCount[0]++;
//                                                                    Log.d("###Fire userhashadd", finalI + ":" + iCount[0] + "/" + items.size());
//
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(DatabaseError databaseError) {
//                                                            }
//                                                        });
//                                                    }
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            } else {
//                                                iCount[0]++;
//                                                Log.d("###Fire userhashadd", ":" + iCount[0] + "/" + items.size());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                            }
//                        });
//                    }
//                } else {
//                    long minusDay1 = DateHelper.getInstance().getDayAfter(millis, -1);
//                    long minusDay2 = DateHelper.getInstance().getDayAfter(millis, -2);
//                    long minusDay3 = DateHelper.getInstance().getDayAfter(millis, -3);
//                    items.addAll(getItemFromRealm(minusDay3));
//                    items.addAll(getItemFromRealm(minusDay2));
//                    items.addAll(getItemFromRealm(minusDay1));
//                    items.addAll(getItemFromRealm(millis));
//                    Collections.sort(items, new ItemComparator());
//                    pb.setVisibility(View.GONE);
//                    adapter.updateItem(items, usersHash);
//                    adapter.notifyDataSetChanged();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void displayRecyclerView() {

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CalendarPinAdapter(this, realm);
        adapter.updateItem(items, usersHash);
//        adapter.setHasStableIds(true);

        rv.addItemDecoration(new PinnedHeaderItemDecoration());
        rv.setHasFixedSize(true);

        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                items.clear();
                updateItemsFromRealm(System.currentTimeMillis());
//                adapter.updateItem(items,usersHash);
//                adapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FriendTagEvent event) {
        updateItemsFromRealm(System.currentTimeMillis());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        slideUp.hide();
    }

    @Override
    public void onDatePinClick(BaseDTO item) {
        Intent intent = new Intent(this, DayActivity.class);
        intent.putExtra("date", item.getDate());
        startActivity(intent);
    }

    @Override
    public void onNotifyItemClick(BaseDTO item) {
        Intent intent = new Intent(this, UnitActivity.class);
        intent.putExtra("item", Parcels.wrap((NotifyGroupDTO) item));
        startActivity(intent);
    }

    @Override
    public void onSmsGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, UnitActivity.class);
        intent.putExtra("item", Parcels.wrap((SmsGroupDTO) item));
        startActivity(intent);
    }

    @Override
    public void onSmsTradeItemClick(BaseDTO item) {
        Intent intent = new Intent(this, SmsTradeActivity.class);
        intent.putExtra("item", Parcels.wrap((SmsTradeDTO) item));
        startActivity(intent);
    }

    @Override
    public void onPhotoGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("item", Parcels.wrap((PhotoGroupDTO) item));
        startActivity(intent);
    }

    @Override
    public void onGpsItemClick(BaseDTO item) {
        Intent intent = new Intent(this, GpsStillActivity.class);
        intent.putExtra("item", Parcels.wrap((GpsDTO) item));
        startActivity(intent);
    }

    @Override
    public void onCallItemClick(BaseDTO item) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("item", Parcels.wrap((CallDTO) item));
        startActivity(intent);
    }

    @Override
    public void onCustomItemClick(BaseDTO item) {
        Intent intent = new Intent(this, CustomActivity.class);
        intent.putExtra("item", Parcels.wrap((CustomDTO) item));
        startActivity(intent);
    }

    @Override
    public void onGpsGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, GpsGroupActivity.class);
        intent.putExtra("item", Parcels.wrap((GpsGroupDTO) item));

        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_calendar:
                if (cardView.getVisibility() == View.VISIBLE) {
                    slideUp.hide();
                } else {
                    slideUp.show();
                }
                break;
        }
        return true;
    }


    private class RealmAsync extends AsyncTask<Integer, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            pbtv.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pbtv.setText(values[0]);
        }

        @Override
        protected Void doInBackground(Integer... params) {

            realmAsync = Realm.getDefaultInstance();
            long today[] = DateHelper.getInstance().getStartEndDate(System.currentTimeMillis());
            ContentProviderData cp = new ContentProviderData(context, today[0], today[1], realmAsync);

            DataReceiveListener<String> listener = new DataReceiveListener<String>() {
                @Override
                public void onReceive(String response) {
                    publishProgress(response);
                }
            };
            cp.readSMSMessage(listener);
            cp.readCallLogs(listener);
            cp.readImages(listener);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pb.setVisibility(View.GONE);
            pbtv.setVisibility(View.GONE);
            Toast.makeText(context, "데이터 로딩 성공", Toast.LENGTH_SHORT).show();
            SharedPreferences mPref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putInt("init", 3);
            editor.commit();
            displayRecyclerView();
            updateItemsFromRealm(System.currentTimeMillis());


        }
    }

}
