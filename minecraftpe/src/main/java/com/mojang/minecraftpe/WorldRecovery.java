package com.mojang.minecraftpe;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.StatFs;
import android.util.Log;
import androidx.documentfile.provider.DocumentFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: classes.dex */
public class WorldRecovery {
    private ContentResolver mContentResolver;
    private Context mContext;
    private int mTotalFilesToCopy = 0;
    private long mTotalBytesRequired = 0;

    private static native void nativeComplete();

    private static native void nativeError(String error, long bytesRequired, long bytesAvailable);

    private static native void nativeUpdate(String status, int filesTotal, int filesCompleted, long bytesTotal, long bytesCompleted);

    WorldRecovery(Context context, ContentResolver contentResolver) {
        this.mContext = null;
        this.mContentResolver = null;
        this.mContext = context;
        this.mContentResolver = contentResolver;
    }

    public String migrateFolderContents(String srcURIString, String destFolderString) {
        final DocumentFile documentFileFromTreeUri = DocumentFile.fromTreeUri(this.mContext, Uri.parse(srcURIString));
        if (documentFileFromTreeUri == null) {
            return "Could not resolve URI to a DocumentFile tree: " + srcURIString;
        }
        if (!documentFileFromTreeUri.isDirectory()) {
            return "Root file of URI is not a directory: " + srcURIString;
        }
        final File file = new File(destFolderString);
        if (!file.isDirectory()) {
            return "Destination folder does not exist: " + destFolderString;
        }
        String[] list = file.list();
        Objects.requireNonNull(list);
        if (list.length != 0) {
            return "Destination folder is not empty: " + destFolderString;
        }
        new Thread(new Runnable() { // from class: com.mojang.minecraftpe.-$$Lambda$WorldRecovery$UZsxLuVpk7mq2T0LZJTbEFR4TUM
            @Override // java.lang.Runnable
            public final void run() {
                lambda$migrateFolderContents$0$WorldRecovery(documentFileFromTreeUri, file);
            }
        }).start();
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: doMigration, reason: merged with bridge method [inline-methods] */
    public void lambda$migrateFolderContents$0$WorldRecovery(DocumentFile root, File destFolder) {
        ArrayList<DocumentFile> arrayList = new ArrayList<>();
        this.mTotalFilesToCopy = 0;
        long j = 0;
        this.mTotalBytesRequired = 0L;
        generateCopyFilesRecursively(arrayList, root);
        long availableBytes = new StatFs(destFolder.getAbsolutePath()).getAvailableBytes();
        long j2 = this.mTotalBytesRequired;
        if (j2 >= availableBytes) {
            nativeError("Insufficient space", j2, availableBytes);
            return;
        }
        String path = root.getUri().getPath();
        String str = destFolder + "_temp";
        File file = new File(str);
        byte[] bArr = new byte[8192];
        Iterator<DocumentFile> it = arrayList.iterator();
        long j3 = 0;
        int i = 0;
        while (it.hasNext()) {
            DocumentFile next = it.next();
            String str2 = str + next.getUri().getPath().substring(path.length());
            if (next.isDirectory()) {
                File file2 = new File(str2);
                if (!file2.isDirectory()) {
                    Log.i("Minecraft", "Creating directory '" + str2 + "'");
                    if (!file2.mkdirs()) {
                        nativeError("Could not create directory: " + str2, j, j);
                        return;
                    }
                } else {
                    Log.i("Minecraft", "Directory '" + str2 + "' already exists");
                }
            } else {
                Log.i("Minecraft", "Copying '" + next.getUri().getPath() + "' to '" + str2 + "'");
                StringBuilder sb = new StringBuilder();
                sb.append("Copying: ");
                sb.append(str2);
                i++;
                nativeUpdate(sb.toString(), this.mTotalFilesToCopy, i, this.mTotalBytesRequired, j3);
                try {
                    InputStream inputStreamOpenInputStream = this.mContentResolver.openInputStream(next.getUri());
                    FileOutputStream fileOutputStream = new FileOutputStream(str2);
                    while (true) {
                        int i2 = inputStreamOpenInputStream.read(bArr, 0, 8192);
                        if (i2 < 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, i2);
                        j3 += i2;
                    }
                    fileOutputStream.close();
                    inputStreamOpenInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    nativeError(e.getMessage(), 0L, 0L);
                    return;
                }
            }
            j = 0;
        }
        if (destFolder.delete()) {
            if (file.renameTo(destFolder)) {
                nativeComplete();
                return;
            }
            if (destFolder.mkdir()) {
                nativeError("Could not replace destination directory: " + destFolder.getAbsolutePath(), 0L, 0L);
                return;
            }
            nativeError("Could not recreate destination directory after failed replace: " + destFolder.getAbsolutePath(), 0L, 0L);
            return;
        }
        nativeError("Could not delete empty destination directory: " + destFolder.getAbsolutePath(), 0L, 0L);
    }

    private void generateCopyFilesRecursively(ArrayList<DocumentFile> result, DocumentFile root) {
        for (DocumentFile documentFile : root.listFiles()) {
            result.add(documentFile);
            if (documentFile.isDirectory()) {
                generateCopyFilesRecursively(result, documentFile);
            } else {
                this.mTotalBytesRequired += documentFile.length();
                this.mTotalFilesToCopy++;
            }
        }
    }
}
