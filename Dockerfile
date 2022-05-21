FROM gradle:7-jdk8 as builder
WORKDIR /tmp/build
ADD . ./
RUN gradle shadowJar

FROM openjdk:8-jre-slim
WORKDIR /var/bot
COPY --from=builder /tmp/build/build/libs/bot.jar bot.jar
CMD java -jar bot.jar
