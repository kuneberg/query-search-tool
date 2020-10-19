package org.kuneberg.qst;

import org.jooq.impl.JooqQueryTypeChecker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuerySearchTool {


    public static void main(String[] args) throws IOException {
        JooqQueryTypeChecker checker = new JooqQueryTypeChecker();
        IbatisXmlParser parser = new IbatisXmlParser();

        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));

        FilesCollector visitor = new FilesCollector();

        Files.walkFileTree(Paths.get(args[0]), visitor);
        visitor.getPaths()
                .stream()
                .forEach(p -> {
                    InputStream stream = pathToStream(p);
                    Map<String, String> queries = parser.getQueries(stream);
                    List<String> filteredQueryIds = queries.entrySet()
                            .stream()
                            .filter(entry -> checker.isUpdateFromQuery(entry.getValue()))
                            .map(entry -> entry.getKey())
                            .collect(Collectors.toList());

                    if (!filteredQueryIds.isEmpty()) {
                        try {
                            writer.write("file: " + p.toAbsolutePath().toString());
                            writer.write(filteredQueryIds.stream().collect(Collectors.joining("\n\t", "\n\t", "\n")));
                        } catch (Exception e) {

                        }
                    }

                });

        writer.flush();
        writer.close();
    }

    private static InputStream pathToStream(Path p) {
        try {
            return Files.newInputStream(p);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
