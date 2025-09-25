FROM mysql:8.0

# Set environment variables
ENV MYSQL_ROOT_PASSWORD=1234
ENV MYSQL_DATABASE=cleanup-app
ENV MYSQL_USER=admin
ENV MYSQL_PASSWORD=1234

# Copy initialization scripts
COPY ./init-scripts/ /docker-entrypoint-initdb.d/

# Set the default authentication plugin
CMD ["mysqld", "--default-authentication-plugin=mysql_native_password"]