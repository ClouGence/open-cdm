#! /bin/bash

case "$#" in
0)
  DEPLOY_SITE="ON_PREMISE_china_dm"
  ;;
1)
  DEPLOY_SITE=$1
  ;;
esac

echo "input deploySite is ${DEPLOY_SITE}"

mvn clean install -P${DEPLOY_SITE}
