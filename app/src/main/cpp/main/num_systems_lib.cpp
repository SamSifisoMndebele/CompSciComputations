#include <jni.h>

//
// Created by Sam on 2024/05/09.
//

extern "C"
JNIEXPORT jobject JNICALL
Java_com_compscicomputations_logic_NumSystemsLib_fromDecimal(JNIEnv *env, jobject thiz,
                                                             jstring decimal_str) {




//    jobject bb = env->NewDirectByteBuffer((void *) "cStringValue", strlen("cStringValue"));
//
//    jclass cls_Charset = env->FindClass("java/nio/charset/Charset");
//    jmethodID mid_Charset_forName = env->GetStaticMethodID(cls_Charset, "forName", "(Ljava/lang/String;)Ljava/nio/charset/Charset;");
//    jobject charset = env->CallStaticObjectMethod(cls_Charset, mid_Charset_forName, env->NewStringUTF("UTF-8"));
//
//    jmethodID mid_Charset_decode = env->GetMethodID(cls_Charset, "decode", "(Ljava/nio/ByteBuffer;)Ljava/nio/CharBuffer;");
//    jobject cb = env->CallObjectMethod(charset, mid_Charset_decode, bb);
//
//    jclass cls_CharBuffer = env->FindClass("java/nio/CharBuffer");
//    jmethodID mid_CharBuffer_toString = env->GetMethodID(cls_CharBuffer, "toString", "()Ljava/lang/String;");
//    jstring str = env->CallObjectMethod(cb, mid_CharBuffer_toString);
//
//    env->SetObjectField(jPosRec, myJniPosRec->_myJavaStringValue, str);





    /*const char *str = env->GetStringUTFChars(decimal_str, 0);
    base_conversion conversion = base_convert::from_decimal(str);
    env->ReleaseStringUTFChars(decimal_str, str);

    jclass cls = env->FindClass("com/compscicomputations/logic/BaseConversion");
    jmethodID constructor = env->GetMethodID(cls, "<init>",
                                             "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;"
                                             "Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V");
    jvalue args[7];
    args[0].l = env->NewStringUTF(conversion.decimal.c_str());
    args[1].l = env->NewStringUTF(conversion.binary.c_str());
    args[2].l = env->NewStringUTF(conversion.octal.c_str());
    args[3].l = env->NewStringUTF(conversion.hexadecimal.c_str());
    args[4].l = env->NewStringUTF("conversion.ascii.c_str()");
    args[5].z = conversion.error;
    args[6].l = env->NewStringUTF(conversion.error_message.c_str());*/

//    return env->NewObjectA(cls, constructor, args);
}