package com.example.eventus.data;
import com.example.eventus.data.model.ServerResponse;

import java.io.File;
import java.io.IOException;

import okhttp3.*;

public class FileUploader {

    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private OkHttpClient client = new OkHttpClient();
    private ServerResponse lastResponse;

    public ServerResponse getLastResponse() {
        return lastResponse;
    }
    public interface UploadCallback {
        void onSuccess(ServerResponse response);
        void onFailure(ServerResponse errorMessage);
    }

    public void uploadFile(String url, byte[] file, final UploadCallback callback) {


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("icon", "img.png", RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                callback.onFailure(new ServerResponse(111,e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle success
                callback.onSuccess(new ServerResponse(response.code(), response.body().string()));
            }
        });


    }


}