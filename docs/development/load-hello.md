# Load smoke for `/api/v1/hello`

Optional sequential smoke scripts (no Node):

| Script | Runtime |
| --- | --- |
| [`scripts/load-hello.sh`](../../scripts/load-hello.sh) | bash + curl |
| [`scripts/load-hello.py`](../../scripts/load-hello.py) | Python 3 stdlib |

```bash
# start the app first (mvn spring-boot:run, compose, or java -jar)
./scripts/load-hello.sh http://127.0.0.1:8080 50
python3 scripts/load-hello.py http://127.0.0.1:8080 50
```

These are local convenience checks—not a CI gate and not a claim of production capacity.
