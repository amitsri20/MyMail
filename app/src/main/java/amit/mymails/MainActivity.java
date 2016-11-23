package amit.mymails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.services.orc.auth.AuthenticationCredentials;
import com.microsoft.services.orc.core.DependencyResolver;
import com.microsoft.services.orc.http.impl.LoggingInterceptor;
import com.microsoft.services.orc.http.impl.OAuthCredentials;
import com.microsoft.services.orc.http.impl.OkHttpTransport;
import com.microsoft.services.orc.serialization.impl.GsonSerializer;
import com.microsoft.services.outlook.Message;
import com.microsoft.services.outlook.fetchers.OutlookClient;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import amit.mymails.model.Email;

public class MainActivity extends AppCompatActivity {


    org.slf4j.Logger logger = LoggerFactory.getLogger(MainActivity.class);

    private static final String TAG = "MainActivity";
    private static final String outlookBaseUrl = "https://outlook.office.com/api/v2.0";

    private AuthenticationContext _authContext;
    private DependencyResolver _resolver;
    public static OutlookClient _client;

    private String[] scopes = new String[]{"https://outlook.office.com/Mail.Read"};
    RecyclerView recyclerView;
    EmailAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        loadItems();
        Futures.addCallback(logon(), new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                _client = new OutlookClient(outlookBaseUrl, _resolver);
                getMessages();
            }

            @Override
            public void onFailure(Throwable t) {
                logger.error("authentication failed", t);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getMessages();

            }
        });
    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }



    public SettableFuture<Boolean> logon() {

        final SettableFuture<Boolean> result = SettableFuture.create();

        try {
            _authContext = new AuthenticationContext(this, getResources().getString(R.string.AADAuthority), true);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Authentication Context with error: " + e.getMessage());
            _authContext = null;
            result.setException(e);
        }

        if (_authContext != null) {
            _authContext.acquireToken(
                    this,
                    scopes,
                    null,
                    getResources().getString(R.string.AADClientId),
                    getResources().getString(R.string.AADRedirectUrl),
                    PromptBehavior.Auto,
                    new AuthenticationCallback<AuthenticationResult>() {

                        @Override
                        public void onSuccess(final AuthenticationResult authenticationResult) {

                            if (authenticationResult != null && authenticationResult.getStatus() == AuthenticationResult.AuthenticationStatus.Succeeded) {
                                _resolver = new DependencyResolver.Builder(
                                        new OkHttpTransport().setInterceptor(new LoggingInterceptor()), new GsonSerializer(),
                                        new AuthenticationCredentials() {
                                            @Override
                                            public com.microsoft.services.orc.http.Credentials getCredentials() {
                                                return new OAuthCredentials(authenticationResult.getAccessToken());
                                            }
                                        }).build();

                                result.set(true);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            result.setException(e);
                        }
                    }
            );
        }

        return result;
    }
    public void getMessages() {
        logger.info("Getting messages...");
        Futures.addCallback(_client.getMe().getMessages().read(), new FutureCallback<List<Message>>() {
            @Override
            public void onSuccess(final List<Message> result) {
                logger.info("Preparing messages for display.");
                List<Email> listOfMessages = new ArrayList<Email>();

                for (Message m : result) {
                    listOfMessages.add(new Email(m.getSubject(),m.getFrom().getEmailAddress().getAddress(),m.getIsRead().toString()
                            ,m.getReceivedDateTime().getTimeInMillis()
                    ,m.getId(),m.getBodyPreview()));
                }

                adapter = new EmailAdapter(listOfMessages, R.layout.list_item_layout, getApplicationContext());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFailure(final Throwable t) {
                logger.error(t.getMessage(), t);
            }
        });

        onItemsLoadComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        _authContext.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getMessages();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
