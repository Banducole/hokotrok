#!/bin/bash
# Build script - compiles all Java sources to ./out/

set -e
PROJ_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJ_DIR"

mkdir -p out
echo "Compiling..."
javac -d out src/*.java
echo "Build done. Class files in: $PROJ_DIR/out"
