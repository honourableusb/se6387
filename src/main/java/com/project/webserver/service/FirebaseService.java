package com.project.webserver.service;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.database.FirebaseDatabase;
import com.project.webserver.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;


@Service
public class FirebaseService {

    private Firestore firestore;// Inject the path from application.properties

    public FirebaseService() {
        try {
            System.out.println("Reached Firestore Constructor");
            String firebaseCredentialsPath = "/Users/aditilve/Desktop/Application/freightflow-7c94c-firebase-adminsdk-fbsvc-d9199f8420.json";
            FileInputStream serviceAccount = new FileInputStream(firebaseCredentialsPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            this.firestore = FirestoreClient.getFirestore();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        } // Initialize Firebase

    }

    // Add user to Firebase
    public String addUserDB(User user) {
        try {
            System.out.println("Reached Firestore addUserDB");
            Firestore db = FirestoreClient.getFirestore();
            // Use username as document ID
            DocumentReference docRef = db.collection("users").document(user.getUsername());
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            userData.put("password", user.getPassword());  // **HASH THIS PASSWORD**
            userData.put("vin", user.getVin());
            userData.put("licensePlate", user.getLicesePlate());
            userData.put("email", user.getEmail());
            WriteResult result = docRef.set(userData).get();
            return "User added to Firestore successfully! Update time: " + result.getUpdateTime();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding user to Firebase: " + e.getMessage();
        }
    }

    public boolean authenticateUserDB(String username, String password) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference docRef = db.collection("users").document(username);
            DocumentSnapshot document = docRef.get().get();

            if (document.exists()) {
                Map<String, Object> data = document.getData();
                String storedPassword = (String) data.get("password"); // Get stored password

                // **SECURE PASSWORD CHECK**
                // 1. Retrieve the salt from the database (if you're using one).
                // 2. Hash the provided password using the same salt.
                // 3. Compare the hashed passwords.

                if (storedPassword != null && storedPassword.equals(password)) {
                    return true; // Passwords match
                } else {
                    return false; // Incorrect password
                }
            } else {
                return false; // User not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Error occurred
        }
    }

    public String updatePassword(String email, String newPassword) {
        try {
            Firestore db = FirestoreClient.getFirestore();

            // Query the collection to find the user with the matching email
            Query query = db.collection("users").whereEqualTo("email", email);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            // Iterate over the results and update the password
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                DocumentReference docRef = db.collection("users").document(document.getId());

                Map<String, Object> updates = new HashMap<>();
                updates.put("password", newPassword);

                WriteResult result = docRef.update(updates).get();
                return "Password updated successfully!";
            }

            return "Email not found "; // No user found with the given email
        } catch (Exception e) {
            e.printStackTrace();
            return "Email not found ";
        }
    }
}