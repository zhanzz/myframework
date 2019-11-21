# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#设置生成的so动态库最后输出的路径 PROJECT_SOURCE_DIR表示CMakeLists.txt所在的目录
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/jniLibs/${ANDROID_ABI})

# 查找文件系统中指定模式的路径，如/* 匹配根目录的文件（注意路径）
file(GLOB bzip_source ${CMAKE_SOURCE_DIR}/bzip/*.c)

add_library( # Sets the name of the library.
        bspatch

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        bspatch.c
        Patch.cpp
        ${bzip_source})

#测试引入第三方包start
#add_library( # Sets the name of the library.
#        my
#
#        # Sets the library as a shared library.
#        SHARED
#
#        # Provides a relative path to your source file(s).
#        source/my.cpp)

#add_library( # Sets the name of the library.
#        testlink
#
#        # Sets the library as a shared library.
#        SHARED
#
#        # Provides a relative path to your source file(s).
#        TestLinkSo.cpp)
#
#target_include_directories(testlink
#        PRIVATE
#        header)
#add_library(my SHARED IMPORTED)
#set_target_properties(my
#        PROPERTIES IMPORTED_LOCATION
#        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libmy.so) #这里需要绝对路径
#测试引入第三方包end

#路径相对于CMakeLists.txt
# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

#target_link_libraries( # Specifies the target library.
#        testlink
#
#        my
#        # Links the target library to the log library
#        # included in the NDK.
#        ${log-lib}) #将my和log连接入 testlink

target_link_libraries( # Specifies the target library.
        bspatch
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})