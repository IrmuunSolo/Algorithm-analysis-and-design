#Minimum CMake version required
cmake_minimum_required(VERSION 3.25)

# Project name and language
project(Lab1 LANGUAGES CXX)

# Set C++ standard
set(CMAKE_CXX_STANDARD 20)

include_directories(include)

Include(FetchContent)

FetchContent_Declare(
    Catch2
    GIT_REPOSITORY https://github.com/catchorg/Catch2.git
    GIT_TAG v3.4.0
)

FetchContent_MakeAvailable(Catch2)

add_executable(main_exec src/main.cpp src/Reader.cpp include/Reader.h)
add_executable(tests tests/test.cpp src/Reader.cpp include/Reader.h)
target_link_libraries(tests PRIVATE Catch2::Catch2WithMain)

enable_testing()
include(CTest)
include(Catch)
catch_discover_tests(tests)
