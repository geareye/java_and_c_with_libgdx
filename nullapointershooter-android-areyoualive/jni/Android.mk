LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

# OpenCV
#OPENCV_CAMERA_MODULES:=on
#OPENCV_INSTALL_MODULES:=on
#OPENCV_LIB_TYPE := SHARED

#NDK_DEBUG:=1
#APP_OPTIM:=debug

#include $(LOCAL_PATH)/../../OpenCV-android-sdk/sdk/native/jni/OpenCV.mk


#for complex arithmetic
#LOCAL_C_INCLUDES := C:\Users\gyalcin\Downloads\android-ndk-r10e\sources\cxx-stl\gnu-libstdc++\gnu-libstdc++\4.8\include
#LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../Classes
LOCAL_LDLIBS := -llog

LOCAL_SRC_FILES := com_simpleprogrammer_nullapointershooter_FibLib.c
LOCAL_MODULE := com_simpleprogrammer_nullapointershooter_FibLib
include $(BUILD_SHARED_LIBRARY)

# Add prebuilt libgdx
include $(CLEAR_VARS)

LOCAL_MODULE := libgdx
LOCAL_SRC_FILES := libgdx.so

include $(PREBUILT_SHARED_LIBRARY)

# Add prebuilt libocr
include $(CLEAR_VARS)

LOCAL_MODULE := libandroidgl20
LOCAL_SRC_FILES := libandroidgl20.so

include $(PREBUILT_SHARED_LIBRARY)