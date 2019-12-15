FROM openjdk:8-jre-alpine
ENV VERTICLE_FILE micro-trader-dashboard-1.0-SNAPSHOT.jar
ENV VERTICLE_HOME /usr/verticles
ENV ENV_CLUSTER -cluster
ENV ENV_METRICS -Dvertx.metrics.options.enabled=true -Dvertx.metrics.options.registryName=vertx-dw
EXPOSE 8090
EXPOSE 5701
COPY $VERTICLE_FILE $VERTICLE_HOME/
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE $ENV_CLUSTER $ENV_METRICS"]

