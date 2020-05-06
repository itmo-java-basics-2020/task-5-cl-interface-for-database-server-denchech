package ru.andrey.kvstorage.console;

import ru.andrey.kvstorage.console.DatabaseCommandResult.SimpleDatabaseCommandResult;
import ru.andrey.kvstorage.exception.DatabaseException;

public class UpdateKeyCommand implements DatabaseCommand {

    private final ExecutionEnvironment env;
    private final String databaseName;
    private final String tableName;
    private final String key;
    private final String value;

    public UpdateKeyCommand(ExecutionEnvironment env,
                            String databaseName,
                            String tableName,
                            String key,
                            String value) {
        this.env = env;
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.key = key;
        this.value = value;
    }

    @Override
    public DatabaseCommandResult execute() {
        return env.getDatabase(databaseName).map(database -> {
            try {
                database.write(tableName, key, value);
                return SimpleDatabaseCommandResult.success(
                        String.format("Key \"%s\" has value \"%s\" in table \"%s\" in database \"%s\"",
                                key,
                                value,
                                tableName,
                                databaseName)
                );
            } catch (DatabaseException ex) {
                return SimpleDatabaseCommandResult.error(ex.getMessage());
            }
        }).orElseGet(() ->
                SimpleDatabaseCommandResult.error(
                        String.format("Database with name \"%s\" doesn't exist", databaseName)
                )
        );
    }
}