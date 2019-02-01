package com.brand.Kratos.network;


import com.brand.Kratos.model.Categories.ResponseCategories;
import com.brand.Kratos.model.Comments.ResponseCommentAction;
import com.brand.Kratos.model.Comments.ResponseCommentsList;
import com.brand.Kratos.model.Login.CallbackResponseLogin;
import com.brand.Kratos.model.PostTags.TagSubscribe;
import com.brand.Kratos.model.Registration.CallbackResponseRegistration;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.model.VideoDetails.ReesponseContentDetails;
import com.brand.Kratos.model.VideoLogAction.ResponseVideoLogAction;
import com.brand.Kratos.model.sports.ResponseSports;
import com.brand.Kratos.model.tags.ResponseTagSubscrription;
import com.brand.Kratos.model.tags.ResponseTags;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface api {
   //String BaseUrl = "http://188.166.157.18:8082/api/";
    String BaseUrl = "https://www.kratosbrand.com/api/";


    @Headers("Content-Type: application/json")
    @GET("categories") // use @POST if api is post.
    Call<ResponseCategories> getCategories(@Query("token") String token);


    @Headers("Content-Type: application/json")
    @GET("tags/list") // use @POST if api is post.
    Call<ResponseTags> getTags(@Query("token") String token);

   @Headers("Content-Type: application/json")
   @POST("tags/subscribe") // use @POST if api is post.
    Call<TagSubscribe> getTagsSubscription(@Body JsonObject getsignup_resp);

        //getting detals of one video
    @Headers("Content-Type: application/json")
    @GET("content") // use @POST if api is post.
    Call<ReesponseContentDetails> getContent_details(
                                     @Query("code") String code
                                     );

    @Headers("Content-Type: application/json")
    @GET("content") // use @POST if api is post.
    Call<ResponseContent> getContent(@Query("category") String category,
                                     @Query("page") Integer integer,
                                     @Query("limit") Integer limit,
                                     @Query("code") String code,
                                     @Query("type") String type,
                                     @Query("search") String search,
                                     @Query("sort") String sort,
                                     @Query("direction") String direction
                                     );
    @Headers("Content-Type: application/json")
    @GET("content") // use @POST if api is post.
    Call<ResponseContent> getContentForTag(@Query("category") String category,
                                     @Query("page") Integer integer,
                                     @Query("limit") Integer limit,
                                     @Query("code") String code,
                                     @Query("type") String type,
                                     @Query("search") String search,
                                     @Query("sort") String sort,
                                     @Query("direction") String direction,
                                     @Query("tag") int tag_id
                                     );
    @Headers("Content-Type: application/json")
    @GET("content") // use @POST if api is post.
    Call<ResponseContent> getHomeContent(
                                     @Query("page") Integer integer,
                                     @Query("limit") Integer limit,
                                     @Query("recommended") Boolean recommended,
                                     @Query("direction") String direction
                                     );
    @Headers("Content-Type: application/json")
    @GET("content") // use @POST if api is post.
    Call<ResponseContent> getSportsContent(
                                    @Query("page") Integer integer,
                                    @Query("limit") Integer limit,
                                    @Query("code") String code,
                                    @Query("type") String type,
                                    @Query("search") String search,
                                    @Query("sort") String sort,
                                    @Query("direction") String direction,

                                    @Query("sport") Integer sport
                                     );
//category page limit code type search sort direction

    @Headers("Content-Type: application/json")
    @POST("register") // use @POST if api is post.
    Call<CallbackResponseRegistration> getsignupResponse(@Body Map<String, String> getsignup_resp);

    @Headers("Content-Type: application/json")
    @POST("login") // use @POST if api is post.
    Call<CallbackResponseLogin> getloginResponse(@Body Map<String, String> getsignup_resp);

    @Headers("Content-Type: application/json")
    @POST("tags/subscribe") // use @POST if api is post.
    Call<ResponseTagSubscrription> getTagSubscriptionResponse(@Body Map<String, String> get_tag_subscription);


    @Headers("Content-Type: application/json")
    @POST("content/log-action") // use @POST if api is post.
    Call<ResponseVideoLogAction> getResponseVideoLogAction (@Body Map<String, String> get_video_log_action);


    @Headers("Content-Type: application/json")
    @POST("comments/{video}") // add a comment.
    Call<ResponseCommentAction> getAddCommentAction (@Path("video") String code, @Body Map<String, String> get_comment_action);
   @Headers("Content-Type: application/json")
   @GET("comments/{video}") // use @POST if api is post.
   Call<ResponseCommentsList> getComments(@Path("video") String code,@Query("limit") Integer limit);


 @Headers("Content-Type: application/json")
   @GET("sports") // use @POST if api is post.
   Call<ResponseSports> getSports();



}
