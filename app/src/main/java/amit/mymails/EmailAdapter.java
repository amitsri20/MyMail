package amit.mymails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import amit.mymails.model.Email;

/**
 * Created by amit on 9/25/2016.
 */

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {

    private List<Email> emails;
    private int rowLayout;
    private Context context;
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    List<Email> itemsPendingRemoval;
    int lastInsertedIndex; // so we can add some more items for testing purposes

//    private Handler handler = new Handler(); // hanlder for running delayed runnables
//    HashMap<Email, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout emailLayout;
        TextView emailSubject;
        TextView emailPreview;
        TextView emailTime;
        ImageView emailStarred;
        TextView emailRead;
        Button undoButton;

        public ViewHolder(View v) {
            super(v);
            emailLayout = (LinearLayout) v.findViewById(R.id.container_layout);
            emailSubject = (TextView) v.findViewById(R.id.email_subject);
            emailPreview = (TextView) v.findViewById(R.id.email_preview);
            emailTime = (TextView) v.findViewById(R.id.email_time);
            emailStarred = (ImageView) v.findViewById(R.id.email_star);
            emailRead = (TextView) v.findViewById(R.id.email_read);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);
        }
    }

    public EmailAdapter(List<Email> emails, int rowLayout, Context context) {
        this.emails = emails;
        this.rowLayout = rowLayout;
        this.context = context;
        itemsPendingRemoval = new ArrayList<>();
    }

    @Override
    public EmailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final Email item = emails.get(position);


        // we need to show the "normal" state
        holder.itemView.setBackgroundColor(Color.WHITE);
        holder.emailLayout.setVisibility(View.VISIBLE);
        holder.undoButton.setVisibility(View.GONE);
        holder.undoButton.setOnClickListener(null);

        holder.emailSubject.setText(emails.get(position).getSubject());
        holder.emailPreview.setText(emails.get(position).getPreview());
        holder.emailTime.setText(getTime(emails.get(position).getTs()));
        if (emails.get(position).getIsRead().equals("true")) {
            holder.emailRead.setText("");
            holder.emailLayout.setBackgroundColor(Color.WHITE);
        } else {
            holder.emailLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGreyLightLight));
            holder.emailRead.setText("New");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        Toast.makeText(context, "Item at id clicked is:"+emails.get(position).getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("EMAILID", emails.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emails.get(position).setIsRead("true");
                notifyDataSetChanged();
                context.startActivity(intent);
            }
        });

    }

    private String getTime(long ts) {
        long dv = ts * 1000;
        Date df = new Date(dv);
//        String time = new SimpleDateFormat("MMMM dd, yyyy hh:mma").format(df);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MMMM dd, hh:mma", new Locale("en", "UK"));

        String time = simpleDateFormat.format(df);
        return time;
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }
}




