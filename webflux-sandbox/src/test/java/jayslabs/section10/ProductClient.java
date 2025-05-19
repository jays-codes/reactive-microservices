package jayslabs.section10;

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
}
