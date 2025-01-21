# Base image
FROM openjdk:17

WORKDIR /app

# Add the application jar
COPY build/libs/*.jar app.jar

#Firebase 및 Google Cloud Vision 사용을 위해 필요한 JSON 파일을 컨테이너 내부로 복사
#COPY src/main/resources/firebase/goldaengtime-firebase-adminsdk.json /app/firebase/goldaengtime-firebase-adminsdk.json

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
