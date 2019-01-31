package com.brand.Kratos;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.brand.Kratos.adapter.CommentsAdapter;
import com.brand.Kratos.adapter.RelatedVideosAdapter;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.model.Comments.ResponseCommentAction;
import com.brand.Kratos.model.Comments.ResponseCommentsList;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.network.api;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *
 fragment_comments_dialog
 */
public class CommentsDialog extends BottomSheetDialogFragment {

   MyTextView_Roboto_Medium username_id;
    ImageButton post_comment_id;
    EditText comment_txt_id;
    RecyclerView recycler_comments;
    CommentsAdapter commentsAdapter;
    SwipeRefreshLayout swipeRefreshLayout_comments;
    MaterialLetterIcon budge_id;
    SharedPreferences pref;
    int limit = 20;

    CircleImageView image_thumbnail;
    public CommentsDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comments_dialog, container, false);
        username_id = v.findViewById(R.id.username_id);
        image_thumbnail = v.findViewById(R.id.image_thumbnail);
        post_comment_id = v.findViewById(R.id.post_comment_id);
        swipeRefreshLayout_comments = v.findViewById(R.id.swipeRefreshLayout_comments);
        comment_txt_id = v.findViewById(R.id.comment_txt_id);
        budge_id = v.findViewById(R.id.budge_id);
        System.out.println("getusername"+getUsername());
       // username_id.setText(getUsername());
        recycler_comments = (RecyclerView) v.findViewById(R.id.recycler_comments);
        RecyclerView.LayoutManager flayoutManager = new LinearLayoutManager(getContext());
        pref = getContext().getSharedPreferences("Settings_variables", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String full_name = pref.getString("full_name", "");
        String login_type = pref.getString("type", "");
        String photourl = pref.getString("photourl", "");
        System.out.println("photo"+photourl);
        if(login_type.equals("BASIC")){
            budge_id.setLetter(full_name);
            image_thumbnail.setVisibility(View.GONE);
            budge_id.setVisibility(View.VISIBLE);
        }else{
            Picasso.get()
                    .load(photourl.toString().replace('"',' ').replace(" ",""))
                    .placeholder(R.drawable.imageplaceholder )

                    // .resize(50, 50)
                    // .centerCrop()
                    .fit().centerCrop()
                    .into(image_thumbnail);
            image_thumbnail.setVisibility(View.VISIBLE);
            budge_id.setVisibility(View.GONE);
        }


        recycler_comments.setLayoutManager(flayoutManager);
        recycler_comments.setNestedScrollingEnabled(false);
        swipeRefreshLayout_comments.setEnabled(false);
        swipeRefreshLayout_comments.setRefreshing(true);
        get_comments(MainContainer.vide_code, limit);
        comment_txt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (comment_txt_id.getText().length() > 1) {
                    post_comment_id.setVisibility(View.VISIBLE);
                }else{
                    post_comment_id.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        post_comment_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = comment_txt_id.getText().toString();

                post_comment( MainContainer.vide_code,comment);
                comment_txt_id.setText("Type a comment...");
                post_comment_id.setVisibility(View.GONE);
            }
        });


        return v;
    }
    public void get_comments(String code, int limit_comments){
       // Map<String, String> requestBody = new HashMap<>();
      //  requestBody.put("body", body);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        //.addHeader("timestamp", "153138130")
                        .addHeader("Authorization", MainContainer.token)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.interceptors().add(new LogJsonInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                //  .baseUrl("http://128.199.33.32:3020/api/")
                .baseUrl(api.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        api apiCallInterface = retrofit.create(api.class);


        Call<ResponseCommentsList> call = apiCallInterface.getComments(code,limit_comments);
        call.enqueue(new Callback<ResponseCommentsList>() {
            @Override
            public void onResponse(Call<ResponseCommentsList> call, Response<ResponseCommentsList> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();
                swipeRefreshLayout_comments.setRefreshing(false);
                if(response.isSuccessful()){
                      Toasty.success(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());
                    commentsAdapter = new CommentsAdapter(getContext(),response.body().getData());
                    recycler_comments.setAdapter(commentsAdapter);

                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //Toasty.error(mcontext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // Toasty.error(mcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                /*currenciesAdapter= new CurrenciesAdapter(getContext(),categories);
                spinner.setAdapter(currenciesAdapter);*/
            }

            @Override
            public void onFailure(Call<ResponseCommentsList> call, Throwable t) {
                swipeRefreshLayout_comments.setRefreshing(false);
            }
        });
    }
    public void post_comment(String code, String body){
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("body", body);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        //.addHeader("timestamp", "153138130")
                        .addHeader("Authorization", MainContainer.token)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.interceptors().add(new LogJsonInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                //  .baseUrl("http://128.199.33.32:3020/api/")
                .baseUrl(api.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        api apiCallInterface = retrofit.create(api.class);


        Call<ResponseCommentAction> call = apiCallInterface.getAddCommentAction(code,requestBody);
        call.enqueue(new Callback<ResponseCommentAction>() {
            @Override
            public void onResponse(Call<ResponseCommentAction> call, Response<ResponseCommentAction> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();
                swipeRefreshLayout_comments.setRefreshing(false);
                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());
                   // commentsAdapter = new RelatedVideosAdapter(getContext(),response.body().getData());
                   // recycler_comments.setAdapter(commentsAdapter);
                    get_comments(MainContainer.vide_code, limit);
                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //Toasty.error(mcontext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // Toasty.error(mcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                /*currenciesAdapter= new CurrenciesAdapter(getContext(),categories);
                spinner.setAdapter(currenciesAdapter);*/
            }

            @Override
            public void onFailure(Call<ResponseCommentAction> call, Throwable t) {
                swipeRefreshLayout_comments.setRefreshing(false);
            }
        });
    }
    public String getUsername() {
        AccountManager manager = AccountManager.get(getContext());
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }
}