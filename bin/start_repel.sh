#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
CLASSPATH_FILE="${PROJECT_DIR}/target/classpath.txt"
ENV_FILE="${PROJECT_DIR}/.env"

cd "${PROJECT_DIR}"

if [[ -f "${ENV_FILE}" ]]; then
  set -a
  # shellcheck disable=SC1090
  source "${ENV_FILE}"
  set +a
fi

if ! command -v jruby >/dev/null 2>&1; then
  echo "jruby was not found on PATH." >&2
  exit 1
fi

export MAVEN_OPTS="${MAVEN_OPTS:-} --enable-final-field-mutation=ALL-UNNAMED"

./mvnw \
  -q \
  -DskipTests \
  compile \
  dependency:build-classpath \
  -Dmdep.includeScope=runtime \
  -Dmdep.outputFile="${CLASSPATH_FILE}"

PROJECT_CLASSPATH="${PROJECT_DIR}/target/classes"
DEPENDENCY_CLASSPATH="$(cat "${CLASSPATH_FILE}")"

if [[ -n "${DEPENDENCY_CLASSPATH}" ]]; then
  PROJECT_CLASSPATH="${PROJECT_CLASSPATH}:${DEPENDENCY_CLASSPATH}"
fi

export CLASSPATH="${PROJECT_CLASSPATH}${CLASSPATH:+:${CLASSPATH}}"

exec jruby -r java -S irb
