cmake_minimum_required(VERSION 3.18.1)
project(Camellia)
add_library(camellia-protect SHARED
            camellia.cpp
            crypto/Bridge.cpp
            utils/Util.h)
          



