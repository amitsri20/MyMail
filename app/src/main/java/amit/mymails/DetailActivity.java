package amit.mymails;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.microsoft.services.outlook.Message;

import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class DetailActivity extends AppCompatActivity {

    org.slf4j.Logger logger = LoggerFactory.getLogger(DetailActivity.class);

    TextView emailBody;
    TextView emailParticipents;
    TextView emailSubject;
    TextView emailTime;
    ImageView emailStarred;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail);
        emailBody =(TextView) findViewById(R.id.emailbody);
        emailParticipents = (TextView) findViewById(R.id.email_participents);
        emailSubject = (TextView) findViewById(R.id.email_subject);
        emailTime = (TextView) findViewById(R.id.email_time);
        emailStarred = (ImageView) findViewById(R.id.email_star);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String id = extras.getString("EMAILID");
        if (id != null) {
            getMessages(id);
        }

    }

    private String getTime(long ts) {
        long dv = ts*1000;
        Date df = new Date(dv);
//        String time = new SimpleDateFormat("MMMM dd, yyyy hh:mma").format(df);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MMMM dd, hh:mma", new Locale("en", "UK"));

        String time = simpleDateFormat.format(df);
        return time;
    }

    public void getMessages(String id) {
        logger.info("Getting messages...");

        Futures.addCallback(MainActivity._client.getMe().getMessage(id).read(), new FutureCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
//                emailBody.setText(message.getBodyPreview());
                final String body = message.getBody().getContent();
                final String participents = message.getFrom().getEmailAddress().getAddress();
                final String subject = message.getSubject();
                final String from = message.getFrom().getEmailAddress().getName();
                final long date = message.getReceivedDateTime().getTimeInMillis();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        emailBody.setText(Html.fromHtml(body));
                        emailParticipents.setText(participents);
                        emailSubject.setText(subject);
                        emailTime.setText(getTime(date));
                    }
                });
            }

            @Override
            public void onFailure(final Throwable t) {
                logger.error(t.getMessage(), t);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    Log.i(TAG, "You have forgotten to specify the parentActivityName in the AndroidManifest!");
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
