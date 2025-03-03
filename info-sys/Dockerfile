FROM  openjdk:17-jdk-alpine


COPY ./target/info-sys-0.0.1-SNAPSHOT.jar /usr/app/

EXPOSE 36002

ENTRYPOINT ["java","-jar","/usr/app/info-sys-0.0.1-SNAPSHOT.jar",""]