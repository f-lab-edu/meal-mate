# Base Image: AWS 공식 Java 21
FROM amazoncorretto:21

# 빌드 타임에 버전 주입 가능
ARG APM_AGENT_VERSION=1.45.0
ARG APP_VERSION=latest

WORKDIR /app

# APM 에이전트 경로
ENV APM_AGENT_PATH=/elastic-apm-agent.jar

# Elastic APM Agent 다운로드
RUN curl -L -o ${APM_AGENT_PATH} https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/${APM_AGENT_VERSION}/elastic-apm-agent-${APM_AGENT_VERSION}.jar

# Spring Boot JAR 복사
COPY build/libs/meal-mate-${APP_VERSION}.jar app.jar

# 컨테이너 실행 시 APM Agent 포함하여 실행
ENTRYPOINT ["sh", "-c", "\
  java \
  -javaagent:/elastic-apm-agent.jar \
  -Delastic.apm.server_urls=${APM_SERVER_URL} \
  -Delastic.apm.service_name=${APM_SERVICE_NAME} \
  -Delastic.apm.application_packages=${APM_APP_PACKAGES} \
  -jar app.jar"]
