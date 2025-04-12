package com.project.webserver.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

public class FirebaseConfig {

//    private String firebaseCredentialsPath="/Users/aditilve/Desktop/Application/freightflow-7c94c-firebase-adminsdk-fbsvc-d9199f8420.json";
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            FileInputStream serviceAccount = new FileInputStream(firebaseCredentialsPath);
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}