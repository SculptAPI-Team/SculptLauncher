//-------------------------------------------------------------
// Includes
//-------------------------------------------------------------

#include <jni.h>
#include <string>
#include <cxxabi.h>
#include <dlfcn.h>

//-------------------------------------------------------------
// Variants
//-------------------------------------------------------------

JavaVM* mJvm = nullptr;
//std::string* mAddrAndroidAppDataPath = nullptr;
std::string mMCPENativeLibPath;

//-------------------------------------------------------------
// Methods Definition
//-------------------------------------------------------------

std::string toString(JNIEnv* env, jstring j_str) {
    //DO NOT RELEASE.
    const char * c_str = env->GetStringUTFChars(j_str, nullptr);
    std::string cpp_str = c_str;
    return cpp_str;
}

//-------------------------------------------------------------
// Native Methods
//-------------------------------------------------------------

namespace NModAPI {
//    void nativeSetDataDirectory(JNIEnv*env,jobject thiz,jstring directory) {
//        *mAddrAndroidAppDataPath = toString(env,directory);
//    }
    jstring nativeDemangle(JNIEnv*env,jobject thiz,jstring str) {
        char const* symbol_name = toString(env,str).c_str();
        if(symbol_name) {
            char const* ret = abi::__cxa_demangle(symbol_name,nullptr,nullptr,nullptr);
            return env->NewStringUTF(ret);
        }
        return env->NewStringUTF("");
    }
}

//-------------------------------------------------------------
// Register Natives
//-------------------------------------------------------------

extern "C" JNIEXPORT jboolean JNICALL Java_org_thelauncher_sculptlauncher_backend_launcher_util_NativeUtil_nativeRegisterNatives(JNIEnv*env, jobject thiz, jclass cls) {
    JNINativeMethod methods[] = {
        //{"nativeSetDataDirectory", "(Ljava/lang/String;)V", (void *)&NModAPI::nativeSetDataDirectory},
        {"nativeDemangle", "(Ljava/lang/String;)Ljava/lang/String;", (void *)&NModAPI::nativeDemangle}
    };
    
    if (env->RegisterNatives(cls,methods,sizeof(methods)/sizeof(JNINativeMethod)) < 0)
        return JNI_FALSE;
    return JNI_TRUE;
}

extern "C" JNIEXPORT void JNICALL Java_org_thelauncher_sculptlauncher_backend_launcher_util_LibraryLoader_nativeOnNModAPILoaded(JNIEnv*env, jobject thiz, jstring libPath) {
    const char* mNativeLibPath = toString(env,libPath).c_str();
    mMCPENativeLibPath = mNativeLibPath;
    void* imageMCPE = (void*)dlopen(mNativeLibPath,RTLD_LAZY);
    //mAddrAndroidAppDataPath = ((std::string*)dlsym(imageMCPE,"_ZN19AppPlatform_android20ANDROID_APPDATA_PATHE"));
    dlclose(imageMCPE);
}

//-------------------------------------------------------------
// On Load
//-------------------------------------------------------------

JNIEXPORT jint JNI_OnLoad(JavaVM*vm,void*) {
    mJvm=vm;
    return JNI_VERSION_1_6;
}

