#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_rkzmn_securesecrets_security_Secrets_getAPIKey(JNIEnv* env, jobject /* this */) {
    std::string api_key = "AWESOME_API_KEY";
    return env->NewStringUTF(api_key.c_str());
}