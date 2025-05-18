package jayslabs.section10.mapper;

import jayslabs.section10.dto.ProductDTO;
import jayslabs.section10.entity.Product;

public class EntityDTOMapper {

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
            product.getId(), 
            product.getDescription(), 
            product.getPrice());
    }

    public static Product toEntity(ProductDTO productDTO) {
        return new Product(productDTO.id(),productDTO.description(), productDTO.price());
    }
}
