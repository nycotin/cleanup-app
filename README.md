# cleanup-app

### Creating the database in Docker

- Make sure Docker is running on your computer.
- Run ``docker compose up -d``
- Check that the container is running with ``docker ps``

The database will be populated with some users and tasks.

### Running the application

- Open `src.main.java.com.app.cleanup.CleanupApplication`
- Click the `Play` icon at the top-right side of the IntelliJ

### Creating a .WAR build

- Run `./mvnw clean package`

### Running tests

- Open `src.test.java.com.app.cleanup.controllers.CleanupApplicationTests`
- Click the `Play` icon at the top-right side of the IntelliJ