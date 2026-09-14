#!/usr/bin/env python3
"""Simple sequential load smoke against GET /api/v1/hello (stdlib only, no Node)."""
from __future__ import annotations

import argparse
import sys
import time
import urllib.error
import urllib.request


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("base_url", nargs="?", default="http://127.0.0.1:8080")
    parser.add_argument("requests", nargs="?", type=int, default=20)
    args = parser.parse_args()

    ok = 0
    fail = 0
    start = time.perf_counter()
    for i in range(1, args.requests + 1):
        url = f"{args.base_url.rstrip('/')}/api/v1/hello?name=load-{i}"
        try:
            with urllib.request.urlopen(url, timeout=5) as resp:
                if 200 <= resp.status < 300:
                    ok += 1
                else:
                    fail += 1
                    print(f"request {i}: HTTP {resp.status}", file=sys.stderr)
        except (urllib.error.URLError, TimeoutError) as exc:
            fail += 1
            print(f"request {i}: {exc}", file=sys.stderr)
    elapsed = time.perf_counter() - start
    print(f"ok={ok} fail={fail} seconds={elapsed:.2f}")
    return 0 if fail == 0 else 1


if __name__ == "__main__":
    raise SystemExit(main())
