FROM  openjdk:17-jdk-alpine


COPY info-sys-backend/target/info-sys-backend-0.0.1-SNAPSHOT.jar /usr/app/

EXPOSE 36002

ENTRYPOINT ["java","-jar","/usr/app/info-sys-backend-0.0.1-SNAPSHOT.jar",""]