package com.example.wishhub.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAhLj_ptI:APA91bGXrDP_puUX8NW9Wv8vjGXJZjSS2iKcloZ4bzZ9ZMDVHBmK4t7pbVGqAPRklR_qsMa-AO_oEtfIItFCJtHiAa1NfD3WV0W_rT6sGSmMmudfeXteNXSkDKmToc87GstGzvJ8kk00"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
