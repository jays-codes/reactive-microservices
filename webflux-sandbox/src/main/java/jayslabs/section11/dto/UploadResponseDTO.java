package jayslabs.section11.dto;

import java.util.UUID;

public record UploadResponseDTO(UUID confirmationId, Long productsCount) {

}
