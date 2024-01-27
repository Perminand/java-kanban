package kanban.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatterWrite = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(localDateTime.format(formatterWrite));
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}
