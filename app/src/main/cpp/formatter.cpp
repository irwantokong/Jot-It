//
// Created by ASUS on 1/1/2021.
//

#include <jni.h>
#include <stdlib.h>
#include <string>
#include <time.h>
using namespace std;

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_irwanto_jotit_ui_EditReminderFragment_getFormattedRemainingTime(
        JNIEnv *env, jobject thiz, jlong remaining_time, jstring hours, jstring minutes,
        jstring seconds) {
    const string h = env->GetStringUTFChars(hours, 0);
    const string m = env->GetStringUTFChars(minutes, 0);
    const string s = env->GetStringUTFChars(seconds, 0);

    long sec = (long) remaining_time / 1000;
    long min = (long) (remaining_time / 1000) / 60;
    long hour = (long) (remaining_time / 1000) / 3600;
    sec = sec % 60;
    min = min % 60;

    string secs_string = to_string(sec);
    secs_string = string(2 - secs_string.length(), '0') + secs_string;
    string mins_string = to_string(min);
    mins_string = string(2 - mins_string.length(), '0') + mins_string;
    string hours_string = to_string(hour);
    hours_string = string(2 - hours_string.length(), '0') + hours_string;
    string combined = hours_string + " " + h + " " + mins_string + " " + m + " " + secs_string + " " + s;
    char const *formatted = combined.c_str();
    return (*env).NewStringUTF(formatted);
}