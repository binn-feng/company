# 基础镜像使用 JDK 1.8
FROM openjdk:8-jre

# 作者
MAINTAINER NL

# 创建非 root 用户和组
RUN groupadd -r myuser && useradd -r -g myuser myuser

# VOLUME 指定临时文件目录为 /tmp，在主机 /var/lib/docker 目录下创建了一个临时文件并链接到容器的 /tmp
VOLUME /tmp

# 定义构建参数，指定 JAR 文件路径
ARG JAR_FILE=target/internal-admin.jar

# 将 JAR 包复制到容器中并更名
COPY ${JAR_FILE} /app.jar

# 将文件所有权更改为非 root 用户
RUN chown myuser:myuser /app.jar

# 切换到非 root 用户
USER myuser

# 为了缩短 Tomcat 启动时间，添加一个系统属性指向 “/dev/./urandom” 作为 Entropy Source
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]

# 暴露 8080 端口
EXPOSE 8080