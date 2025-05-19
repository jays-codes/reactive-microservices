package jayslabs.section10.dto;

import java.util.UUID;

public record UploadResponseDTO(UUID confirmationId, Long productsCount) {

}
