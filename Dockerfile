FROM  openjdk:17-jdk-alpine


COPY ./target/info-sys-1.0-SNAPSHOT.jar /usr/app/

EXPOSE 36002

ENTRYPOINT ["java","-jar","/usr/app/info-sys-1.0-SNAPSHOT.jar",""]