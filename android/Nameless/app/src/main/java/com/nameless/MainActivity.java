package com.nameless;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.widget.TintEditText;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.markushi.ui.RevealColorView;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends Activity implements View.OnClickListener {

    Button connectButton;
    RevealColorView revealColorView;
    static SharedPreferences sharedPreferences;
    ArrayList<String> users = new ArrayList<>();
    public static ArrayList<String> messages = new ArrayList<>();
    static ListView conversationListView;
    RelativeLayout messagingLayout;
    TintEditText newMessageEditText;
    ImageButton sendButton;
    public static Adapter adapter;
    public static boolean paused;
    boolean revealed;
    Button nextButton, closeButton;
    ProgressWheel progress;
    public static boolean isTalking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        initializeViews();
        adapter = new Adapter();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (sharedPreferences.getBoolean("userSaved", false) != true)
            setupParseForInitialUse();
        getRandomUsers();
        getMessages();
    }

    public static void update() {
        adapter.notifyDataSetChanged();
        conversationListView.setSelection(adapter.getCount() - 1);
    }

    public void initializeViews() {
        connectButton = (Button) findViewById(R.id.connectButton);
        revealColorView = (RevealColorView) findViewById(R.id.reveal);
        conversationListView = (ListView) findViewById(R.id.conversationListView);
        messagingLayout = (RelativeLayout) findViewById(R.id.messagingLayout);
        newMessageEditText = (TintEditText) findViewById(R.id.newMessageEditText);
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        closeButton = (Button) findViewById(R.id.closeConversationButton);
        nextButton = (Button) findViewById(R.id.nextConversationButton);
        progress = (ProgressWheel) findViewById(R.id.progressWheel);

        conversationListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        conversationListView.setStackFromBottom(true);

        setListeners();
    }

    public void setListeners() {
        connectButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    public void setupParseForInitialUse() {
        final ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();

//        while (parseInstallation.getObjectId() == null) {
//            if (parseInstallation.getObjectId() != null)
//                break;
//        }

        ParseObject parseObject = new ParseObject("User");
        parseObject.put("username", parseInstallation.getObjectId());
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("userSaved", true);
                    editor.commit();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connectButton:
                openChat();
                break;
            case R.id.sendButton:
                sendNotification();
                break;
            case R.id.closeConversationButton:

                if (!messages.isEmpty()) {
                    final MaterialDialog dialog = new MaterialDialog(this);
                    dialog.setMessage("Are you sure you want to end this conversation? You will never be able to talk to this user again.")
                            .setPositiveButton("YES", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                revealColorView.animate().alpha(0.0f).setInterpolator(new AccelerateDecelerateInterpolator());
                                    revealColorView.hide((revealColorView.getLeft() + revealColorView.getRight()) / 2,
                                            (revealColorView.getTop() + revealColorView.getBottom()) / 2, getColor(R.color.green_800), 0, 450, null);
                                    messagingLayout.animate().alpha(0.0f).setInterpolator(new AccelerateDecelerateInterpolator());
                                    messagingLayout.setVisibility(View.INVISIBLE);

//            revealColorView.setVisibility(View.INVISIBLE);
                                    connectButton.setVisibility(View.VISIBLE);
                                    connectButton.setAlpha(0.0f);
                                    connectButton.animate().alpha(1.0f).translationY(0).setInterpolator(new DecelerateInterpolator());
                                    revealed = false;
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            }).show();

                } else {
//                revealColorView.animate().alpha(0.0f).setInterpolator(new AccelerateDecelerateInterpolator());
                    revealColorView.hide((revealColorView.getLeft() + revealColorView.getRight()) / 2,
                            (revealColorView.getTop() + revealColorView.getBottom()) / 2, getColor(R.color.green_800), 0, 450, null);
                    messagingLayout.animate().alpha(0.0f).setInterpolator(new AccelerateDecelerateInterpolator());
                    messagingLayout.setVisibility(View.INVISIBLE);

//            revealColorView.setVisibility(View.INVISIBLE);
                    connectButton.setVisibility(View.VISIBLE);
                    connectButton.setAlpha(0.0f);
                    connectButton.animate().alpha(1.0f).translationY(0).setInterpolator(new AccelerateDecelerateInterpolator());
                    revealed = false;
                }
                break;
            case R.id.nextConversationButton:
                if (!messages.isEmpty()) {
                    final MaterialDialog dialog2 = new MaterialDialog(this);
                    dialog2.setMessage("Are you sure you want to end this conversation? You will never be able to talk to this user again.")
                            .setPositiveButton("YES", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    messages.clear();
                                    adapter.notifyDataSetChanged();
                                    progress.spin();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.stopSpinning();
                                            progress.animate().alpha(0.0f).setInterpolator(new DecelerateInterpolator());
                                            progress.setVisibility(View.INVISIBLE);
                                            Toast.makeText(MainActivity.this, "Person found", Toast.LENGTH_SHORT).show();
                                        }
                                    }, 3000);
                                    dialog2.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            }).show();
                } else {
                    messages.clear();
                    adapter.notifyDataSetChanged();
                    progress.spin();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.stopSpinning();
                            progress.animate().alpha(0.0f).setInterpolator(new DecelerateInterpolator());
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Person found", Toast.LENGTH_SHORT).show();
                        }
                    }, 3000);
                }
                break;
        }
    }

    public void openChat() {

        connectButton.animate().translationY(-650).setInterpolator(new DecelerateInterpolator()).alpha(0.0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealColorView.setVisibility(View.VISIBLE);
                revealColorView.reveal((connectButton.getLeft() + connectButton.getRight()) / 2,
                        (int) ((connectButton.getTop() + connectButton.getBottom()) / 2.8), getColor(R.color.white), 0, 450, null);
                revealed = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messagingLayout.setVisibility(View.VISIBLE);
                        messagingLayout.animate().alpha(1.0f).setInterpolator(new AccelerateDecelerateInterpolator());

                    }
                }, 300);
            }
        }, 300);
    }

    public void getRandomUsers() {

        ArrayList<String> arr = new ArrayList<String>();
        arr.add(new ParseInstallation().getCurrentInstallation().getObjectId());
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereNotContainedIn("username", arr);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null && parseObjects != null && parseObjects.size() != 0) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        users.add(parseObjects.get(i).getString("username"));
                        Log.d("eeee", parseObjects.get(i).getObjectId());
                    }

                    int random = new Random().nextInt(users.size());
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("CurrentId", users.get(random));
                    edit.commit();

                } else if (e != null) {
                    Log.d("Error = ", e.getMessage());
                }
            }
        });
    }

    public void getMessages() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username", new ParseInstallation().getCurrentInstallation().getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null && object.getList("messages") != null) {
                    for (int i = 0; i < object.getList("messages").size(); i++)
                        messages.add((String) object.getList("messages").get(i));

                    conversationListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Log.d("messages: ", messages.toString());
                } else if (e != null) {
                    Log.d("Error = ", e.getMessage() + " No Messages");
                }
            }
        });
    }

    public void sendNotification() {
        String string = newMessageEditText.getText().toString();
        if (string != null && !string.isEmpty() && !string.trim().isEmpty()) {
            ParsePush parsePush = new ParsePush();
            ParseQuery pQuery = ParseInstallation.getQuery();
            pQuery.whereEqualTo("objectId", getCurrentChatId());
            try {
                parsePush.setData(new JSONObject().put("mes", newMessageEditText.getText().toString())
                        .put("action", "com.nameless.CUSTOM_NOTIFICATION"
                        ));
            } catch (JSONException j) {

            }

            parsePush.setQuery(pQuery);
            parsePush.sendInBackground();
            messages.add(new ParseInstallation().getCurrentInstallation().getObjectId() + "//"
                    + newMessageEditText.getText().toString());
            adapter.notifyDataSetChanged();
            conversationListView.setSelection(adapter.getCount() - 1);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
            query.whereEqualTo("username", getCurrentChatId());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        object.add("messages", new ParseInstallation().getCurrentInstallation().getObjectId() + "//" + newMessageEditText.getText().toString()
                        );
                        object.saveInBackground();
                        final ParseQuery<ParseObject> query2 = ParseQuery.getQuery("User");
                        query2.whereEqualTo("username", new ParseInstallation().getCurrentInstallation().getObjectId());
                        query2.getFirstInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    object.add("messages", new ParseInstallation().getCurrentInstallation().getObjectId() + "//" + newMessageEditText.getText().toString()
                                    );
                                    object.saveInBackground();
                                    newMessageEditText.setText("");
                                } else {
                                    Log.d("Error: ", e.getMessage());
                                }
                            }
                        });

                    } else {
                        Log.d("Error: ", e.getMessage());
                    }
                }
            });


            Log.d("dddd", messages.toString());
        }
    }

    public static String getCurrentChatId() {
        return sharedPreferences.getString("CurrentId", null);
    }

    public int getColor(int id) {
        return getResources().getColor(id);
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, viewGroup, false);

            TextView messageLeft = (TextView) view.findViewById(R.id.messageLeft);
//            TextView timeLeft = (TextView) view.findViewById(R.id.timeLeft);
            TextView messageRight = (TextView) view.findViewById(R.id.messageRight);
//            TextView timeRight = (TextView) view.findViewById(R.id.timeRight);
            LinearLayout cardView = (LinearLayout) view.findViewById(R.id.cardView);
            CardView cardView2 = (CardView) view.findViewById(R.id.cardViewLayout);
            ImageView buttonLeft, buttonRight;
            buttonLeft = (ImageView) view.findViewById(R.id.imageLeft);
            buttonRight = (ImageView) view.findViewById(R.id.imageRight);
            cardView2.setCardElevation(2.0f);
            cardView2.setRadius(8.0f);

            String[] array;
            String string = getItem(position);
            array = string.split("//");

            messageLeft.setText("");
            messageRight.setText("");

            if (!array[0].equals(new ParseInstallation().getCurrentInstallation().getObjectId())) {
                messageLeft.setVisibility(View.VISIBLE);
                buttonLeft.setVisibility(View.VISIBLE);
                buttonRight.setVisibility(View.INVISIBLE);
//                timeLeft.setVisibility(View.VISIBLE);
                cardView.setGravity(Gravity.LEFT | Gravity.CENTER);
                messageLeft.setText(array[1]);
                cardView2.setCardBackgroundColor(getColor(R.color.green_800));
//                timeLeft.setText(array[2]);
            } else {
                messageRight.setVisibility(View.VISIBLE);
                buttonRight.setVisibility(View.VISIBLE);
                buttonLeft.setVisibility(View.INVISIBLE);
//                timeRight.setVisibility(View.VISIBLE);
                cardView.setGravity(Gravity.RIGHT | Gravity.CENTER);
                messageRight.setText(array[1]);
                cardView2.setCardBackgroundColor(getColor(R.color.white));
//                timeRight.setText(array[2]);
            }
            return view;
        }
    }

    @Override
    protected void onResume() {
        paused = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        paused = true;
        super.onPause();
    }
}