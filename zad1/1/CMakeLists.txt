cmake_minimum_required (VERSION 2.6)
project (PiClient)

add_executable(client client.c)

# set the path to the library folder
link_directories(/usr/local/lib)

# link the libraries to the executable
target_link_libraries (client m)
set(CMAKE_BUILD_TYPE Debug)
