//
// Created by 微晞鸢徊 on 2025/6/16.
//

#include <jni.h>
#include <android/native_activity.h>
#include <dlfcn.h>

void (*mOnCreateFunc)(ANativeActivity*,void*,size_t) = 0;
void (*mFinishFunc)(ANativeActivity*) = 0;
void (*mMainFunc)(struct android_app*) = 0;

extern "C" void ANativeActivity_onCreate(ANativeActivity* activity,void* savedState, size_t savedStateSize) {
    mOnCreateFunc(activity,savedState,savedStateSize);
}

extern "C" void ANativeActivity_finish(ANativeActivity* activity) {
    mFinishFunc(activity);
}

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM*,void*) {
return JNI_VERSION_1_6;
}

extern "C" void android_main(struct android_app* state) {
    mMainFunc(state);
}

extern "C" JNIEXPORT void JNICALL Java_org_thelauncher_sculptlauncher_backend_launcher_util_LibraryLoader_nativeOnLauncherLoaded(JNIEnv*env, jobject thiz, jstring libPath) {
    const char * mNativeLibPath = env->GetStringUTFChars(libPath, 0);
    void* imageMCPE=(void*) dlopen(mNativeLibPath,RTLD_LAZY);
    mOnCreateFunc = (void(*)(ANativeActivity*,void*,size_t)) dlsym(imageMCPE,"ANativeActivity_onCreate");
    mFinishFunc = (void(*)(ANativeActivity*)) dlsym(imageMCPE,"ANativeActivity_finish");
    mMainFunc =(void(*)(struct android_app*)) dlsym(imageMCPE,"android_main");
    dlclose(imageMCPE);
    env->ReleaseStringUTFChars(libPath,mNativeLibPath);
}