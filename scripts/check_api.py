"""Regression: a database-backed sensor request must return the existing JSON contract."""
import json
import sys
import urllib.error
import urllib.request

base = sys.argv[1] if len(sys.argv) > 1 else 'http://localhost:8080'
try:
    with urllib.request.urlopen(base + '/api/sensors/1/value', timeout=40) as response:
        assert response.status == 200
        value = json.load(response)
    assert isinstance(value['value'], (int, float))
    assert value['sensorUUID'] and value['zdt']
except (urllib.error.URLError, AssertionError, KeyError) as error:
    print(f'FAIL: database-backed sensor request: {error}')
    sys.exit(1)
print('PASS: sensor value, UUID and timestamp returned through the database-backed API')
