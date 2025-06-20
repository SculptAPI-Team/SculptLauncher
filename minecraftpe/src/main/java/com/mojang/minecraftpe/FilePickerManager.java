package com.mojang.minecraftpe;

import android.content.Intent;

public class FilePickerManager implements ActivityListener {
    static final int PICK_DIRECTORY_REQUEST_CODE = 246242755;
    FilePickerManagerHandler mHandler;

    private static native void nativeDirectoryPickResult(String uri, String error);

    @Override // com.mojang.minecraftpe.ActivityListener
    public void onDestroy() {
    }

    @Override // com.mojang.minecraftpe.ActivityListener
    public void onResume() {
    }

    @Override // com.mojang.minecraftpe.ActivityListener
    public void onStop() {
    }

    public FilePickerManager(FilePickerManagerHandler handler) {
        this.mHandler = null;
        this.mHandler = handler;
    }

    public void pickDirectory(String prompt, String startingLocationURI) {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        if (prompt != null && !prompt.isEmpty()) {
            intent.putExtra("android.provider.extra.PROMPT", prompt);
        }
        if (startingLocationURI != null && !startingLocationURI.isEmpty()) {
            intent.putExtra("android.provider.extra.INITIAL_URI", startingLocationURI);
        }
        this.mHandler.startPickerActivity(intent, PICK_DIRECTORY_REQUEST_CODE);
    }

    @Override // com.mojang.minecraftpe.ActivityListener
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_DIRECTORY_REQUEST_CODE) {
            if (resultCode == -1) {
                nativeDirectoryPickResult(data.getData().toString(), "");
            } else {
                nativeDirectoryPickResult("", "No directory selected");
            }
        }
    }
}
