package org.androidtown.MajorBasicProject2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class Quest2PhotoFragment extends Fragment implements View.OnClickListener {
    public static final int REQUEST_TAKE_CAMERA = 1;
    public static final int REQUEST_TAKE_ALBUM = 2;
    public static final int REQUEST_CROP_IMAGE = 3;
    public static final int MY_PERMISSION_CAMERA = 0;

    Button btBasicSkip;
    Button btBasicNext;

    ArrayList<String> id_image_list = new ArrayList<>();

    AdapterImageGridView adapter;
    GridView glImageGrid;

    String path;
    Uri imageUri, photoUri, albumUri;
    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rd2_photo, container, false);

        ((RegisterActivity) getActivity()).setButtonGone();

        btBasicNext = view.findViewById(R.id.btnAddPhoto);
        btBasicSkip = view.findViewById(R.id.btnSkipPhoto);
        glImageGrid = view.findViewById(R.id.glImageGrid);

        // Data bind GridView with ArrayAdapter
        adapter = new AdapterImageGridView (getActivity(), R.layout.item_imageview);  // GridView 항목의 레이아웃 xml
        glImageGrid.setAdapter(adapter);
        glImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + position,Toast.LENGTH_SHORT).show();
            }
        });
        btBasicNext.setOnClickListener(this);
        btBasicSkip.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSkipPhoto:
                ((RegisterActivity) getActivity()).nextFragment();
                break;
            case R.id.btnAddPhoto:
                checkPermission();
                DialogInterface.OnClickListener cameraListner = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takeCamera();
                    }
                };
                DialogInterface.OnClickListener albumListner = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takeAlbum();
                    }
                };
                DialogInterface.OnClickListener cancleListner = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder
                        .setTitle("업로드 할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListner)
                        .setNegativeButton("앨범선택", albumListner)
                        .setNeutralButton("취소", cancleListner);
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
                break;
        }
    }
    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) ) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                DialogInterface.OnClickListener actionDetailSetting = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package : "+ getActivity().getPackageName()));
                        startActivity(intent);
                    }
                };
                alertBuilder
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNegativeButton("설정", actionDetailSetting)
                        .setCancelable(false)
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},  MY_PERMISSION_CAMERA);
            }
        }
    }
    public void takeCamera(){
        String state = Environment.getExternalStorageState(); //외장메모리 검사
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(intentTakePhoto.resolveActivity(getActivity().getPackageManager())!=null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                }
                catch (IOException ex){
                    Log.e("takeCamera Error", ex.toString());
                }

                if(photoFile != null){
                    //getUriForFile의 두번째 인자는 Mainfest provider의 authorities와 일치해야 함
                    Uri providerUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                    imageUri = providerUri;

                    //인텐트에 전달 할 때는 FileProvider의 Return값인 content://로만
                    //providerUri의 값에 카메라 데이터를 넣어서 보냄
                    photoUri = Uri.fromFile(photoFile); //해당 파일의 위치값을 불러와 EXTRA에 실어 보내기
                    intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, providerUri);
                    startActivityForResult(intentTakePhoto, REQUEST_TAKE_CAMERA);
                }
            }
        } else  {
            Toast.makeText(getActivity(), "저장공간이 접근불가능한 기기잆니다.", Toast.LENGTH_LONG).show();
            return;
        }
    }
    public void takeAlbum(){
        Log.i("getAlbum", "Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }
    public File createImageFile() throws IOException {
        //Creating an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/Pictures", "gyeom");

        if(!storageDir.exists()){
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        path = imageFile.getAbsolutePath();
        return imageFile;
    }
    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent intentMediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); //해당경로에 있는 파일을 객체화 (새로운 파일을 만듬)
        File file = new File(path);
        Uri cotentUri = Uri.fromFile(file);
        intentMediaScan.setData(cotentUri);
        getActivity().sendBroadcast(intentMediaScan);
        Toast.makeText(getActivity(), "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
    private void cropIamge(){
        Log.i("cropImage", "Call");
        Log.i("cropImage", "photoUri = " + photoUri + " / albumUri : "+ albumUri);

        Intent intentCropImage = new Intent("com.android.camera.action.CROP");
        intentCropImage.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentCropImage.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentCropImage.setDataAndType(photoUri, "image/*");
        //intentCropImage.putExtra("outputX", 200); // CROP한 이미지의 X축 크기
        //intentCropImage.putExtra("outputY", 200);
        //intentCropImage.putExtra("aspectX", 1); // CROP한 박스의 X축 비율
        //intentCropImage.putExtra("aspectY", 1);
        intentCropImage.putExtra("scale", true);
        intentCropImage.putExtra("return-inputData", true);
        intentCropImage.putExtra("output", albumUri);

        startActivityForResult(intentCropImage, REQUEST_CROP_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        switch (requestCode){
            case REQUEST_TAKE_CAMERA:{
                try{
                    Log.i("REQUEST_TAKE_PHOTO", "OK");
                    galleryAddPic();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadImage(bitmap);
                    addImageGrid(bitmap);
                }
                catch (Exception e){
                    Log.e("REQUEST_TAKE_PHOTO", e.toString());
                }
                break;
            }
            case REQUEST_TAKE_ALBUM:{
                if(data.getData() != null){
                    try {
                        Log.i("REQUEST_TAKE_ALBUM", "OK");
                        File albumFile = null;
                        albumFile = createImageFile();
                        photoUri = data.getData();
                        albumUri = Uri.fromFile(albumFile);
                        cropIamge();
                    } catch (Exception e){
                        Log.e("TAKE_ALBUM_SIGNLE ERROR", e.toString());
                    }
                }
                break;
            }
            case REQUEST_CROP_IMAGE:{
                galleryAddPic();
                //iv.setImageURI(albumUri);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), albumUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadImage(bitmap);
                addImageGrid(bitmap);
                break;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSION_CAMERA){
            for(int i=0; i<grantResults.length;i++){
                //grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if(grantResults[i]<0){
                    Toast.makeText(getActivity().getApplicationContext(), "해당 권한을 활성화하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage(Bitmap bitmap) {
        String blob_image = getStringImage(bitmap);
        HandlerDB upload_image = new HandlerDB("upload_image.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("blob_image", blob_image);
        upload_image.execute(input);
        upload_image.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    id_image_list.add(dbResult.get(0).get("id_image"));
                    Intent intent = new Intent(getActivity(), PageImageActivity.class);
                    intent.putExtra("id_image", dbResult.get(0).get("id_image"));
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addImageGrid(Bitmap bmp){
        adapter.addItem(bmp);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((RegisterActivity)getActivity()).setButtonVisible();
        ((RegisterActivity)getActivity()).id_image_list.clear();
        for(int i = 0; i< id_image_list.size(); i++)
            ((RegisterActivity)getActivity()).id_image_list.add(id_image_list.get(i).toString());
    }
}