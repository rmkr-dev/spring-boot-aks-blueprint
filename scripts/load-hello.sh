#!/usr/bin/env bash
# Simple sequential load smoke against GET /api/v1/hello (no Node).
# Usage: ./scripts/load-hello.sh [base_url] [requests]
# Example: ./scripts/load-hello.sh http://127.0.0.1:8080 50
set -euo pipefail

BASE_URL="${1:-http://127.0.0.1:8080}"
REQUESTS="${2:-20}"
PATH_HELLO="/api/v1/hello"
OK=0
FAIL=0

echo "load-hello: ${REQUESTS} GETs against ${BASE_URL}${PATH_HELLO}"
START=$(date +%s)
for i in $(seq 1 "${REQUESTS}"); do
  code=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}${PATH_HELLO}?name=load-${i}" || echo "000")
  if [[ "${code}" == "200" ]]; then
    OK=$((OK + 1))
  else
    FAIL=$((FAIL + 1))
    echo "request ${i}: HTTP ${code}" >&2
  fi
done
END=$(date +%s)
echo "ok=${OK} fail=${FAIL} seconds=$((END - START))"
[[ "${FAIL}" -eq 0 ]]
