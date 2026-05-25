#!/bin/bash
# Test evaluation script - runs all 21 test cases and grep-checks expected patterns
PROJ_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJ_DIR"

PASS=0
FAIL=0
RESULTS=()

run_test() {
    local id="$1"
    local input="$2"
    shift 2
    local out_file="$PROJ_DIR/tesztek/${id}.out"
    cat "$input" | java -cp out Proto > "$out_file" 2>&1
    local exit_code=$?

    local ok=true
    local missing=""
    if [ $exit_code -ne 0 ]; then
        ok=false
        missing="\n     EXIT CODE: $exit_code"
    fi
    if grep -qF "[ERROR]" "$out_file"; then
        ok=false
        missing="$missing\n     FOUND [ERROR] in output"
    fi
    for pattern in "$@"; do
        if ! grep -qE "$pattern" "$out_file"; then
            ok=false
            missing="$missing\n     MISSING: $pattern"
        fi
    done

    if $ok; then
        echo "[ OK ] $id"
        ((PASS++))
        RESULTS+=("OK   $id")
    else
        echo "[FAIL] $id"
        echo -e "$missing"
        ((FAIL++))
        RESULTS+=("FAIL $id")
    fi
}

KNOWN_TESTS=(t01 t02 t03 t04 t05 t06 t07 t08 t09 t10 t11 t12 t13 t14 t15 t16 t17 t18 t19 t20 t21)

TOTAL=$(ls "$PROJ_DIR/tesztek/"*.txt 2>/dev/null | wc -l)
echo "=== Running $TOTAL tests ==="

run_test "t01" "tesztek/t01.txt" \
    "auto1.*Sikeres lepes" \
    "Car.*auto1.*Position=s1" \
    "Blocked=false"

run_test "t02" "tesztek/t02.txt" \
    "auto1.*(Utkozes|megcsuszott)" \
    "Car.*auto1.*Blocked=true" \
    "Bus.*busz1.*Blocked=true"

run_test "t03" "tesztek/t03.txt" \
    "hk1.*takarit" \
    "s1.*allapota ClearState" \
    "Lane.*s1.*State=ClearState"

run_test "t04" "tesztek/t04.txt" \
    "hk1.*Zuzalek szorasa sikeres" \
    "Lane.*s1.*State=IcyState.*Slippery=false" \
    "SnowPlow.*hk1.*Fuel_Rock=4"

run_test "t05" "tesztek/t05.txt" \
    "s1.*allapota IcyState" \
    "Lane.*s1.*State=IcyState"

run_test "t06" "tesztek/t06.txt" \
    "busz1.*Sikeres lepes" \
    "Bus.*busz1.*Position="

run_test "t07" "tesztek/t07.txt" \
    "Lane.*s1.*State=ClearState" \
    "Lane.*s2.*State=ThinSnowState"

run_test "t08" "tesztek/t08.txt" \
    "s1.*allapota BrokenIceState" \
    "Lane.*s1.*State=BrokenIceState"

run_test "t09" "tesztek/t09.txt" \
    "(s1.*sozva|olvadasi folyamat|so hatas)" \
    "SnowPlow.*hk1.*Fuel_Salt=4"

run_test "t10" "tesztek/t10.txt" \
    "Lane.*s1.*State=ClearState" \
    "SnowPlow.*hk1.*Fuel_Kerosene=9"

run_test "t11" "tesztek/t11.txt" \
    "(kikerul|Savot valtott)" \
    "Car.*auto1.*Position=s2"

run_test "t12" "tesztek/t12.txt" \
    "(so azonnal felolvasztotta|hot felolvasztotta)" \
    "Lane.*s1.*State=ClearState"

run_test "t13" "tesztek/t13.txt" \
    "Lane.*s1.*State=ClearState" \
    "Lane.*s2.*Rocky=true"

run_test "t14" "tesztek/t14.txt" \
    "SnowPlow.*hk1.*Fuel_Rock=5"

run_test "t15" "tesztek/t15.txt" \
    "Lane.*s1.*Rocky=false"

run_test "t16" "tesztek/t16.txt" \
    "(Nincs ut|Elakadt|nem tud lepni)" \
    "Car.*auto2.*Position=s1"

run_test "t17" "tesztek/t17.txt" \
    "Lane.*s1.*State=ThickSnowState" \
    "Lane.*s1.*IsPassable=false"

run_test "t18" "tesztek/t18.txt" \
    "vasarlas sikeres" \
    "CleanerPlayer.*cp1.*Balance=500" \
    "SnowPlow.*hk2.*Position=s2"

run_test "t19" "tesztek/t19.txt" \
    "CleanerPlayer.*cp1.*Balance=300" \
    "SnowPlow.*hk1.*Head=ThrowHead"

run_test "t20" "tesztek/t20.txt" \
    "CleanerPlayer.*cp1.*Balance=0"

run_test "t21" "tesztek/t21.txt" \
    "auto1.*blokkolt" \
    "Car.*auto1.*BlockedTurns=1" \
    "Car.*auto1.*Position=s1"

for txt_file in "$PROJ_DIR/tesztek/"*.txt; do
    id=$(basename "$txt_file" .txt)
    if [[ ! " ${KNOWN_TESTS[*]} " =~ " ${id} " ]]; then
        run_test "$id" "$txt_file"
    fi
done

echo ""
echo "=== Summary: $PASS passed, $FAIL failed ==="
