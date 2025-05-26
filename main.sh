#!/bin/bash

# description:
# mega drive compiler - project build script
#
# builds compiler classes from java files
# deletes the previous build if it exist
#
# can clean build
# can run compiled classes
#
# usage:
# ./main.sh --help     prints help screen
# ./main.sh --build    build project files [default option]
# ./main.sh --clean    delete all files from build directory
# ./main.sh --run      run compiler
# ./main.sh --format   format source files

# ==============================================================================
# variables
#
# ==============================================================================
code_compiler=/home/zephyr/soft/jdk-21.0.7+6/bin/javac
code_vm=/home/zephyr/soft/jdk-21.0.7+6/bin/java
code_formatter=/home/zephyr/soft/google-java-format_linux-x86-64

work_dir=$(pwd)
src_dir=src
build_dir=build
mdcl_dir=mdcl # source files folder for md compiler

main_file=MegaDriveCompiler.java
main_class=MegaDriveCompiler
mdcl_file=file.mdcl # default source file for md compiler

# ==============================================================================
# code
#
# ==============================================================================
echo "mega drive compiler - project build script"
echo


# --------------------------------------
# print help screen
# --------------------------------------
if test "$1" = "--help"; then
    echo "usage: main.sh [options]..."
    echo "build, clean, run project files"
    echo
    echo "--build    build project files [default option]"
    echo "--clean    delete all files from build directory"
    echo "--run      run compiler"
    echo "--format   format source files"
    echo

    exit 0
fi


# --------------------------------------
# clean project
# --------------------------------------
if test "$1" = "--clean"; then
    echo "clean project: start"

    cd $build_dir || exit 1
    rm -frv *
    cd $work_dir

    echo "clean project: done"
    exit 0
fi

# --------------------------------------
# run project
# --------------------------------------
if test "$1" = "--run"; then
    echo "run project: start"

    $code_vm -cp $build_dir compiler.$main_class $mdcl_dir/$mdcl_file

    echo "run project: done"
    exit 0
fi

# --------------------------------------
# format source files
# --------------------------------------
if test "$1" = "--format"; then
    echo "formatting source files: start"

    $code_formatter -r $src_dir/compiler/*

    echo "formatting source files: done"
    exit 0
fi

# --------------------------------------
# build project
# --------------------------------------
echo "build project: start"

echo "compiling the code ..."
$code_compiler -d $build_dir -cp $src_dir $src_dir/compiler/$main_file

echo "build project: done"
exit 0
