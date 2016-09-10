LOCAL_PATH :=$(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE :libnativetest
LOCAL_SRC_FILES := com_taoy3_ndkdemo_NativeTest.c
#LOCAL_SHARED_LIBRARIES :libcutils

include $(BUILD_SHARED_LIBRARY)