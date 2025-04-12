package com.project.webserver.service;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.project.webserver.model.User;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
//import org.springF

import javax.annotation.Resource;

@Service
public class FirebaseService {
    //TODO logger
    @Resource
    public Environment env;
    private final Firestore firestore;// Inject the path from application.properties
    @Value("${firebase.credentials.path}")
    private final String path = "/Users/matthewhaskell/Documents/freightflow-7c94c-firebase-adminsdk-fbsvc-d9199f8420.json";

    public String getPath() {
        return path;
    }

    public FirebaseService() {
        try {
//            System.out.println("Reached Firestore Constructor");
            FileInputStream serviceAccount = new FileInputStream(getPath());

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
    //TODO this is hard to interpret a success message if there's another service invoking. break into responseentity.
    public String addUserDB(User user) {
        try {
//            System.out.println("Reached Firestore addUserDB");
            Firestore db = FirestoreClient.getFirestore();
            // Use username as document ID
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new IllegalArgumentException("'username' must be a non-empty String");
            }
            DocumentReference docRef = db.collection("users").document(user.getUsername());
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            userData.put("password", user.getPassword());  // **HASH THIS PASSWORD**
            userData.put("vin", user.getVin());
            userData.put("licensePlate", user.getLicensePlate());
            userData.put("email", user.getEmail());
            //TODO this doesn't check if there's already a user with this email it seems
            WriteResult result;
            if (!userExists(user)) {
                result = docRef.set(userData).get();
            }
            else {
                return "A User already exists with that username. If you wish to change the information for that user, please use another API.";
            }
            return "User added to Firestore successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding user to Firebase: " + e.getMessage();
        }
    }

    private boolean userExists(User user) throws ExecutionException, InterruptedException {
//        System.out.println("Get user info");
        Firestore db = FirestoreClient.getFirestore();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("'username' must be a non-empty String");
        }
        DocumentReference ref = db.collection("users").document(user.getUsername());
        ApiFuture<DocumentSnapshot> future = ref.get();
        DocumentSnapshot doc = future.get();
        return doc.exists();
    }

    //TODO break out to UserService
    //TODO more elaboration than boolean, return an enum???
    public ResponseEntity<Object>authenticateUserDB(String username, String password) {
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
                    return ResponseEntity.ok("Authentication Successful"); // Passwords match
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication Failed"); // Incorrect password
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Authentication Failed"); // User not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getStackTrace()); // Error occurred
        }
    }

    //TODO add old password entered as validation metric
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

    public String deleteUser(String username) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            db.collection("users").document(username).delete();
            System.out.printf("user deleted: %s\n", username);
            return "User successfully deleted"; //TODO response returns
//        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        } catch (Exception e) {
            System.out.printf("user %s failed to delete. Exception: %s\n", username, e);
            return "There was an issue deleting this user";
        }
    }

    public User getUser(String username) {
        Firestore db = FirestoreClient.getFirestore();
        User user = new User();
        try {
            ApiFuture<DocumentSnapshot> query = db.collection("users").document(username).get();
            Map data = query.get().getData();
            if (data != null && !data.isEmpty()) {
                user.setVin((String) data.get("vin"));
                user.setPassword((String) data.get("password"));
                user.setEmail((String) data.get("email"));
                user.setUsername((String) data.get("username"));
                user.setLicensePlate((String) data.get("licensePlate"));
            } else {
                return null;
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}