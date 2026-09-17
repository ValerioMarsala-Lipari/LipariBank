#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script
# ----------------------------------------------------------------------------

set -e

MAVEN_WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"

if [ -r "$MAVEN_WRAPPER_PROPERTIES" ]; then
  DISTRIBUTION_URL=$(grep -E '^distributionUrl=' "$MAVEN_WRAPPER_PROPERTIES" | sed 's/distributionUrl=//')
fi

MAVEN_USER_HOME="${MAVEN_USER_HOME:-${HOME}/.m2}"
MAVEN_WRAPPER_HOME="${MAVEN_USER_HOME}/wrapper"

DISTRIBUTION_NAME=$(basename "$DISTRIBUTION_URL")
DISTRIBUTION_DIR="${MAVEN_WRAPPER_HOME}/dists/${DISTRIBUTION_NAME%%-bin.zip}"

if [ -d "${DISTRIBUTION_DIR}" ]; then
  MAVEN_HOME=$(find "${DISTRIBUTION_DIR}" -maxdepth 2 -name "mvn" -type f | head -1 | xargs -I{} dirname {} | xargs -I{} dirname {})
else
  echo "Downloading Maven from ${DISTRIBUTION_URL}"
  mkdir -p "${DISTRIBUTION_DIR}"
  TMP_DOWNLOAD="${DISTRIBUTION_DIR}/download.zip"
  if command -v curl > /dev/null; then
    curl -fsSL -o "${TMP_DOWNLOAD}" "${DISTRIBUTION_URL}"
  elif command -v wget > /dev/null; then
    wget -q -O "${TMP_DOWNLOAD}" "${DISTRIBUTION_URL}"
  else
    echo "ERROR: Neither curl nor wget is available. Install Maven manually." >&2
    exit 1
  fi
  unzip -q "${TMP_DOWNLOAD}" -d "${DISTRIBUTION_DIR}"
  rm "${TMP_DOWNLOAD}"
  MAVEN_HOME=$(find "${DISTRIBUTION_DIR}" -maxdepth 2 -name "mvn" -type f | head -1 | xargs -I{} dirname {} | xargs -I{} dirname {})
fi

exec "${MAVEN_HOME}/bin/mvn" "$@"
