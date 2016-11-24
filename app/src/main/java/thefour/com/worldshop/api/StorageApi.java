package thefour.com.worldshop.api;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import thefour.com.worldshop.models.Item;

/**
 * Created by Quang Quang on 11/23/2016.
 */

public class StorageApi {
    public static void updateImage(Uri file, Item item,
                                   final OnSuccessListener onSuccessListener,
                                   final OnFailureListener onFailureListener){
        StorageReference ref = getStorageRef().child(item.getName()+"_"+System.currentTimeMillis());
        ref.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                onSuccessListener.onSuccess(taskSnapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailureListener.onFailure(e);
            }
        });

    }

    public static StorageReference getStorageRef(){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        storage.child("worldShop").child("image");
        return storage;
    }
}
