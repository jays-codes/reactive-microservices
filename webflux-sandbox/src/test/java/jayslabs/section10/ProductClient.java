package jayslabs.section10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import jayslabs.section10.dto.ProductDTO;
import jayslabs.section10.dto.UploadResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {
    private final WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();


    public Mono<UploadResponseDTO> uploadProducts(Flux<ProductDTO> productDTOflux) {
        return this.client.post().uri("/products/upload")
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(productDTOflux, ProductDTO.class)
            .retrieve()
            .bodyToMono(UploadResponseDTO.class);
    }

    public Flux<ProductDTO> downloadProducts() {
        return this.client.get().uri("/products/download")
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(ProductDTO.class);
    }

    public Mono<Void> downloadProductsToFile() {
        Path filePath = Path.of("products.txt");
        // Delete file if it exists to avoid appending to old data
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete existing products.txt", e);
        }

        return Mono.using(
            () -> Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE),
            writer -> this.client.get().uri("/products/download")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .doOnNext(product -> {
                    try {
                        writer.write(product.toString());
                        writer.newLine();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to write product to file", e);
                    }
                })
                .then(),
            writer -> {
                try {
                    writer.close();
                } catch (IOException e) {
                    // log or handle
                }
            }
        );
    }
}
