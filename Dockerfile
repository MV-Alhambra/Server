FROM openjdk:11-jre-slim-stretch
ENV port=9876
COPY . /app
WORKDIR /app
RUN ./gradlew shadowJar
CMD ./gradlew runShadow
