#ifndef VIDEOWEIGHTLOSS_FFMPEG_JNI_DEFINE_H
#define VIDEOWEIGHTLOSS_FFMPEG_JNI_DEFINE_H

#include <android/log.h>

#define FFMPEG_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_eatradish_videoweightloss_VideoWeightLoss_ ## FUNC_NAME \
    (JNIEnv *env, jclass thiz, ##__VA_ARGS__)\

#endif //VIDEOWEIGHTLOSS_FFMPEG_JNI_DEFINE_H
