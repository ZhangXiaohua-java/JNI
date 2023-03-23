# JNI

## JNI入门

### JNI简介

```text
JNI（Java Native Interface）是Java平台提供的一种编程接口，
用于在Java应用程序中调用本地代码（如C或C++代码）或将Java代码嵌入到本地代码中。

JNI使Java应用程序能够与其他编程语言编写的本地代码交互，并允许Java应用程序利用本地代码的性能和功能。

通过JNI，Java程序员可以定义Java类和本地函数之间的接口，使得Java应用程序能够调用本地函数并处理其返回值。
在本地函数中，可以使用JNI提供的函数访问Java对象、调用Java方法等操作。

同时，JNI还提供了一些功能，如动态链接本地库、类型转换和异常处理等。
需要注意的是，由于JNI涉及到本地代码的编写和调用，因此需要一些额外的注意事项，
如确保本地代码的安全性、跨平台兼容性和性能优化等。

```

### JNI的简单使用

```text
由于我使用的是JDK17，在JDK10及之后的版本中已经移除了javah命令，将其整合到了javac命令中了

编译命令
进入到Java源文件所在的目录,在终端执行以下命令:

javac -h jni -encoding UTF-8 ./*
执行完这条命令之后,在当前目录下就会多出一个jni目录,并且该目录中会多出一个头文件,
将该头文件和jni.h 以及jni_md.h头文件一并拷贝到C工程中,生成动态库

```

### demo
```c
头文件:
#ifndef UNTITLED1_LIBRARY_H
#define UNTITLED1_LIBRARY_H

char* date() ;

void writeFile(char *filename, char *content);

#endif 



C源文件:

# include <stdio.h>
# include <stdlib.h>
# include <time.h>
# include <string.h>
# include "element_io_base_NativeFunction.h"
# include "library.h"


void writeFile(char *filename, char *content) {
    if (filename == NULL) {
        exit(-1);
    }
    FILE * file = fopen(filename, "a+");
    fputs(content, file);
    fclose(file);
    file = NULL;
}

char* date() {
    char *timeStr = "";
    time_t now;
    now = time(NULL);
    struct tm *tm = localtime(&now);
    char *temp = malloc(20);
    sprintf(temp, "%d-", tm->tm_year + 1900);
    strcat(timeStr, temp);
    sprintf(temp, "%d-", tm->tm_mon + 1);
    strcat(timeStr, temp);
    sprintf(temp, "%d ", tm->tm_mday);
    strcat(timeStr, temp);
    sprintf(temp, "%d:", tm->tm_hour);
    strcat(timeStr, temp);
    sprintf(temp, "%d:", tm->tm_min);
    strcat(timeStr, temp);
    sprintf(temp, "%d", tm->tm_sec);
    strcat(timeStr, temp);
    printf("%s", timeStr);
    strcat(timeStr, "\n");
    char *filename = "D:\\appsdata\\test\\test.txt";
    writeFile(filename, timeStr);
    return timeStr;
}


JNIEXPORT jstring JNICALL Java_element_io_base_NativeFunction_date (JNIEnv * env, jobject obj){
    char *str = date();
    jclass cla = (**env).FindClass(env,"java/lang/String");
    jmethodID methodId = (*env)->GetMethodID(env,cla,"<init>","([BLjava/lang/String;)V");
    jbyteArray byteArr = (**env).NewByteArray(env,strlen(str));
    (**env).SetCharArrayRegion(env,byteArr, 0,strlen(str),str);
    jstring  encoding = (**env).NewStringUTF(env,"UTF-8");
    return (**env).NewObject(env,cla,methodId,byteArr,encoding);
}



int main() {
    char *str = date();
    printf("\r\n%s",str);
}

Java程序在执行前必须要先加载动态库
System.load(库文件),剩下的就和调用Java方法没有什么区别了.

```