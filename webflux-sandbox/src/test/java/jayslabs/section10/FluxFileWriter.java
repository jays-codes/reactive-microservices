package jayslabs.section10;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// enables writing to a file as a Flux<String> publisher is being processed
public class FluxFileWriter {

    private final Path path;
    private BufferedWriter writer;

    private FluxFileWriter(Path path) {
        this.path = path;
    }

    private void createFile() {
        try {
            this.writer = Files.newBufferedWriter(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeFile() {
        try {
            this.writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // just for demo
    private void write(String content) {
        try {
            this.writer.write(content);
            this.writer.newLine();
            this.writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Mono<Void> create(Flux<String> flux, Path path) {
        var writer = new FluxFileWriter(path);
        return flux.doOnNext(writer::write)
                   .doFirst(writer::createFile)
                   .doFinally(s -> writer.closeFile())
                   .then();
    }

}
