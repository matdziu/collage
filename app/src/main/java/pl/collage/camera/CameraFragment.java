package pl.collage.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Range;
import android.util.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.collage.R;
import pl.collage.base.BaseFragment;
import pl.collage.home.HomeActivity;
import pl.collage.sendimage.SendImageActivity;

public class CameraFragment extends BaseFragment {

    @BindView(R.id.texture_view)
    TextureView textureView;

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.fab_take_picture)
    FloatingActionButton takePictureButton;

    @BindView(R.id.fab_switch_camera)
    FloatingActionButton switchCameraButton;

    @BindView(R.id.fab_upload_photo)
    FloatingActionButton uploadPhotoButton;

    private static final int CAMERA_FRAGMENT_PERMISSIONS_CODE = 0;
    private int cameraFacing;
    private boolean fragmentVisible;
    private boolean isInPreviewMode;

    private Size previewSize;
    private String cameraId;

    private TextureView.SurfaceTextureListener surfaceTextureListener;

    private CameraDevice cameraDevice;
    private CameraDevice.StateCallback stateCallback;
    private CameraManager cameraManager;
    private CameraCaptureSession cameraCaptureSession;

    private CaptureRequest captureRequest;
    private CaptureRequest.Builder captureRequestBuilder;

    private Handler backgroundHandler;
    private HandlerThread backgroundThread;

    private int screenWidth;
    private int screenHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screenWidth = getScreenSize().x;
        screenHeight = getScreenSize().y;

        requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                CAMERA_FRAGMENT_PERMISSIONS_CODE);

        cameraFacing = CameraCharacteristics.LENS_FACING_BACK;
        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        surfaceTextureListener = initSurfaceTextureListener();
        stateCallback = initStateCallback();
    }

    private TextureView.SurfaceTextureListener initSurfaceTextureListener() {
        return new TextureView.SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                                  int width, int height) {
                setUpCamera(screenWidth, screenHeight);
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                    int width, int height) {
                // onSurfaceTextureSizeChanged()
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                // onSurfaceTextureUpdated()
            }
        };
    }

    private CameraDevice.StateCallback initStateCallback() {
        return new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                CameraFragment.this.cameraDevice = cameraDevice;
                createCameraPreviewSession();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                cameraDevice.close();
                CameraFragment.this.cameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                CameraFragment.this.cameraDevice = null;
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getContext(), "Couldn't access camera or save picture",
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fragmentVisible) {
            hideSystemUI();
        }
        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        openBackgroundThread();
        startOpeningCamera();

        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP
                            && keyCode == KeyEvent.KEYCODE_BACK
                            && fragmentVisible && isInPreviewMode) {
                        unlock();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        closeCamera();
        closeBackgroundThread();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    @Override
    public void setMenuVisibility(boolean fragmentVisible) {
        super.setMenuVisibility(fragmentVisible);
        this.fragmentVisible = fragmentVisible;
        HomeActivity homeActivity = (HomeActivity) getActivity();
        if (homeActivity != null) {
            if (fragmentVisible) {
                hideSystemUI();
                homeActivity.hideHomeNavigation();
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItem.setVisible(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SEND_IMAGE) {
            if (resultCode == RESULT_PICTURE_SENT) {
                unlock();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpCamera(int width, int height) {
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics =
                        cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                        cameraFacing) {
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    previewSize = chooseOptimalSize(streamConfigurationMap
                            .getOutputSizes(SurfaceTexture.class), width, height);
                    this.cameraId = cameraId;
                    return;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            fixDarkPreview();

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                captureRequest = captureRequestBuilder.build();
                                CameraFragment.this.cameraCaptureSession = cameraCaptureSession;
                                CameraFragment.this.cameraCaptureSession
                                        .setRepeatingRequest(captureRequest, null, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            // onConfigureFailed()
                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void startOpeningCamera() {
        if (textureView.isAvailable()) {
            setUpCamera(screenWidth, screenHeight);
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    private void lock(Bitmap previewImage) {
        isInPreviewMode = true;
        imageView.setVisibility(View.VISIBLE);
        textureView.setVisibility(View.GONE);
        takePictureButton.setVisibility(View.GONE);
        switchCameraButton.setVisibility(View.GONE);
        uploadPhotoButton.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(previewImage);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void unlock() {
        imageFile.delete();
        isInPreviewMode = false;
        imageView.setVisibility(View.GONE);
        textureView.setVisibility(View.VISIBLE);
        takePictureButton.setVisibility(View.VISIBLE);
        switchCameraButton.setVisibility(View.VISIBLE);
        uploadPhotoButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.fab_upload_photo)
    public void onUploadButtonClicked() {
        Intent intent = new Intent(getContext(), SendImageActivity.class);
        intent.putExtra(IMAGE_FILE_PATH, imageFile.getPath());
        intent.putExtra(IMAGE_FILE_NAME, imageFile.getName());
        startActivityForResult(intent, REQUEST_SEND_IMAGE);
    }

    @OnClick(R.id.fab_take_picture)
    public void captureImage() {
        try {
            imageFile = createImageFile(createImageGallery());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock(textureView.getBitmap());
        backgroundHandler.post(new Runnable() {
            FileOutputStream outputPhoto = null;

            @Override
            public void run() {
                try {
                    outputPhoto = new FileOutputStream(imageFile);
                    textureView.getBitmap()
                            .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputPhoto != null) {
                            outputPhoto.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @OnClick(R.id.fab_switch_camera)
    public void switchCamera() {
        closeCamera();

        if (cameraFacing == CameraCharacteristics.LENS_FACING_BACK) {
            cameraFacing = CameraCharacteristics.LENS_FACING_FRONT;
        } else if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
            cameraFacing = CameraCharacteristics.LENS_FACING_BACK;
        }

        startOpeningCamera();
    }

    private Size chooseOptimalSize(Size[] outputSizes, int width, int height) {
        double preferredRatio = height / (double) width;
        Size currentOptimalSize = outputSizes[0];
        double currentOptimalRatio = currentOptimalSize.getWidth() / (double) currentOptimalSize.getHeight();
        for (Size currentSize : outputSizes) {
            double currentRatio = currentSize.getWidth() / (double) currentSize.getHeight();
            if (Math.abs(preferredRatio - currentRatio) <
                    Math.abs(preferredRatio - currentOptimalRatio)) {
                currentOptimalSize = currentSize;
                currentOptimalRatio = currentRatio;
            }
        }
        return currentOptimalSize;
    }

    // this method is supposed to fix auto exposure problems resulting in dark preview
    private void fixDarkPreview() throws CameraAccessException {
        Range<Integer>[] autoExposureFPSRanges = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);

        if (autoExposureFPSRanges != null) {
            for (Range<Integer> autoExposureRange : autoExposureFPSRanges) {
                if (autoExposureRange.equals(Range.create(15, 30))) {
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
                            Range.create(15, 30));
                }
            }
        }
    }
}
