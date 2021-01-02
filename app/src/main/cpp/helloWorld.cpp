//
// Created by ASUS on 1/1/2021.
//

#include <jni.h>
#include <stdlib.h>
#include <time.h>

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_irwanto_jotit_ui_EditReminderFragment_hello(
        JNIEnv *env, jobject thiz) {
    char hello[] = "Hello";
    return (*env).NewStringUTF(hello);
}