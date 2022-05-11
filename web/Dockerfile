FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
LABEL version="1.0"

ENV APP_DIR=/web_part
ENV APP_NAME=web_module.jar

RUN mkdir $APP_DIR
COPY web/target/$APP_NAME $APP_DIR

WORKDIR $APP_DIR

CMD java -jar $APP_NAME