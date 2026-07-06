#!/bin/bash

# bash strict mode
# -e: exit immediately if a command fails
# -u: exit on use of an unset variable
# -o pipefail: fail a pipeline if any command in it fails
set -euo pipefail


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
# project directory
script_dir="$(dirname -- "${BASH_SOURCE[0]}")"
project_dir="$(cd -- "$script_dir" && pwd)"

# external tools
java_home="${JAVA_HOME:-$project_dir/tools/jdk}"
code_compiler="$java_home/bin/javac"
code_vm="$java_home/bin/java"

code_formatter="${CODE_FORMATTER:-$project_dir/tool/google-java-format_linux-x86-64}"

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
# check tools
# --------------------------------------
if [[ ! -x "$code_compiler" ]]; then
    echo "compiler not found or not executable: $code_compiler" >&2

    exit 1
fi

if [[ ! -x "$code_vm" ]]; then
    echo "java vm not found or not executable: $code_vm" >&2

    exit 1
fi

if [[ ! -x "$code_formatter" ]]; then
    echo "formatter not found or not executable: $code_formatter" >&2

    exit 1
fi

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

    echo "source file: "$mdcl_file
    nl -b a -n rz  $mdcl_dir/$mdcl_file # print source file with line numbers
    $code_vm -cp $build_dir compiler.$main_class $mdcl_dir/$mdcl_file

    echo "run project: done"
    exit 0
fi

# --------------------------------------
# format source files
# --------------------------------------
if test "$1" = "--format"; then
    echo "formatting source files: start"

    $code_formatter -r $src_dir/compiler/*.java
    $code_formatter -r $src_dir/compiler/*/*.java

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
