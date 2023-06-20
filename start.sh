docker rm application
docker rm company-management_postgres_1
docker image rm company-management_application

./gradlew clean build
docker-compose up