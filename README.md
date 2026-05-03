
# 只构建 Java 包
./package.sh

# 构建包 + Docker（自动检测架构，version=local）
./package.sh --docker

# 构建包 + 指定架构和版本
./package.sh --docker=x86_64 --version=1.2.3
./package.sh --docker=arm64  --version=1.2.3



docker load -i docker-alone-arm64-3.0.7.tar
docker compose -f docker-alone-arm64-3.0.7.yml up -d


企业版本地 MySQL

docker run -d --name main-mysql-8.0.22 -e MYSQL_DATABASE=console -e MYSQL_USER=origin -e MYSQL_PASSWORD=123258 -e MYSQL_ROOT_PASSWORD=123258 -e TZ=Asia/Shanghai -p 3306:3306 mysql:8.0.22 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --default-time_zone='+8:00'
